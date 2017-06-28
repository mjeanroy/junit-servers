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

package com.github.mjeanroy.junit.servers.annotations.handlers;

import com.github.mjeanroy.junit.servers.annotations.TestHttpClient;
import com.github.mjeanroy.junit.servers.client.HttpClient;
import com.github.mjeanroy.junit.servers.servers.EmbeddedServer;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import static com.github.mjeanroy.junit.servers.annotations.handlers.HttpClientAnnotationHandler.newHttpClientAnnotationHandler;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class HttpClientAnnotationHandlerTest {

	@SuppressWarnings("unchecked")
	@Test
	public void it_should_support_server_annotation() {
		EmbeddedServer server = mock(EmbeddedServer.class);
		HttpClientAnnotationHandler handler = newHttpClientAnnotationHandler(server);

		Class serverClass = TestHttpClient.class;
		Annotation annotation = mock(Annotation.class);
		when(annotation.annotationType()).thenReturn(serverClass);

		assertThat(handler.support(annotation)).isTrue();
	}

	@Test
	public void it_should_set_client_instance() throws Exception {
		EmbeddedServer server = mock(EmbeddedServer.class);
		Foo foo = new Foo();
		Field field = Foo.class.getDeclaredField("client");

		HttpClientAnnotationHandler handler = newHttpClientAnnotationHandler(server);

		handler.before(foo, field);
		assertThat(foo.client).isNotNull();

		HttpClient spy = spy(foo.client);
		FieldUtils.writeField(foo, "client", spy, true);

		handler.after(foo, field);
		verify(spy).destroy();
		assertThat(foo.client).isNull();
	}

	private static class Foo {
		@TestHttpClient
		private HttpClient client;
	}
}
