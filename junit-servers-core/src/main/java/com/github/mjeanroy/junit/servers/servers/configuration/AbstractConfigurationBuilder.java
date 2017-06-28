/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2017 <mickael.jeanroy@gmail.com>
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

package com.github.mjeanroy.junit.servers.servers.configuration;

import com.github.mjeanroy.junit.servers.servers.Hook;

import java.io.File;
import java.io.FileFilter;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.github.mjeanroy.junit.servers.commons.Preconditions.notBlank;
import static com.github.mjeanroy.junit.servers.commons.Preconditions.notNull;
import static com.github.mjeanroy.junit.servers.commons.Preconditions.positive;

public abstract class AbstractConfigurationBuilder<T extends AbstractConfigurationBuilder<T, U>, U extends AbstractConfiguration> {

	public static final String DEFAULT_PATH = "/";

	public static final String DEFAULT_WEBAPP = "src/main/webapp";

	public static final String DEFAULT_CLASSPATH = ".";

	public static final int DEFAULT_PORT = 0;

	/**
	 * Path value.
	 *
	 * @see com.github.mjeanroy.junit.servers.servers.configuration.AbstractConfiguration#path
	 */
	private String path;

	/**
	 * Webapp value.
	 *
	 * @see com.github.mjeanroy.junit.servers.servers.configuration.AbstractConfiguration#webapp
	 */
	private String webapp;

	/**
	 * Port value.
	 *
	 * @see com.github.mjeanroy.junit.servers.servers.configuration.AbstractConfiguration#port
	 */
	private int port;

	/**
	 * Classpath value.
	 *
	 * @see com.github.mjeanroy.junit.servers.servers.configuration.AbstractConfiguration#classpath
	 */
	private String classpath;

	/**
	 * Parent classpath URLs
	 *
	 * @see com.github.mjeanroy.junit.servers.servers.configuration.AbstractConfiguration#parentClasspath
	 */
	private Collection<URL> parentClasspath;

	/**
	 * Map of properties.
	 *
	 * @see com.github.mjeanroy.junit.servers.servers.configuration.AbstractConfiguration#envProperties
	 */
	private final Map<String, String> envProperties;

	/**
	 * List of executable hooks.
	 *
	 * @see com.github.mjeanroy.junit.servers.servers.configuration.AbstractConfiguration#hooks
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
		this.envProperties = new HashMap<>();
		this.hooks = new LinkedList<>();
		this.parentClasspath = Collections.emptyList();
	}

	protected abstract T self();

	public abstract U build();

	public String getPath() {
		return path;
	}

	public String getWebapp() {
		return webapp;
	}

	public String getClasspath() {
		return classpath;
	}

	public int getPort() {
		return port;
	}

	public Map<String, String> getEnvProperties() {
		return envProperties;
	}

	public List<Hook> getHooks() {
		return hooks;
	}

	public Collection<URL> getParentClasspath() {
		return parentClasspath;
	}

	public String getOverrideDescriptor() {
		return overrideDescriptor;
	}

	/**
	 * Change path value.
	 *
	 * @param path New path value.
	 * @return this
	 * @throws NullPointerException if path is null.
	 */
	public T withPath(String path) {
		this.path = notNull(path, "path");
		return self();
	}

	/**
	 * Change webapp value.
	 *
	 * @param webapp New webapp value.
	 * @return this
	 * @throws NullPointerException if webapp is null.
	 */
	public T withWebapp(String webapp) {
		this.webapp = notNull(webapp, "webapp");
		return self();
	}

	/**
	 * Change webapp value.
	 *
	 * @param webapp New webapp value.
	 * @return this
	 * @throws NullPointerException if webapp is null.
	 */
	public T withWebapp(File webapp) {
		notNull(webapp, "webapp");
		this.webapp = webapp.getAbsolutePath();
		return self();
	}

	/**
	 * Change port value.
	 *
	 * @param port New port value.
	 * @return this
	 * @throws IllegalArgumentException if port is strictly lower than zero.
	 */
	public T withPort(int port) {
		this.port = positive(port, "port");
		return self();
	}

	/**
	 * Change classpath value.
	 *
	 * @param classpath New webapp value.
	 * @return this
	 */
	public T withClasspath(String classpath) {
		this.classpath = classpath;
		return self();
	}

	/**
	 * Add new property entry.
	 *
	 * @param name Property name.
	 * @param value Property value.
	 * @return this
	 * @throws NullPointerException if name or value is null.
	 * @throws IllegalArgumentException if name is empty or blank.
	 */
	public T withProperty(String name, String value) {
		this.envProperties.put(
				notBlank(name, "name"),
				notNull(value, "value")
		);
		return self();
	}

	/**
	 * Add new executable hook.
	 *
	 * @param hook Hook.
	 * @return this
	 * @throws NullPointerException if hook is null.
	 */
	public T withHook(Hook hook) {
		this.hooks.add(notNull(hook, "hook"));
		return self();
	}

	/**
	 * Change parent classpath value.
	 *
	 * @param classpath New webapp value.
	 * @return this
	 */
	public T withParentClasspath(Collection<URL> classpath) {
		this.parentClasspath = classpath;
		return self();
	}

	/**
	 * Change parent classpath value.
	 *
	 * @param cls The class that will be used to get classloader.
	 * @param filter The file filter.
	 * @return this
	 */
	public T withParentClasspath(Class<?> cls, FileFilter filter) {
		notNull(cls, "Base class must not be null");

		URLClassLoader urlClassLoader = (URLClassLoader) cls.getClassLoader();

		Set<URL> urls = new HashSet<>();
		for (URL url : urlClassLoader.getURLs()) {
			if (filter.accept(new File(url.getFile()))) {
				urls.add(url);
			}
		}

		return withParentClasspath(urls);
	}

	/**
	 * Change parent classpath value.
	 *
	 * @param cls The class that will be used to get classloader.
	 * @return this
	 */
	public T withParentClasspath(Class<?> cls) {
		return withParentClasspath(cls, new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return true;
			}
		});
	}

	/**
	 * Change parent classpath value.
	 *
	 * @param classpath New webapp value.
	 * @param others Other (optional) urls.
	 * @return this
	 */
	public T withParentClasspath(URL classpath, URL... others) {
		Set<URL> classpathUrls = new HashSet<>();
		classpathUrls.add(classpath);
		Collections.addAll(classpathUrls, others);
		return withParentClasspath(classpathUrls);
	}

	/**
	 * Change path of the custom web.xml file descriptor.
	 *
	 * @param overrideDescriptor The new path.
	 * @return this
	 */
	public T withOverrideDescriptor(String overrideDescriptor) {
		this.overrideDescriptor = overrideDescriptor;
		return self();
	}
}
