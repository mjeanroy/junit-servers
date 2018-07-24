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

package com.github.mjeanroy.junit.servers.client.it;

import static com.github.mjeanroy.junit.servers.client.it.HeaderTestUtils.APPLICATION_JSON;
import static com.github.mjeanroy.junit.servers.client.it.HeaderTestUtils.CONTENT_TYPE;
import static com.github.mjeanroy.junit.servers.utils.commons.Pair.pair;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.request;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static java.util.Collections.singleton;

import java.util.Collection;
import java.util.List;

import com.github.mjeanroy.junit.servers.client.HttpMethod;
import com.github.mjeanroy.junit.servers.utils.commons.Pair;
import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.http.HttpHeader;
import com.github.tomakehurst.wiremock.http.HttpHeaders;
import com.github.tomakehurst.wiremock.http.RequestMethod;
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder;
import com.github.tomakehurst.wiremock.matching.UrlPattern;

final class WireMockTestUtils {
	private static final String GET = "GET";
	private static final String POST = "POST";
	private static final String PUT = "PUT";
	private static final String PATCH = "PATCH";
	private static final String DELETE = "DELETE";
	private static final String HEAD = "HEAD";

	/**
	 * Stub default request:
	 *
	 * <ul>
	 *   <li>GET request.</li>
	 *   <li>Returns HTTP status code 200.</li>
	 *   <li>Returns response with JSON content type.</li>
	 *   <li>Returns response with body: {@code "[]"}</li>
	 * </ul>
	 *
	 * @param endpoint Endpoint to stub.
	 */
	static void stubDefaultRequest(String endpoint) {
		final int status = 200;
		final Collection<Pair> headers = singleton(pair(CONTENT_TYPE, APPLICATION_JSON));
		final String body = "[]";
		stubRequest(GET, endpoint, status, headers, body);
	}

	/**
	 * Stub {@code GET} request.
	 *
	 * @param endpoint Request endpoint.
	 * @param status Response HTTP status code.
	 * @param headers Response headers.
	 * @param body Response body.
	 */
	static void stubGetRequest(String endpoint, int status, Collection<Pair> headers, String body) {
		stubRequest(GET, endpoint, status, headers, body);
	}

	/**
	 * Stub {@code HEAD} request.
	 *
	 * @param endpoint Request endpoint.
	 * @param status Response HTTP status code.
	 * @param headers Response headers.
	 */
	static void stubHeadRequest(String endpoint, int status, Collection<Pair> headers) {
		stubRequest(HEAD, endpoint, status, headers);
	}

	/**
	 * Stub {@code POST} request.
	 *
	 * @param endpoint Request endpoint.
	 * @param status Response HTTP status code.
	 * @param headers Response headers.
	 * @param body Response body.
	 */
	static void stubPostRequest(String endpoint, int status, Collection<Pair> headers, String body) {
		stubRequest(POST, endpoint, status, headers, body);
	}

	/**
	 * Stub {@code PUT} request.
	 *
	 * @param endpoint Request endpoint.
	 * @param status Response HTTP status code.
	 * @param headers Response headers.
	 */
	static void stubPutRequest(String endpoint, int status, Collection<Pair> headers) {
		stubRequest(PUT, endpoint, status, headers);
	}

	/**
	 * Stub {@code PATCH} request.
	 *
	 * @param endpoint Request endpoint.
	 * @param status Response HTTP status code.
	 * @param headers Response headers.
	 * @param body Response body.
	 */
	static void stubPatchRequest(String endpoint, int status, Collection<Pair> headers, String body) {
		stubRequest(PATCH, endpoint, status, headers, body);
	}

	/**
	 * Stub {@code DELETE} request.
	 *
	 * @param endpoint Request endpoint.
	 * @param status Response HTTP status code.
	 * @param headers Response headers.
	 */
	static void stubDeleteRequest(String endpoint, int status, Collection<Pair> headers) {
		stubRequest(DELETE, endpoint, status, headers);
	}

	/**
	 * Verify that a given request has been triggered.
	 *
	 * @param endpoint Request endpoint.
	 * @param method Request method.
	 */
	static void assertRequest(String endpoint, HttpMethod method) {
		UrlPattern urlPattern = urlEqualTo(endpoint);
		RequestMethod rqMethod = new RequestMethod(method.name());
		RequestPatternBuilder rq = new RequestPatternBuilder(rqMethod, urlPattern);
		WireMock.verify(1, rq);
	}

	/**
	 * Verify that a given request has been triggered.
	 *
	 * @param endpoint Request endpoint.
	 * @param method Request method.
	 * @param headerName Header name.
	 * @param headerValue Header value.
	 */
	static void assertRequestWithHeader(String endpoint, HttpMethod method, String headerName, String headerValue) {
		UrlPattern urlPattern = urlEqualTo(endpoint);
		RequestMethod rqMethod = new RequestMethod(method.name());
		RequestPatternBuilder rq = new RequestPatternBuilder(rqMethod, urlPattern);
		rq.withHeader(headerName, equalTo(headerValue));
		WireMock.verify(1, rq);
	}

	/**
	 * Verify that a given request has been triggered.
	 *
	 * @param endpoint Request endpoint.
	 * @param method Request method.
	 * @param body Request body.
	 */
	static void assertRequestWithBody(String endpoint, HttpMethod method, String body) {
		UrlPattern urlPattern = urlEqualTo(endpoint);
		RequestMethod rqMethod = new RequestMethod(method.name());
		RequestPatternBuilder rq = new RequestPatternBuilder(rqMethod, urlPattern);
		rq.withRequestBody(equalTo(body));
		WireMock.verify(1, rq);
	}

	/**
	 * Verify that a given request has been triggered.
	 *
	 * @param endpoint Request endpoint.
	 * @param method Request method.
	 * @param cookieName Cookie name sent in HTTP request.
	 * @param cookieValue Cookie value sent in HTTP request.
	 */
	static void assertRequestWithCookie(String endpoint, HttpMethod method, String cookieName, String cookieValue) {
		assertRequestWithCookies(endpoint, method, singleton(pair(cookieName, cookieValue)));
	}

	/**
	 * Verify that a given request has been triggered.
	 *
	 * @param endpoint Request endpoint.
	 * @param method Request method.
	 * @param cookies Cookies sent in HTTP request.
	 */
	static void assertRequestWithCookies(String endpoint, HttpMethod method, Iterable<Pair> cookies) {
		UrlPattern urlPattern = urlEqualTo(endpoint);
		RequestMethod rqMethod = new RequestMethod(method.name());
		RequestPatternBuilder rq = new RequestPatternBuilder(rqMethod, urlPattern);

		for (Pair cookie : cookies) {
			String cookieName = cookie.getO1();
			String cookieValue = cookie.getO2().get(0);
			rq.withCookie(cookieName, equalTo(cookieValue));
		}

		WireMock.verify(1, rq);
	}

	private static void stubRequest(String method, String endpoint, int status, Collection<Pair> headers) {
		stubRequest(method, endpoint, status, headers, null);
	}

	private static void stubRequest(String method, String endpoint, int status, Collection<Pair> headers, String body) {
		UrlPattern urlPattern = urlEqualTo(endpoint);
		MappingBuilder request = request(method, urlPattern);

		ResponseDefinitionBuilder response = aResponse().withStatus(status);

		HttpHeaders httpHeaders = new HttpHeaders();

		for (Pair header : headers) {
			String name = header.getO1();
			List<String> values = header.getO2();
			HttpHeader h = new HttpHeader(name, values);
			httpHeaders = httpHeaders.plus(h);
		}

		response.withHeaders(httpHeaders);

		if (body != null) {
			response.withBody(body);
		}

		stubFor(request.willReturn(response));
	}
}
