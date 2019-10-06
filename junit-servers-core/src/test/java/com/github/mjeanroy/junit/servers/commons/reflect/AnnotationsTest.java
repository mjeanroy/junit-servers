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

package com.github.mjeanroy.junit.servers.commons.reflect;

import org.junit.Test;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

public class AnnotationsTest {

	@Test
	public void it_should_find_annotation_on_class() {
		final Class<TestClassWithAnnotation> klass = TestClassWithAnnotation.class;
		final TestAnnotation annotation = Annotations.findAnnotation(klass, TestAnnotation.class);

		assertThat(annotation).isNotNull();
		assertThat(annotation.value()).isEqualTo(1);
	}

	@Test
	public void it_should_find_annotation_on_super_class() {
		final Class<TestChildClassWithAnnotation> klass = TestChildClassWithAnnotation.class;
		final TestAnnotation annotation = Annotations.findAnnotation(klass, TestAnnotation.class);

		assertThat(annotation).isNotNull();
		assertThat(annotation.value()).isEqualTo(1);
	}

	@Test
	public void it_should_find_annotation_on_interface() {
		final Class<TestClassImplementingAnnotatedInterface> klass = TestClassImplementingAnnotatedInterface.class;
		final TestAnnotation annotation = Annotations.findAnnotation(klass, TestAnnotation.class);

		assertThat(annotation).isNotNull();
		assertThat(annotation.value()).isEqualTo(2);
	}

	@Test
	public void it_should_find_annotation_on_meta_annotation() {
		final Class<TestClassWithMetaAnnotation> klass = TestClassWithMetaAnnotation.class;
		final TestAnnotation annotation = Annotations.findAnnotation(klass, TestAnnotation.class);

		assertThat(annotation).isNotNull();
		assertThat(annotation.value()).isEqualTo(3);
	}

	@Test
	public void it_should_check_if_annotation_is_present_on_field() {
		assertThat(Annotations.isAnnotationPresent((Field) null, TestAnnotation.class)).isFalse();
		assertThat(Annotations.isAnnotationPresent(field("field1"), TestAnnotation.class)).isTrue();
		assertThat(Annotations.isAnnotationPresent(field("field2"), TestAnnotation.class)).isFalse();
	}

	@Test
	public void it_should_check_if_annotation_is_present_on_method() {
		assertThat(Annotations.isAnnotationPresent((Method) null, TestAnnotation.class)).isFalse();
		assertThat(Annotations.isAnnotationPresent(method("method1"), TestAnnotation.class)).isTrue();
		assertThat(Annotations.isAnnotationPresent(method("method2"), TestAnnotation.class)).isFalse();
	}

	private static Field field(String name) {
		try {
			return TestClass.class.getDeclaredField(name);
		}
		catch (Exception ex) {
			throw new AssertionError(ex);
		}
	}

	private static Method method(String name) {
		try {
			return TestClass.class.getDeclaredMethod(name);
		}
		catch (Exception ex) {
			throw new AssertionError(ex);
		}
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
	@Documented
	@Inherited
	private @interface TestAnnotation {
		int value();
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
	@Documented
	@Inherited
	@TestAnnotation(3)
	private @interface TestMetaAnnotation {
	}

	@TestAnnotation(1)
	private static class TestClassWithAnnotation {
	}

	private static class TestChildClassWithAnnotation extends TestClassWithAnnotation {
	}

	@TestAnnotation(2)
	private interface TestInterfaceWithAnnotation {
	}

	private static class TestClassImplementingAnnotatedInterface implements TestInterfaceWithAnnotation {
	}

	@TestMetaAnnotation
	private static class TestClassWithMetaAnnotation {
	}

	@SuppressWarnings("unused")
	private static class TestClass {
		@TestAnnotation(5)
		private static int field1;
		private static int field2;

		@TestAnnotation(6)
		private static int method1() {
			return 0;
		}

		private static int method2() {
			return 0;
		}
	}
}
