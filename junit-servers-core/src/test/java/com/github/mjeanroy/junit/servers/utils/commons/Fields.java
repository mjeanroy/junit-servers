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

package com.github.mjeanroy.junit.servers.utils.commons;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Static field utilities to use in tests.
 */
public final class Fields {

	// Ensure non instantiation.
	private Fields() {
	}

	/**
	 * Get private field on given class.
	 *
	 * @param klass The class.
	 * @param name The field name.
	 * @return The field.
	 */
	public static Field getPrivateField(Class<?> klass, String name) {
		try {
			return klass.getDeclaredField(name);
		} catch (Exception ex) {
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
		} catch (Exception ex) {
			throw new AssertionError(ex);
		}
	}

	/**
	 * Write value on given static final field.
	 *
	 * @param klass The class.
	 * @param name The field name.
	 * @param value The new field value.
	 */
	public static void writeStaticFinal(Class<?> klass, String name, Object value) {
		Field field = null;
		int modifiers = -1;
		boolean forceFinal = false;
		boolean forceAccess = false;

		try {
			field = klass.getDeclaredField(name);
			boolean accessible = field.isAccessible();
			if (!accessible) {
				field.setAccessible(true);
				forceAccess = true;
			}

			// Remove final modifier.
			modifiers = field.getModifiers();
			if (Modifier.isFinal(modifiers)) {
				Field modifiersField = Field.class.getDeclaredField("modifiers");
				modifiersField.setAccessible(true);
				modifiersField.setInt(field, field.getModifiers() & -17);
				forceFinal = true;
			}

			field.set(null, value);

		} catch (Exception ex) {
			throw new AssertionError(ex);
		} finally {
			if (field != null && forceAccess) {
				field.setAccessible(false);
			}

			if (field != null && forceFinal) {
				try {
					Field modifiersField = Field.class.getDeclaredField("modifiers");
					modifiersField.setInt(field, modifiers);
					modifiersField.setAccessible(false);
				} catch (Exception ex) {
					// ok...
				}
			}
		}
	}
}
