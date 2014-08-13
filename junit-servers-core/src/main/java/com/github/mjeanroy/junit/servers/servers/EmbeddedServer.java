package com.github.mjeanroy.junit.servers.servers;

/**
 * Specification of embedded server.
 * An embedded server:
 * - Can be started, stopped or restarted.
 * - Must provide port that can be used to query resources.
 */
public interface EmbeddedServer {

	/**
	 * Start embedded server.
	 * If server is already started, this method do nothing.
	 */
	void start();

	/**
	 * Stop embedded server.
	 * If server is already stopped, this method do nothing.
	 */
	void stop();

	/** Restart embedded server. */
	void restart();

	/**
	 * Check if embedded server is started.
	 * @return True if embedded server is started, false otherwise.
	 */
	boolean isStarted();

	/** Get port used by embedded server. */
	int getPort();

}
