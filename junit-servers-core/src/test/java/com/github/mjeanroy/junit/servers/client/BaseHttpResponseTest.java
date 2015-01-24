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

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static com.github.mjeanroy.junit.servers.client.HttpHeader.header;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests skeleton for http response implementations.
 */
public abstract class BaseHttpResponseTest {

	@Before
	public void setUp() throws Exception {
		onSetUp();
	}

	@Test
	public void it_should_get_request_duration() throws Exception {
		HttpResponse rsp = createHttpResponse();
		long duration = rsp.getRequestDuration();
		assertThat(duration).isEqualTo(1_000_000);
	}

	@Test
	public void it_should_get_request_duration_in_millis() throws Exception {
		HttpResponse rsp = createHttpResponse();
		long duration = rsp.getRequestDurationInMillis();
		assertThat(duration).isEqualTo(1_000);
	}

	@Test
	public void it_should_build_http_response() throws Exception {
		HttpResponse rsp = createHttpResponse();
		checkInternals(rsp);
	}

	@Test
	public void it_should_return_status_code() throws Exception {
		HttpResponse rsp = createHttpResponse();

		int status = 200;
		mockInternals(status, "foo", new HashMap<String, String>());

		int statusCode = rsp.status();
		assertThat(statusCode).isEqualTo(status);
	}

	@Test
	public void it_should_return_response_body() throws Exception {
		HttpResponse rsp = createHttpResponse();

		String body = "foo";
		mockInternals(200, body, new HashMap<String, String>());

		String responseBody = rsp.body();
		assertThat(responseBody).isEqualTo(body);
	}

	@Test
	public void it_should_return_header_value() throws Exception {
		String headerName = "foo";
		String headerValue = "bar";
		HttpResponse rsp = mockHeader(headerName, headerValue);

		HttpHeader responseHeaderValue = rsp.getHeader(headerName);

		assertThat(responseHeaderValue).isNotNull();
		assertThat(responseHeaderValue.getName()).isEqualTo(headerName);
		assertThat(responseHeaderValue.getValues()).isEqualTo(asList(headerValue));
	}

	@Test
	public void it_should_return_true_if_header_is_in_response() throws Exception {
		String name = "foo";
		HttpResponse rsp = mockHeader(name, "bar");
		boolean result = rsp.containsHeader(name);
		assertThat(result).isTrue();
	}

	@Test
	public void it_should_return_null_if_header_is_not_available() throws Exception {
		HttpResponse rsp = createHttpResponse();
		HttpHeader responseHeaderValue = rsp.getHeader("foo");
		assertThat(responseHeaderValue).isNull();
	}

	@Test
	public void it_should_return_false_if_header_is_not_available() throws Exception {
		HttpResponse rsp = createHttpResponse();
		boolean result = rsp.containsHeader("foo");
		assertThat(result).isFalse();
	}

	@Test
	public void it_should_return_true_if_etag_header_is_in_response() throws Exception {
		HttpResponse rsp = mockHeader("ETag", "foo");
		boolean result = rsp.hasETagHeader();
		assertThat(result).isTrue();
	}

	@Test
	public void it_should_return_false_if_etag_header_is_not_available() throws Exception {
		HttpResponse rsp = createHttpResponse();
		boolean result = rsp.hasETagHeader();
		assertThat(result).isFalse();
	}

	@Test
	public void it_should_return_etag_header() throws Exception {
		String headerName = "ETag";
		String headerValue = "foo";
		HttpResponse rsp = mockHeader(headerName, headerValue);

		HttpHeader header = rsp.getETag();

		assertThat(header)
				.isNotNull()
				.isEqualTo(header(headerName, headerValue));
	}

	@Test
	public void it_should_return_content_type_header() throws Exception {
		String headerName = "Content-Type";
		String headerValue = "application/json";
		HttpResponse rsp = mockHeader(headerName, headerValue);

		HttpHeader header = rsp.getContentType();

		assertThat(header)
				.isNotNull()
				.isEqualTo(header(headerName, headerValue));
	}

	@Test
	public void it_should_return_content_encoding_header() throws Exception {
		String headerName = "Content-Encoding";
		String headerValue = "gzip";
		HttpResponse rsp = mockHeader(headerName, headerValue);

		HttpHeader header = rsp.getContentEncoding();

		assertThat(header)
				.isNotNull()
				.isEqualTo(header(headerName, headerValue));
	}

	@Test
	public void it_should_return_location_header() throws Exception {
		String headerName = "Location";
		String headerValue = "http://localhost:8080/resources";
		HttpResponse rsp = mockHeader(headerName, headerValue);

		HttpHeader header = rsp.getLocation();

		assertThat(header)
				.isNotNull()
				.isEqualTo(header(headerName, headerValue));
	}

	@Test
	public void it_should_return_cache_control_header() throws Exception {
		String headerName = "Cache-Control";
		String headerValue = "max-age=3600";
		HttpResponse rsp = mockHeader(headerName, headerValue);

		HttpHeader header = rsp.getCacheControl();

		assertThat(header)
				.isNotNull()
				.isEqualTo(header(headerName, headerValue));
	}

	@Test
	public void it_should_return_last_modified_header() throws Exception {
		String headerName = "Last-Modified";
		String headerValue = "Tue, 15 Nov 1994 12:45:26 GMT";
		HttpResponse rsp = mockHeader(headerName, headerValue);

		HttpHeader header = rsp.getLastModified();

		assertThat(header)
				.isNotNull()
				.isEqualTo(header(headerName, headerValue));
	}

	@Test
	public void it_should_return_strict_transport_security_header() throws Exception {
		String headerName = "Strict-Transport-Security";
		String headerValue = "max-age=16070400; includeSubDomains";
		HttpResponse rsp = mockHeader(headerName, headerValue);

		HttpHeader header = rsp.getStrictTransportSecurity();

		assertThat(header)
				.isNotNull()
				.isEqualTo(header(headerName, headerValue));
	}

	@Test
	public void it_should_return_content_security_policy_header() throws Exception {
		String headerName = "Content-Security-Policy";
		String headerValue = "default-src 'self'";
		HttpResponse rsp = mockHeader(headerName, headerValue);

		HttpHeader header = rsp.getContentSecurityPolicy();

		assertThat(header)
				.isNotNull()
				.isEqualTo(header(headerName, headerValue));
	}

	@Test
	public void it_should_return_x_content_security_policy_header() throws Exception {
		String headerName = "X-Content-Security-Policy";
		String headerValue = "default-src 'self'";
		HttpResponse rsp = mockHeader(headerName, headerValue);

		HttpHeader header = rsp.getXContentSecurityPolicy();

		assertThat(header)
				.isNotNull()
				.isEqualTo(header(headerName, headerValue));
	}

	@Test
	public void it_should_return_x_webkit_csp_header() throws Exception {
		String headerName = "X-Webkit-CSP";
		String headerValue = "default-src 'self'";
		HttpResponse rsp = mockHeader(headerName, headerValue);

		HttpHeader header = rsp.getXWebkitCSP();

		assertThat(header)
				.isNotNull()
				.isEqualTo(header(headerName, headerValue));
	}

	@Test
	public void it_should_return_x_content_type_options_header() throws Exception {
		String headerName = "X-Content-Type-Options";
		String headerValue = "nosniff";
		HttpResponse rsp = mockHeader(headerName, headerValue);

		HttpHeader header = rsp.getXContentTypeOptions();

		assertThat(header)
				.isNotNull()
				.isEqualTo(header(headerName, headerValue));
	}

	@Test
	public void it_should_return_x_xss_protection_header() throws Exception {
		String headerName = "X-XSS-Protection";
		String headerValue = "1; mode=block";
		HttpResponse rsp = mockHeader(headerName, headerValue);

		HttpHeader header = rsp.getXXSSProtection();

		assertThat(header)
				.isNotNull()
				.isEqualTo(header(headerName, headerValue));
	}

	private HttpResponse mockHeader(String name, String value) throws Exception {
		HttpResponse rsp = createHttpResponse();
		Map<String, String> headers = new HashMap<>();
		headers.put(name, value);
		mockInternals(200, "foo", headers);
		return rsp;
	}

	/**
	 * Should create mock data during test setup.
	 *
	 * @throws Exception
	 */
	protected abstract void onSetUp() throws Exception;

	/**
	 * Should create a default http response.
	 *
	 * @return Default http response.
	 * @throws Exception
	 */
	protected abstract HttpResponse createHttpResponse() throws Exception;

	/**
	 * Should check that previously created http response
	 * is valid.
	 *
	 * @param rsp Http response.
	 * @throws Exception
	 */
	protected abstract void checkInternals(HttpResponse rsp) throws Exception;

	/**
	 * Should mock http response methods.
	 *
	 * @param status Http response status.
	 * @param body Http response body.
	 * @param headers Http response headers.
	 * @throws Exception
	 */
	protected abstract void mockInternals(int status, String body, Map<String, String> headers) throws Exception;
}
