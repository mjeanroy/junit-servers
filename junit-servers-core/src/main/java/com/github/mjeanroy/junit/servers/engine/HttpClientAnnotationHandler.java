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

import com.github.mjeanroy.junit.servers.annotations.TestHttpClient;
import com.github.mjeanroy.junit.servers.client.HttpClient;
import com.github.mjeanroy.junit.servers.client.HttpClientStrategy;
import com.github.mjeanroy.junit.servers.servers.EmbeddedServer;

import java.lang.reflect.Field;

import static com.github.mjeanroy.junit.servers.commons.Preconditions.notNull;
import static com.github.mjeanroy.junit.servers.commons.ReflectionUtils.getter;
import static com.github.mjeanroy.junit.servers.commons.ReflectionUtils.setter;

/**
 * Annotation handler that will set simple http client implementation
 * in test classes.
 */
class HttpClientAnnotationHandler extends AbstractAnnotationHandler {

	/**
	 * Create new handler.
	 * @param server Embedded server.
	 * @return Handler.
	 * @throws NullPointerException if server is null.
	 */
	static AnnotationHandler newHttpClientAnnotationHandler(EmbeddedServer<?> server) {
		return new HttpClientAnnotationHandler(notNull(server, "server"));
	}

	/**
	 * Embedded server that will be used with http client.
	 */
	private final EmbeddedServer<?> server;

	// Use static factory instead
	private HttpClientAnnotationHandler(EmbeddedServer<?> server) {
		super(TestHttpClient.class);
		this.server = server;
	}

	@Override
	public void before(Object target, Field field) {
		TestHttpClient httpClient = field.getAnnotation(TestHttpClient.class);
		HttpClientStrategy strategy = httpClient.strategy();
		setter(target, field, strategy.build(server));
	}

	@Override
	public void after(Object target, Field field) {
		HttpClient httpClient = getter(target, field);
		httpClient.destroy();
		setter(target, field, null);
	}
}
