/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2023 <mickael.jeanroy@gmail.com>
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

import com.github.mjeanroy.junit.servers.client.impl.apache.ApacheHttpClient;
import com.github.mjeanroy.junit.servers.client.impl.async.AsyncHttpClient;
import com.github.mjeanroy.junit.servers.client.impl.ning.NingAsyncHttpClient;
import com.github.mjeanroy.junit.servers.client.impl.okhttp3.OkHttpClient;
import com.github.mjeanroy.junit.servers.servers.EmbeddedServer;
import com.github.mjeanroy.junit.servers.utils.builders.EmbeddedServerMockBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HttpClientStrategyTest {

	private EmbeddedServer<?> server;

	@BeforeEach
	void setUp() {
		server = new EmbeddedServerMockBuilder().build();
	}

	@Test
	void it_should_create_apache_http_client() {
		testHttpClient(HttpClientStrategy.APACHE_HTTP_CLIENT, ApacheHttpClient.class);
	}

	@Test
	void it_should_create_async_http_client() {
		testHttpClient(HttpClientStrategy.ASYNC_HTTP_CLIENT, AsyncHttpClient.class);
	}

	@Test
	void it_should_create_ning_async_http_client() {
		testHttpClient(HttpClientStrategy.NING_ASYNC_HTTP_CLIENT, NingAsyncHttpClient.class);
	}

	@Test
	void it_should_create_ok_http_client() {
		testHttpClient(HttpClientStrategy.OK_HTTP3, OkHttpClient.class);
	}

	private void testHttpClient(HttpClientStrategy strategy, Class<?> expectedImpl) {
		assertThat(strategy.support()).isTrue();

		// --- With default configuration.

		HttpClient c1 = strategy.build(server);
		assertThat(c1).isExactlyInstanceOf(expectedImpl);

		// --- With custom configuration.

		HttpClientConfiguration configuration = new HttpClientConfiguration.Builder()
			.disableFollowRedirect()
			.build();

		HttpClient c2 = strategy.build(configuration, server);

		assertThat(c2).isExactlyInstanceOf(expectedImpl);
		assertThat(c2.getConfiguration()).isSameAs(configuration);
	}
}
