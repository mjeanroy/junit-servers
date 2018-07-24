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

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.TimeZone;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class CookieTest {

	private TimeZone tz;

	@Before
	public void setUp() {
		tz = TimeZone.getDefault();
		TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
	}

	@After
	public void tearDown() {
		TimeZone.setDefault(tz);
	}

	@Test
	public void it_should_default_create_cookie() {
		final String name = "foo";
		final String value = "bar";

		@SuppressWarnings("deprecation")
		Cookie cookie = Cookie.cookie(name, value);

		assertThat(cookie).isNotNull();
		assertThat(cookie.getName()).isEqualTo(name);
		assertThat(cookie.getValue()).isEqualTo(value);
		assertThat(cookie.getExpires()).isNull();
		assertThat(cookie.getMaxAge()).isNull();
		assertThat(cookie.getDomain()).isNull();
		assertThat(cookie.getPath()).isNull();
		assertThat(cookie.isSecure()).isFalse();
		assertThat(cookie.isHttpOnly()).isFalse();
	}

	@Test
	public void it_should_create_cookie() {
		final String name = "foo";
		final String value = "bar";
		final long expires = 0;
		final long maxAge = 10;
		final String domain = "domain";
		final String path = "path";
		final boolean secure = true;
		final boolean httpOnly = false;

		@SuppressWarnings("deprecation")
		Cookie cookie = Cookie.cookie(name, value, domain, path, expires, maxAge, secure, httpOnly);

		assertThat(cookie).isNotNull();
		assertThat(cookie.getName()).isEqualTo(name);
		assertThat(cookie.getValue()).isEqualTo(value);
		assertThat(cookie.getExpires()).isEqualTo(expires);
		assertThat(cookie.getMaxAge()).isEqualTo(maxAge);
		assertThat(cookie.getDomain()).isEqualTo(domain);
		assertThat(cookie.getPath()).isEqualTo(path);
		assertThat(cookie.isSecure()).isEqualTo(secure);
		assertThat(cookie.isHttpOnly()).isEqualTo(httpOnly);
	}

	@Test
	public void it_should_create_secure_cookie() {
		final String name = "foo";
		final String value = "bar";
		final long expires = 0;
		final long maxAge = 10;
		final String domain = "domain";
		final String path = "path";

		@SuppressWarnings("deprecation")
		Cookie cookie = Cookie.secureCookie(name, value, domain, path, expires, maxAge);

		assertThat(cookie).isNotNull();
		assertThat(cookie.getName()).isEqualTo(name);
		assertThat(cookie.getValue()).isEqualTo(value);
		assertThat(cookie.getExpires()).isEqualTo(expires);
		assertThat(cookie.getMaxAge()).isEqualTo(maxAge);
		assertThat(cookie.getDomain()).isEqualTo(domain);
		assertThat(cookie.getPath()).isEqualTo(path);
		assertThat(cookie.isSecure()).isTrue();
		assertThat(cookie.isHttpOnly()).isTrue();
	}

	@Test
	public void it_should_create_session_cookie() {
		final String name = "foo";
		final String value = "bar";
		final String domain = "domain";
		final String path = "path";

		@SuppressWarnings("deprecation")
		Cookie cookie = Cookie.sessionCookie(name, value, domain, path);

		assertThat(cookie).isNotNull();
		assertThat(cookie.getName()).isEqualTo(name);
		assertThat(cookie.getValue()).isEqualTo(value);
		assertThat(cookie.getExpires()).isEqualTo(-1L);
		assertThat(cookie.getMaxAge()).isZero();
		assertThat(cookie.getDomain()).isEqualTo(domain);
		assertThat(cookie.getPath()).isEqualTo(path);
		assertThat(cookie.isSecure()).isTrue();
		assertThat(cookie.isHttpOnly()).isTrue();
	}

	@Test
	public void it_should_create_cookie_with_name_and_value() {
		@SuppressWarnings("deprecation")
		Cookie cookie = Cookie.read("name=value");

		assertThat(cookie).isNotNull();
		assertThat(cookie.getName()).isEqualTo("name");
		assertThat(cookie.getValue()).isEqualTo("value");
		assertThat(cookie.getDomain()).isNull();
		assertThat(cookie.getPath()).isNull();
		assertThat(cookie.isSecure()).isFalse();
		assertThat(cookie.isHttpOnly()).isFalse();
		assertThat(cookie.getExpires()).isNull();
		assertThat(cookie.getMaxAge()).isZero();
	}

	@Test
	public void it_should_create_cookie_with_name_value_domain_path_and_flags() {
		@SuppressWarnings("deprecation")
		Cookie cookie = Cookie.read("name=value; Domain=foo.com; Path=/; Secure; HttpOnly");

		assertThat(cookie).isNotNull();
		assertThat(cookie.getName()).isEqualTo("name");
		assertThat(cookie.getValue()).isEqualTo("value");
		assertThat(cookie.getDomain()).isEqualTo("foo.com");
		assertThat(cookie.getPath()).isEqualTo("/");
		assertThat(cookie.isSecure()).isTrue();
		assertThat(cookie.isHttpOnly()).isTrue();
		assertThat(cookie.getExpires()).isNull();
		assertThat(cookie.getMaxAge()).isZero();
	}

	@Test
	public void it_should_create_cookie_with_name_value_domain_path_expires_max_date_and_flags() {
		@SuppressWarnings("deprecation")
		Cookie cookie = Cookie.read("name=value; Domain=foo.com; Expires=Wed, 13-Jan-2021 22:23:01 GMT; max-age=3600; Path=/; Secure; HttpOnly");

		assertThat(cookie).isNotNull();
		assertThat(cookie.getName()).isEqualTo("name");
		assertThat(cookie.getValue()).isEqualTo("value");
		assertThat(cookie.getDomain()).isEqualTo("foo.com");
		assertThat(cookie.getPath()).isEqualTo("/");
		assertThat(cookie.isSecure()).isTrue();
		assertThat(cookie.isHttpOnly()).isTrue();
		assertThat(cookie.getExpires()).isEqualTo(1610576581000L);
		assertThat(cookie.getMaxAge()).isEqualTo(3600);
	}

	@Test
	public void it_should_not_create_cookie_without_name_value() {
		assertThatThrownBy(read("name; Domain=foo.com; Path=/; Secure; HttpOnly"))
			.isExactlyInstanceOf(IllegalArgumentException.class)
			.hasMessage("Cookie must have a valid name and a valid value");
	}

	@Test
	public void it_should_not_create_cookie_with_empty_name() {
		assertThatThrownBy(read("=value; Domain=foo.com; Path=/; Secure; HttpOnly"))
			.isExactlyInstanceOf(IllegalArgumentException.class)
			.hasMessage("Cookie must have a valid name");
	}

	@Test
	public void it_should_implement_to_string() {
		@SuppressWarnings("deprecation")
		Cookie cookie = Cookie.read("name=value; Domain=foo.com; Path=/; Secure; HttpOnly");

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
	public void it_should_create_simple_cookie_with_builder() {
		String name = "name";
		String value = "value";
		Cookie cookie = new Cookie.Builder(name, value).build();
		assertThat(cookie).isNotNull();
		assertThat(cookie.getName()).isEqualTo(name);
		assertThat(cookie.getValue()).isEqualTo(value);
	}

	@Test
	public void it_should_create_complex_cookie_with_builder() {
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
	public void it_should_create_complex_cookie_with_expires_date_with_builder() {
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
	public void it_should_implement_equals() {
		EqualsVerifier.forClass(Cookie.class)
			.suppress(Warning.STRICT_INHERITANCE)
			.verify();
	}

	private static ThrowingCallable read(final String rawValue) {
		return new ThrowingCallable() {
			@Override
			@SuppressWarnings("deprecation")
			public void call() {
				Cookie.read(rawValue);
			}
		};
	}
}
