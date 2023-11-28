/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2022 <mickael.jeanroy@gmail.com>
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

package com.github.mjeanroy.junit.servers.client.impl.okhttp3;

import com.github.mjeanroy.junit.servers.client.HttpClient;
import com.github.mjeanroy.junit.servers.client.HttpClientConfiguration;
import com.github.mjeanroy.junit.servers.client.impl.BaseHttpClientTest;
import com.github.mjeanroy.junit.servers.servers.EmbeddedServer;
import com.github.mjeanroy.junit.servers.utils.builders.EmbeddedServerMockBuilder;
import org.junit.jupiter.api.Test;

import static com.github.mjeanroy.junit.servers.client.impl.okhttp3.OkHttpClient.defaultOkHttpClient;
import static com.github.mjeanroy.junit.servers.client.impl.okhttp3.OkHttpClient.newOkHttpClient;
import static com.github.mjeanroy.junit.servers.testing.ReflectionTestUtils.readPrivate;
import static org.assertj.core.api.Assertions.assertThat;

class OkHttpClientTest extends BaseHttpClientTest {

	@Override
	protected HttpClient createDefaultClient(EmbeddedServer<?> server) {
		return defaultOkHttpClient(server);
	}

	@Override
	protected HttpClient createCustomClient(HttpClientConfiguration configuration, EmbeddedServer<?> server) {
		return newOkHttpClient(configuration, server);
	}

	@Override
	protected void checkInternalHttpClient(HttpClientConfiguration configuration, HttpClient httpClient) {
		okhttp3.OkHttpClient internalClient = readPrivate(httpClient, "client");
		assertThat(internalClient.followRedirects()).isEqualTo(configuration.isFollowRedirect());
	}

	@Override
	protected void checkInternalHttpClient(HttpClient httpClient) {
		okhttp3.OkHttpClient internalClient = readPrivate(httpClient, "client");
		assertThat(internalClient).isNotNull();
	}

	@Test
	void it_should_implement_to_string() {
		EmbeddedServer<?> server = new EmbeddedServerMockBuilder().build();
		HttpClient client = createDefaultClient(server);
		okhttp3.OkHttpClient internalClient = readPrivate(client, "client");

		assertThat(client).hasToString(
			"OkHttpClient{" +
				"configuration: HttpClientConfiguration{" +
					"followRedirect: true, " +
					"defaultHeaders: {}, " +
					"defaultCookies: []" +
				"}, " +
				"server: MockEmbeddedServer, " +
				"client: " + internalClient.toString() + ", " +
				"destroyed: false" +
			"}"
		);
	}
}
