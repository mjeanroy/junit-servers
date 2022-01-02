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

package com.github.mjeanroy.junit.servers.servers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.entry;
import static org.mockito.Mockito.mock;

class EmbeddedConfigurationBuilderTest {

	@Test
	void it_should_change_port() {
		final EmbeddedConfigurationBuilder builder = createBuilder();
		final int oldPort = builder.getPort();
		final int newPort = oldPort + 10;

		final EmbeddedConfigurationBuilder result = builder.withPort(newPort);

		assertThat(result).isSameAs(builder);
		assertThat(result.getPort()).isNotEqualTo(oldPort).isEqualTo(newPort);
	}

	@Test
	void it_should_change_path() {
		final EmbeddedConfigurationBuilder builder = createBuilder();
		final String oldPath = builder.getPath();
		final String newPath = oldPath + "foo";

		final EmbeddedConfigurationBuilder result = builder.withPath(newPath);

		assertThat(result).isSameAs(builder);
		assertThat(result.getPath()).isNotEqualTo(oldPath).isEqualTo(newPath);
	}

	@Test
	void it_should_change_webapp_path() {
		final EmbeddedConfigurationBuilder builder = createBuilder();
		final String oldWebapp = builder.getWebapp();
		final String newWebapp = oldWebapp + "foo";

		final EmbeddedConfigurationBuilder result = builder.withWebapp(newWebapp);

		assertThat(result).isSameAs(builder);
		assertThat(result.getWebapp()).isNotEqualTo(oldWebapp).isEqualTo(newWebapp);
	}

	@Test
	void it_should_change_webapp_path_with_file(@TempDir File tempDir) {
		final EmbeddedConfigurationBuilder builder = createBuilder();
		final String oldWebapp = builder.getWebapp();
		final String newWebapp = tempDir.getAbsolutePath();

		final EmbeddedConfigurationBuilder result = builder.withWebapp(tempDir);

		assertThat(result).isSameAs(builder);
		assertThat(result.getWebapp()).isNotEqualTo(oldWebapp).isEqualTo(newWebapp);
	}

	@Test
	void it_should_override_descriptor_file() {
		final EmbeddedConfigurationBuilder builder = createBuilder();
		final String newDescriptor = "src/test/resources/web.xml";
		final String oldDescriptor = builder.getOverrideDescriptor();
		final EmbeddedConfigurationBuilder result = builder.withOverrideDescriptor(newDescriptor);

		assertThat(result).isSameAs(builder);
		assertThat(result.getOverrideDescriptor()).isNotEqualTo(oldDescriptor).isEqualTo(newDescriptor);
	}

	@Test
	void it_should_fail_to_override_classloader_with_null_class() {
		final EmbeddedConfigurationBuilder builder = createBuilder();
		final Class<?> klass = null;

		assertThatThrownBy(() -> builder.withParentClassLoader(klass))
			.isExactlyInstanceOf(NullPointerException.class)
			.hasMessage("Base class must not be null");
	}

	@Test
	void it_should_add_property() {
		final EmbeddedConfigurationBuilder builder = createBuilder();
		final Map<String, String> oldProperties = builder.getEnvProperties();
		assertThat(oldProperties).isEmpty();

		final String name = "foo";
		final String value = "bar";
		final EmbeddedConfigurationBuilder result = builder.withProperty(name, value);
		assertThat(result).isSameAs(builder);

		final Map<String, String> newProperties = result.getEnvProperties();
		assertThat(newProperties).hasSize(1).containsOnly(entry(name, value));
	}

	@Test
	void it_should_add_hook() {
		final Hook hook = mock(Hook.class);
		final EmbeddedConfigurationBuilder builder = createBuilder();
		final List<Hook> oldHooks = builder.getHooks();
		assertThat(oldHooks).isEmpty();

		final EmbeddedConfigurationBuilder result = builder.withHook(hook);
		assertThat(result).isSameAs(builder);

		final List<Hook> newHooks = result.getHooks();
		assertThat(newHooks).hasSize(1).containsOnly(hook);
	}

	private static EmbeddedConfigurationBuilder createBuilder() {
		return new EmbeddedConfigurationBuilder();
	}
}
