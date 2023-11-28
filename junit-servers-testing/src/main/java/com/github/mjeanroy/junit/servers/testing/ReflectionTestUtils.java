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

package com.github.mjeanroy.junit.servers.testing;

import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Static field utilities, used for testing only.
 */
public final class ReflectionTestUtils {

	// Ensure non instantiation.
	private ReflectionTestUtils() {
	}

	/**
	 * Get private field on given class.
	 *
	 * @param klass The class.
	 * @param name The field name.
	 * @return The field.
	 */
	public static Field getPrivateField(Class<?> klass, String name) {
		Class<?> current = klass;

		while (current != null && current != Object.class) {
			Field field = getDeclaredField(current, name);
			if (field != null) {
				return field;
			}

			current = current.getSuperclass();
		}

		return null;
	}

	/**
	 * Get private field on given class.
	 *
	 * @param klass The class.
	 * @param name The field name.
	 * @return The field.
	 */
	private static Field getDeclaredField(Class<?> klass, String name) {
		try {
			return klass.getDeclaredField(name);
		}
		catch (NoSuchFieldException ex) {
			return null;
		}
		catch (Exception ex) {
			throw new AssertionError(ex);
		}
	}

	/**
	 * Get private method on given class.
	 *
	 * @param klass The class.
	 * @param name The field name.
	 * @return The field.
	 */
	public static Method getPrivateMethod(Class<?> klass, String name) {
		try {
			return klass.getDeclaredMethod(name);
		}
		catch (Exception ex) {
			throw new AssertionError(ex);
		}
	}

	/**
	 * Read private field on given object instance.
	 *
	 * @param instance The object instance.
	 * @param name The field name.
	 * @param <T> Type of the returned value.
	 * @return The value of the field on given instance.
	 */
	public static <T> T readPrivate(Object instance, String name) {
		try {
			@SuppressWarnings("unchecked")
			T value = (T) FieldUtils.readField(instance, name, true);

			return value;
		}
		catch (IllegalAccessException ex) {
			throw new AssertionError(ex);
		}
	}
}
