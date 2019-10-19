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

import com.github.mjeanroy.junit.servers.commons.io.Ios;
import com.github.mjeanroy.junit.servers.commons.lang.ToStringBuilder;

import java.util.Objects;

import static com.github.mjeanroy.junit.servers.commons.lang.Preconditions.notBlank;
import static com.github.mjeanroy.junit.servers.commons.lang.Preconditions.notNull;

/**
 * A simple {@link HttpRequestBody} using a raw string as request body.
 */
final class HttpRequestBodyString implements HttpRequestBody {

	/**
	 * Create {@link HttpRequestBodyString} with a body and without any content-type.
	 *
	 * @param body Request body.
	 * @return The body.
	 * @throws NullPointerException If {@code body} is {@code null}
	 */
	static HttpRequestBodyString of(String body) {
		notNull(body, "body");
		return new HttpRequestBodyString(null, body);
	}

	/**
	 * Create {@link HttpRequestBodyString} with a body and with a content-type.
	 *
	 * @param body Request body.
	 * @return The body.
	 * @throws NullPointerException If {@code body} or {@code contentType} are {@code null}
	 * @throws IllegalArgumentException If {@code contentType} is empty or blank.
	 */
	static HttpRequestBodyString of(String body, String contentType) {
		notNull(body, "body");
		notBlank(contentType, "contentType");
		return new HttpRequestBodyString(contentType, body);
	}

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
	private HttpRequestBodyString(String contentType, String body) {
		this.contentType = contentType;
		this.body = body;
	}

	@Override
	public String getContentType() {
		return contentType;
	}

	@Override
	public byte[] getBody() {
		return Ios.toUtf8Bytes(body);
	}

	/**
	 * Get {@link #body}
	 *
	 * @return {@link #body}
	 */
	String getBodyString() {
		return body;
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
