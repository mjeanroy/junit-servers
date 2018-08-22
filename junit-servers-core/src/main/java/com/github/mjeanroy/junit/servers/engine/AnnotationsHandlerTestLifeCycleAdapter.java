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

import com.github.mjeanroy.junit.servers.servers.AbstractConfiguration;
import com.github.mjeanroy.junit.servers.servers.EmbeddedServer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

import static com.github.mjeanroy.junit.servers.commons.ReflectionUtils.findAllFields;
import static com.github.mjeanroy.junit.servers.engine.ConfigurationAnnotationHandler.newConfigurationAnnotationHandler;
import static com.github.mjeanroy.junit.servers.engine.HttpClientAnnotationHandler.newHttpClientAnnotationHandler;
import static com.github.mjeanroy.junit.servers.engine.ServerAnnotationHandler.newServerAnnotationHandler;
import static java.util.Arrays.asList;

/**
 * An engine that will handle various annotations defined by Junit-Servers:
 *
 * <ul>
 *   <li>{@link com.github.mjeanroy.junit.servers.annotations.TestServer}</li>
 *   <li>{@link com.github.mjeanroy.junit.servers.annotations.TestServerConfiguration}</li>
 *   <li>{@link com.github.mjeanroy.junit.servers.annotations.TestHttpClient}</li>
 * </ul>
 */
public class AnnotationsHandlerTestLifeCycleAdapter extends AbstractTestLifeCycle implements TestLifeCycleAdapter {

	/**
	 * List of handlers.
	 */
	private final List<AnnotationHandler> handlers;

	/**
	 * Create test lifecycle engine that will setup following Junit-Servers annotations declared in test class:
	 *
	 * <ul>
	 *   <li>{@link com.github.mjeanroy.junit.servers.annotations.TestServer}</li>
	 *   <li>{@link com.github.mjeanroy.junit.servers.annotations.TestServerConfiguration}</li>
	 *   <li>{@link com.github.mjeanroy.junit.servers.annotations.TestHttpClient}</li>
	 * </ul>
	 *
	 * @param server The embedded server used in the tested class instance.
	 * @param configuration The embedded server configuration.
	 */
	public AnnotationsHandlerTestLifeCycleAdapter(EmbeddedServer<?> server, AbstractConfiguration configuration) {
		this.handlers = asList(
				newServerAnnotationHandler(server),
				newConfigurationAnnotationHandler(configuration),
				newHttpClientAnnotationHandler(server)
		);
	}

	@Override
	public void beforeEach(Object target) {
		process(target, true);
	}

	@Override
	public void afterEach(Object target) {
		process(target, false);
	}

	/**
	 * Process handlers.
	 *
	 * @param target Target class (i.e tested class).
	 * @param before Flag to know if handler has to run "before" phase or "after" phase.
	 */
	private void process(Object target, boolean before) {
		List<Field> fields = findAllFields(target.getClass());
		for (Field field : fields) {
			for (AnnotationHandler handler : handlers) {
				processField(target, handler, field, before);
			}
		}
	}

	/**
	 * Process field for given handler.
	 *
	 * @param target Target class (i.e tested class).
	 * @param handler Handler.
	 * @param field Field.
	 * @param before Flag to know if handler has to run "before" phase or "after" phase.
	 */
	private void processField(Object target, AnnotationHandler handler, Field field, boolean before) {
		for (Annotation annotation : field.getAnnotations()) {
			if (handler.support(annotation)) {
				if (before) {
					handler.before(target, field);
				} else {
					handler.after(target, field);
				}
			}
		}
	}
}
