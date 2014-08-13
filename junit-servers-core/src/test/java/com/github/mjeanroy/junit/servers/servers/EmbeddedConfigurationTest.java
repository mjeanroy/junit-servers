package com.github.mjeanroy.junit.servers.servers;

import static org.assertj.core.api.Assertions.*;

import java.io.File;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class EmbeddedConfigurationTest {

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	private EmbeddedServerConfiguration configuration;

	@Before
	public void setUp() {
		configuration = new EmbeddedServerConfiguration();
	}

	@Test
	public void it_should_change_port() {
		int oldPort = configuration.getPort();
		int newPort = oldPort + 10;

		EmbeddedServerConfiguration result = configuration.withPort(newPort);

		assertThat(result).isSameAs(configuration);
		assertThat(result.getPort()).isNotEqualTo(oldPort).isEqualTo(newPort);
	}

	@Test
	public void it_should_change_path() {
		String oldPath = configuration.getPath();
		String newPath = oldPath + "foo";

		EmbeddedServerConfiguration result = configuration.withPath(newPath);

		assertThat(result).isSameAs(configuration);
		assertThat(result.getPath()).isNotEqualTo(oldPath).isEqualTo(newPath);
	}

	@Test
	public void it_should_change_webapp_path() {
		String oldWebapp = configuration.getWebapp();
		String newWebapp = oldWebapp + "foo";

		EmbeddedServerConfiguration result = configuration.withWebapp(newWebapp);

		assertThat(result).isSameAs(configuration);
		assertThat(result.getWebapp()).isNotEqualTo(oldWebapp).isEqualTo(newWebapp);
	}

	@Test
	public void it_should_change_webapp_path_with_file() throws Exception {
		String oldWebapp = configuration.getWebapp();
		File file = folder.newFile("foo");
		String newWebapp = file.getAbsolutePath();

		EmbeddedServerConfiguration result = configuration.withWebapp(file);

		assertThat(result).isSameAs(configuration);
		assertThat(result.getWebapp()).isNotEqualTo(oldWebapp).isEqualTo(newWebapp);
	}
}
