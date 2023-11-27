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
import java.nio.file.Path;

import static com.github.mjeanroy.junit.servers.testing.IoTestUtils.getFileFromClasspath;
import static com.github.mjeanroy.junit.servers.testing.IoTestUtils.getPathFromClasspath;
import static com.github.mjeanroy.junit.servers.utils.commons.TestUtils.toUtf8String;
import static org.assertj.core.api.Assertions.assertThat;

class HttpRequestBodyFileTest {

	@Test
	void it_should_create_body_file_from_file_and_guess_content_type() {
		final File file = getFileFromClasspath("/file1.txt");
		final HttpRequestBodyFile bodyFile = HttpRequestBodyFile.of(file);

		assertThat(bodyFile).isNotNull();
		assertThat(bodyFile.getContentType()).isEqualTo("text/plain");
		assertThat(bodyFile.getPath()).isEqualTo(file.toPath());
		assertThat(bodyFile.getFilename()).isEqualTo("file1.txt");
	}

	@Test
	void it_should_create_body_file_from_file_with_content_type() {
		final File file = getFileFromClasspath("/file1.txt");
		final String contentType = "text/plain";
		final HttpRequestBodyFile bodyFile = HttpRequestBodyFile.of(file, contentType);

		assertThat(bodyFile).isNotNull();
		assertThat(bodyFile.getContentType()).isEqualTo(contentType);
		assertThat(bodyFile.getPath()).isEqualTo(file.toPath());
		assertThat(bodyFile.getFilename()).isEqualTo("file1.txt");
	}

	@Test
	void it_should_create_body_file_from_path_and_guess_content_type() {
		final Path path = getPathFromClasspath("/file1.txt");
		final HttpRequestBodyFile bodyFile = HttpRequestBodyFile.of(path);

		assertThat(bodyFile).isNotNull();
		assertThat(bodyFile.getContentType()).isEqualTo("text/plain");
		assertThat(bodyFile.getPath()).isEqualTo(path);
		assertThat(bodyFile.getFilename()).isEqualTo("file1.txt");
	}

	@Test
	void it_should_create_body_file_from_path_with_content_type() {
		final Path path = getPathFromClasspath("/file1.txt");
		final String contentType = "text/plain";
		final HttpRequestBodyFile bodyFile = HttpRequestBodyFile.of(path, contentType);

		assertThat(bodyFile).isNotNull();
		assertThat(bodyFile.getContentType()).isEqualTo(contentType);
		assertThat(bodyFile.getPath()).isEqualTo(path);
		assertThat(bodyFile.getFilename()).isEqualTo("file1.txt");
	}

	@Test
	void it_should_serialize_body() throws Exception {
		final Path path = getPathFromClasspath("/file1.txt");
		final HttpRequestBodyFile bodyFile = HttpRequestBodyFile.of(path);

		assertThat(toUtf8String(bodyFile.getBody())).isEqualToIgnoringNewLines(
			"Content of file1.txt"
		);
	}

	@Test
	void it_should_implement_to_string() {
		final Path path = getPathFromClasspath("/file1.txt");
		final HttpRequestBodyFile bodyFile = HttpRequestBodyFile.of(path);
		assertThat(bodyFile).hasToString(
			"HttpRequestBodyFile{" +
				"contentType: \"text/plain\", " +
				"path: " + path.toString() +
			"}"
		);
	}

	@Test
	void it_should_implement_equals_hash_code() {
		EqualsVerifier.forClass(HttpRequestBodyFile.class).verify();
	}
}
