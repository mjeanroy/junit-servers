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

import java.nio.charset.StandardCharsets;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singleton;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class HttpRequestBodyPartTest {

	@Test
	void it_should_create_body_part() {
		final HttpRequestBody bodyString = HttpRequestBodyString.of("1");
		final HttpRequestBodyPart part = HttpRequestBodyPart.of(bodyString);

		assertThat(part).isNotNull();
		assertThat(part.getHeaders()).isEmpty();
		assertThat(part.getBody()).isEqualTo(bodyString);
	}

	@Test
	void it_should_create_body_part_with_headers() {
		final HttpHeader header = HttpHeader.header("Content-Disposition", "form-data; name=\"file\"");
		final List<HttpHeader> httpHeaders = singletonList(header);
		final HttpRequestBody bodyString = HttpRequestBodyString.of("1");

		final HttpRequestBodyPart part = HttpRequestBodyPart.of(bodyString, httpHeaders);

		assertThat(part).isNotNull();
		assertThat(part.getHeaders()).hasSize(1).containsAll(httpHeaders);
		assertThat(part.getBody()).isEqualTo(bodyString);
	}

	@Test
	void it_should_create_body_part_with_single_header() {
		final HttpHeader header = HttpHeader.header("Content-Disposition", "form-data; name=\"file\"");
		final HttpRequestBody bodyString = HttpRequestBodyString.of("1");

		final HttpRequestBodyPart part = HttpRequestBodyPart.of(bodyString, header);

		assertThat(part).isNotNull();
		assertThat(part.getHeaders()).hasSize(1).containsOnly(header);
		assertThat(part.getBody()).isEqualTo(bodyString);
	}

	@Test
	void it_should_serialize_part() throws Exception {
		final HttpHeader header = HttpHeader.header("Content-Disposition", "form-data; name=\"file\"");
		final List<HttpHeader> httpHeaders = singletonList(header);
		final HttpRequestBody bodyString = HttpRequestBodyString.of("1");
		final HttpRequestBodyPart part = HttpRequestBodyPart.of(bodyString, httpHeaders);

		final String result = new String(part.serialize(), StandardCharsets.UTF_8);

		assertThat(result).isEqualTo(String.join("\r\n", asList(
			"Content-Disposition: form-data; name=\"file\"",
			"",
			"1"
		)));
	}

	@Test
	void it_should_serialize_part_with_a_content_type() throws Exception {
		final HttpHeader header = HttpHeader.header("Content-Disposition", "form-data; name=\"file\"");
		final List<HttpHeader> httpHeaders = singletonList(header);
		final String contentType = "text/plain";
		final String rawBody = "1";
		final HttpRequestBody bodyString = HttpRequestBodyString.of(rawBody, contentType);
		final HttpRequestBodyPart part = HttpRequestBodyPart.of(bodyString, httpHeaders);

		final String result = new String(part.serialize(), StandardCharsets.UTF_8);

		assertThat(result).isEqualTo(String.join("\r\n", asList(
			"Content-Disposition: form-data; name=\"file\"",
			"Content-Type: text/plain",
			"",
			"1"
		)));
	}

	@Test
	void it_should_implement_to_string() {
		final HttpRequestBody body = mock(HttpRequestBody.class, "MockHttpRequestBody");
		final HttpHeader header = HttpHeader.header("Content-Type", "text/plain");
		final HttpRequestBodyPart part = HttpRequestBodyPart.of(body, singleton(header));

		assertThat(part).hasToString(
			"HttpRequestBodyPart{" +
				"headers: {" +
					"content-type: HttpHeader{name: \"Content-Type\", values: [\"text/plain\"]}}, " +
					"body: MockHttpRequestBody" +
			"}"
		);
	}

	@Test
	void it_should_implement_equals_hash_code() {
		EqualsVerifier.forClass(HttpRequestBodyPart.class).verify();
	}
}
