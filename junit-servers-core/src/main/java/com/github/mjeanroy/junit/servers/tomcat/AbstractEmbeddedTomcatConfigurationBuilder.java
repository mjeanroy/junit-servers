/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2022 <mickael.jeanroy@gmail.com>
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

import com.github.mjeanroy.junit.servers.servers.AbstractConfigurationBuilder;

import static com.github.mjeanroy.junit.servers.commons.lang.Preconditions.notNull;
import static com.github.mjeanroy.junit.servers.tomcat.AbstractEmbeddedTomcatConfiguration.DEFAULT_BASE_DIR;
import static com.github.mjeanroy.junit.servers.tomcat.AbstractEmbeddedTomcatConfiguration.DEFAULT_CLASSPATH;
import static com.github.mjeanroy.junit.servers.tomcat.AbstractEmbeddedTomcatConfiguration.DEFAULT_ENABLE_NAMING;
import static com.github.mjeanroy.junit.servers.tomcat.AbstractEmbeddedTomcatConfiguration.DEFAULT_FORCE_META_INF;
import static com.github.mjeanroy.junit.servers.tomcat.AbstractEmbeddedTomcatConfiguration.DEFAULT_KEEP_BASE_DIR;

abstract class AbstractEmbeddedTomcatConfigurationBuilder<
	SELF extends AbstractConfigurationBuilder<SELF, CONFIG>,
	CONFIG extends AbstractEmbeddedTomcatConfiguration
> extends AbstractConfigurationBuilder<SELF, CONFIG> {

	/**
	 * The tomcat baseDir configuration: this directory is where tomcat will store
	 * temporary files.
	 * Default is {@link AbstractEmbeddedTomcatConfiguration#DEFAULT_BASE_DIR}.
	 *
	 * @see AbstractEmbeddedTomcatConfiguration#DEFAULT_BASE_DIR
	 * @see org.apache.catalina.startup.Tomcat#setBaseDir(String)
	 */
	private String baseDir;

	/**
	 * Keep tomcat base directory content on server stop.
	 * Default is {@link AbstractEmbeddedTomcatConfiguration#DEFAULT_KEEP_BASE_DIR}.
	 *
	 * @see AbstractEmbeddedTomcatConfiguration#DEFAULT_KEEP_BASE_DIR
	 */
	private boolean keepBaseDir = DEFAULT_KEEP_BASE_DIR;

	/**
	 * Enable/Disable naming: this is a flag to enables JNDI naming.
	 * Default is {@link AbstractEmbeddedTomcatConfiguration#DEFAULT_ENABLE_NAMING}.
	 *
	 * @see AbstractEmbeddedTomcatConfiguration#DEFAULT_ENABLE_NAMING}
	 * @see org.apache.catalina.startup.Tomcat#enableNaming()
	 */
	private boolean enableNaming;

	/**
	 * Force the creation of the {@code META-INF} directory if it does not exist
	 * in the classpath.
	 */
	private boolean forceMetaInf;

	AbstractEmbeddedTomcatConfigurationBuilder() {
		baseDir = DEFAULT_BASE_DIR;
		enableNaming = DEFAULT_ENABLE_NAMING;
		forceMetaInf = DEFAULT_FORCE_META_INF;

		withClasspath(DEFAULT_CLASSPATH);
	}

	/**
	 * Get current {@code baseDir} value.
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
	 * Get current {@code enableNaming} value.
	 *
	 * @return {@link #enableNaming}
	 */
	public boolean isEnableNaming() {
		return enableNaming;
	}

	/**
	 * Get current {@code forceMetaInf} value.
	 *
	 * @return {@link #forceMetaInf}
	 */
	public boolean isForceMetaInf() {
		return forceMetaInf;
	}

	/**
	 * Change tomcat base directory.
	 *
	 * @param baseDir Base directory.
	 * @return this.
	 * @throws NullPointerException If {@code baseDir} is {@code null}.
	 */
	public SELF withBaseDir(String baseDir) {
		this.baseDir = notNull(baseDir, "baseDir");
		return self();
	}

	/**
	 * Keep tomcat base directory content on server stop.
	 *
	 * @return this.
	 */
	public SELF keepBaseDir() {
		this.keepBaseDir = true;
		return self();
	}

	/**
	 * Delete tomcat base directory on server stop.
	 *
	 * @return this.
	 */
	public SELF deleteBaseDir() {
		this.keepBaseDir = false;
		return self();
	}

	/**
	 * Enable naming (i.e enable JNDI) on tomcat server.
	 *
	 * @return this.
	 */
	public SELF enableNaming() {
		return toggleNaming(true);
	}

	/**
	 * Disable naming (i.e disable JNDI) on tomcat server.
	 *
	 * @return this.
	 */
	public SELF disableNaming() {
		return toggleNaming(false);
	}

	/**
	 * Enable META-INF creation.
	 *
	 * @return this.
	 */
	public SELF enableForceMetaInf() {
		return toggleMetaInf(true);
	}

	/**
	 * Disable META-INF creation.
	 *
	 * @return this.
	 */
	public SELF disableForceMetaInf() {
		return toggleMetaInf(false);
	}

	/**
	 * Toggle {@link #enableNaming} to a new value.
	 *
	 * @param enableNaming New {@link #enableNaming}
	 * @return this.
	 */
	private SELF toggleNaming(boolean enableNaming) {
		this.enableNaming = enableNaming;
		return self();
	}

	/**
	 * Toggle {@link #forceMetaInf} to a new value.
	 *
	 * @param forceMetaInf New {@link #forceMetaInf}
	 * @return this.
	 */
	private SELF toggleMetaInf(boolean forceMetaInf) {
		this.forceMetaInf = forceMetaInf;
		return self();
	}
}
