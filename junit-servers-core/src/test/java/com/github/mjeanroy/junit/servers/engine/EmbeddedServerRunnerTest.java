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
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

public class EmbeddedServerRunnerTest {

	@Test
	public void it_should_instantiate_server_from_service_loader_with_default_configuration() {
		final EmbeddedServerRunner adapter = new EmbeddedServerRunner();
		final EmbeddedServer<?> server = adapter.getServer();

		assertThat(server).isNotNull().isExactlyInstanceOf(FakeEmbeddedServer.class);
		assertThat(server.getConfiguration()).isNotNull();
	}

	@Test
	public void it_should_instantiate_server_from_service_loader_with_custom_configuration() {
		final FakeEmbeddedServerConfiguration configuration = new FakeEmbeddedServerConfigurationBuilder().build();
		final EmbeddedServerRunner adapter = new EmbeddedServerRunner(configuration);
		final EmbeddedServer<?> server = adapter.getServer();

		assertThat(server).isNotNull().isExactlyInstanceOf(FakeEmbeddedServer.class);
		assertThat(server.getConfiguration()).isSameAs(configuration);
	}

	@Test
	public void it_should_start_server_before_test() {
		final EmbeddedServer<?> server = new EmbeddedServerMockBuilder().build();
		final EmbeddedServerRunner adapter = new EmbeddedServerRunner(server);

		adapter.beforeAll();

		verify(server).start();
	}

	@Test
	public void it_should_stop_server_after_test() {
		final EmbeddedServer<?> server = new EmbeddedServerMockBuilder().build();
		final EmbeddedServerRunner adapter = new EmbeddedServerRunner(server);

		adapter.afterAll();

		verify(server).stop();
	}

	@Test
	public void it_should_start_server() {
		final EmbeddedServer<?> server = new EmbeddedServerMockBuilder().build();
		final EmbeddedServerRunner adapter = new EmbeddedServerRunner(server);

		adapter.start();

		verify(server).start();
	}

	@Test
	public void it_should_stop_server() {
		final EmbeddedServer<?> server = new EmbeddedServerMockBuilder().build();
		final EmbeddedServerRunner adapter = new EmbeddedServerRunner(server);

		adapter.stop();

		verify(server).stop();
	}

	@Test
	public void it_should_restart_server() {
		final EmbeddedServer<?> server = new EmbeddedServerMockBuilder().build();
		final EmbeddedServerRunner adapter = new EmbeddedServerRunner(server);

		adapter.restart();

		verify(server).restart();
	}

	@Test
	public void it_should_check_if_server_is_started() {
		final EmbeddedServer<?> server = new EmbeddedServerMockBuilder().build();
		final EmbeddedServerRunner adapter = new EmbeddedServerRunner(server);

		adapter.start();
		assertThat(adapter.isStarted()).isTrue();

		adapter.stop();
		assertThat(adapter.isStarted()).isFalse();
	}

	@Test
	public void it_should_get_server_scheme() {
		final EmbeddedServer<?> server = new EmbeddedServerMockBuilder().build();
		final EmbeddedServerRunner adapter = new EmbeddedServerRunner(server);
		final String scheme = adapter.getScheme();
		assertThat(scheme).isNotNull().isEqualTo(server.getScheme());
	}

	@Test
	public void it_should_get_server_host() {
		final EmbeddedServer<?> server = new EmbeddedServerMockBuilder().build();
		final EmbeddedServerRunner adapter = new EmbeddedServerRunner(server);
		final String host = adapter.getHost();
		assertThat(host).isNotNull().isEqualTo(server.getHost());
	}

	@Test
	public void it_should_get_server_path() {
		final EmbeddedServer<?> server = new EmbeddedServerMockBuilder().build();
		final EmbeddedServerRunner adapter = new EmbeddedServerRunner(server);
		final String path = adapter.getPath();
		assertThat(path).isNotNull().isEqualTo(server.getPath());
	}

	@Test
	public void it_should_get_server_port() {
		final EmbeddedServer<?> server = new EmbeddedServerMockBuilder().build();
		final EmbeddedServerRunner adapter = new EmbeddedServerRunner(server);
		final int port = adapter.getPort();
		assertThat(port).isNotNull().isEqualTo(server.getPort());
	}

	@Test
	public void it_should_get_url() {
		final EmbeddedServer<?> server = new EmbeddedServerMockBuilder().build();
		final EmbeddedServerRunner adapter = new EmbeddedServerRunner(server);
		final String url = adapter.getUrl();
		assertThat(url).isNotNull().isEqualTo(server.getUrl());
	}

	@Test
	public void it_should_get_server() {
		final EmbeddedServer<?> server = new EmbeddedServerMockBuilder().build();
		final EmbeddedServerRunner adapter = new EmbeddedServerRunner(server);
		final EmbeddedServer<?> result = adapter.getServer();
		assertThat(result).isNotNull().isSameAs(server);
	}

	@Test
	public void it_should_get_client() {
		final EmbeddedServer<?> server = new EmbeddedServerMockBuilder().build();
		final EmbeddedServerRunner adapter = new EmbeddedServerRunner(server);
		final HttpClient client = adapter.getClient();
		assertThat(client).isNotNull();
	}

	@Test
	public void it_should_get_client_and_returns_previous_one() {
		final EmbeddedServer<?> server = new EmbeddedServerMockBuilder().build();
		final EmbeddedServerRunner adapter = new EmbeddedServerRunner(server);
		final HttpClient client1 = adapter.getClient();
		final HttpClient client2 = adapter.getClient();

		assertThat(client1).isNotNull();
		assertThat(client2).isNotNull();
		assertThat(client1).isSameAs(client2);
	}

	@Test
	public void it_should_get_client_of_given_strategy() {
		final EmbeddedServer<?> server = new EmbeddedServerMockBuilder().build();
		final EmbeddedServerRunner adapter = new EmbeddedServerRunner(server);
		final HttpClientStrategy strategy = HttpClientStrategy.OK_HTTP3;
		final HttpClient client = adapter.getClient(strategy);
		assertThat(client).isNotNull();
	}

	@Test
	public void it_should_get_client_of_given_strategy_and_returns_previous_one() {
		final EmbeddedServer<?> server = new EmbeddedServerMockBuilder().build();
		final EmbeddedServerRunner adapter = new EmbeddedServerRunner(server);
		final HttpClientStrategy strategy = HttpClientStrategy.OK_HTTP3;
		final HttpClient client1 = adapter.getClient(strategy);
		final HttpClient client2 = adapter.getClient(strategy);

		assertThat(client1).isNotNull();
		assertThat(client2).isNotNull();
		assertThat(client1).isSameAs(client2);
	}

	@Test
	public void it_should_get_client_with_custom_configuration() {
		final EmbeddedServer<?> server = new EmbeddedServerMockBuilder().build();
		final EmbeddedServerRunner adapter = new EmbeddedServerRunner(server);
		final HttpClientConfiguration configuration = new HttpClientConfiguration.Builder().disableFollowRedirect().build();
		final HttpClient client = adapter.getClient(configuration);

		assertThat(client).isNotNull();
		assertThat(client.getConfiguration()).isSameAs(configuration);
	}

	@Test
	public void it_should_get_client_with_custom_configuration_and_return_previous_one_with_same_configuration() {
		final EmbeddedServer<?> server = new EmbeddedServerMockBuilder().build();
		final EmbeddedServerRunner adapter = new EmbeddedServerRunner(server);

		final HttpClientConfiguration configuration1 = new HttpClientConfiguration.Builder().build();
		final HttpClientConfiguration configuration2 = new HttpClientConfiguration.Builder().build();

		final HttpClient client1 = adapter.getClient(configuration1);
		final HttpClient client2 = adapter.getClient(configuration2);

		assertThat(client1).isSameAs(client2);
	}

	@Test
	public void it_should_get_client_and_do_not_return_previous_one_with_different_configuration() {
		final EmbeddedServer<?> server = new EmbeddedServerMockBuilder().build();
		final EmbeddedServerRunner adapter = new EmbeddedServerRunner(server);
		final HttpClientConfiguration configuration1 = new HttpClientConfiguration.Builder().build();
		final HttpClientConfiguration configuration2 = new HttpClientConfiguration.Builder().disableFollowRedirect().build();

		final HttpClient client1 = adapter.getClient(configuration1);
		final HttpClient client2 = adapter.getClient(configuration2);

		assertThat(client1).isNotSameAs(client2);
		assertThat(client1.getConfiguration()).isSameAs(configuration1);
		assertThat(client2.getConfiguration()).isSameAs(configuration2);
	}

	@Test
	public void it_should_get_client_with_given_strategy_custom_configuration() {
		final EmbeddedServer<?> server = new EmbeddedServerMockBuilder().build();
		final EmbeddedServerRunner adapter = new EmbeddedServerRunner(server);
		final HttpClientStrategy strategy = HttpClientStrategy.OK_HTTP3;
		final HttpClientConfiguration configuration = new HttpClientConfiguration.Builder().disableFollowRedirect().build();
		final HttpClient client = adapter.getClient(strategy, configuration);

		assertThat(client).isInstanceOf(OkHttpClient.class);
		assertThat(client.getConfiguration()).isSameAs(configuration);
	}

	@Test
	public void it_should_get_client_with_given_strategy_custom_configuration_and_return_new_one_with_different_configuration() {
		final EmbeddedServer<?> server = new EmbeddedServerMockBuilder().build();
		final EmbeddedServerRunner adapter = new EmbeddedServerRunner(server);
		final HttpClientStrategy strategy = HttpClientStrategy.OK_HTTP3;

		final HttpClientConfiguration configuration1 = new HttpClientConfiguration.Builder().disableFollowRedirect().build();
		final HttpClientConfiguration configuration2 = new HttpClientConfiguration.Builder().build();

		final HttpClient client1 = adapter.getClient(strategy, configuration1);
		final HttpClient client2 = adapter.getClient(strategy, configuration2);

		assertThat(client1).isNotSameAs(client2);
		assertThat(client1).isInstanceOf(OkHttpClient.class);
		assertThat(client2).isInstanceOf(OkHttpClient.class);

		assertThat(client1.getConfiguration()).isSameAs(configuration1);
		assertThat(client2.getConfiguration()).isSameAs(configuration2);
	}

	@Test
	public void it_should_get_client_with_given_strategy_custom_configuration_and_return_previous_one_with_same_configuration() {
		final EmbeddedServer<?> server = new EmbeddedServerMockBuilder().build();
		final EmbeddedServerRunner adapter = new EmbeddedServerRunner(server);
		final HttpClientStrategy strategy = HttpClientStrategy.OK_HTTP3;

		final HttpClientConfiguration configuration1 = new HttpClientConfiguration.Builder().build();
		final HttpClientConfiguration configuration2 = new HttpClientConfiguration.Builder().build();

		final HttpClient client1 = adapter.getClient(strategy, configuration1);
		final HttpClient client2 = adapter.getClient(strategy, configuration2);

		assertThat(client1).isSameAs(client2);
	}

	@Test
	public void it_should_stop_server_and_close_clients() {
		final EmbeddedServer<?> server = new EmbeddedServerMockBuilder().build();
		final EmbeddedServerRunner adapter = new EmbeddedServerRunner(server);
		final HttpClient client = adapter.getClient();

		assertThat(client).isNotNull();
		assertThat(client.isDestroyed()).isFalse();

		adapter.stop();

		assertThat(client).isNotNull();
		assertThat(client.isDestroyed()).isTrue();
	}

	@Test
	public void it_should_get_client_of_given_strategy_and_destroy_it_when_server_stop() {
		final EmbeddedServer<?> server = new EmbeddedServerMockBuilder().build();
		final EmbeddedServerRunner adapter = new EmbeddedServerRunner(server);
		final HttpClientStrategy strategy = HttpClientStrategy.OK_HTTP3;
		final HttpClient client = adapter.getClient(strategy);

		assertThat(client).isNotNull();
		assertThat(client.isDestroyed()).isFalse();

		adapter.stop();

		assertThat(client).isNotNull();
		assertThat(client.isDestroyed()).isTrue();
	}

	@Test
	public void it_should_implement_to_string() {
		final EmbeddedServer<?> server = new EmbeddedServerMockBuilder().build();
		final EmbeddedServerRunner adapter = new EmbeddedServerRunner(server);

		assertThat(adapter).hasToString(
			"EmbeddedServerRunner{" +
				"server: MockEmbeddedServer, " +
				"clients: {}" +
			"}"
		);
	}
}
