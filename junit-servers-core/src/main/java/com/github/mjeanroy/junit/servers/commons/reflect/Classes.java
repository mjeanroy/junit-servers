/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2019 <mickael.jeanroy@gmail.com>
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

package com.github.mjeanroy.junit.servers.commons.reflect;

import com.github.mjeanroy.junit.servers.exceptions.ReflectionException;
import com.github.mjeanroy.junit.servers.loggers.Logger;
import com.github.mjeanroy.junit.servers.loggers.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Static class utilities.
 *
 * <p>
 *
 * <strong>Internal API</strong>: these methods are part of the internal API and may be removed, have their signature change,
 * or have their access level decreased from public to protected, package, or private in future versions without notice.
 */
public final class Classes {

	/**
	 * Class Logger.
	 */
	private static final Logger log = LoggerFactory.getLogger(Classes.class);

	// Ensure non instantiation
	private Classes() {
	}

	/**
	 * Check if a given class is available in classpath.
	 *
	 * @param className Fully qualified name of class to test.
	 * @return {@code true} if class is available, {@code false} otherwise.
	 */
	public static boolean isPresent(String className) {
		try {
			Class.forName(className);
			return true;
		}
		catch (Exception ex) {
			return false;
		}
	}

	/**
	 * Instantie new class instance using its empty constructor (may be a private constructor).
	 *
	 * @param klass The class.
	 * @param <T> The new instance type.
	 * @return The new instance.
	 */
	public static <T> T instantiate(Class<T> klass) {
		boolean wasAccessible = true;
		Constructor<T> ctor = null;

		try {
			ctor = klass.getDeclaredConstructor();
			wasAccessible = ctor.isAccessible();

			if (!wasAccessible) {
				ctor.setAccessible(true);
			}

			return ctor.newInstance();
		}
		catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
			log.error(ex.getMessage(), ex);
			throw new ReflectionException(ex);
		}
		finally {
			if (!wasAccessible) {
				ctor.setAccessible(false);
			}
		}
	}
}
