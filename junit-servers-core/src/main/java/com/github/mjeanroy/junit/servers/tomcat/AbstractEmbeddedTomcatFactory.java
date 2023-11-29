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

package com.github.mjeanroy.junit.servers.tomcat;

import com.github.mjeanroy.junit.servers.loggers.Logger;
import com.github.mjeanroy.junit.servers.loggers.LoggerFactory;

import static com.github.mjeanroy.junit.servers.commons.reflect.Annotations.findAnnotation;
import static com.github.mjeanroy.junit.servers.commons.reflect.Classes.instantiate;
import static com.github.mjeanroy.junit.servers.engine.Servers.findConfiguration;

/**
 * Static factories for Tomcat that can be used in JUnit 4 Runner implementation
 * or JUnit Jupiter Extension.
 *
 * @param <EMBEDDED_TOMCAT> The embedded tomcat implementation.
 */
public abstract class AbstractEmbeddedTomcatFactory<
		EMBEDDED_TOMCAT extends AbstractEmbeddedTomcat<EmbeddedTomcatConfiguration>
> {

	/**
	 * Class Logger.
	 */
	private static final Logger log = LoggerFactory.getLogger(AbstractEmbeddedTomcatFactory.class);

	/**
	 * Create factory.
	 */
	public AbstractEmbeddedTomcatFactory() {
	}

	/**
	 * Instantiate embedded tomcat from given test class.
	 *
	 * @param testClass The test class.
	 * @return Created embedded tomcat instance.
	 */
	public final EMBEDDED_TOMCAT instantiateFrom(Class<?> testClass) {
		return instantiateFrom(testClass, null);
	}

	/**
	 * Instantiate embedded tomcat from given test class, with given provided configuration (may be {@code null}).
	 *
	 * @param testClass The test class.
	 * @param configuration The configuration to use, may be {@code null}.
	 * @return Created embedded tomcat instance.
	 */
	public final EMBEDDED_TOMCAT instantiateFrom(Class<?> testClass, EmbeddedTomcatConfiguration configuration) {
		log.debug("Instantiating embedded tomcat for test class: {}", testClass);
		EmbeddedTomcatConfiguration configurationToUse = extractConfiguration(testClass, configuration);
		return configurationToUse == null ? instantiateFrom() : instantiateFrom(configurationToUse);
	}

	/**
	 * Instantiate embedded Tomcat using default configuration.
	 *
	 * @return Embedded tomcat.
	 */
	protected abstract EMBEDDED_TOMCAT instantiateFrom();

	/**
	 * Instantiate embedded Tomcat using given configuration.
	 *
	 * @param config Tomcat configuration.
	 * @return Embedded tomcat.
	 */
	protected abstract EMBEDDED_TOMCAT instantiateFrom(EmbeddedTomcatConfiguration config);

	private EmbeddedTomcatConfiguration extractConfiguration(Class<?> testClass, EmbeddedTomcatConfiguration configuration) {
		if (configuration != null) {
			log.debug("Returning provided configuration instance: {}", configuration);
			return checkConfiguration(configuration);
		}

		Class<? extends EmbeddedTomcatConfigurationProvider> providerClass = findEmbeddedTomcatConfigurationProvider(testClass);
		if (providerClass != null) {
			return buildEmbeddedTomcatConfiguration(testClass, providerClass);
		}

		log.debug("Extracting configuration from given test class: {}", testClass);
		return checkConfiguration(
				findConfiguration(testClass)
		);
	}

	private Class<? extends EmbeddedTomcatConfigurationProvider> findEmbeddedTomcatConfigurationProvider(Class<?> testClass) {
		TomcatConfiguration configurationAnnotation = findAnnotation(testClass, TomcatConfiguration.class);
		return configurationAnnotation == null ? null : configurationAnnotation.providedBy();
	}

	private EmbeddedTomcatConfiguration buildEmbeddedTomcatConfiguration(
		Class<?> testClass,
		Class<? extends EmbeddedTomcatConfigurationProvider> providerClass
	) {
		log.debug("Returning configuration provided by test class");
		EmbeddedTomcatConfigurationProvider provider = instantiate(providerClass);
		return provider.build(testClass);
	}

	private EmbeddedTomcatConfiguration checkConfiguration(Object configuration) {
		if (configuration == null) {
			return null;
		}

		if (!(configuration instanceof EmbeddedTomcatConfiguration)) {
			log.error("Cannot instantiate embedded tomcat using configuration {} because it does not extends {} class", configuration, EmbeddedTomcatConfiguration.class);
			throw new IllegalTomcatConfigurationException(EmbeddedTomcatConfiguration.class);
		}

		return (EmbeddedTomcatConfiguration) configuration;
	}
}
