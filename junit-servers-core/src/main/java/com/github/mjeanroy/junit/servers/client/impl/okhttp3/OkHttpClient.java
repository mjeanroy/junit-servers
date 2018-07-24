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

package com.github.mjeanroy.junit.servers.client.impl.okhttp3;

import java.util.concurrent.atomic.AtomicBoolean;

import com.github.mjeanroy.junit.servers.client.HttpClient;
import com.github.mjeanroy.junit.servers.client.HttpClientConfiguration;
import com.github.mjeanroy.junit.servers.client.HttpMethod;
import com.github.mjeanroy.junit.servers.client.HttpRequest;
import com.github.mjeanroy.junit.servers.client.HttpUrl;
import com.github.mjeanroy.junit.servers.client.impl.AbstractHttpClient;
import com.github.mjeanroy.junit.servers.servers.EmbeddedServer;

/**
 * Implementation of {@link HttpClient} using OkHttp library from Square.
 *
 * @see com.github.mjeanroy.junit.servers.client.HttpClientStrategy#OK_HTTP3
 * @see <a href="http://square.github.io/okhttp">http://square.github.io/okhttp</a>
 */
public class OkHttpClient extends AbstractHttpClient implements HttpClient {

	/**
	 * Create new http client using internal
	 * http client from ok-http library.
	 *
	 * @param server Embedded server.
	 * @return Http client.
	 * @throws NullPointerException If {@code server} is {@code null}.
	 */
	public static OkHttpClient defaultOkHttpClient(EmbeddedServer<?> server) {
		HttpClientConfiguration configuration = HttpClientConfiguration.defaultConfiguration();
		return newOkHttpClient(configuration, server);
	}

	/**
	 * Create new http client using custom configuration.
	 *
	 * @param configuration Client configuration.
	 * @param server Embedded server.
	 * @return Http client.
	 * @throws NullPointerException If {@code server} or {@code configuration} are {@code null}.
	 */
	public static OkHttpClient newOkHttpClient(HttpClientConfiguration configuration, EmbeddedServer<?> server) {
		okhttp3.OkHttpClient client = new okhttp3.OkHttpClient.Builder()
			.followRedirects(configuration.isFollowRedirect())
			.build();

		return new OkHttpClient(configuration, server, client);
	}

	/**
	 * Create new http client using custom internal
	 * http client from ok-http library.
	 *
	 * @param server Embedded server.
	 * @param client The custom client.
	 * @return Http client.
	 * @throws NullPointerException If {@code server} or {@code client} are {@code null}.
	 * @deprecated Use {@link OkHttpClient#newOkHttpClient(HttpClientConfiguration, EmbeddedServer)}
	 */
	@Deprecated
	public static OkHttpClient newOkHttpClient(EmbeddedServer<?> server, okhttp3.OkHttpClient client) {
		return new OkHttpClient(HttpClientConfiguration.defaultConfiguration(), server, client);
	}

	/**
	 * Flag to ensure that the http client has been destroyed or not.
	 */
	private final AtomicBoolean destroyed;

	/**
	 * The native OkHttp client.
	 */
	private final okhttp3.OkHttpClient client;

	/**
	 * Create the client.
	 * @param server The embedded server that will be queried.
	 * @param client The internal client.
	 */
	private OkHttpClient(HttpClientConfiguration configuration, EmbeddedServer<?> server, okhttp3.OkHttpClient client) {
		super(configuration, server);
		this.client = client;
		this.destroyed = new AtomicBoolean(false);
	}

	@Override
	protected HttpRequest buildRequest(HttpMethod httpMethod, HttpUrl endpoint) {
		return new OkHttpRequest(this.client, httpMethod, endpoint);
	}

	@Override
	public void destroy() {
		if (destroyed.compareAndSet(false, true)) {
			client.dispatcher().executorService().shutdown();
			client.connectionPool().evictAll();
		}
	}

	@Override
	public boolean isDestroyed() {
		return destroyed.get();
	}
}
