/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2019 <mickael.jeanroy@gmail.com>
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

package com.github.mjeanroy.junit.servers.commons;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.Test;

import java.net.URL;
import java.net.URLClassLoader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class CompositeClassLoaderTest {

	@Test
	public void it_should_load_resource_from_primary_or_fallback_class_loader() {
		String name1 = "file1.txt";
		URL file1 = getClass().getResource("/" + name1);

		String name2 = "file2.txt";
		URL file2 = getClass().getResource("/" + name2);

		ClassLoader cl1 = new URLClassLoader(new URL[] { file1 });
		ClassLoader cl2 = new URLClassLoader(new URL[] { file2 });

		ClassLoader cl = new CompositeClassLoader(cl1, cl2);

		assertThat(cl.getResource(name1)).isEqualTo(file1);
		assertThat(cl.getResource(name2)).isEqualTo(file2);
		assertThat(cl.getResource("/file3.txt")).isNull();
	}

	@Test
	public void it_should_load_class_from_primary_or_fallback_class_loader() throws Exception {
		String class1 = "foo";
		String class2 = "boo";

		ClassLoader cl1 = new FakeClassLoader(class1, Foo.class);
		ClassLoader cl2 = new FakeClassLoader(class2, Bar.class);

		ClassLoader cl = new CompositeClassLoader(cl1, cl2);

		assertThat(cl.loadClass(class1)).isEqualTo(Foo.class);
		assertThat(cl.loadClass(class2)).isEqualTo(Bar.class);
		assertThatThrownBy(loadClass(cl, "fake")).isExactlyInstanceOf(ClassNotFoundException.class);
	}

	private static class Foo {
	}

	private static class Bar {
	}

	private static class FakeClassLoader extends ClassLoader {
		private final String name;
		private final Class<?> klass;

		private FakeClassLoader(String name, Class<?> klass) {
			this.name = name;
			this.klass = klass;
		}

		@Override
		protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
			if (this.name.equals(name)) {
				return klass;
			}

			throw new ClassNotFoundException();
		}
	}

	private static ThrowingCallable loadClass(final ClassLoader classLoader, final String name) {
		return new ThrowingCallable() {
			@Override
			public void call() throws Throwable {
				classLoader.loadClass(name);
			}
		};
	}
}
