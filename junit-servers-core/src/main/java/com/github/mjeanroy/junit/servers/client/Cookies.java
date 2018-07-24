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

import java.util.HashMap;
import java.util.Map;

import static com.github.mjeanroy.junit.servers.commons.Dates.getTime;
import static com.github.mjeanroy.junit.servers.commons.Preconditions.notBlank;

/**
 * Static cookie utilities.
 */
public final class Cookies {

	/**
	 * The cookie separator used when multiple cookies needs to be sent to a server
	 * in an HTTP request.
	 */
	private static final String SEPARATOR = "; ";

	/**
	 * The character separating the name and the value of a cookie.
	 */
	private static final String NAME_VALUE_SEPARATOR = "=";

	/**
	 * The character separating cookies fields in the {@code Set-Cookie} header.
	 */
	private static final String FIELD_SEPARATOR = ";";

	// Ensure non instantiation.
	private Cookies() {
	}

	/**
	 * Create cookie with a name and a value, all other parameters will
	 * use the default values:
	 *
	 * <ul>
	 *   <li>The webapp path is initialized with {@code "src/main/webapp"}</li>
	 *   <li>The path is initialized with {@code "/"}</li>
	 *   <li>The port is initialized with zero.</li>
	 *   <li>The cookie is not secured.</li>
	 *   <li>The cookie is not http-only.</li>
	 *   <li>The cookie max-age is {@code null}.</li>
	 * </ul>.
	 *
	 * @param name Cookie name.
	 * @param value Cookie value.
	 * @return Cookie.
	 * @throws NullPointerException if name or value is null.
	 * @throws IllegalArgumentException if name is empty or blank.
	 */
	public static Cookie cookie(String name, String value) {
		return new Cookie.Builder(name, value).build();
	}

	/**
	 * Create cookie.
	 *
	 * @param name Cookie name.
	 * @param value Cookie value.
	 * @param domain Cookie domain.
	 * @param path Cookie path.
	 * @param expires Cookie expires value.
	 * @param maxAge Cookie max age value.
	 * @param secure Secure flag.
	 * @param httpOnly Http flag.
	 * @return Cookie.
	 * @throws NullPointerException if name or value is null.
	 * @throws IllegalArgumentException if name is empty or blank.
	 */
	public static Cookie cookie(String name, String value, String domain, String path, Long expires, Long maxAge, boolean secure, boolean httpOnly) {
		Cookie.Builder builder = new Cookie.Builder(name, value)
			.domain(domain)
			.path(path)
			.secure(secure)
			.httpOnly(httpOnly);

		if (expires != null) {
			builder.expires(expires);
		}

		if (maxAge != null) {
			builder.maxAge(maxAge);
		}

		return builder.build();
	}


	/**
	 * Create a secured cookie, it means that cookie will have
	 * secure flag and http only flag set to true.
	 *
	 * @param name Cookie name.
	 * @param value Cookie value.
	 * @param domain Cookie domain.
	 * @param path Cookie path.
	 * @param expires Cookie expires value.
	 * @param maxAge Cookie max age.
	 * @return Cookie.
	 * @throws NullPointerException if name or value is null.
	 * @throws IllegalArgumentException if name is empty or blank.
	 */
	public static Cookie secureCookie(String name, String value, String domain, String path, Long expires, Long maxAge) {
		return Cookies.cookie(name, value, domain, path, expires, maxAge, true, true);
	}

	/**
	 * Create a session cookie, it means that cookie will have
	 * secure flag and http only flag set to true and expires / max age values
	 * are set to zero.
	 *
	 * @param name Cookie name.
	 * @param value Cookie value.
	 * @param domain Cookie domain.
	 * @param path Cookie path.
	 * @return Cookie.
	 * @throws NullPointerException if name or value is null.
	 * @throws IllegalArgumentException if name is empty or blank.
	 */
	public static Cookie sessionCookie(String name, String value, String domain, String path) {
		return Cookies.cookie(name, value, domain, path, -1L, 0L, true, true);
	}

	/**
	 * Create a cookie from header value.
	 *
	 * @param rawValue Header value.
	 * @return Cookie.
	 */
	public static Cookie read(String rawValue) {
		notBlank(rawValue, "Cookie value");

		final String[] parts = rawValue.split(FIELD_SEPARATOR);

		// Extract name and value
		final String[] nameAndValue = parts[0].split(NAME_VALUE_SEPARATOR);
		if (nameAndValue.length != 2) {
			throw new IllegalArgumentException("Cookie must have a valid name and a valid value");
		}

		final String name = nameAndValue[0].trim();
		final String value = nameAndValue[1].trim();
		if (name.isEmpty()) {
			throw new IllegalArgumentException("Cookie must have a valid name");
		}

		// Extract other parts
		Map<String, String> params = new HashMap<>();
		if (parts.length > 1) {
			for (String part : parts) {
				String[] param = part.split(NAME_VALUE_SEPARATOR);
				String paramName = param[0].toLowerCase().trim();
				String paramValue = param.length == 2 ? param[1].trim() : "";
				params.put(paramName, paramValue);
			}
		}

		final String domain = params.get("domain");
		final String path = params.get("path");
		final boolean secure = params.containsKey("secure");
		final boolean httpOnly = params.containsKey("httponly");

		final String maxAgeTt = params.get("max-age");
		final String expiresDate = params.get("expires");

		Long expires;
		Long maxAge = maxAgeTt == null ? null : Long.valueOf(maxAgeTt);

		if (expiresDate != null) {
			expires = getTime(expiresDate, "EEE, d MMM yyyy HH:mm:ss Z", "EEE, d-MMM-yyyy HH:mm:ss Z", "EEE, d/MMM/yyyy HH:mm:ss Z");
		} else {
			expires = null;
		}

		if (maxAge == null && expires == null) {
			maxAge = 0L;
		} else if (maxAge == null) {
			maxAge = expires - System.currentTimeMillis();
		}

		return Cookies.cookie(name, value, domain, path, expires, maxAge, secure, httpOnly);
	}

	/**
	 * Serialize cookies as a string that is ready to be sent to a server over an HTTP
	 * request (a.k.a value of {@code Cookie} header).
	 *
	 * @param cookies Cookies to serialize.
	 * @return The final value.
	 */
	public static String serialize(Iterable<Cookie> cookies) {
		StringBuilder result = new StringBuilder();
		boolean first = true;

		for (Cookie cookie : cookies) {
			if (!first) {
				result.append(SEPARATOR);
			}

			String name = cookie.getName();
			String value = cookie.getValue();
			result.append(name).append(NAME_VALUE_SEPARATOR).append(value);
			first = false;
		}

		return result.toString();
	}
}
