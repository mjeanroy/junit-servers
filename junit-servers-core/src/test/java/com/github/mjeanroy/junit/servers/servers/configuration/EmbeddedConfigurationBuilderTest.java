/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2017 <mickael.jeanroy@gmail.com>
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

package com.github.mjeanroy.junit.servers.servers.configuration;

import com.github.mjeanroy.junit.servers.servers.Hook;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileFilter;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.entry;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
	public void it_should_override_descriptor_file() {
		String newDescriptor = "src/test/resources/web.xml";
		String oldDescriptor = builder.getOverrideDescriptor();
		EmbeddedConfigurationBuilder result = builder.withOverrideDescriptor(newDescriptor);

		assertThat(result).isSameAs(builder);
		assertThat(result.getOverrideDescriptor()).isNotEqualTo(oldDescriptor).isEqualTo(newDescriptor);
	}

	@Test
	@SuppressWarnings("deprecation")
	public void it_should_override_parent_classpath() throws Exception {
		URL c1 = new File("/").toURI().toURL();
		URL c2 = new File(".").toURI().toURL();

		EmbeddedConfigurationBuilder result = builder.withParentClasspath(c1, c2);

		assertThat(result).isSameAs(builder);
		assertThat(result.getParentClassLoader()).isNotNull();
		assertThat(result.getParentClasspath()).isNotNull();
	}

	@Test
	@SuppressWarnings("deprecation")
	public void it_should_override_parent_classpath_with_collection() throws Exception {
		URL c1 = new File("/").toURI().toURL();
		URL c2 = new File(".").toURI().toURL();
		List<URL> urls = Arrays.asList(c1, c2);

		EmbeddedConfigurationBuilder result = builder.withParentClasspath(urls);

		assertThat(result).isSameAs(builder);
		assertThat(result.getParentClassLoader()).isNotNull();
		assertThat(result.getParentClasspath()).isNotNull();
	}

	@Test
	@SuppressWarnings("deprecation")
	public void it_should_override_class_classloader() {
		Class<?> klass = getClass();
		FileFilter filter = mock(FileFilter.class);
		when(filter.accept(any(File.class))).thenReturn(true);

		EmbeddedConfigurationBuilder result = builder.withParentClasspath(klass, filter);

		assertThat(result).isSameAs(builder);
		assertThat(result.getParentClassLoader()).isNotNull();
		assertThat(result.getParentClassLoader().getResource("logback-test.xml")).isNotNull();

		assertThat(result.getParentClasspath()).isNotNull();
		assertThat(result.getParentClasspath().getResource("logback-test.xml")).isNotNull();
	}

	@Test
	@SuppressWarnings("deprecation")
	public void it_should_override_class_classloader_without_filter() {
		Class<?> klass = getClass();
		EmbeddedConfigurationBuilder result = builder.withParentClasspath(klass);

		assertThat(result).isSameAs(builder);
		assertThat(result.getParentClassLoader()).isNotNull();
		assertThat(result.getParentClassLoader().getResource("logback-test.xml")).isNotNull();

		assertThat(result.getParentClasspath()).isNotNull();
		assertThat(result.getParentClasspath().getResource("logback-test.xml")).isNotNull();
	}

	@Test
	public void it_should_fail_to_override_classloader_with_null_class() {
		assertThatThrownBy(withParentClassLoader(builder, null))
			.isExactlyInstanceOf(NullPointerException.class)
			.hasMessage("Base class must not be null");
	}

	@Test
	public void it_should_fail_to_override_classpath_with_null_class() {
		assertThatThrownBy(withParentClassPath(builder, null))
			.isExactlyInstanceOf(NullPointerException.class)
			.hasMessage("Base class must not be null");
	}

	@Test
	public void it_should_fail_to_override_classpath_with_null_class_and_filter() {
		Class<?> cls = null;
		FileFilter filter = new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return true;
			}
		};

		assertThatThrownBy(withParentClassPath(builder, cls, filter))
			.isExactlyInstanceOf(NullPointerException.class)
			.hasMessage("Base class must not be null");
	}

	@Test
	public void it_should_add_property() {
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
	public void it_should_add_hook() {
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
		EmbeddedConfiguration(EmbeddedConfigurationBuilder builder) {
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

	private static ThrowingCallable withParentClassLoader(final EmbeddedConfigurationBuilder builder, final Class<?> klass) {
		return new ThrowingCallable() {
			@Override
			public void call() {
				builder.withParentClassLoader(klass);
			}
		};
	}

	private static ThrowingCallable withParentClassPath(final EmbeddedConfigurationBuilder builder, final Class<?> klass) {
		return new ThrowingCallable() {
			@Override
			@SuppressWarnings("deprecation")
			public void call() {
				builder.withParentClasspath(klass);
			}
		};
	}

	private static ThrowingCallable withParentClassPath(final EmbeddedConfigurationBuilder builder, final Class<?> klass, final FileFilter fileFilter) {
		return new ThrowingCallable() {
			@Override
			@SuppressWarnings("deprecation")
			public void call() {
				builder.withParentClasspath(klass, fileFilter);
			}
		};
	}
}
