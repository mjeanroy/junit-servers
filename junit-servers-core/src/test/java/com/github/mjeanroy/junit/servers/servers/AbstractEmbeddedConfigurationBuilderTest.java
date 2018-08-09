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

package com.github.mjeanroy.junit.servers.servers;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
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
import static org.mockito.Mockito.mock;

public abstract class AbstractEmbeddedConfigurationBuilderTest<T extends AbstractConfigurationBuilder<T, U>, U extends AbstractConfiguration> {

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	@Test
	public void it_should_change_port() {
		final T builder = createBuilder();
		final int oldPort = builder.getPort();
		final int newPort = oldPort + 10;

		final T result = builder.withPort(newPort);

		assertThat(result).isSameAs(builder);
		assertThat(result.getPort()).isNotEqualTo(oldPort).isEqualTo(newPort);
	}

	@Test
	public void it_should_change_path() {
		final T builder = createBuilder();
		final String oldPath = builder.getPath();
		final String newPath = oldPath + "foo";

		final T result = builder.withPath(newPath);

		assertThat(result).isSameAs(builder);
		assertThat(result.getPath()).isNotEqualTo(oldPath).isEqualTo(newPath);
	}

	@Test
	public void it_should_change_webapp_path() {
		final T builder = createBuilder();
		final String oldWebapp = builder.getWebapp();
		final String newWebapp = oldWebapp + "foo";

		final T result = builder.withWebapp(newWebapp);

		assertThat(result).isSameAs(builder);
		assertThat(result.getWebapp()).isNotEqualTo(oldWebapp).isEqualTo(newWebapp);
	}

	@Test
	public void it_should_change_webapp_path_with_file() throws Exception {
		final T builder = createBuilder();
		final String oldWebapp = builder.getWebapp();
		final File file = folder.newFile("foo");
		final String newWebapp = file.getAbsolutePath();

		final T result = builder.withWebapp(file);

		assertThat(result).isSameAs(builder);
		assertThat(result.getWebapp()).isNotEqualTo(oldWebapp).isEqualTo(newWebapp);
	}

	@Test
	public void it_should_override_descriptor_file() {
		final T builder = createBuilder();
		final String newDescriptor = "src/test/resources/web.xml";
		final String oldDescriptor = builder.getOverrideDescriptor();
		final T result = builder.withOverrideDescriptor(newDescriptor);

		assertThat(result).isSameAs(builder);
		assertThat(result.getOverrideDescriptor()).isNotEqualTo(oldDescriptor).isEqualTo(newDescriptor);
	}

	@Test
	@SuppressWarnings("deprecation")
	public void it_should_override_parent_classpath() throws Exception {
		final T builder = createBuilder();
		final URL c1 = new File("/").toURI().toURL();
		final URL c2 = new File(".").toURI().toURL();

		final T result = builder.withParentClasspath(c1, c2);

		assertThat(result).isSameAs(builder);
		assertThat(result.getParentClassLoader()).isNotNull();
		assertThat(result.getParentClasspath()).isNotNull();
	}

	@Test
	@SuppressWarnings("deprecation")
	public void it_should_override_parent_classpath_with_collection() throws Exception {
		final T builder = createBuilder();
		final URL c1 = new File("/").toURI().toURL();
		final URL c2 = new File(".").toURI().toURL();
		final List<URL> urls = Arrays.asList(c1, c2);

		final T result = builder.withParentClasspath(urls);

		assertThat(result).isSameAs(builder);
		assertThat(result.getParentClassLoader()).isNotNull();
		assertThat(result.getParentClasspath()).isNotNull();
	}

	@Test
	@SuppressWarnings("deprecation")
	public void it_should_override_class_classloader() {
		final T builder = createBuilder();
		final Class<?> klass = getClass();
		final FileFilter filter = new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return true;
			}
		};

		final T result = builder.withParentClasspath(klass, filter);

		assertThat(result).isSameAs(builder);
		assertThat(result.getParentClassLoader()).isNotNull();
		assertThat(result.getParentClassLoader().getResource("logback-test.xml")).isNotNull();
	}

	@Test
	@SuppressWarnings("deprecation")
	public void it_should_override_class_classloader_without_filter() {
		final Class<?> klass = getClass();
		final T builder = createBuilder();
		final T result = builder.withParentClasspath(klass);

		assertThat(result).isSameAs(builder);
		assertThat(result.getParentClassLoader()).isNotNull();
		assertThat(result.getParentClassLoader().getResource("logback-test.xml")).isNotNull();
	}

	@Test
	public void it_should_fail_to_override_classloader_with_null_class() {
		final T builder = createBuilder();
		final Class<?> klass = null;

		assertThatThrownBy(withParentClassLoader(builder, klass))
			.isExactlyInstanceOf(NullPointerException.class)
			.hasMessage("Base class must not be null");
	}

	@Test
	public void it_should_fail_to_override_classpath_with_null_class() {
		final T builder = createBuilder();
		final Class<?> klass = null;

		assertThatThrownBy(withParentClassPath(builder, klass))
			.isExactlyInstanceOf(NullPointerException.class)
			.hasMessage("Base class must not be null");
	}

	@Test
	public void it_should_fail_to_override_classpath_with_null_class_and_filter() {
		final T builder = createBuilder();
		final Class<?> cls = null;
		final FileFilter filter = new FileFilter() {
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
		final T builder = createBuilder();
		final Map<String, String> oldProperties = builder.getEnvProperties();
		assertThat(oldProperties).isEmpty();

		final String name = "foo";
		final String value = "bar";
		final T result = builder.withProperty(name, value);
		assertThat(result).isSameAs(builder);

		final Map<String, String> newProperties = result.getEnvProperties();
		assertThat(newProperties).hasSize(1).containsOnly(entry(name, value));
	}

	@Test
	public void it_should_add_hook() {
		final Hook hook = mock(Hook.class);
		final T builder = createBuilder();
		final List<Hook> oldHooks = builder.getHooks();
		assertThat(oldHooks).isEmpty();

		final T result = builder.withHook(hook);
		assertThat(result).isSameAs(builder);

		final List<Hook> newHooks = result.getHooks();
		assertThat(newHooks).hasSize(1).containsOnly(hook);
	}

	protected abstract T createBuilder();

	private ThrowingCallable withParentClassLoader(final T builder, final Class<?> klass) {
		return new ThrowingCallable() {
			@Override
			public void call() {
				builder.withParentClassLoader(klass);
			}
		};
	}

	private ThrowingCallable withParentClassPath(final T builder, final Class<?> klass) {
		return new ThrowingCallable() {
			@Override
			@SuppressWarnings("deprecation")
			public void call() {
				builder.withParentClasspath(klass);
			}
		};
	}

	private ThrowingCallable withParentClassPath(final T builder, final Class<?> klass, final FileFilter fileFilter) {
		return new ThrowingCallable() {
			@Override
			@SuppressWarnings("deprecation")
			public void call() {
				builder.withParentClasspath(klass, fileFilter);
			}
		};
	}
}
