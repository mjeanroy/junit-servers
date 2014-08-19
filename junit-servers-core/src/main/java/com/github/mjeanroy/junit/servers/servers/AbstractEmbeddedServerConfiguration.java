package com.github.mjeanroy.junit.servers.servers;

import java.io.File;

public abstract class AbstractEmbeddedServerConfiguration<T extends AbstractEmbeddedServerConfiguration> {

	// Default classpath
	// Should be enough for most projects but can be overridden (could be necessary for sub-projects)
	public static final String DEFAULT_CLASSPATH = ".";

	/** Server Path. */
	protected String path;

	/** Webapp Path. */
	protected String webapp;

	/** Server port, default is to use a random port. */
	protected int port;

	/** Additional classpath. */
	protected String classpath;

	public AbstractEmbeddedServerConfiguration() {
		this.port = 0;
		this.path = "/";
		this.webapp = "src/main/webapp";
		this.classpath = DEFAULT_CLASSPATH;
	}

	/**
	 * Create new embedded configuration by copying existing configuration.
	 *
	 * @param configuration Configuration.
	 */
	public AbstractEmbeddedServerConfiguration(AbstractEmbeddedServerConfiguration configuration) {
		this.port = configuration.getPort();
		this.path = configuration.getPath();
		this.webapp = configuration.getWebapp();
		this.classpath = configuration.getClasspath();
	}

	public int getPort() {
		return port;
	}

	public String getPath() {
		return path;
	}

	public String getWebapp() {
		return webapp;
	}

	public String getClasspath() {
		return classpath;
	}

	/**
	 * Change server path.
	 *
	 * @param path New path.
	 * @return this.
	 */
	public T withPath(String path) {
		this.path = path;
		return (T) this;
	}

	/**
	 * Change server port.
	 *
	 * @param port New port.
	 * @return this.
	 */
	public T withPort(int port) {
		this.port = port;
		return (T) this;
	}

	/**
	 * Change webapp path.
	 *
	 * @param webapp New webapp path.
	 * @return this.
	 */
	public T withWebapp(String webapp) {
		this.webapp = webapp;
		return (T) this;
	}

	/**
	 * Change webapp path.
	 *
	 * @param webapp New webapp path.
	 * @return this.
	 */
	public T withWebapp(File webapp) {
		this.webapp = webapp.getAbsolutePath();
		return (T) this;
	}

	/**
	 * Change additional classpath value.
	 *
	 * @param classpath New classpath.
	 * @return this.
	 */
	public T withClasspath(String classpath) {
		this.classpath = classpath;
		return (T) this;
	}
}
