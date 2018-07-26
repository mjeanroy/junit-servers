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
import com.github.mjeanroy.junit.servers.utils.builders.EmbeddedServerMockBuilder;
import com.github.mjeanroy.junit.servers.utils.commons.Function;
import com.github.mjeanroy.junit.servers.utils.commons.MapperFunction;
import com.github.mjeanroy.junit.servers.utils.commons.Pair;
import com.github.mjeanroy.junit4.runif.RunIfRunner;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

import static com.github.mjeanroy.junit.servers.client.HttpParameter.param;
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
import static com.github.mjeanroy.junit.servers.client.it.HeaderTestUtils.MULTIPART_FORM_DATA;
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
import static com.github.mjeanroy.junit.servers.client.it.WireMockTestUtils.stubDefaultRequest;
import static com.github.mjeanroy.junit.servers.client.it.WireMockTestUtils.stubDeleteRequest;
import static com.github.mjeanroy.junit.servers.client.it.WireMockTestUtils.stubGetRequest;
import static com.github.mjeanroy.junit.servers.client.it.WireMockTestUtils.stubHeadRequest;
import static com.github.mjeanroy.junit.servers.client.it.WireMockTestUtils.stubPatchRequest;
import static com.github.mjeanroy.junit.servers.client.it.WireMockTestUtils.stubPostRequest;
import static com.github.mjeanroy.junit.servers.client.it.WireMockTestUtils.stubPutRequest;
import static com.github.mjeanroy.junit.servers.utils.commons.Pair.pair;
import static com.github.mjeanroy.junit.servers.utils.commons.TestUtils.url;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singleton;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

@RunWith(RunIfRunner.class)
public abstract class BaseHttpClientTest {

	private static final String ENDPOINT = "/people";

	@Rule
	public WireMockRule wireMockRule = new WireMockRule();

	private EmbeddedServer<?> server;
	private String scheme;
	private String host;
	private int port;
	private HttpClient client;

	@Before
	public void setUp() {
		scheme = "http";
		host = "localhost";
		port = wireMockRule.port();
		server = new EmbeddedServerMockBuilder()
				.withScheme(scheme)
				.withHost(host)
				.withPort(port)
				.build();
	}

	@After
	public void tearDown() {
		if (client != null) {
			client.destroy();
		}
	}

	@Test
	public void testRequestUrl() {
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
	public void testGet() {
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
	public void testGetReadingBodyTwice() {
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

		String r1 = rsp.body();
		String r2 = rsp.body();
		assertThat(r1).isEqualTo(r2);
	}

	@Test
	public void testGetWithFullEndpoint() {
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
	public void testGetWithNonEncodedPath() {
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
	public void testHead() {
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
	public void testPost() {
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
			.setBody("{\"name\": \"Jane Doe\"}")
			.execute();

		assertRequest(endpoint, HttpMethod.POST);
		assertThat(rsp.status()).isEqualTo(status);
		assertThat(rsp.body()).isEqualTo(body);
		assertThat(rsp.getContentType().getFirstValue()).isEqualTo(APPLICATION_JSON);
		assertThat(rsp.getContentType().getLastValue()).isEqualTo(APPLICATION_JSON);
	}

	@Test
	public void testPostWithBodyElement() {
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
	public void testPut() {
		final String endpoint = ENDPOINT + "/1";
		final int status = 204;
		final Collection<Pair> headers = emptyList();

		stubPutRequest(endpoint, status, headers);

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
		assertThat(rsp.getContentType()).isNull();
	}

	@Test
	public void testPutWithoutBody() {
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
	public void testPatch() {
		final String endpoint = ENDPOINT;
		final int status = 201;
		final Collection<Pair> headers = singleton(pair(CONTENT_TYPE, APPLICATION_JSON));
		final String body = "{\"id\": 1, \"name\": \"Jane Doe\"}";

		stubPatchRequest(endpoint, status, headers, body);

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
		assertThat(rsp.getContentType().getLastValue()).isEqualTo(APPLICATION_JSON);
	}

	@Test
	public void testDelete() {
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
	public void testRequestWithDefaultHeader() {
		final String name = "X-Custom-Header";
		final String value = "FooBar";
		final HttpClientConfiguration configuration = new HttpClientConfiguration.Builder()
			.addDefaultHeader(name, value)
			.build();

		stubDefaultRequest(ENDPOINT);

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

		stubDefaultRequest(ENDPOINT);

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
		String endpoint = ENDPOINT;
		stubDefaultRequest(ENDPOINT);

		// WHEN
		HttpRequest rq = client.prepareGet(endpoint);
		func.apply(rq);

		// THEN
		HttpResponse rsp = rq.execute();
		assertThat(rsp).isNotNull();
		assertThat(rsp.status()).isEqualTo(200);
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
		final Collection<Pair> headers = singleton(pair(CONTENT_TYPE, APPLICATION_JSON));
		final String rspBody = "[]";

		stubGetRequest(endpoint, rspStatus, headers, rspBody);

		// WHEN
		HttpRequest rq = createDefaultClient().prepareGet(ENDPOINT);
		HttpResponse rsp = func.apply(rq);

		// THEN
		assertThat(rsp).isNotNull();
		assertThat(rsp.status()).isEqualTo(rspStatus);
		assertThat(rsp.body()).isEqualTo(rspBody);
		assertRequestWithHeader(endpoint, HttpMethod.GET, CONTENT_TYPE, contentType);
	}

	@Test
	public void testRequest_add_non_encoded_query_param() {
		final String name = "name";
		final String value = "john doe";
		final String expectedUrl = ENDPOINT + "?" + encodeQueryParam(name, value);
		testQueryParams(expectedUrl, new Function<HttpRequest>() {
			@Override
			public void apply(HttpRequest rq) {
				rq.addQueryParam(name, value);
			}
		});
	}

	@Test
	public void testRequest_add_non_encoded_query_param_with_ampersand() {
		final String name = "name";
		final String value = "john & jane doe";
		final String expectedUrl = ENDPOINT + "?" + encodeQueryParam(name, value);
		testQueryParams(expectedUrl, new Function<HttpRequest>() {
			@Override
			public void apply(HttpRequest rq) {
				rq.addQueryParam(name, value);
			}
		});
	}

	@Test
	public void testRequest_add_query_param() {
		final String name = "firstName";
		final String value = "john";
		final String expectedUrl = ENDPOINT + "?" + encodeQueryParam(name, value);
		testQueryParams(expectedUrl, new Function<HttpRequest>() {
			@Override
			public void apply(HttpRequest rq) {
				rq.addQueryParam(name, value);
			}
		});
	}

	@Test
	public void testRequest_add_query_param_without_value() {
		final String name = "flag";
		final String value = null;
		final String expectedUrl = ENDPOINT + "?" + encodeQueryParam(name, value);
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
			"?" + encodeQueryParam(n1, v1) +
			"&" + encodeQueryParam(n2, v2);

		testQueryParams(expectedUrl, new Function<HttpRequest>() {
			@Override
			public void apply(HttpRequest rq) {
				rq.addQueryParams(p1, p2);
			}
		});
	}

	private void testQueryParams(String expectedUrl, Function<HttpRequest> func) {
		// GIVEN
		final int rspStatus = 200;
		final Collection<Pair> headers = singleton(pair(CONTENT_TYPE, APPLICATION_JSON));
		final String rspBody = "[]";

		stubGetRequest(expectedUrl, rspStatus, headers, rspBody);

		// WHEN
		HttpRequest rq = createDefaultClient().prepareGet(ENDPOINT);
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
		final String expectedBody = encodeFormParam(name, value);
		testRequestBody(expectedBody, new Function<HttpRequest>() {
			@Override
			public void apply(HttpRequest rq) {
				rq.addFormParam(name, value);
			}
		});
	}

	@Test
	public void testRequest_add_form_param_without_value() {
		final String name = "flag";
		final String value = "";
		final String expectedBody = encodeFormParam(name, value);
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

		final String expectedBody = encodeFormParam(n1, v1) + "&" + encodeFormParam(n2, v2);

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
		final String expectedBody = encodeFormParam(name, value);
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
		final String expectedBody = encodeFormParam(name, value);
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
		final int rspStatus = 204;
		final Collection<Pair> headers = emptyList();
		final String rspBody = "";
		stubPostRequest(endpoint, rspStatus, headers, rspBody);
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
		HttpRequest httpRequest = createDefaultClient().prepareGet(ENDPOINT);
		String name = "foo";
		String value = "bar";

		assertThatThrownBy(addFormParam(httpRequest, name, value))
			.isExactlyInstanceOf(UnsupportedOperationException.class)
			.hasMessage("Http method GET does not support body parameters");
	}

	@Test
	public void testRequest_should_fail_to_add_form_param_on_delete_request() {
		HttpRequest httpRequest = createDefaultClient().prepareDelete(ENDPOINT);
		String name = "foo";
		String value = "bar";

		assertThatThrownBy(addFormParam(httpRequest, name, value))
			.isExactlyInstanceOf(UnsupportedOperationException.class)
			.hasMessage("Http method DELETE does not support body parameters");
	}

	@Test
	public void testRequest_should_fail_to_set_body_on_get_request() {
		HttpRequest httpRequest = createDefaultClient().prepareGet(ENDPOINT);
		String body = "{\"id\": 1, \"firstName\": \"John\", \"lastName\": \"Doe\"}";

		assertThatThrownBy(setRequestBody(httpRequest, body))
			.isExactlyInstanceOf(UnsupportedOperationException.class)
			.hasMessage("Http method GET does not support request body");
	}

	@Test
	public void testRequest_should_fail_to_set_body_on_delete_request() {
		HttpRequest httpRequest = createDefaultClient().prepareDelete(ENDPOINT);
		String body = "{\"id\": 1, \"firstName\": \"John\", \"lastName\": \"Doe\"}";

		assertThatThrownBy(setRequestBody(httpRequest, body))
			.isExactlyInstanceOf(UnsupportedOperationException.class)
			.hasMessage("Http method DELETE does not support request body");
	}

	@Test
	public void testRequest_with_simple_cookie() {
		String endpoint = ENDPOINT;
		stubDefaultRequest(endpoint);

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

		stubDefaultRequest(endpoint);

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

		stubDefaultRequest(endpoint);

		createCustomClient(configuration)
			.prepareGet(endpoint)
			.executeJson();

		assertRequestWithCookie(endpoint, HttpMethod.GET, name, value);
	}

	@Test
	public void testRequest_with_simple_cookie_method() {
		String endpoint = ENDPOINT;
		stubDefaultRequest(endpoint);

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
		String endpoint = ENDPOINT;
		stubDefaultRequest(endpoint);

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
		String endpoint = ENDPOINT;
		stubDefaultRequest(endpoint);

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
		String endpoint = ENDPOINT;
		stubDefaultRequest(endpoint);

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
		final int status = 200;
		final String body = null;
		final Collection<Pair> headers = emptyList();

		stubGetRequest(endpoint, status, headers, body);

		HttpResponse rsp = createDefaultClient()
			.prepareGet(endpoint)
			.executeJson();

		List<String> testedHeaders = asList(
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
	public void testResponse_with_custom_header() {
		final String name = "X-Custom-Header";
		final String value = "Foo";

		testResponseHeader(name, value, new MapperFunction<HttpResponse, HttpHeader>() {
			@Override
			public HttpHeader apply(HttpResponse rsp) {
				return rsp.getHeader(name);
			}
		});
	}

	@Test
	public void testResponse_with_custom_header_and_multiple_values() {
		final String name = "X-Custom-Header";
		final String v1 = "Foo";
		final String v2 = "Bar";
		final String v3 = "Quix";
		final List<String> values = asList(v1, v2, v3);

		testResponseWithSeveralValues(name, values, new MapperFunction<HttpResponse, HttpHeader>() {
			@Override
			public HttpHeader apply(HttpResponse rsp) {
				return rsp.getHeader(name);
			}
		});
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
		final String endpoint = ENDPOINT;
		final int status = 200;
		final Collection<Pair> headers = singleton(pair(name, value));
		final String body = null;

		stubGetRequest(endpoint, status, headers, body);

		// WHEN
		HttpResponse rsp = createDefaultClient()
			.prepareGet(endpoint)
			.addAcceptEncoding("identity")
			.executeJson();

		// THEN
		HttpHeader header = rsp.getHeader(name);
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
		HttpResponse rsp = createDefaultClient()
				.prepareGet(endpoint)
				.addAcceptEncoding("identity")
				.executeJson();

		// THEN
		HttpHeader header = rsp.getHeader(name);
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
	public void testResponse_with_cookies() {
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

		HttpResponse rsp = createDefaultClient()
			.prepareGet(endpoint)
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
		String endpoint = ENDPOINT;
		stubDefaultRequest(endpoint);

		HttpResponse rsp = createDefaultClient()
			.prepareGet(endpoint)
			.executeJson();

		assertThat(rsp.getCookies()).isNotNull().isEmpty();
		assertThat(rsp.getCookie("id")).isNull();
	}

	@Test
	public void testRequest_Response_Duration() {
		String endpoint = ENDPOINT;
		stubDefaultRequest(endpoint);

		HttpResponse rsp = createDefaultClient()
			.prepareGet(endpoint)
			.executeJson();

		long durationNano = rsp.getRequestDuration();
		long durationMillis = rsp.getRequestDurationInMillis();
		assertThat(durationNano).isGreaterThan(0);
		assertThat(durationMillis).isEqualTo(durationNano / 1000 / 1000);
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
		String endpoint = "/foo";
		HttpClient newClient = createDefaultClient();
		newClient.destroy();

		assertThatThrownBy(prepareGet(newClient, endpoint))
			.isExactlyInstanceOf(IllegalStateException.class)
			.hasMessage("Cannot create request from a destroyed client");
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

	private interface HttpClientFactory {
		HttpClient create();
	}

	private static ThrowingCallable prepareGet(final HttpClient client, final String endpoint) {
		return new ThrowingCallable() {
			@Override
			public void call() {
				client.prepareGet(endpoint);
			}
		};
	}

	private static ThrowingCallable addFormParam(final HttpRequest request, final String name, final String value) {
		return new ThrowingCallable() {
			@Override
			public void call() {
				request.addFormParam(name, value);
			}
		};
	}

	private static ThrowingCallable setRequestBody(final HttpRequest request, final String body) {
		return new ThrowingCallable() {
			@Override
			public void call() {
				request.setBody(body);
			}
		};
	}
}
