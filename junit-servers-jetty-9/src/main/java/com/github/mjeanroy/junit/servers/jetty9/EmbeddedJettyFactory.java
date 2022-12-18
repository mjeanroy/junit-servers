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

package com.github.mjeanroy.junit.servers.jetty9;

import com.github.mjeanroy.junit.servers.servers.AbstractConfiguration;
import com.github.mjeanroy.junit.servers.servers.jetty.AbstractEmbeddedJettyFactory;
import com.github.mjeanroy.junit.servers.servers.jetty.IllegalJettyConfigurationException;

import static com.github.mjeanroy.junit.servers.commons.reflect.Annotations.findAnnotation;

/**
 * Static factories for {@link EmbeddedJetty} that can be used in JUnit 4 Runner implementation
 * or JUnit Jupiter Extension.
 */
public final class EmbeddedJettyFactory extends AbstractEmbeddedJettyFactory<
		EmbeddedJettyConfiguration,
		EmbeddedJettyConfigurationProvider,
		EmbeddedJetty
>{

	private static final EmbeddedJettyFactory INSTANCE = new EmbeddedJettyFactory();

	/**
	 * Instantiate embedded jetty from given test class.
	 *
	 * @param testClass The test class.
	 * @return Created embedded jetty instance.
	 */
	public static EmbeddedJetty createFrom(Class<?> testClass) {
		return INSTANCE.instantiateFrom(testClass, null);
	}

	/**
	 * Instantiate embedded jetty from given test class, with given provided configuration (may be {@code null}).
	 *
	 * @param testClass The test class.
	 * @param configuration The configuration to use, may be {@code null}.
	 * @return Created embedded jetty instance.
	 */
	public static EmbeddedJetty createFrom(Class<?> testClass, AbstractConfiguration configuration) {
		if (configuration == null) {
			return createFrom(testClass);
		}

		if (!(configuration instanceof EmbeddedJettyConfiguration)) {
			throw new IllegalJettyConfigurationException(EmbeddedJettyConfiguration.class);
		}

		return INSTANCE.instantiateFrom(testClass, (EmbeddedJettyConfiguration) configuration);
	}

	// Ensure non instantiation.
	private EmbeddedJettyFactory() {
		super(EmbeddedJettyConfiguration.class);
	}

	@Override
	protected EmbeddedJetty instantiateFrom() {
		return new EmbeddedJetty();
	}

	@Override
	protected EmbeddedJetty instantiateFrom(EmbeddedJettyConfiguration embeddedJettyConfiguration) {
		return new EmbeddedJetty(embeddedJettyConfiguration);
	}

	@Override
	protected Class<? extends EmbeddedJettyConfigurationProvider> findEmbeddedJettyConfigurationProvider(Class<?> testClass) {
		JettyConfiguration configurationAnnotation = findAnnotation(testClass, JettyConfiguration.class);
		return configurationAnnotation == null ? null : configurationAnnotation.providedBy();
	}
}
