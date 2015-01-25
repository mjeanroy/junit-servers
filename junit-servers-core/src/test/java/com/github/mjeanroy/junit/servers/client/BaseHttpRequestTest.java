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

import com.github.mjeanroy.junit.servers.utils.Pair;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static com.github.mjeanroy.junit.servers.client.Cookie.cookie;
import static com.github.mjeanroy.junit.servers.client.HttpParameter.param;
import static com.github.mjeanroy.junit.servers.utils.Pair.pair;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests skeleton for http request implementation.
 */
public abstract class BaseHttpRequestTest {

	@Before
	public void setUp() throws Exception {
		onSetUp();
	}

	@Test
	public void it_should_create_http_request() throws Exception {
		String url = "http://localhost:8080/foo";
		HttpMethod httpMethod = HttpMethod.GET;
		HttpRequest request = createHttpRequest(httpMethod, url);
		checkInternals(request, httpMethod, url);
	}

	@Test
	public void it_should_add_header() throws Exception {
		HttpRequest request = createDefaultRequest();

		String headerName = "foo";
		String headerValue = "bar";
		request.addHeader(headerName, headerValue);

		checkHeader(request, headerName, headerValue);
	}

	@Test
	public void it_should_add_form_param() throws Exception {
		HttpRequest request = createDefaultRequest();

		String paramName = "foo";
		String paramValue = "bar";
		request.addFormParam(paramName, paramValue);

		checkFormParam(request, paramName, paramValue);
	}

	@Test
	public void it_should_add_form_params() throws Exception {
		HttpRequest request = createDefaultRequest();

		HttpParameter param1 = param("foo1", "bar1");
		HttpParameter param2 = param("foo2", "bar2");
		request.addFormParams(param1, param2);

		checkFormParam(request, param1.getName(), param1.getValue());
		checkFormParam(request, param2.getName(), param2.getValue());
	}

	@Test
	public void it_should_add_header_requested_with() throws Exception {
		HttpRequest request = createDefaultRequest();
		request.asXmlHttpRequest();
		checkHeader(request, "X-Requested-With", "XMLHttpRequest");
	}

	@Test
	public void it_should_add_user_agent() throws Exception {
		HttpRequest request = createDefaultRequest();
		String ua = "foo";
		request.withUserAgent(ua);
		checkHeader(request, "User-Agent", ua);
	}

	@Test
	public void it_should_add_header_content_type_json() throws Exception {
		HttpRequest request = createDefaultRequest();
		request.asJson();
		checkHeader(request, "Content-Type", "application/json");
	}

	@Test
	public void it_should_add_header_content_type_xml() throws Exception {
		HttpRequest request = createDefaultRequest();
		request.asXml();
		checkHeader(request, "Content-Type", "application/xml");
	}

	@Test
	public void it_should_add_header_content_type_form_url_encoded() throws Exception {
		HttpRequest request = createDefaultRequest();
		request.asFormUrlEncoded();
		checkHeader(request, "Content-Type", "application/x-www-form-urlencoded");
	}

	@Test
	public void it_should_add_header_content_type_multipart_form_data() throws Exception {
		HttpRequest request = createDefaultRequest();
		request.asMultipartFormData();
		checkHeader(request, "Content-Type", "multipart/form-data");
	}

	@Test
	public void it_should_add_header_accept_json() throws Exception {
		HttpRequest request = createDefaultRequest();
		request.acceptJson();
		checkHeader(request, "Accept", "application/json");
	}

	@Test
	public void it_should_add_header_accept_xml() throws Exception {
		HttpRequest request = createDefaultRequest();
		request.acceptXml();
		checkHeader(request, "Accept", "application/xml");
	}

	@Test
	public void it_should_add_if_none_match_header() throws Exception {
		HttpRequest request = createDefaultRequest();
		String etag = "foo";
		request.addIfNoneMatch(etag);
		checkHeader(request, "If-None-Match", etag);
	}

	@Test
	public void it_should_add_if_match_header() throws Exception {
		HttpRequest request = createDefaultRequest();
		String etag = "foo";
		request.addIfMatch(etag);
		checkHeader(request, "If-Match", etag);
	}

	@Test
	public void it_should_add_if_modified_since_header() throws Exception {
		HttpRequest request = createDefaultRequest();

		Date date = new Date();
		date.setTime(1610576581000L);

		request.addIfModifiedSince(date);
		checkHeader(request, "If-Modified-Since", "Wed, 13 Jan 2021 22:23:01 GMT");
	}

	@Test
	public void it_should_add_if_unmodified_since_header() throws Exception {
		HttpRequest request = createDefaultRequest();

		Date date = new Date();
		date.setTime(1610576581000L);

		request.addIfUnmodifiedSince(date);
		checkHeader(request, "If-Unmodified-Since", "Wed, 13 Jan 2021 22:23:01 GMT");
	}

	@Test
	public void it_should_accept_languages() throws Exception {
		String lang = "FR";
		HttpRequest request = createDefaultRequest();
		request.acceptLanguage(lang);
		checkHeader(request, "Accept-Language", lang);
	}

	@Test
	public void it_should_add_origin() throws Exception {
		String origin = "http://www.example-social-network.com";
		HttpRequest request = createDefaultRequest();
		request.addOrigin(origin);
		checkHeader(request, "Origin", origin);
	}

	@Test
	public void it_should_add_referer() throws Exception {
		String referer = "http://en.wikipedia.org/wiki/Main_Page";
		HttpRequest request = createDefaultRequest();
		request.addReferer(referer);
		checkHeader(request, "Referer", referer);
	}

	@Test
	public void it_should_add_accept_encoding() throws Exception {
		String encoding = "gzip, deflate";
		HttpRequest request = createDefaultRequest();
		request.addAcceptEncoding(encoding);
		checkHeader(request, "Accept-Encoding", encoding);
	}

	@Test
	public void it_should_add_accept_encoding_gzip() throws Exception {
		HttpRequest request = createDefaultRequest();
		request.acceptGzip();
		checkHeader(request, "Accept-Encoding", "gzip, deflate");
	}

	@Test
	public void it_should_override_method_request() throws Exception {
		HttpRequest request = createDefaultRequest();
		request.addXHttpMethodOverride("PUT");
		checkHeader(request, "X-Http-Method-Override", "PUT");
	}

	@Test
	public void it_should_override_put_request() throws Exception {
		HttpRequest request = createDefaultRequest();
		request.overridePut();
		checkHeader(request, "X-Http-Method-Override", "PUT");
	}

	@Test
	public void it_should_override_delete_request() throws Exception {
		HttpRequest request = createDefaultRequest();
		request.overrideDelete();
		checkHeader(request, "X-Http-Method-Override", "DELETE");
	}

	@Test
	public void it_should_add_csrf_token() throws Exception {
		String token = "token";
		HttpRequest request = createDefaultRequest();
		request.addCsrfToken(token);
		checkHeader(request, "X-Csrf-Token", token);
	}

	@Test
	public void it_should_add_query_param() throws Exception {
		HttpRequest request = createDefaultRequest();

		String paramName = "foo";
		String paramValue = "bar";
		request.addQueryParam(paramName, paramValue);

		checkQueryParam(request, paramName, paramValue);
	}

	@Test
	public void it_should_add_query_params() throws Exception {
		HttpRequest request = createDefaultRequest();

		HttpParameter param1 = param("foo1", "bar1");
		HttpParameter param2 = param("foo2", "bar2");
		request.addQueryParams(param1, param2);

		checkQueryParam(request, param1.getName(), param1.getValue());
		checkQueryParam(request, param2.getName(), param2.getValue());
	}

	@Test
	public void it_should_set_request_body() throws Exception {
		HttpRequest request = createDefaultRequest();

		String requestBody = "{foo: 'bar'}";
		request.setBody(requestBody);

		checkRequestBody(request, requestBody);
	}

	@Test
	public void it_should_add_cookie() throws Exception {
		Cookie cookie = cookie("foo", "bar", "foo.com", "/bar", 0, 0, true, true);
		HttpRequest request = createDefaultRequest();
		request.addCookie(cookie);
		checkCookie(request, cookie);
	}

	@Test
	public void it_should_execute_request() throws Exception {
		HttpRequest request = createDefaultRequest();

		HttpResponse httpResponse = fakeExecution(request, new ExecutionStrategy() {
			@Override
			public HttpResponse execute(HttpRequest request) {
				return request.execute();
			}
		});

		assertThat(httpResponse).isNotNull();
		assertThat(httpResponse.getRequestDuration()).isPositive();
		checkExecution(httpResponse);
	}

	@Test
	public void it_should_execute_request_as_json() throws Exception {
		HttpRequest request = createDefaultRequest();

		HttpResponse httpResponse = fakeExecution(request, new ExecutionStrategy() {
			@Override
			public HttpResponse execute(HttpRequest request) {
				return request.executeJson();
			}
		});

		assertThat(httpResponse).isNotNull();
		assertThat(httpResponse.getRequestDuration()).isPositive();
		checkExecution(httpResponse,
				pair("Content-Type", "application/json"),
				pair("Accept", "application/json")
		);
	}

	@Test
	public void it_should_execute_request_as_xml() throws Exception {
		HttpRequest request = createDefaultRequest();

		HttpResponse httpResponse = fakeExecution(request, new ExecutionStrategy() {
			@Override
			public HttpResponse execute(HttpRequest request) {
				return request.executeXml();
			}
		});

		assertThat(httpResponse).isNotNull();
		assertThat(httpResponse.getRequestDuration()).isPositive();
		checkExecution(httpResponse,
				pair("Content-Type", "application/xml"),
				pair("Accept", "application/xml")
		);
	}

	/**
	 * Should create mock data during test setup.
	 *
	 * @throws Exception
	 */
	protected abstract void onSetUp() throws Exception;

	/**
	 * Should create http request implementation.
	 * This http request will be used to check http request
	 * constructor.
	 *
	 * @param httpMethod Http method.
	 * @param url Http request url.
	 * @return Created http request.
	 * @throws Exception
	 */
	protected abstract HttpRequest createHttpRequest(HttpMethod httpMethod, String url) throws Exception;

	/**
	 * Should check that created http request is valid according
	 * to given http method and url.
	 * It should also check that internal data are valid (probably empty
	 * query params and empty headers).
	 *
	 * @param request Created http request.
	 * @param httpMethod Http method used to create http request.
	 * @param url Url used to create http request.
	 * @throws Exception
	 */
	protected abstract void checkInternals(HttpRequest request, HttpMethod httpMethod, String url) throws Exception;

	/**
	 * Create default http request used in tests, except test of constructor.
	 *
	 * @return Http request.
	 * @throws Exception
	 */
	protected abstract HttpRequest createDefaultRequest() throws Exception;

	/**
	 * Execute http request using given strategy.
	 * Strategy must execute http request using request
	 * methods.
	 *
	 * @param httpRequest Http request.
	 * @param executionStrategy Execution strategy.
	 * @return Produced http response.
	 * @throws Exception
	 */
	protected abstract HttpResponse fakeExecution(HttpRequest httpRequest, ExecutionStrategy executionStrategy) throws Exception;

	/**
	 * Check that http request get expected query parameter.
	 *
	 * @param httpRequest Http request.
	 * @param name Parameter name.
	 * @param value Parameter value.
	 * @throws Exception
	 */
	protected abstract void checkQueryParam(HttpRequest httpRequest, String name, String value) throws Exception;

	/**
	 * Check that http request get expected getHeader.
	 *
	 * @param httpRequest Http request.
	 * @param name Header name.
	 * @param value Header value.
	 * @throws Exception
	 */
	protected abstract void checkHeader(HttpRequest httpRequest, String name, String value) throws Exception;

	/**
	 * Check that http request get expected form parameter.
	 *
	 * @param httpRequest Http request.
	 * @param name Parameter name.
	 * @param value Parameter value.
	 * @throws Exception
	 */
	protected abstract void checkFormParam(HttpRequest httpRequest, String name, String value) throws Exception;

	/**
	 * Check that http request get expected request body.
	 *
	 * @param httpRequest Http request.
	 * @param body Request body.
	 * @throws Exception
	 */
	protected abstract void checkRequestBody(HttpRequest httpRequest, String body) throws Exception;

	/**
	 * Check that http request get expected cookie.
	 *
	 * @param httpRequest Http request.
	 * @param cookie Cookie to check.
	 * @throws Exception
	 */
	protected abstract void checkCookie(HttpRequest httpRequest, Cookie cookie) throws Exception;

	/**
	 * Check that http request execution is valid.
	 *
	 * @param httpResponse Produced http response.
	 * @param headers Headers, if http request should had some headers before execution.
	 * @throws Exception
	 */
	protected abstract void checkExecution(HttpResponse httpResponse, Pair... headers) throws Exception;

	protected static interface ExecutionStrategy {
		HttpResponse execute(HttpRequest request);
	}
}
