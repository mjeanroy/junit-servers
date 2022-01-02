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

package com.github.mjeanroy.junit.servers.client.impl;

import com.github.mjeanroy.junit.servers.client.HttpHeader;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static com.github.mjeanroy.junit.servers.client.HttpHeader.header;
import static java.util.Collections.singleton;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class DefaultHttpResponseTest {

	@Test
	void it_should_create_http_response() {
		final long duration = 1000L;
		final int status = 200;
		final String body = "The response body";

		final HttpHeader header = header("Content-Type", "text/plain");
		final Set<HttpHeader> headers = singleton(header);

		final DefaultHttpResponse response = DefaultHttpResponse.of(duration, status, body, headers);

		assertThat(response).isNotNull();
		assertThat(response.getRequestDuration()).isEqualTo(duration);
		assertThat(response.status()).isEqualTo(status);
		assertThat(response.body()).isEqualTo(body);
		assertThat(response.getHeaders())
			.hasSize(1)
			.extracting("name", "values")
			.containsOnly(
				tuple(header.getName(), header.getValues())
			);
	}

	@Test
	void it_should_get_header_case_insensitively() {
		final long duration = 1000L;
		final int status = 200;
		final String body = "The response body";
		final HttpHeader header = header("Content-Type", "text/plain");
		final Set<HttpHeader> headers = singleton(header);

		final DefaultHttpResponse response = DefaultHttpResponse.of(duration, status, body, headers);

		assertThat(response.getHeader("Content-Type")).isEqualTo(header);
		assertThat(response.getHeader("content-type")).isEqualTo(header);
		assertThat(response.getHeader("CONTENT-TYPE")).isEqualTo(header);
	}

	@Test
	void it_should_implement_equals_hash_code() {
		EqualsVerifier.forClass(DefaultHttpResponse.class)
			.withRedefinedSuperclass()
			.withIgnoredFields("readResponseBodyLock", "_body")
			.verify();
	}

	@Test
	void it_should_implement_to_string() {
		long duration = 1000L;
		int status = 200;
		final String responseBody = "The response body";
		final Set<HttpHeader> headers = singleton(header("Content-Type", "text/plain"));
		final DefaultHttpResponse response = DefaultHttpResponse.of(duration, status, responseBody, headers);

		assertThat(response).hasToString(
			"DefaultHttpResponse{" +
				"duration: 1000, " +
				"status: 200, " +
				"body: \"The response body\", " +
				"headers: {" +
					"content-type: HttpHeader{name: \"Content-Type\", values: [\"text/plain\"]}" +
				"}" +
			"}"
		);
	}
}
