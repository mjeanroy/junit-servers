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

package com.github.mjeanroy.junit.servers.client.apache_http_client;

import com.github.mjeanroy.junit.servers.client.BaseHttpClientTest;
import com.github.mjeanroy.junit.servers.client.HttpClient;
import com.github.mjeanroy.junit.servers.client.HttpMethod;
import com.github.mjeanroy.junit.servers.client.HttpRequest;
import com.github.mjeanroy.junit.servers.servers.EmbeddedServer;
import org.apache.http.impl.client.CloseableHttpClient;

import java.util.Map;

import static com.github.mjeanroy.junit.servers.client.apache_http_client.ApacheHttpClient.defaultApacheHttpClient;
import static com.github.mjeanroy.junit.servers.client.apache_http_client.ApacheHttpClient.newApacheHttpClient;
import static java.lang.String.format;
import static org.apache.commons.lang3.reflect.FieldUtils.readField;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ApacheHttpClientTest extends BaseHttpClientTest {

	private CloseableHttpClient internalClient;

	@Override
	protected void onSetUp() throws Exception {
		internalClient = mock(CloseableHttpClient.class);
	}

	@Override
	protected HttpClient createDefaultClient(EmbeddedServer server) throws Exception {
		return defaultApacheHttpClient(server);
	}

	@Override
	protected HttpClient createCustomClient(EmbeddedServer server) throws Exception {
		return newApacheHttpClient(server, internalClient);
	}

	@Override
	protected void checkInternalHttpClient(HttpClient httpClient) throws Exception {
		CloseableHttpClient internalClient = (CloseableHttpClient) readField(httpClient, "client", true);
		assertThat(internalClient).isNotNull();
	}

	@Override
	protected void checkHttpRequest(HttpRequest httpRequest, HttpMethod httpMethod, String path) throws Exception {
		assertThat(httpRequest)
				.isNotNull()
				.isExactlyInstanceOf(ApacheHttpRequest.class);

		CloseableHttpClient internalClient = (CloseableHttpClient) readField(httpRequest, "client", true);
		assertThat(internalClient)
				.isNotNull()
				.isSameAs(this.internalClient);

		HttpMethod method = (HttpMethod) readField(httpRequest, "httpMethod", true);
		assertThat(method)
				.isNotNull()
				.isEqualTo(httpMethod);

		String url = (String) readField(httpRequest, "url", true);
		assertThat(url)
				.isNotNull()
				.isNotEmpty()
				.isEqualTo(format("http://localhost:8080/path%s", path));

		@SuppressWarnings("unchecked")
		Map<String, String> queryParams = (Map<String, String>) readField(httpRequest, "queryParams", true);
		assertThat(queryParams)
				.isNotNull()
				.isEmpty();

		@SuppressWarnings("unchecked")
		Map<String, String> headers = (Map<String, String>) readField(httpRequest, "headers", true);
		assertThat(headers)
				.isNotNull()
				.isEmpty();
	}

	@Override
	protected void checkDestroy(HttpClient client) throws Exception {
		verify(internalClient).close();
	}
}
