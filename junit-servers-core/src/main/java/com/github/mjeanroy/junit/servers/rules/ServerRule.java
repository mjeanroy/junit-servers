package com.github.mjeanroy.junit.servers.rules;

import org.junit.rules.ExternalResource;

import com.github.mjeanroy.junit.servers.servers.EmbeddedServer;

/**
 * Rule that can be used to start and stop embedded server.
 */
public class ServerRule extends ExternalResource {

	/**
	 * Embedded server that will be start and stopped.
	 */
	private final EmbeddedServer server;

	/**
	 * Create rule.
	 *
	 * @param server Embedded server.
	 */
	public ServerRule(EmbeddedServer server) {
		this.server = server;
	}

	@Override
	protected void before() {
		start();
	}

	@Override
	protected void after() {
		stop();
	}

	/**
	 * Start embedded server.
	 *
	 * @see com.github.mjeanroy.junit.servers.servers.EmbeddedServer#start()
	 */
	public void start() {
		server.start();
	}

	/**
	 * Stop embedded server.
	 *
	 * @see com.github.mjeanroy.junit.servers.servers.EmbeddedServer#stop()
	 */
	public void stop() {
		server.stop();
	}

	/**
	 * Restart embedded server.
	 *
	 * @see com.github.mjeanroy.junit.servers.servers.EmbeddedServer#restart()
	 */
	public void restart() {
		server.restart();
	}

	/**
	 * Check if embedded server is started.
	 *
	 * @return True if embedded server is started, false otherwise.
	 * @see com.github.mjeanroy.junit.servers.servers.EmbeddedServer#isStarted()
	 */
	public boolean isStarted() {
		return server.isStarted();
	}

	/**
	 * Get port used by embedded server.
	 *
	 * @return Port.
	 * @see com.github.mjeanroy.junit.servers.servers.EmbeddedServer#getPort()
	 */
	public int getPort() {
		return server.getPort();
	}

	/**
	 * Get embedded server.
	 *
	 * @return Server.
	 */
	public EmbeddedServer getServer() {
		return server;
	}
}
