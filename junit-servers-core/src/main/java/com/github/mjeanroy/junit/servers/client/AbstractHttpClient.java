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

package com.github.mjeanroy.junit.servers.client;

import com.github.mjeanroy.junit.servers.servers.EmbeddedServer;

import static com.github.mjeanroy.junit.servers.commons.Checks.notNull;
import static com.github.mjeanroy.junit.servers.commons.Strings.removePrefix;

/**
 * Abstract skeleton of {HttpClient} interface.
 */
public abstract class AbstractHttpClient implements HttpClient {

	/**
	 * Embedded server to query.
	 */
	private final EmbeddedServer server;

	/**
	 * Create abstract skeleton.
	 *
	 * @param server Server.
	 * @throws NullPointerException if server is null.
	 */
	protected AbstractHttpClient(EmbeddedServer server) {
		this.server = notNull(server, "server");
	}

	@Override
	public HttpRequest prepareDelete(String url) {
		return prepareRequest(HttpMethod.DELETE, url);
	}

	@Override
	public HttpRequest prepareGet(String url) {
		return prepareRequest(HttpMethod.GET, url);
	}

	@Override
	public HttpRequest preparePost(String url) {
		return prepareRequest(HttpMethod.POST, url);
	}

	@Override
	public HttpRequest preparePut(String url) {
		return prepareRequest(HttpMethod.PUT, url);
	}

	@Override
	public HttpRequest prepareRequest(HttpMethod httpMethod, String url) {
		notNull(url, "url");

		url = removePrefix(url, server.getPath());
		url = removePrefix(url, server.getUrl());
		return buildRequest(httpMethod, server.getUrl() + url);
	}

	/**
	 * Build request object.
	 *
	 * @param httpMethod Http method.
	 * @param url Request url.
	 * @return Http request.
	 */
	protected abstract HttpRequest buildRequest(HttpMethod httpMethod, String url);
}
