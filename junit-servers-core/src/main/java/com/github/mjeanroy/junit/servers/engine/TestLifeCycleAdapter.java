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

/**
 * A test lifecycle engine: this is a class that implements commons test lifecycle hooks (before-all, before, after-all,
 * after), thus will be able to be used in various test engine:
 *
 * <ul>
 *   <li>JUnit 4 Rules.</li>
 *   <li>JUnit 4 Runner.</li>
 *   <li>JUnit Jupiter Extensions.</li>
 * </ul>
 */
public interface TestLifeCycleAdapter {

	/**
	 * Method called before instantiating the test class and any test instance.
	 */
	void beforeAll();

	/**
	 * Method called after all tests have been run.
	 */
	void afterAll();

	/**
	 * Method called before running unit test.
	 *
	 * @param target The test class instance.
	 */
	void beforeEach(Object target);

	/**
	 * Method called after running unit test.
	 *
	 * @param target The test class instance.
	 */
	void afterEach(Object target);
}
