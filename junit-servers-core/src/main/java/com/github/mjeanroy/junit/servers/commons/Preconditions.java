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

import java.util.Collection;

import static java.lang.String.format;

/**
 * Static utilities that can be used to check object's values.
 *
 * <p>
 *
 * <strong>Internal API</strong>: these methods are part of the internal API and may be removed, have their signature change,
 * or have their access level decreased from public to protected, package, or private in future versions without notice.
 */
public final class Preconditions {

	private Preconditions() {
	}

	/**
	 * Check that a given value is not null.
	 * It throws a {@link NullPointerException} exception if value is {@code null} or returned the
	 * value.
	 *
	 * @param value Value to check.
	 * @param name Name of parameter, it will produce an error message such as "{name} must not be null"
	 * @param <T> Type of value.
	 * @return First parameter if it is not null.
	 * @throws NullPointerException If {@code value} is {@code null}.
	 */
	public static <T> T notNull(T value, String name) {
		if (value == null) {
			throw new NullPointerException(format("%s must not be null", name));
		}
		return value;
	}

	/**
	 * Check that given collection is not empty (i.e null or with a size equal to zero).
	 * It throws a {@link IllegalArgumentException} exception if collection is empty or
	 * returned original collection otherwise.
	 *
	 * Generated message will be : {name} must not be empty
	 * Where name is the value given in parameter.
	 *
	 * @param collection Collection.
	 * @param name Name of field to produce exception message.
	 * @param <T> Type of elements.
	 * @return Original collection.
	 * @throws NullPointerException If {@code collection} is {@code null}.
	 * @throws IllegalArgumentException If {@code collection} is empty.
	 */
	public static <T> Collection<T> notEmpty(Collection<T> collection, String name) {
		notNull(collection, name);

		if (collection.isEmpty()) {
			throw new IllegalArgumentException(format("%s must not be empty", name));
		}

		return collection;
	}

	/**
	 * Check that a given string value is not blank (i.e not null, not empty and not blank).
	 * It throws a {@link NullPointerException} exception if value is null and throw an
	 * {@link java.lang.IllegalArgumentException} if value is empty or blank.
	 * Otherwise original value is returned
	 *
	 * @param value Value to check.
	 * @param name Name of parameter, it will produce an error message such as "{name} must not be null"
	 *             or "{name} must not be blank".
	 * @return First parameter if it is not blank.
	 */
	public static String notBlank(String value, String name) {
		notNull(value, name);

		if (Strings.isBlank(value)) {
			throw new IllegalArgumentException(format("%s must not be blank", name));
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
		return checkPositiveNumber(value, name);
	}

	/**
	 * Check that a given long value is positive.
	 * If long value is negative, it throws an {@link IllegalArgumentException} exception,
	 * otherwise integer value is returned.
	 *
	 * @param value Value to check.
	 * @param name Name of value.
	 * @return Long value if value is positive.
	 */
	public static long positive(long value, String name) {
		return checkPositiveNumber(value, name);
	}

	private static <T extends Number> T checkPositiveNumber(T value, String name) {
		if (value.doubleValue() < 0) {
			throw new IllegalArgumentException(format("%s must be positive", name));
		}
		return value;
	}
}
