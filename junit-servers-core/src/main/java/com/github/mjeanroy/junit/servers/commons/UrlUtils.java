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

package com.github.mjeanroy.junit.servers.commons;

import java.net.URI;
import java.net.URISyntaxException;

import com.github.mjeanroy.junit.servers.exceptions.UrlException;

/**
 * Static URL utilities.
 *
 * <p>
 *
 * <strong>Internal API</strong>: these methods are part of the internal API and may be removed, have their signature change,
 * or have their access level decreased from public to protected, package, or private in future versions without notice.
 */
public final class UrlUtils {

	private static final String[] HTTP_SCHEMES = new String[] {"http://", "https://"};

	/**
	 * The separator used in URL paths.
	 */
	private static final char PATH_SEPARATOR = '/';

	// Ensure non instantiation
	private UrlUtils() {
	}

	/**
	 * Create URI object and wrap {@link URISyntaxException} to {@link UrlException}.
	 *
	 * @param scheme URL scheme.
	 * @param host URL host.
	 * @param port URL port.
	 * @param path URL path.
	 * @return The final URI.
	 * @throws UrlException If URI cannot be built because of an invalid parameter.
	 */
	public static URI createUri(String scheme, String host, int port, String path) {
		try {
			return new URI(scheme, null, host, port, path, null, null);
		} catch (URISyntaxException ex) {
			throw new UrlException(scheme, host, port, path, ex);
		}
	}

	/**
	 * Ensure that given path is an absolute path (i.e starts with {@code '/'}.
	 *
	 * @param path The path.
	 * @return Absolute path.
	 */
	public static String ensureAbsolutePath(String path) {
		if (path == null || path.isEmpty()) {
			return String.valueOf(PATH_SEPARATOR);
		}

		return path.charAt(0) == PATH_SEPARATOR ? path : String.valueOf(PATH_SEPARATOR) + path;
	}

	/**
	 * Concatenate two path value.
	 *
	 * @param path Path prefix.
	 * @param endpoint Path suffix.
	 * @return Final path.
	 */
	public static String concatenatePath(String path, String endpoint) {
		String firstSegment = ensureAbsolutePath(path);
		if (endpoint == null || endpoint.isEmpty()) {
			return firstSegment;
		}

		StringBuilder sb = new StringBuilder(firstSegment);
		if (path.charAt(path.length() - 1) != PATH_SEPARATOR) {
			sb.append(PATH_SEPARATOR);
		}

		if (endpoint.charAt(0) == PATH_SEPARATOR) {
			sb.append(endpoint.substring(1));
		} else {
			sb.append(endpoint);
		}

		return sb.toString();
	}

	/**
	 * Check if given {@code url} starts with HTTP scheme (i.e {@code "http"} or {@code "https"}).
	 *
	 * @param url The URL to check.
	 * @return {@code true} if {@code url} starts with HTTP scheme, {@code false} otherwise.
	 */
	public static boolean startsWithHttpScheme(String url) {
		if (url == null || url.isEmpty()) {
			return false;
		}

		for (String scheme : HTTP_SCHEMES) {
			int prefixLength = scheme.length();
			if (url.length() > prefixLength) {
				String prefix = url.substring(0, prefixLength);
				if (prefix.equalsIgnoreCase(scheme)) {
					return true;
				}
			}
		}

		return false;
	}
}
