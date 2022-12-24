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

package com.github.mjeanroy.junit.servers.jetty.jupiter;

import com.github.mjeanroy.junit.servers.jetty.EmbeddedJetty;
import com.github.mjeanroy.junit.servers.jetty.EmbeddedJettyFactory;
import com.github.mjeanroy.junit.servers.jupiter.JunitServerExtension;
import com.github.mjeanroy.junit.servers.loggers.Logger;
import com.github.mjeanroy.junit.servers.loggers.LoggerFactory;
import com.github.mjeanroy.junit.servers.servers.AbstractConfiguration;
import com.github.mjeanroy.junit.servers.servers.EmbeddedServer;
import com.github.mjeanroy.junit.servers.jetty.EmbeddedJettyConfiguration;

/**
 * A specialized {@link JunitServerExtension} that will instantiate an {@link EmbeddedJetty}
 * server automatically instead of using the Service Provider API.
 *
 * Since this jupiter extends {@link JunitServerExtension}, it has exactly the same features (parameter
 * injections, etc.).
 *
 * @see JunitServerExtension
 *
 * @deprecated Use {@code junit-servers-jetty-9} instead.
 */
@Deprecated
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
		log.warn("{} is deprecated and will be removed in the next major release, use junit-servers-tomcat-8 instead, see https://mjeanroy.dev/junit-servers", JettyServerExtension.class);
	}

	/**
	 * Create the jupiter and specify the embedded jetty instance to use.
	 *
	 * @param jetty The embedded jetty instance to use.
	 * @throws NullPointerException If {@code jetty} is {@code null}.
	 */
	public JettyServerExtension(EmbeddedJetty jetty) {
		super(jetty);
		log.warn("{} is deprecated and will be removed in the next major release, use junit-servers-tomcat-8 instead, see https://mjeanroy.dev/junit-servers", JettyServerExtension.class);
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
		log.warn("{} is deprecated and will be removed in the next major release, use junit-servers-tomcat-8 instead, see https://mjeanroy.dev/junit-servers", JettyServerExtension.class);
	}

	@Override
	protected EmbeddedServer<?> instantiateServer(Class<?> testClass, AbstractConfiguration configuration) {
		log.debug("Instantiating embedded jetty for test class: {}", testClass);
		return EmbeddedJettyFactory.createFrom(testClass, configuration);
	}
}
