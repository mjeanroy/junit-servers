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

import com.github.mjeanroy.junit.servers.annotations.TestServer;
import com.github.mjeanroy.junit.servers.servers.EmbeddedServer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class HandlersRuleTest {

	private AnnotationHandler handler;
	private Foo foo;
	private HandlersRule rule;

	@Before
	public void setUp() {
		handler = mock(AnnotationHandler.class);
		foo = new Foo();
		rule = new HandlersRule(foo, handler);
	}

	@Test
	public void it_should_process_before_handler() throws Throwable {
		when(handler.support(any(Annotation.class))).thenReturn(true);

		rule.before();

		verify(handler).before(same(foo), any(Field.class));
		verify(handler, never()).after(same(foo), any(Field.class));

		ArgumentCaptor<Annotation> annotationCaptor = ArgumentCaptor.forClass(Annotation.class);
		verify(handler).support(annotationCaptor.capture());

		Annotation annotation = annotationCaptor.getValue();
		assertThat(annotation).isNotNull();
		assertThat(annotation.annotationType()).isEqualTo(TestServer.class);
	}

	@Test
	public void it_should_process_after_handler() {
		when(handler.support(any(Annotation.class))).thenReturn(true);

		rule.after();

		verify(handler, never()).before(same(foo), any(Field.class));
		verify(handler).after(same(foo), any(Field.class));

		ArgumentCaptor<Annotation> annotationCaptor = ArgumentCaptor.forClass(Annotation.class);
		verify(handler).support(annotationCaptor.capture());

		Annotation annotation = annotationCaptor.getValue();
		assertThat(annotation).isNotNull();
		assertThat(annotation.annotationType()).isEqualTo(TestServer.class);
	}

	@Test
	public void it_should_not_call_before_handler_if_annotation_is_not_supported() throws Throwable {
		when(handler.support(any(Annotation.class))).thenReturn(false);

		rule.before();

		verify(handler, never()).before(same(foo), any(Field.class));
		verify(handler, never()).after(same(foo), any(Field.class));

		ArgumentCaptor<Annotation> annotationCaptor = ArgumentCaptor.forClass(Annotation.class);
		verify(handler).support(annotationCaptor.capture());

		Annotation annotation = annotationCaptor.getValue();
		assertThat(annotation).isNotNull();
		assertThat(annotation.annotationType()).isEqualTo(TestServer.class);
	}

	@Test
	public void it_should_not_call_after_handler_if_annotation_is_not_supported() {
		when(handler.support(any(Annotation.class))).thenReturn(false);

		rule.after();

		verify(handler, never()).before(same(foo), any(Field.class));
		verify(handler, never()).after(same(foo), any(Field.class));

		ArgumentCaptor<Annotation> annotationCaptor = ArgumentCaptor.forClass(Annotation.class);
		verify(handler).support(annotationCaptor.capture());

		Annotation annotation = annotationCaptor.getValue();
		assertThat(annotation).isNotNull();
		assertThat(annotation.annotationType()).isEqualTo(TestServer.class);
	}

	private static class Foo {

		@TestServer
		private EmbeddedServer<?> server;
	}
}
