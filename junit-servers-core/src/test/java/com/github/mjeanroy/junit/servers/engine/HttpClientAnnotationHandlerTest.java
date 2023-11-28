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

import com.github.mjeanroy.junit.servers.annotations.TestHttpClient;
import com.github.mjeanroy.junit.servers.client.HttpClient;
import com.github.mjeanroy.junit.servers.client.HttpClientStrategy;
import com.github.mjeanroy.junit.servers.servers.EmbeddedServer;
import com.github.mjeanroy.junit.servers.utils.builders.EmbeddedServerMockBuilder;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

import static com.github.mjeanroy.junit.servers.engine.HttpClientAnnotationHandler.newHttpClientAnnotationHandler;
import static com.github.mjeanroy.junit.servers.testing.ReflectionTestUtils.getPrivateField;
import static com.github.mjeanroy.junit.servers.testing.ReflectionTestUtils.readPrivate;
import static org.assertj.core.api.Assertions.assertThat;

class HttpClientAnnotationHandlerTest {

	@Test
	void it_should_support_server_annotation() {
		EmbeddedServer<?> server = new EmbeddedServerMockBuilder().build();
		AnnotationHandler handler = newHttpClientAnnotationHandler(server);
		Field field = extractClientField(TestClassWithAnnotatedField.class);
		Annotation annotation = readAnnotation(field);

		assertThat(handler.support(annotation)).isTrue();
	}

	@Test
	void it_should_set_client_instance() {
		EmbeddedServer<?> server = new EmbeddedServerMockBuilder().build();
		TestClassWithAnnotatedField target = new TestClassWithAnnotatedField();
		Field field = extractClientField(TestClassWithAnnotatedField.class);
		AnnotationHandler handler = newHttpClientAnnotationHandler(server);

		HttpClient client = verifyBeforeTest(target, field, handler);

		verifyAfterTest(target, field, handler, client);
	}

	@Test
	void it_should_set_client_instance_on_super_class() {
		EmbeddedServer<?> server = new EmbeddedServerMockBuilder().build();
		TestInheritedClassWithAnnotatedField target = new TestInheritedClassWithAnnotatedField();
		Field field = extractClientField(TestInheritedClassWithAnnotatedField.class);
		AnnotationHandler handler = newHttpClientAnnotationHandler(server);

		HttpClient client = verifyBeforeTest(target, field, handler);

		verifyAfterTest(target, field, handler, client);
	}

	@Test
	void it_should_set_client_instance_on_meta_annotated_field() {
		EmbeddedServer<?> server = new EmbeddedServerMockBuilder().build();
		TestClassWithMetaAnnotationField target = new TestClassWithMetaAnnotationField();
		Field field = extractClientField(TestClassWithMetaAnnotationField.class);
		AnnotationHandler handler = newHttpClientAnnotationHandler(server);

		HttpClient client = verifyBeforeTest(target, field, handler);

		verifyAfterTest(target, field, handler, client);
		verifyAsyncHttpClient(client);
	}

	@Test
	void it_should_implement_to_string() {
		EmbeddedServer<?> server = new EmbeddedServerMockBuilder().build();
		AnnotationHandler handler = newHttpClientAnnotationHandler(server);

		assertThat(handler).hasToString(
			"HttpClientAnnotationHandler{" +
				"annotationKlass: interface com.github.mjeanroy.junit.servers.annotations.TestHttpClient, " +
				"server: MockEmbeddedServer" +
			"}"
		);
	}

	private static void verifyAfterTest(Object target, Field field, AnnotationHandler handler, HttpClient client) {
		handler.after(target, field);

		assertThat((HttpClient) readPrivate(target, "client")).isNull();
		assertThat(client.isDestroyed()).isTrue();
	}

	private static HttpClient verifyBeforeTest(Object target, Field field, AnnotationHandler handler) {
		handler.before(target, field);

		HttpClient client = readPrivate(target, "client");
		assertThat(client).isNotNull();
		assertThat(client.isDestroyed()).isFalse();
		return client;
	}

	private static void verifyAsyncHttpClient(HttpClient client) {
		assertThat(client).isInstanceOf(com.github.mjeanroy.junit.servers.client.impl.ning.NingAsyncHttpClient.class);
	}

	private static Field extractClientField(Class<?> targetClass) {
		return getPrivateField(targetClass, "client");
	}

	private static Annotation readAnnotation(Field field) {
		return field.getAnnotation(TestHttpClient.class);
	}

	static class TestClassWithAnnotatedField {

		@TestHttpClient
		public HttpClient client;
	}

	private static class TestInheritedClassWithAnnotatedField extends TestClassWithAnnotatedField {
	}

	static class TestClassWithMetaAnnotationField {
		@AsyncHttpClient
		public HttpClient client;
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	@Documented
	@Inherited
	@TestHttpClient(strategy = HttpClientStrategy.NING_ASYNC_HTTP_CLIENT)
	@interface AsyncHttpClient {
	}
}
