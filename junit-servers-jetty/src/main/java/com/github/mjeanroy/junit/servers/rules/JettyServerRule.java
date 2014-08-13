package com.github.mjeanroy.junit.servers.rules;

import com.github.mjeanroy.junit.servers.jetty.EmbeddedJetty;

/**
 * Rule that can be used to start and stop embedded jetty server.
 */
public class JettyServerRule extends ServerRule {

	/** Create rule using jetty as embedded server. */
	public JettyServerRule() {
		super(new EmbeddedJetty());
	}

	/**
	 * Create rule.
	 *
	 * @param jetty Jetty Embedded Server.
	 */
	public JettyServerRule(EmbeddedJetty jetty) {
		super(jetty);
	}

}
