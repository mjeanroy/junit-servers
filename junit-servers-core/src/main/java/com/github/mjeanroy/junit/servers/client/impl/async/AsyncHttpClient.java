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

package com.github.mjeanroy.junit.servers.client.impl.async;

import static com.github.mjeanroy.junit.servers.commons.Preconditions.notNull;

import java.io.IOException;

import org.asynchttpclient.AsyncHttpClientConfig;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClientConfig;

import com.github.mjeanroy.junit.servers.client.HttpClient;
import com.github.mjeanroy.junit.servers.client.HttpClientConfiguration;
import com.github.mjeanroy.junit.servers.client.HttpMethod;
import com.github.mjeanroy.junit.servers.client.HttpRequest;
import com.github.mjeanroy.junit.servers.client.HttpUrl;
import com.github.mjeanroy.junit.servers.client.impl.AbstractHttpClient;
import com.github.mjeanroy.junit.servers.exceptions.HttpClientException;
import com.github.mjeanroy.junit.servers.servers.EmbeddedServer;

/**
 * Implementation of {@link HttpClient} using async-http-client
 * under the hood.
 *
 * @see <a href="https://asynchttpclient.github.io/">https://asynchttpclient.github.io/</a>
 * @see com.github.mjeanroy.junit.servers.client.HttpClientStrategy#ASYNC_HTTP_CLIENT
 */
public class AsyncHttpClient extends AbstractHttpClient implements HttpClient {

	/**
	 * Create new http client using custom internal http client.
	 *
	 * @param server Embedded server.
	 * @param client Internal http client
	 * @return Http client.
	 * @throws NullPointerException If {@code server} or {@code client} are {@code null}.
	 * @deprecated Use {@link AsyncHttpClient#newAsyncHttpClient(HttpClientConfiguration, EmbeddedServer)}
	 */
	@Deprecated
	public static AsyncHttpClient newAsyncHttpClient(EmbeddedServer<?> server, org.asynchttpclient.AsyncHttpClient client) {
		return new AsyncHttpClient(HttpClientConfiguration.defaultConfiguration(), server, client);
	}

	/**
	 * Create new http client using default internal http client.
	 *
	 * @param server Embedded server.
	 * @return Http client.
	 * @throws NullPointerException If {@code server} is {@code null}.
	 */
	public static AsyncHttpClient defaultAsyncHttpClient(EmbeddedServer<?> server) {
		HttpClientConfiguration configuration = HttpClientConfiguration.defaultConfiguration();
		return newAsyncHttpClient(configuration, server);
	}

	/**
	 * Create new http client using custom configuration.
	 *
	 * @param configuration Client configuration.
	 * @param server Embedded server.
	 * @return Http client.
	 * @throws NullPointerException If {@code server} or {@code configuration} are {@code null}.
	 */
	public static AsyncHttpClient newAsyncHttpClient(HttpClientConfiguration configuration, EmbeddedServer<?> server) {
		AsyncHttpClientConfig config = new DefaultAsyncHttpClientConfig.Builder()
			.setFollowRedirect(configuration.isFollowRedirect())
			.build();

		DefaultAsyncHttpClient client = new DefaultAsyncHttpClient(config);
		return new AsyncHttpClient(configuration, server, client);
	}

	/**
	 * Original http client.
	 */
	private final org.asynchttpclient.AsyncHttpClient client;

	// Use static factory
	private AsyncHttpClient(HttpClientConfiguration configuration, EmbeddedServer<?> server, org.asynchttpclient.AsyncHttpClient client) {
		super(configuration, server);
		this.client = notNull(client, "client");
	}

	@Override
	protected HttpRequest buildRequest(HttpMethod httpMethod, HttpUrl endpoint) {
		return new AsyncHttpRequest(client, httpMethod, endpoint);
	}

	@Override
	public void destroy() {
		try {
			client.close();
		} catch (IOException ex) {
			throw new HttpClientException(ex);
		}
	}

	@Override
	public boolean isDestroyed() {
		return client.isClosed();
	}
}
