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

package com.github.mjeanroy.junit.servers.client.it;

import static com.github.mjeanroy.junit.servers.client.it.HeaderUtils.APPLICATION_JSON;
import static com.github.mjeanroy.junit.servers.client.it.HeaderUtils.CONTENT_TYPE;
import static com.github.mjeanroy.junit.servers.utils.commons.Pair.pair;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.head;
import static com.github.tomakehurst.wiremock.client.WireMock.patch;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.put;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static java.util.Collections.singleton;

import com.github.mjeanroy.junit.servers.client.HttpMethod;
import com.github.mjeanroy.junit.servers.utils.commons.Pair;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.http.RequestMethod;
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder;

final class WireMockUtils {
	static void stubDefaultGetRequest(String endpoint) {
		stubGetRequest(endpoint, 200, "[{\"id\": 1, \"name\": \"John Doe\"}]");
	}

	static void stubGetRequest(String endpoint, int status, String body) {
		stubGetRequest(endpoint, status, body, pair(CONTENT_TYPE, APPLICATION_JSON));
	}

	static void stubGetRequest(String endpoint, int status, String body, Pair header) {
		ResponseDefinitionBuilder rsp = aResponse()
				.withStatus(status)
				.withBody(body);

		if (header != null) {
			rsp.withHeader(header.getO1(), header.getO2());
		}

		stubFor(get(urlEqualTo(endpoint)).willReturn(rsp));
	}

	static void stubHeadRequest(String endpoint, int status) {
		stubFor(head(urlEqualTo(endpoint))
				.willReturn(aResponse()
						.withStatus(status)
						.withHeader(CONTENT_TYPE, APPLICATION_JSON)));
	}

	static void stubPostRequest(String endpoint, int status, String body) {
		stubFor(post(urlEqualTo(endpoint))
				.willReturn(aResponse()
						.withStatus(status)
						.withHeader(CONTENT_TYPE, APPLICATION_JSON)
						.withBody(body)));
	}

	static void stubPutRequest(String endpoint, int status) {
		stubFor(put(urlEqualTo(endpoint))
				.willReturn(aResponse()
						.withStatus(status)
						.withHeader(CONTENT_TYPE, APPLICATION_JSON)));
	}

	static void stubPatchRequest(String endpoint, int status, String body) {
		stubFor(patch(urlEqualTo(endpoint))
				.willReturn(aResponse()
						.withStatus(status)
						.withHeader(CONTENT_TYPE, APPLICATION_JSON)
						.withBody(body)));
	}

	static void stubDeleteRequest(String endpoint, int status) {
		stubFor(delete(urlEqualTo(endpoint))
				.willReturn(aResponse()
						.withStatus(status)
						.withHeader(CONTENT_TYPE, APPLICATION_JSON)));
	}

	static void assertRequest(String endpoint, HttpMethod method) {
		RequestMethod rqMethod = new RequestMethod(method.name());
		RequestPatternBuilder rq = new RequestPatternBuilder(rqMethod, urlEqualTo(endpoint));
		WireMock.verify(1, rq);
	}

	static void assertRequestWithHeader(String endpoint, HttpMethod method, String headerName, String headerValue) {
		RequestMethod rqMethod = new RequestMethod(method.name());
		RequestPatternBuilder rq = new RequestPatternBuilder(rqMethod, urlEqualTo(endpoint));
		rq.withHeader(headerName, equalTo(headerValue));
		WireMock.verify(1, rq);
	}

	static void assertRequestWithBody(String endpoint, HttpMethod method, String body) {
		RequestMethod rqMethod = new RequestMethod(method.name());
		RequestPatternBuilder rq = new RequestPatternBuilder(rqMethod, urlEqualTo(endpoint));
		rq.withRequestBody(equalTo(body));
		WireMock.verify(1, rq);
	}

	static void assertRequestWithCookie(String endpoint, HttpMethod method, String cookieName, String cookieValue) {
		assertRequestWithCookies(endpoint, method, singleton(pair(cookieName, cookieValue)));
	}

	static void assertRequestWithCookies(String endpoint, HttpMethod method, Iterable<Pair> cookies) {
		RequestMethod rqMethod = new RequestMethod(method.name());
		RequestPatternBuilder rq = new RequestPatternBuilder(rqMethod, urlEqualTo(endpoint));

		for (Pair cookie : cookies) {
			rq.withCookie(cookie.getO1(), equalTo(cookie.getO2()));
		}

		WireMock.verify(1, rq);
	}
}
