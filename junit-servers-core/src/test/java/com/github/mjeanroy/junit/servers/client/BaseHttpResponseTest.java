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

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests skeleton for http response implementations.
 */
public abstract class BaseHttpResponseTest {

	@Before
	public void setUp() throws Exception {
		onSetUp();
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
		HttpResponse rsp = createHttpResponse();

		String headerName = "foo";
		String headerValue = "bar";
		Map<String, String> headers = new HashMap<>();
		headers.put(headerName, headerValue);
		mockInternals(200, "foo", headers);

		HttpHeader responseHeaderValue = rsp.getHeader(headerName);

		assertThat(responseHeaderValue).isNotNull();
		assertThat(responseHeaderValue.getName()).isEqualTo(headerName);
		assertThat(responseHeaderValue.getValues()).isEqualTo(asList(headerValue));
	}

	@Test
	public void it_should_return_true_if_header_is_in_response() throws Exception {
		HttpResponse rsp = createHttpResponse();

		String headerName = "foo";
		String headerValue = "bar";
		Map<String, String> headers = new HashMap<>();
		headers.put(headerName, headerValue);
		mockInternals(200, "foo", headers);

		boolean result = rsp.containsHeader(headerName);

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
		HttpResponse rsp = createHttpResponse();

		String headerName = "ETag";
		String headerValue = "foo";
		Map<String, String> headers = new HashMap<>();
		headers.put(headerName, headerValue);
		mockInternals(200, "foo", headers);

		boolean result = rsp.hasETagHeader();

		assertThat(result).isTrue();
	}

	@Test
	public void it_should_return_false_if_etag_header_is_not_available() throws Exception {
		HttpResponse rsp = createHttpResponse();
		boolean result = rsp.hasETagHeader();
		assertThat(result).isFalse();
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
