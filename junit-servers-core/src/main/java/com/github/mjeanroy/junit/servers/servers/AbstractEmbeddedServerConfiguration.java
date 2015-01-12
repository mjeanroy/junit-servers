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

package com.github.mjeanroy.junit.servers.servers;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.mjeanroy.junit.servers.commons.Checks.notBlank;
import static com.github.mjeanroy.junit.servers.commons.Checks.notNull;
import static com.github.mjeanroy.junit.servers.commons.Checks.positive;
import static java.util.Collections.unmodifiableList;
import static java.util.Collections.unmodifiableMap;

public abstract class AbstractEmbeddedServerConfiguration<T extends AbstractEmbeddedServerConfiguration> {

	// Default classpath
	// Should be enough for most projects but can be overridden (could be necessary for sub-projects)
	public static final String DEFAULT_CLASSPATH = ".";

	/**
	 * Server Path.
	 * This path is "/" by default, but it can be customized (and path
	 * suffix will have to be used to query application url).
	 */
	protected String path;

	/**
	 * Webapp Path.
	 * By default, the path follow maven convention and is
	 * set to "src/main/webapp".
	 * Path should be customized if project do not follow maven
	 * convention or webapp path must be set to maven sub-module.
	 */
	protected String webapp;

	/**
	 * Server port, default is to use a random port.
	 * Use this configuration port to define a specific port, otherwise
	 * use a random port to be sure application start on an
	 * available port.
	 */
	protected int port;

	/**
	 * Additional classpath.
	 * The path will be added to application classpath
	 * before server is started.
	 * This additional classpath entry is useful to start
	 * application configured with java configuration instead
	 * of "classic" web.xml file.
	 */
	protected String classpath;

	/**
	 * Map of environment properties to set before server start.
	 *
	 * These properties could also be set / unset using
	 * a custom hooks but since it is a common tasks, this is
	 * supported under the hood.
	 *
	 * Note that custom properties will be set before embedded server
	 * is started and removed after server is stopped.
	 */
	protected final Map<String, String> envProperties;

	/**
	 * Execution hooks.
	 * These hooks will be executed during server start and stop
	 * phases.
	 * Be careful that these hooks should be thread safe and
	 * stateless.
	 */
	protected final List<Hook> hooks;

	/**
	 * Build default configuration.
	 */
	protected AbstractEmbeddedServerConfiguration() {
		this.port = 0;
		this.path = "/";
		this.webapp = "src/main/webapp";
		this.classpath = DEFAULT_CLASSPATH;
		this.envProperties = new HashMap<>();
		this.hooks = new ArrayList<>();
	}

	/**
	 * Create new embedded configuration by copying existing configuration.
	 *
	 * @param configuration Configuration.
	 */
	protected AbstractEmbeddedServerConfiguration(T configuration) {
		notNull(configuration, "configuration");

		this.port = configuration.getPort();
		this.path = configuration.getPath();
		this.webapp = configuration.getWebapp();
		this.classpath = configuration.getClasspath();

		this.envProperties = new HashMap<>(configuration.getEnvProperties());
		this.hooks = new ArrayList<>(configuration.getHooks());
	}

	/**
	 * Get configuration port.
	 *
	 * @return Port.
	 */
	public int getPort() {
		return port;
	}

	/**
	 * Get configuration path.
	 *
	 * @return Path.
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Get configuration webapp.
	 *
	 * @return Webapp path.
	 */
	public String getWebapp() {
		return webapp;
	}

	/**
	 * Get configuration classpath.
	 *
	 * @return Classpath.
	 */
	public String getClasspath() {
		return classpath;
	}

	/**
	 * Get configuration hooks.
	 * Note that returned list is an unmodifiable list.
	 *
	 * @return List of hooks.
	 */
	public List<Hook> getHooks() {
		return unmodifiableList(hooks);
	}

	/**
	 * Get environment properties.
	 * Note that returned map is an unmodifiable map.
	 *
	 * @return Properties.
	 */
	public Map<String, String> getEnvProperties() {
		return unmodifiableMap(envProperties);
	}

	/**
	 * Change server path.
	 *
	 * @param path New path.
	 * @return this.
	 */
	@SuppressWarnings("unchecked")
	public T withPath(String path) {
		this.path = notNull(path, "path");
		return (T) this;
	}

	/**
	 * Change server port.
	 *
	 * @param port New port.
	 * @return this.
	 */
	@SuppressWarnings("unchecked")
	public T withPort(int port) {
		this.port = positive(port, "port");
		return (T) this;
	}

	/**
	 * Change webapp path.
	 *
	 * @param webapp New webapp path.
	 * @return this.
	 */
	@SuppressWarnings("unchecked")
	public T withWebapp(String webapp) {
		this.webapp = notNull(webapp, "webapp");
		return (T) this;
	}

	/**
	 * Change webapp path.
	 *
	 * @param webapp New webapp path.
	 * @return this.
	 */
	@SuppressWarnings("unchecked")
	public T withWebapp(File webapp) {
		notNull(webapp, "webapp");
		this.webapp = webapp.getAbsolutePath();
		return (T) this;
	}

	/**
	 * Change additional classpath value.
	 *
	 * @param classpath New classpath.
	 * @return this.
	 */
	@SuppressWarnings("unchecked")
	public T withClasspath(String classpath) {
		// May be null (disable classpath entry).
		this.classpath = classpath;
		return (T) this;
	}

	/**
	 * Add environment property to set before server start.
	 * Property will be removed once server is stopped.
	 *
	 * @param name  Property name.
	 * @param value Property value.
	 * @return this.
	 */
	@SuppressWarnings("unchecked")
	public T withProperty(String name, String value) {
		envProperties.put(
				notBlank(name, "name"),
				notNull(value, "value")
		);
		return (T) this;
	}

	/**
	 * Add hook that allow code to be executed before and after server start / stop.
	 *
	 * @param hook Hook.
	 * @return this.
	 */
	@SuppressWarnings("unchecked")
	public T withHook(Hook hook) {
		hooks.add(notNull(hook, "hook"));
		return (T) this;
	}
}
