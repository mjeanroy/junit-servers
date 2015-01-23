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

package com.github.mjeanroy.junit.servers.servers.configuration;

import com.github.mjeanroy.junit.servers.servers.Hook;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
	 * Build default configuration.
	 */
	protected AbstractConfigurationBuilder() {
		this.path = DEFAULT_PATH;
		this.webapp = DEFAULT_WEBAPP;
		this.port = DEFAULT_PORT;
		this.classpath = DEFAULT_CLASSPATH;
		this.envProperties = new HashMap<>();
		this.hooks = new LinkedList<>();
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
}
