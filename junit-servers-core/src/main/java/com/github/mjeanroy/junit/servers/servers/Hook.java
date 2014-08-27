package com.github.mjeanroy.junit.servers.servers;

/**
 * Hook that will be invoked before and after server execution.
 */
public interface Hook {

	/**
	 * Method invoked before server starts.
	 *
	 * @param server Server.
	 */
	void pre(EmbeddedServer server);

	/**
	 * Method invoked before server stops.
	 *
	 * @param server Server.
	 */
	void post(EmbeddedServer server);
}
