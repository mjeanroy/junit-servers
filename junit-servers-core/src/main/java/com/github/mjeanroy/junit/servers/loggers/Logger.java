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

/**
 * Logger abstraction.
 */
public interface Logger {

	/**
	 * Log message with {@code "TRACE"} level.
	 *
	 * @param message Message to log.
	 */
	void trace(String message);

	/**
	 * Log message with {@code "TRACE"} level and interpolate parameter
	 * to log message..
	 *
	 * @param message Message to log.
	 * @param parameter Message dynamic parameter.
	 */
	void trace(String message, Object parameter);

	/**
	 * Log message with {@code "TRACE"} level and interpolate parameters
	 * to log message..
	 *
	 * @param message Message to log.
	 * @param p1 First message dynamic parameter.
	 * @param p2 First message dynamic parameter.
	 */
	void trace(String message, Object p1, Object p2);

	/**
	 * Log message with {@code "DEBUG"} level.
	 *
	 * @param message Message to log.
	 */
	void debug(String message);

	/**
	 * Log message with {@code "DEBUG"} level and interpolate parameter
	 * to log message..
	 *
	 * @param message Message to log.
	 * @param parameter Message dynamic parameter.
	 */
	void debug(String message, Object parameter);

	/**
	 * Log message with {@code "DEBUG"} level and interpolate parameters
	 * to log message..
	 *
	 * @param message Message to log.
	 * @param p1 First message dynamic parameter.
	 * @param p2 First message dynamic parameter.
	 */
	void debug(String message, Object p1, Object p2);

	/**
	 * Log message with {@code "INFO"} level.
	 *
	 * @param message Message to log.
	 */
	void info(String message);

	/**
	 * Log message with {@code "INFO"} level and interpolate parameter
	 * to log message..
	 *
	 * @param message Message to log.
	 * @param parameter Message dynamic parameter.
	 */
	void info(String message, Object parameter);

	/**
	 * Log message with {@code "INFO"} level and interpolate parameters
	 * to log message..
	 *
	 * @param message Message to log.
	 * @param p1 First message dynamic parameter.
	 * @param p2 First message dynamic parameter.
	 */
	void info(String message, Object p1, Object p2);

	/**
	 * Log message with {@code "WARN"} level.
	 *
	 * @param message Message to log.
	 */
	void warn(String message);

	/**
	 * Log message with {@code "WARN"} level and interpolate parameter
	 * to log message..
	 *
	 * @param message Message to log.
	 * @param parameter Message dynamic parameter.
	 */
	void warn(String message, Object parameter);

	/**
	 * Log message with {@code "WARN"} level and interpolate parameters
	 * to log message..
	 *
	 * @param message Message to log.
	 * @param p1 First message dynamic parameter.
	 * @param p2 First message dynamic parameter.
	 */
	void warn(String message, Object p1, Object p2);

	/**
	 * Log message with {@code "ERROR"} level.
	 *
	 * @param message Message to log.
	 */
	void error(String message);

	/**
	 * Log message with {@code "ERROR"} level and interpolate parameter
	 * to log message..
	 *
	 * @param message Message to log.
	 * @param parameter Message dynamic parameter.
	 */
	void error(String message, Object parameter);

	/**
	 * Log message with {@code "ERROR"} level and interpolate parameters
	 * to log message..
	 *
	 * @param message Message to log.
	 * @param p1 First message dynamic parameter.
	 * @param p2 First message dynamic parameter.
	 */
	void error(String message, Object p1, Object p2);

	/**
	 * Log message with {@code "ERROR"} level and print given exception stacktrace.
	 *
	 * @param message Message to log.
	 * @param exception The exception.
	 */
	void error(String message, Throwable exception);
}
