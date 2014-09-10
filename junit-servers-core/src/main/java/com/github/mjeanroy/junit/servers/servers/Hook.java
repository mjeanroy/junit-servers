package com.github.mjeanroy.junit.servers.servers;

import javax.servlet.ServletContext;

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

	/**
	 * Method invoked when server is fully started.
	 *
	 * @param server Server.
	 * @param servletContext Servlet context started within container.
	 */
	void onStarted(EmbeddedServer server, ServletContext servletContext);
}
