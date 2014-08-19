package com.github.mjeanroy.junit.servers.tomcat;

import java.util.Objects;

import com.github.mjeanroy.junit.servers.servers.AbstractEmbeddedServerConfiguration;

public class EmbeddedTomcatConfiguration extends AbstractEmbeddedServerConfiguration<EmbeddedTomcatConfiguration> {

	/** Tomcat Base Directory. */
	private String baseDir;

	/** Flag used to enable / disable naming. */
	private boolean enableNaming;

	/** Flag used to force META-INF directory creation for additional classpath entries. */
	private boolean forceMetaInf;

	/** Build new tomcat configuration. */
	public EmbeddedTomcatConfiguration() {
		super();
		this.baseDir = "./tomcat-work";
		this.enableNaming = true;
		this.forceMetaInf = true;

		// Standard target directory for maven projects
		// Should be changed for other projects
		this.classpath += "/target/classes";
	}

	/**
	 * Create new tomcat configuration by copying existing configuration.
	 *
	 * @param configuration Configuration.
	 */
	public EmbeddedTomcatConfiguration(EmbeddedTomcatConfiguration configuration) {
		super(configuration);
		this.baseDir = configuration.baseDir;
		this.forceMetaInf = configuration.forceMetaInf;
		this.enableNaming = configuration.enableNaming;
	}

	public String getBaseDir() {
		return baseDir;
	}

	public boolean getEnableNaming() {
		return enableNaming;
	}

	public boolean getForceMetaInf() {
		return forceMetaInf;
	}

	/**
	 * Change tomcat base directory.
	 *
	 * @param baseDir Base directory.
	 * @return this.
	 */
	public EmbeddedTomcatConfiguration withBaseDir(String baseDir) {
		this.baseDir = baseDir;
		return this;
	}

	/**
	 * Enable naming on tomcat server.
	 *
	 * @return this.
	 */
	public EmbeddedTomcatConfiguration enableNaming() {
		return toggleNaming(true);
	}

	/**
	 * Disable naming on tomcat server.
	 *
	 * @return this.
	 */
	public EmbeddedTomcatConfiguration disableNaming() {
		return toggleNaming(false);
	}

	/**
	 * Enable META-INF creation.
	 *
	 * @return this.
	 */
	public EmbeddedTomcatConfiguration enableForceMetaInf() {
		return toggleMetaInf(true);
	}

	/**
	 * Disable META-INF creation.
	 *
	 * @return this.
	 */
	public EmbeddedTomcatConfiguration disableForceMetaInf() {
		return toggleMetaInf(false);
	}

	private EmbeddedTomcatConfiguration toggleNaming(boolean enableNaming) {
		this.enableNaming = enableNaming;
		return this;
	}

	private EmbeddedTomcatConfiguration toggleMetaInf(boolean forceMetaInf) {
		this.forceMetaInf = forceMetaInf;
		return this;
	}

	@Override
	public String toString() {
		return String.format(
				"%s {path=%s, webapp=%s, port=%s, classpath=%s, baseDir=%s, enableNaming=%s, forceMetaInf=%s}",
				getClass().getSimpleName(),
				path, webapp, port, classpath,
				baseDir, enableNaming, forceMetaInf
		);
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}

		if (o instanceof EmbeddedTomcatConfiguration) {
			EmbeddedTomcatConfiguration c = (EmbeddedTomcatConfiguration) o;
			return Objects.equals(path, c.path)
					&& Objects.equals(webapp, c.webapp)
					&& Objects.equals(port, c.port)
					&& Objects.equals(classpath, c.classpath)
					&& Objects.equals(baseDir, c.baseDir)
					&& Objects.equals(enableNaming, c.enableNaming)
					&& Objects.equals(forceMetaInf, c.forceMetaInf);
		}

		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(path, webapp, port, classpath, baseDir, enableNaming, forceMetaInf);
	}
}
