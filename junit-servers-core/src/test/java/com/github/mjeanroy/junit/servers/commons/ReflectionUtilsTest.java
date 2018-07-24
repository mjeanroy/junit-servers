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

package com.github.mjeanroy.junit.servers.commons;

import com.github.mjeanroy.junit.servers.commons.fixtures.Bar;
import com.github.mjeanroy.junit.servers.commons.fixtures.FooAnnotation;
import com.github.mjeanroy.junit.servers.utils.commons.Fields;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import static com.github.mjeanroy.junit.servers.commons.ReflectionUtils.findAllFields;
import static com.github.mjeanroy.junit.servers.commons.ReflectionUtils.findStaticFieldsAnnotatedWith;
import static com.github.mjeanroy.junit.servers.commons.ReflectionUtils.findStaticMethodsAnnotatedWith;
import static com.github.mjeanroy.junit.servers.commons.ReflectionUtils.getter;
import static com.github.mjeanroy.junit.servers.commons.ReflectionUtils.invoke;
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
	public void it_should_find_all_static_fields_with_annotations() {
		List<Field> fields = findStaticFieldsAnnotatedWith(Bar.class, FooAnnotation.class);

		assertThat(fields)
				.isNotNull()
				.isNotEmpty()
				.hasSize(1);
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
	public void it_should_set_value_on_private_field() throws Exception {
		Bar bar = new Bar(1, "foo");
		String newValue = "bar";

		Field field = Fields.getPrivateField(Bar.class, "name");

		assertThat(field.isAccessible()).isFalse();
		assertThat(bar.getName()).isEqualTo("foo");

		setter(bar, field, newValue);

		assertThat(field.isAccessible()).isFalse();
		assertThat(bar.getName()).isEqualTo(newValue);
	}

	@Test
	public void it_should_get_value_on_private_field() throws Exception {
		String actualValue = "foo";
		Bar bar = new Bar(1, actualValue);
		Field field = Fields.getPrivateField(Bar.class, "name");

		assertThat(field.isAccessible()).isFalse();

		String value = getter(bar, field);

		assertThat(field.isAccessible()).isFalse();
		assertThat(value).isEqualTo(actualValue);
	}

	@Test
	public void it_should_get_value_on_static_private_field() throws Exception {
		Field field = Fields.getPrivateField(Bar.class, "staticField");

		assertThat(field.isAccessible()).isFalse();

		String value = getter(field);

		assertThat(field.isAccessible()).isFalse();
		assertThat(value).isEqualTo("foo");
	}

	@Test
	public void it_should_get_value_on_static_private_method() throws Exception {
		Method method = Fields.getPrivateMethod(Bar.class, "getStaticPrivateMethod");

		assertThat(method.isAccessible()).isFalse();

		int value = invoke(method);

		assertThat(method.isAccessible()).isFalse();
		assertThat(value).isEqualTo(0);
	}
}
