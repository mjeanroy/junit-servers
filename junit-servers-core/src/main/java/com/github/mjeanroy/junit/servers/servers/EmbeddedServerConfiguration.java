package com.github.mjeanroy.junit.servers.servers;

import java.io.File;

public class EmbeddedServerConfiguration {

	/** Server Path. */
	private String path;

	/** Webapp Path. */
	private String webapp;

	/** Server port, default is to use a random port. */
	private int port;

	public EmbeddedServerConfiguration() {
		this.port = 0;
		this.path = "/";
		this.webapp = "src/main/webapp";
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
	public EmbeddedServerConfiguration withPath(String path) {
		this.path = path;
		return this;
	}

	/**
	 * Change server port.
	 *
	 * @param port New port.
	 * @return this.
	 */
	public EmbeddedServerConfiguration withPort(int port) {
		this.port = port;
		return this;
	}

	/**
	 * Change webapp path.
	 *
	 * @param webapp New webapp path.
	 * @return this.
	 */
	public EmbeddedServerConfiguration withWebapp(String webapp) {
		this.webapp = webapp;
		return this;
	}

	/**
	 * Change webapp path.
	 *
	 * @param webapp New webapp path.
	 * @return this.
	 */
	public EmbeddedServerConfiguration withWebapp(File webapp) {
		this.webapp = webapp.getAbsolutePath();
		return this;
	}
}
