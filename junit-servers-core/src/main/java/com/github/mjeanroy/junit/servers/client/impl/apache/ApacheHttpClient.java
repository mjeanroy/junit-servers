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

package com.github.mjeanroy.junit.servers.client.impl.apache;

import com.github.mjeanroy.junit.servers.client.HttpClient;
import com.github.mjeanroy.junit.servers.client.HttpClientConfiguration;
import com.github.mjeanroy.junit.servers.client.HttpMethod;
import com.github.mjeanroy.junit.servers.client.HttpRequest;
import com.github.mjeanroy.junit.servers.client.HttpUrl;
import com.github.mjeanroy.junit.servers.client.impl.AbstractHttpClient;
import com.github.mjeanroy.junit.servers.commons.lang.ToStringBuilder;
import com.github.mjeanroy.junit.servers.servers.EmbeddedServer;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.util.concurrent.atomic.AtomicBoolean;

import static com.github.mjeanroy.junit.servers.commons.lang.Preconditions.notNull;

/**
 * Implementation of {@link HttpClient} using apache http client
 * library under the hood.
 *
 * @see <a href="http://hc.apache.org/httpcomponents-client-ga/index.html">http://hc.apache.org/httpcomponents-client-ga/index.html</a>
 * @see com.github.mjeanroy.junit.servers.client.HttpClientStrategy#APACHE_HTTP_CLIENT
 */
public class ApacheHttpClient extends AbstractHttpClient {

	/**
	 * Create new http client using default internal client.
	 *
	 * @param server Embedded server.
	 * @return Http client.
	 * @throws NullPointerException If {@code server} is {@code null}.
	 */
	public static ApacheHttpClient defaultApacheHttpClient(EmbeddedServer<?> server) {
		HttpClientConfiguration configuration = HttpClientConfiguration.defaultConfiguration();
		return newApacheHttpClient(configuration, server);
	}

	/**
	 * Create new http client using custom configuration.
	 *
	 * @param configuration Client configuration.
	 * @param server Embedded server.
	 * @return Http client.
	 * @throws NullPointerException If {@code server} or {@code configuration} are {@code null}.
	 */
	public static ApacheHttpClient newApacheHttpClient(HttpClientConfiguration configuration, EmbeddedServer<?> server) {
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		if (!configuration.isFollowRedirect()) {
			httpClientBuilder.disableRedirectHandling();
		}

		CloseableHttpClient client = httpClientBuilder.build();
		return new ApacheHttpClient(configuration, server, client);
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
	private ApacheHttpClient(HttpClientConfiguration configuration, EmbeddedServer<?> server, CloseableHttpClient client) {
		super(configuration, server);
		this.client = notNull(client, "client");
		this.destroyed = new AtomicBoolean(false);
	}

	@Override
	protected HttpRequest buildRequest(HttpMethod httpMethod, HttpUrl endpoint) {
		return new ApacheHttpRequest(client, httpMethod, endpoint);
	}

	@Override
	public void doDestroy() throws Exception {
		if (destroyed.compareAndSet(false, true)) {
			client.close();
		}
	}

	@Override
	public boolean isDestroyed() {
		return destroyed.get();
	}

	@Override
	public String toString() {
		return ToStringBuilder.create(getClass())
			.append("configuration", getConfiguration())
			.append("server", getServer())
			.append("client", client)
			.append("destroyed", destroyed)
			.build();
	}
}
