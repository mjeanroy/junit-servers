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

package com.github.mjeanroy.junit.servers.engine;

import com.github.mjeanroy.junit.servers.annotations.TestServer;
import com.github.mjeanroy.junit.servers.servers.EmbeddedServer;
import com.github.mjeanroy.junit.servers.utils.builders.EmbeddedServerMockBuilder;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import static com.github.mjeanroy.junit.servers.engine.ServerAnnotationHandler.newServerAnnotationHandler;
import static com.github.mjeanroy.junit.servers.testing.ReflectionTestUtils.getPrivateField;
import static com.github.mjeanroy.junit.servers.testing.ReflectionTestUtils.readPrivate;
import static org.assertj.core.api.Assertions.assertThat;

class ServerAnnotationHandlerTest {

	@Test
	void it_should_support_server_annotation() {
		final EmbeddedServer<?> server = new EmbeddedServerMockBuilder().build();
		final Field field = extractServerField();
		final AnnotationHandler handler = newServerAnnotationHandler(server);
		final Annotation annotation = field.getAnnotation(TestServer.class);

		assertThat(handler.support(annotation)).isTrue();
	}

	@Test
	void it_should_set_server_instance() {
		final EmbeddedServer<?> server = new EmbeddedServerMockBuilder().build();
		final Field field = extractServerField();
		final TestClassWithAnnotatedField target = new TestClassWithAnnotatedField();
		final AnnotationHandler handler = newServerAnnotationHandler(server);

		verifyBeforeTest(server, field, target, handler);
	}

	@Test
	void it_should_implement_to_string() {
		final EmbeddedServer<?> server = new EmbeddedServerMockBuilder().build();
		final AnnotationHandler handler = newServerAnnotationHandler(server);

		assertThat(handler).hasToString(
			"ServerAnnotationHandler{" +
				"annotationKlass: interface com.github.mjeanroy.junit.servers.annotations.TestServer, " +
				"server: MockEmbeddedServer" +
			"}"
		);
	}

	private static void verifyBeforeTest(EmbeddedServer<?> server, Field field, TestClassWithAnnotatedField targer, AnnotationHandler handler) {
		handler.before(targer, field);
		assertThat((EmbeddedServer<?>) readPrivate(targer, "server")).isSameAs(server);
	}

	private static Field extractServerField() {
		return getPrivateField(TestClassWithAnnotatedField.class, "server");
	}

	static class TestClassWithAnnotatedField {
		@TestServer
		public EmbeddedServer<?> server;
	}
}
