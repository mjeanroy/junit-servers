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

package com.github.mjeanroy.junit.servers.servers;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class EmbeddedConfigurationTest {

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	private AbstractEmbeddedServerConfiguration configuration;

	@Before
	public void setUp() {
		configuration = new EmbeddedConfiguration();
	}

	@Test
	public void it_should_change_port() {
		int oldPort = configuration.getPort();
		int newPort = oldPort + 10;

		AbstractEmbeddedServerConfiguration result = configuration.withPort(newPort);

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

		AbstractEmbeddedServerConfiguration result = configuration.withWebapp(newWebapp);

		assertThat(result).isSameAs(configuration);
		assertThat(result.getWebapp()).isNotEqualTo(oldWebapp).isEqualTo(newWebapp);
	}

	@Test
	public void it_should_change_webapp_path_with_file() throws Exception {
		String oldWebapp = configuration.getWebapp();
		File file = folder.newFile("foo");
		String newWebapp = file.getAbsolutePath();

		AbstractEmbeddedServerConfiguration result = configuration.withWebapp(file);

		assertThat(result).isSameAs(configuration);
		assertThat(result.getWebapp()).isNotEqualTo(oldWebapp).isEqualTo(newWebapp);
	}

	@Test
	public void it_should_add_property() throws Exception {
		Map<String, String> oldProperties = configuration.getEnvProperties();
		assertThat(oldProperties).isEmpty();

		String name = "foo";
		String value = "bar";
		AbstractEmbeddedServerConfiguration result = configuration.withProperty(name, value);

		assertThat(result).isSameAs(configuration);
		Map<String, String> newProperties = result.getEnvProperties();
		assertThat(newProperties)
				.isNotNull()
				.isNotEmpty()
				.hasSize(1)
				.containsOnly(entry(name, value));
	}

	@Test
	public void it_should_add_hook() throws Exception {
		Hook hook = mock(Hook.class);
		List<Hook> oldHooks = configuration.getHooks();
		assertThat(oldHooks).isEmpty();

		AbstractEmbeddedServerConfiguration result = configuration.withHook(hook);

		assertThat(result).isSameAs(configuration);
		List<Hook> newHooks = result.getHooks();
		assertThat(newHooks)
				.isNotNull()
				.isNotEmpty()
				.hasSize(1)
				.containsOnly(hook);
	}

	private static class EmbeddedConfiguration extends AbstractEmbeddedServerConfiguration<EmbeddedConfiguration> {
	}
}
