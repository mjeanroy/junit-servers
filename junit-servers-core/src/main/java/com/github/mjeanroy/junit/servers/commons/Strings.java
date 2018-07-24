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
 * Static string utilities.
 *
 * <p>
 *
 * <strong>Internal API</strong>: these methods are part of the internal API and may be removed, have their signature change,
 * or have their access level decreased from public to protected, package, or private in future versions without notice.
 */
public final class Strings {

	private Strings() {
	}

	/**
	 * Check that given string is blank.
	 *
	 * @param value String to check.
	 * @return {@code true} if string is blank, {@code false} otherwise.
	 */
	static boolean isBlank(String value) {
		if (value == null) {
			return true;
		}

		for (char character : value.toCharArray()) {
			if (!Character.isWhitespace(character)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Check that given string is not blank.
	 *
	 * @param value String to check.
	 * @return {@code true} if string is not blank, {@code false} otherwise.
	 */
	public static boolean isNotBlank(String value) {
		return !isBlank(value);
	}

	/**
	 * Remove string prefix if and only if string value starts with
	 * the prefix, otherwise original string is returned.
	 *
	 * @param value String value.
	 * @param prefix String prefix.
	 * @return New string.
	 */
	public static String removePrefix(String value, String prefix) {
		if (value == null || prefix == null || prefix.length() > value.length()) {
			return value;
		}

		return value.startsWith(prefix) ? value.substring(prefix.length()) : value;
	}

	/**
	 * Returns the lowercase value of a given string:
	 * <ul>
	 *   <li>Returns {@code null} if {@code value} is {@code null}.</li>
	 *   <li>Returns the result of {@link String#toLowerCase()} otherwise.</li>
	 * </ul>
	 *
	 * @param value The string value.
	 * @return The lowercase value.
	 */
	public static String toLowerCase(String value) {
		return value == null ? null : value.toLowerCase();
	}
}
