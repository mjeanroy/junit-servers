/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2026 <mickael.jeanroy@gmail.com>
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

import org.slf4j.LoggerFactory;

/**
 * A logger delegating implementation to SLF4J {@link org.slf4j.Logger}.
 */
class Slf4jLogger implements Logger {

	/**
	 * The internal logger.
	 */
	private final org.slf4j.Logger log;

	/**
	 * Create logger.
	 *
	 * @param klass Logger name.
	 */
	Slf4jLogger(Class<?> klass) {
		log = LoggerFactory.getLogger(klass);
	}

	@Override
	public void trace(String message) {
		log.trace(message);
	}

	@Override
	public void trace(String message, Object parameter) {
		log.trace(message, parameter);
	}

	@Override
	public void trace(String message, Object p1, Object p2) {
		log.trace(message, p1, p2);
	}

	@Override
	public void debug(String message) {
		log.debug(message);
	}

	@Override
	public void debug(String message, Object parameter) {
		log.debug(message, parameter);
	}

	@Override
	public void debug(String message, Object p1, Object p2) {
		log.debug(message, p1, p2);
	}

	@Override
	public void info(String message) {
		log.info(message);
	}

	@Override
	public void info(String message, Object parameter) {
		log.info(message, parameter);
	}

	@Override
	public void info(String message, Object p1, Object p2) {
		log.info(message, p1, p2);
	}

	@Override
	public void warn(String message) {
		log.warn(message);
	}

	@Override
	public void warn(String message, Object parameter) {
		log.warn(message, parameter);
	}

	@Override
	public void warn(String message, Object p1, Object p2) {
		log.warn(message, p1, p2);
	}

	@Override
	public void error(String message) {
		log.error(message);
	}

	@Override
	public void error(String message, Object parameter) {
		log.error(message, parameter);
	}

	@Override
	public void error(String message, Object p1, Object p2) {
		log.error(message, p1, p2);
	}

	@Override
	public void error(String message, Throwable exception) {
		log.error(message, exception);
	}
}
