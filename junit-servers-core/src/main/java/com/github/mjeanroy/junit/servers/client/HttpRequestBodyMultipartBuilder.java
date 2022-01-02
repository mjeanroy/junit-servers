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

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.github.mjeanroy.junit.servers.commons.lang.Preconditions.notBlank;
import static com.github.mjeanroy.junit.servers.commons.lang.Preconditions.notNull;

/**
 * A builder for {@link HttpRequestBodyMultipart} request.
 */
public final class HttpRequestBodyMultipartBuilder {

	/**
	 * The content type, default is {@link MediaType#MULTIPART_FORM_DATA}.
	 */
	private String contentType;

	/**
	 * Multipart boundaries.
	 */
	private String boundaries;

	/**
	 * Request parts.
	 */
	private final List<HttpRequestBodyPart> parts;

	/**
	 * Create multipart request builder.
	 */
	HttpRequestBodyMultipartBuilder() {
		this.contentType = MediaType.MULTIPART_FORM_DATA;
		this.parts = new ArrayList<>();
	}

	/**
	 * Define new boundaries value.
	 *
	 * @param boundaries New {@link #boundaries}
	 * @return The builder.
	 */
	public HttpRequestBodyMultipartBuilder withBoundaries(String boundaries) {
		this.boundaries = notBlank(boundaries, "boundaries");
		return this;
	}

	/**
	 * Add {@code "form-data"} part. Instead of a "simple" part, a {@code Content-Disposition} header
	 * will be automatically added with this part, such as: {@code Content-Disposition: form-data; name="<name>"}.
	 *
	 * @param body Part body.
	 * @param name The name attribute of {@code COntent-Disposition} header.
	 * @return The builder.
	 */
	public HttpRequestBodyMultipartBuilder addFormDataPart(HttpRequestBody body, String name) {
		notNull(body, "body");
		notBlank(name, "name");

		HttpRequestBodyPartBuilder builder = HttpRequestBodyPartBuilder.of(body);
		if (body instanceof HttpRequestBodyFile) {
			builder.asFormData(name, ((HttpRequestBodyFile) body).getFilename());
		} else {
			builder.asFormData(name);
		}

		return addPart(
			builder.build()
		);
	}


	/**
	 * Add {@code "form-data"} part. Instead of a "simple" part, a {@code Content-Disposition} header
	 * will be automatically added with this part, such as: {@code Content-Disposition: form-data; name="<name>"}.
	 *
	 * @param body Part body.
	 * @param name The name attribute of {@code Content-Disposition} header.
	 * @param filename The filename attribute {@code Content-Disposition} header.
	 * @return The builder.
	 */
	public HttpRequestBodyMultipartBuilder addFormDataPart(HttpRequestBody body, String name, String filename) {
		notNull(body, "body");
		notBlank(name, "name");
		notBlank(filename, "filename");

		return addPart(
			HttpRequestBodyPartBuilder.of(body).asFormData(name, filename).build()
		);
	}

	/**
	 * Add {@code "form-data"} part. Instead of a "simple" part, a {@code Content-Disposition} header
	 * will be automatically added with this part, such as: {@code Content-Disposition: form-data; name="<name>"}.
	 *
	 * @param name The name attribute of {@code COntent-Disposition} header.
	 * @param value Part raw value.
	 * @return The builder.
	 */
	public HttpRequestBodyMultipartBuilder addFormDataPart(String name, String value) {
		notBlank(name, "name");
		notNull(value, "value");

		return addPart(
			HttpRequestBodyPartBuilder.of(value).asFormData(name).build()
		);
	}

	/**
	 * Add {@code "form-data"} part. Instead of a "simple" part, a {@code Content-Disposition} header
	 * will be automatically added with this part, such as: {@code Content-Disposition: form-data; name="<name>"; filename="<filename>"}.
	 *
	 * The name of the file in parameter will be automatically used as the {@code filename} attribute of {@code Content-Disposition} header.
	 *
	 * @param file The file being sent.
	 * @param name The name attribute of {@code COntent-Disposition} header.
	 * @return The builder.
	 */
	public HttpRequestBodyMultipartBuilder addFormDataPart(File file, String name) {
		notNull(file, "file");
		notBlank(name, "name");

		return addPart(
			HttpRequestBodyPartBuilder.of(file).asFormData(name, file.getName()).build()
		);
	}

	/**
	 * Add {@code "form-data"} part. Instead of a "simple" part, a {@code Content-Disposition} header
	 * will be automatically added with this part, such as: {@code Content-Disposition: form-data; name="<name>"; filename="<filename>"}.
	 *
	 * @param file The file being sent.
	 * @param name The name attribute of {@code COntent-Disposition} header.
	 * @param filename The filename attribute {@code Content-Disposition} header.
	 * @return The builder.
	 */
	public HttpRequestBodyMultipartBuilder addFormDataPart(File file, String name, String filename) {
		notNull(file, "file");
		notBlank(name, "name");
		notBlank(filename, "filename");

		return addPart(
			HttpRequestBodyPartBuilder.of(file).asFormData(name, filename).build()
		);
	}

	/**
	 * Add {@code "form-data"} part. Instead of a "simple" part, a {@code Content-Disposition} header
	 * will be automatically added with this part, such as: {@code Content-Disposition: form-data; name="<name>"; filename="<filename>"}.
	 *
	 * @param path The file being sent.
	 * @param name The name attribute of {@code COntent-Disposition} header.
	 * @return The builder.
	 */
	public HttpRequestBodyMultipartBuilder addFormDataPart(Path path, String name) {
		notNull(path, "path");
		notBlank(name, "name");

		return addPart(
			HttpRequestBodyPartBuilder.of(path).asFormData(name, path.getFileName().toString()).build()
		);
	}

	/**
	 * Add {@code "form-data"} part. Instead of a "simple" part, a {@code Content-Disposition} header
	 * will be automatically added with this part, such as: {@code Content-Disposition: form-data; name="<name>"; filename="<filename>"}.
	 *
	 * @param path The file being sent.
	 * @param name The name attribute of {@code COntent-Disposition} header.
	 * @param filename The filename attribute {@code COntent-Disposition} header.
	 * @return The builder.
	 */
	public HttpRequestBodyMultipartBuilder addFormDataPart(Path path, String name, String filename) {
		notNull(path, "path");
		notBlank(name, "name");
		notBlank(filename, "filename");

		return addPart(
			HttpRequestBodyPartBuilder.of(path).asFormData(name, filename).build()
		);
	}

	/**
	 * Add part to request body.
	 *
	 * @param part Part to add.
	 * @return The builder.
	 */
	public HttpRequestBodyMultipartBuilder addPart(HttpRequestBodyPart part) {
		notNull(part, "part");
		this.parts.add(part);
		return this;
	}

	/**
	 * Update content-type to {@code "multipart/form-data"}.
	 *
	 * @return The builder.
	 */
	public HttpRequestBodyMultipartBuilder asMultipartFormData() {
		this.contentType = MediaType.MULTIPART_FORM_DATA;
		return this;
	}

	/**
	 * Update content-type to {@code "multipart/mixed"}.
	 *
	 * @return The builder.
	 */
	public HttpRequestBodyMultipartBuilder asMultipartMixed() {
		this.contentType = MediaType.MULTIPART_MIXED;
		return this;
	}

	/**
	 * Update content-type to {@code "multipart/alternative"}.
	 *
	 * @return The builder.
	 */
	public HttpRequestBodyMultipartBuilder asMultipartAlternative() {
		this.contentType = MediaType.MULTIPART_ALTERNATIVE;
		return this;
	}

	/**
	 * Update content-type to {@code "multipart/digest"}.
	 *
	 * @return The builder.
	 */
	public HttpRequestBodyMultipartBuilder asMultipartDigest() {
		this.contentType = MediaType.MULTIPART_DIGEST;
		return this;
	}

	/**
	 * Update content-type to {@code "multipart/parallel"}.
	 *
	 * @return The builder.
	 */
	public HttpRequestBodyMultipartBuilder asMultipartParallel() {
		this.contentType = MediaType.MULTIPART_PARALLEL;
		return this;
	}

	/**
	 * Build multipart body.
	 *
	 * @return The multipart body.
	 */
	public HttpRequestBody build() {
		return new HttpRequestBodyMultipart(contentType, boundaries, parts);
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

		if (o instanceof HttpRequestBodyMultipartBuilder) {
			HttpRequestBodyMultipartBuilder b = (HttpRequestBodyMultipartBuilder) o;
			return Objects.equals(contentType, b.contentType) && Objects.equals(boundaries, b.boundaries) && Objects.equals(parts, b.parts);
		}

		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(contentType, boundaries, parts);
	}
}
