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

package com.github.mjeanroy.junit.servers.client.impl.okhttp;

import com.github.mjeanroy.junit.servers.client.HttpHeader;
import com.github.mjeanroy.junit.servers.client.HttpResponse;
import com.github.mjeanroy.junit.servers.client.impl.AbstractHttpResponse;
import com.github.mjeanroy.junit.servers.exceptions.HttpClientException;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.util.List;

import static com.github.mjeanroy.junit.servers.client.HttpHeader.header;

/**
 * Implementation of {@link HttpResponse} using OkHttp library.
 */
class OkHttpResponse extends AbstractHttpResponse implements HttpResponse {

	/**
	 * The native OkHttp response.
	 */
	private final Response response;

	/**
	 * The request duration.
	 */
	private final long duration;

	/**
	 * Create the response.
	 *
	 * @param response OkHttp response.
	 * @param duration Request duration.
	 */
	OkHttpResponse(Response response, long duration) {
		this.response = response;
		this.duration = duration;
	}

	@Override
	public long getRequestDuration() {
		return duration;
	}

	@Override
	public int status() {
		return response.code();
	}

	@Override
	public String body() {
		try {
			ResponseBody body = response.body();
			return body == null ? "" : body.string();
		} catch (IOException ex) {
			throw new HttpClientException(ex);
		}
	}

	@Override
	public HttpHeader getHeader(String name) {
		List<String> values = response.headers(name);
		if (values == null || values.isEmpty()) {
			return null;
		}

		return header(name, values);
	}
}
