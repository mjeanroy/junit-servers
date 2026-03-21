/*
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

package com.github.mjeanroy.junit.servers.commons.lang;

/// Static string utilities.
///
/// **Internal API**: these methods are part of the internal API and may be removed, have their signature change,
/// or have their access level decreased from public to protected, package, or private in future versions without notice.
public final class Strings {

	private Strings() {
	}

	/// Returns empty string if `value` is `null`, returns `value`
	/// otherwise.
	///
	/// @param value Given value.
	/// @return The non null value.
	public static String nullToEmpty(String value) {
		return value == null ? "" : value;
	}

	/// Check if given string is not `null` and not empty.
	///
	/// @param value Value to check.
	/// @return `true` if `value` is not `null` and not empty, `false` otherwise.
	public static boolean isNotEmpty(String value) {
		return value != null && !value.isEmpty();
	}

	/// Check if given string is `null` or empty.
	///
	/// @param value Value to check.
	/// @return `true` if `value` is `null` or empty, `false` otherwise.
	public static boolean isEmpty(String value) {
		return value == null || value.isEmpty();
	}

	/// Trim given string.
	///
	/// @param value String to trim.
	/// @return Trimmed string.
	public static String trim(String value) {
		return value == null ? value : value.trim();
	}

	/// Check that given string is blank.
	///
	/// @param value String to check.
	/// @return `true` if string is blank, `false` otherwise.
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

	/// Check that given string is not blank.
	///
	/// @param value String to check.
	/// @return `true` if string is not blank, `false` otherwise.
	public static boolean isNotBlank(String value) {
		return !isBlank(value);
	}

	/// Remove string prefix if and only if string value starts with
	/// the prefix, otherwise original string is returned.
	///
	/// @param value String value.
	/// @param prefix String prefix.
	/// @return New string.
	public static String removePrefix(String value, String prefix) {
		if (value == null || prefix == null || prefix.length() > value.length()) {
			return value;
		}

		return value.startsWith(prefix) ? value.substring(prefix.length()) : value;
	}

	/// Returns the lowercase value of a given string:
	/// - Returns `null` if `value` is `null`.
	/// - Returns the result of [String#toLowerCase()] otherwise.
	///
	/// @param value The string value.
	/// @return The lowercase value.
	public static String toLowerCase(String value) {
		return value == null ? null : value.toLowerCase();
	}
}
