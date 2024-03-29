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
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

class HttpRequestBodyStringTest {

	@Test
	void it_should_create_body_string_without_content_type() {
		String rawBody = "{}";
		HttpRequestBodyString bodyString = HttpRequestBodyString.of(rawBody);

		assertThat(bodyString.getContentType()).isNull();
		assertThat(bodyString.getBody()).isEqualTo(rawBody.getBytes(StandardCharsets.UTF_8));
	}

	@Test
	void it_should_create_body_with_content_type() {
		String contentType = "application/json";
		String rawBody = "{}";
		HttpRequestBodyString bodyString = HttpRequestBodyString.of(rawBody, contentType);

		assertThat(bodyString.getContentType()).isEqualTo(contentType);
		assertThat(bodyString.getBody()).isEqualTo(rawBody.getBytes(StandardCharsets.UTF_8));
	}

	@Test
	void it_should_implement_equals_and_hash_code() {
		EqualsVerifier.forClass(HttpRequestBodyString.class).verify();
	}

	@Test
	void it_should_implement_to_string() {
		String body = "{\"id\": 1}";
		String contentType = "application/json";
		HttpRequestBodyString bodyString = HttpRequestBodyString.of(body, contentType);
		assertThat(bodyString).hasToString(
			"HttpRequestBodyString{" +
				"contentType: \"application/json\", " +
				"body: \"{\"id\": 1}\"" +
			"}"
		);
	}
}
