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

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.Test;

import static java.util.Arrays.asList;
import static java.util.Collections.singleton;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class CookiesTest {

	@Test
	public void it_should_serialize_single_cookie() {
		String name = "foo";
		String value = "bar";
		Cookie cookie = Cookies.cookie(name, value);

		String result = Cookies.serialize(singleton(cookie));

		assertThat(result)
			.isNotNull()
			.isNotEmpty()
			.isEqualTo(formatCookie(name, value));
	}

	@Test
	public void it_should_serialize_list_of_cookies() {
		String n1 = "f1";
		String v1 = "b1";
		Cookie c1 = Cookies.cookie(n1, v1);

		String n2 = "f2";
		String v2 = "b2";
		Cookie c2 = Cookies.cookie(n2, v2);

		String result = Cookies.serialize(asList(c1, c2));

		assertThat(result)
			.isNotNull()
			.isNotEmpty()
			.isEqualTo(formatCookie(n1, v1) + "; " + formatCookie(n2, v2));
	}

	@Test
	public void it_should_default_create_cookie() {
		final String name = "foo";
		final String value = "bar";

		Cookie cookie = Cookies.cookie(name, value);

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

		Cookie cookie = Cookies.cookie(name, value, domain, path, expires, maxAge, secure, httpOnly);

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

		Cookie cookie = Cookies.secureCookie(name, value, domain, path, expires, maxAge);

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

		Cookie cookie = Cookies.sessionCookie(name, value, domain, path);

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
		Cookie cookie = Cookies.read("name=value");

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
		Cookie cookie = Cookies.read("name=value; Domain=foo.com; Path=/; Secure; HttpOnly");

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
		Cookie cookie = Cookies.read("name=value; Domain=foo.com; Expires=Wed, 13-Jan-2021 22:23:01 GMT; max-age=3600; Path=/; Secure; HttpOnly");

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

	private static String formatCookie(String name, String value) {
		return name + "=" + value;
	}

	private static ThrowingCallable read(final String rawValue) {
		return new ThrowingCallable() {
			@Override
			public void call() {
				Cookies.read(rawValue);
			}
		};
	}
}
