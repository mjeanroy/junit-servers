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

package com.github.mjeanroy.junit.servers.client.impl.async_http_client;

import com.github.mjeanroy.junit.servers.client.HttpHeader;
import com.github.mjeanroy.junit.servers.client.impl.AbstractHttpResponse;
import com.github.mjeanroy.junit.servers.exceptions.HttpClientException;
import com.ning.http.client.Response;

import java.io.IOException;
import java.util.List;

import static com.github.mjeanroy.junit.servers.client.HttpHeader.header;
import static com.github.mjeanroy.junit.servers.commons.CollectionUtils.isEmpty;

/**
 * Implementation of {HttpResponse} using async-http-client
 * under the hood.
 * See: https://asynchttpclient.github.io/
 */
public class AsyncHttpResponse extends AbstractHttpResponse {

	/**
	 * Original response from async-http-client library.
	 */
	private final Response response;

	/**
	 * Create http response.
	 *
	 * @param response Original http response from async-http-client.
	 */
	AsyncHttpResponse(Response response) {
		this.response = response;
	}

	@Override
	public int status() {
		return response.getStatusCode();
	}

	@Override
	public String body() {
		try {
			return response.getResponseBody();
		}
		catch (IOException ex) {
			throw new HttpClientException(ex);
		}
	}

	@Override
	public HttpHeader getHeader(String name) {
		List<String> headers = response.getHeaders(name);
		if (isEmpty(headers)) {
			return null;
		}

		return header(name, headers);
	}
}
