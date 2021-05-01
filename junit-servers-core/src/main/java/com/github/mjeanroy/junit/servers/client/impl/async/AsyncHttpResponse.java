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

import com.github.mjeanroy.junit.servers.client.HttpHeader;
import com.github.mjeanroy.junit.servers.client.HttpResponse;
import com.github.mjeanroy.junit.servers.client.impl.AbstractHttpResponse;
import com.github.mjeanroy.junit.servers.commons.lang.ToStringBuilder;
import io.netty.handler.codec.http.HttpHeaders;
import org.asynchttpclient.Response;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.github.mjeanroy.junit.servers.commons.lang.Preconditions.notNull;

/**
 * Implementation of {@link HttpResponse} delegating calls to original {@link Response}
 * instance.
 */
final class AsyncHttpResponse extends AbstractHttpResponse {

	/**
	 * The original response.
	 */
	private final Response response;

	/**
	 * Create the response from AsyncHttpClient.
	 *
	 * @param response The original response.
	 * @param duration Request duration.
	 */
	AsyncHttpResponse(Response response, long duration) {
		super(duration);
		this.response = notNull(response, "Response");
	}

	@Override
	public int status() {
		return response.getStatusCode();
	}

	@Override
	protected String readResponseBody() {
		return response.getResponseBody();
	}

	@Override
	public Collection<HttpHeader> getHeaders() {
		HttpHeaders headers = response.getHeaders();

		List<HttpHeader> results = new ArrayList<>(headers.size());
		for (Map.Entry<String, String> entry : headers) {
			String name = entry.getKey();
			List<String> values = headers.getAll(name);
			results.add(HttpHeader.header(name, values));
		}

		return results;
	}

	@Override
	public HttpHeader getHeader(String name) {
		List<String> values = response.getHeaders(name);
		if (values == null || values.isEmpty()) {
			return null;
		}

		return HttpHeader.header(name, values);
	}

	@Override
	public String toString() {
		return ToStringBuilder.create(getClass())
			.append("duration", getRequestDuration())
			.append("response", response)
			.build();
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}

		if (o instanceof AsyncHttpResponse) {
			AsyncHttpResponse r = (AsyncHttpResponse) o;
			return super.equals(r) && Objects.equals(response, r.response);
		}

		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), response);
	}

	@Override
	protected boolean canEqual(AbstractHttpResponse o) {
		return o instanceof AsyncHttpResponse;
	}
}
