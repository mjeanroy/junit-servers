/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2022 <mickael.jeanroy@gmail.com>
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

import com.github.mjeanroy.junit.servers.loggers.Logger;
import com.github.mjeanroy.junit.servers.loggers.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.github.mjeanroy.junit.servers.commons.core.Urls.ensureAbsolutePath;
import static com.github.mjeanroy.junit.servers.commons.lang.Preconditions.notNull;
import static java.lang.System.getProperty;

/**
 * Partial implementation of an embedded server.
 *
 * <p>
 *
 * Subclasses should implement {@link #doStart()} and {@link #doStop()} methods and synchronization is already
 * managed by this abstract implementation.
 */
public abstract class AbstractEmbeddedServer<S, T extends AbstractConfiguration> implements EmbeddedServer<T> {

	/**
	 * Class Logger.
	 */
	private static final Logger log = LoggerFactory.getLogger(AbstractEmbeddedServer.class);

	/**
	 * The default scheme returned by {@link AbstractEmbeddedServer#getScheme()}.
	 */
	private static final String DEFAULT_SCHEME = "http";

	/**
	 * The default host returned by {@link AbstractEmbeddedServer#getHost()}.
	 */
	private static final String DEFAULT_HOST = "localhost";

	private static final String SCHEME_SEPARATOR = "://";

	private static final String PORT_SEPARATOR = ":";

	/**
	 * Server configuration.
	 */
	protected final T configuration;

	/**
	 * Flag to keep server status:
	 * <ul>
	 *   <li>Server can be started if and only if status is equal to {@link ServerStatus#STOPPED}.</li>
	 *   <li>Server can be stopped if and only if status is equal to {@link ServerStatus#STARTED}.</li>
	 * </ul>
	 */
	private volatile ServerStatus status;

	/**
	 * Old properties used to restore initial environment properties values when server stops.
	 * It can be used to set a spring profile property or anything else.
	 */
	private final Map<String, String> oldProperties;

	// Lock used to synchronize start and stop tasks
	private static final Object lock = new Object();

	/**
	 * Build default embedded server.
	 *
	 * @param configuration Server configuration.
	 */
	protected AbstractEmbeddedServer(T configuration) {
		this.configuration = notNull(configuration, "configuration");
		this.status = ServerStatus.STOPPED;
		this.oldProperties = new LinkedHashMap<>();
	}

	@Override
	public void start() {
		log.debug("Attempt to start embedded server (current status is: {})", status);
		if (status != ServerStatus.STARTED) {
			synchronized (lock) {
				log.debug("Lock acquired, starting server (current status is: {})", status);
				if (status != ServerStatus.STARTED) {
					status = ServerStatus.STARTING;
					initEnvironment();
					execHooks(true);
					doStart();
					status = ServerStatus.STARTED;

					// Server is fully initialized
					onStarted();
				}
			}

			log.debug("Lock released, current status is now: {}", status);
		}
	}

	@Override
	public void stop() {
		log.debug("Attempt to stop embedded server (current status is: {})", status);
		if (status != ServerStatus.STOPPED) {
			synchronized (lock) {
				log.debug("Lock acquired, stopping server (current status is: {})", status);
				if (status != ServerStatus.STOPPED) {
					status = ServerStatus.STOPPING;
					execHooks(false);
					doStop();
					destroyEnvironment();
					status = ServerStatus.STOPPED;
				}
			}

			log.debug("Lock released, current status is now: {}", status);
		}
	}

	@Override
	public boolean isStarted() {
		return status == ServerStatus.STARTED;
	}

	@Override
	public void restart() {
		log.debug("Restarting embedded server");
		stop();
		start();
	}

	@Override
	public int getPort() {
		return isStarted() ? doGetPort() : configuration.getPort();
	}

	@Override
	public String getPath() {
		return configuration.getPath();
	}

	/**
	 * Add custom environment properties.
	 * Initial property value will be store in {@link #oldProperties} map
	 * and will be restore later.
	 */
	private void initEnvironment() {
		log.debug("Initialize environment properties");
		for (Map.Entry<String, String> property : configuration.getEnvProperties().entrySet()) {
			String name = property.getKey();
			String newValue = property.getValue();

			String oldValue = getProperty(property.getKey());
			oldProperties.put(name, oldValue);

			log.trace("Setting environment property: {} --> {}", name, newValue);
			System.setProperty(name, newValue);
		}
	}

	/**
	 * Reset custom environment properties.
	 * Initial values stored in {@link #oldProperties} will be restored
	 * or clear.
	 */
	private void destroyEnvironment() {
		log.debug("Resetting environment properties");
		for (Map.Entry<String, String> property : configuration.getEnvProperties().entrySet()) {
			String name = property.getKey();

			String oldValue = oldProperties.get(name);
			oldProperties.remove(name);

			if (oldValue == null) {
				log.trace("Clear environment property: {}", name);
				System.clearProperty(name);
			}
			else {
				log.trace("Setting environment property: {}", name);
				System.setProperty(name, oldValue);
			}
		}
	}

	/**
	 * Exec hooks phase.
	 *
	 * @param pre Phase to execute (true => pre ; false => post).
	 */
	private void execHooks(boolean pre) {
		log.debug("Executing embedded server lifecycle hooks (pre = {})", pre);
		for (Hook hook : configuration.getHooks()) {
			if (pre) {
				hook.pre(this);
			}
			else {
				hook.post(this);
			}
		}
	}

	private void onStarted() {
		log.error("Executing `onStarted` embedded server lifecycle hooks");
		for (Hook hook : configuration.getHooks()) {
			hook.onStarted(this, getServletContext());
		}
	}

	@Override
	public String getScheme() {
		return DEFAULT_SCHEME;
	}

	@Override
	public String getHost() {
		return DEFAULT_HOST;
	}

	@Override
	public String getUrl() {
		final String scheme = getScheme();
		final String host = getHost();
		final String port = String.valueOf(getPort());
		final String path = ensureAbsolutePath(getPath());
		final int initialSize = scheme.length() + host.length() + port.length() + path.length() + SCHEME_SEPARATOR.length() + PORT_SEPARATOR.length();
		return new StringBuilder(initialSize)
			.append(scheme)
			.append(SCHEME_SEPARATOR)
			.append(host)
			.append(PORT_SEPARATOR)
			.append(port)
			.append(path)
			.toString();
	}

	@Override
	public T getConfiguration() {
		return configuration;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <SERVLET_CLASS> SERVLET_CLASS getServletContext(Class<SERVLET_CLASS> klass) {
		Object servletContext = getServletContext();
		if (servletContext == null) {
			return null;
		}

		Class<?> actualClass = servletContext.getClass();
		if (!klass.isAssignableFrom(actualClass)) {
			throw new IllegalArgumentException("Cannot cast '" + actualClass.getName() + "' to '" + klass.getName() + "'");
		}

		return (SERVLET_CLASS) servletContext;
	}


	/**
	 * Get internal server implementation.
	 * Note that this method should not be used to start
	 * or stop internal server, use dedicated method instead.
	 *
	 * This method can be used to do some custom configuration
	 * on original implementation.
	 *
	 * @return Original server implementation.
	 */
	public abstract S getDelegate();

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

	/**
	 * Get port once server is started.
	 *
	 * @return The port.
	 */
	protected abstract int doGetPort();
}
