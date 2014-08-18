package com.github.mjeanroy.junit.servers.samples.jetty.java;

import static org.assertj.core.api.Assertions.*;

import java.io.File;

import org.junit.ClassRule;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import com.github.mjeanroy.junit.servers.jetty.EmbeddedJetty;
import com.github.mjeanroy.junit.servers.jetty.EmbeddedJettyConfiguration;
import com.github.mjeanroy.junit.servers.rules.JettyServerRule;

public class IndexTest {

	private static final String PATH = "samples/spring-java-jetty/";

	private static EmbeddedJettyConfiguration configuration = initConfiguration();

	private static EmbeddedJettyConfiguration initConfiguration() {
		try {
			String current = new File(".").getCanonicalPath();
			if (!current.endsWith("/")) {
				current += "/";
			}

			String path = current.endsWith(PATH) ? current : current + PATH;

			return new EmbeddedJettyConfiguration()
					.withWebapp(path + "src/main/webapp")
					.withClasspath(path + "target/classes");
		}
		catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

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
