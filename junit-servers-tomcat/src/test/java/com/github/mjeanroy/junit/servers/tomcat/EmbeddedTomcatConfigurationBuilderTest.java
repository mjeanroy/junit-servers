/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2018 <mickael.jeanroy@gmail.com>
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

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

public class EmbeddedTomcatConfigurationBuilderTest {

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	private EmbeddedTomcatConfiguration.Builder builder;

	@Before
	public void setUp() {
		builder = EmbeddedTomcatConfiguration.builder();
	}

	@Test
	public void it_should_have_default_values() {
		assertThat(builder.getPath()).isEqualTo("/");
		assertThat(builder.getPort()).isZero();
		assertThat(builder.isEnableNaming()).isTrue();
		assertThat(builder.isForceMetaInf()).isTrue();
		assertThat(builder.getClasspath()).isEqualTo("./target/classes");
		assertThat(builder.getBaseDir()).isEqualTo("./tomcat-work");
		assertThat(builder.isKeepBaseDir()).isFalse();
	}

	@Test
	public void it_should_change_port() {
		int oldPort = builder.getPort();
		int newPort = oldPort + 10;

		EmbeddedTomcatConfiguration.Builder result = builder.withPort(newPort);

		assertThat(result).isSameAs(builder);
		assertThat(result.getPort()).isNotEqualTo(oldPort).isEqualTo(newPort);
	}

	@Test
	public void it_should_change_path() {
		String oldPath = builder.getPath();
		String newPath = oldPath + "foo";

		EmbeddedTomcatConfiguration.Builder result = builder.withPath(newPath);

		assertThat(result).isSameAs(builder);
		assertThat(result.getPath()).isNotEqualTo(oldPath).isEqualTo(newPath);
	}

	@Test
	public void it_should_change_webapp_path() {
		String oldWebapp = builder.getWebapp();
		String newWebapp = oldWebapp + "foo";

		EmbeddedTomcatConfiguration.Builder result = builder.withWebapp(newWebapp);

		assertThat(result).isSameAs(builder);
		assertThat(result.getWebapp()).isNotEqualTo(oldWebapp).isEqualTo(newWebapp);
	}

	@Test
	public void it_should_change_webapp_path_with_file() throws Exception {
		String oldWebapp = builder.getWebapp();
		File file = folder.newFile("foo");
		String newWebapp = file.getAbsolutePath();

		EmbeddedTomcatConfiguration.Builder result = builder.withWebapp(file);

		assertThat(result).isSameAs(builder);
		assertThat(result.getWebapp()).isNotEqualTo(oldWebapp).isEqualTo(newWebapp);
	}

	@Test
	public void it_should_change_classpath_entry() {
		String oldClasspath = builder.getClasspath();
		String newClasspath = oldClasspath + "foo";

		EmbeddedTomcatConfiguration.Builder result = builder.withClasspath(newClasspath);

		assertThat(result).isSameAs(builder);
		assertThat(result.getClasspath()).isNotEqualTo(oldClasspath).isEqualTo(newClasspath);
	}

	@Test
	public void it_should_change_base_dir() {
		String oldBaseDir = builder.getBaseDir();
		String newBaseDir = oldBaseDir + "foo";

		EmbeddedTomcatConfiguration.Builder result = builder.withBaseDir(newBaseDir);

		assertThat(result).isSameAs(builder);
		assertThat(result.getBaseDir()).isNotEqualTo(oldBaseDir).isEqualTo(newBaseDir);
	}

	@Test
	public void it_should_keep_base_dir() {
		EmbeddedTomcatConfiguration.Builder result = builder.keepBaseDir();

		assertThat(result).isSameAs(builder);
		assertThat(result.isKeepBaseDir()).isTrue();
	}

	@Test
	public void it_should_delete_base_dir() {
		EmbeddedTomcatConfiguration.Builder result = builder.deleteBaseDir();

		assertThat(result).isSameAs(builder);
		assertThat(result.isKeepBaseDir()).isFalse();
	}

	@Test
	public void it_should_enable_naming() {
		EmbeddedTomcatConfiguration.Builder result = builder.enableNaming();

		assertThat(result).isSameAs(builder);
		assertThat(result.isEnableNaming()).isTrue();
	}

	@Test
	public void it_should_disable_naming() {
		EmbeddedTomcatConfiguration.Builder result = builder.disableNaming();

		assertThat(result).isSameAs(builder);
		assertThat(result.isEnableNaming()).isFalse();
	}

	@Test
	public void it_should_enable_metaInf_creation() {
		EmbeddedTomcatConfiguration.Builder result = builder.enableForceMetaInf();

		assertThat(result).isSameAs(builder);
		assertThat(result.isForceMetaInf()).isTrue();
	}

	@Test
	public void it_should_disable_metaInf_creation() {
		EmbeddedTomcatConfiguration.Builder result = builder.disableForceMetaInf();

		assertThat(result).isSameAs(builder);
		assertThat(result.isForceMetaInf()).isFalse();
	}
}
