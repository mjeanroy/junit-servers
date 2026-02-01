/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2026 <mickael.jeanroy@gmail.com>
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

package com.github.mjeanroy.junit.servers.client.impl.okhttp3;

import com.github.mjeanroy.junit.servers.client.HttpHeader;
import com.github.mjeanroy.junit.servers.client.HttpResponse;
import com.github.mjeanroy.junit.servers.client.impl.AbstractHttpResponse;
import com.github.mjeanroy.junit.servers.commons.lang.ToStringBuilder;
import okhttp3.Headers;
import okhttp3.Response;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static com.github.mjeanroy.junit.servers.commons.lang.Preconditions.notNull;

/**
 * Implementation of {@link HttpResponse} delegating calls to original {@link Response}
 * instance.
 */
final class OkHttpResponse extends AbstractHttpResponse {

	private final int code;
	private final String body;
	private final Headers headers;

	/**
	 * Create the response from OkHTTP3
	 *
	 * @param code The original response status.
	 * @param body The original response body.
	 * @param headers The original response headers.
	 * @param duration Request duration.
	 */
	OkHttpResponse(
			int code,
			String body,
			Headers headers,
			long duration
	) {
		super(duration);
		this.code = code;
		this.body = body;
		this.headers = notNull(headers, "Headers");
	}

	@Override
	public int status() {
		return code;
	}

	@Override
	protected String readResponseBody() {
		return body;
	}

	@Override
	public Collection<HttpHeader> getHeaders() {
		int size = headers.size();

		List<HttpHeader> results = new ArrayList<>(size);
		for (String name : headers.names()) {
			results.add(HttpHeader.header(name, headers.values(name)));
		}

		return results;
	}

	@Override
	public HttpHeader getHeader(String name) {
		List<String> values = headers.values(name);
		if (values == null || values.isEmpty()) {
			return null;
		}

		return HttpHeader.header(name, values);
	}

	@Override
	public String toString() {
		return ToStringBuilder.create(getClass())
			.append("duration", getRequestDuration())
			.append("code", code)
			.append("body", body)
			.append("headers", headers)
			.build();
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}

		if (o instanceof OkHttpResponse) {
			OkHttpResponse r = (OkHttpResponse) o;
			return super.equals(r)
					&& Objects.equals(code, r.code)
					&& Objects.equals(body, r.body)
					&& Objects.equals(headers, r.headers);
		}

		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), code, body, headers);
	}

	@Override
	protected boolean canEqual(AbstractHttpResponse o) {
		return o instanceof OkHttpResponse;
	}
}
