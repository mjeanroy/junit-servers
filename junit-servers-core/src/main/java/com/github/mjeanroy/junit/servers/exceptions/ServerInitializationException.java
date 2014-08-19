package com.github.mjeanroy.junit.servers.exceptions;

/**
 * Exception thrown when server cannot be initialized.
 */
public class ServerInitializationException extends AbstractEmbeddedServerException {

	public ServerInitializationException(Throwable throwable) {
		super(throwable);
	}
}
