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

package com.github.mjeanroy.junit.servers.commons.core;

import com.github.mjeanroy.junit.servers.exceptions.UrlException;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

class UrlsTest {

	@Test
	void it_should_ensure_absolute_path() {
		assertThat(Urls.ensureAbsolutePath(null)).isEqualTo("/");
		assertThat(Urls.ensureAbsolutePath("")).isEqualTo("/");
		assertThat(Urls.ensureAbsolutePath("/")).isEqualTo("/");
		assertThat(Urls.ensureAbsolutePath("/foo")).isEqualTo("/foo");
		assertThat(Urls.ensureAbsolutePath("foo")).isEqualTo("/foo");
	}

	@Test
	void it_should_create_uri_object() {
		final String scheme = "http";
		final String host = "localhost";
		final int port = 80;
		final String path = "/foo";

		final URI uri = Urls.createUri(scheme, host, port, path);

		assertThat(uri).isNotNull();
		assertThat(uri.getScheme()).isEqualTo(scheme);
		assertThat(uri.getHost()).isEqualTo(host);
		assertThat(uri.getPort()).isEqualTo(port);
		assertThat(uri.getPath()).isEqualTo(path);

		assertThat(uri.getUserInfo()).isNull();
		assertThat(uri.getQuery()).isNull();
		assertThat(uri.getFragment()).isNull();
	}

	@Test
	void it_should_fail_to_create_malformed_uri() {
		final String scheme = "http";
		final String host = "localhost";
		final int port = 80;
		final String path = "foo";

		try {
			Urls.createUri(scheme, host, port, path);
			failBecauseExceptionWasNotThrown(UrlException.class);
		}
		catch (UrlException ex) {
			assertThat(ex.getCause()).isNotNull();
			assertThat(ex.getCause()).isExactlyInstanceOf(URISyntaxException.class);

			assertThat(ex.getScheme()).isEqualTo(scheme);
			assertThat(ex.getHost()).isEqualTo(host);
			assertThat(ex.getPort()).isEqualTo(port);
			assertThat(ex.getPath()).isEqualTo(path);
		}
	}

	@Test
	void it_should_concatenate_path() {
		assertThat(Urls.concatenatePath(null, null)).isEqualTo("/");
		assertThat(Urls.concatenatePath("", "")).isEqualTo("/");
		assertThat(Urls.concatenatePath("/", null)).isEqualTo("/");
		assertThat(Urls.concatenatePath("/", "")).isEqualTo("/");

		assertThat(Urls.concatenatePath("/", "/foo")).isEqualTo("/foo");
		assertThat(Urls.concatenatePath("/foo", "/bar")).isEqualTo("/foo/bar");
		assertThat(Urls.concatenatePath("/foo/", "/bar")).isEqualTo("/foo/bar");
		assertThat(Urls.concatenatePath("foo", "bar")).isEqualTo("/foo/bar");
	}

	@Test
	void it_should_ensure_that_url_starts_with_http_scheme() {
		assertThat(Urls.startsWithHttpScheme("http://localhost")).isTrue();
		assertThat(Urls.startsWithHttpScheme("HTTP://LOCALHOST")).isTrue();
		assertThat(Urls.startsWithHttpScheme("https://localhost")).isTrue();
		assertThat(Urls.startsWithHttpScheme("HTTPS://LOCALHOST")).isTrue();

		assertThat(Urls.startsWithHttpScheme(null)).isFalse();
		assertThat(Urls.startsWithHttpScheme("")).isFalse();
		assertThat(Urls.startsWithHttpScheme("ftp://localhost")).isFalse();
	}
}
