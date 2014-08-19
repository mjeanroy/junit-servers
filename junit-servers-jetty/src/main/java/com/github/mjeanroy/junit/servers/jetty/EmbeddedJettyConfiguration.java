package com.github.mjeanroy.junit.servers.jetty;

import java.util.Objects;

import com.github.mjeanroy.junit.servers.servers.AbstractEmbeddedServerConfiguration;

public class EmbeddedJettyConfiguration extends AbstractEmbeddedServerConfiguration<EmbeddedJettyConfiguration> {

	public EmbeddedJettyConfiguration() {
		super();
	}

	/**
	 * Build new configuration object from existing jetty configuration.
	 *
	 * @param configuration Jetty Configuration.
	 */
	public EmbeddedJettyConfiguration(EmbeddedJettyConfiguration configuration) {
		super(configuration);
	}

	@Override
	public String toString() {
		return String.format(
				"%s {path=%s, webapp=%s, port=%s, classpath=%s}",
				getClass().getSimpleName(),
				path, webapp, port, classpath
		);
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}

		if (o instanceof EmbeddedJettyConfiguration) {
			EmbeddedJettyConfiguration c = (EmbeddedJettyConfiguration) o;
			return Objects.equals(path, c.path)
					&& Objects.equals(webapp, c.webapp)
					&& Objects.equals(port, c.port)
					&& Objects.equals(classpath, c.classpath);
		}

		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(path, webapp, port, classpath);
	}
}
