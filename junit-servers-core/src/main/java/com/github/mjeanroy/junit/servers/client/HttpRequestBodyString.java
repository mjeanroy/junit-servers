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

package com.github.mjeanroy.junit.servers.client;

import com.github.mjeanroy.junit.servers.commons.lang.ToStringBuilder;

import java.nio.charset.Charset;
import java.util.Objects;

import static com.github.mjeanroy.junit.servers.commons.lang.Preconditions.notBlank;

/**
 * A simple {@link HttpRequestBody} using a raw string as request body.
 */
final class HttpRequestBodyString implements HttpRequestBody {

	/**
	 * The body content type.
	 */
	private final String contentType;

	/**
	 * The body content.
	 */
	private final String body;

	/**
	 * Create HTTP Request body from raw string.
	 *
	 * @param contentType The body content type.
	 * @param body The body as a raw string.
	 */
	HttpRequestBodyString(String contentType, String body) {
		this.contentType = contentType;
		this.body = notBlank(body, "HTTP Request body must not be empty");
	}

	@Override
	public String getContentType() {
		return contentType;
	}

	@Override
	public byte[] getBody() {
		return body.getBytes(Charset.defaultCharset());
	}

	@Override
	public String toString() {
		return ToStringBuilder.create(getClass())
			.append("contentType", contentType)
			.append("body", body)
			.build();
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}

		if (o instanceof HttpRequestBodyString) {
			HttpRequestBodyString b = (HttpRequestBodyString) o;
			return Objects.equals(contentType, b.contentType) && Objects.equals(body, b.body);
		}

		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(contentType, body);
	}
}
