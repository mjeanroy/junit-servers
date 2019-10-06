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

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;

/**
 * Static Annotation Utilities.
 *
 * <p>
 *
 * <strong>Internal API</strong>: these methods are part of the internal API and may be removed, have their signature change,
 * or have their access level decreased from public to protected, package, or private in future versions without notice.
 */
public final class Annotations {

	/**
	 * The list of packages that should not be scanned for meta-annotation (since these packages may not
	 * contains annotations of DbUnit+!).
	 */
	private static final List<String> BLACKLISTED_PACKAGES = asList(
		"java.lang.",
		"org.junit."
	);

	// Ensure non instantiation.
	private Annotations() {
	}

	/**
	 * Check if annotation is present on given field.
	 *
	 * @param field The field.
	 * @param annotationClass The annotation to check.
	 * @return {@code true} if annotation is present, {@code false} otherwise.
	 */
	static boolean isAnnotationPresent(Field field, Class<? extends Annotation> annotationClass) {
		return field != null && field.isAnnotationPresent(annotationClass);
	}

	/**
	 * Check if annotation is present on given method.
	 *
	 * @param method The method.
	 * @param annotationClass The annotation to check.
	 * @return {@code true} if annotation is present, {@code false} otherwise.
	 */
	static boolean isAnnotationPresent(Method method, Class<? extends Annotation> annotationClass) {
		return method != null && method.isAnnotationPresent(annotationClass);
	}

	/**
	 * Find expected annotation on class, or a class in the hierarchy.
	 *
	 * @param klass Class.
	 * @param annotationClass Annotation class to look for.
	 * @param <T> Type of annotation.
	 * @return Annotation if found, {@code null} otherwise.
	 */
	public static <T extends Annotation> T findAnnotation(Class<?> klass, Class<T> annotationClass) {
		Collection<T> annotations = findAnnotationOn(klass, annotationClass);
		return annotations.isEmpty() ? null : annotations.iterator().next();
	}

	/**
	 * Find expected annotation on class, or a class in the hierarchy.
	 *
	 * @param klass Class.
	 * @param annotationClass Annotation class to look for.
	 * @param <T> Type of annotation.
	 * @return Annotation if found, {@code null} otherwise.
	 */
	private static <T extends Annotation> List<T> findAnnotations(Class<?> klass, Class<T> annotationClass) {
		return findAnnotationOn(klass, annotationClass);
	}

	/**
	 * Find expected annotation on given element.
	 *
	 * @param element Class.
	 * @param annotationClass Annotation class to look for.
	 * @param <T> Type of annotation.
	 * @return Annotation if found, {@code null} otherwise.
	 */
	private static <T extends Annotation> List<T> findAnnotationOn(AnnotatedElement element, Class<T> annotationClass) {
		final List<T> results = new ArrayList<>();

		if (element == null) {
			return emptyList();
		}

		// Is it directly present?
		if (element.isAnnotationPresent(annotationClass)) {
			results.add(element.getAnnotation(annotationClass));
		}

		// Search for meta-annotation
		for (Annotation candidate : element.getAnnotations()) {
			Class<? extends Annotation> candidateAnnotationType = candidate.annotationType();
			if (shouldScan(candidateAnnotationType)) {
				T result = findAnnotation(candidateAnnotationType, annotationClass);
				if (result != null) {
					results.add(result);
				}
			}
		}

		if (element instanceof Class) {
			Class<?> klass = (Class<?>) element;

			// Look on interfaces.
			for (Class<?> intf : klass.getInterfaces()) {
				if (shouldScan(intf)) {
					final List<T> subResults = findAnnotationOn(intf, annotationClass);
					if (!subResults.isEmpty()) {
						results.addAll(subResults);
					}
				}
			}

			// Go up in the class hierarchy.
			Class<?> superClass = klass.getSuperclass();
			if (shouldScan(superClass)) {
				final List<T> subResults = findAnnotations(superClass, annotationClass);
				if (!subResults.isEmpty()) {
					results.addAll(subResults);
				}
			}

			// Search in outer class.
			Class<?> declaringClass = klass.getDeclaringClass();
			if (shouldScan(declaringClass)) {
				final List<T> subResults = findAnnotations(declaringClass, annotationClass);
				if (!subResults.isEmpty()) {
					results.addAll(subResults);
				}
			}
		}

		return unmodifiableList(results);
	}

	/**
	 * Check if it is worth scanning this element (i.e there is a chance to find useful
	 * annotation).
	 *
	 * @param elementType The element class type.
	 * @return {@code true} if element should be scanned, {@code false} otherwise.
	 */
	private static boolean shouldScan(Class<?> elementType) {
		if (elementType == null) {
			return false;
		}

		String name = elementType.getName();

		for (String pkg : BLACKLISTED_PACKAGES) {
			if (name.startsWith(pkg)) {
				return false;
			}
		}

		return true;
	}
}
