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

package com.github.mjeanroy.junit.servers.client.impl;

import com.github.mjeanroy.junit.servers.client.Cookie;
import com.github.mjeanroy.junit.servers.client.HttpHeader;
import com.github.mjeanroy.junit.servers.client.HttpResponse;
import com.github.mjeanroy.junit.servers.exceptions.HttpClientException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.github.mjeanroy.junit.servers.client.Cookies.read;
import static com.github.mjeanroy.junit.servers.client.HttpHeaders.CACHE_CONTROL;
import static com.github.mjeanroy.junit.servers.client.HttpHeaders.CONTENT_ENCODING;
import static com.github.mjeanroy.junit.servers.client.HttpHeaders.CONTENT_SECURITY_POLICY;
import static com.github.mjeanroy.junit.servers.client.HttpHeaders.CONTENT_TYPE;
import static com.github.mjeanroy.junit.servers.client.HttpHeaders.ETAG;
import static com.github.mjeanroy.junit.servers.client.HttpHeaders.LAST_MODIFIED;
import static com.github.mjeanroy.junit.servers.client.HttpHeaders.LOCATION;
import static com.github.mjeanroy.junit.servers.client.HttpHeaders.SET_COOKIE;
import static com.github.mjeanroy.junit.servers.client.HttpHeaders.STRICT_TRANSPORT_SECURITY;
import static com.github.mjeanroy.junit.servers.client.HttpHeaders.X_CONTENT_SECURITY_POLICY;
import static com.github.mjeanroy.junit.servers.client.HttpHeaders.X_CONTENT_TYPE_OPTIONS;
import static com.github.mjeanroy.junit.servers.client.HttpHeaders.X_WEBKIT_CSP;
import static com.github.mjeanroy.junit.servers.client.HttpHeaders.X_XSS_PROTECTION;
import static com.github.mjeanroy.junit.servers.commons.Preconditions.notBlank;
import static com.github.mjeanroy.junit.servers.commons.Preconditions.positive;
import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;

/**
 * Abstract skeleton of {@link HttpResponse} interface.
 *
 * <p>
 *
 * <strong>This abstract class is not part of the public API and should not be used publicly.</strong>
 */
public abstract class AbstractHttpResponse implements HttpResponse {

	/**
	 * The original request duration.
	 */
	private final long duration;

	/**
	 * The lock used inside {@link #readResponseBody()} to avoid concurrent
	 * writing to {@link #_body} value.
	 */
	private final Lock readResponseBodyLock;

	/**
	 * The response _body element, that will be computed the first time {@link #readResponseBody()} is
	 * called.
	 */
	private String _body;

	/**
	 * Create the partial HTTP response implementation.
	 *
	 * @param duration Original request duration.
	 */
	protected AbstractHttpResponse(long duration) {
		this.duration = positive(duration, "Duration must be positive");
		this.readResponseBodyLock = new ReentrantLock();
	}

	@Override
	public long getRequestDuration() {
		return duration;
	}

	@Override
	public long getRequestDurationInMillis() {
		return getRequestDuration() / 1000 / 1000;
	}

	@Override
	public String body() {
		readResponseBodyLock.lock();

		try {
			readBodyIfNotAlreadyComputed();
			return _body;
		} catch (IOException ex) {
			throw new HttpClientException(ex);
		} finally {
			readResponseBodyLock.unlock();
		}
	}

	/**
	 * Read HTTP Response _body as a {@link String} and update {@link #_body} value.
	 *
	 * @throws IOException If an error occurred while reading _body.
	 */
	private void readBodyIfNotAlreadyComputed() throws IOException {
		if (_body == null) {
			_body = readResponseBody();
		}
	}

	/**
	 * Read HTTP Response _body as a {@link String}.
	 *
	 * @return The response _body.
	 * @throws IOException If an error occurred while reading _body.
	 */
	protected abstract String readResponseBody() throws IOException;

	@Override
	public boolean containsHeader(String name) {
		return getHeader(name) != null;
	}

	@Override
	public Cookie getCookie(String name) {
		notBlank(name, "name");

		HttpHeader header = getHeader(SET_COOKIE);
		if (header == null) {
			return null;
		}

		// Check each cookie to find cookie by its name
		for (String value : header.getValues()) {
			Cookie cookie = read(value);
			if (cookie.getName().equals(name)) {
				return cookie;
			}
		}

		// No matching
		return null;
	}

	@Override
	public List<Cookie> getCookies() {
		HttpHeader header = getHeader(SET_COOKIE);
		if (header == null) {
			return emptyList();
		}

		List<String> values = header.getValues();
		List<Cookie> cookies = new ArrayList<>(values.size());
		for (String value : values) {
			cookies.add(read(value));
		}

		return unmodifiableList(cookies);
	}

	@Override
	public HttpHeader getETag() {
		return getHeader(ETAG);
	}

	@Override
	public HttpHeader getContentType() {
		return getHeader(CONTENT_TYPE);
	}

	@Override
	public HttpHeader getContentEncoding() {
		return getHeader(CONTENT_ENCODING);
	}

	@Override
	public HttpHeader getLocation() {
		return getHeader(LOCATION);
	}

	@Override
	public HttpHeader getCacheControl() {
		return getHeader(CACHE_CONTROL);
	}

	@Override
	public HttpHeader getLastModified() {
		return getHeader(LAST_MODIFIED);
	}

	@Override
	public HttpHeader getStrictTransportSecurity() {
		return getHeader(STRICT_TRANSPORT_SECURITY);
	}

	@Override
	public HttpHeader getContentSecurityPolicy() {
		return getHeader(CONTENT_SECURITY_POLICY);
	}

	@Override
	public HttpHeader getXContentSecurityPolicy() {
		return getHeader(X_CONTENT_SECURITY_POLICY);
	}

	@Override
	public HttpHeader getXWebkitCSP() {
		return getHeader(X_WEBKIT_CSP);
	}

	@Override
	public HttpHeader getXContentTypeOptions() {
		return getHeader(X_CONTENT_TYPE_OPTIONS);
	}

	@Override
	public HttpHeader getXXSSProtection() {
		return getHeader(X_XSS_PROTECTION);
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}

		if (o instanceof AbstractHttpResponse) {
			AbstractHttpResponse r = (AbstractHttpResponse) o;
			return r.canEqual(this) && Objects.equals(duration, r.duration);
		}

		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(duration);
	}

	/**
	 * Ensure that given object o can be equal to this.
	 *
	 * @param o The HTTP Response.
	 * @return The flag.
	 */
	protected boolean canEqual(AbstractHttpResponse o) {
		return o instanceof AbstractHttpResponse;
	}
}
