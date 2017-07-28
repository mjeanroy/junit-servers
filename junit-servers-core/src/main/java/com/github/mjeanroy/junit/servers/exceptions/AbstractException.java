package com.github.mjeanroy.junit.servers.exceptions;

abstract class AbstractException extends RuntimeException {

	/**
	 * Wrap existing exception.
	 *
	 * @param throwable Original exception.
	 */
	AbstractException(Throwable throwable) {
		super(throwable);
	}

	/**
	 * Create exception with specific message.
	 *
	 * @param msg Message.
	 */
	AbstractException(String msg) {
		super(msg);
	}

	/**
	 * Wrap existing exception.
	 *
	 * @param message Error message.
	 * @param throwable Original exception.
	 */
	AbstractException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
