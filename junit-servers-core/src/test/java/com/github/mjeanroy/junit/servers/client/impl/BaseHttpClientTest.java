/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2019 <mickael.jeanroy@gmail.com>
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

	@BeforeEach
	void setUp() {
		scheme = "http";
		host = "localhost";
		port = 8080;
		path = "/path";
		server = new EmbeddedServerMockBuilder().withScheme(scheme).withHost(host).withPort(port).withPath(path).build();
	}

	@Test
	void it_should_create_default_client() {
		final HttpClient client = createDefaultClient(server);
		assertThat(client).isNotNull();

		final EmbeddedServer<?> internalServer = readPrivate(client, "server");
		assertThat(internalServer).isSameAs(server);

		checkInternalHttpClient(client);
	}

	@Test
	void it_should_create_client_with_custom_configuration() {
		final HttpClientConfiguration configuration = new HttpClientConfiguration.Builder()
			.disableFollowRedirect()
			.build();

		final HttpClient client = createCustomClient(configuration, server);
		assertThat(client).isNotNull();

		final EmbeddedServer<?> server = readPrivate(client, "server");
		assertThat(server).isNotNull().isSameAs(this.server);
		checkInternalHttpClient(configuration, client);
	}

	@Test
	void it_should_create_request() {
		final HttpClient client = createDefaultClient(server);
		final String endpoint = "/foo";
		final HttpMethod httpMethod = HttpMethod.POST;

		final HttpRequest httpRequest = client.prepareRequest(httpMethod, endpoint);

		assertThat(httpRequest.getMethod()).isEqualTo(httpMethod);
		assertThat(httpRequest.getEndpoint()).isNotNull();
		assertThat(httpRequest.getEndpoint().getScheme()).isEqualTo(scheme);
		assertThat(httpRequest.getEndpoint().getHost()).isEqualTo(host);
		assertThat(httpRequest.getEndpoint().getPort()).isEqualTo(port);
		assertThat(httpRequest.getEndpoint().getPath()).isEqualTo(path + endpoint);
		assertThat(httpRequest.getEndpoint().toString()).isEqualTo(url(scheme, host, port, path + endpoint));
	}

	@Test
	void it_should_create_request_and_do_not_prepend_server_path() {
		final HttpClient client = createDefaultClient(server);
		final String endpoint = "/foo";
		final String fullPath = server.getPath() + endpoint;
		final HttpMethod httpMethod = HttpMethod.POST;

		final HttpRequest httpRequest = client.prepareRequest(httpMethod, fullPath);

		assertThat(httpRequest.getMethod()).isEqualTo(httpMethod);
		assertThat(httpRequest.getEndpoint()).isNotNull();
		assertThat(httpRequest.getEndpoint().getScheme()).isEqualTo(scheme);
		assertThat(httpRequest.getEndpoint().getHost()).isEqualTo(host);
		assertThat(httpRequest.getEndpoint().getPort()).isEqualTo(port);
		assertThat(httpRequest.getEndpoint().getPath()).isEqualTo(fullPath);
		assertThat(httpRequest.getEndpoint().toString()).isEqualTo(url(scheme, host, port, fullPath));
	}

	@Test
	void it_should_create_request_and_do_not_prepend_server_url() {
		final HttpClient client = createDefaultClient(server);
		final String endpoint = "/foo";
		final String absoluteUrl = server.getUrl() + endpoint;
		final HttpMethod httpMethod = HttpMethod.POST;

		final HttpRequest httpRequest = client.prepareRequest(httpMethod, absoluteUrl);

		assertThat(httpRequest.getMethod()).isEqualTo(httpMethod);

		assertThat(httpRequest.getEndpoint()).isNotNull();
		assertThat(httpRequest.getEndpoint().getScheme()).isEqualTo(scheme);
		assertThat(httpRequest.getEndpoint().getHost()).isEqualTo(host);
		assertThat(httpRequest.getEndpoint().getPort()).isEqualTo(port);
		assertThat(httpRequest.getEndpoint().getPath()).isEqualTo(path + endpoint);
		assertThat(httpRequest.getEndpoint().toString()).isEqualTo(url(scheme, host, port, path + endpoint));
	}

	@Test
	void it_should_create_request_and_set_path_separator() {
		final String serverUrl = url(scheme, host, port, "");

		when(server.getUrl()).thenReturn(serverUrl);
		when(server.getPath()).thenReturn("/");

		final HttpClient client = createDefaultClient(server);
		final String path = "/foo";
		final HttpMethod httpMethod = HttpMethod.GET;

		final HttpRequest httpRequest = client.prepareRequest(httpMethod, path);

		assertThat(httpRequest.getEndpoint()).isNotNull();
		assertThat(httpRequest.getEndpoint().getScheme()).isEqualTo(scheme);
		assertThat(httpRequest.getEndpoint().getHost()).isEqualTo(host);
		assertThat(httpRequest.getEndpoint().getPort()).isEqualTo(port);
		assertThat(httpRequest.getEndpoint().getPath()).isEqualTo(path);
		assertThat(httpRequest.getEndpoint().toString()).isEqualTo(url(scheme, host, port, path));
	}

	@Test
	void it_should_create_request_with_root_path() {
		final String serverUrl = url(scheme, host, 8080, "");
		final String path = "/";

		when(server.getUrl()).thenReturn(serverUrl);
		when(server.getPath()).thenReturn(path);

		final HttpClient client = createDefaultClient(server);
		final HttpMethod httpMethod = HttpMethod.GET;

		final HttpRequest httpRequest = client.prepareRequest(httpMethod, path);

		assertThat(httpRequest.getEndpoint()).isNotNull();
		assertThat(httpRequest.getEndpoint().getScheme()).isEqualTo(scheme);
		assertThat(httpRequest.getEndpoint().getHost()).isEqualTo(host);
		assertThat(httpRequest.getEndpoint().getPort()).isEqualTo(port);
		assertThat(httpRequest.getEndpoint().getPath()).isEqualTo(path);
		assertThat(httpRequest.getEndpoint().toString()).isEqualTo(localUrl(port));
	}

	@Test
	void it_should_create_get_request() {
		final HttpClient client = createDefaultClient(server);
		final HttpRequest httpRequest = client.prepareGet("/foo");
		assertThat(httpRequest.getMethod()).isEqualTo(HttpMethod.GET);
	}

	@Test
	void it_should_create_post_request() {
		final HttpClient client = createDefaultClient(server);
		final HttpRequest httpRequest = client.preparePost("/foo");
		assertThat(httpRequest.getMethod()).isEqualTo(HttpMethod.POST);
	}

	@Test
	void it_should_create_put_request() {
		final HttpClient client = createDefaultClient(server);
		final HttpRequest httpRequest = client.preparePut("/foo");
		assertThat(httpRequest.getMethod()).isEqualTo(HttpMethod.PUT);
	}

	@Test
	void it_should_create_delete_request() {
		final HttpClient client = createDefaultClient(server);
		final HttpRequest httpRequest = client.prepareDelete("/foo");
		assertThat(httpRequest.getMethod()).isEqualTo(HttpMethod.DELETE);
	}

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
