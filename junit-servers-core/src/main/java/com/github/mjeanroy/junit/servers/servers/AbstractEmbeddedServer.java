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

import static java.lang.String.format;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Partial implementation of an embedded server.
 */
public abstract class AbstractEmbeddedServer implements EmbeddedServer {

	// Volatile because it can be accessed from more than one thread
	/** Flag to keep server status. */
	private volatile boolean started;

	/** Force specific port. */
	protected final int port;

	/** Server path, default is '/' */
	protected final String path;

	/** Webapp Path. */
	protected final String webapp;

	/** Additional classpath directory path. */
	protected final String classpath;

	/** Environment properties to create when server starts and destroy when server stops. */
	protected final Map<String, String> properties;

	/** Old properties used to restore initial environment properties values when server stops. */
	protected final Map<String, String> oldProperties;

	/** Hooks that will be invoked before and after server execution. */
	protected final List<Hook> hooks;

	/**
	 * Build default embedded.
	 *
	 * @param configuration Server configuration.
	 */
	public AbstractEmbeddedServer(AbstractEmbeddedServerConfiguration configuration) {
		this.port = configuration.getPort();
		this.path = configuration.getPath();
		this.webapp = configuration.getWebapp();
		this.classpath = configuration.getClasspath();
		this.oldProperties = new HashMap<>();
		this.properties = configuration.getEnvProperties();
		this.hooks = configuration.getHooks();
	}

	@Override
	public void start() {
		synchronized (this) {
			if (!isStarted()) {
				initEnvironment();
				execHooks(true);
				doStart();
			}

			started = true;
		}
	}

	@Override
	public void stop() {
		synchronized (this) {
			if (isStarted()) {
				doStop();
				execHooks(false);
				destroyEnvironment();
			}

			started = false;
		}
	}

	@Override
	public boolean isStarted() {
		return started;
	}

	@Override
	public void restart() {
		stop();
		start();
	}

	@Override
	public String getPath() {
		return path;
	}

	/**
	 * Add custom environment properties.
	 */
	private void initEnvironment() {
		// Already in synchronized block, don't need to synchronize anything
		for (Map.Entry<String, String> property : properties.entrySet()) {
			String name = property.getKey();
			String newValue = property.getValue();

			String oldValue = System.getProperty(property.getKey());
			oldProperties.put(name, oldValue);

			System.setProperty(name, newValue);
		}
	}

	/**
	 * Reset custom environment properties.
	 */
	private void destroyEnvironment() {
		// Already in synchronized block, don't need to synchronize anything
		for (Map.Entry<String, String> property : properties.entrySet()) {
			String name = property.getKey();

			String oldValue = oldProperties.get(name);
			oldProperties.remove(name);

			if (oldValue == null) {
				System.clearProperty(name);
			} else {
				System.setProperty(name, oldValue);
			}
		}
	}

	private void execHooks(boolean pre) {
		for (Hook hook : hooks) {
			if (pre) {
				hook.pre(this);
			} else {
				hook.post(this);
			}
		}
	}

	@Override
	public String getUrl() {
		int port = getPort();
		String path = getPath();
		if (!path.isEmpty() && path.charAt(0) != '/') {
			path = "/" + path;
		}

		return format("http://localhost:%s%s", port, path);
	}

	/**
	 * Start embedded server.
	 * Must block until server is fully started.
	 */
	protected abstract void doStart();

	/**
	 * Stop embedded server.
	 * Must block until server is fully stopped.
	 */
	protected abstract void doStop();
}
