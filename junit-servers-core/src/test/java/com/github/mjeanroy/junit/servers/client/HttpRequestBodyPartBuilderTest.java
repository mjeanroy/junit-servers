/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2019 <mickael.jeanroy@gmail.com>
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
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.Test;

import java.io.File;
import java.nio.file.Path;

import static com.github.mjeanroy.junit.servers.utils.commons.TestUtils.classpathFile;
import static com.github.mjeanroy.junit.servers.utils.commons.TestUtils.classpathPath;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

public class HttpRequestBodyPartBuilderTest {

	@Test
	public void it_should_create_part_from_raw_value() {
		final String rawValue = "123";
		final HttpRequestBodyPart part = HttpRequestBodyPartBuilder.of(rawValue).build();

		assertThat(part).isNotNull();
		assertThat(part.getHeaders()).isEmpty();
		assertThat(part.getBody()).isInstanceOf(HttpRequestBodyString.class);

		HttpRequestBodyString bodyString = (HttpRequestBodyString) part.getBody();
		assertThat(bodyString.getContentType()).isNull();
		assertThat(bodyString.getBodyString()).isEqualTo(rawValue);
	}

	@Test
	public void it_should_create_part_from_file_and_guess_content_type() {
		final File file = classpathFile("/file1.txt");
		final HttpRequestBodyPart part = HttpRequestBodyPartBuilder.of(file).build();

		assertThat(part).isNotNull();
		assertThat(part.getHeaders()).isEmpty();
		assertThat(part.getBody()).isInstanceOf(HttpRequestBodyFile.class);

		HttpRequestBodyFile bodyString = (HttpRequestBodyFile) part.getBody();
		assertThat(bodyString.getContentType()).isEqualTo("text/plain");
		assertThat(bodyString.getPath()).isEqualTo(file.toPath());
	}

	@Test
	public void it_should_create_part_from_path_and_guess_content_type() {
		final Path path = classpathPath("/file1.txt");
		final HttpRequestBodyPart part = HttpRequestBodyPartBuilder.of(path).build();

		assertThat(part).isNotNull();
		assertThat(part.getHeaders()).isEmpty();
		assertThat(part.getBody()).isInstanceOf(HttpRequestBodyFile.class);

		HttpRequestBodyFile bodyString = (HttpRequestBodyFile) part.getBody();
		assertThat(bodyString.getContentType()).isEqualTo("text/plain");
		assertThat(bodyString.getPath()).isEqualTo(path);
	}

	@Test
	public void it_should_create_part_from_given_request_body() {
		final HttpRequestBody body = HttpRequestBodyString.of("{}", "application/json");
		final HttpRequestBodyPart part = HttpRequestBodyPartBuilder.of(body).build();

		assertThat(part).isNotNull();
		assertThat(part.getHeaders()).isEmpty();
		assertThat(part.getBody()).isSameAs(body);
	}

	@Test
	public void it_should_create_part_with_header() {
		final HttpRequestBody body = HttpRequestBodyString.of("{}", "application/json");
		final String headerName = "Content-Disposition";
		final String headerValue = "form-data; name=\"foo\"";
		final HttpRequestBodyPart part = HttpRequestBodyPartBuilder.of(body).addHeader(headerName, headerValue).build();

		assertThat(part).isNotNull();
		assertThat(part.getBody()).isSameAs(body);
		assertThat(part.getHeaders()).hasSize(1)
			.extracting("name", "values")
			.contains(
				tuple(headerName, singletonList(headerValue))
			);
	}

	@Test
	public void it_should_create_part_with_header_object() {
		final HttpRequestBody body = HttpRequestBodyString.of("{}", "application/json");
		final String headerName = "Content-Disposition";
		final String headerValue = "form-data; name=\"foo\"";
		final HttpHeader header = HttpHeader.of(headerName, headerValue);
		final HttpRequestBodyPart part = HttpRequestBodyPartBuilder.of(body).addHeader(header).build();

		assertThat(part).isNotNull();
		assertThat(part.getBody()).isSameAs(body);
		assertThat(part.getHeaders()).hasSize(1)
			.extracting("name", "values")
			.contains(
				tuple(headerName, singletonList(headerValue))
			);
	}

	@Test
	public void it_should_set_part_to_form_data_part_with_given_name() {
		final HttpRequestBody body = HttpRequestBodyString.of("{}", "application/json");
		final String name = "foo";
		final HttpRequestBodyPart part = HttpRequestBodyPartBuilder.of(body).asFormData(name).build();

		assertThat(part).isNotNull();
		assertThat(part.getBody()).isSameAs(body);
		assertThat(part.getHeaders()).hasSize(1)
			.extracting("name", "values")
			.contains(
				tuple("Content-Disposition", singletonList("form-data; name=\"" + name + "\""))
			);
	}

	@Test
	public void it_should_set_part_to_form_data_part_with_given_name_and_filename() {
		final HttpRequestBody body = HttpRequestBodyString.of("{}", "application/json");
		final String name = "foo";
		final String filename = "request.json";
		final HttpRequestBodyPart part = HttpRequestBodyPartBuilder.of(body).asFormData(name, filename).build();

		assertThat(part).isNotNull();
		assertThat(part.getBody()).isSameAs(body);
		assertThat(part.getHeaders()).hasSize(1)
			.extracting("name", "values")
			.contains(
				tuple("Content-Disposition", singletonList("form-data; name=\"" + name + "\"; filename=\"" + filename + "\""))
			);
	}

	@Test
	public void it_should_set_part_to_form_data_part_with_given_name_being_escaped() {
		final HttpRequestBody body = HttpRequestBodyString.of("{}", "application/json");
		final String name = "foo\nbar\r\"quix\"";
		final HttpRequestBodyPart part = HttpRequestBodyPartBuilder.of(body).asFormData(name).build();

		assertThat(part).isNotNull();
		assertThat(part.getBody()).isSameAs(body);
		assertThat(part.getHeaders()).hasSize(1)
			.extracting("name", "values")
			.contains(
				tuple("Content-Disposition", singletonList("form-data; name=\"foo%0Abar%0D%22quix%22\""))
			);
	}

	@Test
	public void it_should_fail_to_create_part_with_content_type_header() {
		final HttpRequestBody body = HttpRequestBodyString.of("{}", "application/json");
		final HttpRequestBodyPartBuilder builder = HttpRequestBodyPartBuilder.of(body);
		final ThrowingCallable addHeader = new ThrowingCallable() {
			@Override
			public void call() {
				builder.addHeader("Content-Type", "application/json");
			}
		};

		assertThatThrownBy(addHeader)
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("Header 'Content-Type' is forbidden, please specify Content-Type wuth request body");
	}

	@Test
	public void it_should_set_part_to_form_data_part_with_given_name_and_filename_being_escaped() {
		final HttpRequestBody body = HttpRequestBodyString.of("{}", "application/json");
		final String name = "foo\rbar\n\"quix\"";
		final String filename = "request\r.\n\"json";
		final HttpRequestBodyPart part = HttpRequestBodyPartBuilder.of(body).asFormData(name, filename).build();

		assertThat(part).isNotNull();
		assertThat(part.getBody()).isSameAs(body);
		assertThat(part.getHeaders()).hasSize(1)
			.extracting("name", "values")
			.contains(
				tuple("Content-Disposition", singletonList("form-data; name=\"foo%0Dbar%0A%22quix%22\"; filename=\"request%0D.%0A%22json\""))
			);
	}

	@Test
	public void it_should_fail_to_create_part_with_header_added_twice() {
		final HttpRequestBody body = HttpRequestBodyString.of("{}", "application/json");
		final String headerName = "Content-Disposition";
		final String headerValue = "form-data; name=\"foo\"";
		final HttpRequestBodyPartBuilder builder = HttpRequestBodyPartBuilder.of(body).addHeader(headerName, headerValue);
		final ThrowingCallable addHeader = new ThrowingCallable() {
			@Override
			public void call() {
				builder.addHeader(headerName, headerValue);
			}
		};

		assertThatThrownBy(addHeader)
			.isInstanceOf(IllegalStateException.class)
			.hasMessage("Header 'Content-Disposition' is already defined");
	}

	@Test
	public void it_should_implement_to_string() {
		final String rawValue = "123";
		final HttpRequestBodyPartBuilder builder = HttpRequestBodyPartBuilder.of(rawValue).addHeader("Content-Transfer-Encoding", "binary");

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
	public void it_should_implement_equals_hash_code() {
		EqualsVerifier.forClass(HttpRequestBodyPartBuilder.class).verify();
	}
}
