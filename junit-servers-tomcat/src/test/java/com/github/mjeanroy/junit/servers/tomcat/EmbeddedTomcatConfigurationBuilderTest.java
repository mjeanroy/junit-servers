/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2022 <mickael.jeanroy@gmail.com>
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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Files;

import static org.assertj.core.api.Assertions.assertThat;

class EmbeddedTomcatConfigurationBuilderTest {

	private EmbeddedTomcatConfiguration.Builder builder;

	@BeforeEach
	void setUp() {
		builder = EmbeddedTomcatConfiguration.builder();
	}

	@Test
	void it_should_have_default_values() {
		assertThat(builder.getPath()).isEqualTo("/");
		assertThat(builder.getPort()).isZero();
		assertThat(builder.isEnableNaming()).isTrue();
		assertThat(builder.isForceMetaInf()).isTrue();
		assertThat(builder.getClasspath()).isEqualTo("./target/classes");
		assertThat(builder.getBaseDir()).isEqualTo("./tomcat-work");
		assertThat(builder.isKeepBaseDir()).isFalse();
	}

	@Test
	void it_should_change_port() {
		final int oldPort = builder.getPort();
		final int newPort = oldPort + 10;

		final EmbeddedTomcatConfiguration.Builder result = builder.withPort(newPort);

		assertThat(result).isSameAs(builder);
		assertThat(result.getPort()).isNotEqualTo(oldPort).isEqualTo(newPort);
	}

	@Test
	void it_should_change_path() {
		final String oldPath = builder.getPath();
		final String newPath = oldPath + "foo";

		final EmbeddedTomcatConfiguration.Builder result = builder.withPath(newPath);

		assertThat(result).isSameAs(builder);
		assertThat(result.getPath()).isNotEqualTo(oldPath).isEqualTo(newPath);
	}

	@Test
	void it_should_change_webapp_path() {
		final String oldWebapp = builder.getWebapp();
		final String newWebapp = oldWebapp + "foo";

		final EmbeddedTomcatConfiguration.Builder result = builder.withWebapp(newWebapp);

		assertThat(result).isSameAs(builder);
		assertThat(result.getWebapp()).isNotEqualTo(oldWebapp).isEqualTo(newWebapp);
	}

	@Test
	void it_should_change_webapp_path_with_file(@TempDir File dir) throws Exception {
		final String oldWebapp = builder.getWebapp();
		final File file = Files.createTempFile(dir.toPath(), null, null).toFile();
		final String newWebapp = file.getAbsolutePath();

		final EmbeddedTomcatConfiguration.Builder result = builder.withWebapp(file);

		assertThat(result).isSameAs(builder);
		assertThat(result.getWebapp()).isNotEqualTo(oldWebapp).isEqualTo(newWebapp);
	}

	@Test
	void it_should_change_classpath_entry() {
		final String oldClasspath = builder.getClasspath();
		final String newClasspath = oldClasspath + "foo";

		final EmbeddedTomcatConfiguration.Builder result = builder.withClasspath(newClasspath);

		assertThat(result).isSameAs(builder);
		assertThat(result.getClasspath()).isNotEqualTo(oldClasspath).isEqualTo(newClasspath);
	}

	@Test
	void it_should_change_base_dir() {
		final String oldBaseDir = builder.getBaseDir();
		final String newBaseDir = oldBaseDir + "foo";

		final EmbeddedTomcatConfiguration.Builder result = builder.withBaseDir(newBaseDir);

		assertThat(result).isSameAs(builder);
		assertThat(result.getBaseDir()).isNotEqualTo(oldBaseDir).isEqualTo(newBaseDir);
	}

	@Test
	void it_should_keep_base_dir() {
		final EmbeddedTomcatConfiguration.Builder result = builder.keepBaseDir();

		assertThat(result).isSameAs(builder);
		assertThat(result.isKeepBaseDir()).isTrue();
	}

	@Test
	void it_should_delete_base_dir() {
		final EmbeddedTomcatConfiguration.Builder result = builder.deleteBaseDir();

		assertThat(result).isSameAs(builder);
		assertThat(result.isKeepBaseDir()).isFalse();
	}

	@Test
	void it_should_enable_naming() {
		final EmbeddedTomcatConfiguration.Builder result = builder.enableNaming();

		assertThat(result).isSameAs(builder);
		assertThat(result.isEnableNaming()).isTrue();
	}

	@Test
	void it_should_disable_naming() {
		final EmbeddedTomcatConfiguration.Builder result = builder.disableNaming();

		assertThat(result).isSameAs(builder);
		assertThat(result.isEnableNaming()).isFalse();
	}

	@Test
	void it_should_enable_metaInf_creation() {
		final EmbeddedTomcatConfiguration.Builder result = builder.enableForceMetaInf();

		assertThat(result).isSameAs(builder);
		assertThat(result.isForceMetaInf()).isTrue();
	}

	@Test
	void it_should_disable_metaInf_creation() {
		final EmbeddedTomcatConfiguration.Builder result = builder.disableForceMetaInf();

		assertThat(result).isSameAs(builder);
		assertThat(result.isForceMetaInf()).isFalse();
	}
}
