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

package com.github.mjeanroy.junit.servers.servers;

import java.io.File;
import java.io.FileFilter;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.github.mjeanroy.junit.servers.commons.Preconditions.notBlank;
import static com.github.mjeanroy.junit.servers.commons.Preconditions.notNull;
import static com.github.mjeanroy.junit.servers.commons.Preconditions.positive;
import static com.github.mjeanroy.junit.servers.servers.AbstractConfiguration.DEFAULT_CLASSPATH;
import static com.github.mjeanroy.junit.servers.servers.AbstractConfiguration.DEFAULT_PATH;
import static com.github.mjeanroy.junit.servers.servers.AbstractConfiguration.DEFAULT_PORT;
import static com.github.mjeanroy.junit.servers.servers.AbstractConfiguration.DEFAULT_WEBAPP;

/**
 * Builder for {@link AbstractConfiguration} instances, should be extended by custom configuration implementation.
 *
 * @param <T> Type of extended builder.
 * @param <U> Type of {@link AbstractConfiguration} implementation.
 */
public abstract class AbstractConfigurationBuilder<T extends AbstractConfigurationBuilder<T, U>, U extends AbstractConfiguration> {

	/**
	 * Path value.
	 *
	 * @see AbstractConfiguration#path
	 */
	private String path;

	/**
	 * Webapp value.
	 *
	 * @see AbstractConfiguration#webapp
	 */
	private String webapp;

	/**
	 * Port value.
	 *
	 * @see AbstractConfiguration#port
	 */
	private int port;

	/**
	 * Classpath value.
	 *
	 * @see AbstractConfiguration#classpath
	 */
	private String classpath;

	/**
	 * Parent classloader.
	 *
	 * @see AbstractConfiguration#parentClassLoader
	 */
	private ClassLoader parentClassLoader;

	/**
	 * Map of properties.
	 *
	 * @see AbstractConfiguration#envProperties
	 */
	private final Map<String, String> envProperties;

	/**
	 * List of executable hooks.
	 *
	 * @see AbstractConfiguration#hooks
	 */
	private final List<Hook> hooks;

	/**
	 * The path of the custom web.xml descriptor.
	 */
	private String overrideDescriptor;

	/**
	 * Build default configuration.
	 */
	protected AbstractConfigurationBuilder() {
		this.path = DEFAULT_PATH;
		this.webapp = DEFAULT_WEBAPP;
		this.port = DEFAULT_PORT;
		this.classpath = DEFAULT_CLASSPATH;
		this.envProperties = new LinkedHashMap<>();
		this.hooks = new ArrayList<>();
		this.parentClassLoader = null;
	}

	/**
	 * The {@code this} object, useful to get correct chaining.
	 *
	 * @return this.
	 */
	protected abstract T self();

	/**
	 * Build the final configuration instance.
	 *
	 * @return The configuration instance.
	 */
	public abstract U build();

	/**
	 * Get current {@link #path}.
	 *
	 * @return {@link #path}.
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Get current {@link #webapp}.
	 *
	 * @return {@link #webapp}.
	 */
	public String getWebapp() {
		return webapp;
	}

	/**
	 * Get current {@link #classpath}.
	 *
	 * @return {@link #classpath}.
	 */
	public String getClasspath() {
		return classpath;
	}

	/**
	 * Get current {@link #port}.
	 *
	 * @return {@link #port}.
	 */
	public int getPort() {
		return port;
	}

	/**
	 * Get current {@link #envProperties}.
	 *
	 * @return {@link #envProperties}.
	 */
	public Map<String, String> getEnvProperties() {
		return envProperties;
	}

	/**
	 * Get current {@link #hooks}.
	 *
	 * @return {@link #hooks}.
	 */
	public List<Hook> getHooks() {
		return hooks;
	}

	/**
	 * Get current {@link #parentClassLoader}.
	 *
	 * @return {@link #parentClassLoader}.
	 * @deprecated Use {@link #getParentClassLoader()} instead.
	 */
	@Deprecated
	public ClassLoader getParentClasspath() {
		return getParentClassLoader();
	}

	/**
	 * Get current {@link #parentClassLoader}.
	 *
	 * @return {@link #parentClassLoader}.
	 */
	public ClassLoader getParentClassLoader() {
		return parentClassLoader;
	}

	/**
	 * Get current {@link #overrideDescriptor}.
	 *
	 * @return {@link #overrideDescriptor}.
	 */
	public String getOverrideDescriptor() {
		return overrideDescriptor;
	}

	/**
	 * Change {@link #path} value.
	 *
	 * @param path New {@link #path} value.
	 * @return this
	 * @throws NullPointerException If {@code path} is {@code null}.
	 */
	public T withPath(String path) {
		this.path = notNull(path, "path");
		return self();
	}

	/**
	 * Change {@link #webapp} value.
	 *
	 * @param webapp New {@link #webapp} value.
	 * @return this
	 * @throws NullPointerException If {@code webapp} is {@code null}.
	 */
	public T withWebapp(String webapp) {
		this.webapp = notNull(webapp, "webapp");
		return self();
	}

	/**
	 * Change {@link #webapp} value.
	 *
	 * @param webapp New {@link #webapp} value (the absolute path will be used).
	 * @return this
	 * @throws NullPointerException If {@code webapp} is null.
	 */
	public T withWebapp(File webapp) {
		notNull(webapp, "webapp");
		this.webapp = webapp.getAbsolutePath();
		return self();
	}

	/**
	 * Change {@link #port} value.
	 *
	 * @param port New {@link #port} value.
	 * @return this
	 * @throws IllegalArgumentException If {@code port} is strictly lower than zero.
	 */
	public T withPort(int port) {
		this.port = positive(port, "port");
		return self();
	}

	/**
	 * Change {@link #classpath} value.
	 *
	 * @param classpath New {@link #classpath} value.
	 * @return this
	 */
	public T withClasspath(String classpath) {
		this.classpath = classpath;
		return self();
	}

	/**
	 * Add new property entry to the {@link #envProperties} map.
	 *
	 * @param name Property name.
	 * @param value Property value.
	 * @return this
	 * @throws NullPointerException If {@code name} or {@code value} are {@code null}.
	 * @throws IllegalArgumentException if {@code name} is empty or blank.
	 */
	public T withProperty(String name, String value) {
		this.envProperties.put(
				notBlank(name, "name"),
				notNull(value, "value")
		);
		return self();
	}

	/**
	 * Add new executable hook to the {@link #hooks} list.
	 *
	 * @param hook Hook.
	 * @return this
	 * @throws NullPointerException if {@code hook} is {@code null}.
	 */
	public T withHook(Hook hook) {
		this.hooks.add(notNull(hook, "hook"));
		return self();
	}

	/**
	 * Change {@link #parentClassLoader} value.
	 *
	 * @param cls The class that will be used to get classloader.
	 * @param filter The file filter.
	 * @return this
	 * @deprecated The {@code filter} parameter will be useless in the next release for JDK9 compatibility, please use {@link #withParentClassLoader(Class)} instead.
	 */
	@Deprecated
	public T withParentClasspath(Class<?> cls, FileFilter filter) {
		return withParentClassLoader(cls);
	}

	/**
	 * Change {@link #parentClassLoader} value.
	 *
	 * @param cls The class that will be used to get classloader.
	 * @return this
	 * @deprecated Use {@link #withParentClassLoader(Class)} instead.
	 */
	@Deprecated
	public T withParentClasspath(Class<?> cls) {
		notNull(cls, "Base class");
		return withParentClassLoader(cls.getClassLoader());
	}

	/**
	 * Change {@link #parentClassLoader} value.
	 *
	 * @param cls The class that will be used to get classloader.
	 * @return this
	 */
	public T withParentClassLoader(Class<?> cls) {
		notNull(cls, "Base class");
		return withParentClassLoader(cls.getClassLoader());
	}

	/**
	 * Change {@link #parentClassLoader} value.
	 *
	 * @param classpath New classpath entry.
	 * @param others Other (optional) classpath urls.
	 * @return this
	 */
	public T withParentClasspath(URL classpath, URL... others) {
		Set<URL> classpathUrls = new HashSet<>();
		classpathUrls.add(classpath);
		Collections.addAll(classpathUrls, others);
		return withParentClasspath(classpathUrls);
	}

	/**
	 * Change {@link #parentClassLoader} value.
	 *
	 * @param classpath New {@link #parentClassLoader} value.
	 * @return this
	 */
	public T withParentClasspath(Collection<URL> classpath) {
		int nbUrls = classpath.size();
		if (nbUrls > 0) {
			URL[] urls = classpath.toArray(new URL[nbUrls]);
			URLClassLoader urlClassLoader = new URLClassLoader(urls);
			withParentClassLoader(urlClassLoader);
		}

		return self();
	}

	private T withParentClassLoader(ClassLoader parentClassLoader) {
		this.parentClassLoader = parentClassLoader;
		return self();
	}

	/**
	 * Change {@link #overrideDescriptor} (path of the custom web.xml file descriptor=.
	 *
	 * @param overrideDescriptor The new {@link #overrideDescriptor} value.
	 * @return this
	 */
	public T withOverrideDescriptor(String overrideDescriptor) {
		this.overrideDescriptor = overrideDescriptor;
		return self();
	}
}
