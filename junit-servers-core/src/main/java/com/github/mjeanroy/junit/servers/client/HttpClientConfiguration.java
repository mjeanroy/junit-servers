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

import com.github.mjeanroy.junit.servers.commons.ToStringBuilder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.Collections.unmodifiableList;
import static java.util.Collections.unmodifiableMap;

import static com.github.mjeanroy.junit.servers.commons.Preconditions.notNull;

/**
 * HTTP Client configuration that can be used to configure instances of {@link HttpClient}.
 */
public class HttpClientConfiguration {

	/**
	 * Default value for {@link HttpClientConfiguration#followRedirect}.
	 */
	private static final boolean DEFAULT_FOLLOW_REDIRECT = true;

	/**
	 * The default configuration.
	 * Since the {@link HttpClientConfiguration} is immutable, it is safe to use a singleton.
	 */
	private static final HttpClientConfiguration DEFAULT_CONFIGURATION = new HttpClientConfiguration.Builder().build();

	/**
	 * Get the default configuration.
	 *
	 * @return The default configuration.
	 */
	public static HttpClientConfiguration defaultConfiguration() {
		return DEFAULT_CONFIGURATION;
	}

	/**
	 * Set of default headers that will be added for each HTTP requests.
	 */
	private final Map<String, HttpHeader> defaultHeaders;

	/**
	 * List of cookies that will be added for each HTTP requests.
	 */
	private final List<Cookie> defaultCookies;

	/**
	 * Flag that can enable/disable automatic redirection handling.
	 */
	private final boolean followRedirect;

	// Private constructor: use the builder instead.
	private HttpClientConfiguration(boolean followRedirect, Map<String, HttpHeader> defaultHeaders, List<Cookie> defaultCookies) {
		this.followRedirect = followRedirect;
		this.defaultHeaders = unmodifiableMap(new LinkedHashMap<>(defaultHeaders));
		this.defaultCookies = unmodifiableList(new ArrayList<>(defaultCookies));
	}

	/**
	 * Get {@link #defaultHeaders} (non-modifiable map).
	 *
	 * @return {@link #defaultHeaders}.
	 */
	public Map<String, HttpHeader> getDefaultHeaders() {
		return defaultHeaders;
	}

	/**
	 * Get {@link #defaultCookies} (non-modifiable list).
	 *
	 * @return {@link #defaultCookies}.
	 */
	public List<Cookie> getDefaultCookies() {
		return defaultCookies;
	}

	/**
	 * Get {@link #followRedirect} flag.
	 *
	 * @return {@link #followRedirect}.
	 */
	public boolean isFollowRedirect() {
		return followRedirect;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}

		if (o instanceof HttpClientConfiguration) {
			HttpClientConfiguration c = (HttpClientConfiguration) o;
			return followRedirect == c.followRedirect &&
				Objects.equals(defaultHeaders, c.defaultHeaders) &&
				Objects.equals(defaultCookies, c.defaultCookies);
		}

		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(followRedirect, defaultHeaders, defaultCookies);
	}

	@Override
	public String toString() {
		return ToStringBuilder.create(getClass())
			.append("followRedirect", followRedirect)
			.append("defaultHeaders", defaultHeaders)
			.append("defaultCookies", defaultCookies)
			.build();
	}

	/**
	 * Builder for {@link HttpClientConfiguration} class.
	 */
	public static class Builder {
		/**
		 * Flag that can be used to enable/disable automatic redirection.
		 * @see HttpClientConfiguration#DEFAULT_FOLLOW_REDIRECT
		 */
		private boolean followRedirect;

		/**
		 * The set of default headers that will be added for each HTTP request.
		 */
		private final Map<String, HttpHeader> defaultHeaders;

		/**
		 * The list of cookies that will be added for each HTTP request.
		 */
		private final List<Cookie> defaultCookies;

		/**
		 * Create builder with default values.
		 *
		 * @see HttpClientConfiguration#DEFAULT_FOLLOW_REDIRECT
		 */
		public Builder() {
			this.followRedirect = DEFAULT_FOLLOW_REDIRECT;
			this.defaultHeaders = new LinkedHashMap<>();
			this.defaultCookies = new ArrayList<>();
		}

		/**
		 * Add new default header: if a header with the same name as already been added, it
		 * will be overwritten with this new header.
		 *
		 * @param header The header.
		 * @return The builder (for chaining).
		 * @throws NullPointerException If {@code header} is {@code null}.
		 */
		public Builder addDefaultHeader(HttpHeader header) {
			notNull(header, "header");
			this.defaultHeaders.put(header.getName(), header);
			return this;
		}

		/**
		 * Add new default header: if a header with the same name as already been added, it
		 * will be overwritten with this new header.
		 *
		 * @param name Header name.
		 * @param value Header value.
		 * @return The builder (for chaining).
		 * @throws NullPointerException If {@code name} or {@code value} are {@code null}.
		 * @throws IllegalArgumentException If {@code name} is empty or blank.
		 * @see HttpHeader#header(String, String)
		 * @see #addDefaultHeader(HttpHeader)
		 */
		public Builder addDefaultHeader(String name, String value) {
			return addDefaultHeader(HttpHeader.header(name, value));
		}

		/**
		 * Add new default cookie.
		 *
		 * @param cookie The cookie to add.
		 * @return The builder (for chaining).
		 * @throws NullPointerException If {@code cookie} is {@code null}.
		 */
		public Builder addDefaultCookie(Cookie cookie) {
			notNull(cookie, "cookie");
			this.defaultCookies.add(cookie);
			return this;
		}

		/**
		 * Add new default cookie.
		 *
		 * @param name Cookie name.
		 * @param value Cookie value.
		 * @return The builder (for chaining).
		 * @throws NullPointerException If {@code name} or {@code value} are {@code null}.
		 * @see Cookies#cookie(String, String)
		 * @see #addDefaultCookie(Cookie)
		 */
		public Builder addDefaultCookie(String name, String value) {
			return addDefaultCookie(Cookies.cookie(name, value));
		}

		/**
		 * Enable follow redirection handling.
		 *
		 * @return The builder (for chaining).
		 */
		public Builder enableFollowRedirect() {
			this.followRedirect = true;
			return this;
		}

		/**
		 * Disable follow redirection handling.
		 *
		 * @return The builder (for chaining).
		 */
		public Builder disableFollowRedirect() {
			this.followRedirect = false;
			return this;
		}

		/**
		 * Create new client configuration.
		 *
		 * @return The HTTP client configuration.
		 */
		public HttpClientConfiguration build() {
			return new HttpClientConfiguration(followRedirect, defaultHeaders, defaultCookies);
		}
	}
}
