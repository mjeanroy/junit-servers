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
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.TimeZone;

import static org.assertj.core.api.Assertions.assertThat;

class CookieTest {

	private TimeZone tz;

	@BeforeEach
	void setUp() {
		tz = TimeZone.getDefault();
		TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
	}

	@AfterEach
	void tearDown() {
		TimeZone.setDefault(tz);
	}

	@Test
	void it_should_implement_to_string() {
		String name = "name";
		String value = "value";
		String domain = "foo.com";
		String path = "/";
		boolean secure = true;
		boolean httpOnly = true;
		long maxAge = 0;
		Cookie cookie = new Cookie.Builder(name, value)
			.domain(domain)
			.path(path)
			.secure(secure)
			.httpOnly(httpOnly)
			.maxAge(maxAge)
			.build();

		assertThat(cookie.toString()).isEqualTo(
			"Cookie{" +
				"name: \"name\", " +
				"value: \"value\", " +
				"domain: \"foo.com\", " +
				"path: \"/\", " +
				"expires: null, " +
				"maxAge: 0, " +
				"secure: true, " +
				"httpOnly: true" +
			"}"
		);
	}

	@Test
	void it_should_create_simple_cookie_with_builder() {
		String name = "name";
		String value = "value";
		Cookie cookie = new Cookie.Builder(name, value).build();

		assertThat(cookie).isNotNull();
		assertThat(cookie.getName()).isEqualTo(name);
		assertThat(cookie.getValue()).isEqualTo(value);
	}

	@Test
	void it_should_create_complex_cookie_with_builder() {
		String name = "name";
		String value = "value";
		String domain = "domain";
		String path = "path";
		boolean secure = true;
		boolean httpOnly = true;
		long maxAge = 3600;

		Cookie cookie = new Cookie.Builder(name, value)
			.domain(domain)
			.path(path)
			.secure(secure)
			.httpOnly(httpOnly)
			.maxAge(maxAge)
			.build();

		assertThat(cookie).isNotNull();
		assertThat(cookie.getName()).isEqualTo(name);
		assertThat(cookie.getValue()).isEqualTo(value);
		assertThat(cookie.getDomain()).isEqualTo(domain);
		assertThat(cookie.getPath()).isEqualTo(path);
		assertThat(cookie.isSecure()).isEqualTo(secure);
		assertThat(cookie.isHttpOnly()).isEqualTo(httpOnly);
		assertThat(cookie.getMaxAge()).isEqualTo(maxAge);
		assertThat(cookie.getExpires()).isNull();
	}

	@Test
	void it_should_create_complex_cookie_with_expires_date_with_builder() {
		String name = "name";
		String value = "value";
		String domain = "domain";
		String path = "path";
		boolean secure = true;
		boolean httpOnly = true;
		long maxAge = 3600;

		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) + 1);
		long expires = cal.getTimeInMillis();

		Cookie cookie = new Cookie.Builder(name, value)
			.domain(domain)
			.path(path)
			.secure(secure)
			.httpOnly(httpOnly)
			.maxAge(maxAge)
			.expires(expires)
			.build();

		assertThat(cookie).isNotNull();
		assertThat(cookie.getName()).isEqualTo(name);
		assertThat(cookie.getValue()).isEqualTo(value);
		assertThat(cookie.getDomain()).isEqualTo(domain);
		assertThat(cookie.getPath()).isEqualTo(path);
		assertThat(cookie.isSecure()).isEqualTo(secure);
		assertThat(cookie.isHttpOnly()).isEqualTo(httpOnly);
		assertThat(cookie.getMaxAge()).isEqualTo(maxAge);
		assertThat(cookie.getExpires()).isEqualTo(expires);
	}

	@Test
	void it_should_implement_equals() {
		EqualsVerifier.forClass(Cookie.class)
			.suppress(Warning.STRICT_INHERITANCE)
			.verify();
	}
}
