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

import java.lang.reflect.Field;

import static com.github.mjeanroy.junit.servers.commons.ReflectionUtils.setter;
import static com.github.mjeanroy.junit.servers.commons.Preconditions.notNull;

/**
 * Annotation handler that will set embedded server to a field
 * on a given class instance.
 */
class ServerAnnotationHandler extends AbstractAnnotationHandler {

	/**
	 * Create new handler.
	 * @param server Embedded server.
	 * @return Handler.
	 * @throws NullPointerException if server is null.
	 */
	static AnnotationHandler newServerAnnotationHandler(EmbeddedServer<?> server) {
		return new ServerAnnotationHandler(notNull(server, "server"));
	}

	/**
	 * Embedded server set on class fields.
	 */
	private final EmbeddedServer<?> server;

	// Use static factory instead
	private ServerAnnotationHandler(EmbeddedServer<?> server) {
		super(TestServer.class);
		this.server = server;
	}

	@Override
	public void before(Object target, Field field) {
		setter(target, field, server);
	}
}
