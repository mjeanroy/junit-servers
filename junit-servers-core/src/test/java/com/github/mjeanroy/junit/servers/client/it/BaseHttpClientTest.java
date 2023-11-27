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

package com.github.mjeanroy.junit.servers.client.it;

import com.github.mjeanroy.junit.servers.client.Cookie;
import com.github.mjeanroy.junit.servers.client.Cookies;
import com.github.mjeanroy.junit.servers.client.HttpClient;
import com.github.mjeanroy.junit.servers.client.HttpClientConfiguration;
import com.github.mjeanroy.junit.servers.client.HttpClientStrategy;
import com.github.mjeanroy.junit.servers.client.HttpHeader;
import com.github.mjeanroy.junit.servers.client.HttpMethod;
import com.github.mjeanroy.junit.servers.client.HttpParameter;
import com.github.mjeanroy.junit.servers.client.HttpRequest;
import com.github.mjeanroy.junit.servers.client.HttpRequestBody;
import com.github.mjeanroy.junit.servers.client.HttpResponse;
import com.github.mjeanroy.junit.servers.servers.EmbeddedServer;
import com.github.mjeanroy.junit.servers.utils.builders.EmbeddedServerMockBuilder;
import com.github.mjeanroy.junit.servers.utils.commons.Function;
import com.github.mjeanroy.junit.servers.utils.commons.MapperFunction;
import com.github.mjeanroy.junit.servers.utils.commons.Pair;
import com.github.mjeanroy.junit.servers.utils.jupiter.WireMockTest;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

import static com.github.mjeanroy.junit.servers.client.HttpParameter.param;
import static com.github.mjeanroy.junit.servers.client.HttpRequestBodies.formUrlEncodedBody;
import static com.github.mjeanroy.junit.servers.client.HttpRequestBodies.jsonBody;
import static com.github.mjeanroy.junit.servers.client.HttpRequestBodies.multipartBuilder;
import static com.github.mjeanroy.junit.servers.client.HttpRequestBodies.requestBody;
import static com.github.mjeanroy.junit.servers.client.it.HeaderTestUtils.ACCEPT;
import static com.github.mjeanroy.junit.servers.client.it.HeaderTestUtils.ACCEPT_ENCODING;
import static com.github.mjeanroy.junit.servers.client.it.HeaderTestUtils.ACCEPT_LANGUAGE;
import static com.github.mjeanroy.junit.servers.client.it.HeaderTestUtils.APPLICATION_JSON;
import static com.github.mjeanroy.junit.servers.client.it.HeaderTestUtils.APPLICATION_XML;
import static com.github.mjeanroy.junit.servers.client.it.HeaderTestUtils.CACHE_CONTROL;
import static com.github.mjeanroy.junit.servers.client.it.HeaderTestUtils.CONTENT_ENCODING;
import static com.github.mjeanroy.junit.servers.client.it.HeaderTestUtils.CONTENT_SECURITY_POLICY;
import static com.github.mjeanroy.junit.servers.client.it.HeaderTestUtils.CONTENT_TYPE;
import static com.github.mjeanroy.junit.servers.client.it.HeaderTestUtils.ETAG;
import static com.github.mjeanroy.junit.servers.client.it.HeaderTestUtils.IF_MATCH;
import static com.github.mjeanroy.junit.servers.client.it.HeaderTestUtils.IF_MODIFIED_SINCE;
import static com.github.mjeanroy.junit.servers.client.it.HeaderTestUtils.IF_NONE_MATCH;
import static com.github.mjeanroy.junit.servers.client.it.HeaderTestUtils.IF_UNMODIFIED_SINCE;
import static com.github.mjeanroy.junit.servers.client.it.HeaderTestUtils.LAST_MODIFIED;
import static com.github.mjeanroy.junit.servers.client.it.HeaderTestUtils.LOCATION;
import static com.github.mjeanroy.junit.servers.client.it.HeaderTestUtils.ORIGIN;
import static com.github.mjeanroy.junit.servers.client.it.HeaderTestUtils.REFERER;
import static com.github.mjeanroy.junit.servers.client.it.HeaderTestUtils.SET_COOKIE;
import static com.github.mjeanroy.junit.servers.client.it.HeaderTestUtils.STRICT_TRANSPORT_SECURITY;
import static com.github.mjeanroy.junit.servers.client.it.HeaderTestUtils.USER_AGENT;
import static com.github.mjeanroy.junit.servers.client.it.HeaderTestUtils.X_CONTENT_SECURITY_POLICY;
import static com.github.mjeanroy.junit.servers.client.it.HeaderTestUtils.X_CONTENT_TYPE_OPTIONS;
import static com.github.mjeanroy.junit.servers.client.it.HeaderTestUtils.X_CSRF_TOKEN;
import static com.github.mjeanroy.junit.servers.client.it.HeaderTestUtils.X_HTTP_METHOD_OVERRIDE;
import static com.github.mjeanroy.junit.servers.client.it.HeaderTestUtils.X_REQUESTED_WITH;
import static com.github.mjeanroy.junit.servers.client.it.HeaderTestUtils.X_WEBKIT_CSP;
import static com.github.mjeanroy.junit.servers.client.it.HeaderTestUtils.X_XSS_PROTECTION;
import static com.github.mjeanroy.junit.servers.client.it.HttpTestUtils.encodeFormParam;
import static com.github.mjeanroy.junit.servers.client.it.HttpTestUtils.encodePath;
import static com.github.mjeanroy.junit.servers.client.it.HttpTestUtils.encodeQueryParam;
import static com.github.mjeanroy.junit.servers.client.it.HttpTestUtils.utcDate;
import static com.github.mjeanroy.junit.servers.client.it.WireMockTestUtils.assertRequest;
import static com.github.mjeanroy.junit.servers.client.it.WireMockTestUtils.assertRequestWithBody;
import static com.github.mjeanroy.junit.servers.client.it.WireMockTestUtils.assertRequestWithCookie;
import static com.github.mjeanroy.junit.servers.client.it.WireMockTestUtils.assertRequestWithCookies;
import static com.github.mjeanroy.junit.servers.client.it.WireMockTestUtils.assertRequestWithHeader;
import static com.github.mjeanroy.junit.servers.client.it.WireMockTestUtils.assertUploadRequest;
import static com.github.mjeanroy.junit.servers.client.it.WireMockTestUtils.stubDefaultRequest;
import static com.github.mjeanroy.junit.servers.client.it.WireMockTestUtils.stubDeleteRequest;
import static com.github.mjeanroy.junit.servers.client.it.WireMockTestUtils.stubGetRequest;
import static com.github.mjeanroy.junit.servers.client.it.WireMockTestUtils.stubHeadRequest;
import static com.github.mjeanroy.junit.servers.client.it.WireMockTestUtils.stubPatchRequest;
import static com.github.mjeanroy.junit.servers.client.it.WireMockTestUtils.stubPostRequest;
import static com.github.mjeanroy.junit.servers.client.it.WireMockTestUtils.stubPutRequest;
import static com.github.mjeanroy.junit.servers.client.it.WireMockTestUtils.stubUploadRequest;
import static com.github.mjeanroy.junit.servers.testing.HttpTestUtils.url;
import static com.github.mjeanroy.junit.servers.testing.IoTestUtils.getFileFromClasspath;
import static com.github.mjeanroy.junit.servers.utils.commons.Pair.pair;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singleton;
import static java.util.Collections.singletonList;
import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

@WireMockTest
public abstract class BaseHttpClientTest {

	private static final String ENDPOINT = "/people";

	private EmbeddedServer<?> server;
	private String scheme;
	private String host;
	private int port;
	private HttpClient client;

	@BeforeEach
	void setUp(WireMockServer wireMockServer) {
		scheme = "http";
		host = "localhost";
		port = wireMockServer.port();
		server = new EmbeddedServerMockBuilder()
			.withScheme(scheme)
			.withHost(host)
			.withPort(port)
			.build();
	}

	@AfterEach
	void tearDown() {
		if (client != null) {
			client.destroy();
		}
	}

	@Test
	void testRequestUrl() {
		final String endpoint = ENDPOINT;
		final HttpRequest rq = createDefaultClient()
			.prepareGet(endpoint)
			.acceptJson()
			.asXmlHttpRequest();

		assertThat(rq.getEndpoint()).isNotNull();
		assertThat(rq.getEndpoint().getScheme()).isEqualTo(scheme);
		assertThat(rq.getEndpoint().getHost()).isEqualTo(host);
		assertThat(rq.getEndpoint().getPort()).isEqualTo(port);
		assertThat(rq.getEndpoint().getPath()).isEqualTo(endpoint);

		assertThat(rq.getMethod()).isEqualTo(HttpMethod.GET);
	}

	@Test
	void testGet() {
		final String endpoint = ENDPOINT;
		final int status = 200;
		final Collection<Pair> headers = singleton(pair(CONTENT_TYPE, APPLICATION_JSON));
		final String body = "[{\"id\": 1, \"name\": \"John Doe\"}]";

		stubGetRequest(endpoint, status, headers, body);

		final HttpResponse rsp = createDefaultClient()
			.prepareGet(endpoint)
			.acceptJson()
			.asXmlHttpRequest()
			.execute();

		assertRequest(endpoint, HttpMethod.GET);
		assertThat(rsp.status()).isEqualTo(status);
		assertThat(rsp.body()).isEqualTo(body);
		assertThat(rsp.getContentType().getFirstValue()).isEqualTo(APPLICATION_JSON);
		assertThat(rsp.getContentType().getLastValue()).isEqualTo(APPLICATION_JSON);
	}

	@Test
	void testGetReadingBodyTwice() {
		final String endpoint = ENDPOINT;
		final int status = 200;
		final Collection<Pair> headers = singleton(pair(CONTENT_TYPE, APPLICATION_JSON));
		final String body = "[{\"id\": 1, \"name\": \"John Doe\"}]";

		stubGetRequest(endpoint, status, headers, body);

		final HttpResponse rsp = createDefaultClient()
			.prepareGet(endpoint)
			.acceptJson()
			.asXmlHttpRequest()
			.execute();

		final String r1 = rsp.body();
		final String r2 = rsp.body();
		assertThat(r1).isEqualTo(r2);
	}

	@Test
	void testGetWithFullEndpoint() {
		final String endpoint = ENDPOINT;
		final String rqUrl = url(scheme, host, port, endpoint);
		final int status = 200;
		final Collection<Pair> headers = singleton(pair(CONTENT_TYPE, APPLICATION_JSON));
		final String body = "[{\"id\": 1, \"name\": \"John Doe\"}]";

		stubGetRequest(endpoint, status, headers, body);

		final HttpResponse rsp = createDefaultClient()
			.prepareGet(rqUrl)
			.acceptJson()
			.asXmlHttpRequest()
			.execute();

		assertRequest(endpoint, HttpMethod.GET);
		assertThat(rsp.status()).isEqualTo(status);
		assertThat(rsp.body()).isEqualTo(body);
		assertThat(rsp.getContentType().getFirstValue()).isEqualTo(APPLICATION_JSON);
		assertThat(rsp.getContentType().getLastValue()).isEqualTo(APPLICATION_JSON);
	}

	@Test
	void testGetWithNonEncodedPath() {
		final String endpoint = ENDPOINT + "/john doe";
		final String encodedPath = encodePath(endpoint);
		final int status = 200;
		final Collection<Pair> headers = singleton(pair(CONTENT_TYPE, APPLICATION_JSON));
		final String body = "[{\"id\": 1, \"name\": \"John Doe\"}]";

		stubGetRequest(encodedPath, status, headers, body);

		final HttpResponse rsp = createDefaultClient()
			.prepareGet(endpoint)
			.acceptJson()
			.asXmlHttpRequest()
			.execute();

		assertRequest(encodedPath, HttpMethod.GET);
		assertThat(rsp.status()).isEqualTo(status);
		assertThat(rsp.body()).isEqualTo(body);
		assertThat(rsp.getContentType().getFirstValue()).isEqualTo(APPLICATION_JSON);
		assertThat(rsp.getContentType().getLastValue()).isEqualTo(APPLICATION_JSON);
	}

	@Test
	void testHead() {
		final String endpoint = ENDPOINT;
		final int status = 200;
		final Collection<Pair> headers = singleton(pair(CONTENT_TYPE, APPLICATION_JSON));

		stubHeadRequest(endpoint, status, headers);

		final HttpResponse rsp = createDefaultClient()
			.prepareHead(endpoint)
			.acceptJson()
			.asXmlHttpRequest()
			.execute();

		assertRequest(endpoint, HttpMethod.HEAD);
		assertThat(rsp.status()).isEqualTo(status);
		assertThat(rsp.body()).isNullOrEmpty();
		assertThat(rsp.getContentType().getFirstValue()).isEqualTo(APPLICATION_JSON);
		assertThat(rsp.getContentType().getLastValue()).isEqualTo(APPLICATION_JSON);
	}

	@Test
	void testPost() {
		final String endpoint = ENDPOINT;
		final int status = 201;
		final Collection<Pair> headers = singleton(pair(CONTENT_TYPE, APPLICATION_JSON));
		final String body = "{\"id\": 1, \"name\": \"Jane Doe\"}";

		stubPostRequest(endpoint, status, headers, body);

		final HttpResponse rsp = createDefaultClient()
			.preparePost(endpoint)
			.acceptJson()
			.asXmlHttpRequest()
			.setBody(jsonBody("{\"name\": \"Jane Doe\"}"))
			.execute();

		assertRequest(endpoint, HttpMethod.POST);
		assertThat(rsp.status()).isEqualTo(status);
		assertThat(rsp.body()).isEqualTo(body);
		assertThat(rsp.getContentType().getFirstValue()).isEqualTo(APPLICATION_JSON);
		assertThat(rsp.getContentType().getLastValue()).isEqualTo(APPLICATION_JSON);
	}

	@Test
	void testPostWithJsonBodyString() {
		final String endpoint = ENDPOINT;
		final int status = 201;
		final String rawBody = "{\"id\": 1, \"name\": \"Jane Doe\"}";
		final HttpRequestBody body = jsonBody(rawBody);
		final Collection<Pair> expectedHeaders = singleton(pair(CONTENT_TYPE, APPLICATION_JSON));

		stubPostRequest(endpoint, status, expectedHeaders, rawBody);

		final HttpResponse rsp = createDefaultClient()
			.preparePost(endpoint)
			.acceptJson()
			.asXmlHttpRequest()
			.setBody(body)
			.execute();

		assertRequest(endpoint, HttpMethod.POST);
		assertThat(rsp.status()).isEqualTo(status);
		assertThat(rsp.body()).isEqualTo(rawBody);
		assertThat(rsp.getContentType().getFirstValue()).isEqualTo(APPLICATION_JSON);
		assertThat(rsp.getContentType().getLastValue()).isEqualTo(APPLICATION_JSON);
	}

	@Test
	void testPostWithoutBodyElement() {
		final String endpoint = ENDPOINT;
		final int status = 201;
		final Collection<Pair> headers = singleton(pair(CONTENT_TYPE, APPLICATION_JSON));
		final String body = "{\"id\": 1, \"name\": \"Jane Doe\"}";

		stubPostRequest(endpoint, status, headers, body);

		final HttpResponse rsp = createDefaultClient()
			.preparePost(endpoint)
			.acceptJson()
			.asJson()
			.asXmlHttpRequest()
			.execute();

		assertRequest(endpoint, HttpMethod.POST);
		assertThat(rsp.status()).isEqualTo(status);
		assertThat(rsp.body()).isEqualTo(body);
		assertThat(rsp.getContentType().getFirstValue()).isEqualTo(APPLICATION_JSON);
		assertThat(rsp.getContentType().getLastValue()).isEqualTo(APPLICATION_JSON);
	}

	@Test
	void testPreparePostWithBodyElement() {
		final String endpoint = ENDPOINT;
		final int status = 201;
		final Collection<Pair> headers = singleton(pair(CONTENT_TYPE, APPLICATION_JSON));
		final String body = "{\"id\": 1, \"name\": \"Jane Doe\"}";

		stubPostRequest(endpoint, status, headers, body);

		final HttpResponse rsp = createDefaultClient()
			.preparePost(endpoint, jsonBody(body))
			.execute();

		assertRequest(endpoint, HttpMethod.POST);
		assertThat(rsp.status()).isEqualTo(status);
		assertThat(rsp.body()).isEqualTo(body);
		assertThat(rsp.getContentType().getFirstValue()).isEqualTo(APPLICATION_JSON);
		assertThat(rsp.getContentType().getLastValue()).isEqualTo(APPLICATION_JSON);
	}

	@Test
	void testPut() {
		final String endpoint = ENDPOINT + "/1";
		final int status = 204;
		final Collection<Pair> headers = emptyList();

		stubPutRequest(endpoint, status, headers);

		final HttpResponse rsp = createDefaultClient()
			.preparePut(endpoint)
			.acceptJson()
			.asXmlHttpRequest()
			.setBody(jsonBody("{\"id\": 1, \"name\": \"Jane Doe\"}"))
			.execute();

		assertRequest(endpoint, HttpMethod.PUT);
		assertThat(rsp.status()).isEqualTo(status);
		assertThat(rsp.body()).isEmpty();
		assertThat(rsp.getContentType()).isNull();
	}

	@Test
	void testPutWithJsonBodyString() {
		final String endpoint = ENDPOINT + "/1";
		final int status = 204;
		final Collection<Pair> headers = emptyList();
		final String rawBody = "{\"id\": 1, \"name\": \"Jane Doe\"}";
		final HttpRequestBody body = jsonBody(rawBody);

		stubPutRequest(endpoint, status, headers);

		final HttpResponse rsp = createDefaultClient()
			.preparePut(endpoint)
			.acceptJson()
			.asXmlHttpRequest()
			.setBody(body)
			.execute();

		assertRequest(endpoint, HttpMethod.PUT);
		assertThat(rsp.status()).isEqualTo(status);
		assertThat(rsp.body()).isEmpty();
		assertThat(rsp.getContentType()).isNull();
	}

	@Test
	void testPutWithoutBody() {
		final String endpoint = ENDPOINT + "/1";
		final int status = 204;
		final Collection<Pair> headers = emptyList();

		stubPutRequest(endpoint, status, headers);

		final HttpResponse rsp = createDefaultClient()
			.preparePut(endpoint)
			.acceptJson()
			.asJson()
			.asXmlHttpRequest()
			.execute();

		assertRequest(endpoint, HttpMethod.PUT);
		assertThat(rsp.status()).isEqualTo(status);
		assertThat(rsp.body()).isEmpty();
		assertThat(rsp.getContentType()).isNull();
	}

	@Test
	void testPreparePutWithBody() {
		final String endpoint = ENDPOINT + "/1";
		final int status = 204;
		final Collection<Pair> headers = emptyList();
		final String body = "{\"id\": 1, \"name\": \"Jane Doe\"}";

		stubPutRequest(endpoint, status, headers);

		final HttpResponse rsp = createDefaultClient()
			.preparePut(endpoint, jsonBody(body))
			.execute();

		assertRequest(endpoint, HttpMethod.PUT);
		assertThat(rsp.status()).isEqualTo(status);
		assertThat(rsp.body()).isEmpty();
		assertThat(rsp.getContentType()).isNull();
	}

	@Test
	void testPatch() {
		final String endpoint = ENDPOINT;
		final int status = 201;
		final Collection<Pair> headers = singleton(pair(CONTENT_TYPE, APPLICATION_JSON));
		final String bodyResponse = "{\"id\": 1, \"name\": \"Jane Doe\"}";
		final String rawBody = "{\"name\": \"Jane Doe\"}";
		final HttpRequestBody body = jsonBody(rawBody);

		stubPatchRequest(endpoint, status, headers, bodyResponse);

		final HttpResponse rsp = createDefaultClient()
			.preparePatch(endpoint)
			.acceptJson()
			.asXmlHttpRequest()
			.setBody(body)
			.execute();

		assertRequest(endpoint, HttpMethod.PATCH);
		assertThat(rsp.status()).isEqualTo(status);
		assertThat(rsp.body()).isEqualTo(bodyResponse);
		assertThat(rsp.getContentType().getFirstValue()).isEqualTo(APPLICATION_JSON);
		assertThat(rsp.getContentType().getLastValue()).isEqualTo(APPLICATION_JSON);
	}

	@Test
	void testPatchWithRawBody() {
		final String endpoint = ENDPOINT;
		final int status = 201;
		final Collection<Pair> headers = singleton(pair(CONTENT_TYPE, APPLICATION_JSON));
		final String responseBody = "{\"id\": 1, \"name\": \"Jane Doe\"}";
		final String rawBody = "{\"name\": \"Jane Doe\"}";
		final HttpRequestBody body = jsonBody(rawBody);

		stubPatchRequest(endpoint, status, headers, responseBody);

		final HttpResponse rsp = createDefaultClient()
			.preparePatch(endpoint)
			.acceptJson()
			.asJson()
			.asXmlHttpRequest()
			.setBody(body)
			.execute();

		assertRequest(endpoint, HttpMethod.PATCH);
		assertThat(rsp.status()).isEqualTo(status);
		assertThat(rsp.body()).isEqualTo(responseBody);
		assertThat(rsp.getContentType().getFirstValue()).isEqualTo(APPLICATION_JSON);
		assertThat(rsp.getContentType().getLastValue()).isEqualTo(APPLICATION_JSON);
	}

	@Test
	void testPreparePatchWithBody() {
		final String endpoint = ENDPOINT + "/1";
		final int status = 204;
		final Collection<Pair> headers = emptyList();
		final String body = "{\"id\": 1, \"name\": \"Jane Doe\"}";

		stubPatchRequest(endpoint, status, headers, body);

		final HttpResponse rsp = createDefaultClient()
			.preparePatch(endpoint, jsonBody(body))
			.execute();

		assertRequest(endpoint, HttpMethod.PATCH);
		assertThat(rsp.status()).isEqualTo(status);
		assertThat(rsp.body()).isEmpty();
	}

	@Test
	void testDelete() {
		final int status = 204;
		final Collection<Pair> headers = emptyList();
		final String endpoint = ENDPOINT + "/1";

		stubDeleteRequest(endpoint, status, headers);

		final HttpResponse rsp = createDefaultClient()
			.prepareDelete(endpoint)
			.acceptJson()
			.asJson()
			.asXmlHttpRequest()
			.execute();

		assertRequest(endpoint, HttpMethod.DELETE);
		assertThat(rsp.status()).isEqualTo(status);
		assertThat(rsp.body()).isEmpty();
		assertThat(rsp.getContentType()).isNull();
	}

	@Test
	void testDeleteWithRequestBody() {
		final String endpoint = ENDPOINT;
		final int status = 200;
		final Collection<Pair> headers = singleton(pair(CONTENT_TYPE, APPLICATION_JSON));
		final String body = "{\"id\": 1, \"name\": \"Jane Doe\"}";

		stubDeleteRequest(endpoint, status, headers, body);

		final HttpResponse rsp = createDefaultClient()
				.prepareDelete(endpoint, jsonBody(body))
				.acceptJson()
				.execute();

		assertRequest(endpoint, HttpMethod.DELETE);
		assertThat(rsp.status()).isEqualTo(status);
		assertThat(rsp.body()).isEqualTo(body);
		assertThat(rsp.getContentType().getFirstValue()).isEqualTo(APPLICATION_JSON);
		assertThat(rsp.getContentType().getLastValue()).isEqualTo(APPLICATION_JSON);
	}

	@Test
	void testUploadWithMultipartRequest() {
		final String endpoint = ENDPOINT;
		final int status = 201;
		final File file = getFileFromClasspath("/img1.jpg");

		stubUploadRequest(endpoint, status);

		final HttpResponse rsp = createDefaultClient()
			.preparePost(endpoint)
			.acceptJson()
			.asXmlHttpRequest()
			.setBody(multipartBuilder()
				.addFormDataPart(file, "image")
				.build())
			.execute();

		assertUploadRequest(endpoint, HttpMethod.POST, file);
		assertThat(rsp.status()).isEqualTo(status);
	}

	@Test
	void testRequestWithDefaultHeader() {
		final String name = "X-Custom-Header";
		final String value = "FooBar";
		final HttpClientConfiguration configuration = new HttpClientConfiguration.Builder()
			.addDefaultHeader(name, value)
			.build();

		stubDefaultRequest(ENDPOINT);

		testRequestHeader(createCustomClient(configuration), name, value, rq -> {});
	}

	@Test
	void testRequestWithDefaultHeaderObject() {
		final String name = "X-Custom-Header";
		final String value = "FooBar";
		final HttpHeader header = HttpHeader.header(name, value);
		final HttpClientConfiguration configuration = new HttpClientConfiguration.Builder()
			.addDefaultHeader(header)
			.build();

		stubDefaultRequest(ENDPOINT);

		testRequestHeader(createCustomClient(configuration), name, value, rq -> {});
	}

	@Test
	void testRequest_with_custom_header() {
		final String name = "X-Custom-Header";
		final String value = "FooBar";
		testRequestHeader(name, value, rq ->
			rq.addHeader(name, value)
		);
	}

	@Test
	void testRequest_with_custom_header_instance() {
		final String name = "X-Custom-Header";
		final String value = "FooBar";
		final HttpHeader header = HttpHeader.header(name, value);
		testRequestHeader(name, value, rq ->
			rq.addHeader(header)
		);
	}

	@Test
	void testRequest_as_xml_http_request() {
		final String value = "XMLHttpRequest";
		testRequestHeader(X_REQUESTED_WITH, value,
			HttpRequest::asXmlHttpRequest
		);
	}

	@Test
	void testRequest_accept_language() {
		final String value = "fr";
		testRequestHeader(ACCEPT_LANGUAGE, value, rq ->
			rq.acceptLanguage(value)
		);
	}

	@Test
	void testRequest_accept_gzip() {
		final String value = "gzip, deflate";
		testRequestHeader(ACCEPT_ENCODING, value,
			HttpRequest::acceptGzip
		);
	}

	@Test
	void testRequest_if_match() {
		final String value = UUID.randomUUID().toString();
		testRequestHeader(IF_MATCH, value, rq ->
			rq.addIfMatch(value)
		);
	}

	@Test
	void testRequest_if_none_match() {
		final String value = UUID.randomUUID().toString();
		testRequestHeader(IF_NONE_MATCH, value, rq ->
			rq.addIfNoneMatch(value)
		);
	}

	@Test
	void testRequest_if_modified_since() {
		final Date since = utcDate(2017, 0, 1, 0, 10, 20);
		final String value = "Sun, 01 Jan 2017 00:10:20 GMT";
		testRequestHeader(IF_MODIFIED_SINCE, value, rq ->
			rq.addIfModifiedSince(since)
		);
	}

	@Test
	void testRequest_if_unmodified_since() {
		final Date unmodifiedSince = utcDate(2016, 1, 10, 11, 12, 13);
		final String value = "Wed, 10 Feb 2016 11:12:13 GMT";
		testRequestHeader(IF_UNMODIFIED_SINCE, value, rq ->
			rq.addIfUnmodifiedSince(unmodifiedSince)
		);
	}

	@Test
	void testRequest_add_referer() {
		final String value = "ref";
		testRequestHeader(REFERER, value, rq ->
			rq.addReferer(value)
		);
	}

	@Test
	void testRequest_add_origin() {
		final String value = "http://localhost:8080";
		testRequestHeader(ORIGIN, value, rq ->
			rq.addOrigin(value)
		);
	}

	@Test
	void testRequest_with_user_agent() {
		final String value = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.43 Safari/537.31";
		testRequestHeader(USER_AGENT, value, rq ->
			rq.withUserAgent(value)
		);
	}

	@Test
	void testRequest_accept_xml() {
		testRequestHeader(ACCEPT, APPLICATION_XML,
			HttpRequest::acceptXml
		);
	}

	@Test
	void testRequest_accept_json() {
		testRequestHeader(ACCEPT, APPLICATION_JSON,
			HttpRequest::acceptJson
		);
	}

	@Test
	void testRequest_add_csrf_token() {
		final String value = UUID.randomUUID().toString();
		testRequestHeader(X_CSRF_TOKEN, value, rq ->
			rq.addCsrfToken(value)
		);
	}

	@Test
	void testRequest_as_json() {
		testRequestHeader(CONTENT_TYPE, APPLICATION_JSON,
			HttpRequest::asJson
		);
	}

	@Test
	void testRequest_as_xml() {
		testRequestHeader(CONTENT_TYPE, APPLICATION_XML,
			HttpRequest::asXml
		);
	}

	@Test
	void testRequest_override_put() {
		testRequestHeader(X_HTTP_METHOD_OVERRIDE, HttpMethod.PUT.name(),
			HttpRequest::overridePut
		);
	}

	@Test
	void testRequest_override_delete() {
		testRequestHeader(X_HTTP_METHOD_OVERRIDE, HttpMethod.DELETE.name(),
			HttpRequest::overrideDelete
		);
	}

	@Test
	void testRequest_add_x_http_method_override() {
		final String value = "PATCH";
		testRequestHeader(X_HTTP_METHOD_OVERRIDE, value, rq ->
			rq.addXHttpMethodOverride(value)
		);
	}

	private void testRequestHeader(String name, String value, Function<HttpRequest> func) {
		testRequestHeader(createDefaultClient(), name, value, func);
	}

	private void testRequestHeader(HttpClient client, String name, String value, Function<HttpRequest> func) {
		// GIVEN
		final String endpoint = ENDPOINT;
		stubDefaultRequest(ENDPOINT);

		// WHEN
		final HttpRequest rq = client.prepareGet(endpoint);
		func.apply(rq);

		// THEN
		final HttpResponse rsp = rq.execute();
		assertThat(rsp).isNotNull();
		assertThat(rsp.status()).isEqualTo(200);
		assertRequestWithHeader(endpoint, HttpMethod.GET, name, value);
	}

	@Test
	void testRequest_execute_json() {
		testRequestExecuteAs(APPLICATION_JSON,
			HttpRequest::executeJson
		);
	}

	@Test
	void testRequest_execute_xml() {
		testRequestExecuteAs(APPLICATION_XML,
			HttpRequest::executeXml
		);
	}

	private void testRequestExecuteAs(String contentType, MapperFunction<HttpRequest, HttpResponse> func) {
		// GIVEN
		final String endpoint = ENDPOINT;
		final int rspStatus = 200;
		final Collection<Pair> headers = singleton(pair(CONTENT_TYPE, APPLICATION_JSON));
		final String rspBody = "[]";

		stubGetRequest(endpoint, rspStatus, headers, rspBody);

		// WHEN
		final HttpRequest rq = createDefaultClient().prepareGet(ENDPOINT);
		final HttpResponse rsp = func.apply(rq);

		// THEN
		assertThat(rsp).isNotNull();
		assertThat(rsp.status()).isEqualTo(rspStatus);
		assertThat(rsp.body()).isEqualTo(rspBody);
		assertRequestWithHeader(endpoint, HttpMethod.GET, CONTENT_TYPE, contentType);
	}

	@Test
	void testRequest_add_non_encoded_query_param() {
		final String name = "name";
		final String value = "john doe";
		final String expectedUrl = ENDPOINT + "?" + encodeQueryParam(name, value);
		testQueryParams(expectedUrl, rq ->
			rq.addQueryParam(name, value)
		);
	}

	@Test
	void testRequest_add_non_encoded_query_param_with_ampersand() {
		final String name = "name";
		final String value = "john & jane doe";
		final String expectedUrl = ENDPOINT + "?" + encodeQueryParam(name, value);
		testQueryParams(expectedUrl, rq ->
			rq.addQueryParam(name, value)
		);
	}

	@Test
	void testRequest_add_query_param() {
		final String name = "firstName";
		final String value = "john";
		final String expectedUrl = ENDPOINT + "?" + encodeQueryParam(name, value);
		testQueryParams(expectedUrl, rq ->
			rq.addQueryParam(name, value)
		);
	}

	@Test
	void testRequest_add_query_param_without_value() {
		final String name = "flag";
		final String value = null;
		final String expectedUrl = ENDPOINT + "?" + encodeQueryParam(name, value);
		testQueryParams(expectedUrl, rq ->
			rq.addQueryParam(name, value)
		);
	}

	@Test
	void testRequest_add_several_query_params() {
		final String n1 = "firstName";
		final String v1 = "john";
		final HttpParameter p1 = param(n1, v1);

		final String n2 = "lastName";
		final String v2 = "doe";
		final HttpParameter p2 = param(n2, v2);

		final String expectedUrl = ENDPOINT +
			"?" + encodeQueryParam(n1, v1) +
			"&" + encodeQueryParam(n2, v2);

		testQueryParams(expectedUrl, rq ->
			rq.addQueryParams(p1, p2)
		);
	}

	private void testQueryParams(String expectedUrl, Function<HttpRequest> func) {
		// GIVEN
		final int rspStatus = 200;
		final Collection<Pair> headers = singleton(pair(CONTENT_TYPE, APPLICATION_JSON));
		final String rspBody = "[]";

		stubGetRequest(expectedUrl, rspStatus, headers, rspBody);

		// WHEN
		final HttpRequest rq = createDefaultClient().prepareGet(ENDPOINT);
		func.apply(rq);

		// THEN
		final HttpResponse rsp = rq.execute();
		assertThat(rsp).isNotNull();
		assertThat(rsp.status()).isEqualTo(rspStatus);
		assertThat(rsp.body()).isEqualTo(rspBody);
		assertRequest(expectedUrl, HttpMethod.GET);
	}

	@Test
	void testRequest_add_form_param() {
		final String name = "firstName";
		final String value = "john";
		final String expectedBody = encodeFormParam(name, value);
		final Map<String, String> parameters = singletonMap(name, value);
		final HttpRequestBody body = formUrlEncodedBody(parameters);

		testRequestBody(expectedBody, rq ->
				rq.setBody(body)
		);
	}

	@Test
	void testRequest_add_form_param_without_value() {
		final String name = "flag";
		final String value = "";
		final Map<String, String> parameters = singletonMap(name, value);
		final HttpRequestBody body = formUrlEncodedBody(parameters);
		final String expectedBody = encodeFormParam(name, value);

		testRequestBody(expectedBody, rq ->
			rq.setBody(body)
		);
	}

	@Test
	void testRequest_add_several_form_params() {
		final String n1 = "firstName";
		final String v1 = "John";
		final HttpParameter p1 = param(n1, v1);

		final String n2 = "lastName";
		final String v2 = "Doe";
		final HttpParameter p2 = param(n2, v2);

		final HttpRequestBody body = formUrlEncodedBody(p1, p2);
		final String expectedBody = encodeFormParam(n1, v1) + "&" + encodeFormParam(n2, v2);

		testRequestBody(expectedBody, rq ->
			rq.setBody(body)
		);
	}

	@Test
	void testRequest_add_non_escaped_form_param_name() {
		final String name = "first name";
		final String value = "john";
		final String expectedBody = encodeFormParam(name, value);
		final Map<String, String> parameters = singletonMap(name, value);
		final HttpRequestBody body = formUrlEncodedBody(parameters);

		testRequestBody(expectedBody, rq ->
			rq.setBody(body)
		);
	}

	@Test
	void testRequest_add_non_escaped_form_param_value() {
		final String name = "name";
		final String value = "john doe";
		final String expectedBody = encodeFormParam(name, value);
		final Map<String, String> parameters = singletonMap(name, value);
		final HttpRequestBody body = formUrlEncodedBody(parameters);

		testRequestBody(expectedBody, rq ->
			rq.setBody(body)
		);
	}

	@Test
	void testRequest_set_raw_body() {
		final String rawBody = "{\"id\": 1, \"firstName\": \"John\", \"lastName\": \"Doe\"}";
		final HttpRequestBody body = requestBody(rawBody);
		testRequestBody(rawBody, rq ->
			rq.setBody(body)
		);
	}

	@Test
	void testRequest_set_body() {
		final String rawBody = "{\"id\": 1, \"firstName\": \"John\", \"lastName\": \"Doe\"}";
		final HttpRequestBody body = requestBody(rawBody);
		testRequestBody(rawBody, rq ->
			rq.setBody(body)
		);
	}

	@Test
	void testRequest_add_body_form() {
		final String n1 = "firstName";
		final String v1 = "John";
		final HttpParameter p1 = param(n1, v1);

		final String n2 = "lastName";
		final String v2 = "Doe";
		final HttpParameter p2 = param(n2, v2);

		final String expectedBody = encodeFormParam(n1, v1) + "&" + encodeFormParam(n2, v2);
		final HttpRequestBody body = formUrlEncodedBody(p1, p2);

		testRequestBody(expectedBody, rq ->
			rq.setBody(body)
		);
	}

	private void testRequestBody(String body, Function<HttpRequest> func) {
		final String endpoint = ENDPOINT;
		final int rspStatus = 204;
		final Collection<Pair> headers = emptyList();
		final String rspBody = "";

		stubPostRequest(endpoint, rspStatus, headers, rspBody);

		final HttpRequest rq = createDefaultClient().preparePost(ENDPOINT);

		func.apply(rq);

		final HttpResponse rsp = rq.execute();
		assertThat(rsp).isNotNull();
		assertThat(rsp.status()).isEqualTo(rspStatus);
		assertThat(rsp.body()).isEqualTo(rspBody);
		assertRequestWithBody(endpoint, HttpMethod.POST, body);
	}

	@Test
	void testRequest_should_fail_to_set_body_on_get_request() {
		final HttpRequest httpRequest = createDefaultClient().prepareGet(ENDPOINT);
		final String body = "{\"id\": 1, \"firstName\": \"John\", \"lastName\": \"Doe\"}";

		assertThatThrownBy(() -> httpRequest.setBody(requestBody(body)))
			.isExactlyInstanceOf(UnsupportedOperationException.class)
			.hasMessage("Http method GET does not support request body");
	}

	@Test
	void testRequest_with_simple_cookie() {
		final String endpoint = ENDPOINT;
		stubDefaultRequest(endpoint);

		final String name = "foo";
		final String value = "bar";

		createDefaultClient()
			.prepareGet(endpoint)
			.addCookie(Cookies.cookie(name, value))
			.executeJson();

		assertRequestWithCookie(endpoint, HttpMethod.GET, name, value);
	}

	@Test
	void testRequest_with_default_cookie() {
		final String endpoint = ENDPOINT;
		final String name = "foo";
		final String value = "bar";
		final HttpClientConfiguration configuration = new HttpClientConfiguration.Builder()
			.addDefaultCookie(name, value)
			.build();

		stubDefaultRequest(endpoint);

		createCustomClient(configuration)
			.prepareGet(endpoint)
			.executeJson();

		assertRequestWithCookie(endpoint, HttpMethod.GET, name, value);
	}

	@Test
	void testRequest_with_default_cookie_object() {
		final String endpoint = ENDPOINT;
		final String name = "foo";
		final String value = "bar";
		final HttpClientConfiguration configuration = new HttpClientConfiguration.Builder()
			.addDefaultCookie(Cookies.cookie(name, value))
			.build();

		stubDefaultRequest(endpoint);

		createCustomClient(configuration)
			.prepareGet(endpoint)
			.executeJson();

		assertRequestWithCookie(endpoint, HttpMethod.GET, name, value);
	}

	@Test
	void testRequest_with_simple_cookie_method() {
		final String endpoint = ENDPOINT;
		stubDefaultRequest(endpoint);

		final String name = "foo";
		final String value = "bar";

		createDefaultClient()
			.prepareGet(endpoint)
			.addCookie(name, value)
			.executeJson();

		assertRequestWithCookie(endpoint, HttpMethod.GET, name, value);
	}

	@Test
	void testRequest_with_simple_cookies() {
		final String endpoint = ENDPOINT;
		stubDefaultRequest(endpoint);

		final String n1 = "f1";
		final String v1 = "b1";

		final String n2 = "f2";
		final String v2 = "b2";

		createDefaultClient()
			.prepareGet(endpoint)
			.addCookie(Cookies.cookie(n1, v1))
			.addCookie(Cookies.cookie(n2, v2))
			.executeJson();

		final List<Pair> expectedCookies = asList(
			pair(n1, v1),
			pair(n2, v2)
		);

		assertRequestWithCookies(endpoint, HttpMethod.GET, expectedCookies);
	}

	@Test
	void testRequest_with_simple_cookies_method() {
		final String endpoint = ENDPOINT;
		stubDefaultRequest(endpoint);

		final String n1 = "f1";
		final String v1 = "b1";

		final String n2 = "f2";
		final String v2 = "b2";

		createDefaultClient()
			.prepareGet(endpoint)
			.addCookie(n1, v1)
			.addCookie(n2, v2)
			.executeJson();

		final List<Pair> expectedCookies = asList(
			pair(n1, v1),
			pair(n2, v2)
		);

		assertRequestWithCookies(endpoint, HttpMethod.GET, expectedCookies);
	}

	@Test
	void testRequest_with_complex_cookie() {
		final String endpoint = ENDPOINT;
		stubDefaultRequest(endpoint);

		final String name = "foo";
		final String value = "bar";
		final String domain = "localhost";
		final String path = server.getPath();
		final boolean secured = true;
		final boolean httpOnly = true;

		final Calendar now = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		final Date maxAgeUtc = utcDate(
			now.get(Calendar.YEAR) + 1,
			now.get(Calendar.MONTH),
			now.get(Calendar.DAY_OF_MONTH),
			now.get(Calendar.HOUR_OF_DAY),
			now.get(Calendar.MINUTE),
			now.get(Calendar.MILLISECOND)
		);

		final long maxAge = maxAgeUtc.getTime();
		final Long expires = null;

		createDefaultClient()
			.prepareGet(endpoint)
			.addHeader(ORIGIN, server.getUrl())
			.addCookie(Cookies.cookie(name, value, domain, path, expires, maxAge, secured, httpOnly))
			.executeJson();

		assertRequestWithCookie(endpoint, HttpMethod.GET, name, value);
	}

	@Test
	void testResponse_without_headers() {
		final String endpoint = ENDPOINT;
		final int status = 200;
		final String body = null;
		final Collection<Pair> headers = emptyList();

		stubGetRequest(endpoint, status, headers, body);

		final HttpResponse rsp = createDefaultClient()
			.prepareGet(endpoint)
			.executeJson();

		final List<String> testedHeaders = asList(
			ETAG,
			LOCATION,
			LAST_MODIFIED,
			CONTENT_ENCODING,
			CACHE_CONTROL,
			CONTENT_TYPE,
			STRICT_TRANSPORT_SECURITY,
			X_CONTENT_SECURITY_POLICY,
			X_WEBKIT_CSP,
			CONTENT_SECURITY_POLICY,
			X_XSS_PROTECTION,
			X_CONTENT_TYPE_OPTIONS
		);

		for (String header : testedHeaders) {
			assertThat(rsp.containsHeader(header))
				.overridingErrorMessage("Header %s should be missing", header)
				.isFalse();

			assertThat(rsp.getHeader(header))
				.overridingErrorMessage("Header %s should be null", header)
				.isNull();
		}
	}

	@Test
	void testResponse_with_custom_header() {
		final String name = "X-Custom-Header";
		final String value = "Foo";

		testResponseHeader(name, value, rsp ->
			rsp.getHeader(name)
		);
	}

	@Test
	void testResponse_with_custom_header_and_multiple_values() {
		final String name = "X-Custom-Header";
		final String v1 = "Foo";
		final String v2 = "Bar";
		final String v3 = "Quix";
		final List<String> values = asList(v1, v2, v3);

		testResponseWithSeveralValues(name, values, rsp ->
			rsp.getHeader(name)
		);
	}

	@Test
	void testResponse_with_content_type_header() {
		testResponseHeader(CONTENT_TYPE, APPLICATION_XML,
			HttpResponse::getContentType
		);
	}

	@Test
	void testResponse_with_etag() {
		final String value = UUID.randomUUID().toString();
		testResponseHeader(ETAG, value,
			HttpResponse::getETag
		);
	}

	@Test
	void testResponse_with_cache_control() {
		final String value = "no-cache, no-store, must-revalidate";
		testResponseHeader(CACHE_CONTROL, value,
			HttpResponse::getCacheControl
		);
	}

	@Test
	void testResponse_with_content_encoding() {
		final String value = "identity";
		testResponseHeader(CONTENT_ENCODING, value,
			HttpResponse::getContentEncoding
		);
	}

	@Test
	void testResponse_with_location() {
		final String value = "http://localhost";
		testResponseHeader(LOCATION, value,
			HttpResponse::getLocation
		);
	}

	@Test
	void testResponse_with_last_modified() {
		final String value = "Wed, 15 Nov 1995 04:58:08 GMT";
		testResponseHeader(LAST_MODIFIED, value,
			HttpResponse::getLastModified
		);
	}

	@Test
	void testResponse_with_strict_transport_security() {
		final String value = "max-age=31536000; includeSubDomains; preload";
		testResponseHeader(STRICT_TRANSPORT_SECURITY, value,
			HttpResponse::getStrictTransportSecurity
		);
	}

	@Test
	void testResponse_with_content_security_policy() {
		final String value = "default-src 'self';";
		testResponseHeader(CONTENT_SECURITY_POLICY, value,
			HttpResponse::getContentSecurityPolicy
		);
	}

	@Test
	void testResponse_with_x_content_security_policy() {
		final String value = "default-src 'self';";
		testResponseHeader(X_CONTENT_SECURITY_POLICY, value,
			HttpResponse::getXContentSecurityPolicy
		);
	}

	@Test
	void testResponse_with_x_webkit_csp() {
		final String value = "default-src 'self';";
		testResponseHeader(X_WEBKIT_CSP, value,
			HttpResponse::getXWebkitCSP
		);
	}

	@Test
	void testResponse_with_xss_protection() {
		final String value = "1; mode=block";
		testResponseHeader(X_XSS_PROTECTION, value,
			HttpResponse::getXXSSProtection
		);
	}

	@Test
	void testResponse_with_content_type_options() {
		final String value = "nosniff";
		testResponseHeader(X_CONTENT_TYPE_OPTIONS, value,
			HttpResponse::getXContentTypeOptions
		);
	}

	private void testResponseHeader(String name, String value, MapperFunction<HttpResponse, HttpHeader> func) {
		final String endpoint = ENDPOINT;
		final int status = 200;
		final Collection<Pair> headers = singleton(pair(name, value));
		final String body = null;

		stubGetRequest(endpoint, status, headers, body);

		final HttpResponse rsp = createDefaultClient()
			.prepareGet(endpoint)
			.addAcceptEncoding("identity")
			.executeJson();

		final HttpHeader header = rsp.getHeader(name);
		assertThat(rsp.containsHeader(name)).isTrue();
		assertThat(func.apply(rsp)).isEqualTo(header);

		assertThat(header.getName()).isEqualTo(name);
		assertThat(header.getValues()).isEqualTo(singletonList(value));
		assertThat(header.getFirstValue()).isEqualTo(value);
		assertThat(header.getLastValue()).isEqualTo(value);

		assertThat(rsp.getHeaders())
			.extracting("name", "values")
			.contains(
				tuple(name, singletonList(value))
			);
	}

	private void testResponseWithSeveralValues(String name, List<String> values, MapperFunction<HttpResponse, HttpHeader> func) {
		// GIVEN
		final String endpoint = ENDPOINT;
		final int status = 200;
		final String body = null;
		final Collection<Pair> headers = singleton(pair(name, values));

		stubGetRequest(endpoint, status, headers, body);

		// WHEN
		final HttpResponse rsp = createDefaultClient()
			.prepareGet(endpoint)
			.addAcceptEncoding("identity")
			.executeJson();

		// THEN
		final HttpHeader header = rsp.getHeader(name);
		assertThat(rsp.containsHeader(name)).isTrue();
		assertThat(func.apply(rsp)).isEqualTo(header);

		assertThat(header.getName()).isEqualTo(name);
		assertThat(header.getValues()).isEqualTo(values);
		assertThat(header.getFirstValue()).isEqualTo(values.get(0));
		assertThat(header.getLastValue()).isEqualTo(values.get(values.size() - 1));

		assertThat(rsp.getHeaders())
			.extracting("name", "values")
			.contains(
				tuple(name, values)
			);
	}

	@Test
	void testResponse_with_cookies() {
		final String name = "id";
		final String value = "foo";
		final String domain = "localhost";
		final String path = "/";
		final long maxAge = 3600;

		final String cookieValue = name + "=" + value + "; " +
			"Domain=" + domain + "; " +
			"Path=" + path + "; " +
			"Max-Age=" + maxAge + "; " +
			"Secure; " +
			"HttpOnly";

		final String endpoint = ENDPOINT;
		final int status = 200;
		final Collection<Pair> headers = singleton(pair(SET_COOKIE, cookieValue));
		final String body = null;

		stubGetRequest(endpoint, status, headers, body);

		final HttpResponse rsp = createDefaultClient()
			.prepareGet(endpoint)
			.executeJson();

		final List<Cookie> cookies = rsp.getCookies();
		assertThat(cookies).hasSize(1);

		final Cookie cookie = cookies.get(0);
		assertThat(cookie.getName()).isEqualTo(name);
		assertThat(cookie.getValue()).isEqualTo(value);
		assertThat(cookie.getDomain()).isEqualTo(domain);
		assertThat(cookie.getPath()).isEqualTo(path);
		assertThat(cookie.getMaxAge()).isEqualTo(maxAge);
		assertThat(cookie.isSecure()).isTrue();
		assertThat(cookie.isHttpOnly()).isTrue();

		assertThat(rsp.getCookie(name)).isEqualTo(cookie);
		assertThat(rsp.getCookie("foobar")).isNull();
	}

	@Test
	void testResponse_without_cookies() {
		final String endpoint = ENDPOINT;
		stubDefaultRequest(endpoint);

		final HttpResponse rsp = createDefaultClient()
			.prepareGet(endpoint)
			.executeJson();

		assertThat(rsp.getCookies()).isNotNull().isEmpty();
		assertThat(rsp.getCookie("id")).isNull();
	}

	@Test
	void testRequest_Response_Duration() {
		final String endpoint = ENDPOINT;
		stubDefaultRequest(endpoint);

		final HttpResponse rsp = createDefaultClient()
			.prepareGet(endpoint)
			.executeJson();

		final long durationNano = rsp.getRequestDuration();
		final long durationMillis = rsp.getRequestDurationInMillis();
		assertThat(durationNano).isGreaterThan(0);
		assertThat(durationMillis).isEqualTo(durationNano / 1000 / 1000);
	}

	@Test
	void it_should_destroy_client() {
		final HttpClient newClient = createDefaultClient();

		assertThat(newClient.isDestroyed()).isFalse();
		newClient.destroy();
		assertThat(newClient.isDestroyed()).isTrue();
	}

	@Test
	void it_should_fail_to_create_request_from_a_destroyed_client() {
		final String endpoint = "/foo";
		final HttpClient newClient = createDefaultClient();
		newClient.destroy();

		assertThatThrownBy(() -> newClient.prepareGet(endpoint))
			.isExactlyInstanceOf(IllegalStateException.class)
			.hasMessage("Cannot create request from a destroyed client");
	}

	protected abstract HttpClientStrategy strategy();

	private HttpClient createDefaultClient() {
		return createClient(() ->
			strategy().build(server)
		);
	}

	private HttpClient createCustomClient(final HttpClientConfiguration configuration) {
		return createClient(() ->
			strategy().build(configuration, server)
		);
	}

	private HttpClient createClient(HttpClientFactory factory) {
		ensureOneClient();
		client = factory.create();
		return client;
	}

	private void ensureOneClient() {
		if (client != null) {
			throw new AssertionError("Cannot create two clients on a test");
		}
	}

	private interface HttpClientFactory {
		HttpClient create();
	}
}
