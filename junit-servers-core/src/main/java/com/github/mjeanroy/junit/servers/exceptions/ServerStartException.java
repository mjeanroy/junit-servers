package com.github.mjeanroy.junit.servers.exceptions;

/**
 * Exception thrown when server cannot be started.
 */
public class ServerStartException extends AbstractEmbeddedServerException {

	public ServerStartException(Throwable throwable) {
		super(throwable);
	}
}
