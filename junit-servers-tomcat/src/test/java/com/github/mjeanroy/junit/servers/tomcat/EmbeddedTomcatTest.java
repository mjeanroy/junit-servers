package com.github.mjeanroy.junit.servers.tomcat;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.After;
import org.junit.Test;

public class EmbeddedTomcatTest {

	private EmbeddedTomcat tomcat;

	@After
	public void tearDown() {
		if (tomcat != null) {
			tomcat.stop();
		}
	}

	@Test
	public void it_should_start_tomcat() {
		tomcat = new EmbeddedTomcat();
		tomcat.start();

		assertThat(tomcat.isStarted()).isTrue();
		assertThat(tomcat.getPort()).isNotZero();
	}

	@Test
	public void it_should_stop_tomcat() {
		tomcat = new EmbeddedTomcat();
		tomcat.start();

		assertThat(tomcat.isStarted()).isTrue();
		assertThat(tomcat.getPort()).isNotZero();

		tomcat.stop();
		assertThat(tomcat.isStarted()).isFalse();
		assertThat(tomcat.getPort()).isNotZero();
	}
}
