package com.github.mjeanroy.junit.servers.samples.tomcat.java;

import static org.assertj.core.api.Assertions.*;

import java.io.File;

import org.junit.ClassRule;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import com.github.mjeanroy.junit.servers.rules.TomcatServerRule;
import com.github.mjeanroy.junit.servers.tomcat.EmbeddedTomcat;
import com.github.mjeanroy.junit.servers.tomcat.EmbeddedTomcatConfiguration;

public class IndexTest {

	private static final String PATH = "samples/spring-java-tomcat/";

	private static EmbeddedTomcatConfiguration configuration = initConfiguration();

	private static EmbeddedTomcatConfiguration initConfiguration() {
		try {
			String current = new File(".").getCanonicalPath();
			if (!current.endsWith("/")) {
				current += "/";
			}

			String path = current.endsWith(PATH) ? current : current + PATH;

			return new EmbeddedTomcatConfiguration()
					.withWebapp(path + "src/main/webapp")
					.withClasspath(path + "target/classes");
		}
		catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	private static EmbeddedTomcat tomcat = new EmbeddedTomcat(configuration);

	private static RestTemplate restTemplate = new RestTemplate();

	@ClassRule
	public static TomcatServerRule serverRule = new TomcatServerRule(tomcat);

	@Test
	public void it_should_have_an_index() {
		String url = url() + "index";
		String message = restTemplate.getForObject(url, String.class);
		assertThat(message).isNotEmpty().isEqualTo("Hello World");
	}

	public String url() {
		return String.format("http://%s:%s/", "localhost", tomcat.getPort());
	}
}
