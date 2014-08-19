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

	protected final String classpath;

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
	}

	@Override
	public void start() {
		synchronized (this) {
			if (!isStarted()) {
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
