/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2017 <mickael.jeanroy@gmail.com>
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

package com.github.mjeanroy.junit.servers.rules;

import com.github.mjeanroy.junit.servers.client.HttpClient;
import com.github.mjeanroy.junit.servers.exceptions.ServerImplMissingException;
import com.github.mjeanroy.junit.servers.servers.EmbeddedServer;
import com.github.mjeanroy.junit.servers.servers.configuration.AbstractConfiguration;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static com.github.mjeanroy.junit.servers.utils.commons.TestUtils.localUrl;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ServerRuleTest {

	private AbstractConfiguration configuration;
	private EmbeddedServer<?> server;
	private ServerRule rule;

	@Before
	public void setUp() {
		configuration = mock(AbstractConfiguration.class);
		server = mock(EmbeddedServer.class);

		when(server.getConfiguration()).thenAnswer(new Answer<AbstractConfiguration>() {
			@Override
			public AbstractConfiguration answer(InvocationOnMock invocation) {
				return configuration;
			}
		});

		rule = new ServerRule(server);
	}

	@Test
	public void it_should_start_server() {
		rule.before();
		verify(server).start();
	}

	@Test
	public void it_should_stop_server() {
		rule.after();
		verify(server).stop();
	}

	@Test
	public void it_should_restart_server() {
		rule.restart();
		verify(server).restart();
	}

	@Test
	public void it_should_check_if_server_is_started() {
		boolean started = true;
		when(server.isStarted()).thenReturn(started);

		boolean result = rule.isStarted();

		assertThat(result).isEqualTo(started);
		verify(server).isStarted();
	}

	@Test
	public void it_should_get_server_scheme() {
		String scheme = "http";
		when(server.getScheme()).thenReturn(scheme);

		String result = rule.getScheme();

		assertThat(result).isEqualTo(scheme);
		verify(server).getScheme();
	}

	@Test
	public void it_should_get_server_host() {
		String host = "http";
		when(server.getHost()).thenReturn(host);

		String result = rule.getHost();

		assertThat(result).isEqualTo(host);
		verify(server).getHost();
	}

	@Test
	public void it_should_get_server_path() {
		String path = "/foo";
		when(server.getPath()).thenReturn(path);

		String result = rule.getPath();

		assertThat(result).isEqualTo(path);
		verify(server).getPath();
	}

	@Test
	public void it_should_get_server_url() {
		String url = localUrl(8080, "/foo");
		when(server.getUrl()).thenReturn(url);

		String result = rule.getUrl();

		assertThat(result).isEqualTo(url);
		verify(server).getUrl();
	}

	@Test
	public void it_should_get_server() {
		EmbeddedServer<?> result = rule.getServer();
		assertThat(result).isSameAs(server);
	}

	@Test
	public void it_should_get_http_client() {
		HttpClient client = rule.getClient();
		assertThat(client).isNotNull();
		assertThat(client).isSameAs(rule.getClient());
		assertThat(client.isDestroyed()).isFalse();
	}

	@Test
	public void it_should_get_new_http_client_if_it_has_been_destroyed() {
		HttpClient client = rule.getClient();
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
		HttpClient client = rule.getClient();
		assertThat(client).isNotNull();
		assertThat(client.isDestroyed()).isFalse();

		rule.after();

		assertThat(client.isDestroyed()).isTrue();
	}

	@Test
	public void it_should_fail_to_instantiate_server_without_implementations() {
		assertThatThrownBy(newServerRule())
				.isExactlyInstanceOf(ServerImplMissingException.class)
				.hasMessage("Embedded server implementation is missing, please import appropriate sub-module");
	}

	@Test
	public void it_should_fail_to_instantiate_server_with_configuration_but_without_implementations() {
		assertThatThrownBy(newServerRule(mock(AbstractConfiguration.class)))
				.isExactlyInstanceOf(ServerImplMissingException.class)
				.hasMessage("Embedded server implementation is missing, please import appropriate sub-module");
	}

	private static ThrowingCallable newServerRule() {
		return new ThrowingCallable() {
			@Override
			public void call() {
				new ServerRule();
			}
		};
	}

	private static ThrowingCallable newServerRule(final AbstractConfiguration configuration) {
		return new ThrowingCallable() {
			@Override
			public void call() {
				new ServerRule(configuration);
			}
		};
	}
}
