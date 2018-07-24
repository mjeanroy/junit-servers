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

package com.github.mjeanroy.junit.servers.client.impl.ning;

import static java.lang.System.nanoTime;

import com.github.mjeanroy.junit.servers.client.Cookies;
import com.github.mjeanroy.junit.servers.client.HttpHeader;
import com.github.mjeanroy.junit.servers.client.HttpHeaders;
import com.github.mjeanroy.junit.servers.client.HttpMethod;
import com.github.mjeanroy.junit.servers.client.HttpParameter;
import com.github.mjeanroy.junit.servers.client.HttpRequest;
import com.github.mjeanroy.junit.servers.client.HttpResponse;
import com.github.mjeanroy.junit.servers.client.HttpUrl;
import com.github.mjeanroy.junit.servers.client.impl.AbstractHttpRequest;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Request;
import com.ning.http.client.RequestBuilder;
import com.ning.http.client.Response;
import com.ning.http.client.uri.Uri;
import com.ning.http.util.UTF8UrlEncoder;

/**
 * Implementation for {@link HttpRequest} that use (ning) async-http-client
 * under the hood.
 *
 * @see <a href="https://github.com/ning/async-http-client">https://github.com/ning/async-http-client</a>
 * @see com.github.mjeanroy.junit.servers.client.HttpClientStrategy#NING_ASYNC_HTTP_CLIENT
 */
class NingAsyncHttpRequest extends AbstractHttpRequest implements HttpRequest {

	/**
	 * Original http client, will be used to execute http request.
	 */
	private final AsyncHttpClient client;

	/**
	 * Create http request.
	 *
	 * @param client Client used to execute request using async-http-client.
	 * @param httpMethod Http method.
	 * @param url Request URL.
	 */
	NingAsyncHttpRequest(AsyncHttpClient client, HttpMethod httpMethod, HttpUrl url) {
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
		final String path = UTF8UrlEncoder.encodePath(endpoint.getPath());
		final String query = null;
		final Uri uri = new Uri(scheme, userInfo, host, port, path, query);

		final String method = getMethod().getVerb();
		final RequestBuilder builder = new RequestBuilder(method, true).setUri(uri);

		handleQueryParameters(builder);
		handleBody(builder);
		handleHeaders(builder);
		handleCookies(builder);

		final Request request = builder.build();
		final long start = nanoTime();
		final Response response = client.executeRequest(request).get();
		final long duration = nanoTime() - start;

		return NingAsyncHttpResponseFactory.of(response, duration);
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
	 * Add request body:
	 *
	 * <ul>
	 *   <li>Set request body if {@link #body} is defined.</li>
	 *   <li>Add form parameters ({@link #formParams}) otherwise if it is not empty.</li>
	 * </ul>
	 *
	 * @param builder The pending HTTP request.
	 * @see RequestBuilder#addFormParam(String, String)
	 * @see RequestBuilder#setBody(String)
	 */
	private void handleBody(RequestBuilder builder) {
		if (!hasBody()) {
			return;
		}

		if (body != null) {
			handleRequestBody(builder);
		} else {
			handleFormParameters(builder);
		}
	}

	/**
	 * Add form parameters to the final HTTP request.
	 *
	 * @param builder The pending HTTP request.
	 * @see RequestBuilder#addFormParam(String, String)
	 */
	private void handleFormParameters(RequestBuilder builder) {
		for (HttpParameter p : formParams.values()) {
			builder.addFormParam(p.getName(), p.getValue());
		}
	}

	/**
	 * Set request body value.
	 *
	 * @param builder The pending HTTP request.
	 * @see RequestBuilder#setBody(String)
	 */
	private void handleRequestBody(RequestBuilder builder) {
		builder.setBody(body);
	}

	/**
	 * Add cookies to the final HTTP request.
	 *
	 * @param builder The pending HTTP request.
	 * @see RequestBuilder#addCookie(com.ning.http.client.cookie.Cookie)
	 */
	private void handleCookies(RequestBuilder builder) {
		if (!cookies.isEmpty()) {
			builder.addHeader(HttpHeaders.COOKIE, Cookies.serialize(cookies));
		}
	}

	/**
	 * Add request headers.
	 *
	 * @param builder The pending HTTP request.
	 * @see RequestBuilder#addHeader(String, String)
	 */
	private void handleHeaders(RequestBuilder builder) {
		for (HttpHeader header : headers.values()) {
			builder.addHeader(header.getName(), header.serializeValues());
		}
	}
}
