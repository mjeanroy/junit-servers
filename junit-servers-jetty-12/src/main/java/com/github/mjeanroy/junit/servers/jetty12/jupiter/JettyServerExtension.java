/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2026 <mickael.jeanroy@gmail.com>
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

package com.github.mjeanroy.junit.servers.jetty12.jupiter;

import com.github.mjeanroy.junit.servers.jetty12.EmbeddedJetty;
import com.github.mjeanroy.junit.servers.jetty12.EmbeddedJettyFactory;
import com.github.mjeanroy.junit.servers.jupiter.JunitServerExtension;
import com.github.mjeanroy.junit.servers.jupiter.JunitServerExtensionLifecycle;
import com.github.mjeanroy.junit.servers.loggers.Logger;
import com.github.mjeanroy.junit.servers.loggers.LoggerFactory;
import com.github.mjeanroy.junit.servers.servers.AbstractConfiguration;
import com.github.mjeanroy.junit.servers.servers.EmbeddedServer;
import com.github.mjeanroy.junit.servers.jetty.EmbeddedJettyConfiguration;

import java.util.Optional;

import static org.junit.platform.commons.support.AnnotationSupport.findAnnotation;

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
	 * Class Logger.
	 */
	private static final Logger log = LoggerFactory.getLogger(JettyServerExtension.class);

	/**
	 * Create the jupiter with default behavior.
	 */
	public JettyServerExtension() {
		super();
	}

	/**
	 * Create the jupiter with default behavior.
	 *
	 * @param lifecycle The extension lifecycle.
	 * @throws NullPointerException If {@code lifecycle} is {@code null}.
	 */
	public JettyServerExtension(JunitServerExtensionLifecycle lifecycle) {
		super(lifecycle);
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
	 * Create the jupiter and specify the embedded jetty instance to use.
	 *
	 * @param lifecycle The extension lifecycle.
	 * @param jetty The embedded jetty instance to use.
	 * @throws NullPointerException If {@code jetty} is {@code null}.
	 * @throws NullPointerException If {@code lifecycle} is {@code null}.
	 */
	public JettyServerExtension(JunitServerExtensionLifecycle lifecycle, EmbeddedJetty jetty) {
		super(lifecycle, jetty);
	}

	/**
	 * Create the jupiter and specify the embedded jetty configuration to use (when using
	 * jupiter with {@link org.junit.jupiter.api.extension.RegisterExtension}).
	 *
	 * @param configuration The embedded jetty configuration to use.
	 * @throws NullPointerException If {@code configuration} is {@code null}.
	 */
	public JettyServerExtension(EmbeddedJettyConfiguration configuration) {
		super(configuration);
	}

	/**
	 * Create the jupiter and specify the embedded jetty configuration to use (when using
	 * jupiter with {@link org.junit.jupiter.api.extension.RegisterExtension}).
	 *
	 * @param lifecycle The extension lifecycle.
	 * @param configuration The embedded jetty configuration to use.
	 * @throws NullPointerException If {@code configuration} is {@code null}.
	 * @throws NullPointerException If {@code lifecycle} is {@code null}.
	 */
	public JettyServerExtension(JunitServerExtensionLifecycle lifecycle, EmbeddedJettyConfiguration configuration) {
		super(lifecycle, configuration);
	}

	@Override
	protected EmbeddedServer<?> instantiateServer(Class<?> testClass, AbstractConfiguration configuration) {
		log.debug("Instantiating embedded jetty for test class: {}", testClass);
		return EmbeddedJettyFactory.createFrom(testClass, configuration);
	}

	@Override
	protected Optional<JunitServerExtensionLifecycle> findLifecycle(Class<?> testClass) {
		return findAnnotation(testClass, JettyTest.class).map(JettyTest::lifecycle);
	}
}
