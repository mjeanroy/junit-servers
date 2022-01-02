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

import com.github.mjeanroy.junit.servers.commons.io.Ios;
import com.github.mjeanroy.junit.servers.commons.lang.ToStringBuilder;
import com.github.mjeanroy.junit.servers.loggers.Logger;
import com.github.mjeanroy.junit.servers.loggers.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static com.github.mjeanroy.junit.servers.commons.lang.Objects.firstNonNull;
import static java.util.Collections.unmodifiableList;

/**
 * An implementation of {@link HttpRequestBody} for {@code "multipart/form-data"} request bodies.
 */
final class HttpRequestBodyMultipart implements HttpRequestBody {

	/**
	 * Class Logger.
	 */
	private static final Logger log = LoggerFactory.getLogger(HttpRequestBodyMultipart.class);

	/**
	 * The value added before boundaries in multipart request.
	 */
	private static final byte[] DASH_DASH = Ios.toUtf8Bytes("--");

	/**
	 * The default boundaries that will be used if no one is defined.
	 */
	private static final String DEFAULT_BOUNDARIES = "---------------------------974767299852498929531610575";

	/**
	 * The content-type, starts with {@code "multipart/"}.
	 */
	private final String contentType;

	/**
	 * Part boundaries.
	 */
	private final String boundaries;

	/**
	 * The request parts.
	 */
	private final List<HttpRequestBodyPart> parts;

	/**
	 * Create multipart request body.
	 *
	 * @param boundaries Multipart boundaries (may be {@code null}).
	 * @param parts The request parts.
	 */
	HttpRequestBodyMultipart(String contentType, String boundaries, Collection<HttpRequestBodyPart> parts) {
		this.contentType = firstNonNull(contentType, MediaType.MULTIPART_FORM_DATA);
		this.boundaries = firstNonNull(boundaries, DEFAULT_BOUNDARIES);
		this.parts = unmodifiableList(new ArrayList<>(parts));
	}

	@Override
	public String getContentType() {
		return contentType + "; boundary=" + boundaries;
	}

	@Override
	public byte[] getBody() throws IOException {
		log.debug("Generating multipart body");

		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		for (HttpRequestBodyPart bodyPart : parts) {
			log.debug("Writing part: {}", bodyPart);
			log.debug("Use boundary: {}", boundaries);

			bos.write(DASH_DASH);
			bos.write(Ios.toUtf8Bytes(boundaries));
			bos.write(Ios.CRLF);
			bos.write(bodyPart.serialize());
			bos.write(Ios.CRLF);
		}

		log.debug("Writing last boundaries: {}", boundaries);

		bos.write(DASH_DASH);
		bos.write(Ios.toUtf8Bytes(boundaries));
		return bos.toByteArray();
	}

	/**
	 * Get {@link #boundaries}
	 *
	 * @return {@link #boundaries}
	 */
	String getBoundaries() {
		return boundaries;
	}

	/**
	 * Get {@link #parts}
	 *
	 * @return {@link #parts}
	 */
	List<HttpRequestBodyPart> getParts() {
		return parts;
	}

	@Override
	public String toString() {
		return ToStringBuilder.create(getClass())
			.append("contentType", contentType)
			.append("boundaries", boundaries)
			.append("parts", parts)
			.build();
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}

		if (o instanceof HttpRequestBodyMultipart) {
			HttpRequestBodyMultipart b = (HttpRequestBodyMultipart) o;
			return Objects.equals(contentType, b.contentType) && Objects.equals(boundaries, b.boundaries) && Objects.equals(parts, b.parts);
		}

		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(contentType, boundaries, parts);
	}
}
