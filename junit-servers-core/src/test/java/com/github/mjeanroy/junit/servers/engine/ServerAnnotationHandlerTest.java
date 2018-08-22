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

package com.github.mjeanroy.junit.servers.engine;

import com.github.mjeanroy.junit.servers.annotations.TestServer;
import com.github.mjeanroy.junit.servers.servers.EmbeddedServer;
import com.github.mjeanroy.junit.servers.utils.builders.EmbeddedServerMockBuilder;
import com.github.mjeanroy.junit.servers.utils.fixtures.FixtureClass;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import static com.github.mjeanroy.junit.servers.engine.ServerAnnotationHandler.newServerAnnotationHandler;
import static com.github.mjeanroy.junit.servers.utils.commons.Fields.getPrivateField;
import static com.github.mjeanroy.junit.servers.utils.commons.Fields.readPrivate;
import static org.assertj.core.api.Assertions.assertThat;

public class ServerAnnotationHandlerTest {

	@Test
	public void it_should_support_server_annotation() {
		final EmbeddedServer<?> server = new EmbeddedServerMockBuilder().build();
		final Field field = extractServerField();
		final AnnotationHandler handler = newServerAnnotationHandler(server);
		final Annotation annotation = field.getAnnotation(TestServer.class);

		assertThat(handler.support(annotation)).isTrue();
	}

	@Test
	public void it_should_set_server_instance() {
		final EmbeddedServer<?> server = new EmbeddedServerMockBuilder().build();
		final Field field = extractServerField();
		final FixtureClass fixture = new FixtureClass();
		final AnnotationHandler handler = newServerAnnotationHandler(server);

		verifyBeforeTest(server, field, fixture, handler);
	}

	private static void verifyBeforeTest(EmbeddedServer<?> server, Field field, FixtureClass fixture, AnnotationHandler handler) {
		handler.before(fixture, field);
		assertThat(readPrivate(fixture, "server")).isSameAs(server);
	}

	private static Field extractServerField() {
		return getPrivateField(FixtureClass.class, "server");
	}
}
