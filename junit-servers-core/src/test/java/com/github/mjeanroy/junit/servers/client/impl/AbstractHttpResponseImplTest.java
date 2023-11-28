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
		long durationInMillis = 1L;
		long durationInNano = durationInMillis * 1000 * 1000;
		U delegate = getBuilder().build();

		V response = createHttpResponse(delegate, durationInNano);

		assertThat(response.getRequestDuration()).isEqualTo(durationInNano);
		assertThat(response.getRequestDurationInMillis()).isEqualTo(durationInMillis);
	}

	@Test
	void it_should_get_status() {
		long duration = 1000L;
		int status = 204;
		U delegate = getBuilder().withStatus(status).build();

		V response = createHttpResponse(delegate, duration);

		assertThat(response.status()).isEqualTo(status);
	}

	@Test
	void it_should_get_response_body() {
		long duration = 1000L;
		String body = "Hello World";
		U delegate = getBuilder().withBody(body).build();

		V response = createHttpResponse(delegate, duration);

		assertThat(response.body()).isEqualTo(body);
	}

	@Test
	void it_should_get_all_headers() {
		HttpHeader h1 = HttpHeader.header("Content-Type", "text/html; charset=utf-8");
		HttpHeader h2 = HttpHeader.header("Status", "200");
		V response = createHttpResponseWithHeaders(h1, h2);

		Collection<HttpHeader> headers = response.getHeaders();

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
		V response = createHttpResponseWithHeaders();

		Collection<HttpHeader> headers = response.getHeaders();

		assertThat(headers).isNotNull().isEmpty();
	}

	@Test
	void it_should_get_header() {
		HttpHeader h1 = HttpHeader.header("Content-Type", "text/html; charset=utf-8");
		HttpHeader h2 = HttpHeader.header("Status", "200");
		V response = createHttpResponseWithHeaders(h1, h2);

		HttpHeader header = response.getHeader(h1.getName());

		assertHeader(header, h1.getName(), h1.getValues());
	}

	@Test
	void it_should_get_header_case_insensitively() {
		HttpHeader h1 = HttpHeader.header("Content-Type", "text/html; charset=utf-8");
		HttpHeader h2 = HttpHeader.header("Status", "200");
		V response = createHttpResponseWithHeaders(h1, h2);

		HttpHeader header = response.getHeader(h1.getName());

		assertThat(response.getHeader(h1.getName().toLowerCase())).isEqualTo(header);
		assertThat(response.getHeader(h1.getName().toUpperCase())).isEqualTo(header);
	}

	@Test
	void it_should_get_header_and_return_null_if_header_is_not_set() {
		HttpHeader h1 = HttpHeader.header("Content-Type", "text/html; charset=utf-8");
		HttpHeader h2 = HttpHeader.header("Status", "200");
		V response = createHttpResponseWithHeaders(h1, h2);

		HttpHeader header = response.getHeader("FooBar");

		assertThat(header).isNull();
	}

	@Test
	void it_should_check_if_response_contains_header_case_insensitively() {
		HttpHeader h1 = HttpHeader.header("Content-Type", "text/html; charset=utf-8");
		HttpHeader h2 = HttpHeader.header("Status", "200");

		V response = createHttpResponseWithHeaders(h1, h2);

		assertThat(response.containsHeader(h1.getName())).isTrue();
		assertThat(response.containsHeader(h1.getName().toUpperCase())).isTrue();
		assertThat(response.containsHeader(h1.getName().toLowerCase())).isTrue();
	}

	@Test
	void it_get_all_cookies() {
		V response = createHttpResponseWithHeaders(givenCookieHeader());

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
		V response = createHttpResponseWithHeaders();

		List<Cookie> cookies = response.getCookies();

		assertThat(cookies).isNotNull().isEmpty();
	}

	@Test
	void it_get_given_cookie() {
		V response = createHttpResponseWithHeaders(givenCookieHeader());

		Cookie cookie = response.getCookie("ABT_force_signin_anonymous");

		assertThat(cookie).isNotNull();
		assertThat(cookie.getName()).isEqualTo("ABT_force_signin_anonymous");
		assertThat(cookie.getValue()).isEqualTo("ON");
		assertThat(cookie.getMaxAge()).isEqualTo(15552000L);
		assertThat(cookie.getPath()).isEqualTo("/");
		assertThat(cookie.getDomain()).isNull();
	}

	@Test
	void it_get_given_cookie_and_return_null_without_cookie() {
		V response = createHttpResponseWithHeaders(givenCookieHeader());

		Cookie cookie = response.getCookie("fake_cookie");

		assertThat(cookie).isNull();
	}

	@Test
	void it_get_given_cookie_and_return_null_without_any_cookie() {
		V response = createHttpResponseWithHeaders();

		Cookie cookie = response.getCookie("fake_cookie");

		assertThat(cookie).isNull();
	}

	@Test
	void it_should_get_content_type_header() {
		String name = "Content-Type";
		String value = "text/html; charset=utf-8";
		V response = createHttpResponseWithHeader(name, value);

		HttpHeader header = response.getContentType();

		assertHeader(header, name, value);
	}

	@Test
	void it_should_get_cache_control_header() {
		String name = "Cache-Control";
		String value = "nocache";
		V response = createHttpResponseWithHeader(name, value);

		HttpHeader header = response.getCacheControl();

		assertHeader(header, name, value);
	}

	@Test
	void it_should_get_content_encoding_header() {
		String name = "Content-Encoding";
		String value = "gzip";
		V response = createHttpResponseWithHeader(name, value);

		HttpHeader header = response.getContentEncoding();

		assertHeader(header, name, value);
	}

	@Test
	void it_should_get_etag_header() {
		String name = "ETag";
		String value = UUID.randomUUID().toString();
		V response = createHttpResponseWithHeader(name, value);

		HttpHeader header = response.getETag();

		assertHeader(header, name, value);
	}

	@Test
	void it_should_get_location_header() {
		String name = "Location";
		String value = "http://localhost:8080";
		V response = createHttpResponseWithHeader(name, value);

		HttpHeader header = response.getLocation();

		assertHeader(header, name, value);
	}

	@Test
	void it_should_get_strict_transport_security_header() {
		String name = "Strict-Transport-Security";
		String value = "max-age=3600; includeSubDomains; preload";
		V response = createHttpResponseWithHeader(name, value);

		HttpHeader header = response.getStrictTransportSecurity();

		assertHeader(header, name, value);
	}

	@Test
	void it_should_get_x_xss_protection_header() {
		String name = "X-XSS-Protection";
		String value = "0";
		V response = createHttpResponseWithHeader(name, value);

		HttpHeader header = response.getXXSSProtection();

		assertHeader(header, name, value);
	}

	@Test
	void it_should_get_content_security_policy_header() {
		String name = "Content-Security-Policy";
		String value = "default-src 'self'";
		V response = createHttpResponseWithHeader(name, value);

		HttpHeader header = response.getContentSecurityPolicy();

		assertHeader(header, name, value);
	}

	@Test
	void it_should_get_x_content_security_policy_header() {
		String name = "X-Content-Security-Policy";
		String value = "default-src 'self'";
		V response = createHttpResponseWithHeader(name, value);

		HttpHeader header = response.getXContentSecurityPolicy();

		assertHeader(header, name, value);
	}

	@Test
	void it_should_get_x_webkit_csp_header() {
		String name = "X-Webkit-CSP";
		String value = "default-src 'self'";
		V response = createHttpResponseWithHeader(name, value);

		HttpHeader header = response.getXWebkitCSP();

		assertHeader(header, name, value);
	}

	@Test
	void it_should_get_x_content_type_options_header() {
		String name = "X-Content-Type-Options";
		String value = "nosniff";
		V response = createHttpResponseWithHeader(name, value);

		HttpHeader header = response.getXContentTypeOptions();

		assertHeader(header, name, value);
	}

	@Test
	void it_should_get_last_modified_header() {
		String name = "Last-Modified";
		String value = "Wed, 21 Oct 2015 07:28:00 GMT";
		V response = createHttpResponseWithHeader(name, value);

		HttpHeader header = response.getLastModified();

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
		long duration = 1000L;
		T builder = getBuilder();

		for (HttpHeader h : headers) {
			for (String value : h.getValues()) {
				builder.withHeader(h.getName(), value);
			}
		}

		U delegate = builder.build();
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
		long duration = 1000L;
		U delegate = getBuilder().withHeader(name, value).build();
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
