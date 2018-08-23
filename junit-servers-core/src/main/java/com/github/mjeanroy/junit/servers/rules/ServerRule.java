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

package com.github.mjeanroy.junit.servers.rules;

import com.github.mjeanroy.junit.servers.servers.AbstractConfiguration;
import com.github.mjeanroy.junit.servers.servers.EmbeddedServer;

/**
 * Rule that can be used to start and stop embedded server.
 * This rule is automatically used if the {@link com.github.mjeanroy.junit.servers.runner.JunitServerRunner} is used.
 *
 * <p>
 *
 * This rule should be used as a {@link org.junit.ClassRule} to start embedded container before all tests and shutdown
 * after all tests. Here is an example automatic classpath detection:
 *
 * <pre><code>
 *   public class Test {
 *     &#064;ClassRule
 *     public static serverRule = ServerRule();
 *
 *     &#064;Test
 *     public void it_should_have_a_port() {
 *       assertTrue(serverRule.getPort() &gt; 0).
 *     }
 *   }
 * </code></pre>
 *
 * @deprecated Use {@link com.github.mjeanroy.junit.servers.junit4.ServerRule} instead.
 */
@Deprecated
public class ServerRule extends com.github.mjeanroy.junit.servers.junit4.ServerRule {

	/**
	 * Create rule with default embedded server.
	 *
	 * <p>
	 *
	 * Embedded server implementation is chosen using
	 * the service provider interface from the JDK.
	 *
	 * <p>
	 *
	 * The server will automatically use the default configuration,
	 * to specify a custom configuration, use {@link #ServerRule(AbstractConfiguration)} constructor.
	 */
	public ServerRule() {
		super();
	}

	/**
	 * Create rule with embedded server configuration.
	 *
	 * <p>
	 *
	 * Embedded server implementation is chosen using
	 * the service provider interface from the JDK.
	 *
	 * @param configuration Server configuration.
	 */
	public ServerRule(AbstractConfiguration configuration) {
		super(configuration);
	}

	/**
	 * Create rule with an embedded server.
	 *
	 * @param server Embedded server, not null.
	 * @throws NullPointerException If {@code server} is {@code null}.
	 */
	public ServerRule(EmbeddedServer<?> server) {
		super(server);
	}
}
