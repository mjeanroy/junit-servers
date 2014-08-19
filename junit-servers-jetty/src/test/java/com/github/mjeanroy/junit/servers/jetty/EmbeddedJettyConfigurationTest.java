package com.github.mjeanroy.junit.servers.jetty;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.github.mjeanroy.junit.servers.servers.AbstractEmbeddedServerConfiguration;

public class EmbeddedJettyConfigurationTest {

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	private EmbeddedJettyConfiguration configuration;

	@Before
	public void setUp() {
		configuration = new EmbeddedJettyConfiguration();
	}

	@Test
	public void it_should_have_default_values() {
		assertThat(configuration.getPath()).isEqualTo("/");
		assertThat(configuration.getPort()).isZero();
		assertThat(configuration.getClasspath()).isEqualTo(".");
	}

	@Test
	public void it_should_change_port() {
		int oldPort = configuration.getPort();
		int newPort = oldPort + 10;

		EmbeddedJettyConfiguration result = configuration.withPort(newPort);

		assertThat(result).isSameAs(configuration);
		assertThat(result.getPort()).isNotEqualTo(oldPort).isEqualTo(newPort);
	}

	@Test
	public void it_should_change_path() {
		String oldPath = configuration.getPath();
		String newPath = oldPath + "foo";

		AbstractEmbeddedServerConfiguration result = configuration.withPath(newPath);

		assertThat(result).isSameAs(configuration);
		assertThat(result.getPath()).isNotEqualTo(oldPath).isEqualTo(newPath);
	}

	@Test
	public void it_should_change_webapp_path() {
		String oldWebapp = configuration.getWebapp();
		String newWebapp = oldWebapp + "foo";

		EmbeddedJettyConfiguration result = configuration.withWebapp(newWebapp);

		assertThat(result).isSameAs(configuration);
		assertThat(result.getWebapp()).isNotEqualTo(oldWebapp).isEqualTo(newWebapp);
	}

	@Test
	public void it_should_change_webapp_path_with_file() throws Exception {
		String oldWebapp = configuration.getWebapp();
		File file = folder.newFile("foo");
		String newWebapp = file.getAbsolutePath();

		EmbeddedJettyConfiguration result = configuration.withWebapp(file);

		assertThat(result).isSameAs(configuration);
		assertThat(result.getWebapp()).isNotEqualTo(oldWebapp).isEqualTo(newWebapp);
	}

	@Test
	public void it_should_change_classpath_entry() {
		String oldClasspath = configuration.getClasspath();
		String newClasspath = oldClasspath + "foo";

		EmbeddedJettyConfiguration result = configuration.withClasspath(newClasspath);

		assertThat(result).isSameAs(configuration);
		assertThat(result.getClasspath()).isNotEqualTo(oldClasspath).isEqualTo(newClasspath);
	}
}
