/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 <mickael.jeanroy@gmail.com>
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

package com.github.mjeanroy.junit.servers.rules;

import com.github.mjeanroy.junit.servers.annotations.handlers.AnnotationHandler;
import org.junit.runner.Description;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import static com.github.mjeanroy.junit.servers.commons.Preconditions.notNull;
import static com.github.mjeanroy.junit.servers.commons.ReflectionUtils.findAllFields;

/**
 * Create new rule that will execute a list of annotation
 * handlers before and after test executions.
 */
public class HandlersRule extends AbstractRuleInstance {

	/**
	 * List of handlers.
	 */
	private final List<AnnotationHandler> handlers;

	/**
	 * Create new rules.
	 *
	 * @param target Target class (i.e tested class).
	 * @param handlers List of handlers.
	 */
	public HandlersRule(Object target, AnnotationHandler handler, AnnotationHandler... handlers) {
		super(target);

		this.handlers = new LinkedList<>();
		this.handlers.add(notNull(handler, "handler"));

		for (AnnotationHandler h : handlers) {
			this.handlers.add(notNull(h, "handler"));
		}
	}

	@Override
	protected void before(Description description) {
		process(true);
	}

	@Override
	protected void after(Description description) {
		process(false);
	}

	/**
	 * Process handlers.
	 *
	 * @param before Flag to know if handler has to run "before" phase
	 *               or "after" phase.
	 */
	protected void process(boolean before) {
		List<Field> fields = findAllFields(getTarget().getClass());
		for (Field field : fields) {
			for (AnnotationHandler handler : handlers) {
				processField(handler, field, before);
			}
		}
	}

	/**
	 * Process field for given handler.
	 *
	 * @param handler Handler.
	 * @param field Field.
	 * @param before Flag to know if handler has to run "before" phase
	 *               or "after" phase.
	 */
	protected void processField(AnnotationHandler handler, Field field, boolean before) {
		for (Annotation annotation : field.getAnnotations()) {
			if (handler.support(annotation)) {
				if (before) {
					handler.before(getTarget(), field);
				} else {
					handler.after(getTarget(), field);
				}
			}
		}
	}
}
