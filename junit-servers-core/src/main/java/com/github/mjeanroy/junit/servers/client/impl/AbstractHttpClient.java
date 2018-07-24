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

package com.github.mjeanroy.junit.servers.client.impl;

import static com.github.mjeanroy.junit.servers.commons.Preconditions.notNull;
import static com.github.mjeanroy.junit.servers.commons.Strings.removePrefix;
import static com.github.mjeanroy.junit.servers.commons.UrlUtils.concatenatePath;
import static com.github.mjeanroy.junit.servers.commons.UrlUtils.startsWithHttpScheme;

import com.github.mjeanroy.junit.servers.client.Cookie;
import com.github.mjeanroy.junit.servers.client.HttpClient;
import com.github.mjeanroy.junit.servers.client.HttpClientConfiguration;
import com.github.mjeanroy.junit.servers.client.HttpHeader;
import com.github.mjeanroy.junit.servers.client.HttpMethod;
import com.github.mjeanroy.junit.servers.client.HttpRequest;
import com.github.mjeanroy.junit.servers.client.HttpUrl;
import com.github.mjeanroy.junit.servers.servers.EmbeddedServer;

/**
 * Abstract skeleton of {@link HttpClient} interface.
 *
 * <p>
 *
 * <strong>This abstract class is not part of the public API and should not be used publicly.</strong>
 */
public abstract class AbstractHttpClient implements HttpClient {

	/**
	 * The client configuration.
	 */
	private final HttpClientConfiguration configuration;

	/**
	 * Embedded server to query.
	 */
	private final EmbeddedServer<?> server;

	/**
	 * Create abstract skeleton.
	 *
	 * @param configuration The HTTP client configuration.
	 * @param server Server.
	 * @throws NullPointerException if server is null.
	 */
	protected AbstractHttpClient(HttpClientConfiguration configuration, EmbeddedServer<?> server) {
		this.configuration = notNull(configuration, "configuration");
		this.server = notNull(server, "server");
	}

	@Override
	public HttpClientConfiguration getConfiguration() {
		return configuration;
	}

	@Override
	public HttpRequest prepareDelete(String endpoint) {
		return prepareRequest(HttpMethod.DELETE, endpoint);
	}

	@Override
	public HttpRequest prepareGet(String endpoint) {
		return prepareRequest(HttpMethod.GET, endpoint);
	}

	@Override
	public HttpRequest preparePost(String endpoint) {
		return prepareRequest(HttpMethod.POST, endpoint);
	}

	@Override
	public HttpRequest preparePut(String url) {
		return prepareRequest(HttpMethod.PUT, url);
	}

	@Override
	public HttpRequest preparePatch(String endpoint) {
		return prepareRequest(HttpMethod.PATCH, endpoint);
	}

	@Override
	public HttpRequest prepareHead(String endpoint) {
		return prepareRequest(HttpMethod.HEAD, endpoint);
	}

	@Override
	public HttpRequest prepareRequest(HttpMethod httpMethod, String endpoint) {
		notNull(endpoint, "endpoint");

		if (isDestroyed()) {
			throw new IllegalStateException("Cannot create request from a destroyed client");
		}

		final HttpUrl requestEndpoint;

		if (startsWithHttpScheme(endpoint)) {
			requestEndpoint = HttpUrl.parse(endpoint);
		} else {
			String serverPath = server.getPath();
			requestEndpoint = new HttpUrl.Builder()
				.withScheme(server.getScheme())
				.withHost(server.getHost())
				.withPort(server.getPort())
				.withPath(concatenatePath(serverPath, removePrefix(endpoint, serverPath)))
				.build();
		}

		HttpRequest rq = buildRequest(httpMethod, requestEndpoint);

		// Add default headers.
		for (HttpHeader header : configuration.getDefaultHeaders().values()) {
			rq = rq.addHeader(header);
		}

		// Add default cookies.
		for (Cookie cookie : configuration.getDefaultCookies()) {
			rq = rq.addCookie(cookie);
		}

		return rq;
	}

	/**
	 * Build request object.
	 *
	 * @param httpMethod Http method.
	 * @param endpoint Request url.
	 * @return Http request.
	 */
	protected abstract HttpRequest buildRequest(HttpMethod httpMethod, HttpUrl endpoint);

	// Ensure that the client is properly destroyed when garbage collected.
	// This is a just a simple "security" if the caller forget to destroy the client: the caller should always destroy
	// the client once used.
	@Override
	protected void finalize() throws Throwable {
		super.finalize();

		// Destroy it.
		if (!isDestroyed()) {
			destroy();
		}
	}
}
