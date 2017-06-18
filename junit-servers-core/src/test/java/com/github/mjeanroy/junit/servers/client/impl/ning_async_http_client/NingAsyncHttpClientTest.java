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

package com.github.mjeanroy.junit.servers.client.impl.ning_async_http_client;

import com.github.mjeanroy.junit.servers.client.BaseHttpClientTest;
import com.github.mjeanroy.junit.servers.client.HttpClient;
import com.github.mjeanroy.junit.servers.client.HttpMethod;
import com.github.mjeanroy.junit.servers.client.HttpRequest;
import com.github.mjeanroy.junit.servers.servers.EmbeddedServer;
import com.ning.http.client.Request;
import com.ning.http.client.RequestBuilder;

import static com.github.mjeanroy.junit.servers.client.impl.ning_async_http_client.NingAsyncHttpClient.defaultAsyncHttpClient;
import static com.github.mjeanroy.junit.servers.client.impl.ning_async_http_client.NingAsyncHttpClient.newAsyncHttpClient;
import static java.lang.String.format;
import static org.apache.commons.lang3.reflect.FieldUtils.readField;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class NingAsyncHttpClientTest extends BaseHttpClientTest {

	private com.ning.http.client.AsyncHttpClient internalClient;

	@Override
	protected void onSetUp() throws Exception {
		internalClient = mock(com.ning.http.client.AsyncHttpClient.class);
	}

	@Override
	protected HttpClient createDefaultClient(EmbeddedServer server) throws Exception {
		return defaultAsyncHttpClient(server);
	}

	@Override
	protected HttpClient createCustomClient(EmbeddedServer server) throws Exception {
		return newAsyncHttpClient(server, internalClient);
	}

	@Override
	protected void checkInternalHttpClient(HttpClient httpClient) throws Exception {
		com.ning.http.client.AsyncHttpClient internalClient = (com.ning.http.client.AsyncHttpClient) readField(httpClient, "client", true);
		assertThat(internalClient).isNotNull();
	}

	@Override
	protected void checkHttpRequest(HttpRequest httpRequest, HttpMethod httpMethod, String path) throws Exception {
		assertThat(httpRequest)
				.isNotNull()
				.isExactlyInstanceOf(NingAsyncHttpRequest.class);

		com.ning.http.client.AsyncHttpClient internalClient = (com.ning.http.client.AsyncHttpClient) readField(httpRequest, "client", true);
		assertThat(internalClient)
				.isNotNull()
				.isSameAs(this.internalClient);

		RequestBuilder requestBuilder = (RequestBuilder) readField(httpRequest, "builder", true);
		assertThat(requestBuilder).isNotNull();

		// Build request to test initialization values

		Request request = requestBuilder.build();
		assertThat(request.getMethod())
				.isNotNull()
				.isEqualTo(httpMethod.getVerb());

		assertThat(request.getUrl())
				.isNotNull()
				.isEqualTo(format("http://localhost:8080/path%s", path));
	}

	@Override
	protected void checkDestroy(HttpClient client) throws Exception {
		verify(internalClient).close();
	}
}
