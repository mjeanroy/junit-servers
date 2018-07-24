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

package com.github.mjeanroy.junit.servers.jetty;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

public class EmbeddedJettyConfigurationBuilderTest {

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	private EmbeddedJettyConfiguration.Builder builder;

	@Before
	public void setUp() {
		builder = EmbeddedJettyConfiguration.builder();
	}

	@Test
	public void it_should_have_default_values() {
		assertThat(builder.getPath()).isEqualTo("/");
		assertThat(builder.getPort()).isZero();
		assertThat(builder.getClasspath()).isEqualTo(".");
	}

	@Test
	public void it_should_change_port() {
		int oldPort = builder.getPort();
		int newPort = oldPort + 10;

		EmbeddedJettyConfiguration.Builder result = builder.withPort(newPort);

		assertThat(result).isSameAs(builder);
		assertThat(result.getPort()).isNotEqualTo(oldPort).isEqualTo(newPort);
	}

	@Test
	public void it_should_change_path() {
		String oldPath = builder.getPath();
		String newPath = oldPath + "foo";

		EmbeddedJettyConfiguration.Builder result = builder.withPath(newPath);

		assertThat(result).isSameAs(builder);
		assertThat(result.getPath()).isNotEqualTo(oldPath).isEqualTo(newPath);
	}

	@Test
	public void it_should_change_webapp_path() {
		String oldWebapp = builder.getWebapp();
		String newWebapp = oldWebapp + "foo";

		EmbeddedJettyConfiguration.Builder result = builder.withWebapp(newWebapp);

		assertThat(result).isSameAs(builder);
		assertThat(result.getWebapp()).isNotEqualTo(oldWebapp).isEqualTo(newWebapp);
	}

	@Test
	public void it_should_change_webapp_path_with_file() throws Exception {
		String oldWebapp = builder.getWebapp();
		File file = folder.newFile("foo");
		String newWebapp = file.getAbsolutePath();

		EmbeddedJettyConfiguration.Builder result = builder.withWebapp(file);

		assertThat(result).isSameAs(builder);
		assertThat(result.getWebapp()).isNotEqualTo(oldWebapp).isEqualTo(newWebapp);
	}

	@Test
	public void it_should_change_classpath_entry() {
		String oldClasspath = builder.getClasspath();
		String newClasspath = oldClasspath + "foo";

		EmbeddedJettyConfiguration.Builder result = builder.withClasspath(newClasspath);

		assertThat(result).isSameAs(builder);
		assertThat(result.getClasspath()).isNotEqualTo(oldClasspath).isEqualTo(newClasspath);
	}

	@Test
	public void it_should_change_stop_timeout() {
		int oldStopTimeout = builder.getStopTimeout();
		int newStopTimeout = oldStopTimeout + 10;

		EmbeddedJettyConfiguration.Builder result = builder.withStopTimeout(newStopTimeout);

		assertThat(result).isSameAs(builder);
		assertThat(result.getStopTimeout()).isNotEqualTo(oldStopTimeout).isEqualTo(newStopTimeout);
	}

	@Test
	public void it_should_enable_stop_at_shutdown() {
		EmbeddedJettyConfiguration.Builder result = builder.enableStopAtShutdown();

		assertThat(result).isSameAs(builder);
		assertThat(result.isStopAtShutdown()).isTrue();
	}

	@Test
	public void it_should_disable_stop_at_shutdown() {
		EmbeddedJettyConfiguration.Builder result = builder.disableStopAtShutdown();

		assertThat(result).isSameAs(builder);
		assertThat(result.isStopAtShutdown()).isFalse();
	}
}
