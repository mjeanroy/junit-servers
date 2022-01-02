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

package com.github.mjeanroy.junit.servers.loggers;

import com.github.mjeanroy.junit.servers.commons.reflect.Classes;

/**
 * Static Logger Factory.
 */
public final class LoggerFactory {

	/**
	 * A flag indicating if SL4J is available in the classpath.
	 */
	private static final boolean SLF4J_AVAILABLE = Classes.isPresent("org.slf4j.LoggerFactory");

	/**
	 * A flag indicating if LOG4J2 is available in the classpath.
	 */
	private static final boolean LOG4J2_AVAILABLE = Classes.isPresent("org.apache.logging.log4j.Logger");

	// Ensure no instantiation.
	private LoggerFactory() {
	}

	/**
	 * Create logger from given class (class name will be used as logger name).
	 *
	 * @param klass Class name.
	 * @return The logger.
	 */
	public static Logger getLogger(Class<?> klass) {
		if (SLF4J_AVAILABLE) {
			return new Slf4jLogger(klass);
		}

		if (LOG4J2_AVAILABLE) {
			return new Log4jLogger(klass);
		}

		return new NoOpLogger();
	}
}
