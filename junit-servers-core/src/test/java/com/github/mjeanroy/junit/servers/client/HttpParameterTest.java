/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2018 <mickael.jeanroy@gmail.com>
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

import static com.github.mjeanroy.junit.servers.utils.commons.TestUtils.urlEncode;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

public class HttpParameterTest {

	@Test
	public void it_should_create_http_parameter() {
		final String name = "foo";
		final String value = "bar";

		final HttpParameter parameter = HttpParameter.param(name, value);

		assertThat(parameter.getName()).isEqualTo(name);
		assertThat(parameter.getValue()).isEqualTo(value);
	}

	@Test
	public void it_should_get_url_encoded_name() {
		final String name = "foo bar";
		final String value = "bar";

		final HttpParameter parameter = HttpParameter.param(name, value);

		assertThat(parameter.getName()).isEqualTo(name);
		assertThat(parameter.getEncodedName()).isEqualTo(urlEncode(name));
	}

	@Test
	public void it_should_get_url_encoded_value() {
		final String name = "foo";
		final String value = "foo bar";

		final HttpParameter parameter = HttpParameter.param(name, value);

		assertThat(parameter.getValue()).isEqualTo(value);
		assertThat(parameter.getEncodedValue()).isEqualTo(urlEncode(value));
	}

	@Test
	public void it_should_implement_to_string() {
		final String name = "foo";
		final String value = "bar";
		final HttpParameter parameter = HttpParameter.param(name, value);
		assertThat(parameter.toString()).isEqualTo("HttpParameter{name: \"foo\", value: \"bar\"}");
	}

	@Test
	public void it_should_implement_equals() {
		EqualsVerifier.forClass(HttpParameter.class)
			.suppress(Warning.STRICT_INHERITANCE)
			.verify();
	}
}
