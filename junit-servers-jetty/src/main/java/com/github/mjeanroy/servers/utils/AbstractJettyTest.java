package com.github.mjeanroy.servers.utils;

import org.junit.ClassRule;

import com.github.mjeanroy.rules.ServerRule;
import com.github.mjeanroy.servers.rules.JettyServerRule;

/**
 * Simple abstraction that define a server rule using jetty as embedded server.
 */
public abstract class AbstractJettyTest {

	@ClassRule
	public static ServerRule server = new JettyServerRule();

}
