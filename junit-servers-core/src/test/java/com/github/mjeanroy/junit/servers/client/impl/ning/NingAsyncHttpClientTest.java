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

package com.github.mjeanroy.junit.servers.client.impl.ning;

import com.github.mjeanroy.junit.servers.client.HttpClient;
import com.github.mjeanroy.junit.servers.client.HttpClientConfiguration;
import com.github.mjeanroy.junit.servers.client.impl.BaseHttpClientTest;
import com.github.mjeanroy.junit.servers.servers.EmbeddedServer;
import com.ning.http.client.AsyncHttpClientConfig;

import static com.github.mjeanroy.junit.servers.client.impl.ning.NingAsyncHttpClient.defaultAsyncHttpClient;
import static com.github.mjeanroy.junit.servers.client.impl.ning.NingAsyncHttpClient.newAsyncHttpClient;
import static com.github.mjeanroy.junit.servers.utils.commons.Fields.readPrivate;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class NingAsyncHttpClientTest extends BaseHttpClientTest {

	private com.ning.http.client.AsyncHttpClient internalClient;

	@Override
	protected void onSetUp() {
		internalClient = mock(com.ning.http.client.AsyncHttpClient.class);
	}

	@Override
	protected HttpClient createDefaultClient(EmbeddedServer<?> server) {
		return defaultAsyncHttpClient(server);
	}

	@Override
	@SuppressWarnings("deprecation")
	protected HttpClient createCustomClient(EmbeddedServer<?> server) {
		return newAsyncHttpClient(server, internalClient);
	}

	@Override
	protected HttpClient createCustomClient(HttpClientConfiguration configuration, EmbeddedServer<?> server) {
		return newAsyncHttpClient(configuration, server);
	}

	@Override
	protected void checkInternalHttpClient(HttpClientConfiguration configuration, HttpClient httpClient) {
		com.ning.http.client.AsyncHttpClient internalClient = readPrivate(httpClient, "client");
		AsyncHttpClientConfig config = internalClient.getConfig();
		assertThat(config.isFollowRedirect()).isEqualTo(configuration.isFollowRedirect());
	}

	@Override
	protected void checkInternalHttpClient(HttpClient httpClient) {
		com.ning.http.client.AsyncHttpClient internalClient = readPrivate(httpClient, "client");
		assertThat(internalClient).isNotNull();
	}
}
