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

package com.github.mjeanroy.junit.servers.tomcat8.exceptions;

import com.github.mjeanroy.junit.servers.exceptions.IllegalConfigurationException;
import com.github.mjeanroy.junit.servers.tomcat8.EmbeddedTomcatConfiguration;

/**
 * Error thrown when Tomcat configuration is not valid.
 */
public class IllegalTomcatConfigurationException extends IllegalConfigurationException {

	/**
	 * Create exception with default error message.
	 */
	public IllegalTomcatConfigurationException() {
		super(createMessage());
	}

	private static String createMessage() {
		return "Embedded tomcat server requires a configuration that is an instance of " + EmbeddedTomcatConfiguration.class.getName() + ", please fix it.";
	}
}
