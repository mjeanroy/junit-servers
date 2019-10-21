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
import org.junit.jupiter.api.Test;

import static java.util.Collections.singleton;
import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class HttpRequestBodyFormBuilderTest {

	@Test
	void it_should_create_form_body_from_single_parameter() {
		final HttpRequestBody bodyForm = new HttpRequestBodyFormBuilder().add("id", "1").build();
		assertThat(bodyForm).isInstanceOf(HttpRequestBodyForm.class);
		assertThat(((HttpRequestBodyForm) bodyForm).getParameters()).hasSize(1)
			.extracting("name", "value")
			.contains(
				tuple("id", "1")
			);
	}

	@Test
	void it_should_create_form_body_from_single_parameter_object() {
		final HttpRequestBody bodyForm = new HttpRequestBodyFormBuilder().add(HttpParameter.of("id", "1")).build();
		assertThat(bodyForm).isInstanceOf(HttpRequestBodyForm.class);
		assertThat(((HttpRequestBodyForm) bodyForm).getParameters()).hasSize(1)
			.extracting("name", "value")
			.contains(
				tuple("id", "1")
			);
	}

	@Test
	void it_should_create_form_body_from_parameter_map() {
		final HttpRequestBody bodyForm = new HttpRequestBodyFormBuilder().addAll(singletonMap("id", "1")).build();
		assertThat(bodyForm).isInstanceOf(HttpRequestBodyForm.class);
		assertThat(((HttpRequestBodyForm) bodyForm).getParameters()).hasSize(1)
			.extracting("name", "value")
			.contains(
				tuple("id", "1")
			);
	}

	@Test
	void it_should_create_form_body_from_parameters() {
		final HttpRequestBody bodyForm = new HttpRequestBodyFormBuilder().addAll(singleton(HttpParameter.of("id", "1"))).build();
		assertThat(bodyForm).isInstanceOf(HttpRequestBodyForm.class);
		assertThat(((HttpRequestBodyForm) bodyForm).getParameters()).hasSize(1)
			.extracting("name", "value")
			.contains(
				tuple("id", "1")
			);
	}

	@Test
	void it_should_implement_equals_and_hash_code() {
		EqualsVerifier.forClass(HttpRequestBodyFormBuilder.class).verify();
	}

	@Test
	void it_should_implement_to_string() {
		final HttpRequestBodyFormBuilder builder = new HttpRequestBodyFormBuilder()
			.add("id", "1")
			.add("name", "John Doe");

		assertThat(builder).hasToString(
			"HttpRequestBodyFormBuilder{" +
				"parameters: {" +
					"id: HttpParameter{" +
						"name: \"id\", " +
						"value: \"1\"" +
					"}, " +
					"name: HttpParameter{" +
						"name: \"name\", " +
						"value: \"John Doe\"" +
					"}" +
				"}" +
			"}"
		);
	}
}
