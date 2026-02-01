/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2026 <mickael.jeanroy@gmail.com>
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

import com.github.mjeanroy.junit.servers.commons.lang.ToStringBuilder;
import com.github.mjeanroy.junit.servers.servers.AbstractConfiguration;

import java.util.Objects;

/**
 * Tomcat configuration settings.
 */
abstract class AbstractEmbeddedTomcatConfiguration extends AbstractConfiguration {

	static final String DEFAULT_CLASSPATH = "./target/classes";
	static final String DEFAULT_BASE_DIR = "./tomcat-work";
	static final boolean DEFAULT_KEEP_BASE_DIR = false;
	static final boolean DEFAULT_ENABLE_NAMING = true;
	static final boolean DEFAULT_FORCE_META_INF = true;

	/**
	 * Tomcat Base Directory: this directory is where tomcat will store
	 * temporary files.
	 *
	 * @see org.apache.catalina.startup.Tomcat#setBaseDir(String)
	 */
	private final String baseDir;

	/**
	 * Keep Tomcat Base Directory content on server stop.
	 */
	private final boolean keepBaseDir;

	/**
	 * Flag used to enable / disable naming.
	 * This is a flag to enables JNDI naming.
	 *
	 * @see org.apache.catalina.startup.Tomcat#enableNaming()
	 */
	private final boolean enableNaming;

	/**
	 * Flag used to force META-INF directory creation
	 * for additional classpath entries.
	 */
	private final boolean forceMetaInf;

	/**
	 * Build new tomcat configuration.
	 *
	 * @param builder Builder object.
	 */
	AbstractEmbeddedTomcatConfiguration(
			AbstractEmbeddedTomcatConfigurationBuilder<?, ?> builder
	) {
		super(
			builder.getClasspath(),
			builder.getPath(),
			builder.getWebapp(),
			builder.getPort(),
			builder.getEnvProperties(),
			builder.getHooks(),
			builder.getParentClassLoader(),
			builder.getOverrideDescriptor()
		);

		this.baseDir = builder.getBaseDir();
		this.keepBaseDir = builder.isKeepBaseDir();
		this.enableNaming = builder.isEnableNaming();
		this.forceMetaInf = builder.isForceMetaInf();
	}

	/**
	 * Get {@code baseDir}.
	 *
	 * @return {@link #baseDir}
	 */
	public String getBaseDir() {
		return baseDir;
	}

	/**
	 * Get current {@code keepBaseDir} value.
	 *
	 * @return {@link #keepBaseDir}
	 */
	public boolean isKeepBaseDir() {
		return keepBaseDir;
	}

	/**
	 * Get {@code enableNaming}.
	 *
	 * @return {@link #enableNaming}
	 */
	public boolean isEnableNaming() {
		return enableNaming;
	}

	/**
	 * Get {@code forceMetaInf}.
	 *
	 * @return {@link #forceMetaInf}
	 */
	public boolean isForceMetaInf() {
		return forceMetaInf;
	}

	@Override
	public String toString() {
		return ToStringBuilder.create(getClass())
			.append("port", getPort())
			.append("path", getPath())
			.append("webapp", getWebapp())
			.append("classpath", getClasspath())
			.append("overrideDescriptor", getOverrideDescriptor())
			.append("parentClassLoader", getParentClassLoader())
			.append("baseDir", baseDir)
			.append("keepBaseDir", keepBaseDir)
			.append("enableNaming", enableNaming)
			.append("forceMetaInf", forceMetaInf)
			.build();
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}

		if (o instanceof AbstractEmbeddedTomcatConfiguration) {
			AbstractEmbeddedTomcatConfiguration c = (AbstractEmbeddedTomcatConfiguration) o;
			return c.canEqual(this)
				&& super.equals(c)
				&& Objects.equals(baseDir, c.baseDir)
				&& Objects.equals(keepBaseDir, c.keepBaseDir)
				&& Objects.equals(enableNaming, c.enableNaming)
				&& Objects.equals(forceMetaInf, c.forceMetaInf);
		}

		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(
				super.hashCode(),
				baseDir,
				keepBaseDir,
				enableNaming,
				forceMetaInf
		);
	}
}
