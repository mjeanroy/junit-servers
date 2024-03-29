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

package com.github.mjeanroy.junit.servers.jetty.junit4;

import com.github.mjeanroy.junit.servers.jetty.EmbeddedJetty;
import com.github.mjeanroy.junit.servers.jetty.EmbeddedJettyConfiguration;
import com.github.mjeanroy.junit.servers.junit4.ServerRule;
import com.github.mjeanroy.junit.servers.loggers.Logger;
import com.github.mjeanroy.junit.servers.loggers.LoggerFactory;

/**
 * Rule that can be used to start and stop embedded jetty server.
 *
 * @deprecated Use {@code junit-servers-jetty-9} instead.
 */
@Deprecated
public class JettyServerJunit4Rule extends ServerRule {

	private static final Logger log = LoggerFactory.getLogger(JettyServerJunit4Rule.class);

	/**
	 * Create rule.
	 *
	 * @param jetty Jetty Embedded Server.
	 */
	public JettyServerJunit4Rule(EmbeddedJetty jetty) {
		super(jetty);
		log.warn("{} is deprecated and will be removed in the next major release, use junit-servers-tomcat-8 instead, see https://mjeanroy.dev/junit-servers", JettyServerJunit4Rule.class);
	}

	/**
	 * Create rule using jetty as embedded server.
	 */
	public JettyServerJunit4Rule() {
		this(new EmbeddedJetty());
		log.warn("{} is deprecated and will be removed in the next major release, use junit-servers-tomcat-8 instead, see https://mjeanroy.dev/junit-servers", JettyServerJunit4Rule.class);
	}

	/**
	 * Create rule.
	 *
	 * @param configuration Jetty Configuration.
	 */
	public JettyServerJunit4Rule(EmbeddedJettyConfiguration configuration) {
		this(new EmbeddedJetty(configuration));
		log.warn("{} is deprecated and will be removed in the next major release, use junit-servers-tomcat-8 instead, see https://mjeanroy.dev/junit-servers", JettyServerJunit4Rule.class);
	}
}
