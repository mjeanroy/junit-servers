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

package com.github.mjeanroy.junit.servers.client;

import com.github.mjeanroy.junit.servers.commons.io.Ios;
import com.github.mjeanroy.junit.servers.commons.lang.Strings;
import com.github.mjeanroy.junit.servers.commons.lang.ToStringBuilder;
import com.github.mjeanroy.junit.servers.loggers.Logger;
import com.github.mjeanroy.junit.servers.loggers.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import static com.github.mjeanroy.junit.servers.client.HttpHeader.header;
import static com.github.mjeanroy.junit.servers.commons.lang.Preconditions.doesNotContainNull;
import static com.github.mjeanroy.junit.servers.commons.lang.Preconditions.notNull;
import static java.util.Collections.emptyList;
import static java.util.Collections.singleton;
import static java.util.Collections.unmodifiableMap;

/**
 * An implementation of {@link HttpRequestBody} for {@code "multipart/form-data"} request bodies.
 */
public final class HttpRequestBodyPart {

	/**
	 * Class Logger.
	 */
	private static final Logger log = LoggerFactory.getLogger(HttpRequestBodyPart.class);

	/**
	 * Create body part without any header and body.
	 *
	 * @param body Part body.
	 * @return The request body part.
	 */
	static HttpRequestBodyPart of(HttpRequestBody body) {
		notNull(body, "body");
		return new HttpRequestBodyPart(body, emptyList());
	}

	/**
	 * Create body part with given header and body.
	 *
	 * @param body Part body.
	 * @param headers Part headers.
	 * @return The request body part.
	 */
	static HttpRequestBodyPart of(HttpRequestBody body, Collection<HttpHeader> headers) {
		notNull(body, "body");
		doesNotContainNull(headers, "headers");
		return new HttpRequestBodyPart(body, headers);
	}

	/**
	 * Create body part with given single header and body.
	 *
	 * @param body Part body.
	 * @param header Part header.
	 * @return The request body part.
	 * @throws NullPointerException If {@code body} or {@code header} are {@code null}.
	 */
	static HttpRequestBodyPart of(HttpRequestBody body, HttpHeader header) {
		notNull(body, "body");
		notNull(header, "header");
		return new HttpRequestBodyPart(body, singleton(header));
	}

	/**
	 * The list of headers for this body part.
	 */
	private final Map<String, HttpHeader> headers;

	/**
	 * The request body.
	 */
	private final HttpRequestBody body;

	/**
	 * Create the body part.
	 *
	 * @param body The part body.
	 * @param headers The list of headers to add.
	 */
	private HttpRequestBodyPart(HttpRequestBody body, Collection<HttpHeader> headers) {
		Map<String, HttpHeader> map = new LinkedHashMap<>();
		for (HttpHeader header : headers) {
			map.put(header.getName().toLowerCase(), header);
		}

		this.body = body;
		this.headers = unmodifiableMap(map);
	}

	/**
	 * Serialize body port as a byte array (using UTF-8 charset).
	 *
	 * @return The serialization result.
	 * @throws IOException If an error occurred during byte transformation.
	 */
	byte[] serialize() throws IOException {
		log.debug("Writing part body");

		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		for (HttpHeader header : headers.values()) {
			log.debug("Writing header: {}", header);
			bos.write(Ios.toUtf8Bytes(header.serialize()));
			bos.write(Ios.CRLF);
		}

		String contentType = body.getContentType();
		if (Strings.isNotEmpty(contentType)) {
			log.debug("Writing content type: {}", contentType);
			bos.write(Ios.toUtf8Bytes(header("Content-Type", body.getContentType()).serialize()));
			bos.write(Ios.CRLF);
		}

		log.debug("Writing body: {}", body);
		bos.write(Ios.CRLF);
		bos.write(body.getBody());

		return bos.toByteArray();
	}

	/**
	 * Get {@link #body}
	 *
	 * @return {@link #body}
	 */
	HttpRequestBody getBody() {
		return body;
	}

	/**
	 * Get {@link #headers}
	 *
	 * @return {@link #headers}
	 */
	Collection<HttpHeader> getHeaders() {
		return headers.values();
	}

	@Override
	public String toString() {
		return ToStringBuilder.create(getClass())
			.append("headers", headers)
			.append("body", body)
			.build();
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}

		if (o instanceof HttpRequestBodyPart) {
			HttpRequestBodyPart b = (HttpRequestBodyPart) o;
			return Objects.equals(headers, b.headers) && Objects.equals(body, b.body);
		}

		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(headers, body);
	}
}
