/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2017 <mickael.jeanroy@gmail.com>
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

package com.github.mjeanroy.junit.servers.runner;

import com.github.mjeanroy.junit.servers.annotations.TestHttpClient;
import com.github.mjeanroy.junit.servers.client.HttpClient;
import com.github.mjeanroy.junit.servers.servers.EmbeddedServer;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import static com.github.mjeanroy.junit.servers.runner.HttpClientAnnotationHandler.newHttpClientAnnotationHandler;
import static com.github.mjeanroy.junit.servers.utils.commons.Fields.getPrivateField;
import static com.github.mjeanroy.junit.servers.utils.commons.Fields.readPrivate;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class HttpClientAnnotationHandlerTest {

	@Test
	public void it_should_support_server_annotation() {
		EmbeddedServer<?> server = mock(EmbeddedServer.class);
		AnnotationHandler handler = newHttpClientAnnotationHandler(server);

		Field field = getPrivateField(FixtureClass.class, "client");
		Annotation annotation = field.getAnnotation(TestHttpClient.class);
		assertThat(handler.support(annotation)).isTrue();
	}

	@Test
	public void it_should_set_client_instance() throws Exception {
		EmbeddedServer<?> server = mock(EmbeddedServer.class);
		FixtureClass fixture = new FixtureClass();
		Field field = FixtureClass.class.getDeclaredField("client");

		AnnotationHandler handler = newHttpClientAnnotationHandler(server);

		handler.before(fixture, field);

		HttpClient client = readPrivate(fixture, "client");
		assertThat(client).isNotNull();
		assertThat(client.isDestroyed()).isFalse();

		handler.after(fixture, field);
		assertThat(readPrivate(fixture, "client")).isNull();
		assertThat(client.isDestroyed()).isTrue();
	}
}
