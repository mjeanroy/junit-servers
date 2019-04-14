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

package com.github.mjeanroy.junit.servers.jetty.junit4;

import com.github.mjeanroy.junit.servers.jetty.EmbeddedJetty;
import com.github.mjeanroy.junit.servers.jetty.EmbeddedJettyConfiguration;
import com.github.mjeanroy.junit.servers.jetty.exceptions.IllegalJettyConfigurationException;
import com.github.mjeanroy.junit.servers.junit4.JunitServerRunner;
import com.github.mjeanroy.junit.servers.servers.AbstractConfiguration;
import org.junit.runners.model.InitializationError;

import static com.github.mjeanroy.junit.servers.engine.Servers.findConfiguration;

/**
 * Rule that can be used to start and stop embedded jetty server.
 */
public class JettyServerJunit4Runner extends JunitServerRunner {

	/**
	 * Create runner.
	 *
	 * @param klass Running class.
	 * @throws InitializationError If an error occurred while starting embedded server.
	 */
	public JettyServerJunit4Runner(Class<?> klass) throws InitializationError {
		super(klass, instantiate(klass));
	}

	/**
	 * Instantiate embedded jetty to be used in tests.
	 *
	 * @param klass The tested class.
	 * @return The embedded jetty.
	 */
	private static EmbeddedJetty instantiate(Class<?> klass) {
		final EmbeddedJettyConfiguration configuration = extractConfiguration(klass);
		return configuration == null ? new EmbeddedJetty() : new EmbeddedJetty(configuration);
	}

	/**
	 * Try to extract {@link EmbeddedJetty} configuration from the test class: search for a static
	 * field/method annotated with {@link com.github.mjeanroy.junit.servers.annotations.TestServerConfiguration}).
	 *
	 * @param klass The test class.
	 * @return The {@link EmbeddedJetty} configuration.
	 * @throws IllegalJettyConfigurationException If extracted annotation is not an instance of {@link EmbeddedJetty}.
	 */
	private static EmbeddedJettyConfiguration extractConfiguration(Class<?> klass) {
		final AbstractConfiguration configuration = findConfiguration(klass);

		if (configuration == null) {
			return null;
		}

		if (!(configuration instanceof EmbeddedJettyConfiguration)) {
			throw new IllegalJettyConfigurationException();
		}

		return (EmbeddedJettyConfiguration) configuration;
	}
}
