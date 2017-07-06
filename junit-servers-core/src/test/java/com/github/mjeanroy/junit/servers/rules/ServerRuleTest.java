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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.runner.Description.createTestDescription;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.Description;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.github.mjeanroy.junit.servers.exceptions.ServerImplMissingException;
import com.github.mjeanroy.junit.servers.servers.EmbeddedServer;
import com.github.mjeanroy.junit.servers.servers.configuration.AbstractConfiguration;

public class ServerRuleTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private AbstractConfiguration configuration;
	private EmbeddedServer<?> server;
	private ServerRule rule;
	private Description description;

	@Before
	public void setUp() {
		configuration = mock(AbstractConfiguration.class);
		server = mock(EmbeddedServer.class);

		when(server.getConfiguration()).thenAnswer(new Answer<AbstractConfiguration>() {
			@Override
			public AbstractConfiguration answer(InvocationOnMock invocation) throws Throwable {
				return configuration;
			}
		});

		rule = new ServerRule(server);
		description = createTestDescription(ServerRuleTest.class, "name");
	}

	@Test
	public void it_should_start_server() {
		rule.before(description);
		verify(server).start();
	}

	@Test
	public void it_should_stop_server() {
		rule.after(description);
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
	public void it_should_get_server_port_when_server_is_stopped() {
		int port = 8080;
		int configPort = 9000;
		when(configuration.getPort()).thenReturn(configPort);
		when(server.getPort()).thenReturn(port);
		when(server.isStarted()).thenReturn(false);

		int result = rule.getPort();

		assertThat(result).isEqualTo(configPort);
		verify(server, never()).getPort();
		verify(configuration).getPort();
	}

	@Test
	public void it_should_get_server_port_when_server_is_started() {
		int port = 8080;
		int configPort = 9000;
		when(configuration.getPort()).thenReturn(configPort);
		when(server.getPort()).thenReturn(port);
		when(server.isStarted()).thenReturn(true);

		int result = rule.getPort();

		assertThat(result).isEqualTo(port);
		verify(server).getPort();
		verify(configuration, never()).getPort();
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
		String url = "http://localhost:8080/foo";
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
	public void it_should_fail_to_instantiate_server_without_implementations() {
		thrown.expect(ServerImplMissingException.class);
		thrown.expectMessage("Embedded server implementation is missing, please import appropriate sub-module");
		new ServerRule();
	}

	@Test
	public void it_should_fail_to_instantiate_server_with_configuration_but_without_implementations() {
		thrown.expect(ServerImplMissingException.class);
		thrown.expectMessage("Embedded server implementation is missing, please import appropriate sub-module");
		new ServerRule(mock(AbstractConfiguration.class));
	}
}
