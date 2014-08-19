package com.github.mjeanroy.junit.servers.utils;

import org.junit.ClassRule;

import com.github.mjeanroy.junit.servers.rules.ServerRule;
import com.github.mjeanroy.junit.servers.rules.TomcatServerRule;

/**
 * Simple abstraction that define a server rule using tomcat as embedded server.
 */
public abstract class AbstractTomcatTest {

	@ClassRule
	public static ServerRule server = new TomcatServerRule();

}
