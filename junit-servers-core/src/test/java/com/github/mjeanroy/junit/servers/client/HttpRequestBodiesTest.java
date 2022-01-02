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

import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.github.mjeanroy.junit.servers.utils.commons.TestUtils.classpathFile;
import static com.github.mjeanroy.junit.servers.utils.commons.TestUtils.classpathPath;
import static com.github.mjeanroy.junit.servers.utils.commons.TestUtils.toUtf8String;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;

class HttpRequestBodiesTest {

	@Test
	void it_should_create_request_body() throws Exception {
		final String rawBody = "{}";
		final HttpRequestBody body = HttpRequestBodies.requestBody(rawBody);
		assertThat(body).isNotNull();
		assertThat(body.getContentType()).isNull();
		assertThat(toUtf8String(body.getBody())).isEqualTo(rawBody);
	}

	@Test
	void it_should_create_json_raw_body() throws Exception {
		final String rawBody = "{}";
		final HttpRequestBody body = HttpRequestBodies.jsonBody(rawBody);
		assertThat(body).isNotNull();
		assertThat(body.getContentType()).isEqualTo("application/json");
		assertThat(toUtf8String(body.getBody())).isEqualTo(rawBody);
	}

	@Test
	void it_should_create_xml_raw_body() throws Exception {
		final String rawBody = "<person></person>";
		final HttpRequestBody body = HttpRequestBodies.xmlBody(rawBody);
		assertThat(body).isNotNull();
		assertThat(body.getContentType()).isEqualTo("application/xml");
		assertThat(toUtf8String(body.getBody())).isEqualTo(rawBody);
	}

	@Test
	void it_should_create_text_raw_body() throws Exception {
		final String rawBody = "test";
		final HttpRequestBody body = HttpRequestBodies.textBody(rawBody);
		assertThat(body).isNotNull();
		assertThat(body.getContentType()).isEqualTo("text/plain");
		assertThat(toUtf8String(body.getBody())).isEqualTo(rawBody);
	}

	@Test
	void it_should_create_body_file_and_guess_content_type() throws Exception {
		final File file = classpathFile("/file1.txt");
		final HttpRequestBody body = HttpRequestBodies.fileBody(file);
		assertThat(body).isNotNull();
		assertThat(body.getContentType()).isEqualTo("text/plain");
		assertThat(toUtf8String(body.getBody())).isEqualToIgnoringNewLines(
			"Content of file1.txt"
		);
	}

	@Test
	void it_should_create_body_file_with_content_type() throws Exception {
		final File file = classpathFile("/file1.txt");
		final String contentType = "text/plain";
		final HttpRequestBody body = HttpRequestBodies.fileBody(file, contentType);
		assertThat(body).isNotNull();
		assertThat(body.getContentType()).isEqualTo(contentType);
		assertThat(toUtf8String(body.getBody())).isEqualToIgnoringNewLines(
			"Content of file1.txt"
		);
	}

	@Test
	void it_should_create_body_file_from_path_and_guess_content_type() throws Exception {
		final Path path = classpathPath("/file1.txt");
		final HttpRequestBody body = HttpRequestBodies.fileBody(path);
		assertThat(body).isNotNull();
		assertThat(body.getContentType()).isEqualTo("text/plain");
		assertThat(toUtf8String(body.getBody())).isEqualToIgnoringNewLines(
			"Content of file1.txt"
		);
	}

	@Test
	void it_should_create_body_file_from_path_with_content_type() throws Exception {
		final Path path = classpathPath("/file1.txt");
		final String contentType = "text/plain";
		final HttpRequestBody body = HttpRequestBodies.fileBody(path, contentType);
		assertThat(body).isNotNull();
		assertThat(body.getContentType()).isEqualTo(contentType);
		assertThat(toUtf8String(body.getBody())).isEqualToIgnoringNewLines(
			"Content of file1.txt"
		);
	}

	@Test
	void it_should_create_body_file_with_jpeg_content_type() throws Exception {
		final File file = classpathFile("/img1.jpg");
		final HttpRequestBody body = HttpRequestBodies.jpeg(file);
		assertThat(body).isNotNull();
		assertThat(body.getContentType()).isEqualTo("image/jpeg");
		assertThat(body.getBody()).isEqualTo(Files.readAllBytes(file.toPath()));
	}

	@Test
	void it_should_create_body_file_with_png_content_type() throws Exception {
		final File file = classpathFile("/img2.png");
		final HttpRequestBody body = HttpRequestBodies.png(file);
		assertThat(body).isNotNull();
		assertThat(body.getContentType()).isEqualTo("image/png");
		assertThat(body.getBody()).isEqualTo(Files.readAllBytes(file.toPath()));
	}

	@Test
	void it_should_create_body_file_from_path_with_png_content_type() throws Exception {
		final Path path = classpathPath("/img2.png");
		final HttpRequestBody body = HttpRequestBodies.png(path);
		assertThat(body).isNotNull();
		assertThat(body.getContentType()).isEqualTo("image/png");
		assertThat(body.getBody()).isEqualTo(Files.readAllBytes(path));
	}


	@Test
	void it_should_create_body_file_with_pdf_content_type() throws Exception {
		final File file = classpathFile("/file1.pdf");
		final HttpRequestBody body = HttpRequestBodies.pdf(file);
		assertThat(body).isNotNull();
		assertThat(body.getContentType()).isEqualTo("application/pdf");
		assertThat(body.getBody()).isEqualTo(Files.readAllBytes(file.toPath()));
	}

	@Test
	void it_should_create_body_file_from_path_with_pdf_content_type() throws Exception {
		final Path path = classpathPath("/file1.pdf");
		final HttpRequestBody body = HttpRequestBodies.pdf(path);
		assertThat(body).isNotNull();
		assertThat(body.getContentType()).isEqualTo("application/pdf");
		assertThat(body.getBody()).isEqualTo(Files.readAllBytes(path));
	}

	@Test
	void it_should_create_form_body_from_parameters() throws Exception {
		final HttpRequestBody body = HttpRequestBodies.formUrlEncodedBody(asList(
			HttpParameter.of("id", "1"),
			HttpParameter.of("firstName", "John"),
			HttpParameter.of("lastName", "Doe")
		));

		assertThat(body).isNotNull();
		assertThat(body.getContentType()).isEqualTo("application/x-www-form-urlencoded");
		assertThat(toUtf8String(body.getBody())).isEqualTo("id=1&firstName=John&lastName=Doe");
	}

	@Test
	void it_should_create_form_body_from_parameter_map() throws Exception {
		final HttpRequestBody body = HttpRequestBodies.formUrlEncodedBody(singletonMap(
			"id", "1"
		));

		assertThat(body).isNotNull();
		assertThat(body.getContentType()).isEqualTo("application/x-www-form-urlencoded");
		assertThat(toUtf8String(body.getBody())).isEqualTo("id=1");
	}

	@Test
	void it_should_create_form_body_builder() throws Exception {
		final HttpRequestBody body = HttpRequestBodies.formBuilder()
			.add("id", "1")
			.add("firstName", "John")
			.add("lastName", "Doe")
			.build();

		assertThat(body).isNotNull();
		assertThat(body.getContentType()).isEqualTo("application/x-www-form-urlencoded");
		assertThat(toUtf8String(body.getBody())).isEqualTo("id=1&firstName=John&lastName=Doe");
	}

	@Test
	void it_should_create_multipart_body_builder() throws Exception {
		final HttpRequestBody body = HttpRequestBodies.multipartBuilder()
			.addFormDataPart("id", "1")
			.addFormDataPart("firstName", "John")
			.addFormDataPart("lastName", "Doe")
			.build();

		assertThat(body).isNotNull();
		assertThat(body.getContentType()).isEqualTo("multipart/form-data; boundary=---------------------------974767299852498929531610575");
		assertThat(toUtf8String(body.getBody())).isEqualTo(String.join("\r\n", asList(
			"-----------------------------974767299852498929531610575",
			"Content-Disposition: form-data; name=\"id\"",
			"",
			"1",
			"-----------------------------974767299852498929531610575",
			"Content-Disposition: form-data; name=\"firstName\"",
			"",
			"John",
			"-----------------------------974767299852498929531610575",
			"Content-Disposition: form-data; name=\"lastName\"",
			"",
			"Doe",
			"-----------------------------974767299852498929531610575"
		)));
	}
}
