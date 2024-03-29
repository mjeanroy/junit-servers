/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2022 <mickael.jeanroy@gmail.com>
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

import com.github.mjeanroy.junit.servers.loggers.Logger;
import com.github.mjeanroy.junit.servers.loggers.LoggerFactory;

import static com.github.mjeanroy.junit.servers.commons.reflect.Annotations.findAnnotation;
import static com.github.mjeanroy.junit.servers.commons.reflect.Classes.instantiate;
import static com.github.mjeanroy.junit.servers.engine.Servers.findConfiguration;

/**
 * Static factories for {@link AbstractEmbeddedJetty} that can be used in JUnit 4 Runner implementation
 * or JUnit Jupiter Extension.
 *
 * @param <EMBEDDED_JETTY> The embedded jetty implementation.
 */
public abstract class AbstractEmbeddedJettyFactory<
	EMBEDDED_JETTY extends AbstractBaseEmbeddedJetty<?, EmbeddedJettyConfiguration>
> {

	/**
	 * Class Logger.
	 */
	private static final Logger log = LoggerFactory.getLogger(AbstractEmbeddedJettyFactory.class);

	/**
	 * Create factory.
	 */
	public AbstractEmbeddedJettyFactory() {
	}

	/**
	 * Instantiate embedded jetty from given test class.
	 *
	 * @param testClass The test class.
	 * @return Created embedded jetty instance.
	 */
	public final EMBEDDED_JETTY instantiateFrom(Class<?> testClass) {
		return instantiateFrom(testClass, null);
	}

	/**
	 * Instantiate embedded jetty from given test class, with given provided configuration (may be {@code null}).
	 *
	 * @param testClass The test class.
	 * @param configuration The configuration to use, may be {@code null}.
	 * @return Created embedded jetty instance.
	 */
	public final EMBEDDED_JETTY instantiateFrom(Class<?> testClass, EmbeddedJettyConfiguration configuration) {
		log.debug("Instantiating embedded jetty for test class: {}", testClass);
		EmbeddedJettyConfiguration configurationToUse = extractConfiguration(testClass, configuration);
		return configurationToUse == null ? instantiateFrom() : instantiateFrom(configurationToUse);
	}

	/**
	 * Instantiate embedded Jetty using default configuration.
	 *
	 * @return Embedded jetty.
	 */
	protected abstract EMBEDDED_JETTY instantiateFrom();

	/**
	 * Instantiate embedded Jetty using given configuration.
	 *
	 * @param config Jetty configuration.
	 * @return Embedded jetty.
	 */
	protected abstract EMBEDDED_JETTY instantiateFrom(EmbeddedJettyConfiguration config);

	private EmbeddedJettyConfiguration extractConfiguration(Class<?> testClass, EmbeddedJettyConfiguration configuration) {
		if (configuration != null) {
			log.debug("Returning provided configuration instance: {}", configuration);
			return checkConfiguration(configuration);
		}

		Class<? extends EmbeddedJettyConfigurationProvider> providerClass = findEmbeddedJettyConfigurationProvider(testClass);
		if (providerClass != null) {
			return buildEmbeddedJettyConfiguration(testClass, providerClass);
		}

		log.debug("Extracting configuration from given test class: {}", testClass);
		return checkConfiguration(
				findConfiguration(testClass)
		);
	}

	private Class<? extends EmbeddedJettyConfigurationProvider> findEmbeddedJettyConfigurationProvider(Class<?> testClass) {
		JettyConfiguration configurationAnnotation = findAnnotation(testClass, JettyConfiguration.class);
		return configurationAnnotation == null ? null : configurationAnnotation.providedBy();
	}

	private EmbeddedJettyConfiguration buildEmbeddedJettyConfiguration(
		Class<?> testClass,
		Class<? extends EmbeddedJettyConfigurationProvider> providerClass
	) {
		log.debug("Returning configuration provided by test class");
		EmbeddedJettyConfigurationProvider provider = instantiate(providerClass);
		return provider.build(testClass);
	}

	private EmbeddedJettyConfiguration checkConfiguration(Object configuration) {
		if (configuration == null) {
			return null;
		}

		if (!(configuration instanceof EmbeddedJettyConfiguration)) {
			log.error("Cannot instantiate embedded jetty using configuration {} because it does not extends {} class", configuration, EmbeddedJettyConfiguration.class);
			throw new IllegalJettyConfigurationException(EmbeddedJettyConfiguration.class);
		}

		return (EmbeddedJettyConfiguration) configuration;
	}
}
