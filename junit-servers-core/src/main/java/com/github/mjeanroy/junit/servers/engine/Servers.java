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

package com.github.mjeanroy.junit.servers.engine;

import com.github.mjeanroy.junit.servers.annotations.TestServerConfiguration;
import com.github.mjeanroy.junit.servers.exceptions.DuplicateConfigurationException;
import com.github.mjeanroy.junit.servers.exceptions.ServerImplMissingException;
import com.github.mjeanroy.junit.servers.loggers.Logger;
import com.github.mjeanroy.junit.servers.loggers.LoggerFactory;
import com.github.mjeanroy.junit.servers.servers.AbstractConfiguration;
import com.github.mjeanroy.junit.servers.servers.EmbeddedServer;
import com.github.mjeanroy.junit.servers.servers.EmbeddedServerProvider;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

import static com.github.mjeanroy.junit.servers.commons.reflect.Reflections.findStaticFieldsAnnotatedWith;
import static com.github.mjeanroy.junit.servers.commons.reflect.Reflections.findStaticMethodsAnnotatedWith;
import static com.github.mjeanroy.junit.servers.commons.reflect.Reflections.getter;
import static com.github.mjeanroy.junit.servers.commons.reflect.Reflections.invoke;
import static java.lang.System.lineSeparator;

/**
 * Static utilities for server instantiation
 * and configuration.
 */
public final class Servers {

	/**
	 * Class Logger.
	 */
	private static final Logger log = LoggerFactory.getLogger(Servers.class);

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
	@SuppressWarnings({"unchecked", "rawtypes"})
	public static <T extends AbstractConfiguration> EmbeddedServer<T> instantiate(T configuration) {
		log.debug("Instantiating embedded server using configuration: {}", configuration);

		ServiceLoader<EmbeddedServerProvider> serviceProviders = ServiceLoader.load(EmbeddedServerProvider.class);

		List<EmbeddedServerProvider<T>> coreServerProviders = new ArrayList<>();
		List<EmbeddedServerProvider<T>> customServerProviders = new ArrayList<>();

		for (EmbeddedServerProvider<T> provider : serviceProviders) {
			Class<? extends EmbeddedServerProvider> providerClass = provider.getClass();

			log.debug("Found provider {}", providerClass);

			if (providerClass.getName().startsWith("com.github.mjeanroy.junit.servers")) {
				coreServerProviders.add(provider);
			}
			else {
				customServerProviders.add(provider);
			}
		}

		// No match, fail fast.
		if (coreServerProviders.isEmpty() && customServerProviders.isEmpty()) {
			log.error(
				"Cannot find embedded server implementation, please add `junit-servers-tomcat` or `junit-server-jetty` dependency or provide your own service loader."
			);

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

		return instantiate(customServerProviders.get(0), configuration);
	}

	private static <T extends AbstractConfiguration> EmbeddedServer<T> instantiate(EmbeddedServerProvider<T> provider, T configuration) {
		log.debug("Instantiate embedded server using provider: {}", provider);
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
	public static <T extends AbstractConfiguration> T findConfiguration(Class<?> klass) {
		log.debug("Extract configuration from class: {}", klass);

		// Look for static methods first
		List<Method> methods = findStaticMethodsAnnotatedWith(klass, TestServerConfiguration.class);
		List<Field> fields = findStaticFieldsAnnotatedWith(klass, TestServerConfiguration.class);
		int nbOfConfigurations = methods.size() + fields.size();

		if (nbOfConfigurations > 1) {
			log.error("Found {} eligible configuration, fail", nbOfConfigurations);
			failBecauseOfDuplicateConfiguration(klass, methods, fields);
		}

		if (!methods.isEmpty()) {
			log.debug("Extracting configuration from method {}", methods.get(0));
			return invoke(methods.get(0));
		}

		// Then, look for static field
		if (!fields.isEmpty()) {
			log.debug("Extracting configuration from field {}", fields.get(0));
			return getter(fields.get(0));
		}

		return null;
	}

	private static void failBecauseOfDuplicateConfiguration(Class<?> klass, List<Method> methods, List<Field> fields) {
		List<String> lines = new ArrayList<>(1 + methods.size() + fields.size());

		lines.add("It looks like multiple fields may be used to extract configuration: found:");

		for (Method method : methods) {
			lines.add("- " + klass.getName() + "#" + method.getName() + "()");
		}

		for (Field field : fields) {
			lines.add("- " + klass.getName() + "#" + field.getName());
		}

		throw new DuplicateConfigurationException(
			String.join(lineSeparator(), lines)
		);
	}
}
