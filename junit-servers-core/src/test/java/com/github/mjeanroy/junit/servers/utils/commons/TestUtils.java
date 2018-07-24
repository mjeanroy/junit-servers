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

package com.github.mjeanroy.junit.servers.utils.commons;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Static Test Utilities.
 */
public final class TestUtils {

	// Ensure non instantiation.
	private TestUtils() {
	}

	/**
	 * Create URL string.
	 *
	 * @param scheme URL Scheme (HTTP, HTTPS).
	 * @param host URL Host.
	 * @param port URL port.
	 * @param path URL path.
	 * @return Full URL.
	 */
	public static String url(String scheme, String host, int port, String path) {
		return new StringBuilder()
				.append(scheme)
				.append("://")
				.append(host)
				.append(":")
				.append(port)
				.append(path)
				.toString();
	}

	/**
	 * Create URL string with HTTP scheme and "localhost" host.
	 *
	 * @param port URL port.
	 * @param path URL path.
	 * @return Full URL.
	 */
	public static String localUrl(int port, String path) {
		return url("http", "localhost", port, path);
	}

	/**
	 * Create URL string with HTTP scheme and "localhost" host.
	 *
	 * @param port URL port.
	 * @return Full URL.
	 */
	public static String localUrl(int port) {
		return localUrl(port, "/");
	}

	/**
	 * URL encode string value.
	 * @param value String value.
	 * @return URL encoded value.
	 * @see URLEncoder#encode(String, String)
	 */
	public static String urlEncode(String value) {
		try {
			return URLEncoder.encode(value, StandardCharsets.UTF_8.displayName());
		} catch (UnsupportedEncodingException ex) {
			throw new AssertionError(ex);
		}
	}
}
