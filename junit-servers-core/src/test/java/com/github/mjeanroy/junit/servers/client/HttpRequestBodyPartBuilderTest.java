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
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

class HttpRequestBodyPartBuilderTest {

	@Test
	void it_should_create_part_from_raw_value() {
		String rawValue = "123";
		HttpRequestBodyPart part = HttpRequestBodyPartBuilder.of(rawValue).build();

		assertThat(part).isNotNull();
		assertThat(part.getHeaders()).isEmpty();
		assertThat(part.getBody()).isInstanceOf(HttpRequestBodyString.class);

		HttpRequestBodyString bodyString = (HttpRequestBodyString) part.getBody();
		assertThat(bodyString.getContentType()).isNull();
		assertThat(bodyString.getBodyString()).isEqualTo(rawValue);
	}

	@Test
	void it_should_create_part_from_file_and_guess_content_type() {
		File file = getFileFromClasspath("/file1.txt");
		HttpRequestBodyPart part = HttpRequestBodyPartBuilder.of(file).build();

		assertThat(part).isNotNull();
		assertThat(part.getHeaders()).isEmpty();
		assertThat(part.getBody()).isInstanceOf(HttpRequestBodyFile.class);

		HttpRequestBodyFile bodyString = (HttpRequestBodyFile) part.getBody();
		assertThat(bodyString.getContentType()).isEqualTo("text/plain");
		assertThat(bodyString.getPath()).isEqualTo(file.toPath());
	}

	@Test
	void it_should_create_part_from_path_and_guess_content_type() {
		Path path = getPathFromClasspath("/file1.txt");
		HttpRequestBodyPart part = HttpRequestBodyPartBuilder.of(path).build();

		assertThat(part).isNotNull();
		assertThat(part.getHeaders()).isEmpty();
		assertThat(part.getBody()).isInstanceOf(HttpRequestBodyFile.class);

		HttpRequestBodyFile bodyString = (HttpRequestBodyFile) part.getBody();
		assertThat(bodyString.getContentType()).isEqualTo("text/plain");
		assertThat(bodyString.getPath()).isEqualTo(path);
	}

	@Test
	void it_should_create_part_from_given_request_body() {
		HttpRequestBody body = HttpRequestBodyString.of("{}", "application/json");
		HttpRequestBodyPart part = HttpRequestBodyPartBuilder.of(body).build();

		assertThat(part).isNotNull();
		assertThat(part.getHeaders()).isEmpty();
		assertThat(part.getBody()).isSameAs(body);
	}

	@Test
	void it_should_create_part_with_header() {
		HttpRequestBody body = HttpRequestBodyString.of("{}", "application/json");
		String headerName = "Content-Disposition";
		String headerValue = "form-data; name=\"foo\"";
		HttpRequestBodyPart part = HttpRequestBodyPartBuilder.of(body).addHeader(headerName, headerValue).build();

		assertThat(part).isNotNull();
		assertThat(part.getBody()).isSameAs(body);
		assertThat(part.getHeaders()).hasSize(1)
			.extracting("name", "values")
			.contains(
				tuple(headerName, singletonList(headerValue))
			);
	}

	@Test
	void it_should_create_part_with_header_object() {
		HttpRequestBody body = HttpRequestBodyString.of("{}", "application/json");
		String headerName = "Content-Disposition";
		String headerValue = "form-data; name=\"foo\"";
		HttpHeader header = HttpHeader.of(headerName, headerValue);
		HttpRequestBodyPart part = HttpRequestBodyPartBuilder.of(body).addHeader(header).build();

		assertThat(part).isNotNull();
		assertThat(part.getBody()).isSameAs(body);
		assertThat(part.getHeaders()).hasSize(1)
			.extracting("name", "values")
			.contains(
				tuple(headerName, singletonList(headerValue))
			);
	}

	@Test
	void it_should_set_part_to_form_data_part_with_given_name() {
		HttpRequestBody body = HttpRequestBodyString.of("{}", "application/json");
		String name = "foo";
		HttpRequestBodyPart part = HttpRequestBodyPartBuilder.of(body).asFormData(name).build();

		assertThat(part).isNotNull();
		assertThat(part.getBody()).isSameAs(body);
		assertThat(part.getHeaders()).hasSize(1)
			.extracting("name", "values")
			.contains(
				tuple("Content-Disposition", singletonList("form-data; name=\"" + name + "\""))
			);
	}

	@Test
	void it_should_set_part_to_form_data_part_with_given_name_and_filename() {
		HttpRequestBody body = HttpRequestBodyString.of("{}", "application/json");
		String name = "foo";
		String filename = "request.json";
		HttpRequestBodyPart part = HttpRequestBodyPartBuilder.of(body).asFormData(name, filename).build();

		assertThat(part).isNotNull();
		assertThat(part.getBody()).isSameAs(body);
		assertThat(part.getHeaders()).hasSize(1)
			.extracting("name", "values")
			.contains(
				tuple("Content-Disposition", singletonList("form-data; name=\"" + name + "\"; filename=\"" + filename + "\""))
			);
	}

	@Test
	void it_should_set_part_to_form_data_part_with_given_name_being_escaped() {
		HttpRequestBody body = HttpRequestBodyString.of("{}", "application/json");
		String name = "foo\nbar\r\"quix\"";
		HttpRequestBodyPart part = HttpRequestBodyPartBuilder.of(body).asFormData(name).build();

		assertThat(part).isNotNull();
		assertThat(part.getBody()).isSameAs(body);
		assertThat(part.getHeaders()).hasSize(1)
			.extracting("name", "values")
			.contains(
				tuple("Content-Disposition", singletonList("form-data; name=\"foo%0Abar%0D%22quix%22\""))
			);
	}

	@Test
	void it_should_fail_to_create_part_with_content_type_header() {
		HttpRequestBody body = HttpRequestBodyString.of("{}", "application/json");
		HttpRequestBodyPartBuilder builder = HttpRequestBodyPartBuilder.of(body);

		assertThatThrownBy(() -> builder.addHeader("Content-Type", "application/json"))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("Header 'Content-Type' is forbidden, please specify Content-Type wuth request body");
	}

	@Test
	void it_should_set_part_to_form_data_part_with_given_name_and_filename_being_escaped() {
		HttpRequestBody body = HttpRequestBodyString.of("{}", "application/json");
		String name = "foo\rbar\n\"quix\"";
		String filename = "request\r.\n\"json";
		HttpRequestBodyPart part = HttpRequestBodyPartBuilder.of(body).asFormData(name, filename).build();

		assertThat(part).isNotNull();
		assertThat(part.getBody()).isSameAs(body);
		assertThat(part.getHeaders()).hasSize(1)
			.extracting("name", "values")
			.contains(
				tuple("Content-Disposition", singletonList("form-data; name=\"foo%0Dbar%0A%22quix%22\"; filename=\"request%0D.%0A%22json\""))
			);
	}

	@Test
	void it_should_fail_to_create_part_with_header_added_twice() {
		HttpRequestBody body = HttpRequestBodyString.of("{}", "application/json");
		String headerName = "Content-Disposition";
		String headerValue = "form-data; name=\"foo\"";
		HttpRequestBodyPartBuilder builder = HttpRequestBodyPartBuilder.of(body).addHeader(headerName, headerValue);

		assertThatThrownBy(() -> builder.addHeader(headerName, headerValue))
			.isInstanceOf(IllegalStateException.class)
			.hasMessage("Header 'Content-Disposition' is already defined");
	}

	@Test
	void it_should_implement_to_string() {
		String rawValue = "123";
		HttpRequestBodyPartBuilder builder = HttpRequestBodyPartBuilder.of(rawValue).addHeader("Content-Transfer-Encoding", "binary");

		assertThat(builder).hasToString(
			"HttpRequestBodyPartBuilder{" +
				"body: HttpRequestBodyString{" +
					"contentType: null, " +
					"body: \"123\"" +
				"}, " +
				"headers: {" +
					"content-transfer-encoding: HttpHeader{name: \"Content-Transfer-Encoding\", values: [\"binary\"]}" +
				"}" +
			"}"
		);
	}

	@Test
	void it_should_implement_equals_hash_code() {
		EqualsVerifier.forClass(HttpRequestBodyPartBuilder.class).verify();
	}
}
