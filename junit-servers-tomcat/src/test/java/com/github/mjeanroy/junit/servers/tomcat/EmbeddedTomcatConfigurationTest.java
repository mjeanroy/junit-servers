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

package com.github.mjeanroy.junit.servers.tomcat;

import static org.assertj.core.api.Assertions.*;

import java.io.File;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.github.mjeanroy.junit.servers.servers.AbstractEmbeddedServerConfiguration;

public class EmbeddedTomcatConfigurationTest {

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	private EmbeddedTomcatConfiguration configuration;

	@Before
	public void setUp() {
		configuration = new EmbeddedTomcatConfiguration();
	}

	@Test
	public void it_should_have_default_values() {
		assertThat(configuration.getPath()).isEqualTo("/");
		assertThat(configuration.getPort()).isZero();
		assertThat(configuration.getEnableNaming()).isTrue();
		assertThat(configuration.getForceMetaInf()).isTrue();
		assertThat(configuration.getClasspath()).isEqualTo("./target/classes");
		assertThat(configuration.getBaseDir()).isEqualTo("./tomcat-work");
	}

	@Test
	public void it_should_change_port() {
		int oldPort = configuration.getPort();
		int newPort = oldPort + 10;

		EmbeddedTomcatConfiguration result = configuration.withPort(newPort);

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

		EmbeddedTomcatConfiguration result = configuration.withWebapp(newWebapp);

		assertThat(result).isSameAs(configuration);
		assertThat(result.getWebapp()).isNotEqualTo(oldWebapp).isEqualTo(newWebapp);
	}

	@Test
	public void it_should_change_webapp_path_with_file() throws Exception {
		String oldWebapp = configuration.getWebapp();
		File file = folder.newFile("foo");
		String newWebapp = file.getAbsolutePath();

		EmbeddedTomcatConfiguration result = configuration.withWebapp(file);

		assertThat(result).isSameAs(configuration);
		assertThat(result.getWebapp()).isNotEqualTo(oldWebapp).isEqualTo(newWebapp);
	}

	@Test
	public void it_should_change_classpath_entry() {
		String oldClasspath = configuration.getClasspath();
		String newClasspath = oldClasspath + "foo";

		EmbeddedTomcatConfiguration result = configuration.withClasspath(newClasspath);

		assertThat(result).isSameAs(configuration);
		assertThat(result.getClasspath()).isNotEqualTo(oldClasspath).isEqualTo(newClasspath);
	}

	@Test
	public void it_should_change_base_dir() {
		String oldBaseDir = configuration.getBaseDir();
		String newBaseDir = oldBaseDir + "foo";

		EmbeddedTomcatConfiguration result = configuration.withBaseDir(newBaseDir);

		assertThat(result).isSameAs(configuration);
		assertThat(result.getBaseDir()).isNotEqualTo(oldBaseDir).isEqualTo(newBaseDir);
	}

	@Test
	public void it_should_enable_naming() {
		EmbeddedTomcatConfiguration result = configuration.enableNaming();

		assertThat(result).isSameAs(configuration);
		assertThat(result.getEnableNaming()).isTrue();
	}

	@Test
	public void it_should_disable_naming() {
		EmbeddedTomcatConfiguration result = configuration.disableNaming();

		assertThat(result).isSameAs(configuration);
		assertThat(result.getEnableNaming()).isFalse();
	}

	@Test
	public void it_should_enable_metaInf_creation() {
		EmbeddedTomcatConfiguration result = configuration.enableForceMetaInf();

		assertThat(result).isSameAs(configuration);
		assertThat(result.getForceMetaInf()).isTrue();
	}

	@Test
	public void it_should_disable_metaInf_creation() {
		EmbeddedTomcatConfiguration result = configuration.disableForceMetaInf();

		assertThat(result).isSameAs(configuration);
		assertThat(result.getForceMetaInf()).isFalse();
	}
}
