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

import com.github.mjeanroy.junit.servers.servers.EmbeddedServerProvider;
import com.github.mjeanroy.junit.servers.jetty.EmbeddedJettyConfiguration;

/**
 * Jetty Embedded Server provider, used by the service provider interface from the JDK.
 */
public class EmbeddedJettyProvider implements EmbeddedServerProvider<EmbeddedJettyConfiguration> {

	/**
	 * Create provider.
	 */
	public EmbeddedJettyProvider() {
	}

	@Override
	public EmbeddedJetty instantiate() {
		return new EmbeddedJetty();
	}

	@Override
	public EmbeddedJetty instantiate(EmbeddedJettyConfiguration configuration) {
		return new EmbeddedJetty(configuration);
	}
}
