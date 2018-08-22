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

package com.github.mjeanroy.junit.servers.engine;

import com.github.mjeanroy.junit.servers.annotations.TestServerConfiguration;
import com.github.mjeanroy.junit.servers.exceptions.ServerImplMissingException;
import com.github.mjeanroy.junit.servers.servers.AbstractConfiguration;
import com.github.mjeanroy.junit.servers.servers.EmbeddedServer;
import com.github.mjeanroy.junit.servers.servers.EmbeddedServerProvider;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

import static com.github.mjeanroy.junit.servers.commons.ReflectionUtils.findStaticFieldsAnnotatedWith;
import static com.github.mjeanroy.junit.servers.commons.ReflectionUtils.findStaticMethodsAnnotatedWith;
import static com.github.mjeanroy.junit.servers.commons.ReflectionUtils.getter;
import static com.github.mjeanroy.junit.servers.commons.ReflectionUtils.invoke;

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
	 * <p>
	 *
	 * Server configuration is automatically read on static field / methods
	 * available on given class (i.e field or method annotated
	 * with {@link com.github.mjeanroy.junit.servers.annotations.TestServerConfiguration} annotation).
	 *
	 * <p>
	 *
	 * Server implementation is automatically detected (jetty or
	 * tomcat) with classpath detection.
	 *
	 * @param klass Class to inspect.
	 * @return Embedded server.
	 */
	public static EmbeddedServer<?> instantiate(Class<?> klass) {
		AbstractConfiguration configuration = findConfiguration(klass);
		return instantiate(configuration);
	}

	/**
	 * Instantiate jetty or tomcat embedded server.
	 *
	 * <p>
	 *
	 * Configuration is an optional parameter and can be null.
	 * If configuration is null, empty constructor will be used to instantiate
	 * embedded server.
	 * Otherwise, constructor with one parameter (configuration) will
	 * be used.
	 *
	 * <p>
	 *
	 * Server implementation is automatically detected (jetty or
	 * tomcat) with classpath detection.
	 *
	 * @param configuration Optional configuration.
	 * @return Embedded server.
	 */
	public static EmbeddedServer<?> instantiate(AbstractConfiguration configuration) {
		ServiceLoader<EmbeddedServerProvider> serviceProviders = ServiceLoader.load(EmbeddedServerProvider.class);

		List<EmbeddedServerProvider> coreServerProviders = new ArrayList<>();
		List<EmbeddedServerProvider> customServerProviders = new ArrayList<>();

		for (EmbeddedServerProvider provider : serviceProviders) {
			if (provider.getClass().getName().startsWith("com.github.mjeanroy.junit.servers")) {
				coreServerProviders.add(provider);
			} else {
				customServerProviders.add(provider);
			}
		}

		// No match, fail fast.
		if (coreServerProviders.isEmpty() && customServerProviders.isEmpty()) {
			throw new ServerImplMissingException();
		}

		// Choose the first one available.
		if (customServerProviders.isEmpty()) {
			return instantiate(coreServerProviders.get(0), configuration);
		}

		// Try to instantiate the custom available implementation.
		if (customServerProviders.size() > 1) {
			throw new ServerImplMissingException();
		}

		return instantiate(coreServerProviders.get(0), configuration);
	}

	private static <T extends AbstractConfiguration> EmbeddedServer<T> instantiate(EmbeddedServerProvider<T> provider, T configuration) {
		return configuration == null ? provider.instantiate() : provider.instantiate(configuration);
	}

	/**
	 * Find configuration object on static field / method on
	 * running class. Configuration is read from static method or static field
	 * annotated with {@link com.github.mjeanroy.junit.servers.annotations.TestServerConfiguration} annotation.
	 *
	 * @param klass Class to inspect.
	 * @param <T> Type of configuration.
	 * @return Configuration.
	 */
	public static  <T extends AbstractConfiguration> T findConfiguration(Class<?> klass) {
		// Look for static methods first
		List<Method> methods = findStaticMethodsAnnotatedWith(klass, TestServerConfiguration.class);
		if (!methods.isEmpty()) {
			return invoke(methods.get(0));
		}

		// Then, look for static field
		List<Field> fields = findStaticFieldsAnnotatedWith(klass, TestServerConfiguration.class);
		if (!fields.isEmpty()) {
			return getter(fields.get(0));
		}

		return null;
	}
}
