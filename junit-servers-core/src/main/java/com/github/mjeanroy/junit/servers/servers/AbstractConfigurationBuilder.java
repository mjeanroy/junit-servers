/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2026 <mickael.jeanroy@gmail.com>
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

import static com.github.mjeanroy.junit.servers.commons.lang.Preconditions.notBlank;
import static com.github.mjeanroy.junit.servers.commons.lang.Preconditions.notNull;
import static com.github.mjeanroy.junit.servers.commons.lang.Preconditions.positive;
import static com.github.mjeanroy.junit.servers.servers.AbstractConfiguration.DEFAULT_CLASSPATH;
import static com.github.mjeanroy.junit.servers.servers.AbstractConfiguration.DEFAULT_PATH;
import static com.github.mjeanroy.junit.servers.servers.AbstractConfiguration.DEFAULT_PORT;
import static com.github.mjeanroy.junit.servers.servers.AbstractConfiguration.DEFAULT_WEBAPP;

/// Builder for [AbstractConfiguration] instances, should be extended by custom configuration implementation.
///
/// @param <SELF> Type of extended builder.
/// @param <CONFIG> Type of [AbstractConfiguration] implementation.
public abstract class AbstractConfigurationBuilder<SELF extends AbstractConfigurationBuilder<SELF, CONFIG>, CONFIG extends AbstractConfiguration> {

	/// Path value.
	///
	/// @see AbstractConfiguration#getPath()
	private String path;

	/// Webapp value.
	///
	/// @see AbstractConfiguration#getWebapp()
	private String webapp;

	/// Port value.
	///
	/// @see AbstractConfiguration#getPort()
	private int port;

	/// Classpath value.
	///
	/// @see AbstractConfiguration#getClasspath()
	private String classpath;

	/// Parent classloader.
	///
	/// @see AbstractConfiguration#getParentClassLoader()
	private ClassLoader parentClassLoader;

	/// Map of properties.
	///
	/// @see AbstractConfiguration#getEnvProperties()
	private final Map<String, String> envProperties;

	/// List of executable hooks.
	///
	/// @see AbstractConfiguration#getHooks()
	private final List<Hook> hooks;

	/// The path of the custom web.xml descriptor.
	///
	/// @see AbstractConfiguration#getOverrideDescriptor()
	private String overrideDescriptor;

	/// Build default configuration.
	protected AbstractConfigurationBuilder() {
		this.path = DEFAULT_PATH;
		this.webapp = DEFAULT_WEBAPP;
		this.port = DEFAULT_PORT;
		this.classpath = DEFAULT_CLASSPATH;
		this.envProperties = new LinkedHashMap<>();
		this.hooks = new ArrayList<>();
		this.parentClassLoader = null;
	}

	/// The `this` object, useful to get correct chaining.
	///
	/// @return this.
	protected abstract SELF self();

	/// Build the final configuration instance.
	///
	/// @return The configuration instance.
	public abstract CONFIG build();

	/// Get current [#path].
	///
	/// @return [#path].
	public String getPath() {
		return path;
	}

	/// Get current [#webapp].
	///
	/// @return [#webapp].
	public String getWebapp() {
		return webapp;
	}

	/// Get current [#classpath].
	///
	/// @return [#classpath].
	public String getClasspath() {
		return classpath;
	}

	/// Get current [#port].
	///
	/// @return [#port].
	public int getPort() {
		return port;
	}

	/// Get current [#envProperties].
	///
	/// @return [#envProperties].
	public Map<String, String> getEnvProperties() {
		return envProperties;
	}

	/// Get current [#hooks].
	///
	/// @return [#hooks].
	public List<Hook> getHooks() {
		return hooks;
	}

	/// Get current [#parentClassLoader].
	///
	/// @return [#parentClassLoader].
	public ClassLoader getParentClassLoader() {
		return parentClassLoader;
	}

	/// Get current [#overrideDescriptor].
	///
	/// @return [#overrideDescriptor].
	public String getOverrideDescriptor() {
		return overrideDescriptor;
	}

	/// Change [#path] value.
	///
	/// @param path New [#path] value.
	/// @return this
	/// @throws NullPointerException If `path` is `null`.
	public SELF withPath(String path) {
		this.path = notNull(path, "path");
		return self();
	}

	/// Change [#webapp] value.
	///
	/// @param webapp New [#webapp] value.
	/// @return this
	/// @throws NullPointerException If `webapp` is `null`.
	public SELF withWebapp(String webapp) {
		this.webapp = notNull(webapp, "webapp");
		return self();
	}

	/// Change [#webapp] value.
	///
	/// @param webapp New [#webapp] value (the absolute path will be used).
	/// @return this
	/// @throws NullPointerException If `webapp` is null.
	public SELF withWebapp(File webapp) {
		notNull(webapp, "webapp");
		this.webapp = webapp.getAbsolutePath();
		return self();
	}

	/// Change [#port] value.
	///
	/// @param port New [#port] value.
	/// @return this
	/// @throws IllegalArgumentException If `port` is strictly lower than zero.
	public SELF withPort(int port) {
		this.port = positive(port, "port");
		return self();
	}

	/// Change [#classpath] value.
	///
	/// @param classpath New [#classpath] value.
	/// @return this
	public SELF withClasspath(String classpath) {
		this.classpath = classpath;
		return self();
	}

	/// Add new property entry to the [#envProperties] map.
	///
	/// @param name Property name.
	/// @param value Property value.
	/// @return this
	/// @throws NullPointerException If `name` or `value` are `null`.
	/// @throws IllegalArgumentException if `name` is empty or blank.
	public SELF withProperty(String name, String value) {
		this.envProperties.put(
			notBlank(name, "name"),
			notNull(value, "value")
		);
		return self();
	}

	/// Add new executable hook to the [#hooks] list.
	///
	/// @param hook Hook.
	/// @return this
	/// @throws NullPointerException if `hook` is `null`.
	public SELF withHook(Hook hook) {
		this.hooks.add(notNull(hook, "hook"));
		return self();
	}

	/// Change [#parentClassLoader] value.
	///
	/// @param cls The class that will be used to get classloader.
	/// @return this
	public SELF withParentClassLoader(Class<?> cls) {
		notNull(cls, "Base class");
		return withParentClassLoader(cls.getClassLoader());
	}

	/// Change [#parentClassLoader] value.
	///
	/// @param classpath New classpath entry.
	/// @param others Other (optional) classpath urls.
	/// @return this
	public SELF withParentClasspath(URL classpath, URL... others) {
		Set<URL> classpathUrls = new HashSet<>();
		classpathUrls.add(classpath);
		Collections.addAll(classpathUrls, others);
		return withParentClasspath(classpathUrls);
	}

	/// Change [#parentClassLoader] value.
	///
	/// @param classpath New [#parentClassLoader] value.
	/// @return this
	public SELF withParentClasspath(Collection<URL> classpath) {
		int nbUrls = classpath.size();
		if (nbUrls > 0) {
			URL[] urls = classpath.toArray(new URL[nbUrls]);
			URLClassLoader urlClassLoader = new URLClassLoader(urls);
			withParentClassLoader(urlClassLoader);
		}

		return self();
	}

	private SELF withParentClassLoader(ClassLoader parentClassLoader) {
		this.parentClassLoader = parentClassLoader;
		return self();
	}

	/// Change [#overrideDescriptor] (path of the custom web.xml file descriptor=.
	///
	/// @param overrideDescriptor The new [#overrideDescriptor] value.
	/// @return this
	public SELF withOverrideDescriptor(String overrideDescriptor) {
		this.overrideDescriptor = overrideDescriptor;
		return self();
	}
}
