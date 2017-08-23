package com.github.mjeanroy.junit.servers.commons;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.net.URL;
import java.net.URLClassLoader;

import static org.assertj.core.api.Assertions.assertThat;

public class CompositeClassLoaderTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void it_should_load_resource_from_primary_or_fallback_class_loader() {
		String name1 = "file1.txt";
		URL file1 = getClass().getResource("/" + name1);

		String name2 = "file2.txt";
		URL file2 = getClass().getResource("/" + name2);

		ClassLoader cl1 = new URLClassLoader(new URL[] {file1});
		ClassLoader cl2 = new URLClassLoader(new URL[] {file2});

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

		thrown.expect(ClassNotFoundException.class);
		cl.loadClass("fake");
	}

	private static class Foo {}
	private static class Bar {}

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
}
