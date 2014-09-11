/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 <mickael.jeanroy@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.github.mjeanroy.junit.servers.tomcat;

import static com.github.mjeanroy.junit.servers.commons.Checks.notNull;

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
		this.baseDir = notNull(baseDir, "baseDir");
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
