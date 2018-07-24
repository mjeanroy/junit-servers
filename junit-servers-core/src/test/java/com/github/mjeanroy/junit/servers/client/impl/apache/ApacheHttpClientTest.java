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

package com.github.mjeanroy.junit.servers.client.impl.apache;

import com.github.mjeanroy.junit.servers.client.HttpClient;
import com.github.mjeanroy.junit.servers.client.HttpClientConfiguration;
import com.github.mjeanroy.junit.servers.client.impl.BaseHttpClientTest;
import com.github.mjeanroy.junit.servers.servers.EmbeddedServer;
import org.apache.http.impl.client.CloseableHttpClient;

import static com.github.mjeanroy.junit.servers.client.impl.apache.ApacheHttpClient.defaultApacheHttpClient;
import static com.github.mjeanroy.junit.servers.client.impl.apache.ApacheHttpClient.newApacheHttpClient;
import static com.github.mjeanroy.junit.servers.utils.commons.Fields.readPrivate;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class ApacheHttpClientTest extends BaseHttpClientTest {

	private CloseableHttpClient internalClient;

	@Override
	protected void onSetUp() {
		internalClient = mock(CloseableHttpClient.class);
	}

	@Override
	protected HttpClient createDefaultClient(EmbeddedServer<?> server) {
		return defaultApacheHttpClient(server);
	}

	@Override
	@SuppressWarnings("deprecation")
	protected HttpClient createCustomClient(EmbeddedServer<?> server) {
		return newApacheHttpClient(server, internalClient);
	}

	@Override
	protected HttpClient createCustomClient(HttpClientConfiguration configuration, EmbeddedServer<?> server) {
		return newApacheHttpClient(configuration, server);
	}

	@Override
	protected void checkInternalHttpClient(HttpClientConfiguration configuration, HttpClient httpClient) {
	}

	@Override
	protected void checkInternalHttpClient(HttpClient httpClient) {
		CloseableHttpClient internalClient = readPrivate(httpClient, "client");
		assertThat(internalClient).isNotNull();
	}
}
