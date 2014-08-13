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
