package com.github.mjeanroy.junit.servers.rules;

import com.github.mjeanroy.junit.servers.tomcat.EmbeddedTomcat;

/**
 * Rule that can be used to start and stop embedded tomcat server.
 */
public class TomcatServerRule extends ServerRule {

	/** Create rule using tomcat as embedded server. */
	public TomcatServerRule() {
		super(new EmbeddedTomcat());
	}

	/**
	 * Create rule.
	 *
	 * @param tomcat Tomcat Embedded Server.
	 */
	public TomcatServerRule(EmbeddedTomcat tomcat) {
		super(tomcat);
	}

}
