package com.github.mjeanroy.junit.servers.commons;

public final class Strings {

	private Strings() {
	}

	/**
	 * Check that given string is not blank.
	 *
	 * @param value String to check.
	 * @return True if string is not blank, false otherwise.
	 */
	public static boolean isNotBlank(String value) {
		return value != null && !value.trim().isEmpty();
	}
}
