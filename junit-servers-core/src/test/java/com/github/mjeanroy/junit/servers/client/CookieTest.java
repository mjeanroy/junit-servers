/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 <mickael.jeanroy@gmail.com>
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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.TimeZone;

import static com.github.mjeanroy.junit.servers.client.Cookie.cookie;
import static com.github.mjeanroy.junit.servers.client.Cookie.read;
import static com.github.mjeanroy.junit.servers.client.Cookie.secureCookie;
import static com.github.mjeanroy.junit.servers.client.Cookie.sessionCookie;
import static org.assertj.core.api.Assertions.assertThat;

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

		Cookie cookie = cookie(name, value);

		assertThat(cookie).isNotNull();
		assertThat(cookie.getName()).isEqualTo(name);
		assertThat(cookie.getValue()).isEqualTo(value);
		assertThat(cookie.getExpires()).isEqualTo(-1L);
		assertThat(cookie.getMaxAge()).isZero();
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
		final int maxAge = 10;
		final String domain = "domain";
		final String path = "path";
		final boolean secure = true;
		final boolean httpOnly = false;

		Cookie cookie = cookie(name, value, domain, path, expires, maxAge, secure, httpOnly);

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
		final int maxAge = 10;
		final String domain = "domain";
		final String path = "path";

		Cookie cookie = secureCookie(name, value, domain, path, expires, maxAge);

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

		Cookie cookie = sessionCookie(name, value, domain, path);

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
		Cookie cookie = read("name=value");

		assertThat(cookie).isNotNull();
		assertThat(cookie.getName()).isEqualTo("name");
		assertThat(cookie.getValue()).isEqualTo("value");
		assertThat(cookie.getDomain()).isNull();
		assertThat(cookie.getPath()).isNull();
		assertThat(cookie.isSecure()).isFalse();
		assertThat(cookie.isHttpOnly()).isFalse();
		assertThat(cookie.getExpires()).isZero();
		assertThat(cookie.getMaxAge()).isZero();
	}

	@Test
	public void it_should_create_cookie_with_name_value_domain_path_and_flags() {
		Cookie cookie = read("name=value; Domain=foo.com; Path=/; Secure; HttpOnly");

		assertThat(cookie).isNotNull();
		assertThat(cookie.getName()).isEqualTo("name");
		assertThat(cookie.getValue()).isEqualTo("value");
		assertThat(cookie.getDomain()).isEqualTo("foo.com");
		assertThat(cookie.getPath()).isEqualTo("/");
		assertThat(cookie.isSecure()).isTrue();
		assertThat(cookie.isHttpOnly()).isTrue();
		assertThat(cookie.getExpires()).isZero();
		assertThat(cookie.getMaxAge()).isZero();
	}

	@Test
	public void it_should_create_cookie_with_name_value_domain_path_expires_max_date_and_flags() {
		Cookie cookie = read("name=value; Domain=foo.com; Expires=Wed, 13-Jan-2021 22:23:01 GMT; max-age=3600; Path=/; Secure; HttpOnly");

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
	public void it_should_create_cookie_header_value_with_name_value() {
		Cookie cookie = cookie("foo", "bar", null, null, 0, 0, false, false);
		String rawValue = cookie.toHeaderValue();
		assertThat(rawValue).isEqualTo("foo=bar");
	}
}
