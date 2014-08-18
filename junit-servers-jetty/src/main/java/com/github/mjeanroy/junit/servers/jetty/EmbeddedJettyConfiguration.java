package com.github.mjeanroy.junit.servers.jetty;

import com.github.mjeanroy.junit.servers.servers.AbstractEmbeddedServerConfiguration;

public class EmbeddedJettyConfiguration extends AbstractEmbeddedServerConfiguration<EmbeddedJettyConfiguration> {

	// Default classpath
	// Should be enough for most projects but can be overridden (could be necessary for sub-projects)
	public static final String DEFAULT_CLASSPATH = ".";

	/** Additional classpath. */
	private String classpath;

	public EmbeddedJettyConfiguration() {
		super();
		this.classpath = DEFAULT_CLASSPATH;
	}

	/**
	 * Build new configuration object from existing jetty configuration.
	 *
	 * @param configuration Jetty Configuration.
	 */
	public EmbeddedJettyConfiguration(EmbeddedJettyConfiguration configuration) {
		super(configuration);
		this.classpath = configuration.getClasspath();
	}

	public String getClasspath() {
		return classpath;
	}

	/**
	 * Change additional classpath value.
	 *
	 * @param classpath New classpath.
	 * @return this.
	 */
	public EmbeddedJettyConfiguration withClasspath(String classpath) {
		this.classpath = classpath;
		return this;
	}
}
