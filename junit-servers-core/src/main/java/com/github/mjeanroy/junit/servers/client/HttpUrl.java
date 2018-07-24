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

import static com.github.mjeanroy.junit.servers.commons.ObjectUtils.firstNonNull;
import static com.github.mjeanroy.junit.servers.commons.Preconditions.notNull;
import static com.github.mjeanroy.junit.servers.commons.UrlUtils.ensureAbsolutePath;
import static java.util.Collections.unmodifiableMap;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.github.mjeanroy.junit.servers.exceptions.UrlException;

/**
 * URL object without:
 *
 * <ul>
 *   <li>Authority part.</li>
 *   <li>Query string.</li>
 *   <li>Fragment.</li>
 * </ul>
 */
public class HttpUrl {

	/**
	 * Default URL scheme.
	 */
	private static final HttpScheme DEFAULT_SCHEME = HttpScheme.HTTP;

	/**
	 * Default URL host.
	 */
	private static final String DEFAULT_HOST = "localhost";

	/**
	 * Default URL path.
	 */
	private static final String DEFAULT_PATH = "/";

	/**
	 * Parse URL string to create new {@link HttpUrl} instance.
	 *
	 * @param endpoint URL value.
	 * @return The final {@link HttpUrl} instance.
	 * @throws IllegalArgumentException If URL is malformed.
	 */
	public static HttpUrl parse(String endpoint) {
		try {
			final URL url = new URL(endpoint);
			final String protocol = url.getProtocol();
			final String host = url.getHost();
			final int port = url.getPort();
			final String path = url.getPath();
			return new Builder()
				.withScheme(protocol)
				.withHost(host)
				.withPort(port >= 0 ? port : url.getDefaultPort())
				.withPath(path)
				.build();
		} catch (MalformedURLException ex) {
			throw new IllegalArgumentException(ex);
		}
	}

	/**
	 * URL Scheme.
	 */
	private final HttpScheme scheme;

	/**
	 * URL Host.
	 */
	private final String host;

	/**
	 * URL Port.
	 */
	private final int port;

	/**
	 * URL Path.
	 */
	private final String path;

	/**
	 * Create URL.
	 *
	 * @param scheme URL Scheme.
	 * @param host URL Host.
	 * @param port URL Port.
	 * @param path URL Path.
	 */
	private HttpUrl(HttpScheme scheme, String host, int port, String path) {
		this.scheme = scheme;
		this.host = host;
		this.port = port;
		this.path = path;
	}

	/**
	 * Get URL Scheme.
	 *
	 * @return URL Scheme.
	 * @see #scheme
	 */
	public String getScheme() {
		return scheme.getProtocol();
	}

	/**
	 * Get URL Host.
	 *
	 * @return URL Host.
	 * @see #host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * Get URL Port.
	 *
	 * @return URL Port.
	 * @see #port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * Get URL Path.
	 * @return URL Path.
	 * @see #path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Create new {@link URI} from URL fields.
	 *
	 * @return URI instance.
	 * @throws IllegalArgumentException If {@code URI} cannot be built because of {@link URISyntaxException}.
	 */
	public URI toURI() {
		final String protocol = scheme.getProtocol();
		try {
			return new URI(protocol, null, host, port, path, null, null);
		} catch (URISyntaxException ex) {
			throw new UrlException(protocol, host, port, path, ex);
		}
	}

	@Override
	public String toString() {
		return new StringBuilder()
			.append(scheme.getProtocol())
			.append("://")
			.append(host)
			.append(":")
			.append(port)
			.append(path)
			.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}

		if (o instanceof HttpUrl) {
			HttpUrl u = (HttpUrl) o;
			return Objects.equals(scheme, u.scheme)
					&& Objects.equals(host, u.host)
					&& Objects.equals(port, u.port)
					&& Objects.equals(path, u.path);
		}

		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(scheme, host, port, path);
	}

	/**
	 * Builder used to create {@link HttpUrl} instances.
	 */
	public static class Builder {

		/**
		 * The URL Scheme, default to {@code "http"}.
		 * @see HttpUrl#DEFAULT_SCHEME
		 */
		private HttpScheme scheme;

		/**
		 * The URL Host, default to {@code "localhost"}.
		 * @see HttpUrl#DEFAULT_HOST
		 */
		private String host;

		/**
		 * The URL Port, default to {@code 80} if scheme is {@code "http"}, 443 if scheme is {@code https}.
		 */
		private Integer port;

		/**
		 * The URL Path, default to {@code "/"}.
		 * @see HttpUrl#DEFAULT_PATH
		 */
		private String path;

		/**
		 * Create builder.
		 */
		public Builder() {
		}

		/**
		 * Update URL Scheme.
		 *
		 * @param scheme New URL Scheme.
		 * @return The builder (for chaining).
		 */
		public Builder withScheme(String scheme) {
			notNull(scheme, "scheme");

			HttpScheme result = HttpScheme.parse(scheme);
			if (result == null) {
				throw new IllegalArgumentException(String.format("Unknown scheme: %s", scheme));
			}

			this.scheme = result;
			return this;
		}

		/**
		 * Update URL Host.
		 *
		 * @param host New URL Host.
		 * @return The builder (for chaining).
		 */
		public Builder withHost(String host) {
			this.host = notNull(host, "host");
			return this;
		}

		/**
		 * Update URL Port.
		 *
		 * @param port New URL Port.
		 * @return The builder (for chaining).
		 */
		public Builder withPort(int port) {
			this.port = port;
			return this;
		}

		/**
		 * Update URL Path.
		 *
		 * @param path New URL Path.
		 * @return The builder (for chaining).
		 */
		public Builder withPath(String path) {
			this.path = notNull(path, "path");
			return this;
		}

		/**
		 * Create immutable {@link HttpUrl} instance from current field values.
		 *
		 * @return URL.
		 */
		public HttpUrl build() {
			final HttpScheme scheme = firstNonNull(this.scheme, DEFAULT_SCHEME);
			final String host = firstNonNull(this.host, DEFAULT_HOST);
			final String path = firstNonNull(this.path, DEFAULT_PATH);
			final int port = firstNonNull(this.port, scheme.getDefaultPort());
			return new HttpUrl(scheme, host, port, ensureAbsolutePath(path));
		}
	}

	/**
	 * Supported scheme: currently only {@code "http"} and {@code "https"} protocols.
	 */
	private enum HttpScheme {
		/**
		 * The {@code "http"} protocol.
		 */
		HTTP("http", 80),

		/**
		 * The {@code "https"}protocol.
		 */
		HTTPS("https", 443);

		/**
		 * The protocol identifier to use in URL.
		 */
		private final String protocol;

		/**
		 * The default port for the given protocol.
		 */
		private final int defaultPort;

		HttpScheme(String protocol, int defaultPort) {
			this.protocol = protocol;
			this.defaultPort = defaultPort;
		}

		/**
		 * Get default port for the given scheme.
		 *
		 * @return Default port.
		 */
		private int getDefaultPort() {
			return defaultPort;
		}

		/**
		 * Get protocol prefix to use in URL.
		 *
		 * @return The protocol.
		 */
		private String getProtocol() {
			return protocol;
		}

		private static final Map<String, HttpScheme> VALUES = unmodifiableMap(new HashMap<String, HttpScheme>() {{
			put(HttpScheme.HTTP.getProtocol(), HttpScheme.HTTP);
			put(HttpScheme.HTTPS.getProtocol(), HttpScheme.HTTPS);
		}});

		/**
		 * Parse protocol to get the corresponding scheme.
		 *
		 * @param protocol The protocol.
		 * @return The scheme, {@code null} if it does not exist.
		 */
		private static HttpScheme parse(String protocol) {
			return VALUES.get(protocol.toLowerCase());
		}
	}
}
