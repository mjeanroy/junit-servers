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

import com.github.mjeanroy.junit.servers.client.Cookie;
import com.github.mjeanroy.junit.servers.client.Cookies;
import com.github.mjeanroy.junit.servers.client.HttpClient;
import com.github.mjeanroy.junit.servers.client.HttpClientStrategy;
import com.github.mjeanroy.junit.servers.client.HttpHeader;
import com.github.mjeanroy.junit.servers.client.HttpMethod;
import com.github.mjeanroy.junit.servers.client.HttpParameter;
import com.github.mjeanroy.junit.servers.client.HttpRequest;
import com.github.mjeanroy.junit.servers.client.HttpResponse;
import com.github.mjeanroy.junit.servers.servers.EmbeddedServer;
import com.github.mjeanroy.junit.servers.utils.commons.Function;
import com.github.mjeanroy.junit.servers.utils.commons.MapperFunction;
import com.github.mjeanroy.junit.servers.utils.commons.Pair;
import com.github.mjeanroy.junit.servers.utils.junit.run_if.RunIfRunner;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.http.RequestMethod;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

import static com.github.mjeanroy.junit.servers.client.HttpParameter.param;
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
import static java.util.Arrays.asList;
import static java.util.Collections.singleton;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RunIfRunner.class)
public abstract class BaseHttpClientTest {

	private static final String ENDPOINT = "/people";
	private static final String APPLICATION_JSON = "application/json";
	private static final String APPLICATION_XML = "application/xml";
	private static final String MULTIPART_FORM_DATA = "multipart/form-data";
	private static final String X_CSRF_TOKEN = "X-Csrf-Token";
	private static final String X_HTTP_METHOD_OVERRIDE = "X-Http-Method-Override";
	private static final String X_REQUESTED_WITH = "X-Requested-With";
	private static final String ACCEPT = "Accept";
	private static final String ACCEPT_LANGUAGE = "Accept-Language";
	private static final String ACCEPT_ENCODING = "Accept-Encoding";
	private static final String IF_MATCH = "If-Match";
	private static final String IF_NONE_MATCH = "If-None-Match";
	private static final String IF_MODIFIED_SINCE = "If-Modified-Since";
	private static final String IF_UNMODIFIED_SINCE = "If-Unmodified-Since";
	private static final String REFERER = "Referer";
	private static final String ORIGIN = "Origin";
	private static final String USER_AGENT = "User-Agent";
	private static final String CONTENT_TYPE = "Content-Type";
	private static final String CONTENT_ENCODING = "Content-Encoding";
	private static final String LOCATION = "Location";
	private static final String ETAG = "ETag";
	private static final String CACHE_CONTROL = "Cache-Control";
	private static final String LAST_MODIFIED = "Last-Modified";
	private static final String STRICT_TRANSPORT_SECURITY = "Strict-Transport-Security";
	private static final String X_CONTENT_SECURITY_POLICY = "X-Content-Security-Policy";
	private static final String X_WEBKIT_CSP = "X-Webkit-CSP";
	private static final String CONTENT_SECURITY_POLICY = "Content-Security-Policy";
	private static final String X_XSS_PROTECTION = "X-Xss-Protection";
	private static final String X_CONTENT_TYPE_OPTIONS = "X-Content-Type-Options";
	private static final String SET_COOKIE = "Set-Cookie";

	@Rule
	public WireMockRule wireMockRule = new WireMockRule();

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private EmbeddedServer<?> server;
	private String url;
	private HttpClient client;

	@Before
	public void setUp() throws Exception {
		int port = wireMockRule.port();
		String path = "/";

		url = "http://localhost:" + port;
		server = mock(EmbeddedServer.class);
		when(server.getPort()).thenReturn(port);
		when(server.getUrl()).thenReturn(url);
		when(server.getPath()).thenReturn(path);

		client = createClient(server);
	}

	@After
	public void tearDown() {
		client.destroy();
	}

	@Test
	public void testRequestUrl() {
		final HttpRequest rq = client
			.prepareGet(ENDPOINT)
			.acceptJson()
			.asXmlHttpRequest();

		assertThat(rq.getUrl()).isEqualTo(url + "/people");
		assertThat(rq.getMethod()).isEqualTo(HttpMethod.GET);
	}

	@Test
	public void testGet() {
		final int status = 200;
		final String body = "[{\"id\": 1, \"name\": \"John Doe\"}]";

		stubGetRequest(status, body);

		final HttpResponse rsp = client
			.prepareGet(ENDPOINT)
			.acceptJson()
			.asXmlHttpRequest()
			.execute();

		assertRequest(ENDPOINT, HttpMethod.GET);
		assertThat(rsp.status()).isEqualTo(status);
		assertThat(rsp.body()).isEqualTo(body);
		assertThat(rsp.getContentType().getFirstValue()).isEqualTo(APPLICATION_JSON);
	}

	@Test
	public void testHead() {
		final int status = 200;

		stubHeadRequest(status);

		final HttpResponse rsp = client
				.prepareHead(ENDPOINT)
				.acceptJson()
				.asXmlHttpRequest()
				.execute();

		assertRequest(ENDPOINT, HttpMethod.HEAD);
		assertThat(rsp.status()).isEqualTo(status);
		assertThat(rsp.body()).isNullOrEmpty();
		assertThat(rsp.getContentType().getFirstValue()).isEqualTo(APPLICATION_JSON);
	}

	@Test
	public void testPost() {
		final int status = 201;
		final String body = "{\"id\": 1, \"name\": \"Jane Doe\"}";

		stubPostRequest(status, body);

		final HttpResponse rsp = client
			.preparePost(ENDPOINT)
			.acceptJson()
			.asJson()
			.asXmlHttpRequest()
			.setBody("{\"name\": \"Jane Doe\"}")
			.execute();

		assertRequest(ENDPOINT, HttpMethod.POST);
		assertThat(rsp.status()).isEqualTo(status);
		assertThat(rsp.body()).isEqualTo(body);
		assertThat(rsp.getContentType().getFirstValue()).isEqualTo(APPLICATION_JSON);
	}

	@Test
	public void testPostWithBodyElement() {
		final int status = 201;
		final String body = "{\"id\": 1, \"name\": \"Jane Doe\"}";

		stubPostRequest(status, body);

		final HttpResponse rsp = client
			.preparePost(ENDPOINT)
			.acceptJson()
			.asJson()
			.asXmlHttpRequest()
			.execute();

		assertRequest(ENDPOINT, HttpMethod.POST);
		assertThat(rsp.status()).isEqualTo(status);
		assertThat(rsp.body()).isEqualTo(body);
		assertThat(rsp.getContentType().getFirstValue()).isEqualTo(APPLICATION_JSON);
	}

	@Test
	public void testPut() {
		final String endpoint = ENDPOINT + "/1";
		final int status = 204;

		stubPutRequest(status);

		final HttpResponse rsp = client
			.preparePut(endpoint)
			.acceptJson()
			.asJson()
			.asXmlHttpRequest()
			.setBody("{\"id\": 1, \"name\": \"Jane Doe\"}")
			.execute();

		assertRequest(endpoint, HttpMethod.PUT);
		assertThat(rsp.status()).isEqualTo(status);
		assertThat(rsp.body()).isEmpty();
		assertThat(rsp.getContentType().getFirstValue()).isEqualTo(APPLICATION_JSON);
		assertThat(rsp.getContentType().getLastValue()).isEqualTo(APPLICATION_JSON);
	}

	@Test
	public void testPutWithoutBody() {
		final String endpoint = ENDPOINT + "/1";
		final int status = 204;

		stubPutRequest(status);

		final HttpResponse rsp = client
			.preparePut(endpoint)
			.acceptJson()
			.asJson()
			.asXmlHttpRequest()
			.execute();

		assertRequest(endpoint, HttpMethod.PUT);
		assertThat(rsp.status()).isEqualTo(status);
		assertThat(rsp.body()).isEmpty();
		assertThat(rsp.getContentType().getFirstValue()).isEqualTo(APPLICATION_JSON);
		assertThat(rsp.getContentType().getLastValue()).isEqualTo(APPLICATION_JSON);
	}

	@Test
	public void testPatch() {
		final int status = 201;
		final String body = "{\"id\": 1, \"name\": \"Jane Doe\"}";

		stubPatchRequest(status, body);

		final HttpResponse rsp = client
				.preparePatch(ENDPOINT)
				.acceptJson()
				.asJson()
				.asXmlHttpRequest()
				.setBody("{\"name\": \"Jane Doe\"}")
				.execute();

		assertRequest(ENDPOINT, HttpMethod.PATCH);
		assertThat(rsp.status()).isEqualTo(status);
		assertThat(rsp.body()).isEqualTo(body);
		assertThat(rsp.getContentType().getFirstValue()).isEqualTo(APPLICATION_JSON);
	}

	@Test
	public void testDelete() {
		final int status = 204;
		final String endpoint = ENDPOINT + "/1";

		stubDeleteRequest(status);

		final HttpResponse rsp = client
			.prepareDelete(endpoint)
			.acceptJson()
			.asJson()
			.asXmlHttpRequest()
			.execute();

		assertRequest(endpoint, HttpMethod.DELETE);
		assertThat(rsp.status()).isEqualTo(status);
		assertThat(rsp.body()).isEmpty();
		assertThat(rsp.getContentType().getFirstValue()).isEqualTo(APPLICATION_JSON);
		assertThat(rsp.getContentType().getLastValue()).isEqualTo(APPLICATION_JSON);
	}

	@Test
	public void testRequest_with_custom_header() {
		final String name = "X-Custom-Header";
		final String value = "FooBar";
		testRequestHeader(name, value, new Function<HttpRequest>() {
			@Override
			public void apply(HttpRequest rq) {
				rq.addHeader(name, value);
			}
		});
	}

	@Test
	public void testRequest_as_xml_http_request() {
		final String value = "XMLHttpRequest";
		testRequestHeader(X_REQUESTED_WITH, value, new Function<HttpRequest>() {
			@Override
			public void apply(HttpRequest rq) {
				rq.asXmlHttpRequest();
			}
		});
	}

	@Test
	public void testRequest_accept_language() {
		final String value = "fr";
		testRequestHeader(ACCEPT_LANGUAGE, value, new Function<HttpRequest>() {
			@Override
			public void apply(HttpRequest rq) {
				rq.acceptLanguage(value);
			}
		});
	}

	@Test
	public void testRequest_accept_gzip() {
		final String value = "gzip, deflate";
		testRequestHeader(ACCEPT_ENCODING, value, new Function<HttpRequest>() {
			@Override
			public void apply(HttpRequest rq) {
				rq.acceptGzip();
			}
		});
	}

	@Test
	public void testRequest_if_match() {
		final String value = UUID.randomUUID().toString();
		testRequestHeader(IF_MATCH, value, new Function<HttpRequest>() {
			@Override
			public void apply(HttpRequest rq) {
				rq.addIfMatch(value);
			}
		});
	}

	@Test
	public void testRequest_if_none_match() {
		final String value = UUID.randomUUID().toString();
		testRequestHeader(IF_NONE_MATCH, value, new Function<HttpRequest>() {
			@Override
			public void apply(HttpRequest rq) {
				rq.addIfNoneMatch(value);
			}
		});
	}

	@Test
	public void testRequest_if_modified_since() {
		final Date since = utcDate(2017, 0, 1, 0, 10, 20);
		final String value = "Sun, 01 Jan 2017 00:10:20 GMT";
		testRequestHeader(IF_MODIFIED_SINCE, value, new Function<HttpRequest>() {
			@Override
			public void apply(HttpRequest rq) {
				rq.addIfModifiedSince(since);
			}
		});
	}

	@Test
	public void testRequest_if_unmodified_since() {
		final Date unmodifiedSince = utcDate(2016, 1, 10, 11, 12, 13);
		final String value = "Wed, 10 Feb 2016 11:12:13 GMT";
		testRequestHeader(IF_UNMODIFIED_SINCE, value, new Function<HttpRequest>() {
			@Override
			public void apply(HttpRequest rq) {
				rq.addIfUnmodifiedSince(unmodifiedSince);
			}
		});
	}

	@Test
	public void testRequest_add_referer() {
		final String value = "ref";
		testRequestHeader(REFERER, value, new Function<HttpRequest>() {
			@Override
			public void apply(HttpRequest rq) {
				rq.addReferer(value);
			}
		});
	}

	@Test
	public void testRequest_add_origin() {
		final String value = "http://localhost:8080";
		testRequestHeader(ORIGIN, value, new Function<HttpRequest>() {
			@Override
			public void apply(HttpRequest rq) {
				rq.addOrigin(value);
			}
		});
	}

	@Test
	public void testRequest_with_user_agent() {
		final String value = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.43 Safari/537.31";
		testRequestHeader(USER_AGENT, value, new Function<HttpRequest>() {
			@Override
			public void apply(HttpRequest rq) {
				rq.withUserAgent(value);
			}
		});
	}

	@Test
	public void testRequest_accept_xml() {
		testRequestHeader(ACCEPT, APPLICATION_XML, new Function<HttpRequest>() {
			@Override
			public void apply(HttpRequest rq) {
				rq.acceptXml();
			}
		});
	}

	@Test
	public void testRequest_accept_json() {
		testRequestHeader(ACCEPT, APPLICATION_JSON, new Function<HttpRequest>() {
			@Override
			public void apply(HttpRequest rq) {
				rq.acceptJson();
			}
		});
	}

	@Test
	public void testRequest_add_csrf_token() {
		final String value = UUID.randomUUID().toString();
		testRequestHeader(X_CSRF_TOKEN, value, new Function<HttpRequest>() {
			@Override
			public void apply(HttpRequest rq) {
				rq.addCsrfToken(value);
			}
		});
	}

	@Test
	public void testRequest_as_json() {
		testRequestHeader(CONTENT_TYPE, APPLICATION_JSON, new Function<HttpRequest>() {
			@Override
			public void apply(HttpRequest rq) {
				rq.asJson();
			}
		});
	}

	@Test
	public void testRequest_as_xml() {
		testRequestHeader(CONTENT_TYPE, APPLICATION_XML, new Function<HttpRequest>() {
			@Override
			public void apply(HttpRequest rq) {
				rq.asXml();
			}
		});
	}

	@Test
	public void testRequest_as_multipart_form_data() {
		testRequestHeader(CONTENT_TYPE, MULTIPART_FORM_DATA, new Function<HttpRequest>() {
			@Override
			public void apply(HttpRequest rq) {
				rq.asMultipartFormData();
			}
		});
	}

	@Test
	public void testRequest_override_put() {
		testRequestHeader(X_HTTP_METHOD_OVERRIDE, HttpMethod.PUT.name(), new Function<HttpRequest>() {
			@Override
			public void apply(HttpRequest rq) {
				rq.overridePut();
			}
		});
	}

	@Test
	public void testRequest_override_delete() {
		testRequestHeader(X_HTTP_METHOD_OVERRIDE, HttpMethod.DELETE.name(), new Function<HttpRequest>() {
			@Override
			public void apply(HttpRequest rq) {
				rq.overrideDelete();
			}
		});
	}

	@Test
	public void testRequest_add_x_http_method_override() {
		final String value = "PATCH";
		testRequestHeader(X_HTTP_METHOD_OVERRIDE, value, new Function<HttpRequest>() {
			@Override
			public void apply(HttpRequest rq) {
				rq.addXHttpMethodOverride(value);
			}
		});
	}

	private void testRequestHeader(String name, String value, Function<HttpRequest> func) {
		// GIVEN
		int rspStatus = 200;
		String rspBody = "[]";
		stubGetRequest(rspStatus, rspBody);
		HttpRequest rq = client.prepareGet(ENDPOINT);

		// WHEN
		func.apply(rq);

		// THEN
		HttpResponse rsp = rq.execute();
		assertThat(rsp).isNotNull();
		assertThat(rsp.status()).isEqualTo(rspStatus);
		assertRequestWithHeader(ENDPOINT, HttpMethod.GET, name, value);
	}

	@Test
	public void testRequest_execute_json() {
		testRequestExecuteAs(APPLICATION_JSON, new MapperFunction<HttpRequest, HttpResponse>() {
			@Override
			public HttpResponse apply(HttpRequest rq) {
				return rq.executeJson();
			}
		});
	}

	@Test
	public void testRequest_execute_xml() {
		testRequestExecuteAs(APPLICATION_XML, new MapperFunction<HttpRequest, HttpResponse>() {
			@Override
			public HttpResponse apply(HttpRequest rq) {
				return rq.executeXml();
			}
		});
	}

	private void testRequestExecuteAs(String contentType, MapperFunction<HttpRequest, HttpResponse> func) {
		// GIVEN
		int rspStatus = 200;
		String rspBody = "[]";
		stubGetRequest(200, rspBody);
		HttpRequest rq = client.prepareGet(ENDPOINT);

		// WHEN
		HttpResponse rsp = func.apply(rq);

		// THEN
		assertThat(rsp).isNotNull();
		assertThat(rsp.status()).isEqualTo(rspStatus);
		assertThat(rsp.body()).isEqualTo(rspBody);
		assertRequestWithHeader(ENDPOINT, HttpMethod.GET, CONTENT_TYPE, contentType);
	}

	@Test
	public void testRequest_add_query_param() {
		final String name = "firstName";
		final String value = "john";
		final String expectedUrl = ENDPOINT + "?" + formatParam(name, value);
		testQueryParams(expectedUrl, new Function<HttpRequest>() {
			@Override
			public void apply(HttpRequest rq) {
				rq.addQueryParam(name, value);
			}
		});
	}

	@Test
	public void testRequest_add_several_query_params() {
		final String n1 = "firstName";
		final String v1= "john";
		final HttpParameter p1 = param(n1, v1);

		final String n2 = "lastName";
		final String v2 = "doe";
		final HttpParameter p2 = param(n2, v2);

		final String expectedUrl = ENDPOINT +
			"?" + formatParam(n1, v1) +
			"&" + formatParam(n2, v2);

		testQueryParams(expectedUrl, new Function<HttpRequest>() {
			@Override
			public void apply(HttpRequest rq) {
				rq.addQueryParams(p1, p2);
			}
		});
	}

	private void testQueryParams(String expectedUrl, Function<HttpRequest> func) {
		// GIVEN
		int rspStatus = 200;
		String rspBody = "[]";
		stubGetRequest(expectedUrl, rspStatus, rspBody);
		HttpRequest rq = client.prepareGet(ENDPOINT);

		// WHEN
		func.apply(rq);

		// THEN
		HttpResponse rsp = rq.execute();
		assertThat(rsp).isNotNull();
		assertThat(rsp.status()).isEqualTo(rspStatus);
		assertThat(rsp.body()).isEqualTo(rspBody);
		assertRequest(expectedUrl, HttpMethod.GET);
	}

	@Test
	public void testRequest_add_form_param() {
		final String name = "firstName";
		final String value = "john";
		final String expectedBody = formatParam(name, value);
		testRequestBody(expectedBody, new Function<HttpRequest>() {
			@Override
			public void apply(HttpRequest rq) {
				rq.addFormParam(name, value);
			}
		});
	}

	@Test
	public void testRequest_add_several_form_params() {
		final String n1 = "firstName";
		final String v1 = "John";
		final HttpParameter p1 = param(n1, v1);

		final String n2 = "lastName";
		final String v2 = "Doe";
		final HttpParameter p2 = param(n2, v2);

		final String expectedBody = formatParam(n1, v1) + "&" + formatParam(n2, v2);

		testRequestBody(expectedBody, new Function<HttpRequest>() {
			@Override
			public void apply(HttpRequest rq) {
				rq.addFormParams(p1, p2);
			}
		});
	}

	@Test
	public void testRequest_set_body() {
		final String body = "{\"id\": 1, \"firstName\": \"John\", \"lastName\": \"Doe\"}";
		testRequestBody(body, new Function<HttpRequest>() {
			@Override
			public void apply(HttpRequest rq) {
				rq.setBody(body);
			}
		});
	}

	private void testRequestBody(String body, Function<HttpRequest> func) {
		// GIVEN
		int rspStatus = 201;
		String rspBody = "";
		stubPostRequest(rspStatus, rspBody);
		HttpRequest rq = client.preparePost(ENDPOINT);

		// WHEN
		func.apply(rq);

		// THEN
		HttpResponse rsp = rq.execute();
		assertThat(rsp).isNotNull();
		assertThat(rsp.status()).isEqualTo(rspStatus);
		assertThat(rsp.body()).isEqualTo(rspBody);
		assertRequestWithBody(ENDPOINT, HttpMethod.POST, body);
	}

	@Test
	public void testRequest_should_fail_to_add_form_param_on_get_request() {
		thrown.expect(UnsupportedOperationException.class);
		thrown.expectMessage("Http method GET does not support body parameters");

		client
			.prepareGet(ENDPOINT)
			.addFormParam("foo", "bar")
			.executeJson();
	}

	@Test
	public void testRequest_should_fail_to_add_form_param_on_delete_request() {
		thrown.expect(UnsupportedOperationException.class);
		thrown.expectMessage("Http method DELETE does not support body parameters");

		client
			.prepareDelete(ENDPOINT)
			.addFormParam("foo", "bar")
			.executeJson();
	}

	@Test
	public void testRequest_should_fail_to_set_body_on_get_request() {
		thrown.expect(UnsupportedOperationException.class);
		thrown.expectMessage("Http method GET does not support request body");

		client
			.prepareGet(ENDPOINT)
			.setBody("{\"id\": 1, \"firstName\": \"John\", \"lastName\": \"Doe\"}")
			.executeJson();
	}

	@Test
	public void testRequest_should_fail_to_set_body_on_delete_request() {
		thrown.expect(UnsupportedOperationException.class);
		thrown.expectMessage("Http method DELETE does not support request body");

		client
			.prepareDelete(ENDPOINT)
			.setBody("{\"id\": 1, \"firstName\": \"John\", \"lastName\": \"Doe\"}")
			.executeJson();
	}

	@Test
	public void testRequest_with_simple_cookie() {
		stubGetRequest();

		String name = "foo";
		String value = "bar";

		client
			.prepareGet(ENDPOINT)
			.addCookie(Cookies.cookie(name, value))
			.executeJson();

		assertRequestWithCookie(ENDPOINT, HttpMethod.GET, name, value);
	}

	@Test
	public void testRequest_with_simple_cookie_method() {
		stubGetRequest();

		String name = "foo";
		String value = "bar";

		client
			.prepareGet(ENDPOINT)
			.addCookie(name, value)
			.executeJson();

		assertRequestWithCookie(ENDPOINT, HttpMethod.GET, name, value);
	}

	@Test
	public void testRequest_with_simple_cookies() {
		stubGetRequest();

		String n1 = "f1";
		String v1 = "b1";

		String n2 = "f2";
		String v2 = "b2";

		client
			.prepareGet(ENDPOINT)
			.addCookie(Cookies.cookie(n1, v1))
			.addCookie(Cookies.cookie(n2, v2))
			.executeJson();

		List<Pair> expectedCookies = asList(
			pair(n1, v1),
			pair(n2, v2)
		);

		assertRequestWithCookies(ENDPOINT, HttpMethod.GET, expectedCookies);
	}

	@Test
	public void testRequest_with_simple_cookies_method() {
		stubGetRequest();

		String n1 = "f1";
		String v1 = "b1";

		String n2 = "f2";
		String v2 = "b2";

		client
			.prepareGet(ENDPOINT)
			.addCookie(n1, v1)
			.addCookie(n2, v2)
			.executeJson();

		List<Pair> expectedCookies = asList(
			pair(n1, v1),
			pair(n2, v2)
		);

		assertRequestWithCookies(ENDPOINT, HttpMethod.GET, expectedCookies);
	}

	@Test
	public void testRequest_with_complex_cookie() {
		stubGetRequest();

		String name = "foo";
		String value = "bar";
		String domain = "localhost";
		String path = server.getPath();
		boolean secured = true;
		boolean httpOnly = true;

		Calendar now = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		Date maxAgeUtc = utcDate(
			now.get(Calendar.YEAR) + 1,
			now.get(Calendar.MONTH),
			now.get(Calendar.DAY_OF_MONTH),
			now.get(Calendar.HOUR_OF_DAY),
			now.get(Calendar.MINUTE),
			now.get(Calendar.MILLISECOND));

		long maxAge = maxAgeUtc.getTime();
		Long expires = null;

		client
			.prepareGet(ENDPOINT)
			.addHeader(ORIGIN, server.getUrl())
			.addCookie(Cookies.cookie(name, value, domain, path, expires, maxAge, secured, httpOnly))
			.executeJson();

		assertRequestWithCookie(ENDPOINT, HttpMethod.GET, name, value);
	}

	@Test
	public void testResponse_without_headers() {
		stubGetRequest(ENDPOINT, 200, null, null);

		HttpResponse rsp = client
			.prepareGet(ENDPOINT)
			.executeJson();

		List<String> headers = asList(
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

		for (String header : headers) {
			assertThat(rsp.containsHeader(header))
				.overridingErrorMessage("Header " + header + " should be missing")
				.isFalse();

			assertThat(rsp.getHeader(header))
				.overridingErrorMessage("Header " + header + " should be null")
				.isNull();
		}
	}

	@Test
	public void testResponse_with_content_type_header() {
		testResponseHeader(CONTENT_TYPE, APPLICATION_XML, new MapperFunction<HttpResponse, HttpHeader>() {
			@Override
			public HttpHeader apply(HttpResponse rsp) {
				return rsp.getContentType();
			}
		});
	}

	@Test
	public void testResponse_with_etag() {
		final String value = UUID.randomUUID().toString();
		testResponseHeader(ETAG, value, new MapperFunction<HttpResponse, HttpHeader>() {
			@Override
			public HttpHeader apply(HttpResponse rsp) {
				return rsp.getETag();
			}
		});
	}

	@Test
	public void testResponse_with_cache_control() {
		final String value = "no-cache, no-store, must-revalidate";
		testResponseHeader(CACHE_CONTROL, value, new MapperFunction<HttpResponse, HttpHeader>() {
			@Override
			public HttpHeader apply(HttpResponse rsp) {
				return rsp.getCacheControl();
			}
		});
	}

	@Test
	public void testResponse_with_content_encoding() {
		final String value = "identity";
		testResponseHeader(CONTENT_ENCODING, value, new MapperFunction<HttpResponse, HttpHeader>() {
			@Override
			public HttpHeader apply(HttpResponse rsp) {
				return rsp.getContentEncoding();
			}
		});
	}

	@Test
	public void testResponse_with_location() {
		final String value = "http://localhost";
		testResponseHeader(LOCATION, value, new MapperFunction<HttpResponse, HttpHeader>() {
			@Override
			public HttpHeader apply(HttpResponse rsp) {
				return rsp.getLocation();
			}
		});
	}

	@Test
	public void testResponse_with_last_modified() {
		final String value = "Wed, 15 Nov 1995 04:58:08 GMT";
		testResponseHeader(LAST_MODIFIED, value, new MapperFunction<HttpResponse, HttpHeader>() {
			@Override
			public HttpHeader apply(HttpResponse rsp) {
				return rsp.getLastModified();
			}
		});
	}

	@Test
	public void testResponse_with_strict_transport_security() {
		final String value = "max-age=31536000; includeSubDomains; preload";
		testResponseHeader(STRICT_TRANSPORT_SECURITY, value, new MapperFunction<HttpResponse, HttpHeader>() {
			@Override
			public HttpHeader apply(HttpResponse rsp) {
				return rsp.getStrictTransportSecurity();
			}
		});
	}

	@Test
	public void testResponse_with_content_security_policy() {
		final String value = "default-src 'self';";
		testResponseHeader(CONTENT_SECURITY_POLICY, value, new MapperFunction<HttpResponse, HttpHeader>() {
			@Override
			public HttpHeader apply(HttpResponse rsp) {
				return rsp.getContentSecurityPolicy();
			}
		});
	}

	@Test
	public void testResponse_with_x_content_security_policy() {
		final String value = "default-src 'self';";
		testResponseHeader(X_CONTENT_SECURITY_POLICY, value, new MapperFunction<HttpResponse, HttpHeader>() {
			@Override
			public HttpHeader apply(HttpResponse rsp) {
				return rsp.getXContentSecurityPolicy();
			}
		});
	}

	@Test
	public void testResponse_with_x_webkit_csp() {
		final String value = "default-src 'self';";
		testResponseHeader(X_WEBKIT_CSP, value, new MapperFunction<HttpResponse, HttpHeader>() {
			@Override
			public HttpHeader apply(HttpResponse rsp) {
				return rsp.getXWebkitCSP();
			}
		});
	}

	@Test
	public void testResponse_with_xss_protection() {
		final String value = "1; mode=block";
		testResponseHeader(X_XSS_PROTECTION, value, new MapperFunction<HttpResponse, HttpHeader>() {
			@Override
			public HttpHeader apply(HttpResponse rsp) {
				return rsp.getXXSSProtection();
			}
		});
	}

	@Test
	public void testResponse_with_content_type_options() {
		final String value = "nosniff";
		testResponseHeader(X_CONTENT_TYPE_OPTIONS, value, new MapperFunction<HttpResponse, HttpHeader>() {
			@Override
			public HttpHeader apply(HttpResponse rsp) {
				return rsp.getXContentTypeOptions();
			}
		});
	}

	private void testResponseHeader(String name, String value, MapperFunction<HttpResponse, HttpHeader> func) {
		// GIVEN
		stubGetRequest(ENDPOINT, 200, null, pair(name, value));

		// WHEN
		HttpResponse rsp = client
			.prepareGet(ENDPOINT)
			.addAcceptEncoding("identity")
			.executeJson();

		// THEN
		HttpHeader header = rsp.getHeader(name);
		assertThat(rsp.containsHeader(name)).isTrue();
		assertHeader(header, name, value);
		assertThat(func.apply(rsp)).isEqualTo(header);
	}

	@Test
	public void testResponse_with_cookies() {
		final String name = "id";
		final String value = "foo";
		final String domain = "localhost";
		final String path = "/";
		final long maxAge = 3600;

		String cookieValue = name + "=" + value + "; " +
				"Domain=" + domain + "; " +
				"Path=" + path + "; " +
				"Max-Age=" + maxAge + "; " +
				"Secure; " +
				"HttpOnly";

		stubGetRequest(ENDPOINT, 200, null, pair(SET_COOKIE, cookieValue));

		HttpResponse rsp = client
			.prepareGet(ENDPOINT)
			.executeJson();

		List<Cookie> cookies = rsp.getCookies();
		assertThat(cookies).hasSize(1);

		Cookie cookie = cookies.get(0);
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
	public void testResponse_without_cookies() {
		stubGetRequest();

		HttpResponse rsp = client
			.prepareGet(ENDPOINT)
			.executeJson();

		assertThat(rsp.getCookies()).isNotNull().isEmpty();
		assertThat(rsp.getCookie("id")).isNull();
	}

	@Test
	public void testRequest_Response_Duration() {
		stubGetRequest();

		HttpResponse rsp = client
			.prepareGet(ENDPOINT)
			.executeJson();

		long durationNano = rsp.getRequestDuration();
		long durationMillis = rsp.getRequestDurationInMillis();
		assertThat(durationNano).isGreaterThan(0);
		assertThat(durationMillis).isEqualTo(durationNano / 1000);
	}

	@Test
	public void it_should_destroy_client() {
		HttpClient newClient = createClient(server);

		assertThat(newClient.isDestroyed()).isFalse();
		newClient.destroy();
		assertThat(newClient.isDestroyed()).isTrue();
	}

	@Test
	public void it_should_fail_to_create_request_from_a_destroyed_client() {
		HttpClient newClient = createClient(server);
		newClient.destroy();

		thrown.expect(IllegalStateException.class);
		thrown.expectMessage("Cannot create request from a destroyed client");
		newClient.prepareGet("/foo");
	}

	protected abstract HttpClientStrategy strategy();

	private HttpClient createClient(EmbeddedServer<?> server) {
		return strategy().build(server);
	}

	private static void assertHeader(HttpHeader header, String name, String value) {
		assertThat(header.getName()).isEqualTo(name);
		assertThat(header.getValues()).isEqualTo(singletonList(value));
		assertThat(header.getFirstValue()).isEqualTo(value);
		assertThat(header.getLastValue()).isEqualTo(value);
	}

	private static void assertRequest(String endpoint, HttpMethod method) {
		RequestMethod rqMethod = new RequestMethod(method.name());
		RequestPatternBuilder rq = new RequestPatternBuilder(rqMethod, urlEqualTo(endpoint));
		WireMock.verify(1, rq);
	}

	private static void assertRequestWithHeader(String endpoint, HttpMethod method, String headerName, String headerValue) {
		RequestMethod rqMethod = new RequestMethod(method.name());
		RequestPatternBuilder rq = new RequestPatternBuilder(rqMethod, urlEqualTo(endpoint));
		rq.withHeader(headerName, equalTo(headerValue));
		WireMock.verify(1, rq);
	}

	private static void assertRequestWithBody(String endpoint, HttpMethod method, String body) {
		RequestMethod rqMethod = new RequestMethod(method.name());
		RequestPatternBuilder rq = new RequestPatternBuilder(rqMethod, urlEqualTo(endpoint));
		rq.withRequestBody(equalTo(body));
		WireMock.verify(1, rq);
	}

	private static void assertRequestWithCookie(String endpoint, HttpMethod method, String cookieName, String cookieValue) {
		assertRequestWithCookies(endpoint, method, singleton(pair(cookieName, cookieValue)));
	}

	private static void assertRequestWithCookies(String endpoint, HttpMethod method, Iterable<Pair> cookies) {
		RequestMethod rqMethod = new RequestMethod(method.name());
		RequestPatternBuilder rq = new RequestPatternBuilder(rqMethod, urlEqualTo(endpoint));

		for (Pair cookie : cookies) {
			rq.withCookie(cookie.getO1(), equalTo(cookie.getO2()));
		}

		WireMock.verify(1, rq);
	}

	private static void stubGetRequest(int status, String body) {
		stubGetRequest(ENDPOINT, status, body);
	}

	private static void stubGetRequest() {
		stubGetRequest(200, "[{\"id\": 1, \"name\": \"John Doe\"}]");
	}

	private static void stubGetRequest(String url, int status, String body) {
		stubGetRequest(url, status, body, pair(CONTENT_TYPE, APPLICATION_JSON));
	}

	private static void stubGetRequest(String url, int status, String body, Pair header) {
		ResponseDefinitionBuilder rsp = aResponse()
			.withStatus(status)
			.withBody(body);

		if (header != null) {
			rsp.withHeader(header.getO1(), header.getO2());
		}

		stubFor(get(urlEqualTo(url)).willReturn(rsp));
	}

	private static void stubHeadRequest(int status) {
		stubFor(head(urlEqualTo(ENDPOINT))
			.willReturn(aResponse()
				.withStatus(status)
				.withHeader(CONTENT_TYPE, APPLICATION_JSON)));
	}

	private static void stubPostRequest(int status, String body) {
		stubFor(post(urlEqualTo(ENDPOINT))
			.willReturn(aResponse()
				.withStatus(status)
				.withHeader(CONTENT_TYPE, APPLICATION_JSON)
				.withBody(body)));
	}

	private static void stubPutRequest(int status) {
		String url = ENDPOINT + "/1";
		stubFor(put(urlEqualTo(url))
			.willReturn(aResponse()
				.withStatus(status)
				.withHeader(CONTENT_TYPE, APPLICATION_JSON)));
	}

	private static void stubPatchRequest(int status, String body) {
		stubFor(patch(urlEqualTo(ENDPOINT))
				.willReturn(aResponse()
						.withStatus(status)
						.withHeader(CONTENT_TYPE, APPLICATION_JSON)
						.withBody(body)));
	}

	private static void stubDeleteRequest(int status) {
		String url = ENDPOINT + "/1";
		stubFor(delete(urlEqualTo(url))
			.willReturn(aResponse()
				.withStatus(status)
				.withHeader(CONTENT_TYPE, APPLICATION_JSON)));
	}

	private static Date utcDate(int year, int month, int dayOfMonth, int hour, int minutes, int second) {
		final Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, minutes);
		cal.set(Calendar.SECOND, second);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	private static String formatParam(String name, String value) {
		return name + "=" + value;
	}
}
