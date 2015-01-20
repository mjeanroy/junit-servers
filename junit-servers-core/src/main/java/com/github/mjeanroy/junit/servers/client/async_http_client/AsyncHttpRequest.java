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

package com.github.mjeanroy.junit.servers.client.async_http_client;

import com.github.mjeanroy.junit.servers.client.AbstractHttpRequest;
import com.github.mjeanroy.junit.servers.client.HttpMethod;
import com.github.mjeanroy.junit.servers.client.HttpRequest;
import com.github.mjeanroy.junit.servers.client.HttpResponse;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Request;
import com.ning.http.client.RequestBuilder;
import com.ning.http.client.Response;

import static com.github.mjeanroy.junit.servers.commons.Checks.notBlank;
import static com.github.mjeanroy.junit.servers.commons.Checks.notNull;

/**
 * Implementation for {HttpRequest} that use async-http-client
 * under the hood.
 * See: https://asynchttpclient.github.io/
 */
public class AsyncHttpRequest extends AbstractHttpRequest {

	/**
	 * Original http client.
	 * It will be used to execute http request.
	 */
	private final AsyncHttpClient client;

	/**
	 * Http request builder.
	 * This builder will be used to create request to execute.
	 */
	private final RequestBuilder builder;

	/**
	 * Create http request.
	 *
	 * @param client Client used to execute request using async-http-client.
	 * @param httpMethod Http method.
	 * @param url Request URL.
	 */
	AsyncHttpRequest(AsyncHttpClient client, HttpMethod httpMethod, String url) {
		this.client = client;
		this.builder = new RequestBuilder()
				.setUrl(notBlank(url, "url"))
				.setMethod(notNull(httpMethod, "httpMethod").getVerb());
	}

	@Override
	public HttpRequest addHeader(String name, String value) {
		builder.addHeader(
				notBlank(name, "name"),
				notBlank(value, "value")
		);
		return this;
	}

	@Override
	public HttpRequest addQueryParam(String name, String value) {
		builder.addQueryParam(
				notBlank(name, "name"),
				notNull(value, "value") // Empty values should be allowed
		);
		return this;
	}

	@Override
	protected HttpResponse doExecute() throws Exception {
		Request request = builder.build();
		Response response = client.executeRequest(request).get();
		return new AsyncHttpResponse(response);
	}
}
