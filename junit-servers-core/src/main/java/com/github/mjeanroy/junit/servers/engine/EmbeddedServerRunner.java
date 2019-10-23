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

package com.github.mjeanroy.junit.servers.engine;

import com.github.mjeanroy.junit.servers.client.HttpClient;
import com.github.mjeanroy.junit.servers.client.HttpClientConfiguration;
import com.github.mjeanroy.junit.servers.client.HttpClientStrategy;
import com.github.mjeanroy.junit.servers.commons.lang.ToStringBuilder;
import com.github.mjeanroy.junit.servers.loggers.Logger;
import com.github.mjeanroy.junit.servers.loggers.LoggerFactory;
import com.github.mjeanroy.junit.servers.servers.AbstractConfiguration;
import com.github.mjeanroy.junit.servers.servers.EmbeddedServer;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.github.mjeanroy.junit.servers.client.HttpClientConfiguration.defaultConfiguration;
import static com.github.mjeanroy.junit.servers.commons.lang.Preconditions.notNull;
import static com.github.mjeanroy.junit.servers.engine.Servers.instantiate;

/**
 * An engine that will handle the embedded server lifecycle:
 *
 * <ol>
 *   <li>Start embedded server before running any tests.</li>
 *   <li>Stop embedded server after all tests are run.</li>
 * </ol>
 */
public final class EmbeddedServerRunner extends AbstractTestRunner implements TestRunner {

	/**
	 * Class Logger.
	 */
	private static final Logger log = LoggerFactory.getLogger(EmbeddedServerRunner.class);

	/**
	 * Embedded server that will be start and stopped.
	 */
	private final EmbeddedServer<?> server;

	/**
	 * The HTTP clients (will be automatically destroyed in the {@code after} step.
	 */
	private final Map<HttpClientId, HttpClient> clients;

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
	 * to specify a custom configuration, use {@link #EmbeddedServerRunner(AbstractConfiguration)} constructor.
	 */
	public EmbeddedServerRunner() {
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
	public EmbeddedServerRunner(AbstractConfiguration configuration) {
		this(instantiate(configuration));
	}

	/**
	 * Create rule with an embedded server.
	 *
	 * @param server Embedded server, not null.
	 * @throws NullPointerException If {@code server} is {@code null}.
	 */
	public EmbeddedServerRunner(EmbeddedServer<?> server) {
		this.server = notNull(server, "server");
		this.clients = new HashMap<>();
	}

	/**
	 * Run the BEFORE step of the lifecycle test.
	 */
	@Override
	public void beforeAll() {
		start();
	}

	/**
	 * Run the AFTER step of the lifecycle test.
	 */
	@Override
	public void afterAll() {
		stop();
	}

	/**
	 * Start embedded server.
	 *
	 * @see EmbeddedServer#start()
	 */
	public void start() {
		log.debug("Starting embedded server");
		server.start();
	}

	/**
	 * Stop embedded server.
	 *
	 * @see EmbeddedServer#stop()
	 */
	public void stop() {
		stopServer();
		closeOpenedClients();
	}

	private void stopServer() {
		log.debug("Stopping embedded server");
		server.stop();
	}

	private void closeOpenedClients() {
		log.debug("Closing embedded server HTTP clients");

		synchronized (clients) {
			for (HttpClient client : clients.values()) {
				if (!client.isDestroyed()) {
					client.destroy();
				}
			}

			clients.clear();
		}
	}

	/**
	 * Restart embedded server.
	 *
	 * @see EmbeddedServer#restart()
	 */
	public void restart() {
		log.debug("Restarting embedded server");
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
		return getClient(defaultConfiguration());
	}

	/**
	 * Returns HTTP client that can be used against {@link #server}.
	 *
	 * @param configuration The client configuration.
	 * @return The HTTP client.
	 * @throws UnsupportedOperationException If the client cannot be returned because of missing implementation.
	 */
	public HttpClient getClient(HttpClientConfiguration configuration) {
		return openClient(HttpClientStrategy.AUTO, configuration);
	}

	/**
	 * Returns HTTP client that can be used against {@link #server}.
	 *
	 * @param strategy The strategy to use.
	 * @return The HTTP client.
	 * @throws UnsupportedOperationException If the client cannot be returned because of missing implementation.
	 */
	public HttpClient getClient(HttpClientStrategy strategy) {
		return getClient(strategy, defaultConfiguration());
	}

	/**
	 * Returns HTTP client that can be used against {@link #server}.
	 *
	 * @param strategy The strategy to use.
	 * @param configuration The client configuration.
	 * @return The HTTP client.
	 * @throws UnsupportedOperationException If the client cannot be returned because of missing implementation.
	 */
	public HttpClient getClient(HttpClientStrategy strategy, HttpClientConfiguration configuration) {
		return openClient(strategy, configuration);
	}

	private HttpClient openClient(HttpClientStrategy strategy, HttpClientConfiguration configuration) {
		log.debug("Opening HTTP client using strategy: {}", strategy);

		notNull(strategy, "strategy");
		notNull(configuration, "configuration");

		synchronized (clients) {
			HttpClientId id = new HttpClientId(strategy, configuration);

			if (!clients.containsKey(id) || clients.get(id).isDestroyed()) {
				HttpClient client = strategy.build(configuration, server);
				clients.put(id, client);
			}

			return clients.get(id);
		}
	}

	@Override
	public String toString() {
		return ToStringBuilder.create(getClass())
			.append("server", server)
			.append("clients", clients)
			.build();
	}

	private static final class HttpClientId {
		private final HttpClientStrategy strategy;
		private final HttpClientConfiguration configuration;

		private HttpClientId(HttpClientStrategy strategy, HttpClientConfiguration configuration) {
			this.strategy = strategy;
			this.configuration = configuration;
		}

		@Override
		public boolean equals(Object o) {
			if (o == this) {
				return true;
			}

			if (o instanceof HttpClientId) {
				HttpClientId id = (HttpClientId) o;
				return Objects.equals(strategy, id.strategy) && Objects.equals(configuration, id.configuration);
			}

			return false;
		}

		@Override
		public int hashCode() {
			return Objects.hash(strategy, configuration);
		}

		@Override
		public String toString() {
			return ToStringBuilder.create(getClass())
				.append("strategy", strategy)
				.append("configuration", configuration)
				.build();
		}
	}
}
