package com.github.mjeanroy.junit.servers.jetty;

import static org.assertj.core.api.Assertions.*;

import org.junit.After;
import org.junit.Test;

public class EmbeddedJettyTest {

	private EmbeddedJetty jetty;

	@After
	public void tearDown() {
		if (jetty != null) {
			jetty.stop();
		}
	}

	@Test
	public void it_should_start_jetty() {
		EmbeddedJetty jetty = new EmbeddedJetty();
		jetty.start();

		assertThat(jetty.isStarted()).isTrue();
		assertThat(jetty.getPort()).isNotZero();
	}

	@Test
	public void it_should_stop_jetty() {
		EmbeddedJetty jetty = new EmbeddedJetty();
		jetty.start();

		assertThat(jetty.isStarted()).isTrue();
		assertThat(jetty.getPort()).isNotZero();

		jetty.stop();
		assertThat(jetty.isStarted()).isFalse();
		assertThat(jetty.getPort()).isNotZero();
	}
}
