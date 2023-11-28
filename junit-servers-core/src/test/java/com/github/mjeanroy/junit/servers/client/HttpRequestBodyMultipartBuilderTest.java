/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2023 <mickael.jeanroy@gmail.com>
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

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;

import static com.github.mjeanroy.junit.servers.testing.IoTestUtils.getFileFromClasspath;
import static com.github.mjeanroy.junit.servers.testing.IoTestUtils.getPathFromClasspath;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class HttpRequestBodyMultipartBuilderTest {

	@Test
	void it_should_build_multipart_body_with_multipart_form_data_content_type() {
		HttpRequestBody requestBody = new HttpRequestBodyMultipartBuilder().asMultipartFormData().build();
		assertThat(requestBody).isInstanceOf(HttpRequestBodyMultipart.class);
		assertThat(((HttpRequestBodyMultipart) requestBody).getContentType()).isEqualTo("multipart/form-data; boundary=---------------------------974767299852498929531610575");
	}

	@Test
	void it_should_build_multipart_body_with_multipart_mixed_content_type() {
		HttpRequestBody requestBody = new HttpRequestBodyMultipartBuilder().asMultipartMixed().build();
		assertThat(requestBody).isInstanceOf(HttpRequestBodyMultipart.class);
		assertThat(((HttpRequestBodyMultipart) requestBody).getContentType()).isEqualTo("multipart/mixed; boundary=---------------------------974767299852498929531610575");
	}

	@Test
	void it_should_build_multipart_body_with_multipart_alternative_content_type() {
		HttpRequestBody requestBody = new HttpRequestBodyMultipartBuilder().asMultipartAlternative().build();
		assertThat(requestBody).isInstanceOf(HttpRequestBodyMultipart.class);
		assertThat(((HttpRequestBodyMultipart) requestBody).getContentType()).isEqualTo("multipart/alternative; boundary=---------------------------974767299852498929531610575");
	}

	@Test
	void it_should_build_multipart_body_with_multipart_digest_content_type() {
		HttpRequestBody requestBody = new HttpRequestBodyMultipartBuilder().asMultipartDigest().build();
		assertThat(requestBody).isInstanceOf(HttpRequestBodyMultipart.class);
		assertThat(((HttpRequestBodyMultipart) requestBody).getContentType()).isEqualTo("multipart/digest; boundary=---------------------------974767299852498929531610575");
	}

	@Test
	void it_should_build_multipart_body_with_multipart_parallel_content_type() {
		HttpRequestBody requestBody = new HttpRequestBodyMultipartBuilder().asMultipartParallel().build();
		assertThat(requestBody).isInstanceOf(HttpRequestBodyMultipart.class);
		assertThat(((HttpRequestBodyMultipart) requestBody).getContentType()).isEqualTo("multipart/parallel; boundary=---------------------------974767299852498929531610575");
	}

	@Test
	void it_should_build_multipart_body_with_boundaries() {
		String boundaries = "123456789";
		HttpRequestBody requestBody = new HttpRequestBodyMultipartBuilder().withBoundaries(boundaries).build();
		assertThat(requestBody).isInstanceOf(HttpRequestBodyMultipart.class);

		HttpRequestBodyMultipart multipartBody = (HttpRequestBodyMultipart) requestBody;
		assertThat(multipartBody.getBoundaries()).isEqualTo(boundaries);
		assertThat(multipartBody.getParts()).isEmpty();
	}

	@Test
	void it_should_build_multipart_body_with_default_boundaries() {
		HttpRequestBody requestBody = new HttpRequestBodyMultipartBuilder().build();
		assertThat(requestBody).isInstanceOf(HttpRequestBodyMultipart.class);

		HttpRequestBodyMultipart multipartBody = (HttpRequestBodyMultipart) requestBody;
		assertThat(multipartBody.getBoundaries()).isEqualTo("---------------------------974767299852498929531610575");
		assertThat(multipartBody.getParts()).isEmpty();
	}

	@Test
	void it_should_build_multipart_with_given_part() {
		HttpRequestBody body = HttpRequestBodyString.of("request_body");
		HttpRequestBodyPart part = HttpRequestBodyPart.of(body);
		HttpRequestBody requestBody = new HttpRequestBodyMultipartBuilder().addPart(part).build();
		assertThat(requestBody).isInstanceOf(HttpRequestBodyMultipart.class);

		HttpRequestBodyMultipart multipartBody = (HttpRequestBodyMultipart) requestBody;
		assertThat(multipartBody.getParts()).hasSize(1).containsOnly(part);
	}

	@Test
	void it_should_build_multipart_body_with_given_file() {
		String name = "file";
		String filename = "file1.txt";
		File file = getFileFromClasspath("/" + filename);
		HttpRequestBody requestBody = new HttpRequestBodyMultipartBuilder().addFormDataPart(file, name).build();

		verifyMultipartBody(requestBody, name, filename);
	}

	@Test
	void it_should_build_multipart_body_with_given_file_and_custom_file_name() {
		String name = "file";
		File file = getFileFromClasspath("/file1.txt");
		String filename = "foo.txt";
		HttpRequestBody requestBody = new HttpRequestBodyMultipartBuilder().addFormDataPart(file, name, filename).build();

		verifyMultipartBody(requestBody, name, filename);
	}

	@Test
	void it_should_build_multipart_body_with_given_path() {
		String name = "path";
		String filename = "file1.txt";
		Path path = getPathFromClasspath("/" + filename);
		HttpRequestBody requestBody = new HttpRequestBodyMultipartBuilder().addFormDataPart(path, name).build();

		verifyMultipartBody(requestBody, name, filename);
	}

	@Test
	void it_should_build_multipart_body_with_given_path_and_custom_file_name() {
		String name = "path";
		Path path = getPathFromClasspath("/file1.txt");
		String filename = "foo.txt";
		HttpRequestBody requestBody = new HttpRequestBodyMultipartBuilder().addFormDataPart(path, name, filename).build();

		verifyMultipartBody(requestBody, name, filename);
	}

	@Test
	void it_should_build_multipart_body_with_given_raw_value() {
		String name = "id";
		String value = "1";
		HttpRequestBody requestBody = new HttpRequestBodyMultipartBuilder().addFormDataPart(name, value).build();

		verifyFormDataPart(requestBody, name, value);
	}

	@Test
	void it_should_build_multipart_body_with_given_body() {
		String name = "name";
		String rawBody = "John Doe";
		HttpRequestBodyString partBody = HttpRequestBodyString.of(rawBody);
		HttpRequestBody requestBody = new HttpRequestBodyMultipartBuilder().addFormDataPart(partBody, name).build();

		verifyFormDataPart(requestBody, name, rawBody);
	}

	@Test
	void it_should_build_multipart_body_with_given_body_file() {
		String name = "name";
		String filename = "file1.txt";
		File file = getFileFromClasspath("/" + filename);
		HttpRequestBodyFile partBody = HttpRequestBodyFile.of(file);
		HttpRequestBody requestBody = new HttpRequestBodyMultipartBuilder().addFormDataPart(partBody, name).build();

		verifyMultipartBody(requestBody, name, filename);
	}

	@Test
	void it_should_build_multipart_body_with_given_body_file_and_custom_filename() {
		String name = "name";
		String filename = "foo.txt";
		File file = getFileFromClasspath("/file1.txt");
		HttpRequestBodyFile partBody = HttpRequestBodyFile.of(file);
		HttpRequestBody requestBody = new HttpRequestBodyMultipartBuilder().addFormDataPart(partBody, name, filename).build();

		verifyMultipartBody(requestBody, name, filename);
	}

	@Test
	void it_should_implement_to_string() {
		String boundaries = "123456789";
		HttpRequestBodyMultipartBuilder requestBodyBuilder = new HttpRequestBodyMultipartBuilder()
			.addFormDataPart("id", "1")
			.withBoundaries(boundaries);

		assertThat(requestBodyBuilder).hasToString(
			"HttpRequestBodyMultipartBuilder{" +
				"contentType: \"multipart/form-data\", " +
				"boundaries: \"123456789\", " +
				"parts: [" +
					"HttpRequestBodyPart{" +
						"headers: {" +
							"content-disposition: HttpHeader{name: \"Content-Disposition\", values: [\"form-data; name=\"id\"\"]}" +
						"}, " +
						"body: HttpRequestBodyString{contentType: null, body: \"1\"}" +
					"}" +
				"]" +
			"}"
		);
	}

	@Test
	void it_should_implement_equals_hash_code() {
		EqualsVerifier.forClass(HttpRequestBodyMultipartBuilder.class)
			.suppress(Warning.NONFINAL_FIELDS)
			.verify();
	}

	private static void verifyFormDataPart(HttpRequestBody requestBody, String name, String rawBody) {
		assertThat(requestBody).isInstanceOf(HttpRequestBodyMultipart.class);

		HttpRequestBodyMultipart multipartBody = (HttpRequestBodyMultipart) requestBody;
		assertThat(multipartBody).isNotNull();
		assertThat(multipartBody.getParts()).isNotEmpty();
		assertThat(multipartBody.getParts().get(0)).isInstanceOf(HttpRequestBodyPart.class);

		HttpRequestBodyPart part = multipartBody.getParts().get(0);
		assertThat(part).isNotNull();
		assertThat(part.getBody()).isInstanceOf(HttpRequestBodyString.class);
		assertThat(part.getHeaders()).hasSize(1)
			.extracting("name", "values")
			.contains(
				tuple("Content-Disposition", singletonList("form-data; name=\"" + name + "\""))
			);

		HttpRequestBodyString bodyString = (HttpRequestBodyString) part.getBody();
		assertThat(bodyString.getContentType()).isNull();
		assertThat(bodyString.getBodyString()).isEqualTo(rawBody);
	}

	private static void verifyMultipartBody(HttpRequestBody requestBody, String name, String filename) {
		assertThat(requestBody).isInstanceOf(HttpRequestBodyMultipart.class);

		HttpRequestBodyMultipart multipartBody = (HttpRequestBodyMultipart) requestBody;
		assertThat(multipartBody.getParts()).isNotEmpty();
		assertThat(multipartBody.getParts().get(0)).isInstanceOf(HttpRequestBodyPart.class);

		HttpRequestBodyPart part = multipartBody.getParts().get(0);
		assertThat(part).isNotNull();
		assertThat(part.getHeaders()).hasSize(1)
			.extracting("name", "values")
			.contains(
				tuple("Content-Disposition", singletonList("form-data; name=\"" + name + "\"; filename=\"" + filename + "\""))
			);

		HttpRequestBodyFile bodyFile = (HttpRequestBodyFile) part.getBody();
		assertThat(bodyFile.getContentType()).isEqualTo("text/plain");
	}
}
