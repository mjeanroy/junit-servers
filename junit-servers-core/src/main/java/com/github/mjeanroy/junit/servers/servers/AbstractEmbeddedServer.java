package com.github.mjeanroy.junit.servers.servers;

/**
 * Partial implementation of an embedded server.
 */
public abstract class AbstractEmbeddedServer implements EmbeddedServer {

	/** Flag to keep server status. */
	private boolean started;

	/** Force specific port. */
	protected final int port;

	/** Server path, default is '/' */
	protected final String path;

	/** Webapp Path. */
	protected final String webapp;

	/** Build default embedded. */
	public AbstractEmbeddedServer() {
		this(new EmbeddedServerConfiguration());
	}

	/**
	 * Build default embedded.
	 *
	 * @param configuration Server configuration.
	 */
	public AbstractEmbeddedServer(EmbeddedServerConfiguration configuration) {
		this.port = configuration.getPort();
		this.path = configuration.getPath();
		this.webapp = configuration.getWebapp();
	}

	@Override
	public void start() {
		if (!isStarted()) {
			doStart();
		}

		started = true;
	}

	@Override
	public void stop() {
		if (isStarted()) {
			doStop();
		}

		started = false;
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

	/** Start embedded server. */
	protected abstract void doStart();

	/** Stop embedded server. */
	protected abstract void doStop();
}
