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

package com.github.mjeanroy.junit.servers.client.impl.async;

import com.github.mjeanroy.junit.servers.client.Cookies;
import com.github.mjeanroy.junit.servers.client.HttpHeader;
import com.github.mjeanroy.junit.servers.client.HttpHeaders;
import com.github.mjeanroy.junit.servers.client.HttpMethod;
import com.github.mjeanroy.junit.servers.client.HttpParameter;
import com.github.mjeanroy.junit.servers.client.HttpRequest;
import com.github.mjeanroy.junit.servers.client.HttpResponse;
import com.github.mjeanroy.junit.servers.client.HttpUrl;
import com.github.mjeanroy.junit.servers.client.impl.AbstractHttpRequest;
import com.github.mjeanroy.junit.servers.loggers.Logger;
import com.github.mjeanroy.junit.servers.loggers.LoggerFactory;
import io.netty.handler.codec.http.cookie.Cookie;
import org.asynchttpclient.ListenableFuture;
import org.asynchttpclient.Request;
import org.asynchttpclient.RequestBuilder;
import org.asynchttpclient.Response;
import org.asynchttpclient.uri.Uri;
import org.asynchttpclient.util.Utf8UrlEncoder;

import java.io.IOException;

import static java.lang.System.nanoTime;

/**
 * Implementation for {@link HttpRequest} that use async-http-client
 * under the hood.
 *
 * @see <a href="https://asynchttpclient.github.io/">https://asynchttpclient.github.io/</a>
 * @see com.github.mjeanroy.junit.servers.client.HttpClientStrategy#ASYNC_HTTP_CLIENT
 */
class AsyncHttpRequest extends AbstractHttpRequest {

	/**
	 * Class Logger.
	 */
	private static final Logger log = LoggerFactory.getLogger(AsyncHttpRequest.class);

	/**
	 * Original http client, will be used to execute http request.
	 */
	private final org.asynchttpclient.AsyncHttpClient client;

	/**
	 * Create http request.
	 *
	 * @param client Client used to execute request using async-http-client.
	 * @param httpMethod Http method.
	 * @param url Request URL.
	 */
	AsyncHttpRequest(org.asynchttpclient.AsyncHttpClient client, HttpMethod httpMethod, HttpUrl url) {
		super(url, httpMethod);
		this.client = client;
	}

	@Override
	protected HttpResponse doExecute() throws Exception {
		final HttpUrl endpoint = getEndpoint();
		final String scheme = endpoint.getScheme();
		final String userInfo = null;
		final String host = endpoint.getHost();
		final int port = endpoint.getPort();
		final String path = Utf8UrlEncoder.encodePath(endpoint.getPath());
		final String query = null;
		final String fragment = null;
		final Uri uri = new Uri(scheme, userInfo, host, port, path, query, fragment);

		final String method = getMethod().getVerb();
		final RequestBuilder builder = new RequestBuilder(method, true).setUri(uri);

		handleQueryParameters(builder);
		handleBody(builder);
		handleHeaders(builder);
		handleCookies(builder);

		final Request request = builder.build();
		final ListenableFuture<Response> future = client.executeRequest(request);

		final long start = nanoTime();
		final Response response = future.get();
		final long duration = nanoTime() - start;

		return AsyncHttpResponseFactory.of(response, duration);
	}

	/**
	 * Add query parameter to the final HTTP request.
	 *
	 * @param builder The pending HTTP request.
	 * @see RequestBuilder#addQueryParam(String, String)
	 */
	private void handleQueryParameters(RequestBuilder builder) {
		for (HttpParameter p : queryParams.values()) {
			builder.addQueryParam(p.getEncodedName(), p.getEncodedValue());
		}
	}

	/**
	 * Add body entity to the final HTTP request.
	 *
	 * @param builder The pending HTTP request.
	 * @see RequestBuilder#addFormParam(String, String)
	 * @see RequestBuilder#setBody(String)
	 */
	private void handleBody(RequestBuilder builder) throws IOException {
		if (!hasBody()) {
			log.debug("HTTP Request does not have body, skip.");
			return;
		}

		log.debug("Set body to current request builder using: {}", body);
		builder.setBody(body.getBody());

		if (body.getContentType() != null) {
			builder.setHeader("Content-Type", body.getContentType());
		}
	}

	/**
	 * Add headers to the final HTTP request.
	 *
	 * @param builder The pending HTTP request.
	 * @see RequestBuilder#addHeader(CharSequence, String)
	 */
	private void handleHeaders(RequestBuilder builder) {
		for (HttpHeader header : headers.values()) {
			builder.setHeader(header.getName(), header.getValues());
		}
	}

	/**
	 * Add cookies to the final HTTP request.
	 *
	 * @param builder The pending HTTP request.
	 * @see RequestBuilder#addCookie(Cookie)
	 */
	private void handleCookies(RequestBuilder builder) {
		if (!cookies.isEmpty()) {
			builder.addHeader(HttpHeaders.COOKIE, Cookies.serialize(cookies));
		}
	}
}
