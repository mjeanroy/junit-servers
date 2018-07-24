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

import com.github.mjeanroy.junit.servers.client.HttpHeader;
import com.github.mjeanroy.junit.servers.client.HttpResponse;
import com.github.mjeanroy.junit.servers.commons.ToStringBuilder;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A simple implementation of {@link HttpResponse}.
 */
public final class DefaultHttpResponse extends AbstractHttpResponse implements HttpResponse {

	/**
	 * Create a default HTTP Response from given values.
	 *
	 * @param duration Request-Response duration.
	 * @param status The response status code.
	 * @param body The response body.
	 * @param headers The response headers.
	 * @return The HTTP response.
	 */
	public static DefaultHttpResponse of(long duration, int status, String body, Collection<HttpHeader> headers) {
		return new DefaultHttpResponse(duration, status, body, headers);
	}

	/**
	 * The response status code.
	 */
	private final int status;

	/**
	 * The response body.
	 */
	private final String body;

	/**
	 * The response headers.
	 */
	private final Map<String, HttpHeader> headers;

	private DefaultHttpResponse(long duration, int status, String body, Collection<HttpHeader> headers) {
		super(duration);
		this.status = status;
		this.body = body;

		this.headers = new LinkedHashMap<>();
		for (HttpHeader header : headers) {
			this.headers.put(header.getName().toLowerCase(), header);
		}
	}

	@Override
	public int status() {
		return status;
	}

	@Override
	protected String readResponseBody() {
		return body;
	}

	@Override
	public Collection<HttpHeader> getHeaders() {
		return headers.values();
	}

	@Override
	public HttpHeader getHeader(String name) {
		return headers.get(name.toLowerCase());
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}

		if (o instanceof DefaultHttpResponse) {
			DefaultHttpResponse r = (DefaultHttpResponse) o;
			return super.equals(o)
					&& Objects.equals(status, r.status)
					&& Objects.equals(body, r.body)
					&& Objects.equals(headers, r.headers);
		}

		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), status, body, headers);
	}

	@Override
	public String toString() {
		return ToStringBuilder.create(getClass())
				.append("duration", getRequestDuration())
				.append("status", status)
				.append("body", body)
				.append("headers", headers)
				.build();
	}

	@Override
	protected boolean canEqual(AbstractHttpResponse o) {
		return o instanceof DefaultHttpResponse;
	}
}
