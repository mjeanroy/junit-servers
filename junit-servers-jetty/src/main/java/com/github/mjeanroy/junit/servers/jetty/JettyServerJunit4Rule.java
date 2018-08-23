/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2018 <mickael.jeanroy@gmail.com>
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

/**
 * Rule that can be used to start and stop embedded jetty server.
 *
 * @deprecated Use {@link com.github.mjeanroy.junit.servers.jetty.junit4.JettyServerJunit4Rule} instead.
 */
@Deprecated
public class JettyServerJunit4Rule extends com.github.mjeanroy.junit.servers.jetty.junit4.JettyServerJunit4Rule {
	/**
	 * Create rule.
	 *
	 * @param jetty Jetty Embedded Server.
	 */
	public JettyServerJunit4Rule(EmbeddedJetty jetty) {
		super(jetty);
	}

	/**
	 * Create rule using jetty as embedded server.
	 */
	public JettyServerJunit4Rule() {
		super();
	}

	/**
	 * Create rule.
	 *
	 * @param configuration Jetty Configuration.
	 */
	public JettyServerJunit4Rule(EmbeddedJettyConfiguration configuration) {
		super(configuration);
	}
}
