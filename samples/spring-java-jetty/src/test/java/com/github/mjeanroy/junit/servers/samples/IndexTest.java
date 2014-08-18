package com.github.mjeanroy.junit.servers.samples;

import static org.assertj.core.api.Assertions.*;

import org.junit.ClassRule;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import com.github.mjeanroy.junit.servers.jetty.EmbeddedJetty;
import com.github.mjeanroy.junit.servers.rules.JettyServerRule;
import com.github.mjeanroy.junit.servers.servers.EmbeddedServerConfiguration;

public class IndexTest {

	private static EmbeddedServerConfiguration configuration = new EmbeddedServerConfiguration()
			.withWebapp("samples/spring-java-jetty/src/main/webapp");

	private static EmbeddedJetty jetty = new EmbeddedJetty(configuration);

	private static RestTemplate restTemplate = new RestTemplate();

	@ClassRule
	public static JettyServerRule serverRule = new JettyServerRule(jetty);

	@Test
	public void it_should_have_an_index() {
		String url = url() + "index";
		String message = restTemplate.getForObject(url, String.class);
		assertThat(message).isNotEmpty().isEqualTo("Hello World");
	}

	public String url() {
		return String.format("http://%s:%s/", "localhost", jetty.getPort());
	}
}
