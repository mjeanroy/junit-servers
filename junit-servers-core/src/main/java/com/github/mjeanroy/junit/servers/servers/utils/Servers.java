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

package com.github.mjeanroy.junit.servers.servers.utils;

import com.github.mjeanroy.junit.servers.exceptions.ServerImplMissingException;
import com.github.mjeanroy.junit.servers.junit.annotations.Configuration;
import com.github.mjeanroy.junit.servers.servers.EmbeddedServer;
import com.github.mjeanroy.junit.servers.servers.configuration.AbstractConfiguration;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import static com.github.mjeanroy.junit.servers.commons.ReflectionUtils.findStaticFieldsAnnotatedWith;
import static com.github.mjeanroy.junit.servers.commons.ReflectionUtils.findStaticMethodsAnnotatedWith;
import static com.github.mjeanroy.junit.servers.commons.ReflectionUtils.getter;
import static com.github.mjeanroy.junit.servers.commons.ReflectionUtils.invoke;
import static com.github.mjeanroy.junit.servers.commons.Utils.firstNonNull;

/**
 * Static utilities for server instantiation
 * and configuration.
 */
public final class Servers {

	// Ensure non instantiation
	private Servers() {
	}

	/**
	 * Instantiate jetty or tomcat embedded server.
	 *
	 * Server configuration is automatically read on static field / methods
	 * available on given class (i.e field or method annotated
	 * with {@link Configuration} annotation).
	 *
	 * Server implementation is automatically detected (jetty or
	 * tomcat) with classpath detection.
	 *
	 * @param klass Class to inspect.
	 * @return Embedded server.
	 */
	public static EmbeddedServer instantiate(Class<?> klass) {
		AbstractConfiguration configuration = findConfiguration(klass);
		EmbeddedServer srv = firstNonNull(
				instantiate("com.github.mjeanroy.junit.servers.jetty.EmbeddedJetty", configuration),
				instantiate("com.github.mjeanroy.junit.servers.tomcat.EmbeddedTomcat", configuration)
		);

		if (srv == null) {
			throw new ServerImplMissingException();
		}

		return srv;
	}

	/**
	 * Instantiate embedded server.
	 *
	 * If second parameter is null (configuration), empty constructor will
	 * be used to instantiate embedded server by reflection.
	 * Otherwise, constructor with one parameter (configuration) will
	 * be used.
	 *
	 * @param className Class implementation of embedded server.
	 * @param configuration Optional configuration, may be null.
	 * @return Embedded server.
	 */
	public static EmbeddedServer instantiate(String className, AbstractConfiguration configuration) {
		try {
			Class klass = Class.forName(className);
			if (configuration == null) {
				return (EmbeddedServer) klass.newInstance();
			}
			else {
				@SuppressWarnings({ "raw", "unchecked" }) Constructor constructor = klass.getConstructor(configuration.getClass());
				return (EmbeddedServer) constructor.newInstance(configuration);
			}
		}
		catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
			return null;
		}
	}

	/**
	 * Find configuration object on static field / method on
	 * running class.
	 *
	 * Configuration is read from static method or static field
	 * annotated with {@link Configuration} annotation.
	 *
	 * @param klass Class to inspect.
	 * @param <T> Type of configuration.
	 * @return Configuration.
	 */
	public static  <T extends AbstractConfiguration> T findConfiguration(Class<?> klass) {
		// Look for static methods first
		List<Method> methods = findStaticMethodsAnnotatedWith(klass, Configuration.class);
		if (!methods.isEmpty()) {
			return invoke(methods.get(0));
		}

		// Then, look for static field
		List<Field> fields = findStaticFieldsAnnotatedWith(klass, Configuration.class);
		if (!fields.isEmpty()) {
			return getter(fields.get(0));
		}

		return null;
	}
}
