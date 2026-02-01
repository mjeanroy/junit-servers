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

package com.github.mjeanroy.junit.servers.tomcat9.tests.core;

import org.apache.juli.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * An implementation of Java Util Logging for Tomcat, delegating all call to
 * a slf4j logger.
 *
 * <br>
 *
 * Note that this class will automatically be loaded by tomcat using the Service Provider
 * Interface.
 */
public class TomcatLogger implements Log {

	/**
	 * The internal SLF4J Logger.
	 */
	private final Logger logger;

	// this constructor is important, otherwise the ServiceLoader cannot start
	public TomcatLogger() {
		logger = LoggerFactory.getLogger(TomcatLogger.class);
	}

	// this constructor is needed by the LogFactory implementation
	public TomcatLogger(String name) {
		logger = LoggerFactory.getLogger(name);
	}

	@Override
	public boolean isDebugEnabled() {
		return logger.isDebugEnabled();
	}

	@Override
	public boolean isErrorEnabled() {
		return logger.isErrorEnabled();
	}

	@Override
	public boolean isFatalEnabled() {
		return logger.isErrorEnabled();
	}

	@Override
	public boolean isInfoEnabled() {
		return logger.isInfoEnabled();
	}

	@Override
	public boolean isTraceEnabled() {
		return logger.isTraceEnabled();
	}

	@Override
	public boolean isWarnEnabled() {
		return logger.isWarnEnabled();
	}

	@Override
	public void trace(Object message) {
		logger.trace(Objects.toString(message));
	}

	@Override
	public void trace(Object message, Throwable t) {
		logger.trace(Objects.toString(message), t);
	}

	@Override
	public void debug(Object message) {
		logger.debug(Objects.toString(message));
	}

	@Override
	public void debug(Object message, Throwable t) {
		logger.debug(Objects.toString(message), t);
	}

	@Override
	public void info(Object message) {
		logger.info(Objects.toString(message));
	}

	@Override
	public void info(Object message, Throwable t) {
		logger.info(Objects.toString(message), t);
	}

	@Override
	public void warn(Object message) {
		logger.warn(Objects.toString(message));
	}

	@Override
	public void warn(Object message, Throwable t) {
		logger.warn(Objects.toString(message), t);
	}

	@Override
	public void error(Object message) {
		logger.error(Objects.toString(message));
	}

	@Override
	public void error(Object message, Throwable t) {
		logger.error(Objects.toString(message), t);
	}

	@Override
	public void fatal(Object message) {
		logger.error(Objects.toString(message));
	}

	@Override
	public void fatal(Object message, Throwable t) {
		logger.error(Objects.toString(message), t);
	}
}
