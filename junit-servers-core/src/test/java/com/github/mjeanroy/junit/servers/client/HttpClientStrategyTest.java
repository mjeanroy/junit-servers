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

package com.github.mjeanroy.junit.servers.client;

import com.github.mjeanroy.junit.servers.client.impl.apache_http_client.ApacheHttpClient;
import com.github.mjeanroy.junit.servers.client.impl.async_http_client.AsyncHttpClient;
import com.github.mjeanroy.junit.servers.client.impl.ning_async_http_client.NingAsyncHttpClient;
import com.github.mjeanroy.junit.servers.servers.EmbeddedServer;
import com.github.mjeanroy.junit.servers.utils.Fields;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class HttpClientStrategyTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private EmbeddedServer server;

	@Before
	public void setUp() {
		server = mock(EmbeddedServer.class);
	}

	@After
	public void tearDown() {
		Fields.writeStaticFinal(HttpClientStrategy.class, "SUPPORT_ASYNC_HTTP_CLIENT", true);
		Fields.writeStaticFinal(HttpClientStrategy.class, "SUPPORT_NING_ASYNC_HTTP_CLIENT", true);
		Fields.writeStaticFinal(HttpClientStrategy.class, "SUPPORT_APACHE_HTTP_CLIENT", true);
	}

	@Test
	public void it_should_create_apache_http_client() {
		HttpClient client = HttpClientStrategy.APACHE_HTTP_CLIENT.build(server);
		assertThat(client)
			.isNotNull()
			.isExactlyInstanceOf(ApacheHttpClient.class);
	}

	@Test
	public void it_should_create_ning_async_http_client() {
		HttpClient client = HttpClientStrategy.NING_ASYNC_HTTP_CLIENT.build(server);
		assertThat(client)
			.isNotNull()
			.isExactlyInstanceOf(NingAsyncHttpClient.class);
	}

	@Test
	public void it_should_create_async_http_client() {
		HttpClient client = HttpClientStrategy.ASYNC_HTTP_CLIENT.build(server);
		assertThat(client)
			.isNotNull()
			.isExactlyInstanceOf(AsyncHttpClient.class);
	}

	@Test
	public void it_should_create_automatic_http_client_with_async_http_client() {
		Fields.writeStaticFinal(HttpClientStrategy.class, "SUPPORT_ASYNC_HTTP_CLIENT", true);
		Fields.writeStaticFinal(HttpClientStrategy.class, "SUPPORT_NING_ASYNC_HTTP_CLIENT", true);
		Fields.writeStaticFinal(HttpClientStrategy.class, "SUPPORT_APACHE_HTTP_CLIENT", true);

		HttpClient client = HttpClientStrategy.AUTO.build(server);
		assertThat(client)
			.isNotNull()
			.isExactlyInstanceOf(AsyncHttpClient.class);
	}

	@Test
	public void it_should_create_automatic_http_client_with_ning_async_http_client() {
		Fields.writeStaticFinal(HttpClientStrategy.class, "SUPPORT_ASYNC_HTTP_CLIENT", false);
		Fields.writeStaticFinal(HttpClientStrategy.class, "SUPPORT_NING_ASYNC_HTTP_CLIENT", true);
		Fields.writeStaticFinal(HttpClientStrategy.class, "SUPPORT_APACHE_HTTP_CLIENT", true);

		HttpClient client = HttpClientStrategy.AUTO.build(server);
		assertThat(client)
			.isNotNull()
			.isExactlyInstanceOf(NingAsyncHttpClient.class);
	}

	@Test
	public void it_should_create_automatic_http_client_with_apache_async_http_client() {
		Fields.writeStaticFinal(HttpClientStrategy.class, "SUPPORT_ASYNC_HTTP_CLIENT", false);
		Fields.writeStaticFinal(HttpClientStrategy.class, "SUPPORT_NING_ASYNC_HTTP_CLIENT", false);
		Fields.writeStaticFinal(HttpClientStrategy.class, "SUPPORT_APACHE_HTTP_CLIENT", true);

		HttpClient client = HttpClientStrategy.AUTO.build(server);
		assertThat(client)
			.isNotNull()
			.isExactlyInstanceOf(ApacheHttpClient.class);
	}

	@Test
	public void it_should_not_create_automatic_http_client_and_fail_without_implementation() {
		Fields.writeStaticFinal(HttpClientStrategy.class, "SUPPORT_ASYNC_HTTP_CLIENT", false);
		Fields.writeStaticFinal(HttpClientStrategy.class, "SUPPORT_NING_ASYNC_HTTP_CLIENT", false);
		Fields.writeStaticFinal(HttpClientStrategy.class, "SUPPORT_APACHE_HTTP_CLIENT", false);

		thrown.expect(UnsupportedOperationException.class);
		thrown.expectMessage("Http client implementation cannot be found, please add AsyncHttpClient or ApacheHttpClient to your classpath");

		HttpClientStrategy.AUTO.build(server);
	}
}
