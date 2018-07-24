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

package com.github.mjeanroy.junit.servers.client.impl.apache;

import com.github.mjeanroy.junit.servers.client.HttpHeader;
import com.github.mjeanroy.junit.servers.client.HttpResponse;
import com.github.mjeanroy.junit.servers.client.impl.AbstractHttpResponse;
import com.github.mjeanroy.junit.servers.commons.ToStringBuilder;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.github.mjeanroy.junit.servers.commons.Preconditions.notNull;
import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;

/**
 * Implementation of {@link HttpResponse} delegating calls to origina {@link org.apache.http.HttpResponse}
 * instance.
 */
final class ApacheHttpResponse extends AbstractHttpResponse implements HttpResponse {

	/**
	 * The original response.
	 */
	private final org.apache.http.HttpResponse response;

	/**
	 * Create the response from Apache HTTP Component.
	 *
	 * @param response The original response.
	 * @param duration Request duration.
	 */
	ApacheHttpResponse(org.apache.http.HttpResponse response, long duration) {
		super(duration);
		this.response = notNull(response, "Response");
	}

	@Override
	public int status() {
		return response.getStatusLine().getStatusCode();
	}

	@Override
	protected String readResponseBody() throws IOException {
		HttpEntity entity = response.getEntity();
		return entity == null ? "" : EntityUtils.toString(entity);
	}

	@Override
	public Collection<HttpHeader> getHeaders() {
		final Header[] headers = response.getAllHeaders();
		if (headers == null || headers.length == 0) {
			return emptyList();
		}

		final Map<String, HttpHeader.Builder> builders = new HashMap<>();

		for (Header h : headers) {
			final String name = h.getName();
			final String value = h.getValue();
			final String key = h.getName().toLowerCase();

			HttpHeader.Builder current = builders.get(key);
			if (current == null) {
				current = HttpHeader.builder(name);
				builders.put(key, current);
			}

			current.addValue(value);
		}

		final List<HttpHeader> results = new ArrayList<>(builders.size());
		for (HttpHeader.Builder header : builders.values()) {
			results.add(header.build());
		}

		return unmodifiableList(results);
	}

	@Override
	public HttpHeader getHeader(String name) {
		Header[] values = response.getHeaders(name);
		if (values == null || values.length == 0) {
			return null;
		}

		HttpHeader.Builder builder = HttpHeader.builder(name);
		for (Header header : values) {
			builder.addValue(header.getValue());
		}

		return builder.build();
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

		if (o instanceof ApacheHttpResponse) {
			ApacheHttpResponse r = (ApacheHttpResponse) o;
			return super.equals(o) && Objects.equals(response, r.response);
		}

		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), response);
	}

	@Override
	protected boolean canEqual(AbstractHttpResponse o) {
		return o instanceof ApacheHttpResponse;
	}
}
