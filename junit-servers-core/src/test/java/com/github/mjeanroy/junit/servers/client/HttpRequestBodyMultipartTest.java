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

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static com.github.mjeanroy.junit.servers.testing.IoTestUtils.getFileFromClasspath;
import static com.github.mjeanroy.junit.servers.utils.commons.TestUtils.toUtf8String;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singleton;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class HttpRequestBodyMultipartTest {

	private static final String TEXT_PLAIN = "text/plain";

	@Test
	void it_should_create_multipart_body_with_default_content_type() {
		String contentType = null;
		String boundaries = null;
		List<HttpRequestBodyPart> parts = emptyList();

		HttpRequestBodyMultipart requestBody = new HttpRequestBodyMultipart(contentType, boundaries, parts);

		assertThat(requestBody.getContentType()).isEqualTo("multipart/form-data; boundary=---------------------------974767299852498929531610575");
	}

	@Test
	void it_should_create_multipart_body_with_custom_content_type() {
		String contentType = "multipart/mixed";
		String boundaries = null;
		List<HttpRequestBodyPart> parts = emptyList();

		HttpRequestBodyMultipart requestBody = new HttpRequestBodyMultipart(contentType, boundaries, parts);

		assertThat(requestBody.getContentType()).isEqualTo("multipart/mixed; boundary=---------------------------974767299852498929531610575");
	}

	@Test
	void it_should_create_multipart_body_with_parts_and_boundaries() {
		String contentType = "multipart/form-data";
		String boundaries = "123456789";
		List<HttpRequestBodyPart> parts = asList(
			givenHttpRequestBodyPart("part1", HttpRequestBodyString.of("HttpRequestBody1", TEXT_PLAIN)),
			givenHttpRequestBodyPart("part2", HttpRequestBodyString.of("HttpRequestBody2", TEXT_PLAIN))
		);

		HttpRequestBodyMultipart requestBody = new HttpRequestBodyMultipart(contentType, boundaries, parts);

		assertThat(requestBody.getBoundaries()).isEqualTo(boundaries);
		assertThat(requestBody.getParts()).isEqualTo(parts);
	}

	@Test
	void it_should_create_multipart_body_with_parts_and_default_boundaries() {
		String contentType = "multipart/form-data";
		String boundaries = null;
		List<HttpRequestBodyPart> parts = asList(
			givenHttpRequestBodyPart("part1", HttpRequestBodyString.of("HttpRequestBody1", TEXT_PLAIN)),
			givenHttpRequestBodyPart("part2", HttpRequestBodyString.of("HttpRequestBody2", TEXT_PLAIN))
		);

		HttpRequestBodyMultipart requestBody = new HttpRequestBodyMultipart(contentType, boundaries, parts);

		assertThat(requestBody.getBoundaries()).isEqualTo("---------------------------974767299852498929531610575");
		assertThat(requestBody.getParts()).isEqualTo(parts);
	}

	@Test
	void it_should_have_content_type_with_custom_boundaries() {
		String contentType = "multipart/form-data";
		String boundaries = "123456789";
		List<HttpRequestBodyPart> parts = emptyList();
		HttpRequestBodyMultipart requestBody = new HttpRequestBodyMultipart(contentType, boundaries, parts);

		assertThat(requestBody.getContentType()).isEqualTo("multipart/form-data; boundary=123456789");
	}

	@Test
	void it_should_have_content_type_with_default_boundaries() {
		String contentType = "multipart/form-data";
		String boundaries = null;
		List<HttpRequestBodyPart> parts = emptyList();
		HttpRequestBodyMultipart requestBody = new HttpRequestBodyMultipart(contentType, boundaries, parts);

		assertThat(requestBody.getContentType()).isEqualTo("multipart/form-data; boundary=---------------------------974767299852498929531610575");
	}

	@Test
	void it_should_serialize_multipart_body() throws Exception {
		String contentType = "multipart/form-data";
		String boundaries = "123456789";
		List<HttpRequestBodyPart> parts = asList(
			givenHttpRequestBodyPart("part1", HttpRequestBodyString.of("HttpRequestBody1", TEXT_PLAIN)),
			givenHttpRequestBodyPart("part2", HttpRequestBodyString.of("HttpRequestBody2", TEXT_PLAIN))
		);

		HttpRequestBodyMultipart requestBody = new HttpRequestBodyMultipart(contentType, boundaries, parts);
		String result = new String(requestBody.getBody(), StandardCharsets.UTF_8);

		assertThat(result).isEqualTo(String.join("\r\n", asList(
			"--123456789",
			"Content-Disposition: form-data; name=\"part1\"",
			"Content-Type: text/plain",
			"",
			"HttpRequestBody1",
			"--123456789",
			"Content-Disposition: form-data; name=\"part2\"",
			"Content-Type: text/plain",
			"",
			"HttpRequestBody2",
			"--123456789"
		)));
	}

	@Test
	void it_should_serialize_multipart_body_with_a_file() throws Exception {
		String contentType = "multipart/form-data";
		String boundaries = "123456789";
		File file = getFileFromClasspath("/file1.txt");
		List<HttpRequestBodyPart> parts = asList(
			givenHttpRequestBodyPart("param", HttpRequestBodyString.of("HttpRequestBody", TEXT_PLAIN)),
			givenHttpRequestBodyPart("file", HttpRequestBodyFile.of(file))
		);

		HttpRequestBodyMultipart requestBody = new HttpRequestBodyMultipart(contentType, boundaries, parts);
		String result = toUtf8String(requestBody.getBody());

		assertThat(result).isEqualToNormalizingNewlines(String.join("\r\n", asList(
			"--123456789",
			"Content-Disposition: form-data; name=\"param\"",
			"Content-Type: text/plain",
			"",
			"HttpRequestBody",
			"--123456789",
			"Content-Disposition: form-data; name=\"file\"",
			"Content-Type: text/plain",
			"",
			"Content of file1.txt",
			"",
			"--123456789"
		)));
	}

	@Test
	void it_should_implement_to_string() {
		String contentType = "multipart/form-data";
		String boundaries = "123456789";
		HttpRequestBody body = mock(HttpRequestBody.class, "MockHttpRequestBody");
		HttpHeader header = HttpHeader.header("Content-Encoding", "gzip");
		HttpRequestBodyPart part = HttpRequestBodyPart.of(body, singleton(header));
		HttpRequestBodyMultipart requestBody = new HttpRequestBodyMultipart(contentType, boundaries, singleton(part));

		assertThat(requestBody).hasToString(
			"HttpRequestBodyMultipart{" +
				"contentType: \"multipart/form-data\", " +
				"boundaries: \"123456789\", " +
				"parts: [" +
					"HttpRequestBodyPart{" +
						"headers: {" +
							"content-encoding: HttpHeader{name: \"Content-Encoding\", values: [\"gzip\"]}" +
						"}, " +
						"body: MockHttpRequestBody" +
					"}" +
				"]" +
			"}"
		);
	}

	@Test
	void it_should_implement_equals_hash_code() {
		EqualsVerifier.forClass(HttpRequestBodyMultipart.class).verify();
	}

	private static HttpRequestBodyPart givenHttpRequestBodyPart(String name, HttpRequestBody body) {
		List<HttpHeader> headers = new ArrayList<>();
		headers.add(HttpHeader.of("Content-Disposition", "form-data; name=\"" + name + "\""));
		return HttpRequestBodyPart.of(body, headers);
	}
}
