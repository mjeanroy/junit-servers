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

package com.github.mjeanroy.junit.servers.junit4;

import com.github.mjeanroy.junit.servers.engine.AnnotationsHandlerTestLifeCycleAdapter;
import com.github.mjeanroy.junit.servers.servers.AbstractConfiguration;
import com.github.mjeanroy.junit.servers.servers.EmbeddedServer;

/**
 * Create new rule that will execute a list of annotation
 * handlers before and after test executions.
 */
class AnnotationsHandlerRule extends AbstractRuleInstance {

	/**
	 * List of handlers.
	 */
	private final AnnotationsHandlerTestLifeCycleAdapter annotationHandlers;

	/**
	 * Create new rules.
	 *
	 * @param target Target class (i.e tested class).
	 * @param server The embedded server used in the tested class instance.
	 * @param configuration The embedded server configuration.
	 */
	AnnotationsHandlerRule(Object target, EmbeddedServer<?> server, AbstractConfiguration configuration) {
		super(target);
		this.annotationHandlers = new AnnotationsHandlerTestLifeCycleAdapter(server, configuration);
	}

	@Override
	protected void before() {
		annotationHandlers.beforeEach(getTarget());
	}

	@Override
	protected void after() {
		annotationHandlers.afterEach(getTarget());
	}
}
