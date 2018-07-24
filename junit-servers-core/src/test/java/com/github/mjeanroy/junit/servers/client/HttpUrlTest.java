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
import org.junit.Test;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class HttpUrlTest {

	@Test
	public void it_should_create_default_url() {
		HttpUrl url = new HttpUrl.Builder().build();
		assertUrl(url, "http", "localhost", 80, "/");
	}

	@Test
	public void it_should_create_default_https_url() {
		HttpUrl url = new HttpUrl.Builder()
			.withScheme("https")
			.build();

		assertUrl(url, "https", "localhost", 443, "/");
	}

	@Test
	public void it_should_fail_with_unknown_scheme() {
		HttpUrl.Builder builder = new HttpUrl.Builder();
		assertThatThrownBy(withScheme(builder, "ftp"))
				.isExactlyInstanceOf(IllegalArgumentException.class)
				.hasMessage("Unknown scheme: ftp");
	}

	@Test
	public void it_should_create_url_with_empty_path() {
		HttpUrl url = new HttpUrl.Builder()
			.withPath("")
			.build();

		assertUrl(url, "http", "localhost", 80, "/");
	}

	@Test
	public void it_should_create_url_and_ensure_absolute_path() {
		HttpUrl url = new HttpUrl.Builder()
				.withPath("foo")
				.build();

		assertUrl(url, "http", "localhost", 80, "/foo");
	}

	@Test
	public void it_should_create_custom_url() {
		String scheme = "https";
		String host = "127.0.0.1";
		int port = 443;
		String path = "/app";

		HttpUrl url = new HttpUrl.Builder()
			.withScheme(scheme)
			.withHost(host)
			.withPort(port)
			.withPath(path)
			.build();

		assertUrl(url, "https", host, port, path);
	}

	@Test
	public void it_should_create_uri() {
		HttpUrl url = new HttpUrl.Builder().build();
		assertThat(url.toURI()).isEqualTo(URI.create("http://localhost:80/"));
	}

	@Test
	public void it_should_create_uri_with_non_encoded_path() {
		HttpUrl url = new HttpUrl.Builder()
			.withPath("foo bar")
			.build();

		assertThat(url.toURI()).isEqualTo(URI.create("http://localhost:80/foo%20bar"));
	}

	@Test
	public void it_should_implement_to_string() {
		HttpUrl url = new HttpUrl.Builder().build();
		assertThat(url.toString()).isEqualTo("http://localhost:80/");
	}

	@Test
	public void it_should_implement_to_string_with_custom_path() {
		HttpUrl url = new HttpUrl.Builder()
			.withPath("/foo")
			.build();

		assertThat(url.toString()).isEqualTo("http://localhost:80/foo");
	}

	@Test
	public void it_should_implement_equals_hash_code() {
		EqualsVerifier.forClass(HttpUrl.class)
			.suppress(Warning.STRICT_INHERITANCE)
			.verify();
	}

	@Test
	public void it_should_parse_url() {
		HttpUrl url = HttpUrl.parse("http://localhost:80/");
		assertUrl(url, "http", "localhost", 80, "/");
	}

	@Test
	public void it_should_parse_url_with_default_port() {
		HttpUrl url = HttpUrl.parse("http://localhost/");
		assertUrl(url, "http", "localhost", 80, "/");
	}

	@Test
	public void it_should_parse_https_url() {
		HttpUrl url = HttpUrl.parse("https://localhost:443/");
		assertUrl(url, "https", "localhost", 443, "/");
	}

	@Test
	public void it_should_parse_https_url_with_default_port() {
		HttpUrl url = HttpUrl.parse("https://localhost/");
		assertUrl(url, "https", "localhost", 443, "/");
	}

	@Test
	public void it_should_parse_with_non_encoded_path() {
		HttpUrl url = HttpUrl.parse("http://localhost:80/people/john doe");
		assertUrl(url, "http", "localhost", 80, "/people/john doe");
	}

	private static void assertUrl(HttpUrl url, String scheme, String host, int port, String path) {
		assertThat(url.getScheme()).isEqualTo(scheme);
		assertThat(url.getHost()).isEqualTo(host);
		assertThat(url.getPort()).isEqualTo(port);
		assertThat(url.getPath()).isEqualTo(path);
	}

	private static ThrowingCallable withScheme(final HttpUrl.Builder builder, final String scheme) {
		return new ThrowingCallable() {
			@Override
			public void call() {
				builder.withScheme(scheme);
			}
		};
	}
}
