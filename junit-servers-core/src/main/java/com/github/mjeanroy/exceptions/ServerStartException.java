package com.github.mjeanroy.exceptions;

/**
 * Exception thrown when server cannot be started.
 */
public class ServerStartException extends AbstractEmbeddedServerException {

	public ServerStartException(Throwable throwable) {
		super(throwable);
	}
}
