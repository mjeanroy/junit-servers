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

package com.github.mjeanroy.junit.servers.client.impl;

import com.github.mjeanroy.junit.servers.client.Cookie;
import com.github.mjeanroy.junit.servers.client.HttpHeader;
import com.github.mjeanroy.junit.servers.client.HttpResponse;
import com.github.mjeanroy.junit.servers.utils.builders.AbstractHttpResponseBuilder;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

/**
 * Structure for testing of {@link HttpResponse} implementations.
 * @param <T> Builder implementation to create {@link HttpResponse} implementations.
 * @param <U> The original HTTP Response implementation (apache, okhttp, etc.).
 * @param <V> The {@link HttpResponse} implementation.
 */
public abstract class AbstractHttpResponseImplTest<T extends AbstractHttpResponseBuilder<U, T>, U, V extends AbstractHttpResponse> {

	@Test
	void it_should_get_request_duration() {
		final long durationInMillis = 1L;
		final long durationInNano = durationInMillis * 1000 * 1000;
		final U delegate = getBuilder().build();

		final V response = createHttpResponse(delegate, durationInNano);

		assertThat(response.getRequestDuration()).isEqualTo(durationInNano);
		assertThat(response.getRequestDurationInMillis()).isEqualTo(durationInMillis);
	}

	@Test
	void it_should_get_status() {
		final long duration = 1000L;
		final int status = 204;
		final U delegate = getBuilder().withStatus(status).build();

		final V response = createHttpResponse(delegate, duration);

		assertThat(response.status()).isEqualTo(status);
	}

	@Test
	void it_should_get_response_body() {
		final long duration = 1000L;
		final String body = "Hello World";
		final U delegate = getBuilder().withBody(body).build();

		final V response = createHttpResponse(delegate, duration);

		assertThat(response.body()).isEqualTo(body);
	}

	@Test
	void it_should_get_all_headers() {
		final HttpHeader h1 = HttpHeader.header("Content-Type", "text/html; charset=utf-8");
		final HttpHeader h2 = HttpHeader.header("Status", "200");
		final V response = createHttpResponseWithHeaders(h1, h2);

		final Collection<HttpHeader> headers = response.getHeaders();

		assertThat(headers)
			.hasSize(2)
			.extracting("name", "values")
			.contains(
				tuple(h1.getName(), h1.getValues()),
				tuple(h2.getName(), h2.getValues())
			);
	}

	@Test
	void it_should_get_all_headers_and_returns_empty_list_without_any_headers() {
		final V response = createHttpResponseWithHeaders();

		final Collection<HttpHeader> headers = response.getHeaders();

		assertThat(headers).isNotNull().isEmpty();
	}

	@Test
	void it_should_get_header() {
		final HttpHeader h1 = HttpHeader.header("Content-Type", "text/html; charset=utf-8");
		final HttpHeader h2 = HttpHeader.header("Status", "200");
		final V response = createHttpResponseWithHeaders(h1, h2);

		final HttpHeader header = response.getHeader(h1.getName());

		assertHeader(header, h1.getName(), h1.getValues());
	}

	@Test
	void it_should_get_header_case_insensitively() {
		final HttpHeader h1 = HttpHeader.header("Content-Type", "text/html; charset=utf-8");
		final HttpHeader h2 = HttpHeader.header("Status", "200");
		final V response = createHttpResponseWithHeaders(h1, h2);

		final HttpHeader header = response.getHeader(h1.getName());

		assertThat(response.getHeader(h1.getName().toLowerCase())).isEqualTo(header);
		assertThat(response.getHeader(h1.getName().toUpperCase())).isEqualTo(header);
	}

	@Test
	void it_should_get_header_and_return_null_if_header_is_not_set() {
		final HttpHeader h1 = HttpHeader.header("Content-Type", "text/html; charset=utf-8");
		final HttpHeader h2 = HttpHeader.header("Status", "200");
		final V response = createHttpResponseWithHeaders(h1, h2);

		final HttpHeader header = response.getHeader("FooBar");

		assertThat(header).isNull();
	}

	@Test
	void it_should_check_if_response_contains_header_case_insensitively() {
		final HttpHeader h1 = HttpHeader.header("Content-Type", "text/html; charset=utf-8");
		final HttpHeader h2 = HttpHeader.header("Status", "200");

		final V response = createHttpResponseWithHeaders(h1, h2);

		assertThat(response.containsHeader(h1.getName())).isTrue();
		assertThat(response.containsHeader(h1.getName().toUpperCase())).isTrue();
		assertThat(response.containsHeader(h1.getName().toLowerCase())).isTrue();
	}

	@Test
	void it_get_all_cookies() {
		final V response = createHttpResponseWithHeaders(givenCookieHeader());

		List<Cookie> cookies = response.getCookies();

		assertThat(cookies).hasSize(2)
			.extracting("name", "value")
			.contains(
				tuple("hopsi", "5b16593d9933d2325ad89633"),
				tuple("ABT_force_signin_anonymous", "ON")
			);
	}

	@Test
	void it_get_all_cookies_and_returns_empty_list_without_set_cookie_header() {
		final V response = createHttpResponseWithHeaders();

		final List<Cookie> cookies = response.getCookies();

		assertThat(cookies).isNotNull().isEmpty();
	}

	@Test
	void it_get_given_cookie() {
		final V response = createHttpResponseWithHeaders(givenCookieHeader());

		final Cookie cookie = response.getCookie("ABT_force_signin_anonymous");

		assertThat(cookie).isNotNull();
		assertThat(cookie.getName()).isEqualTo("ABT_force_signin_anonymous");
		assertThat(cookie.getValue()).isEqualTo("ON");
		assertThat(cookie.getMaxAge()).isEqualTo(15552000L);
		assertThat(cookie.getPath()).isEqualTo("/");
		assertThat(cookie.getDomain()).isNull();
	}

	@Test
	void it_get_given_cookie_and_return_null_without_cookie() {
		final V response = createHttpResponseWithHeaders(givenCookieHeader());

		final Cookie cookie = response.getCookie("fake_cookie");

		assertThat(cookie).isNull();
	}

	@Test
	void it_get_given_cookie_and_return_null_without_any_cookie() {
		final V response = createHttpResponseWithHeaders();

		final Cookie cookie = response.getCookie("fake_cookie");

		assertThat(cookie).isNull();
	}

	@Test
	void it_should_get_content_type_header() {
		final String name = "Content-Type";
		final String value = "text/html; charset=utf-8";
		final V response = createHttpResponseWithHeader(name, value);

		final HttpHeader header = response.getContentType();

		assertHeader(header, name, value);
	}

	@Test
	void it_should_get_cache_control_header() {
		final String name = "Cache-Control";
		final String value = "nocache";
		final V response = createHttpResponseWithHeader(name, value);

		final HttpHeader header = response.getCacheControl();

		assertHeader(header, name, value);
	}

	@Test
	void it_should_get_content_encoding_header() {
		final String name = "Content-Encoding";
		final String value = "gzip";
		final V response = createHttpResponseWithHeader(name, value);

		HttpHeader header = response.getContentEncoding();

		assertHeader(header, name, value);
	}

	@Test
	void it_should_get_etag_header() {
		final String name = "ETag";
		final String value = UUID.randomUUID().toString();
		final V response = createHttpResponseWithHeader(name, value);

		final HttpHeader header = response.getETag();

		assertHeader(header, name, value);
	}

	@Test
	void it_should_get_location_header() {
		final String name = "Location";
		final String value = "http://localhost:8080";
		final V response = createHttpResponseWithHeader(name, value);

		final HttpHeader header = response.getLocation();

		assertHeader(header, name, value);
	}

	@Test
	void it_should_get_strict_transport_security_header() {
		final String name = "Strict-Transport-Security";
		final String value = "max-age=3600; includeSubDomains; preload";
		final V response = createHttpResponseWithHeader(name, value);

		final HttpHeader header = response.getStrictTransportSecurity();

		assertHeader(header, name, value);
	}

	@Test
	void it_should_get_x_xss_protection_header() {
		final String name = "X-XSS-Protection";
		final String value = "0";
		final V response = createHttpResponseWithHeader(name, value);

		final HttpHeader header = response.getXXSSProtection();

		assertHeader(header, name, value);
	}

	@Test
	void it_should_get_content_security_policy_header() {
		final String name = "Content-Security-Policy";
		final String value = "default-src 'self'";
		final V response = createHttpResponseWithHeader(name, value);

		final HttpHeader header = response.getContentSecurityPolicy();

		assertHeader(header, name, value);
	}

	@Test
	void it_should_get_x_content_security_policy_header() {
		final String name = "X-Content-Security-Policy";
		final String value = "default-src 'self'";
		final V response = createHttpResponseWithHeader(name, value);

		final HttpHeader header = response.getXContentSecurityPolicy();

		assertHeader(header, name, value);
	}

	@Test
	void it_should_get_x_webkit_csp_header() {
		final String name = "X-Webkit-CSP";
		final String value = "default-src 'self'";
		final V response = createHttpResponseWithHeader(name, value);

		final HttpHeader header = response.getXWebkitCSP();

		assertHeader(header, name, value);
	}

	@Test
	void it_should_get_x_content_type_options_header() {
		final String name = "X-Content-Type-Options";
		final String value = "nosniff";
		final V response = createHttpResponseWithHeader(name, value);

		final HttpHeader header = response.getXContentTypeOptions();

		assertHeader(header, name, value);
	}

	@Test
	void it_should_get_last_modified_header() {
		final String name = "Last-Modified";
		final String value = "Wed, 21 Oct 2015 07:28:00 GMT";
		final V response = createHttpResponseWithHeader(name, value);

		final HttpHeader header = response.getLastModified();

		assertHeader(header, name, value);
	}

	/**
	 * Get a builder instance that will be used to build instance of
	 * delegated HTTP Response.
	 *
	 * @return The builder.
	 */
	protected abstract T getBuilder();

	/**
	 * Create the HTTP response to be tested.
	 *
	 * @param delegate The delegated (internal implementation).
	 * @param duration The request duration.
	 * @return The HTTP response instance.
	 */
	protected abstract V createHttpResponse(U delegate, long duration);

	/**
	 * Create HTTP response with given headers.
	 *
	 * @param headers The headers.
	 * @return The HTTP Response.
	 */
	private V createHttpResponseWithHeaders(HttpHeader... headers) {
		final long duration = 1000L;
		final T builder = getBuilder();

		for (HttpHeader h : headers) {
			for (String value : h.getValues()) {
				builder.withHeader(h.getName(), value);
			}
		}

		final U delegate = builder.build();
		return createHttpResponse(delegate, duration);
	}

	/**
	 * Create HTTP Response with given header value.
	 *
	 * @param name Header name.
	 * @param value Header value.
	 * @return The HTTP Response.
	 */
	private V createHttpResponseWithHeader(String name, String value) {
		final long duration = 1000L;
		final U delegate = getBuilder().withHeader(name, value).build();
		return createHttpResponse(delegate, duration);
	}

	/**
	 * Create default cookie header.
	 *
	 * @return The cookie header.
	 */
	private static HttpHeader givenCookieHeader() {
		return HttpHeader.header("Set-Cookie", asList(
			"hopsi=5b16593d9933d2325ad89633; Path=/",
			"ABT_force_signin_anonymous=ON; Max-Age=15552000; Expires=Thu, 10-Jan-2019 20:55:23 GMT; Path=/"
		));
	}

	/**
	 * Assert that given {@code header} is not {@code null} and has expected name and expected single value.
	 *
	 * @param header The header.
	 * @param expectedName The expected name.
	 * @param expectedValue The expected value.
	 */
	private static void assertHeader(HttpHeader header, String expectedName, String expectedValue) {
		assertHeader(header, expectedName, singletonList(expectedValue));
	}

	/**
	 * Assert that given {@code header} is not {@code null} and has expected name and expected values.
	 *
	 * @param header The header.
	 * @param expectedName The expected name.
	 * @param expectedValues The expected values.
	 */
	private static void assertHeader(HttpHeader header, String expectedName, List<String> expectedValues) {
		assertThat(header).isNotNull();
		assertThat(header.getName()).isEqualTo(expectedName);
		assertThat(header.getValues()).isEqualTo(expectedValues);
	}
}
