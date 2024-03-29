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

package com.github.mjeanroy.junit.servers.engine;

import com.github.mjeanroy.junit.servers.annotations.TestServerConfiguration;
import com.github.mjeanroy.junit.servers.servers.AbstractConfiguration;
import com.github.mjeanroy.junit.servers.utils.builders.AbstractConfigurationMockBuilder;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import static com.github.mjeanroy.junit.servers.engine.ConfigurationAnnotationHandler.newConfigurationAnnotationHandler;
import static com.github.mjeanroy.junit.servers.testing.ReflectionTestUtils.getPrivateField;
import static com.github.mjeanroy.junit.servers.testing.ReflectionTestUtils.readPrivate;
import static org.assertj.core.api.Assertions.assertThat;

class ConfigurationAnnotationHandlerTest {

	@Test
	void it_should_support_server_annotation() {
		AbstractConfiguration configuration = new AbstractConfigurationMockBuilder().build();
		AnnotationHandler handler = newConfigurationAnnotationHandler(configuration);
		Field field = extractConfigurationField();
		Annotation annotation = readAnnotation(field);

		assertThat(handler.support(annotation)).isTrue();
	}

	@Test
	void it_should_set_configuration_instance() {
		AbstractConfiguration configuration = new AbstractConfigurationMockBuilder().build();
		TestClassWithAnnotatedField target = new TestClassWithAnnotatedField();
		Field field = extractConfigurationField();
		AnnotationHandler handler = newConfigurationAnnotationHandler(configuration);

		verifyBeforeTest(configuration, target, field, handler);
	}

	@Test
	void it_should_implement_to_string() {
		AbstractConfiguration configuration = new AbstractConfigurationMockBuilder().build();
		AnnotationHandler handler = newConfigurationAnnotationHandler(configuration);

		assertThat(handler).hasToString(
			"ConfigurationAnnotationHandler{" +
				"annotationKlass: interface com.github.mjeanroy.junit.servers.annotations.TestServerConfiguration, " +
				"configuration: MockAbstractConfiguration" +
			"}"
		);
	}

	private static void verifyBeforeTest(AbstractConfiguration configuration, TestClassWithAnnotatedField target, Field field, AnnotationHandler handler) {
		handler.before(target, field);
		assertThat((AbstractConfiguration) readPrivate(target, "configuration")).isSameAs(configuration);
	}

	private static Field extractConfigurationField() {
		return getPrivateField(TestClassWithAnnotatedField.class, "configuration");
	}

	private static Annotation readAnnotation(Field field) {
		return field.getAnnotation(TestServerConfiguration.class);
	}

	static class TestClassWithAnnotatedField {

		@TestServerConfiguration
		public AbstractConfiguration configuration;
	}
}
