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

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * Abstract skeleton of {@link AnnotationHandler}.
 */
abstract class AbstractAnnotationHandler implements AnnotationHandler {

	/**
	 * Annotation class processed by handler.
	 */
	private final Class<? extends Annotation> annotationKlass;

	/**
	 * Initialize new abstract handler.
	 *
	 * @param annotationKlass Annotation class processed by handler.
	 */
	AbstractAnnotationHandler(Class<? extends Annotation> annotationKlass) {
		this.annotationKlass = annotationKlass;
	}

	@Override
	public boolean support(Annotation annotation) {
		return annotation.annotationType().equals(annotationKlass);
	}

	@Override
	public void before(Object target, Field field) {
		// Should be overridden
	}

	@Override
	public void after(Object target, Field field) {
		// Should be overridden
	}
}
