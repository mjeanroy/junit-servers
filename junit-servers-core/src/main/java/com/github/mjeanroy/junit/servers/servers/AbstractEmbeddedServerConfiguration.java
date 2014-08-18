package com.github.mjeanroy.junit.servers.servers;

import java.io.File;

public abstract class AbstractEmbeddedServerConfiguration<T extends AbstractEmbeddedServerConfiguration> {

	/** Server Path. */
	private String path;

	/** Webapp Path. */
	private String webapp;

	/** Server port, default is to use a random port. */
	private int port;

	public AbstractEmbeddedServerConfiguration() {
		this.port = 0;
		this.path = "/";
		this.webapp = "src/main/webapp";
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
}
