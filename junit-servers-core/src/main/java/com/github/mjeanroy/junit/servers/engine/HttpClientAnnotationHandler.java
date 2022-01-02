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
import com.github.mjeanroy.junit.servers.commons.lang.ToStringBuilder;
import com.github.mjeanroy.junit.servers.commons.reflect.Annotations;
import com.github.mjeanroy.junit.servers.loggers.Logger;
import com.github.mjeanroy.junit.servers.loggers.LoggerFactory;
import com.github.mjeanroy.junit.servers.servers.EmbeddedServer;

import java.lang.reflect.Field;

import static com.github.mjeanroy.junit.servers.commons.lang.Preconditions.notNull;
import static com.github.mjeanroy.junit.servers.commons.reflect.Reflections.getter;
import static com.github.mjeanroy.junit.servers.commons.reflect.Reflections.setter;

/**
 * Annotation handler that will set simple http client implementation
 * in test classes.
 */
class HttpClientAnnotationHandler extends AbstractAnnotationHandler {

	/**
	 * Class Logger.
	 */
	private static final Logger log = LoggerFactory.getLogger(HttpClientAnnotationHandler.class);

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
		log.debug("Inject HTTP client to {} # {}", target, field);
		TestHttpClient httpClient = Annotations.findAnnotation(field, TestHttpClient.class);
		if (httpClient != null) {
			HttpClientStrategy strategy = httpClient.strategy();
			setter(target, field, strategy.build(server));
		}
	}

	@Override
	public void after(Object target, Field field) {
		log.debug("Clearing HTTP client to {} # {}", target, field);
		HttpClient httpClient = getter(target, field);
		httpClient.destroy();
		setter(target, field, null);
	}

	@Override
	public String toString() {
		return ToStringBuilder.create(getClass())
			.append("annotationKlass", getAnnotationKlass())
			.append("server", server)
			.build();
	}
}
