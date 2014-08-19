/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 <mickael.jeanroy@gmail.com>
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

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.github.mjeanroy.junit.servers.servers.EmbeddedServer;

@RunWith(MockitoJUnitRunner.class)
public class ServerRuleTest {

	@Mock
	private EmbeddedServer server;

	private ServerRule rule;

	@Before
	public void setUp() {
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
	public void it_should_get_server_port() {
		int port = 8080;
		when(server.getPort()).thenReturn(port);

		int result = rule.getPort();

		assertThat(result).isEqualTo(port);
		verify(server).getPort();
	}

	@Test
	public void it_should_get_server() {
		EmbeddedServer result = rule.getServer();
		assertThat(result).isSameAs(server);
	}
}
