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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.github.mjeanroy.junit.servers.commons.Dates.getTime;
import static com.github.mjeanroy.junit.servers.commons.Preconditions.notBlank;

/**
 * Default implementation for cookie object.
 */
public class Cookie {

	/**
	 * Create a cookie from header value.
	 *
	 * @param rawValue Header value.
	 * @return Cookie.
	 */
	public static Cookie read(String rawValue) {
		rawValue = notBlank(rawValue, "Cookie value");

		final String[] parts = rawValue.split(";");

		// Extract name and value
		final String[] nameAndValue = parts[0].split("=");
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
				String[] param = part.split("=");
				params.put(
						param[0].toLowerCase().trim(),
						param.length == 2 ? param[1].trim() : ""
				);
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

		return new Cookie(name, value, domain, path, secure, httpOnly, expires, maxAge);
	}

	/**
	 * Create cookie.
	 *
	 * @param name Cookie name.
	 * @param value Cookie value.
	 * @return Cookie.
	 * @throws NullPointerException if name or value is null.
	 * @throws IllegalArgumentException if name is empty or blank.
	 */
	public static Cookie cookie(String name, String value) {
		return new Cookie(name, value, null, null, false, false, null, null);
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
	public static Cookie cookie(String name, String value, String domain, String path, long expires, long maxAge, boolean secure, boolean httpOnly) {
		return new Cookie(name, value, domain, path, secure, httpOnly, expires, maxAge);
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
	public static Cookie secureCookie(String name, String value, String domain, String path, long expires, long maxAge) {
		return new Cookie(name, value, domain, path, true, true, expires, maxAge);
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
		return new Cookie(name, value, domain, path, true, true, -1L, 0L);
	}

	/**
	 * Cookie name.
	 * Must not be null, empty or blank.
	 */
	private final String name;

	/**
	 * Cookie value.
	 * Must not be null.
	 */
	private final String value;

	/**
	 * Cookie domain.
	 */
	private final String domain;

	/**
	 * Cookie path.
	 */
	private final String path;

	/**
	 * Cookie expires value.
	 */
	private final Long expires;

	/**
	 * Cookie max age.
	 */
	private final Long maxAge;

	/**
	 * Secure flag.
	 */
	private final boolean secure;

	/**
	 * Http Only flag.
	 */
	private final boolean httpOnly;

	// Use static factories
	private Cookie(String name, String value, String domain, String path, boolean secure, boolean httpOnly, Long expires, Long maxAge) {
		this.name = name;
		this.value = value;
		this.domain = domain;
		this.path = path;
		this.expires = expires;
		this.maxAge = maxAge;
		this.secure = secure;
		this.httpOnly = httpOnly;
	}

	/**
	 * Get cookie name.
	 *
	 * @return Cookie name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get cookie value.
	 *
	 * @return Cookie value.
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Get cookie domain.
	 *
	 * @return Cookie domain.
	 */
	public String getDomain() {
		return domain;
	}

	/**
	 * Get cookie path.
	 *
	 * @return Cookie path.
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Get cookie expires value.
	 * The value is a timestamp.
	 * If value is lower than zero, it means that expires
	 * value has not been set on cookie.
	 *
	 * @return Expires value.
	 */
	public Long getExpires() {
		return expires;
	}

	/**
	 * Cookie max age.
	 * This is the max validity time in seconds.
	 *
	 * @return Max age value.
	 */
	public Long getMaxAge() {
		return maxAge;
	}

	/**
	 * Cookie secure flag.
	 *
	 * @return Secure flag.
	 */
	public boolean isSecure() {
		return secure;
	}

	/**
	 * Cookie http-only flag.
	 *
	 * @return Http-Only flag.
	 */
	public boolean isHttpOnly() {
		return httpOnly;
	}

	/**
	 * Transform cookie as header value: result string
	 * can be used and transmit by client as header value (i.e
	 * value of header named "Cookie").
	 *
	 * @return Header value.
	 */
	public String toHeaderValue() {
		return new StringBuilder()
				.append(getName()).append("=").append(getValue())
				.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}

		if (o instanceof Cookie) {
			Cookie c = (Cookie) o;
			return getName().equals(c.getName()) &&
					getValue().equals(c.getValue()) &&
					Objects.equals(getDomain(), c.getDomain()) &&
					Objects.equals(getPath(), c.getPath()) &&
					getExpires() == c.getExpires() &&
					getMaxAge() == c.getMaxAge() &&
					isSecure() == c.isSecure() &&
					isHttpOnly() == c.isHttpOnly();
		}

		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(
				getName(), getValue(),
				getDomain(), getPath(),
				getExpires(), getMaxAge(),
				isSecure(), isHttpOnly()
		);
	}

	@Override
	public String toString() {
		return new StringBuilder()
				.append("Cookie { ")
				.append("Name=").append(getName()).append("; ")
				.append("Value=").append(getValue()).append("; ")
				.append("Domain=").append(getDomain()).append("; ")
				.append("Path=").append(getPath()).append("; ")
				.append("Expires=").append(getExpires()).append("; ")
				.append("MaxAge=").append(getMaxAge()).append("; ")
				.append("Secure=").append(isSecure()).append("; ")
				.append("HttpOnly=").append(isHttpOnly())
				.append(" }")
				.toString();
	}
}
