package com.github.mjeanroy.junit.servers.utils;

import org.junit.ClassRule;

import com.github.mjeanroy.junit.servers.rules.ServerRule;
import com.github.mjeanroy.junit.servers.rules.JettyServerRule;

/**
 * Simple abstraction that define a server rule using jetty as embedded server.
 */
public abstract class AbstractJettyTest {

	@ClassRule
	public static ServerRule server = new JettyServerRule();

}
