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

package com.github.mjeanroy.junit.servers.engine;

import com.github.mjeanroy.junit.servers.client.HttpClient;
import com.github.mjeanroy.junit.servers.client.HttpClientConfiguration;
import com.github.mjeanroy.junit.servers.client.HttpClientStrategy;
import com.github.mjeanroy.junit.servers.servers.AbstractConfiguration;
import com.github.mjeanroy.junit.servers.servers.EmbeddedServer;

/**
 * This goal of this class is to provide a way to create {@link HttpClient} instances in a thread-safe way:
 * <ul>
 *   <li>Access to {@link #get()} and {@link #destroy()} are synchronized</li>
 *   <li>Note that the returned {@link HttpClient} by the {@link #get()} should not be used once {@link #destroy()} has been called.</li>
 * </ul>
 */
class HttpClientHolder {

	/**
	 * The server that will be queried by the created {@link HttpClient}.
	 */
	private final EmbeddedServer<? extends AbstractConfiguration> server;

	/**
	 * The HTTP client strategy to use.
	 */
	private final HttpClientStrategy strategy;

	/**
	 * The HTTP client configuration to use.
	 */
	private final HttpClientConfiguration configuration;

	/**
	 * THe {@link HttpClient} that will be returned by the {@link #get()} method and destroyed by the {@link #destroy()} method: the
	 * client will be created during the first call to the {@link #get()} method.
	 */
	private HttpClient client;

	/**
	 * Create the holder.
	 *
	 * @param strategy The HTTP client strategy.
	 * @param configuration The HTTP client configuration.
	 * @param server The embedded server to query.
	 */
	HttpClientHolder(HttpClientStrategy strategy, HttpClientConfiguration configuration, EmbeddedServer<? extends AbstractConfiguration> server) {
		this.strategy = strategy;
		this.configuration = configuration;
		this.server = server;
	}

	/**
	 * Returns the HTTP client:
	 * <ul>
	 *   <li>Create HTTP client on the first call, and returns it.</li>
	 *   <li>If the HTTP client has been destroyed, a new one will be created.</li>
	 * </ul>
	 *
	 * @return A valid (i.e ready to use) HTTP client.
	 */
	synchronized HttpClient get() {
		if (client == null || client.isDestroyed()) {
			client = strategy.build(configuration, server);
		}

		return client;
	}

	/**
	 * Destroy the old HTTP client.
	 */
	synchronized void destroy() {
		if (client != null && !client.isDestroyed()) {
			client.destroy();
		}

		client = null;
	}
}
