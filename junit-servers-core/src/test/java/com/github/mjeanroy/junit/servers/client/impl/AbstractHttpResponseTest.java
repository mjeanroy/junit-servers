/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2017 <mickael.jeanroy@gmail.com>
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
import com.github.mjeanroy.junit.servers.utils.builders.AbstractHttpResponseBuilder;
import org.junit.Test;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

public abstract class AbstractHttpResponseTest<T extends AbstractHttpResponseBuilder<U, T>, U, V extends AbstractHttpResponse> {

	@Test
	public void it_should_get_request_duration() {
		long durationInMillis = 1L;
		long duration = durationInMillis * 1000;
		U delegate = getBuilder().build();
		V response = createHttpResponse(delegate, duration);
		assertThat(response.getRequestDuration()).isEqualTo(duration);
		assertThat(response.getRequestDurationInMillis()).isEqualTo(durationInMillis);
	}

	@Test
	public void it_should_get_status() {
		long duration = 1000L;
		int status = 204;
		U delegate = getBuilder().withStatus(status).build();
		V response = createHttpResponse(delegate, duration);
		assertThat(response.status()).isEqualTo(status);
	}

	@Test
	public void it_should_get_response_body() {
		long duration = 1000L;
		String body = "Hello World";
		U delegate = getBuilder().withBody(body).build();
		V response = createHttpResponse(delegate, duration);
		assertThat(response.body()).isEqualTo(body);
	}

	@Test
	public void it_should_get_all_headers() {
		long duration = 1000L;
		HttpHeader h1 = HttpHeader.header("Content-Type", "text/html; charset=utf-8");
		HttpHeader h2 = HttpHeader.header("Status", "200");
		U delegate = getBuilder()
				.withHeader(h1.getName(), h1.getFirstValue())
				.withHeader(h2.getName(), h2.getFirstValue())
				.build();

		V response = createHttpResponse(delegate, duration);
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
	public void it_should_get_all_headers_and_returns_empty_list_without_any_headers() {
		long duration = 1000L;
		U delegate = getBuilder().build();
		V response = createHttpResponse(delegate, duration);
		Collection<HttpHeader> headers = response.getHeaders();
		assertThat(headers).isNotNull().isEmpty();
	}

	@Test
	public void it_should_get_header() {
		long duration = 1000L;
		HttpHeader h1 = HttpHeader.header("Content-Type", "text/html; charset=utf-8");
		HttpHeader h2 = HttpHeader.header("Status", "200");
		U delegate = getBuilder()
				.withHeader(h1.getName(), h1.getFirstValue())
				.withHeader(h2.getName(), h2.getFirstValue())
				.build();

		V response = createHttpResponse(delegate, duration);
		HttpHeader header = response.getHeader(h1.getName());

		assertThat(header).isNotNull();
		assertThat(header.getName()).isEqualTo(h1.getName());
		assertThat(header.getValues()).isEqualTo(h1.getValues());
	}

	@Test
	public void it_should_get_header_case_insensitively() {
		long duration = 1000L;
		HttpHeader h1 = HttpHeader.header("Content-Type", "text/html; charset=utf-8");
		HttpHeader h2 = HttpHeader.header("Status", "200");
		U delegate = getBuilder()
				.withHeader(h1.getName(), h1.getFirstValue())
				.withHeader(h2.getName(), h2.getFirstValue())
				.build();

		V response = createHttpResponse(delegate, duration);
		HttpHeader header = response.getHeader(h1.getName());

		assertThat(response.getHeader(h1.getName().toLowerCase())).isEqualTo(header);
		assertThat(response.getHeader(h1.getName().toUpperCase())).isEqualTo(header);
	}

	@Test
	public void it_should_get_header_and_return_null_if_header_is_not_set() {
		long duration = 1000L;
		HttpHeader h1 = HttpHeader.header("Content-Type", "text/html; charset=utf-8");
		HttpHeader h2 = HttpHeader.header("Status", "200");
		U delegate = getBuilder()
				.withHeader(h1.getName(), h1.getFirstValue())
				.withHeader(h2.getName(), h2.getFirstValue())
				.build();

		V response = createHttpResponse(delegate, duration);
		HttpHeader header = response.getHeader("FooBar");

		assertThat(header).isNull();
	}

	@Test
	public void it_should_check_if_response_contains_header_case_insensitively() {
		long duration = 1000L;
		HttpHeader h1 = HttpHeader.header("Content-Type", "text/html; charset=utf-8");
		HttpHeader h2 = HttpHeader.header("Status", "200");
		U delegate = getBuilder()
				.withHeader(h1.getName(), h1.getFirstValue())
				.withHeader(h2.getName(), h2.getFirstValue())
				.build();

		V response = createHttpResponse(delegate, duration);

		assertThat(response.containsHeader(h1.getName())).isTrue();
		assertThat(response.containsHeader(h1.getName().toUpperCase())).isTrue();
		assertThat(response.containsHeader(h1.getName().toLowerCase())).isTrue();
	}

	@Test
	public void it_get_all_cookies() {
		long duration = 1000L;
		HttpHeader h1 = HttpHeader.header("Set-Cookie", asList(
				"hopsi=5b16593d9933d2325ad89633; Path=/",
				"ABT_force_signin_anonymous=ON; Max-Age=15552000; Expires=Thu, 10-Jan-2019 20:55:23 GMT; Path=/"
		));

		U delegate = getBuilder()
				.withHeader(h1.getName(), h1.getFirstValue())
				.withHeader(h1.getName(), h1.getLastValue())
				.build();

		V response = createHttpResponse(delegate, duration);
		List<Cookie> cookies = response.getCookies();

		assertThat(cookies).hasSize(2)
				.extracting("name", "value")
				.contains(
						tuple("hopsi", "5b16593d9933d2325ad89633"),
						tuple("ABT_force_signin_anonymous", "ON")
				);
	}

	@Test
	public void it_get_all_cookies_and_returns_empty_list_without_set_cookie_header() {
		long duration = 1000L;
		U delegate = getBuilder().build();
		V response = createHttpResponse(delegate, duration);
		List<Cookie> cookies = response.getCookies();
		assertThat(cookies).isNotNull().isEmpty();
	}

	@Test
	public void it_get_given_cookie() {
		long duration = 1000L;
		HttpHeader h1 = HttpHeader.header("Set-Cookie", asList(
				"hopsi=5b16593d9933d2325ad89633; Path=/",
				"ABT_force_signin_anonymous=ON; Max-Age=15552000; Expires=Thu, 10-Jan-2019 20:55:23 GMT; Path=/"
		));

		U delegate = getBuilder()
				.withHeader(h1.getName(), h1.getFirstValue())
				.withHeader(h1.getName(), h1.getLastValue())
				.build();

		V response = createHttpResponse(delegate, duration);
		Cookie cookie = response.getCookie("ABT_force_signin_anonymous");

		assertThat(cookie).isNotNull();
		assertThat(cookie.getName()).isEqualTo("ABT_force_signin_anonymous");
		assertThat(cookie.getValue()).isEqualTo("ON");
		assertThat(cookie.getMaxAge()).isEqualTo(15552000L);
		assertThat(cookie.getPath()).isEqualTo("/");
		assertThat(cookie.getDomain()).isNull();
	}

	@Test
	public void it_get_given_cookie_and_return_null_without_cookie() {
		long duration = 1000L;
		HttpHeader h1 = HttpHeader.header("Set-Cookie", asList(
				"hopsi=5b16593d9933d2325ad89633; Path=/",
				"ABT_force_signin_anonymous=ON; Max-Age=15552000; Expires=Thu, 10-Jan-2019 20:55:23 GMT; Path=/"
		));

		U delegate = getBuilder()
				.withHeader(h1.getName(), h1.getFirstValue())
				.withHeader(h1.getName(), h1.getLastValue())
				.build();

		V response = createHttpResponse(delegate, duration);
		Cookie cookie = response.getCookie("fake_cookie");
		assertThat(cookie).isNull();
	}

	@Test
	public void it_get_given_cookie_and_return_null_without_any_cookie() {
		long duration = 1000L;
		U delegate = getBuilder().build();
		V response = createHttpResponse(delegate, duration);
		Cookie cookie = response.getCookie("fake_cookie");
		assertThat(cookie).isNull();
	}

	@Test
	public void it_should_get_content_type_header() {
		long duration = 1000L;
		String name = "Content-Type";
		String value = "text/html; charset=utf-8";
		U delegate = getBuilder().withHeader(name, value).build();
		V response = createHttpResponse(delegate, duration);

		HttpHeader header = response.getContentType();

		assertThat(header).isNotNull();
		assertThat(header.getName()).isEqualTo(name);
		assertThat(header.getValues()).isEqualTo(singletonList(value));
	}

	@Test
	public void it_should_get_cache_control_header() {
		long duration = 1000L;
		String name = "Cache-Control";
		String value = "nocache";
		U delegate = getBuilder().withHeader(name, value).build();
		V response = createHttpResponse(delegate, duration);

		HttpHeader header = response.getCacheControl();

		assertThat(header).isNotNull();
		assertThat(header.getName()).isEqualTo(name);
		assertThat(header.getValues()).isEqualTo(singletonList(value));
	}

	@Test
	public void it_should_get_content_encoding_header() {
		long duration = 1000L;
		String name = "Content-Encoding";
		String value = "gzip";
		U delegate = getBuilder().withHeader(name, value).build();
		V response = createHttpResponse(delegate, duration);

		HttpHeader header = response.getContentEncoding();

		assertThat(header).isNotNull();
		assertThat(header.getName()).isEqualTo(name);
		assertThat(header.getValues()).isEqualTo(singletonList(value));
	}

	@Test
	public void it_should_get_etag_header() {
		long duration = 1000L;
		String name = "ETag";
		String value = UUID.randomUUID().toString();
		U delegate = getBuilder().withHeader(name, value).build();
		V response = createHttpResponse(delegate, duration);

		HttpHeader header = response.getETag();

		assertThat(header).isNotNull();
		assertThat(header.getName()).isEqualTo(name);
		assertThat(header.getValues()).isEqualTo(singletonList(value));
	}

	@Test
	public void it_should_get_location_header() {
		long duration = 1000L;
		String name = "Location";
		String value = "http://localhost:8080";
		U delegate = getBuilder().withHeader(name, value).build();
		V response = createHttpResponse(delegate, duration);

		HttpHeader header = response.getLocation();

		assertThat(header).isNotNull();
		assertThat(header.getName()).isEqualTo(name);
		assertThat(header.getValues()).isEqualTo(singletonList(value));
	}

	@Test
	public void it_should_get_strict_transport_security_header() {
		long duration = 1000L;
		String name = "Strict-Transport-Security";
		String value = "max-age=3600; includeSubDomains; preload";
		U delegate = getBuilder().withHeader(name, value).build();
		V response = createHttpResponse(delegate, duration);

		HttpHeader header = response.getStrictTransportSecurity();

		assertThat(header).isNotNull();
		assertThat(header.getName()).isEqualTo(name);
		assertThat(header.getValues()).isEqualTo(singletonList(value));
	}

	@Test
	public void it_should_get_x_xss_protection_header() {
		long duration = 1000L;
		String name = "X-XSS-Protection";
		String value = "0";
		U delegate = getBuilder().withHeader(name, value).build();
		V response = createHttpResponse(delegate, duration);

		HttpHeader header = response.getXXSSProtection();

		assertThat(header).isNotNull();
		assertThat(header.getName()).isEqualTo(name);
		assertThat(header.getValues()).isEqualTo(singletonList(value));
	}

	@Test
	public void it_should_get_content_security_policy_header() {
		long duration = 1000L;
		String name = "Content-Security-Policy";
		String value = "default-src 'self'";
		U delegate = getBuilder().withHeader(name, value).build();
		V response = createHttpResponse(delegate, duration);

		HttpHeader header = response.getContentSecurityPolicy();

		assertThat(header).isNotNull();
		assertThat(header.getName()).isEqualTo(name);
		assertThat(header.getValues()).isEqualTo(singletonList(value));
	}

	@Test
	public void it_should_get_x_content_security_policy_header() {
		long duration = 1000L;
		String name = "X-Content-Security-Policy";
		String value = "default-src 'self'";
		U delegate = getBuilder().withHeader(name, value).build();
		V response = createHttpResponse(delegate, duration);

		HttpHeader header = response.getXContentSecurityPolicy();

		assertThat(header).isNotNull();
		assertThat(header.getName()).isEqualTo(name);
		assertThat(header.getValues()).isEqualTo(singletonList(value));
	}

	@Test
	public void it_should_get_x_webkit_csp_header() {
		long duration = 1000L;
		String name = "X-Webkit-CSP";
		String value = "default-src 'self'";
		U delegate = getBuilder().withHeader(name, value).build();
		V response = createHttpResponse(delegate, duration);

		HttpHeader header = response.getXWebkitCSP();

		assertThat(header).isNotNull();
		assertThat(header.getName()).isEqualTo(name);
		assertThat(header.getValues()).isEqualTo(singletonList(value));
	}

	@Test
	public void it_should_get_x_content_type_options_header() {
		long duration = 1000L;
		String name = "X-Content-Type-Options";
		String value = "nosniff";
		U delegate = getBuilder().withHeader(name, value).build();
		V response = createHttpResponse(delegate, duration);

		HttpHeader header = response.getXContentTypeOptions();

		assertThat(header).isNotNull();
		assertThat(header.getName()).isEqualTo(name);
		assertThat(header.getValues()).isEqualTo(singletonList(value));
	}

	@Test
	public void it_should_get_last_modified_header() {
		long duration = 1000L;
		String name = "Last-Modified";
		String value = "Wed, 21 Oct 2015 07:28:00 GMT";
		U delegate = getBuilder().withHeader(name, value).build();
		V response = createHttpResponse(delegate, duration);

		HttpHeader header = response.getLastModified();

		assertThat(header).isNotNull();
		assertThat(header.getName()).isEqualTo(name);
		assertThat(header.getValues()).isEqualTo(singletonList(value));
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
}
