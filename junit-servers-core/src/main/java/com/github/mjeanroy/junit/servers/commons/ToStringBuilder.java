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

import java.util.Map;

/**
 * Static utility to create easily toString implementation.
 *
 * <p>
 *
 * <strong>Internal API</strong>: these methods are part of the internal API and may be removed, have their signature change,
 * or have their access level decreased from public to protected, package, or private in future versions without notice.
 */
public final class ToStringBuilder {

	/**
	 * Field separator.
	 */
	private static final String SEPARATOR = ", ";

	/**
	 * Character prepended before any iterable output values.
	 */
	private static final String OPEN_ARRAY = "[";

	/**
	 * Character appended after any iterable output values.
	 */
	private static final String CLOSE_ARRAY = "]";

	/**
	 * Characters separating field name with its value.
	 */
	private static final String FIELD_VALUE_SEPARATOR = ": ";

	/**
	 * Character prepended to object values output.
	 */
	private static final String START_OBJ = "{";

	/**
	 * Character appended to object values output.
	 */
	private static final String END_OBJ = "}";

	/**
	 * The pending string returned at the end.
	 */
	private final StringBuilder sb;

	/**
	 * Flag to know if the first field has already been appended.
	 */
	private boolean first;

	// Use static factory instead
	private ToStringBuilder(String klassName) {
		this.sb = new StringBuilder(klassName).append(START_OBJ);
		this.first = true;
	}

	/**
	 * Create initial builder.
	 *
	 * @param klass The class name.
	 * @return The builder.
	 */
	public static ToStringBuilder create(Class<?> klass) {
		return new ToStringBuilder(klass.getSimpleName());
	}

	/**
	 * Append new string value.
	 *
	 * @param name The name of the field.
	 * @param value The field value.
	 * @return The current builder (for chaining).
	 */
	public ToStringBuilder append(String name, String value) {
		return appendValue(name, value);
	}

	/**
	 * Append new integer value.
	 *
	 * @param name The name of the field.
	 * @param value The field value.
	 * @return The current builder (for chaining).
	 */
	public ToStringBuilder append(String name, Integer value) {
		return appendValue(name, value);
	}

	/**
	 * Append new long value.
	 *
	 * @param name The name of the field.
	 * @param value The field value.
	 * @return The current builder (for chaining).
	 */
	public ToStringBuilder append(String name, Long value) {
		return appendValue(name, value);
	}

	/**
	 * Append new boolean value.
	 *
	 * @param name The name of the field.
	 * @param value The field value.
	 * @return The current builder (for chaining).
	 */
	public ToStringBuilder append(String name, Boolean value) {
		return appendValue(name, value);
	}

	/**
	 * Append new map value.
	 *
	 * @param name The name of the field.
	 * @param map The map.
	 * @return The current builder (for chaining).
	 */
	public ToStringBuilder append(String name, Map<?, ?> map) {
		StringBuilder sb = new StringBuilder(START_OBJ);
		for (Map.Entry<?, ?> entry : map.entrySet()) {
			sb.append(entry.getKey()).append(FIELD_VALUE_SEPARATOR).append(formatValue(entry.getValue()));
		}

		sb.append(END_OBJ);

		return appendFormattedValue(name, sb.toString());
	}

	/**
	 * Append new object value.
	 *
	 * @param name The name of the field.
	 * @param value The field value.
	 * @return The current builder (for chaining).
	 */
	public ToStringBuilder append(String name, Object value) {
		return appendValue(name, value);
	}

	/**
	 * Append new boolean value.
	 *
	 * @param name The name of the field.
	 * @param values The collection of values.
	 * @param <T> Type of elements in collection.
	 * @return The current builder (for chaining).
	 */
	public <T> ToStringBuilder append(String name, Iterable<T> values) {
		StringBuilder pending = new StringBuilder().append(OPEN_ARRAY);
		boolean firstItem = true;

		for (T value : values) {
			if (!firstItem) {
				pending.append(SEPARATOR);
			}

			pending.append(formatValue(value));
			firstItem = false;
		}

		return appendFormattedValue(name, pending.append(CLOSE_ARRAY).toString());
	}

	/**
	 * Format value (i.e wrapped string with double quote for example), and append
	 * the field the final output.
	 *
	 * @param name Field name.
	 * @param value The value.
	 * @return The current builder.
	 */
	private ToStringBuilder appendValue(String name, Object value) {
		return appendFormattedValue(name, formatValue(value));
	}

	/**
	 * Append formatted value to the final output.
	 * @param name Field name.
	 * @param value Field value.
	 * @return The current builder.
	 */
	private ToStringBuilder appendFormattedValue(String name, Object value) {
		if (!first) {
			sb.append(SEPARATOR);
		}

		sb.append(name).append(FIELD_VALUE_SEPARATOR).append(value);
		first = false;
		return this;
	}

	/**
	 * Build the final string.
	 *
	 * @return The final string.
	 */
	public String build() {
		return sb.append(END_OBJ).toString();
	}

	/**
	 * Format value:
	 * <ul>
	 *   <li>Returns {@code null} if {@code value} is null.</li>
	 *   <li>Wrapped value inside double quote if it is a string value.</li>
	 *   <li>Returns the result of {@code toString} function otherwise.</li>
	 * </ul>
	 *
	 * @param value The value to raw.
	 * @return The formatted value.
	 */
	private static String formatValue(Object value) {
		if (value == null) {
			return null;
		}

		return value instanceof CharSequence ? "\"" + value + "\"" : value.toString();
	}
}
