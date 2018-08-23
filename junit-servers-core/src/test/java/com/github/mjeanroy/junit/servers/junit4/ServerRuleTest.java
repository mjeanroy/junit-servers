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

package com.github.mjeanroy.junit.servers.junit4;

import com.github.mjeanroy.junit.servers.client.HttpClient;
import com.github.mjeanroy.junit.servers.servers.AbstractConfiguration;
import com.github.mjeanroy.junit.servers.servers.EmbeddedServer;
import com.github.mjeanroy.junit.servers.utils.builders.EmbeddedServerMockBuilder;
import com.github.mjeanroy.junit.servers.utils.impl.FakeEmbeddedServer;
import com.github.mjeanroy.junit.servers.utils.impl.FakeEmbeddedServerConfiguration;
import com.github.mjeanroy.junit.servers.utils.impl.FakeEmbeddedServerConfigurationBuilder;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ServerRuleTest {

	@Test
	public void it_should_instantiate_server_from_service_loader_with_default_configuration() {
		final ServerRule rule = createRule();
		final EmbeddedServer<?> server = rule.getServer();

		assertThat(server).isNotNull().isExactlyInstanceOf(FakeEmbeddedServer.class);
		assertThat(server.getConfiguration()).isNotNull();
	}

	@Test
	public void it_should_instantiate_server_from_service_loader_with_custom_configuration() {
		final FakeEmbeddedServerConfiguration configuration = new FakeEmbeddedServerConfigurationBuilder().build();
		final ServerRule rule = createRule(configuration);
		final EmbeddedServer<?> server = rule.getServer();

		assertThat(server).isNotNull().isExactlyInstanceOf(FakeEmbeddedServer.class);
		assertThat(server.getConfiguration()).isSameAs(configuration);
	}

	@Test
	public void it_should_start_server() {
		final EmbeddedServer<?> server = new EmbeddedServerMockBuilder().build();
		final ServerRule rule = createRule(server);

		rule.before();

		verify(server).start();
	}

	@Test
	public void it_should_stop_server() {
		final EmbeddedServer<?> server = new EmbeddedServerMockBuilder().build();
		final ServerRule rule = createRule(server);

		rule.after();

		verify(server).stop();
	}

	@Test
	public void it_should_restart_server() {
		final EmbeddedServer<?> server = new EmbeddedServerMockBuilder().build();
		final ServerRule rule = createRule(server);

		rule.restart();

		verify(server).restart();
	}

	@Test
	public void it_should_check_if_server_is_started() {
		final EmbeddedServer<?> server = new EmbeddedServerMockBuilder().build();
		final ServerRule rule = createRule(server);
		assertThat(rule.isStarted()).isFalse();

		rule.start();
		assertThat(rule.isStarted()).isTrue();
		verify(server, times(2)).isStarted();
	}

	@Test
	public void it_should_get_server_scheme() {
		final String scheme = "http";
		final EmbeddedServer<?> server = new EmbeddedServerMockBuilder().withScheme(scheme).build();
		final ServerRule rule = createRule(server);

		final String result = rule.getScheme();

		assertThat(result).isEqualTo(scheme);
		verify(server).getScheme();
	}

	@Test
	public void it_should_get_server_host() {
		final String host = "http";
		final EmbeddedServer<?> server = new EmbeddedServerMockBuilder().withHost(host).build();
		final ServerRule rule = createRule(server);

		final String result = rule.getHost();

		assertThat(result).isEqualTo(host);
		verify(server).getHost();
	}

	@Test
	public void it_should_get_server_path() {
		final String path = "/foo";
		final EmbeddedServer<?> server = new EmbeddedServerMockBuilder().withPath(path).build();
		final ServerRule rule = createRule(server);

		final String result = rule.getPath();

		assertThat(result).isEqualTo(path);
		verify(server).getPath();
	}

	@Test
	public void it_should_get_server_url() {
		final EmbeddedServer<?> server = new EmbeddedServerMockBuilder()
				.withScheme("http")
				.withHost("localhost")
				.withPort(8080)
				.withPath("/")
				.build();

		final ServerRule rule = createRule(server);
		final String result = rule.getUrl();

		assertThat(result).isEqualTo("http://localhost:8080/");
		verify(server).getUrl();
	}

	@Test
	public void it_should_get_server() {
		final EmbeddedServer<?> server = new EmbeddedServerMockBuilder().build();
		final ServerRule rule = createRule(server);
		final EmbeddedServer<?> result = rule.getServer();

		assertThat(result).isSameAs(server);
	}

	@Test
	public void it_should_get_http_client() {
		final EmbeddedServer<?> server = new EmbeddedServerMockBuilder().build();
		final ServerRule rule = createRule(server);
		final HttpClient client = rule.getClient();

		assertThat(client).isNotNull();
		assertThat(client).isSameAs(rule.getClient());
		assertThat(client.isDestroyed()).isFalse();
	}

	@Test
	public void it_should_get_new_http_client_if_it_has_been_destroyed() {
		final EmbeddedServer<?> server = new EmbeddedServerMockBuilder().build();
		final ServerRule rule = createRule(server);
		final HttpClient client = rule.getClient();

		assertThat(client).isNotNull();
		assertThat(client).isSameAs(rule.getClient());
		assertThat(client.isDestroyed()).isFalse();

		client.destroy();

		HttpClient newClient = rule.getClient();
		assertThat(newClient).isNotNull();
		assertThat(newClient).isNotSameAs(client);
		assertThat(newClient.isDestroyed()).isFalse();
	}

	@Test
	public void it_should_destroy_http_client_when_rule_stop() {
		final EmbeddedServer<?> server = new EmbeddedServerMockBuilder().build();
		final ServerRule rule = createRule(server);
		final HttpClient client = rule.getClient();
		assertThat(client).isNotNull();
		assertThat(client.isDestroyed()).isFalse();

		rule.after();

		assertThat(client.isDestroyed()).isTrue();
	}

	protected ServerRule createRule() {
		return new ServerRule();
	}

	protected ServerRule createRule(AbstractConfiguration configuration) {
		return new ServerRule(configuration);
	}

	protected ServerRule createRule(EmbeddedServer<?> server) {
		return new ServerRule(server);
	}
}
