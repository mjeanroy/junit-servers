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

import java.nio.charset.Charset;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

class HttpRequestBodyFormTest {

	@Test
	void it_should_create_form_body() {
		final List<HttpParameter> parameters = asList(
			HttpParameter.of("id", "1"),
			HttpParameter.of("name", "JohnDoe")
		);

		final HttpRequestBodyForm bodyForm = new HttpRequestBodyForm(parameters);

		assertThat(bodyForm.getContentType()).isEqualTo("application/x-www-form-urlencoded");
		assertThat(bodyForm.getBody()).isEqualTo("id=1&name=JohnDoe".getBytes(Charset.defaultCharset()));
		assertThat(bodyForm.getParameters()).isEqualTo(parameters);
	}

	@Test
	void it_should_create_form_body_with_url_encoded_param() {
		final List<HttpParameter> parameters = asList(
			HttpParameter.of("id", "1"),
			HttpParameter.of("full name", "John Doe")
		);

		final HttpRequestBodyForm bodyForm = new HttpRequestBodyForm(parameters);

		assertThat(bodyForm.getContentType()).isEqualTo("application/x-www-form-urlencoded");
		assertThat(bodyForm.getBody()).isEqualTo("id=1&full+name=John+Doe".getBytes(Charset.defaultCharset()));
		assertThat(bodyForm.getParameters()).isEqualTo(parameters);
	}

	@Test
	void it_should_implement_equals_and_hash_code() {
		EqualsVerifier.forClass(HttpRequestBodyForm.class).verify();
	}

	@Test
	void it_should_implement_to_string() {
		final List<HttpParameter> parameters = singletonList(
			HttpParameter.of("id", "1")
		);

		final HttpRequestBodyForm bodyForm = new HttpRequestBodyForm(parameters);

		assertThat(bodyForm).hasToString(
			"HttpRequestBodyForm{" +
				"parameters: [" +
					"HttpParameter{name: \"id\", value: \"1\"}" +
				"]" +
			"}"
		);
	}
}
