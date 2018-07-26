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

package com.github.mjeanroy.junit.servers.client.impl;

import com.github.mjeanroy.junit.servers.client.HttpClient;
import com.github.mjeanroy.junit.servers.client.HttpClientConfiguration;
import com.github.mjeanroy.junit.servers.client.HttpMethod;
import com.github.mjeanroy.junit.servers.client.HttpRequest;
import com.github.mjeanroy.junit.servers.servers.EmbeddedServer;
import com.github.mjeanroy.junit.servers.utils.builders.EmbeddedServerMockBuilder;
import org.junit.Before;
import org.junit.Test;

import static com.github.mjeanroy.junit.servers.utils.commons.Fields.readPrivate;
import static com.github.mjeanroy.junit.servers.utils.commons.TestUtils.localUrl;
import static com.github.mjeanroy.junit.servers.utils.commons.TestUtils.url;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Tests skeleton for http client implementations.
 */
public abstract class BaseHttpClientTest {

	private EmbeddedServer<?> server;
	private String scheme;
	private String host;
	private int port;
	private String path;

	@Before
	public void setUp() {
		scheme = "http";
		host = "localhost";
		port = 8080;
		path = "/path";
		server = new EmbeddedServerMockBuilder()
				.withScheme(scheme)
				.withHost(host)
				.withPort(port)
				.withPath(path)
				.build();

		onSetUp();
	}

	@Test
	public void it_should_create_default_client() {
		HttpClient client = createDefaultClient(server);
		assertThat(client).isNotNull();

		EmbeddedServer<?> internalServer = readPrivate(client, "server");
		assertThat(internalServer).isSameAs(server);

		checkInternalHttpClient(client);
	}

	@Test
	public void it_should_create_custom_client() {
		HttpClient client = createCustomClient(server);
		assertThat(client).isNotNull();

		EmbeddedServer<?> internalServer = readPrivate(client, "server");
		assertThat(internalServer).isNotNull().isSameAs(server);

		checkInternalHttpClient(client);
	}

	@Test
	public void it_should_create_client_with_custom_configuration() {
		HttpClientConfiguration configuration = new HttpClientConfiguration.Builder()
			.disableFollowRedirect()
			.build();

		HttpClient client = createCustomClient(configuration, server);

		assertThat(client).isNotNull();
		assertThat(readPrivate(client, "server")).isNotNull().isSameAs(server);
		checkInternalHttpClient(configuration, client);
	}

	@Test
	public void it_should_create_request() {
		HttpClient client = createCustomClient(server);

		String endpoint = "/foo";
		HttpMethod httpMethod = HttpMethod.POST;

		HttpRequest httpRequest = client.prepareRequest(httpMethod, endpoint);

		assertThat(httpRequest.getMethod()).isEqualTo(httpMethod);
		assertThat(httpRequest.getEndpoint()).isNotNull();
		assertThat(httpRequest.getEndpoint().getScheme()).isEqualTo(scheme);
		assertThat(httpRequest.getEndpoint().getHost()).isEqualTo(host);
		assertThat(httpRequest.getEndpoint().getPort()).isEqualTo(port);
		assertThat(httpRequest.getEndpoint().getPath()).isEqualTo(path + endpoint);
		assertThat(httpRequest.getEndpoint().toString()).isEqualTo(url(scheme, host, port, path + endpoint));
	}

	@Test
	public void it_should_create_request_and_do_not_prepend_server_path() {
		HttpClient client = createCustomClient(server);

		String endpoint = "/foo";
		String fullPath = server.getPath() + endpoint;
		HttpMethod httpMethod = HttpMethod.POST;

		HttpRequest httpRequest = client.prepareRequest(httpMethod, fullPath);

		assertThat(httpRequest.getMethod()).isEqualTo(httpMethod);
		assertThat(httpRequest.getEndpoint()).isNotNull();
		assertThat(httpRequest.getEndpoint().getScheme()).isEqualTo(scheme);
		assertThat(httpRequest.getEndpoint().getHost()).isEqualTo(host);
		assertThat(httpRequest.getEndpoint().getPort()).isEqualTo(port);
		assertThat(httpRequest.getEndpoint().getPath()).isEqualTo(fullPath);
		assertThat(httpRequest.getEndpoint().toString()).isEqualTo(url(scheme, host, port, fullPath));
	}

	@Test
	public void it_should_create_request_and_do_not_prepend_server_url() {
		HttpClient client = createCustomClient(server);

		String endpoint = "/foo";
		String absoluteUrl = server.getUrl() + endpoint;
		HttpMethod httpMethod = HttpMethod.POST;

		HttpRequest httpRequest = client.prepareRequest(httpMethod, absoluteUrl);

		assertThat(httpRequest.getMethod()).isEqualTo(httpMethod);

		assertThat(httpRequest.getEndpoint()).isNotNull();
		assertThat(httpRequest.getEndpoint().getScheme()).isEqualTo(scheme);
		assertThat(httpRequest.getEndpoint().getHost()).isEqualTo(host);
		assertThat(httpRequest.getEndpoint().getPort()).isEqualTo(port);
		assertThat(httpRequest.getEndpoint().getPath()).isEqualTo(path + endpoint);
		assertThat(httpRequest.getEndpoint().toString()).isEqualTo(url(scheme, host, port, path + endpoint));
	}

	@Test
	public void it_should_create_request_and_set_path_separator() {
		final String serverUrl = url(scheme, host, port, "");
		when(server.getUrl()).thenReturn(serverUrl);
		when(server.getPath()).thenReturn("/");

		final HttpClient client = createCustomClient(server);
		final String path = "/foo";
		final HttpMethod httpMethod = HttpMethod.GET;

		HttpRequest httpRequest = client.prepareRequest(httpMethod, path);

		assertThat(httpRequest.getEndpoint()).isNotNull();
		assertThat(httpRequest.getEndpoint().getScheme()).isEqualTo(scheme);
		assertThat(httpRequest.getEndpoint().getHost()).isEqualTo(host);
		assertThat(httpRequest.getEndpoint().getPort()).isEqualTo(port);
		assertThat(httpRequest.getEndpoint().getPath()).isEqualTo(path);
		assertThat(httpRequest.getEndpoint().toString()).isEqualTo(url(scheme, host, port, path));
	}

	@Test
	public void it_should_create_request_with_root_path() {
		final String serverUrl = url(scheme, host, 8080, "");
		final String path = "/";
		when(server.getUrl()).thenReturn(serverUrl);
		when(server.getPath()).thenReturn(path);

		final HttpClient client = createCustomClient(server);
		final HttpMethod httpMethod = HttpMethod.GET;

		HttpRequest httpRequest = client.prepareRequest(httpMethod, path);

		assertThat(httpRequest.getEndpoint()).isNotNull();
		assertThat(httpRequest.getEndpoint().getScheme()).isEqualTo(scheme);
		assertThat(httpRequest.getEndpoint().getHost()).isEqualTo(host);
		assertThat(httpRequest.getEndpoint().getPort()).isEqualTo(port);
		assertThat(httpRequest.getEndpoint().getPath()).isEqualTo(path);
		assertThat(httpRequest.getEndpoint().toString()).isEqualTo(localUrl(port));
	}

	@Test
	public void it_should_create_get_request() {
		HttpClient client = createCustomClient(server);
		HttpRequest httpRequest = client.prepareGet("/foo");
		assertThat(httpRequest.getMethod()).isEqualTo(HttpMethod.GET);
	}

	@Test
	public void it_should_create_post_request() {
		HttpClient client = createCustomClient(server);
		HttpRequest httpRequest = client.preparePost("/foo");
		assertThat(httpRequest.getMethod()).isEqualTo(HttpMethod.POST);
	}

	@Test
	public void it_should_create_put_request() {
		HttpClient client = createCustomClient(server);
		HttpRequest httpRequest = client.preparePut("/foo");
		assertThat(httpRequest.getMethod()).isEqualTo(HttpMethod.PUT);
	}

	@Test
	public void it_should_create_delete_request() {
		HttpClient client = createCustomClient(server);
		HttpRequest httpRequest = client.prepareDelete("/foo");
		assertThat(httpRequest.getMethod()).isEqualTo(HttpMethod.DELETE);
	}

	/**
	 * Should create mock data during test setup.
	 */
	protected abstract void onSetUp();

	/**
	 * Should create a default http client.
	 *
	 * @param server Fake embedded server.
	 * @return Default http client.
	 */
	protected abstract HttpClient createDefaultClient(EmbeddedServer<?> server);

	/**
	 * Should create a custom http client.
	 *
	 * @param server Fake embedded server.
	 * @return Default http client.
	 */
	protected abstract HttpClient createCustomClient(EmbeddedServer<?> server);

	/**
	 * Should create a custom http client.
	 *
	 * @param configuration The HTTP client custom configuration.
	 * @param server Fake embedded server.
	 * @return Default http client.
	 */
	protected abstract HttpClient createCustomClient(HttpClientConfiguration configuration, EmbeddedServer<?> server);

	/**
	 * Should check internal data of previously created custom
	 * http client.
	 *
	 * @param httpClient Previously created client.
	 */
	protected abstract void checkInternalHttpClient(HttpClient httpClient);

	/**
	 * Should check internal data of previously created custom
	 * http client.
	 *
	 * @param configuration The configuration used to create client.
	 * @param httpClient Previously created client.
	 */
	protected abstract void checkInternalHttpClient(HttpClientConfiguration configuration, HttpClient httpClient);
}
