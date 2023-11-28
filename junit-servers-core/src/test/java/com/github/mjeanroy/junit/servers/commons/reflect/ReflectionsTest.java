/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2023 <mickael.jeanroy@gmail.com>
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

package com.github.mjeanroy.junit.servers.commons.reflect;

import com.github.mjeanroy.junit.servers.commons.fixtures.Bar;
import com.github.mjeanroy.junit.servers.commons.fixtures.FooAnnotation;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import static com.github.mjeanroy.junit.servers.commons.reflect.Reflections.findAllFields;
import static com.github.mjeanroy.junit.servers.commons.reflect.Reflections.findStaticFieldsAnnotatedWith;
import static com.github.mjeanroy.junit.servers.commons.reflect.Reflections.findStaticMethodsAnnotatedWith;
import static com.github.mjeanroy.junit.servers.commons.reflect.Reflections.getter;
import static com.github.mjeanroy.junit.servers.commons.reflect.Reflections.invoke;
import static com.github.mjeanroy.junit.servers.commons.reflect.Reflections.setter;
import static com.github.mjeanroy.junit.servers.testing.ReflectionTestUtils.getPrivateField;
import static com.github.mjeanroy.junit.servers.testing.ReflectionTestUtils.getPrivateMethod;
import static org.assertj.core.api.Assertions.assertThat;

class ReflectionsTest {

	@Test
	void it_should_find_all_fields() {
		List<Field> fields = findAllFields(Bar.class);
		assertThat(fields).hasSize(4);
	}

	@Test
	void it_should_find_all_static_fields_with_annotations() {
		List<Field> fields = findStaticFieldsAnnotatedWith(Bar.class, FooAnnotation.class);
		assertThat(fields).hasSize(1);
	}

	@Test
	void it_should_find_all_static_methods_with_annotations() {
		List<Method> methods = findStaticMethodsAnnotatedWith(Bar.class, FooAnnotation.class);
		assertThat(methods).hasSize(1);
	}

	@Test
	void it_should_set_value_on_private_field() {
		Bar bar = new Bar(1, "foo");
		String newValue = "bar";

		Field field = getPrivateField(Bar.class, "name");

		assertThat(field.isAccessible()).isFalse();
		assertThat(bar.getName()).isEqualTo("foo");

		setter(bar, field, newValue);

		assertThat(field.isAccessible()).isFalse();
		assertThat(bar.getName()).isEqualTo(newValue);
	}

	@Test
	void it_should_get_value_on_private_field() {
		String actualValue = "foo";
		Bar bar = new Bar(1, actualValue);
		Field field = getPrivateField(Bar.class, "name");

		assertThat(field.isAccessible()).isFalse();

		String value = getter(bar, field);

		assertThat(field.isAccessible()).isFalse();
		assertThat(value).isEqualTo(actualValue);
	}

	@Test
	void it_should_get_value_on_static_private_field() {
		Field field = getPrivateField(Bar.class, "staticField");

		assertThat(field.isAccessible()).isFalse();

		String value = getter(field);

		assertThat(field.isAccessible()).isFalse();
		assertThat(value).isEqualTo("foo");
	}

	@Test
	void it_should_get_value_on_static_private_method() {
		Method method = getPrivateMethod(Bar.class, "getStaticPrivateMethod");

		assertThat(method.isAccessible()).isFalse();

		int value = invoke(method);

		assertThat(method.isAccessible()).isFalse();
		assertThat(value).isEqualTo(0);
	}
}
