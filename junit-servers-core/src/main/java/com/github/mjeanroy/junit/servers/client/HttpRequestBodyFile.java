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

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

import static com.github.mjeanroy.junit.servers.commons.lang.Objects.firstNonNull;
import static com.github.mjeanroy.junit.servers.commons.lang.Preconditions.notBlank;
import static com.github.mjeanroy.junit.servers.commons.lang.Preconditions.notNull;

/**
 * An implementation of {@link HttpRequestBody} with a file body.
 */
final class HttpRequestBodyFile implements HttpRequestBody {

	/**
	 * Create body file from given {@code file}.
	 *
	 * @param file The file.
	 * @return The request body.
	 * @throws NullPointerException If {@code file} is {@code null}
	 */
	static HttpRequestBodyFile of(File file) {
		notNull(file, "file");
		return HttpRequestBodyFile.of(file.toPath());
	}

	/**
	 * Create body file from given {@code path}.
	 *
	 * @param path The path.
	 * @return The request body.
	 * @throws NullPointerException If {@code path} is {@code null}
	 */
	static HttpRequestBodyFile of(Path path) {
		notNull(path, "path");

		String contentType = firstNonNull(
			Ios.guessContentType(path),
			MediaType.APPLICATION_OCTET_STREAM
		);

		return new HttpRequestBodyFile(contentType, path);
	}

	/**
	 * Create body file from given {@code file} and given content type.
	 *
	 * @param contentType The content type.
	 * @param file The file.
	 * @return The request body.
	 * @throws NullPointerException If {@code file} or {@code contentType} are {@code null}
	 * @throws IllegalArgumentException If {@code contentType} is empty or blank.
	 */
	static HttpRequestBodyFile of(File file, String contentType) {
		notNull(file, "file");
		notBlank(contentType, "content type");
		return new HttpRequestBodyFile(contentType, file.toPath());
	}

	/**
	 * Create body file from given {@code path} and given content type.
	 *
	 * @param contentType The content type.
	 * @param path The path.
	 * @return The request body.
	 * @throws NullPointerException If {@code path} or {@code contentType} are {@code null}
	 * @throws IllegalArgumentException If {@code contentType} is empty or blank.
	 */
	static HttpRequestBodyFile of(Path path, String contentType) {
		notNull(path, "path");
		notBlank(contentType, "Content Type");
		return new HttpRequestBodyFile(contentType, path);
	}

	/**
	 * File content type.
	 */
	private final String contentType;

	/**
	 * The file.
	 */
	private final Path path;

	/**
	 * Create body file.
	 * @param contentType The request body content type.
	 * @param path The path file.
	 */
	private HttpRequestBodyFile(String contentType, Path path) {
		this.contentType = contentType;
		this.path = path;
	}

	@Override
	public String getContentType() {
		return contentType;
	}

	@Override
	public byte[] getBody() throws IOException {
		return Ios.toBytes(path);
	}

	/**
	 * Get {@link #path}
	 *
	 * @return {@link #path}
	 */
	Path getPath() {
		return path;
	}

	/**
	 * Get name of given file.
	 *
	 * @return Filename.
	 */
	String getFilename() {
		return path.getFileName().toString();
	}

	@Override
	public String toString() {
		return ToStringBuilder.create(getClass())
			.append("contentType", contentType)
			.append("path", path)
			.build();
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}

		if (o instanceof HttpRequestBodyFile) {
			HttpRequestBodyFile b = (HttpRequestBodyFile) o;
			return Objects.equals(contentType, b.contentType) && Objects.equals(path, b.path);
		}

		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(contentType, path);
	}
}
