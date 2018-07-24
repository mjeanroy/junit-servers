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

import java.util.Objects;

import com.github.mjeanroy.junit.servers.commons.ToStringBuilder;

/**
 * Default implementation for cookie object.
 *
 * <br>
 *
 * To build a cookie, use:
 * <ul>
 *   <li>The {@link Builder} API</li>
 *   <li>Or one of the static factories in {@link Cookies} class</li>
 * </ul>
 *
 * <br>
 *
 * Using the builder is easy:
 *
 * <pre>{@code
 *   Cookie cookie = new Cookie.Builder("name", "value")
 *     .domain("domain")
 *     .path("path")
 *     .secure(true)   // Defaults to false.
 *     .httpOnly(true) // Defaults to false.
 *     .maxAge(3600)
 *     .expires(tt.getTime())
 *     .build();
 * }</pre>
 *
 * You can also use one of the static factories:
 *
 * <pre>{@code
 *   Cookie c1 = Cookies.cookie("name", "value");
 *   Cookie c2 = Cookies.cookie("name", "value", "domain", "path", maxAge, expires, true, true);
 * }</pre>
 *
 * @see <a href="https://developer.mozilla.org/fr/docs/Web/HTTP/Headers/Set-Cookie">https://developer.mozilla.org/fr/docs/Web/HTTP/Headers/Set-Cookie</a>
 */
public class Cookie {

	/**
	 * Create a cookie from header value.
	 *
	 * @param rawValue Header value.
	 * @return Cookie.
	 * @deprecated Use {@link Cookies#read(String)} instead.
	 */
	@Deprecated
	public static Cookie read(String rawValue) {
		return Cookies.read(rawValue);
	}

	/**
	 * Create cookie.
	 *
	 * @param name Cookie name.
	 * @param value Cookie value.
	 * @return Cookie.
	 * @throws NullPointerException if name or value is null.
	 * @throws IllegalArgumentException if name is empty or blank.
	 * @deprecated Use {@link Cookies#cookie(String, String)} instead.
	 */
	@Deprecated
	public static Cookie cookie(String name, String value) {
		return Cookies.cookie(name, value);
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
	 * @deprecated Use {@link Cookies#cookie(String, String, String, String, Long, Long, boolean, boolean)} instead.
	 */
	@Deprecated
	public static Cookie cookie(String name, String value, String domain, String path, Long expires, Long maxAge, boolean secure, boolean httpOnly) {
		return Cookies.cookie(name, value, domain, path, expires, maxAge, secure, httpOnly);
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
	 * @deprecated Use {@link Cookies#secureCookie(String, String, String, String, Long, Long)} instead.
	 */
	@Deprecated
	public static Cookie secureCookie(String name, String value, String domain, String path, Long expires, Long maxAge) {
		return Cookies.secureCookie(name, value, domain, path, expires, maxAge);
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
	 * @deprecated Use {@link Cookies#sessionCookie(String, String, String, String)} instead.
	 */
	@Deprecated
	public static Cookie sessionCookie(String name, String value, String domain, String path) {
		return Cookies.sessionCookie(name, value, domain, path);
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

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}

		if (o instanceof Cookie) {
			Cookie c = (Cookie) o;
			return Objects.equals(name, c.name) &&
					Objects.equals(value, c.value) &&
					Objects.equals(domain, c.domain) &&
					Objects.equals(path, c.path) &&
					Objects.equals(expires, c.expires) &&
					Objects.equals(maxAge, c.maxAge) &&
					secure == c.secure &&
					httpOnly == c.httpOnly;
		}

		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, value, domain, path, expires, maxAge, secure, httpOnly);
	}

	@Override
	public String toString() {
		return ToStringBuilder.create(getClass())
				.append("name", name)
				.append("value", value)
				.append("domain", domain)
				.append("path", path)
				.append("expires", expires)
				.append("maxAge", maxAge)
				.append("secure", secure)
				.append("httpOnly", httpOnly)
				.build();
	}

	/**
	 * Builder to create {@link Cookie} instances.
	 */
	public static class Builder {
		/**
		 * The cookie name.
		 * @see Cookie#name
		 */
		private final String name;

		/**
		 * The cookie value.
		 * @see Cookie#value
		 */
		private final String value;

		/**
		 * The cookie domain.
		 * @see Cookie#domain
		 */
		private String domain;

		/**
		 * The cookie path.
		 * @see Cookie#path
		 */
		private String path;

		/**
		 * The cookie max-age.
		 * @see Cookie#maxAge
		 */
		private Long maxAge;

		/**
		 * The cookie expires value.
		 * @see Cookie#expires
		 */
		private Long expires;

		/**
		 * The cookie secure flag.
		 * @see Cookie#secure
		 */
		private boolean secure;

		/**
		 * The cookie http-only flag.
		 * @see Cookie#httpOnly
		 */
		private boolean httpOnly;

		/**
		 * Create the builder with a cookie name and value.
		 *
		 * @param name Cookie name.
		 * @param value Cookie value.
		 */
		public Builder(String name, String value) {
			this.name = name;
			this.value = value;
		}

		/**
		 * Update cookie domain.
		 *
		 * @param domain Cookie domain.
		 * @return The builder (for chaining).
		 */
		public Builder domain(String domain) {
			this.domain = domain;
			return this;
		}

		/**
		 * Update cookie path.
		 *
		 * @param path Cookie path.
		 * @return The builder (for chaining).
		 */
		public Builder path(String path) {
			this.path = path;
			return this;
		}

		/**
		 * Update cookie secure flag.
		 *
		 * @param secure Cookie secure flag.
		 * @return The builder (for chaining).
		 */
		public Builder secure(boolean secure) {
			this.secure = secure;
			return this;
		}

		/**
		 * Update cookie http-only flag.
		 *
		 * @param httpOnly Cookie http-only flag.
		 * @return The builder (for chaining).
		 */
		public Builder httpOnly(boolean httpOnly) {
			this.httpOnly = httpOnly;
			return this;
		}

		/**
		 * Update cookie max-age.
		 *
		 * @param maxAge Cookie max-age.
		 * @return The builder (for chaining).
		 */
		public Builder maxAge(long maxAge) {
			this.maxAge = maxAge;
			return this;
		}

		/**
		 * Update cookie expires value.
		 *
		 * @param expires Cookie expires.
		 * @return The builder (for chaining).
		 */
		public Builder expires(long expires) {
			this.expires = expires;
			return this;
		}

		/**
		 * Create the cookie.
		 *
		 * @return The cookie.
		 */
		public Cookie build() {
			return new Cookie(name, value, domain, path, secure, httpOnly, expires, maxAge);
		}
	}
}
