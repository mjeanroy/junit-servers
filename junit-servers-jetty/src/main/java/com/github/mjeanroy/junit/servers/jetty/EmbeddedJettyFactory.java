/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2019 <mickael.jeanroy@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.github.mjeanroy.junit.servers.jetty;

import com.github.mjeanroy.junit.servers.jetty.exceptions.IllegalJettyConfigurationException;
import com.github.mjeanroy.junit.servers.loggers.Logger;
import com.github.mjeanroy.junit.servers.loggers.LoggerFactory;
import com.github.mjeanroy.junit.servers.servers.AbstractConfiguration;

import static com.github.mjeanroy.junit.servers.engine.Servers.findConfiguration;

/**
 * Static factories for {@link EmbeddedJetty} that can be used in JUnit 4 Runner implementation
 * or JUnit Jupiter Extension.
 */
public final class EmbeddedJettyFactory extends AbstractConfiguration {

	/**
	 * Class Logger.
	 */
	private static final Logger log = LoggerFactory.getLogger(EmbeddedJettyFactory.class);

	// Ensure non instantiation.
	private EmbeddedJettyFactory() {
	}

	/**
	 * Instantiate embedded jetty from given test class.
	 *
	 * @param testClass The test class.
	 * @return Created embedded jetty instance.
	 */
	public static EmbeddedJetty createFrom(Class<?> testClass) {
		return createFrom(testClass, null);
	}

	/**
	 * Instantiate embedded jetty from given test class, with given provided configuration (may be {@code null}).
	 *
	 * @param testClass The test class.
	 * @param configuration The configuration to use, may be {@code null}.
	 * @return Created embedded jetty instance.
	 */
	public static EmbeddedJetty createFrom(Class<?> testClass, AbstractConfiguration configuration) {
		log.debug("Instantiating embedded jetty for test class: {}", testClass);
		final EmbeddedJettyConfiguration configurationToUse = extractConfiguration(testClass, configuration);
		return configurationToUse == null ? new EmbeddedJetty() : new EmbeddedJetty(configurationToUse);
	}

	/**
	 * Try to extract {@link EmbeddedJetty} configuration from:
	 * <ul>
	 *   <li>The given {@code configuration} if it is not {@code null}.</li>
	 *   <li>A class field/method annotated with {@link com.github.mjeanroy.junit.servers.annotations.TestServerConfiguration} on given {@code testClass} otherwise.</li>
	 * </ul>
	 *
	 * @param testClass The test class to analyze.
	 * @param configuration The configuration to use if not {@code null}.
	 * @return The {@link EmbeddedJetty} configuration.
	 * @throws IllegalJettyConfigurationException If extracted {@code configuration} is not an instance of {@link EmbeddedJettyConfiguration}.
	 */
	private static EmbeddedJettyConfiguration extractConfiguration(Class<?> testClass, AbstractConfiguration configuration) {
		if (configuration != null) {
			log.debug("Returning provided configuration instance: {}", configuration);
			return checkConfiguration(configuration);
		}

		log.debug("Extracting configuration from given test class: {}", testClass);
		return checkConfiguration(
			findConfiguration(testClass)
		);
	}

	/**
	 * Ensure that given {@code configuration} parameter is an instance of {@link EmbeddedJettyConfiguration} and returns it,
	 * or fail with {@link IllegalJettyConfigurationException} otherwise.
	 *
	 * @param configuration The configuration.
	 * @return The configuration.
	 */
	private static EmbeddedJettyConfiguration checkConfiguration(AbstractConfiguration configuration) {
		if (configuration == null) {
			return null;
		}

		if (!(configuration instanceof EmbeddedJettyConfiguration)) {
			log.error("Cannot instantiate embedded jetty using configuration {} because it does not extends EmbeddedJettyConfiguration class", configuration);
			throw new IllegalJettyConfigurationException();
		}

		return (EmbeddedJettyConfiguration) configuration;
	}
}
