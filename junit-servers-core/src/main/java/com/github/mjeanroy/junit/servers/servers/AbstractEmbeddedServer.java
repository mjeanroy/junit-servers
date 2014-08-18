package com.github.mjeanroy.junit.servers.servers;

/**
 * Partial implementation of an embedded server.
 */
public abstract class AbstractEmbeddedServer implements EmbeddedServer {

	/** Use lock object to be sure embedded server cannot be started twice. */
	protected static final Object lock = new Object();

	// Volatile because it can be accessed from more than one thread
	/** Flag to keep server status. */
	private volatile boolean started;

	/** Force specific port. */
	protected final int port;

	/** Server path, default is '/' */
	protected final String path;

	/** Webapp Path. */
	protected final String webapp;

	/** Build default embedded server. */
	public AbstractEmbeddedServer() {
		// Build default configuration
		this(new AbstractEmbeddedServerConfiguration() {
		});
	}

	/**
	 * Build default embedded.
	 *
	 * @param configuration Server configuration.
	 */
	public AbstractEmbeddedServer(AbstractEmbeddedServerConfiguration configuration) {
		this.port = configuration.getPort();
		this.path = configuration.getPath();
		this.webapp = configuration.getWebapp();
	}

	@Override
	public void start() {
		synchronized (lock) {
			if (!isStarted()) {
				doStart();
			}

			started = true;
		}
	}

	@Override
	public void stop() {
		synchronized (lock) {
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
