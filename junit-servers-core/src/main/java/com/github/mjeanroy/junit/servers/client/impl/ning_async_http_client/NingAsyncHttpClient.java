/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 <mickael.jeanroy@gmail.com>
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

package com.github.mjeanroy.junit.servers.client.impl.ning_async_http_client;

import com.github.mjeanroy.junit.servers.client.impl.AbstractHttpClient;
import com.github.mjeanroy.junit.servers.client.HttpMethod;
import com.github.mjeanroy.junit.servers.client.HttpRequest;
import com.github.mjeanroy.junit.servers.servers.EmbeddedServer;

import static com.github.mjeanroy.junit.servers.commons.Preconditions.notNull;

/**
 * Http client implementation using async-http-client
 * under the hood.
 * See: https://asynchttpclient.github.io/
 */
public class NingAsyncHttpClient extends AbstractHttpClient {

	/**
	 * Create new http client using internal
	 * http client from async-http-client library.
	 *
	 * @param server Embedded server.
	 * @param client Internal http client
	 * @return Http client.
	 */
	public static NingAsyncHttpClient newAsyncHttpClient(EmbeddedServer server, com.ning.http.client.AsyncHttpClient client) {
		return new NingAsyncHttpClient(server, client);
	}

	/**
	 * Create new http client using internal
	 * http client from async-http-client library.
	 * An instance of {com.ning.http.client.NingAsyncHttpClient} will be automatically
	 * created.
	 *
	 * @param server Embedded server.
	 * @return Http client.
	 */
	public static NingAsyncHttpClient defaultAsyncHttpClient(EmbeddedServer server) {
		return new NingAsyncHttpClient(server, new com.ning.http.client.AsyncHttpClient());
	}

	/**
	 * Original http client.
	 * This client will be used under the hood.
	 */
	private final com.ning.http.client.AsyncHttpClient client;

	// Use static factory
	private NingAsyncHttpClient(EmbeddedServer server, com.ning.http.client.AsyncHttpClient client) {
		super(server);
		this.client = notNull(client, "client");
	}

	@Override
	protected HttpRequest buildRequest(HttpMethod httpMethod, String url) {
		return new NingAsyncHttpRequest(client, httpMethod, url);
	}

	@Override
	public void destroy() {
		client.close();
	}

	@Override
	public boolean isDestroyed() {
		return client.isClosed();
	}
}
