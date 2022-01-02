/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2022 <mickael.jeanroy@gmail.com>
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
import com.github.mjeanroy.junit.servers.loggers.Logger;
import com.github.mjeanroy.junit.servers.loggers.LoggerFactory;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.github.mjeanroy.junit.servers.commons.lang.Preconditions.notBlank;
import static com.github.mjeanroy.junit.servers.commons.lang.Preconditions.notNull;

/**
 * Builder for {@link HttpRequestBodyPart}.
 */
final class HttpRequestBodyPartBuilder {

	/**
	 * Class Logger.
	 */
	private static final Logger log = LoggerFactory.getLogger(HttpRequestBodyPartBuilder.class);

	/**
	 * Initialize builder.
	 *
	 * @param body The part body.
	 * @return The part builder.
	 */
	static HttpRequestBodyPartBuilder of(HttpRequestBody body) {
		return new HttpRequestBodyPartBuilder(body);
	}

	/**
	 * Initialize builder.
	 *
	 * @param file The file body.
	 * @return The part builder.
	 */
	static HttpRequestBodyPartBuilder of(File file) {
		return new HttpRequestBodyPartBuilder(HttpRequestBodyFile.of(file));
	}

	/**
	 * Initialize builder.
	 *
	 * @param path The file body.
	 * @return The part builder.
	 */
	static HttpRequestBodyPartBuilder of(Path path) {
		return new HttpRequestBodyPartBuilder(HttpRequestBodyFile.of(path));
	}

	/**
	 * Initialize builder.
	 *
	 * @param value The body string.
	 * @return The part builder.
	 */
	static HttpRequestBodyPartBuilder of(String value) {
		return new HttpRequestBodyPartBuilder(HttpRequestBodyString.of(value));
	}

	/**
	 * The part body.
	 */
	private final HttpRequestBody body;

	/**
	 * Part headers.
	 */
	private final Map<String, HttpHeader> headers;

	/**
	 * Create builder with given body.
	 *
	 * @param body The body.
	 */
	private HttpRequestBodyPartBuilder(HttpRequestBody body) {
		this.body = notNull(body, "body");
		this.headers = new LinkedHashMap<>();
	}

	/**
	 * Add new header.
	 *
	 * Note that {@code Content-Type} header is forbidden here, as it should be defined with
	 * the part {@link #body} (and it will be automatically added when part will be serialized).
	 *
	 * @param header Header.
	 * @return The builder.
	 * @throws NullPointerException If {@code header} is {@code null}
	 * @throws IllegalArgumentException If header name is the {@code "Content-Type"} header.
	 * @throws IllegalStateException If this header has already been added
	 */
	HttpRequestBodyPartBuilder addHeader(HttpHeader header) {
		log.trace("Attempt to add header: {}", header);

		notNull(header, "header");

		String key = header.getName().toLowerCase();

		if (Objects.equals(key, "content-type")) {
			log.error("Attempt to add content-type header: {}", header);
			throw new IllegalArgumentException(String.format("Header '%s' is forbidden, please specify Content-Type wuth request body", header.getName()));
		}

		if (headers.containsKey(key)) {
			log.error("Attempt to add duplicated header: {}", header);
			throw new IllegalStateException(String.format("Header '%s' is already defined", header.getName()));
		}

		log.trace("Adding header: {}", header);
		this.headers.put(header.getName().toLowerCase(), header);
		return this;
	}

	/**
	 * Add new header to this part.
	 *
	 * Note that {@code Content-Type} header is forbidden here, as it should be defined with
	 * the part {@link #body} (and it will be automatically added when part will be serialized).
	 *
	 * @param name Header name.
	 * @param value Header value.
	 * @return The builder.
	 * @throws NullPointerException If {@code name} is {@code null}
	 * @throws IllegalArgumentException If {@code name} is empty or blank
	 * @throws IllegalArgumentException If header name is the {@code "Content-Type"} header.
	 * @throws IllegalStateException If this header has already been added
	 */
	HttpRequestBodyPartBuilder addHeader(String name, String value) {
		return addHeader(
			HttpHeader.of(name, value)
		);
	}

	/**
	 * Add the {@code "Content-Disposition"} header, specifying the form-data part with given {@code name}.
	 *
	 * @param name Part name.
	 * @return The builder.
	 * @throws NullPointerException If {@code name} is {@code null}
	 * @throws IllegalArgumentException If {@code name} is empty, or blank.
	 */
	HttpRequestBodyPartBuilder asFormData(String name) {
		notBlank(name, "name");

		return addHeader(
			createContentDispositionHeader(name, null)
		);
	}

	/**
	 * Add the {@code "Content-Disposition"} header, specifying the form-data part with given {@code name}
	 * and given {@code filename} being sent.
	 *
	 * @param name Part name.
	 * @param filename Filename.
	 * @return The builder.
	 * @throws NullPointerException If {@code name} or {@code filename} are {@code null}
	 * @throws IllegalArgumentException If {@code name} or {@code filename} are empty, or blank.
	 */
	HttpRequestBodyPartBuilder asFormData(String name, String filename) {
		notBlank(name, "name");
		notBlank(filename, "filename");

		return addHeader(
			createContentDispositionHeader(name, filename)
		);
	}

	/**
	 * Build HTTP request body part.
	 *
	 * @return The body part.
	 */
	HttpRequestBodyPart build() {
		return HttpRequestBodyPart.of(body, headers.values());
	}

	@Override
	public String toString() {
		return ToStringBuilder.create(getClass())
			.append("body", body)
			.append("headers", headers)
			.build();
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}

		if (o instanceof HttpRequestBodyPartBuilder) {
			HttpRequestBodyPartBuilder b = (HttpRequestBodyPartBuilder) o;
			return Objects.equals(body, b.body) && Objects.equals(headers, b.headers);
		}

		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(body, headers);
	}

	/**
	 * Create the {@code Content-Disposition} header, that can be use as header for multipart form-data
	 * part.
	 *
	 * @param name The part name.
	 * @param filename THe filename, may be {@code null}.
	 * @return The header value.
	 */
	private static HttpHeader createContentDispositionHeader(String name, String filename) {
		return HttpHeader.of(
			HttpHeaders.CONTENT_DISPOSITION, createContentDispositionHeaderValue(name, filename)
		);
	}

	/**
	 * Create the {@code Content-Disposition} header value, that can be use as header for multipart form-data
	 * part.
	 *
	 * @param name The part name.
	 * @param filename THe filename, may be {@code null}.
	 * @return The header value.
	 */
	private static String createContentDispositionHeaderValue(String name, String filename) {
		List<String> parts = new ArrayList<>(3);
		parts.add("form-data");
		parts.add("name=\"" + toEscapedString(name) + "\"");

		if (filename != null) {
			parts.add("filename=\"" + toEscapedString(filename) + "\"");
		}

		return String.join("; ", parts);
	}

	/**
	 * Escape given input string to use it in {@code "Content-Disposition"} header safely.
	 *
	 * @param input Input string to escape.
	 * @return Escaped string.
	 */
	private static String toEscapedString(String input) {
		final int size = input.length();
		final StringBuilder output = new StringBuilder(size * 3);

		for (int i = 0; i < size; i++) {
			char ch = input.charAt(i);
			switch (ch) {
				case '\n':
					output.append("%0A");
					break;
				case '\r':
					output.append("%0D");
					break;
				case '"':
					output.append("%22");
					break;
				default:
					output.append(ch);
					break;
			}
		}

		return output.toString();
	}
}
