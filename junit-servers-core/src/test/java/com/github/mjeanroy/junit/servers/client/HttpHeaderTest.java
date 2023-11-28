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

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

class HttpHeaderTest {

	@Test
	void it_should_create_http_header_of_single_value() {
		String name = "foo1";
		String value = "bar1";
		HttpHeader header = HttpHeader.of(name, value);

		verifyHeaderWithSingleValue(name, value, header);
	}

	@Test
	void it_should_create_http_header_off_several_value() {
		String name = "foo1";
		String v1 = "bar11";
		String v2 = "bar12";
		HttpHeader header = HttpHeader.header(name, asList(v1, v2));

		verifyHeaderWithValues(name, v1, v2, header);
	}

	@Test
	void it_should_create_http_header_with_single_value() {
		String name = "foo2";
		String value = "bar2";
		HttpHeader header = HttpHeader.header(name, value);

		verifyHeaderWithSingleValue(name, value, header);
	}

	@Test
	void it_should_create_http_header_with_several_value() {
		String name = "foo2";
		String v1 = "bar21";
		String v2 = "bar22";
		HttpHeader header = HttpHeader.header(name, asList(v1, v2));

		verifyHeaderWithValues(name, v1, v2, header);
	}

	@Test
	void it_should_implement_to_string_with_single_value() {
		String name = "foo";
		String value = "bar";
		HttpHeader parameter = HttpHeader.header(name, value);

		assertThat(parameter.toString()).isEqualTo(
			"HttpHeader{" +
				"name: \"foo\", " +
				"values: [\"bar\"]" +
			"}"
		);
	}

	@Test
	void it_should_implement_to_string_with_several_value() {
		String name = "foo";
		String v1 = "bar1";
		String v2 = "bar2";
		HttpHeader parameter = HttpHeader.header(name, asList(v1, v2));

		assertThat(parameter.toString()).isEqualTo(
			"HttpHeader{" +
				"name: \"foo\", " +
				"values: [\"bar1\", \"bar2\"]" +
			"}"
		);
	}

	@Test
	void it_should_implement_equals() {
		EqualsVerifier.forClass(HttpHeader.class).verify();
	}

	private static void verifyHeaderWithSingleValue(String name, String value, HttpHeader header) {
		assertThat(header.getName()).isEqualTo(name);
		assertThat(header.getValues()).isEqualTo(singletonList(value));
		assertThat(header.getFirstValue()).isEqualTo(value);
		assertThat(header.getLastValue()).isEqualTo(value);
	}

	private void verifyHeaderWithValues(String name, String firstValue, String lastValue, HttpHeader header) {
		assertThat(header.getName()).isEqualTo(name);
		assertThat(header.getValues()).isEqualTo(asList(firstValue, lastValue));
		assertThat(header.getFirstValue()).isEqualTo(firstValue);
		assertThat(header.getLastValue()).isEqualTo(lastValue);
	}
}
