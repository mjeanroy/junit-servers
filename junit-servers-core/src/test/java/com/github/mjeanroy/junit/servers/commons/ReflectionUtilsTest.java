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

package com.github.mjeanroy.junit.servers.commons;

import org.junit.Test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import static com.github.mjeanroy.junit.servers.commons.ReflectionUtils.findAllFields;
import static com.github.mjeanroy.junit.servers.commons.ReflectionUtils.findStaticFields;
import static com.github.mjeanroy.junit.servers.commons.ReflectionUtils.findStaticFieldsAnnotatedWith;
import static com.github.mjeanroy.junit.servers.commons.ReflectionUtils.findStaticMethods;
import static com.github.mjeanroy.junit.servers.commons.ReflectionUtils.findStaticMethodsAnnotatedWith;
import static com.github.mjeanroy.junit.servers.commons.ReflectionUtils.setter;
import static org.assertj.core.api.Assertions.assertThat;

public class ReflectionUtilsTest {

	@Test
	public void it_should_find_all_fields() {
		List<Field> fields = findAllFields(Bar.class);

		assertThat(fields)
				.isNotNull()
				.isNotEmpty()
				.hasSize(4);
	}

	@Test
	public void it_should_find_all_static_fields() {
		List<Field> fields = findStaticFields(Bar.class);

		assertThat(fields)
				.isNotNull()
				.isNotEmpty()
				.hasSize(2);
	}

	@Test
	public void it_should_find_all_static_fields_with_annotations() {
		List<Field> fields = findStaticFieldsAnnotatedWith(Bar.class, FooAnnotation.class);

		assertThat(fields)
				.isNotNull()
				.isNotEmpty()
				.hasSize(1);
	}

	@Test
	public void it_should_find_all_static_methods() {
		List<Method> methods = findStaticMethods(Bar.class);

		assertThat(methods)
				.isNotNull()
				.isNotEmpty();
	}

	@Test
	public void it_should_find_all_static_methods_with_annotations() {
		List<Method> methods = findStaticMethodsAnnotatedWith(Bar.class, FooAnnotation.class);

		assertThat(methods)
				.isNotNull()
				.isNotEmpty()
				.hasSize(1);
	}

	@Test
	public void it_should_set_value_on_private_field() {
		Bar bar = new Bar(1, "foo");
		String newValue = "bar";

		Field[] fields = Bar.class.getDeclaredFields();
		setter(bar, fields[2], newValue);

		assertThat(bar.name)
				.isNotNull()
				.isNotEmpty()
				.isEqualTo(newValue);
	}

	private static class Foo {
		private final int id;

		protected Foo(int id) {
			this.id = id;
		}
	}

	protected static class Bar extends Foo {
		private static String staticField = "foo";

		@FooAnnotation
		private static String staticFieldAnnotated = "foo";

		public static int getStaticMethod() {
			return 0;
		}

		@FooAnnotation
		public static int getStaticMethodAnnotated() {
			return 0;
		}

		private final String name;

		public Bar(int id, String name) {
			super(id);
			this.name = name;
		}
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.FIELD, ElementType.METHOD })
	public @interface FooAnnotation {

	}
}
