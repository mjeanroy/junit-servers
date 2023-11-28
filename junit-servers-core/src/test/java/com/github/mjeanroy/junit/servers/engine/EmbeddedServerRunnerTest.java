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

package com.github.mjeanroy.junit.servers.engine;

import com.github.mjeanroy.junit.servers.client.HttpClient;
import com.github.mjeanroy.junit.servers.client.HttpClientConfiguration;
import com.github.mjeanroy.junit.servers.client.HttpClientStrategy;
import com.github.mjeanroy.junit.servers.client.impl.okhttp3.OkHttpClient;
import com.github.mjeanroy.junit.servers.servers.EmbeddedServer;
import com.github.mjeanroy.junit.servers.utils.builders.EmbeddedServerMockBuilder;
import com.github.mjeanroy.junit.servers.utils.impl.FakeEmbeddedServer;
import com.github.mjeanroy.junit.servers.utils.impl.FakeEmbeddedServerConfiguration;
import com.github.mjeanroy.junit.servers.utils.impl.FakeEmbeddedServerConfigurationBuilder;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

class EmbeddedServerRunnerTest {

	@Test
	void it_should_instantiate_server_from_service_loader_with_default_configuration() {
		EmbeddedServerRunner adapter = new EmbeddedServerRunner();
		EmbeddedServer<?> server = adapter.getServer();

		assertThat(server).isNotNull().isExactlyInstanceOf(FakeEmbeddedServer.class);
		assertThat(server.getConfiguration()).isNotNull();
	}

	@Test
	void it_should_instantiate_server_from_service_loader_with_custom_configuration() {
		FakeEmbeddedServerConfiguration configuration = new FakeEmbeddedServerConfigurationBuilder().build();
		EmbeddedServerRunner adapter = new EmbeddedServerRunner(configuration);
		EmbeddedServer<?> server = adapter.getServer();

		assertThat(server).isNotNull().isExactlyInstanceOf(FakeEmbeddedServer.class);
		assertThat(server.getConfiguration()).isSameAs(configuration);
	}

	@Test
	void it_should_start_server_before_test() {
		EmbeddedServer<?> server = new EmbeddedServerMockBuilder().build();
		EmbeddedServerRunner adapter = new EmbeddedServerRunner(server);

		adapter.beforeAll();

		verify(server).start();
	}

	@Test
	void it_should_stop_server_after_test() {
		EmbeddedServer<?> server = new EmbeddedServerMockBuilder().build();
		EmbeddedServerRunner adapter = new EmbeddedServerRunner(server);

		adapter.afterAll();

		verify(server).stop();
	}

	@Test
	void it_should_start_server() {
		EmbeddedServer<?> server = new EmbeddedServerMockBuilder().build();
		EmbeddedServerRunner adapter = new EmbeddedServerRunner(server);

		adapter.start();

		verify(server).start();
	}

	@Test
	void it_should_stop_server() {
		EmbeddedServer<?> server = new EmbeddedServerMockBuilder().build();
		EmbeddedServerRunner adapter = new EmbeddedServerRunner(server);

		adapter.stop();

		verify(server).stop();
	}

	@Test
	void it_should_restart_server() {
		EmbeddedServer<?> server = new EmbeddedServerMockBuilder().build();
		EmbeddedServerRunner adapter = new EmbeddedServerRunner(server);

		adapter.restart();

		verify(server).restart();
	}

	@Test
	void it_should_check_if_server_is_started() {
		EmbeddedServer<?> server = new EmbeddedServerMockBuilder().build();
		EmbeddedServerRunner adapter = new EmbeddedServerRunner(server);

		adapter.start();
		assertThat(adapter.isStarted()).isTrue();

		adapter.stop();
		assertThat(adapter.isStarted()).isFalse();
	}

	@Test
	void it_should_get_server_scheme() {
		EmbeddedServer<?> server = new EmbeddedServerMockBuilder().build();
		EmbeddedServerRunner adapter = new EmbeddedServerRunner(server);
		String scheme = adapter.getScheme();
		assertThat(scheme).isNotNull().isEqualTo(server.getScheme());
	}

	@Test
	void it_should_get_server_host() {
		EmbeddedServer<?> server = new EmbeddedServerMockBuilder().build();
		EmbeddedServerRunner adapter = new EmbeddedServerRunner(server);
		String host = adapter.getHost();
		assertThat(host).isNotNull().isEqualTo(server.getHost());
	}

	@Test
	void it_should_get_server_path() {
		EmbeddedServer<?> server = new EmbeddedServerMockBuilder().build();
		EmbeddedServerRunner adapter = new EmbeddedServerRunner(server);
		String path = adapter.getPath();
		assertThat(path).isNotNull().isEqualTo(server.getPath());
	}

	@Test
	void it_should_get_server_port() {
		EmbeddedServer<?> server = new EmbeddedServerMockBuilder().build();
		EmbeddedServerRunner adapter = new EmbeddedServerRunner(server);
		int port = adapter.getPort();
		assertThat(port).isNotNull().isEqualTo(server.getPort());
	}

	@Test
	void it_should_get_url() {
		EmbeddedServer<?> server = new EmbeddedServerMockBuilder().build();
		EmbeddedServerRunner adapter = new EmbeddedServerRunner(server);
		String url = adapter.getUrl();
		assertThat(url).isNotNull().isEqualTo(server.getUrl());
	}

	@Test
	void it_should_get_server() {
		EmbeddedServer<?> server = new EmbeddedServerMockBuilder().build();
		EmbeddedServerRunner adapter = new EmbeddedServerRunner(server);
		EmbeddedServer<?> result = adapter.getServer();
		assertThat(result).isNotNull().isSameAs(server);
	}

	@Test
	void it_should_get_client() {
		EmbeddedServer<?> server = new EmbeddedServerMockBuilder().build();
		EmbeddedServerRunner adapter = new EmbeddedServerRunner(server);
		HttpClient client = adapter.getClient();
		assertThat(client).isNotNull();
	}

	@Test
	void it_should_get_client_and_returns_previous_one() {
		EmbeddedServer<?> server = new EmbeddedServerMockBuilder().build();
		EmbeddedServerRunner adapter = new EmbeddedServerRunner(server);
		HttpClient client1 = adapter.getClient();
		HttpClient client2 = adapter.getClient();

		assertThat(client1).isNotNull();
		assertThat(client2).isNotNull();
		assertThat(client1).isSameAs(client2);
	}

	@Test
	void it_should_get_client_of_given_strategy() {
		EmbeddedServer<?> server = new EmbeddedServerMockBuilder().build();
		EmbeddedServerRunner adapter = new EmbeddedServerRunner(server);
		HttpClientStrategy strategy = HttpClientStrategy.OK_HTTP3;
		HttpClient client = adapter.getClient(strategy);
		assertThat(client).isNotNull();
	}

	@Test
	void it_should_get_client_of_given_strategy_and_returns_previous_one() {
		EmbeddedServer<?> server = new EmbeddedServerMockBuilder().build();
		EmbeddedServerRunner adapter = new EmbeddedServerRunner(server);
		HttpClientStrategy strategy = HttpClientStrategy.OK_HTTP3;
		HttpClient client1 = adapter.getClient(strategy);
		HttpClient client2 = adapter.getClient(strategy);

		assertThat(client1).isNotNull();
		assertThat(client2).isNotNull();
		assertThat(client1).isSameAs(client2);
	}

	@Test
	public void it_should_get_client_with_custom_configuration() {
		EmbeddedServer<?> server = new EmbeddedServerMockBuilder().build();
		EmbeddedServerRunner adapter = new EmbeddedServerRunner(server);
		HttpClientConfiguration configuration = new HttpClientConfiguration.Builder().disableFollowRedirect().build();
		HttpClient client = adapter.getClient(configuration);

		assertThat(client).isNotNull();
		assertThat(client.getConfiguration()).isSameAs(configuration);
	}

	@Test
	public void it_should_get_client_with_custom_configuration_and_return_previous_one_with_same_configuration() {
		EmbeddedServer<?> server = new EmbeddedServerMockBuilder().build();
		EmbeddedServerRunner adapter = new EmbeddedServerRunner(server);

		HttpClientConfiguration configuration1 = new HttpClientConfiguration.Builder().build();
		HttpClientConfiguration configuration2 = new HttpClientConfiguration.Builder().build();

		HttpClient client1 = adapter.getClient(configuration1);
		HttpClient client2 = adapter.getClient(configuration2);

		assertThat(client1).isSameAs(client2);
	}

	@Test
	public void it_should_get_client_and_do_not_return_previous_one_with_different_configuration() {
		EmbeddedServer<?> server = new EmbeddedServerMockBuilder().build();
		EmbeddedServerRunner adapter = new EmbeddedServerRunner(server);
		HttpClientConfiguration configuration1 = new HttpClientConfiguration.Builder().build();
		HttpClientConfiguration configuration2 = new HttpClientConfiguration.Builder().disableFollowRedirect().build();

		HttpClient client1 = adapter.getClient(configuration1);
		HttpClient client2 = adapter.getClient(configuration2);

		assertThat(client1).isNotSameAs(client2);
		assertThat(client1.getConfiguration()).isSameAs(configuration1);
		assertThat(client2.getConfiguration()).isSameAs(configuration2);
	}

	@Test
	public void it_should_get_client_with_given_strategy_custom_configuration() {
		EmbeddedServer<?> server = new EmbeddedServerMockBuilder().build();
		EmbeddedServerRunner adapter = new EmbeddedServerRunner(server);
		HttpClientStrategy strategy = HttpClientStrategy.OK_HTTP3;
		HttpClientConfiguration configuration = new HttpClientConfiguration.Builder().disableFollowRedirect().build();
		HttpClient client = adapter.getClient(strategy, configuration);

		assertThat(client).isInstanceOf(OkHttpClient.class);
		assertThat(client.getConfiguration()).isSameAs(configuration);
	}

	@Test
	public void it_should_get_client_with_given_strategy_custom_configuration_and_return_new_one_with_different_configuration() {
		EmbeddedServer<?> server = new EmbeddedServerMockBuilder().build();
		EmbeddedServerRunner adapter = new EmbeddedServerRunner(server);
		HttpClientStrategy strategy = HttpClientStrategy.OK_HTTP3;

		HttpClientConfiguration configuration1 = new HttpClientConfiguration.Builder().disableFollowRedirect().build();
		HttpClientConfiguration configuration2 = new HttpClientConfiguration.Builder().build();

		HttpClient client1 = adapter.getClient(strategy, configuration1);
		HttpClient client2 = adapter.getClient(strategy, configuration2);

		assertThat(client1).isNotSameAs(client2);
		assertThat(client1).isInstanceOf(OkHttpClient.class);
		assertThat(client2).isInstanceOf(OkHttpClient.class);

		assertThat(client1.getConfiguration()).isSameAs(configuration1);
		assertThat(client2.getConfiguration()).isSameAs(configuration2);
	}

	@Test
	public void it_should_get_client_with_given_strategy_custom_configuration_and_return_previous_one_with_same_configuration() {
		EmbeddedServer<?> server = new EmbeddedServerMockBuilder().build();
		EmbeddedServerRunner adapter = new EmbeddedServerRunner(server);
		HttpClientStrategy strategy = HttpClientStrategy.OK_HTTP3;

		HttpClientConfiguration configuration1 = new HttpClientConfiguration.Builder().build();
		HttpClientConfiguration configuration2 = new HttpClientConfiguration.Builder().build();

		HttpClient client1 = adapter.getClient(strategy, configuration1);
		HttpClient client2 = adapter.getClient(strategy, configuration2);

		assertThat(client1).isSameAs(client2);
	}

	@Test
	void it_should_stop_server_and_close_clients() {
		EmbeddedServer<?> server = new EmbeddedServerMockBuilder().build();
		EmbeddedServerRunner adapter = new EmbeddedServerRunner(server);
		HttpClient client = adapter.getClient();

		assertThat(client).isNotNull();
		assertThat(client.isDestroyed()).isFalse();

		adapter.stop();

		assertThat(client).isNotNull();
		assertThat(client.isDestroyed()).isTrue();
	}

	@Test
	void it_should_get_client_of_given_strategy_and_destroy_it_when_server_stop() {
		EmbeddedServer<?> server = new EmbeddedServerMockBuilder().build();
		EmbeddedServerRunner adapter = new EmbeddedServerRunner(server);
		HttpClientStrategy strategy = HttpClientStrategy.OK_HTTP3;
		HttpClient client = adapter.getClient(strategy);

		assertThat(client).isNotNull();
		assertThat(client.isDestroyed()).isFalse();

		adapter.stop();

		assertThat(client).isNotNull();
		assertThat(client.isDestroyed()).isTrue();
	}

	@Test
	void it_should_implement_to_string() {
		EmbeddedServer<?> server = new EmbeddedServerMockBuilder().build();
		EmbeddedServerRunner adapter = new EmbeddedServerRunner(server);

		assertThat(adapter).hasToString(
			"EmbeddedServerRunner{" +
				"server: MockEmbeddedServer, " +
				"clients: {}" +
			"}"
		);
	}
}
