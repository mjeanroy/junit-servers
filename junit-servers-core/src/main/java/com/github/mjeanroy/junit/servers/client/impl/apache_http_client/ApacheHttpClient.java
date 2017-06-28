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

package com.github.mjeanroy.junit.servers.client.impl.apache_http_client;

import com.github.mjeanroy.junit.servers.client.HttpMethod;
import com.github.mjeanroy.junit.servers.client.HttpRequest;
import com.github.mjeanroy.junit.servers.client.impl.AbstractHttpClient;
import com.github.mjeanroy.junit.servers.exceptions.HttpClientException;
import com.github.mjeanroy.junit.servers.servers.EmbeddedServer;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.github.mjeanroy.junit.servers.commons.Preconditions.notNull;

/**
 * Implementation of http client using apache http client
 * library under the hood.
 * See: http://hc.apache.org/httpcomponents-client-ga/index.html
 */
public class ApacheHttpClient extends AbstractHttpClient {

	/**
	 * Create new http client using internal
	 * http client from apache http-client library.
	 *
	 * @param server Embedded server.
	 * @param client Internal http client
	 * @return Http client.
	 */
	public static ApacheHttpClient newApacheHttpClient(EmbeddedServer server, CloseableHttpClient client) {
		return new ApacheHttpClient(server, client);
	}

	/**
	 * Create new http client using internal
	 * http client from apache http-client library.
	 * An instance of {CloseableHttpClient} will be automatically
	 * created.
	 *
	 * @param server Embedded server.
	 * @return Http client.
	 */
	public static ApacheHttpClient defaultApacheHttpClient(EmbeddedServer server) {
		CloseableHttpClient client = HttpClientBuilder.create().build();
		return new ApacheHttpClient(server, client);
	}

	/**
	 * The {@code close} flag.
	 */
	private final AtomicBoolean destroyed;

	/**
	 * Internal apache http client.
	 * This http client will be closed when http client
	 * is destroyed.
	 */
	private final CloseableHttpClient client;

	// Use static factory
	private ApacheHttpClient(EmbeddedServer server, CloseableHttpClient client) {
		super(server);
		this.client = notNull(client, "client");
		this.destroyed = new AtomicBoolean(false);
	}

	@Override
	protected HttpRequest buildRequest(HttpMethod httpMethod, String url) {
		return new ApacheHttpRequest(client, httpMethod, url);
	}

	@Override
	public void destroy() {
		if (destroyed.compareAndSet(false, true)) {
			try {
				client.close();
			} catch (IOException ex) {
				throw new HttpClientException(ex);
			}
		}
	}

	@Override
	public boolean isDestroyed() {
		return destroyed.get();
	}
}
