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

import org.junit.Test;

import java.nio.charset.Charset;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;

public class HttpRequestBodiesTest {

	@Test
	public void it_should_create_request_body() {
		final String rawBody = "{}";
		final HttpRequestBody body = HttpRequestBodies.requestBody(rawBody);
		assertThat(body).isNotNull();
		assertThat(body.getContentType()).isNull();
		assertThat(body.getBody()).isEqualTo(rawBody.getBytes(Charset.defaultCharset()));
	}

	@Test
	public void it_should_create_json_raw_body() {
		final String rawBody = "{}";
		final HttpRequestBody body = HttpRequestBodies.jsonBody(rawBody);
		assertThat(body).isNotNull();
		assertThat(body.getContentType()).isEqualTo("application/json");
		assertThat(body.getBody()).isEqualTo(rawBody.getBytes(Charset.defaultCharset()));
	}

	@Test
	public void it_should_create_xml_raw_body() {
		final String rawBody = "<person></person>";
		final HttpRequestBody body = HttpRequestBodies.xmlBody(rawBody);
		assertThat(body).isNotNull();
		assertThat(body.getContentType()).isEqualTo("application/xml");
		assertThat(body.getBody()).isEqualTo(rawBody.getBytes(Charset.defaultCharset()));
	}

	@Test
	public void it_should_create_text_raw_body() {
		final String rawBody = "test";
		final HttpRequestBody body = HttpRequestBodies.textBody(rawBody);
		assertThat(body).isNotNull();
		assertThat(body.getContentType()).isEqualTo("text/plain");
		assertThat(body.getBody()).isEqualTo(rawBody.getBytes(Charset.defaultCharset()));
	}

	@Test
	public void it_should_create_form_body_from_parameters() {
		final HttpRequestBody body = HttpRequestBodies.formUrlEncodedBody(asList(
			HttpParameter.of("id", "1"),
			HttpParameter.of("firstName", "John"),
			HttpParameter.of("lastName", "Doe")
		));

		assertThat(body).isNotNull();
		assertThat(body.getContentType()).isEqualTo("application/x-www-form-urlencoded");
		assertThat(body.getBody()).isEqualTo("id=1&firstName=John&lastName=Doe".getBytes(Charset.defaultCharset()));
	}

	@Test
	public void it_should_create_form_body_from_parameter_map() {
		final HttpRequestBody body = HttpRequestBodies.formUrlEncodedBody(singletonMap(
			"id", "1"
		));

		assertThat(body).isNotNull();
		assertThat(body.getContentType()).isEqualTo("application/x-www-form-urlencoded");
		assertThat(body.getBody()).isEqualTo("id=1".getBytes(Charset.defaultCharset()));
	}

	@Test
	public void it_should_create_form_body_builder() {
		final HttpRequestBody body = HttpRequestBodies.formBuilder()
			.add("id", "1")
			.add("firstName", "John")
			.add("lastName", "Doe")
			.build();

		assertThat(body).isNotNull();
		assertThat(body.getContentType()).isEqualTo("application/x-www-form-urlencoded");
		assertThat(body.getBody()).isEqualTo("id=1&firstName=John&lastName=Doe".getBytes(Charset.defaultCharset()));
	}
}
