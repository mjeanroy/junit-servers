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

package com.github.mjeanroy.junit.servers.jetty.jupiter;

import com.github.mjeanroy.junit.servers.jetty.EmbeddedJetty;
import com.github.mjeanroy.junit.servers.jetty.EmbeddedJettyConfiguration;
import com.github.mjeanroy.junit.servers.jetty.exceptions.IllegalJettyConfigurationException;
import com.github.mjeanroy.junit.servers.jupiter.JunitServerExtension;
import com.github.mjeanroy.junit.servers.servers.AbstractConfiguration;
import com.github.mjeanroy.junit.servers.servers.EmbeddedServer;

import static com.github.mjeanroy.junit.servers.engine.Servers.findConfiguration;

/**
 * A specialized {@link JunitServerExtension} that will instantiate an {@link EmbeddedJetty}
 * server automatically instead of using the Service Provider API.
 *
 * Since this jupiter extends {@link JunitServerExtension}, it has exactly the same features (parameter
 * injections, etc.).
 *
 * @see JunitServerExtension
 */
public class JettyServerExtension extends JunitServerExtension {

	/**
	 * Create the jupiter with default behavior.
	 */
	public JettyServerExtension() {
		super();
	}

	/**
	 * Create the jupiter and specify the embedded jetty instance to use.
	 *
	 * @param jetty The embedded jetty instance to use.
	 * @throws NullPointerException If {@code jetty} is {@code null}.
	 */
	public JettyServerExtension(EmbeddedJetty jetty) {
		super(jetty);
	}

	/**
	 * Create the jupiter and specify the embedded jetty configuration to use (when using
	 * 	 * jupiter with {@link org.junit.jupiter.api.extension.RegisterExtension}).
	 *
	 * @param configuration The embedded jetty configuration to use.
	 * @throws NullPointerException If {@code configuration} is {@code null}.
	 */
	public JettyServerExtension(EmbeddedJettyConfiguration configuration) {
		super(configuration);
	}

	@Override
	protected EmbeddedServer<?> instantiateServer(Class<?> testClass, AbstractConfiguration configuration) {
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
		final AbstractConfiguration configurationToUse = configuration == null ? findConfiguration(testClass) : configuration;

		if (configurationToUse == null) {
			return null;
		}

		if (!(configurationToUse instanceof EmbeddedJettyConfiguration)) {
			throw new IllegalJettyConfigurationException();
		}

		return (EmbeddedJettyConfiguration) configurationToUse;
	}
}
