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

package com.github.mjeanroy.junit.servers.client.it;

import com.github.mjeanroy.junit.servers.client.HttpClient;
import com.github.mjeanroy.junit.servers.client.HttpResponse;
import com.github.mjeanroy.junit.servers.servers.EmbeddedServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public abstract class BaseHttpClientTest {

	private static final String APPLICATION_JSON = "application/json";

	@Rule
	public WireMockRule wireMockRule = new WireMockRule();

	private EmbeddedServer server;

	@Before
	public void setUp() {
		final int port = wireMockRule.port();
		server = mock(EmbeddedServer.class);
		when(server.getPort()).thenReturn(port);
		when(server.getUrl()).thenReturn("http://localhost:" + port);
		when(server.getPath()).thenReturn("/");
	}

	@Test
	public void testGet() {
		final int status = 200;
		final String body = "[{\"id\": 1, \"name\": \"John Doe\"}]";
		final String contentType = APPLICATION_JSON;

		WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/people"))
			.withHeader("Accept", WireMock.equalTo(APPLICATION_JSON))
			.willReturn(WireMock.aResponse()
				.withStatus(status)
				.withHeader("Content-Type", contentType)
				.withBody(body)));

		final HttpResponse rsp = createClient(server)
			.prepareGet("/people")
			.acceptJson()
			.asXmlHttpRequest()
			.execute();

		assertThat(rsp.status()).isEqualTo(status);
		assertThat(rsp.body()).isEqualTo(body);
		assertThat(rsp.getContentType().getFirstValue()).isEqualTo(contentType);
	}

	@Test
	public void testPost() {
		final int status = 201;
		final String body = "{\"id\": 1, \"name\": \"Jane Doe\"}";
		final String contentType = APPLICATION_JSON;

		WireMock.stubFor(WireMock.post(WireMock.urlEqualTo("/people"))
			.withHeader("Accept", WireMock.equalTo(APPLICATION_JSON))
			.willReturn(WireMock.aResponse()
				.withStatus(status)
				.withHeader("Content-Type", contentType)
				.withBody(body)));

		final HttpResponse rsp = createClient(server)
			.preparePost("/people")
			.acceptJson()
			.asJson()
			.asXmlHttpRequest()
			.setBody("{\"name\": \"Jane Doe\"}")
			.execute();

		assertThat(rsp.status()).isEqualTo(status);
		assertThat(rsp.body()).isEqualTo(body);
		assertThat(rsp.getContentType().getFirstValue()).isEqualTo(contentType);
	}

	@Test
	public void testPut() {
		final int status = 204;
		final String contentType = APPLICATION_JSON;

		WireMock.stubFor(WireMock.put(WireMock.urlEqualTo("/people/1"))
			.withHeader("Accept", WireMock.equalTo(APPLICATION_JSON))
			.willReturn(WireMock.aResponse()
				.withStatus(status)
				.withHeader("Content-Type", contentType)));

		final HttpResponse rsp = createClient(server)
			.preparePut("/people/1")
			.acceptJson()
			.asJson()
			.asXmlHttpRequest()
			.setBody("{\"id\": 1, \"name\": \"Jane Doe\"}")
			.execute();

		assertThat(rsp.status()).isEqualTo(status);
		assertThat(rsp.body()).isEmpty();
		assertThat(rsp.getContentType().getFirstValue()).isEqualTo(contentType);
	}

	@Test
	public void testDelete() {
		final int status = 204;
		final String contentType = APPLICATION_JSON;

		WireMock.stubFor(WireMock.delete(WireMock.urlEqualTo("/people/1"))
			.withHeader("Accept", WireMock.equalTo(APPLICATION_JSON))
			.willReturn(WireMock.aResponse()
				.withStatus(status)
				.withHeader("Content-Type", contentType)));

		final HttpResponse rsp = createClient(server)
			.prepareDelete("/people/1")
			.acceptJson()
			.asJson()
			.asXmlHttpRequest()
			.execute();

		assertThat(rsp.status()).isEqualTo(status);
		assertThat(rsp.body()).isEmpty();
		assertThat(rsp.getContentType().getFirstValue()).isEqualTo(contentType);
	}

	protected abstract HttpClient createClient(EmbeddedServer server);
}
