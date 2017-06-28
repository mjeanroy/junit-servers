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

package com.github.mjeanroy.junit.servers.utils.junit.run_if;

import org.junit.internal.builders.IgnoredClassRunner;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;

/**
 * Runner that can be used to ignore a test (or a test class) with a given condition
 * using {@link RunIf} annotation.
 *
 * <br>
 *
 * This is a more readable version of {@link org.junit.Assume} API.
 */
public class RunIfRunner extends Runner {

	/**
	 * The delegated runner: internal operation will be delegated to this runner.
	 */
	private final Runner delegate;

	/**
	 * Create the runner.
	 *
	 * @param klass The test class.
	 * @throws InitializationError If the test class is malformed.
	 */
	public RunIfRunner(Class<?> klass) throws InitializationError {
		boolean ignoreClass = RunIfUtils.isIgnored(klass);

		// If the entire test class must be ignored, we instantiate an instance of IgnoredClassRunner
		// since this runner will not instantiate the test class, thus allowing the class to use
		// illegal API such as Java8 method on JDK7 (since it will never be evaluated).
		this.delegate = ignoreClass ? new IgnoredClassRunner(klass) : new RunIfBlockJunit4ClassRunner(klass);
	}

	@Override
	public Description getDescription() {
		return delegate.getDescription();
	}

	@Override
	public void run(RunNotifier notifier) {
		delegate.run(notifier);
	}
}
