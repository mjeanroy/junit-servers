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

import com.github.mjeanroy.junit.servers.exceptions.ReflectionException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import static com.github.mjeanroy.junit.servers.commons.CollectionUtils.filter;
import static java.lang.reflect.Modifier.isStatic;
import static java.util.Arrays.asList;
import static java.util.Collections.addAll;

/**
 * Static reflection utilities.
 *
 * <p>
 *
 * <strong>Internal API</strong>: these methods are part of the internal API and may be removed, have their signature change,
 * or have their access level decreased from public to protected, package, or private in future versions without notice.
 */
public final class ReflectionUtils {

	// Ensure non instantiation.
	private ReflectionUtils() {
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
	private static List<Field> findStaticFields(Class<?> type) {
		return filter(asList(type.getDeclaredFields()), STATIC_FIELD_PREDICATE);
	}

	/**
	 * Get all static methods on given class object.
	 *
	 * @param type Class to inspect.
	 * @return Fields.
	 */
	private static List<Method> findStaticMethods(Class<?> type) {
		return filter(asList(type.getDeclaredMethods()), STATIC_METHOD_PREDICATE);
	}

	/**
	 * Get all static fields on given class object
	 * annotated with given annotation.
	 *
	 * @param type Class to inspect.
	 * @param klass Annotation class.
	 * @return Fields.
	 */
	public static List<Field> findStaticFieldsAnnotatedWith(Class<?> type, Class<? extends Annotation> klass) {
		List<Field> fields = findStaticFields(type);
		return filter(fields, new FieldAnnotatedWithPredicate(klass));
	}

	/**
	 * Get all static methods on given class object
	 * annotated with given annotation.
	 *
	 * @param type Class to inspect.
	 * @param klass Annotation class.
	 * @return Fields.
	 */
	public static List<Method> findStaticMethodsAnnotatedWith(Class<?> type, Class<? extends Annotation> klass) {
		List<Method> methods = findStaticMethods(type);
		return filter(methods, new MethodAnnotatedWithPredicate(klass));
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

	/**
	 * Predicate that will return {@code true} if the field in parameter is annotated with a
	 * given {@link Annotation}.
	 */
	private static class FieldAnnotatedWithPredicate implements Predicate<Field> {

		/**
		 * The annotation to check.
		 */
		private final Class<? extends Annotation> annotationKlass;

		/**
		 * Create the predicate.
		 *
		 * @param annotationKlass The annotation to check.
		 */
		private FieldAnnotatedWithPredicate(Class<? extends Annotation> annotationKlass) {
			this.annotationKlass = annotationKlass;
		}

		@Override
		public boolean apply(Field field) {
			return field.isAnnotationPresent(annotationKlass);
		}
	}

	/**
	 * Predicate that will return {@code true} if the method in parameter is annotated with a
	 * given {@link Annotation}.
	 */
	private static class MethodAnnotatedWithPredicate implements Predicate<Method> {

		/**
		 * The annotation to check.
		 */
		private final Class<? extends Annotation> annotationKlass;

		/**
		 * Create the predicate.
		 *
		 * @param annotationKlass The annotation to check.
		 */
		private MethodAnnotatedWithPredicate(Class<? extends Annotation> annotationKlass) {
			this.annotationKlass = annotationKlass;
		}

		@Override
		public boolean apply(Method method) {
			return method.isAnnotationPresent(annotationKlass);
		}
	}

	/**
	 * Predicate that will return {@code true} if the field in parameter is a static field.
	 */
	private static final Predicate<Field> STATIC_FIELD_PREDICATE = new Predicate<Field>() {
		@Override
		public boolean apply(Field field) {
			return isStatic(field.getModifiers());
		}
	};

	/**
	 * Predicate that will return {@code true} if the method in parameter is a static method.
	 */
	private static final Predicate<Method> STATIC_METHOD_PREDICATE = new Predicate<Method>() {
		@Override
		public boolean apply(Method object) {
			return isStatic(object.getModifiers());
		}
	};
}
