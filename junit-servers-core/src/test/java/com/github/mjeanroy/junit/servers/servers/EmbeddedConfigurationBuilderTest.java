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

import com.github.mjeanroy.junit.servers.servers.configuration.AbstractConfiguration;
import com.github.mjeanroy.junit.servers.servers.configuration.AbstractConfigurationBuilder;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.mockito.Mockito.mock;

public class EmbeddedConfigurationBuilderTest {

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	private EmbeddedConfigurationBuilder builder;

	@Before
	public void setUp() {
		builder = new EmbeddedConfigurationBuilder();
	}

	@Test
	public void it_should_change_port() {
		int oldPort = builder.getPort();
		int newPort = oldPort + 10;

		EmbeddedConfigurationBuilder result = builder.withPort(newPort);

		assertThat(result).isSameAs(builder);
		assertThat(result.getPort()).isNotEqualTo(oldPort).isEqualTo(newPort);
	}

	@Test
	public void it_should_change_path() {
		String oldPath = builder.getPath();
		String newPath = oldPath + "foo";

		EmbeddedConfigurationBuilder result = builder.withPath(newPath);

		assertThat(result).isSameAs(builder);
		assertThat(result.getPath()).isNotEqualTo(oldPath).isEqualTo(newPath);
	}

	@Test
	public void it_should_change_webapp_path() {
		String oldWebapp = builder.getWebapp();
		String newWebapp = oldWebapp + "foo";

		EmbeddedConfigurationBuilder result = builder.withWebapp(newWebapp);

		assertThat(result).isSameAs(builder);
		assertThat(result.getWebapp()).isNotEqualTo(oldWebapp).isEqualTo(newWebapp);
	}

	@Test
	public void it_should_change_webapp_path_with_file() throws Exception {
		String oldWebapp = builder.getWebapp();
		File file = folder.newFile("foo");
		String newWebapp = file.getAbsolutePath();

		EmbeddedConfigurationBuilder result = builder.withWebapp(file);

		assertThat(result).isSameAs(builder);
		assertThat(result.getWebapp()).isNotEqualTo(oldWebapp).isEqualTo(newWebapp);
	}

	@Test
	public void it_should_add_property() throws Exception {
		Map<String, String> oldProperties = builder.getEnvProperties();
		assertThat(oldProperties).isEmpty();

		String name = "foo";
		String value = "bar";
		EmbeddedConfigurationBuilder result = builder.withProperty(name, value);

		assertThat(result).isSameAs(builder);
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
		List<Hook> oldHooks = builder.getHooks();
		assertThat(oldHooks).isEmpty();

		EmbeddedConfigurationBuilder result = builder.withHook(hook);

		assertThat(result).isSameAs(builder);
		List<Hook> newHooks = result.getHooks();
		assertThat(newHooks)
				.isNotNull()
				.isNotEmpty()
				.hasSize(1)
				.containsOnly(hook);
	}

	private static class EmbeddedConfiguration extends AbstractConfiguration {

		public EmbeddedConfiguration(EmbeddedConfigurationBuilder builder) {
			super(builder);
		}
	}

	public static class EmbeddedConfigurationBuilder extends AbstractConfigurationBuilder<EmbeddedConfigurationBuilder, EmbeddedConfiguration> {
		@Override
		protected EmbeddedConfigurationBuilder self() {
			return this;
		}

		@Override
		public EmbeddedConfiguration build() {
			return new EmbeddedConfiguration(this);
		}
	}
}
