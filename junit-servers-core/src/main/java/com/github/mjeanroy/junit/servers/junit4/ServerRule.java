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

package com.github.mjeanroy.junit.servers.junit4;

import com.github.mjeanroy.junit.servers.client.HttpClient;
import com.github.mjeanroy.junit.servers.engine.EmbeddedServerTestLifeCycleAdapter;
import com.github.mjeanroy.junit.servers.servers.AbstractConfiguration;
import com.github.mjeanroy.junit.servers.servers.EmbeddedServer;

/**
 * Rule that can be used to start and stop embedded server.
 * This rule is automatically used if the {@link JunitServerRunner} is used.
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
 */
public class ServerRule extends AbstractRule {

	/**
	 * The test engine.
	 */
	private final EmbeddedServerTestLifeCycleAdapter adapter;

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
		this.adapter = new EmbeddedServerTestLifeCycleAdapter();
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
		this.adapter = new EmbeddedServerTestLifeCycleAdapter(configuration);
	}

	/**
	 * Create rule with an embedded server.
	 *
	 * @param server Embedded server, not null.
	 * @throws NullPointerException If {@code server} is {@code null}.
	 */
	public ServerRule(EmbeddedServer<?> server) {
		this.adapter = new EmbeddedServerTestLifeCycleAdapter(server);
	}

	@Override
	protected void before() {
		adapter.beforeAll();
	}

	@Override
	protected void after() {
		adapter.afterAll();
	}

	/**
	 * Start embedded server.
	 *
	 * @see EmbeddedServer#start()
	 */
	public void start() {
		adapter.start();
	}

	/**
	 * Stop embedded server.
	 *
	 * @see EmbeddedServer#stop()
	 */
	public void stop() {
		adapter.stop();
	}

	/**
	 * Restart embedded server.
	 *
	 * @see EmbeddedServer#restart()
	 */
	public void restart() {
		adapter.restart();
	}

	/**
	 * Check if embedded server is started.
	 *
	 * @return {@code true} if embedded server is started, {@code false} otherwise.
	 * @see EmbeddedServer#isStarted()
	 */
	public boolean isStarted() {
		return adapter.isStarted();
	}

	/**
	 * Get embedded server scheme (a.k.a {@code "http"} or {@code "https"}).
	 *
	 * @return Scheme.
	 * @see EmbeddedServer#getScheme()
	 */
	public String getScheme() {
		return adapter.getScheme();
	}

	/**
	 * Get embedded server scheme (should be {@code "localhost"}).
	 *
	 * @return Host.
	 * @see EmbeddedServer#getHost()
	 */
	public String getHost() {
		return adapter.getHost();
	}

	/**
	 * Get port used by embedded server.
	 *
	 * <p>
	 *
	 * Note that:
	 * <ul>
	 *   <li>If the server is not started, the returned port is the one set in the configuration.</li>
	 *   <li>Otherwise, the "real" port is returned (the port used by the embedded server)</li>
	 * </ul>
	 *
	 * @return The port.
	 * @see EmbeddedServer#getPort()
	 */
	public int getPort() {
		return adapter.getPort();
	}

	/**
	 * Get path defined on embedded server.
	 *
	 * @return Path.
	 * @see EmbeddedServer#getPath()
	 */
	public String getPath() {
		return adapter.getPath();
	}

	/**
	 * Get url used to query embedded server.
	 *
	 * @return URL.
	 * @see EmbeddedServer#getUrl()
	 */
	public String getUrl() {
		return adapter.getUrl();
	}

	/**
	 * Get embedded server.
	 *
	 * @return Server.
	 */
	public EmbeddedServer<?> getServer() {
		return adapter.getServer();
	}

	/**
	 * Returns HTTP client that can be used against embedded server.
	 *
	 * @return The HTTP client.
	 * @throws UnsupportedOperationException If the client cannot be returned because of missing implementation.
	 */
	public HttpClient getClient() {
		return adapter.getClient();
	}
}
