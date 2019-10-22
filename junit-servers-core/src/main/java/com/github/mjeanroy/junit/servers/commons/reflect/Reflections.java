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

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.github.mjeanroy.junit.servers.commons.reflect.Annotations.isAnnotationPresent;
import static java.lang.reflect.Modifier.isStatic;
import static java.util.Collections.addAll;

/**
 * Static reflection utilities.
 *
 * <p>
 *
 * <strong>Internal API</strong>: these methods are part of the internal API and may be removed, have their signature change,
 * or have their access level decreased from public to protected, package, or private in future versions without notice.
 */
public final class Reflections {

	/**
	 * Class Logger.
	 */
	private static final Logger log = LoggerFactory.getLogger(Reflections.class);

	// Ensure non instantiation.
	private Reflections() {
	}

	/**
	 * Get all fields on given object and look for fields of
	 * super classes.
	 *
	 * @param type Class to inspect.
	 * @return Fields.
	 */
	public static List<Field> findAllFields(Class<?> type) {
		List<Field> fields = new LinkedList<>();
		if (type != null) {
			addAll(fields, type.getDeclaredFields());

			if (type.getSuperclass() != null) {
				fields.addAll(findAllFields(type.getSuperclass()));
			}
		}
		return fields;
	}

	/**
	 * Get all static fields on given class object.
	 *
	 * @param type Class to inspect.
	 * @return Fields.
	 */
	private static Stream<Field> findStaticFields(Class<?> type) {
		return Arrays.stream(type.getDeclaredFields()).filter(field -> isStatic(field.getModifiers()));
	}

	/**
	 * Get all static methods on given class object.
	 *
	 * @param type Class to inspect.
	 * @return Fields.
	 */
	private static Stream<Method> findStaticMethods(Class<?> type) {
		return Arrays.stream(type.getDeclaredMethods()).filter(method -> isStatic(method.getModifiers()));
	}

	/**
	 * Get all static fields on given class object
	 * annotated with given annotation.
	 *
	 * @param type Class to inspect.
	 * @param annotationClass Annotation class.
	 * @return Fields.
	 */
	public static List<Field> findStaticFieldsAnnotatedWith(Class<?> type, Class<? extends Annotation> annotationClass) {
		log.trace("Find static fields of {} annotated with {}", type, annotationClass);
		return findStaticFields(type).filter(field -> isAnnotationPresent(field, annotationClass)).collect(Collectors.toList());
	}

	/**
	 * Get all static methods on given class object
	 * annotated with given annotation.
	 *
	 * @param type Class to inspect.
	 * @param annotationClass Annotation class.
	 * @return Fields.
	 */
	public static List<Method> findStaticMethodsAnnotatedWith(Class<?> type, Class<? extends Annotation> annotationClass) {
		log.trace("Extract static methods of class {} annotated with {}", type, annotationClass);
		return findStaticMethods(type).filter(method -> isAnnotationPresent(method, annotationClass)).collect(Collectors.toList());
	}

	/**
	 * Set value of given field on given instance.
	 *
	 * @param instance Instance.
	 * @param field Field.
	 * @param value Field value.
	 * @throws ReflectionException if set operation is not permitted.
	 */
	public static void setter(Object instance, Field field, Object value) {
		boolean forceAccess = false;

		try {
			if (!field.isAccessible()) {
				forceAccess = true;
				field.setAccessible(true);
			}

			field.set(instance, value);
		}
		catch (IllegalAccessException ex) {
			throw new ReflectionException(ex);
		}
		finally {
			if (forceAccess) {
				field.setAccessible(false);
			}
		}
	}

	/**
	 * Get value of static field.
	 *
	 * @param field Field.
	 * @param <T> Type of expected value.
	 * @return Value of static field.
	 */
	public static <T> T getter(Field field) {
		return getter(null, field);
	}

	/**
	 * Get value of field on target object.
	 * If target is null, it means that field is static and do not
	 * need any target instance.
	 *
	 * @param target Target object.
	 * @param field Field.
	 * @param <T> Type of expected value.
	 * @return Field value.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getter(Object target, Field field) {
		boolean forceAccess = false;

		try {
			if (!field.isAccessible()) {
				field.setAccessible(true);
				forceAccess = true;
			}

			return (T) field.get(target);
		}
		catch (IllegalAccessException ex) {
			throw new ReflectionException(ex);
		}
		finally {
			if (forceAccess) {
				field.setAccessible(false);
			}
		}
	}

	/**
	 * Invoke the static method without any arguments.
	 *
	 * @param method The method to invoke.
	 * @param <T> Return type.
	 * @return The result of the method invocation.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T invoke(Method method) {
		boolean forceAccess = false;

		try {
			if (!method.isAccessible()) {
				method.setAccessible(true);
				forceAccess = true;
			}

			return (T) method.invoke(null);
		}
		catch (InvocationTargetException | IllegalAccessException ex) {
			throw new ReflectionException(ex);
		}
		finally {
			if (forceAccess) {
				method.setAccessible(false);
			}
		}
	}
}
