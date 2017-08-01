/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2017 <mickael.jeanroy@gmail.com>
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

import static com.github.mjeanroy.junit.servers.commons.Preconditions.notNull;
import static com.github.mjeanroy.junit.servers.servers.utils.Servers.instantiate;

import com.github.mjeanroy.junit.servers.client.HttpClient;
import com.github.mjeanroy.junit.servers.client.HttpClientConfiguration;
import com.github.mjeanroy.junit.servers.client.HttpClientStrategy;
import com.github.mjeanroy.junit.servers.servers.EmbeddedServer;
import com.github.mjeanroy.junit.servers.servers.configuration.AbstractConfiguration;

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
 */
public class ServerRule extends AbstractRule {

	/**
	 * Embedded server that will be start and stopped.
	 */
	private final EmbeddedServer<?> server;

	/**
	 * The HTTP client (will be automatically destroyed in the {@code after} step.
	 */
	private final HttpClientHolder client;

	/**
	 * Create rule with default embedded server.
	 *
	 * <p>
	 *
	 * Embedded server implementation is chosen using
	 * classpath detection: jetty or tomcat will be instantiate
	 * if implementation is available on classpath (it means if
	 * sub-module is imported, it should be enough to instantiate
	 * embedded server).
	 *
	 * <p>
	 *
	 * The server will automatically use the default configuration,
	 * to specify a custom configuration, use {@link #ServerRule(AbstractConfiguration)} constructor.
	 */
	public ServerRule() {
		this((AbstractConfiguration) null);
	}

	/**
	 * Create rule with embedded server configuration.
	 *
	 * <p>
	 *
	 * Embedded server implementation is chosen using
	 * classpath detection: jetty or tomcat will be instantiate
	 * if implementation is available on classpath (it means if
	 * sub-module is imported, it should be enough to instantiate
	 * embedded server !).
	 *
	 * @param configuration Server configuration.
	 */
	public ServerRule(AbstractConfiguration configuration) {
		this(instantiate(configuration));
	}

	/**
	 * Create rule with an embedded server.
	 *
	 * @param server Embedded server, not null.
	 * @throws NullPointerException If {@code server} is {@code null}.
	 */
	public ServerRule(EmbeddedServer<?> server) {
		notNull(server, "server");

		this.server = server;
		this.client = new HttpClientHolder(HttpClientStrategy.AUTO, HttpClientConfiguration.defaultConfiguration(), server);
	}

	@Override
	protected void before() {
		start();
	}

	@Override
	protected void after() {
		stop();
	}

	/**
	 * Start embedded server.
	 *
	 * @see EmbeddedServer#start()
	 */
	public void start() {
		server.start();
	}

	/**
	 * Stop embedded server.
	 *
	 * @see EmbeddedServer#stop()
	 */
	public void stop() {
		server.stop();
		client.destroy();
	}

	/**
	 * Restart embedded server.
	 *
	 * @see EmbeddedServer#restart()
	 */
	public void restart() {
		server.restart();
	}

	/**
	 * Check if embedded server is started.
	 *
	 * @return {@code true} if embedded server is started, {@code false} otherwise.
	 * @see EmbeddedServer#isStarted()
	 */
	public boolean isStarted() {
		return server.isStarted();
	}

	/**
	 * Get embedded server scheme (a.k.a {@code "http"} or {@code "https"}).
	 *
	 * @return Scheme.
	 * @see EmbeddedServer#getScheme()
	 */
	public String getScheme() {
		return server.getScheme();
	}

	/**
	 * Get embedded server scheme (should be {@code "localhost"}).
	 *
	 * @return Host.
	 * @see EmbeddedServer#getHost()
	 */
	public String getHost() {
		return server.getHost();
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
		return server.getPort();
	}

	/**
	 * Get path defined on embedded server.
	 *
	 * @return Path.
	 * @see EmbeddedServer#getPath()
	 */
	public String getPath() {
		return server.getPath();
	}

	/**
	 * Get url used to query embedded server.
	 *
	 * @return URL.
	 * @see EmbeddedServer#getUrl()
	 */
	public String getUrl() {
		return server.getUrl();
	}

	/**
	 * Get embedded server.
	 *
	 * @return Server.
	 */
	public EmbeddedServer<?> getServer() {
		return server;
	}

	/**
	 * Returns HTTP client that can be used against {@link #server}.
	 *
	 * @return The HTTP client.
	 * @throws UnsupportedOperationException If the client cannot be returned because of missing implementation.
	 */
	public HttpClient getClient() {
		return client.get();
	}
}
