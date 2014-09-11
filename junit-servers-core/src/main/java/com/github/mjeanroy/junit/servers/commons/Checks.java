/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 <mickael.jeanroy@gmail.com>
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

import static java.lang.String.format;

/**
 * Static utilities that can be used to check object's values.
 */
public final class Checks {

	private Checks() {
	}

	/**
	 * Check that a given value is not null.
	 * It throws a {@link NullPointerException} exception if value is null or returned the
	 * value.
	 *
	 * @param value Value to check.
	 * @param name Name of parameter, it will produce an error message such as "{name} must not be null"
	 * @param <T> Type of value.
	 * @return First parameter if it is not null.
	 */
	public static <T> T notNull(T value, String name) {
		if (value == null) {
			throw new NullPointerException(format("%s must not be null", name));
		}
		return value;
	}

	/**
	 * Check that a given integer is positive.
	 * If integer value is negative, it throws an {@link IllegalArgumentException} exception,
	 * otherwise integer value is returned.
	 *
	 * @param value Value to check.
	 * @param name Name of value.
	 * @return Integer value if value is positive.
	 */
	public static int positive(int value, String name) {
		if (value < 0) {
			throw new IllegalArgumentException(format("%s must be positive", name));
		}
		return value;
	}
}
