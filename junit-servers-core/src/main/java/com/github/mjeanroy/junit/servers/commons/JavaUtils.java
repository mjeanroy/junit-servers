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

package com.github.mjeanroy.junit.servers.commons;

/**
 * Static Java Utilities.
 *
 * <strong>Internal API</strong>: these methods are part of the internal API and may be removed, have their signature change,
 * or have their access level decreased from public to protected, package, or private in future versions without notice.
 */
public final class JavaUtils {

	// Ensure non instantiation.
	private JavaUtils() {
	}

	/**
	 * The system property name that will be read to get Java version.
	 */
	private static final String JAVA_SPECIFICATION_VERSION_PROP = "java.specification.version";

	/**
	 * The Java Specification version.
	 */
	private static final String JAVA_SPECIFICATION_VERSION = System.getProperty(JAVA_SPECIFICATION_VERSION_PROP);

	/**
	 * The parsed Java Version.
	 */
	private static final int JAVA_MAJOR_VERSION = parseJavaVersion();

	/**
	 * Check if runtime java version is at least Java 9.
	 *
	 * @return {@code true} if current Java version is at least Java 9, {@code false} otherwise.
	 */
	public static boolean isPostJdk9() {
		return JAVA_MAJOR_VERSION >= 9;
	}

	/**
	 * Parse java version.
	 *
	 * @return The JAVA Version.
	 */
	private static int parseJavaVersion() {
		String[] parts = JAVA_SPECIFICATION_VERSION.split("\\.");
		int nbParts = parts.length;
		int majorIndex = nbParts > 1 ? 1 : 0;
		return Integer.parseInt(parts[majorIndex]);
	}
}
