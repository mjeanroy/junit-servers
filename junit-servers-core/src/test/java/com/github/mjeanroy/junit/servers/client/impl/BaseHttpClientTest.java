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

package com.github.mjeanroy.junit.servers.client.impl;

import com.github.mjeanroy.junit.servers.client.HttpClient;
import com.github.mjeanroy.junit.servers.client.HttpMethod;
import com.github.mjeanroy.junit.servers.client.HttpRequest;
import com.github.mjeanroy.junit.servers.servers.EmbeddedServer;
import com.github.mjeanroy.junit.servers.utils.junit.run_if.RunIfRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.apache.commons.lang3.reflect.FieldUtils.readField;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests skeleton for http client implementations.
 */
@RunWith(RunIfRunner.class)
public abstract class BaseHttpClientTest {

	protected EmbeddedServer server;

	@Before
	public void setUp() throws Exception {
		server = mock(EmbeddedServer.class);

		when(server.getUrl()).thenReturn("http://localhost:8080/path");
		when(server.getPort()).thenReturn(8080);
		when(server.getPath()).thenReturn("/path");

		onSetUp();
	}

	@Test
	public void it_should_create_default_client() throws Exception {
		HttpClient client = createDefaultClient(server);

		assertThat(client)
				.isNotNull();

		EmbeddedServer internalServer = (EmbeddedServer) readField(client, "server", true);
		assertThat(internalServer)
				.isNotNull()
				.isSameAs(server);

		checkInternalHttpClient(client);
	}

	@Test
	public void it_should_create_custom_client() throws Exception {
		HttpClient client = createCustomClient(server);
		assertThat(client).isNotNull();

		EmbeddedServer internalServer = (EmbeddedServer) readField(client, "server", true);
		assertThat(internalServer).isNotNull().isSameAs(server);

		checkInternalHttpClient(client);
	}

	@Test
	public void it_should_create_request() throws Exception {
		HttpClient client = createCustomClient(server);

		String path = "/foo";
		HttpMethod httpMethod = HttpMethod.POST;

		HttpRequest httpRequest = client.prepareRequest(httpMethod, path);
		assertThat(httpRequest.getUrl()).startsWith(server.getUrl() + path);
		assertThat(httpRequest.getMethod()).isEqualTo(httpMethod);
	}

	@Test
	public void it_should_create_request_and_do_not_prepend_server_path() throws Exception {
		HttpClient client = createCustomClient(server);

		String path = "/foo";
		HttpMethod httpMethod = HttpMethod.POST;

		HttpRequest httpRequest = client.prepareRequest(httpMethod, server.getPath() + path);
		assertThat(httpRequest.getUrl()).startsWith(server.getUrl() + path);
		assertThat(httpRequest.getMethod()).isEqualTo(httpMethod);
	}

	@Test
	public void it_should_create_request_and_do_not_prepend_server_url() throws Exception {
		HttpClient client = createCustomClient(server);

		String path = "/foo";
		HttpMethod httpMethod = HttpMethod.POST;

		HttpRequest httpRequest = client.prepareRequest(httpMethod, server.getUrl() + path);
		assertThat(httpRequest.getUrl()).startsWith(server.getUrl() + path);
		assertThat(httpRequest.getMethod()).isEqualTo(httpMethod);
	}

	@Test
	public void it_should_create_request_and_set_path_separator() throws Exception {
		final String serverUrl = "http://localhost:9999";
		when(server.getUrl()).thenReturn(serverUrl);
		when(server.getPath()).thenReturn("/");

		final HttpClient client = createCustomClient(server);
		final String path = "/foo";
		final HttpMethod httpMethod = HttpMethod.GET;

		HttpRequest httpRequest = client.prepareRequest(httpMethod, path);
		assertThat(httpRequest.getUrl()).isEqualTo(serverUrl + path);
	}

	@Test
	public void it_should_create_get_request() throws Exception {
		HttpClient client = createCustomClient(server);
		HttpRequest httpRequest = client.prepareGet("/foo");
		assertThat(httpRequest.getMethod()).isEqualTo(HttpMethod.GET);
	}

	@Test
	public void it_should_create_post_request() throws Exception {
		HttpClient client = createCustomClient(server);
		HttpRequest httpRequest = client.preparePost("/foo");
		assertThat(httpRequest.getMethod()).isEqualTo(HttpMethod.POST);
	}

	@Test
	public void it_should_create_put_request() throws Exception {
		HttpClient client = createCustomClient(server);
		HttpRequest httpRequest = client.preparePut("/foo");
		assertThat(httpRequest.getMethod()).isEqualTo(HttpMethod.PUT);
	}

	@Test
	public void it_should_create_delete_request() throws Exception {
		HttpClient client = createCustomClient(server);
		HttpRequest httpRequest = client.prepareDelete("/foo");
		assertThat(httpRequest.getMethod()).isEqualTo(HttpMethod.DELETE);
	}

	/**
	 * Should create mock data during test setup.
	 *
	 * @throws Exception If an error occurred.
	 */
	protected abstract void onSetUp() throws Exception;

	/**
	 * Should create a default http client.
	 *
	 * @param server Fake embedded server.
	 * @return Default http client.
	 * @throws Exception If an error occurred while creating client.
	 */
	protected abstract HttpClient createDefaultClient(EmbeddedServer server) throws Exception;

	/**
	 * Should create a custom http client.
	 *
	 * @param server Fake embedded server.
	 * @return Default http client.
	 * @throws Exception If an error occurred while creating custom client.
	 */
	protected abstract HttpClient createCustomClient(EmbeddedServer server) throws Exception;

	/**
	 * Should check internal data of previously created custom
	 * http client.
	 *
	 * @param httpClient Previously created client.
	 * @throws Exception If an error occurred.
	 */
	protected abstract void checkInternalHttpClient(HttpClient httpClient) throws Exception;
}
