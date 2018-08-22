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

package com.github.mjeanroy.junit.servers.engine;

import com.github.mjeanroy.junit.servers.client.HttpClient;
import com.github.mjeanroy.junit.servers.servers.EmbeddedServer;
import com.github.mjeanroy.junit.servers.utils.builders.EmbeddedServerMockBuilder;
import com.github.mjeanroy.junit.servers.utils.impl.FakeEmbeddedServer;
import com.github.mjeanroy.junit.servers.utils.impl.FakeEmbeddedServerConfiguration;
import com.github.mjeanroy.junit.servers.utils.impl.FakeEmbeddedServerConfigurationBuilder;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

public class EmbeddedServerTestLifeCycleAdapterTest {

	@Test
	public void it_should_instantiate_server_from_service_loader_with_default_configuration() {
		final EmbeddedServerTestLifeCycleAdapter adapter = new EmbeddedServerTestLifeCycleAdapter();
		final EmbeddedServer<?> server = adapter.getServer();

		assertThat(server).isNotNull().isExactlyInstanceOf(FakeEmbeddedServer.class);
		assertThat(server.getConfiguration()).isNotNull();
	}

	@Test
	public void it_should_instantiate_server_from_service_loader_with_custom_configuration() {
		final FakeEmbeddedServerConfiguration configuration = new FakeEmbeddedServerConfigurationBuilder().build();
		final EmbeddedServerTestLifeCycleAdapter adapter = new EmbeddedServerTestLifeCycleAdapter(configuration);
		final EmbeddedServer<?> server = adapter.getServer();

		assertThat(server).isNotNull().isExactlyInstanceOf(FakeEmbeddedServer.class);
		assertThat(server.getConfiguration()).isSameAs(configuration);
	}

	@Test
	public void it_should_start_server_before_test() {
		final EmbeddedServer server = new EmbeddedServerMockBuilder().build();
		final EmbeddedServerTestLifeCycleAdapter adapter = new EmbeddedServerTestLifeCycleAdapter(server);

		adapter.beforeAll();

		verify(server).start();
	}

	@Test
	public void it_should_stop_server_after_test() {
		final EmbeddedServer server = new EmbeddedServerMockBuilder().build();
		final EmbeddedServerTestLifeCycleAdapter adapter = new EmbeddedServerTestLifeCycleAdapter(server);

		adapter.afterAll();

		verify(server).stop();
	}

	@Test
	public void it_should_start_server() {
		final EmbeddedServer server = new EmbeddedServerMockBuilder().build();
		final EmbeddedServerTestLifeCycleAdapter adapter = new EmbeddedServerTestLifeCycleAdapter(server);

		adapter.start();

		verify(server).start();
	}

	@Test
	public void it_should_stop_server() {
		final EmbeddedServer server = new EmbeddedServerMockBuilder().build();
		final EmbeddedServerTestLifeCycleAdapter adapter = new EmbeddedServerTestLifeCycleAdapter(server);

		adapter.stop();

		verify(server).stop();
	}

	@Test
	public void it_should_restart_server() {
		final EmbeddedServer server = new EmbeddedServerMockBuilder().build();
		final EmbeddedServerTestLifeCycleAdapter adapter = new EmbeddedServerTestLifeCycleAdapter(server);

		adapter.restart();

		verify(server).restart();
	}

	@Test
	public void it_should_check_if_server_is_started() {
		final EmbeddedServer server = new EmbeddedServerMockBuilder().build();
		final EmbeddedServerTestLifeCycleAdapter adapter = new EmbeddedServerTestLifeCycleAdapter(server);

		adapter.start();
		assertThat(adapter.isStarted()).isTrue();

		adapter.stop();
		assertThat(adapter.isStarted()).isFalse();
	}

	@Test
	public void it_should_get_server_scheme() {
		final EmbeddedServer server = new EmbeddedServerMockBuilder().build();
		final EmbeddedServerTestLifeCycleAdapter adapter = new EmbeddedServerTestLifeCycleAdapter(server);
		final String scheme = adapter.getScheme();
		assertThat(scheme).isNotNull().isEqualTo(server.getScheme());
	}

	@Test
	public void it_should_get_server_host() {
		final EmbeddedServer server = new EmbeddedServerMockBuilder().build();
		final EmbeddedServerTestLifeCycleAdapter adapter = new EmbeddedServerTestLifeCycleAdapter(server);
		final String host = adapter.getHost();
		assertThat(host).isNotNull().isEqualTo(server.getHost());
	}

	@Test
	public void it_should_get_server_path() {
		final EmbeddedServer server = new EmbeddedServerMockBuilder().build();
		final EmbeddedServerTestLifeCycleAdapter adapter = new EmbeddedServerTestLifeCycleAdapter(server);
		final String path = adapter.getPath();
		assertThat(path).isNotNull().isEqualTo(server.getPath());
	}

	@Test
	public void it_should_get_server_port() {
		final EmbeddedServer server = new EmbeddedServerMockBuilder().build();
		final EmbeddedServerTestLifeCycleAdapter adapter = new EmbeddedServerTestLifeCycleAdapter(server);
		final int port = adapter.getPort();
		assertThat(port).isNotNull().isEqualTo(server.getPort());
	}

	@Test
	public void it_should_get_url() {
		final EmbeddedServer server = new EmbeddedServerMockBuilder().build();
		final EmbeddedServerTestLifeCycleAdapter adapter = new EmbeddedServerTestLifeCycleAdapter(server);
		final String url = adapter.getUrl();
		assertThat(url).isNotNull().isEqualTo(server.getUrl());
	}

	@Test
	public void it_should_get_server() {
		final EmbeddedServer server = new EmbeddedServerMockBuilder().build();
		final EmbeddedServerTestLifeCycleAdapter adapter = new EmbeddedServerTestLifeCycleAdapter(server);
		final EmbeddedServer result = adapter.getServer();
		assertThat(result).isNotNull().isSameAs(server);
	}

	@Test
	public void it_should_get_client() {
		final EmbeddedServer server = new EmbeddedServerMockBuilder().build();
		final EmbeddedServerTestLifeCycleAdapter adapter = new EmbeddedServerTestLifeCycleAdapter(server);
		final HttpClient client = adapter.getClient();
		assertThat(client).isNotNull();
	}
}
