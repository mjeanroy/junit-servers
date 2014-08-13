package com.github.mjeanroy.junit.servers.exceptions;

/**
 * Exception thrown when embedded server fail.
 */
public abstract class AbstractEmbeddedServerException extends RuntimeException {

	public AbstractEmbeddedServerException(Throwable throwable) {
		super(throwable);
	}
}
