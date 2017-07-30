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

import static com.github.mjeanroy.junit.servers.client.HttpParameter.param;
import static com.github.mjeanroy.junit.servers.client.it.HeaderUtils.ACCEPT;
import static com.github.mjeanroy.junit.servers.client.it.HeaderUtils.ACCEPT_ENCODING;
import static com.github.mjeanroy.junit.servers.client.it.HeaderUtils.ACCEPT_LANGUAGE;
import static com.github.mjeanroy.junit.servers.client.it.HeaderUtils.APPLICATION_JSON;
import static com.github.mjeanroy.junit.servers.client.it.HeaderUtils.APPLICATION_XML;
import static com.github.mjeanroy.junit.servers.client.it.HeaderUtils.CACHE_CONTROL;
import static com.github.mjeanroy.junit.servers.client.it.HeaderUtils.CONTENT_ENCODING;
import static com.github.mjeanroy.junit.servers.client.it.HeaderUtils.CONTENT_SECURITY_POLICY;
import static com.github.mjeanroy.junit.servers.client.it.HeaderUtils.CONTENT_TYPE;
import static com.github.mjeanroy.junit.servers.client.it.HeaderUtils.ETAG;
import static com.github.mjeanroy.junit.servers.client.it.HeaderUtils.IF_MATCH;
import static com.github.mjeanroy.junit.servers.client.it.HeaderUtils.IF_MODIFIED_SINCE;
import static com.github.mjeanroy.junit.servers.client.it.HeaderUtils.IF_NONE_MATCH;
import static com.github.mjeanroy.junit.servers.client.it.HeaderUtils.IF_UNMODIFIED_SINCE;
import static com.github.mjeanroy.junit.servers.client.it.HeaderUtils.LAST_MODIFIED;
import static com.github.mjeanroy.junit.servers.client.it.HeaderUtils.LOCATION;
import static com.github.mjeanroy.junit.servers.client.it.HeaderUtils.MULTIPART_FORM_DATA;
import static com.github.mjeanroy.junit.servers.client.it.HeaderUtils.ORIGIN;
import static com.github.mjeanroy.junit.servers.client.it.HeaderUtils.REFERER;
import static com.github.mjeanroy.junit.servers.client.it.HeaderUtils.SET_COOKIE;
import static com.github.mjeanroy.junit.servers.client.it.HeaderUtils.STRICT_TRANSPORT_SECURITY;
import static com.github.mjeanroy.junit.servers.client.it.HeaderUtils.USER_AGENT;
import static com.github.mjeanroy.junit.servers.client.it.HeaderUtils.X_CONTENT_SECURITY_POLICY;
import static com.github.mjeanroy.junit.servers.client.it.HeaderUtils.X_CONTENT_TYPE_OPTIONS;
import static com.github.mjeanroy.junit.servers.client.it.HeaderUtils.X_CSRF_TOKEN;
import static com.github.mjeanroy.junit.servers.client.it.HeaderUtils.X_HTTP_METHOD_OVERRIDE;
import static com.github.mjeanroy.junit.servers.client.it.HeaderUtils.X_REQUESTED_WITH;
import static com.github.mjeanroy.junit.servers.client.it.HeaderUtils.X_WEBKIT_CSP;
import static com.github.mjeanroy.junit.servers.client.it.HeaderUtils.X_XSS_PROTECTION;
import static com.github.mjeanroy.junit.servers.client.it.HeaderUtils.assertHeader;
import static com.github.mjeanroy.junit.servers.client.it.HttpUtils.formatFormParam;
import static com.github.mjeanroy.junit.servers.client.it.HttpUtils.formatParam;
import static com.github.mjeanroy.junit.servers.client.it.HttpUtils.utcDate;
import static com.github.mjeanroy.junit.servers.client.it.WireMockUtils.assertRequest;
import static com.github.mjeanroy.junit.servers.client.it.WireMockUtils.assertRequestWithBody;
import static com.github.mjeanroy.junit.servers.client.it.WireMockUtils.assertRequestWithCookie;
import static com.github.mjeanroy.junit.servers.client.it.WireMockUtils.assertRequestWithCookies;
import static com.github.mjeanroy.junit.servers.client.it.WireMockUtils.assertRequestWithHeader;
import static com.github.mjeanroy.junit.servers.client.it.WireMockUtils.stubDefaultGetRequest;
import static com.github.mjeanroy.junit.servers.client.it.WireMockUtils.stubDeleteRequest;
import static com.github.mjeanroy.junit.servers.client.it.WireMockUtils.stubGetRequest;
import static com.github.mjeanroy.junit.servers.client.it.WireMockUtils.stubHeadRequest;
import static com.github.mjeanroy.junit.servers.client.it.WireMockUtils.stubPatchRequest;
import static com.github.mjeanroy.junit.servers.client.it.WireMockUtils.stubPostRequest;
import static com.github.mjeanroy.junit.servers.client.it.WireMockUtils.stubPutRequest;
import static com.github.mjeanroy.junit.servers.utils.commons.Pair.pair;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import com.github.mjeanroy.junit.servers.client.Cookie;
import com.github.mjeanroy.junit.servers.client.Cookies;
import com.github.mjeanroy.junit.servers.client.HttpClient;
import com.github.mjeanroy.junit.servers.client.HttpClientConfiguration;
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
import com.github.tomakehurst.wiremock.junit.WireMockRule;

@RunWith(RunIfRunner.class)
public abstract class BaseHttpClientTest {

	private static final String ENDPOINT = "/people";

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
	}

	@After
	public void tearDown() {
		if (client != null) {
			client.destroy();
		}
	}

	@Test
	public void testRequestUrl() {
		final HttpRequest rq = createDefaultClient()
			.prepareGet(ENDPOINT)
			.acceptJson()
			.asXmlHttpRequest();

		assertThat(rq.getUrl()).isEqualTo(url + "/people");
		assertThat(rq.getMethod()).isEqualTo(HttpMethod.GET);
	}

	@Test
	public void testGet() {
		final String endpoint = ENDPOINT;
		final int status = 200;
		final String body = "[{\"id\": 1, \"name\": \"John Doe\"}]";

		stubGetRequest(endpoint, status, body);

		final HttpResponse rsp = createDefaultClient()
			.prepareGet(endpoint)
			.acceptJson()
			.asXmlHttpRequest()
			.execute();

		assertRequest(endpoint, HttpMethod.GET);
		assertThat(rsp.status()).isEqualTo(status);
		assertThat(rsp.body()).isEqualTo(body);
		assertThat(rsp.getContentType().getFirstValue()).isEqualTo(APPLICATION_JSON);
	}

	@Test
	public void testHead() {
		final String endpoint = ENDPOINT;
		final int status = 200;

		stubHeadRequest(endpoint, status);

		final HttpResponse rsp = createDefaultClient()
				.prepareHead(endpoint)
				.acceptJson()
				.asXmlHttpRequest()
				.execute();

		assertRequest(endpoint, HttpMethod.HEAD);
		assertThat(rsp.status()).isEqualTo(status);
		assertThat(rsp.body()).isNullOrEmpty();
		assertThat(rsp.getContentType().getFirstValue()).isEqualTo(APPLICATION_JSON);
	}

	@Test
	public void testPost() {
		final String endpoint = ENDPOINT;
		final int status = 201;
		final String body = "{\"id\": 1, \"name\": \"Jane Doe\"}";

		stubPostRequest(endpoint, status, body);

		final HttpResponse rsp = createDefaultClient()
			.preparePost(endpoint)
			.acceptJson()
			.asJson()
			.asXmlHttpRequest()
			.setBody("{\"name\": \"Jane Doe\"}")
			.execute();

		assertRequest(endpoint, HttpMethod.POST);
		assertThat(rsp.status()).isEqualTo(status);
		assertThat(rsp.body()).isEqualTo(body);
		assertThat(rsp.getContentType().getFirstValue()).isEqualTo(APPLICATION_JSON);
	}

	@Test
	public void testPostWithBodyElement() {
		final String endpoint = ENDPOINT;
		final int status = 201;
		final String body = "{\"id\": 1, \"name\": \"Jane Doe\"}";

		stubPostRequest(endpoint, status, body);

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
	}

	@Test
	public void testPut() {
		final String endpoint = ENDPOINT + "/1";
		final int status = 204;

		stubPutRequest(endpoint, status);

		final HttpResponse rsp = createDefaultClient()
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

		stubPutRequest(endpoint, status);

		final HttpResponse rsp = createDefaultClient()
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
		final String endpoint = ENDPOINT;
		final int status = 201;
		final String body = "{\"id\": 1, \"name\": \"Jane Doe\"}";

		stubPatchRequest(endpoint, status, body);

		final HttpResponse rsp = createDefaultClient()
				.preparePatch(endpoint)
				.acceptJson()
				.asJson()
				.asXmlHttpRequest()
				.setBody("{\"name\": \"Jane Doe\"}")
				.execute();

		assertRequest(endpoint, HttpMethod.PATCH);
		assertThat(rsp.status()).isEqualTo(status);
		assertThat(rsp.body()).isEqualTo(body);
		assertThat(rsp.getContentType().getFirstValue()).isEqualTo(APPLICATION_JSON);
	}

	@Test
	public void testDelete() {
		final int status = 204;
		final String endpoint = ENDPOINT + "/1";

		stubDeleteRequest(endpoint, status);

		final HttpResponse rsp = createDefaultClient()
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
	public void testRequestWithDefaultHeader() {
		final String name = "X-Custom-Header";
		final String value = "FooBar";
		final HttpClientConfiguration configuration = new HttpClientConfiguration.Builder()
			.addDefaultHeader(name, value)
			.build();

		final int status = 200;
		final String body = "[{\"id\": 1, \"name\": \"John Doe\"}]";
		stubGetRequest(ENDPOINT, status, body);

		testRequestHeader(createCustomClient(configuration), name, value, new Function<HttpRequest>() {
			@Override
			public void apply(HttpRequest rq) {
			}
		});
	}

	@Test
	public void testRequestWithDefaultHeaderObject() {
		final String name = "X-Custom-Header";
		final String value = "FooBar";
		final HttpHeader header = HttpHeader.header(name, value);
		final HttpClientConfiguration configuration = new HttpClientConfiguration.Builder()
			.addDefaultHeader(header)
			.build();

		final int status = 200;
		final String body = "[{\"id\": 1, \"name\": \"John Doe\"}]";
		stubGetRequest(ENDPOINT, status, body);

		testRequestHeader(createCustomClient(configuration), name, value, new Function<HttpRequest>() {
			@Override
			public void apply(HttpRequest rq) {
			}
		});
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
	public void testRequest_with_custom_header_instance() {
		final String name = "X-Custom-Header";
		final String value = "FooBar";
		final HttpHeader header = HttpHeader.header(name, value);
		testRequestHeader(name, value, new Function<HttpRequest>() {
			@Override
			public void apply(HttpRequest rq) {
				rq.addHeader(header);
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
		testRequestHeader(createDefaultClient(), name, value, func);
	}

	private void testRequestHeader(HttpClient client, String name, String value, Function<HttpRequest> func) {
		// GIVEN
		final String endpoint = ENDPOINT;
		final int rspStatus = 200;
		final String rspBody = "[]";
		stubGetRequest(endpoint, rspStatus, rspBody);
		HttpRequest rq = client.prepareGet(endpoint);

		// WHEN
		func.apply(rq);

		// THEN
		HttpResponse rsp = rq.execute();
		assertThat(rsp).isNotNull();
		assertThat(rsp.status()).isEqualTo(rspStatus);
		assertRequestWithHeader(endpoint, HttpMethod.GET, name, value);
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
		final String endpoint = ENDPOINT;
		final int rspStatus = 200;
		final String rspBody = "[]";
		stubGetRequest(endpoint, 200, rspBody);
		HttpRequest rq = createDefaultClient().prepareGet(ENDPOINT);

		// WHEN
		HttpResponse rsp = func.apply(rq);

		// THEN
		assertThat(rsp).isNotNull();
		assertThat(rsp.status()).isEqualTo(rspStatus);
		assertThat(rsp.body()).isEqualTo(rspBody);
		assertRequestWithHeader(endpoint, HttpMethod.GET, CONTENT_TYPE, contentType);
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
		HttpRequest rq = createDefaultClient().prepareGet(ENDPOINT);

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
		final String expectedBody = formatFormParam(name, value);
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

		final String expectedBody = formatFormParam(n1, v1) + "&" + formatParam(n2, v2);

		testRequestBody(expectedBody, new Function<HttpRequest>() {
			@Override
			public void apply(HttpRequest rq) {
				rq.addFormParams(p1, p2);
			}
		});
	}

	@Test
	public void testRequest_add_non_escaped_form_param_name() {
		final String name = "first name";
		final String value = "john";
		final String expectedBody = formatFormParam(name, value);
		testRequestBody(expectedBody, new Function<HttpRequest>() {
			@Override
			public void apply(HttpRequest rq) {
				rq.addFormParam(name, value);
			}
		});
	}

	@Test
	public void testRequest_add_non_escaped_form_param_value() {
		final String name = "name";
		final String value = "john doe";
		final String expectedBody = formatFormParam(name, value);
		testRequestBody(expectedBody, new Function<HttpRequest>() {
			@Override
			public void apply(HttpRequest rq) {
				rq.addFormParam(name, value);
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
		final String endpoint = ENDPOINT;
		final int rspStatus = 201;
		final String rspBody = "";
		stubPostRequest(endpoint, rspStatus, rspBody);
		HttpRequest rq = createDefaultClient().preparePost(ENDPOINT);

		// WHEN
		func.apply(rq);

		// THEN
		HttpResponse rsp = rq.execute();
		assertThat(rsp).isNotNull();
		assertThat(rsp.status()).isEqualTo(rspStatus);
		assertThat(rsp.body()).isEqualTo(rspBody);
		assertRequestWithBody(endpoint, HttpMethod.POST, body);
	}

	@Test
	public void testRequest_should_fail_to_add_form_param_on_get_request() {
		thrown.expect(UnsupportedOperationException.class);
		thrown.expectMessage("Http method GET does not support body parameters");

		createDefaultClient()
			.prepareGet(ENDPOINT)
			.addFormParam("foo", "bar")
			.executeJson();
	}

	@Test
	public void testRequest_should_fail_to_add_form_param_on_delete_request() {
		thrown.expect(UnsupportedOperationException.class);
		thrown.expectMessage("Http method DELETE does not support body parameters");

		createDefaultClient()
			.prepareDelete(ENDPOINT)
			.addFormParam("foo", "bar")
			.executeJson();
	}

	@Test
	public void testRequest_should_fail_to_set_body_on_get_request() {
		thrown.expect(UnsupportedOperationException.class);
		thrown.expectMessage("Http method GET does not support request body");

		createDefaultClient()
			.prepareGet(ENDPOINT)
			.setBody("{\"id\": 1, \"firstName\": \"John\", \"lastName\": \"Doe\"}")
			.executeJson();
	}

	@Test
	public void testRequest_should_fail_to_set_body_on_delete_request() {
		thrown.expect(UnsupportedOperationException.class);
		thrown.expectMessage("Http method DELETE does not support request body");

		createDefaultClient()
			.prepareDelete(ENDPOINT)
			.setBody("{\"id\": 1, \"firstName\": \"John\", \"lastName\": \"Doe\"}")
			.executeJson();
	}

	@Test
	public void testRequest_with_simple_cookie() {
		final String endpoint = ENDPOINT;

		stubDefaultGetRequest(endpoint);

		String name = "foo";
		String value = "bar";

		createDefaultClient()
			.prepareGet(endpoint)
			.addCookie(Cookies.cookie(name, value))
			.executeJson();

		assertRequestWithCookie(endpoint, HttpMethod.GET, name, value);
	}

	@Test
	public void testRequest_with_default_cookie() {
		final String endpoint = ENDPOINT;
		final String name = "foo";
		final String value = "bar";
		final HttpClientConfiguration configuration = new HttpClientConfiguration.Builder()
			.addDefaultCookie(name, value)
			.build();

		stubDefaultGetRequest(endpoint);

		createCustomClient(configuration)
			.prepareGet(endpoint)
			.executeJson();

		assertRequestWithCookie(endpoint, HttpMethod.GET, name, value);
	}

	@Test
	public void testRequest_with_default_cookie_object() {
		final String endpoint = ENDPOINT;
		final String name = "foo";
		final String value = "bar";
		final HttpClientConfiguration configuration = new HttpClientConfiguration.Builder()
			.addDefaultCookie(Cookies.cookie(name, value))
			.build();

		stubDefaultGetRequest(endpoint);

		createCustomClient(configuration)
			.prepareGet(endpoint)
			.executeJson();

		assertRequestWithCookie(endpoint, HttpMethod.GET, name, value);
	}

	@Test
	public void testRequest_with_simple_cookie_method() {
		final String endpoint = ENDPOINT;

		stubDefaultGetRequest(endpoint);

		String name = "foo";
		String value = "bar";

		createDefaultClient()
			.prepareGet(endpoint)
			.addCookie(name, value)
			.executeJson();

		assertRequestWithCookie(endpoint, HttpMethod.GET, name, value);
	}

	@Test
	public void testRequest_with_simple_cookies() {
		final String endpoint = ENDPOINT;

		stubDefaultGetRequest(endpoint);

		String n1 = "f1";
		String v1 = "b1";

		String n2 = "f2";
		String v2 = "b2";

		createDefaultClient()
			.prepareGet(endpoint)
			.addCookie(Cookies.cookie(n1, v1))
			.addCookie(Cookies.cookie(n2, v2))
			.executeJson();

		List<Pair> expectedCookies = asList(
			pair(n1, v1),
			pair(n2, v2)
		);

		assertRequestWithCookies(endpoint, HttpMethod.GET, expectedCookies);
	}

	@Test
	public void testRequest_with_simple_cookies_method() {
		final String endpoint = ENDPOINT;

		stubDefaultGetRequest(endpoint);

		String n1 = "f1";
		String v1 = "b1";

		String n2 = "f2";
		String v2 = "b2";

		createDefaultClient()
			.prepareGet(endpoint)
			.addCookie(n1, v1)
			.addCookie(n2, v2)
			.executeJson();

		List<Pair> expectedCookies = asList(
			pair(n1, v1),
			pair(n2, v2)
		);

		assertRequestWithCookies(endpoint, HttpMethod.GET, expectedCookies);
	}

	@Test
	public void testRequest_with_complex_cookie() {
		final String endpoint = ENDPOINT;

		stubDefaultGetRequest(endpoint);

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

		createDefaultClient()
			.prepareGet(endpoint)
			.addHeader(ORIGIN, server.getUrl())
			.addCookie(Cookies.cookie(name, value, domain, path, expires, maxAge, secured, httpOnly))
			.executeJson();

		assertRequestWithCookie(endpoint, HttpMethod.GET, name, value);
	}

	@Test
	public void testResponse_without_headers() {
		final String endpoint = ENDPOINT;

		stubGetRequest(endpoint, 200, null, null);

		HttpResponse rsp = createDefaultClient()
			.prepareGet(endpoint)
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
		HttpResponse rsp = createDefaultClient()
			.prepareGet(ENDPOINT)
			.addAcceptEncoding("identity")
			.executeJson();

		// THEN
		HttpHeader header = rsp.getHeader(name);
		assertThat(rsp.containsHeader(name)).isTrue();
		assertThat(func.apply(rsp)).isEqualTo(header);
		assertHeader(header, name, value);
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

		HttpResponse rsp = createDefaultClient()
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
		final String endpoint = ENDPOINT;

		stubDefaultGetRequest(endpoint);

		HttpResponse rsp = createDefaultClient()
			.prepareGet(endpoint)
			.executeJson();

		assertThat(rsp.getCookies()).isNotNull().isEmpty();
		assertThat(rsp.getCookie("id")).isNull();
	}

	@Test
	public void testRequest_Response_Duration() {
		final String endpoint = ENDPOINT;

		stubDefaultGetRequest(endpoint);

		HttpResponse rsp = createDefaultClient()
			.prepareGet(endpoint)
			.executeJson();

		long durationNano = rsp.getRequestDuration();
		long durationMillis = rsp.getRequestDurationInMillis();
		assertThat(durationNano).isGreaterThan(0);
		assertThat(durationMillis).isEqualTo(durationNano / 1000);
	}

	@Test
	public void it_should_destroy_client() {
		HttpClient newClient = createDefaultClient();

		assertThat(newClient.isDestroyed()).isFalse();
		newClient.destroy();
		assertThat(newClient.isDestroyed()).isTrue();
	}

	@Test
	public void it_should_fail_to_create_request_from_a_destroyed_client() {
		HttpClient newClient = createDefaultClient();
		newClient.destroy();

		thrown.expect(IllegalStateException.class);
		thrown.expectMessage("Cannot create request from a destroyed client");
		newClient.prepareGet("/foo");
	}

	protected abstract HttpClientStrategy strategy();

	private HttpClient createDefaultClient() {
		return createClient(new HttpClientFactory() {
			@Override
			public HttpClient create() {
				return strategy().build(server);
			}
		});
	}

	private HttpClient createCustomClient(final HttpClientConfiguration configuration) {
		return createClient(new HttpClientFactory() {
			@Override
			public HttpClient create() {
				return strategy().build(configuration, server);
			}
		});
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

	interface HttpClientFactory {
		HttpClient create();
	}
}
