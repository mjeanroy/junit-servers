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

package com.github.mjeanroy.junit.servers.jetty;

import com.github.mjeanroy.junit.servers.commons.lang.ToStringBuilder;
import com.github.mjeanroy.junit.servers.servers.AbstractConfiguration;
import org.eclipse.jetty.util.resource.Resource;

import java.util.Objects;

/**
 * Jetty configuration settings.
 */
abstract class AbstractEmbeddedJettyConfiguration extends AbstractConfiguration {

	static final int DEFAULT_STOP_TIMEOUT = 30000;
	static final boolean DEFAULT_STOP_AT_SHUTDOWN = true;

	/**
	 * Configure the stop timeout in milliseconds: set a graceful stop time.
	 *
	 * @see org.eclipse.jetty.server.Server#setStopTimeout(long)
	 */
	private final int stopTimeout;

	/**
	 * Configure jetty embedded server to stop at shutdown.
	 *
	 * <p>
	 *
	 * If true, the server instance will be explicitly stopped when the
	 * JVM is shutdown. Otherwise the JVM is stopped with the server running.
	 *
	 * @see org.eclipse.jetty.server.Server#setStopAtShutdown(boolean)
	 */
	private final boolean stopAtShutdown;

	/**
	 * The Jetty base resource (default is "./src/main/webapp").
	 */
	private final Resource baseResource;

	/**
	 * Control which parts of the container’s classpath should be processed for things like annotations,
	 * META-INF/resources, META-INF/web-fragment.xml and tlds inside META-INF.
	 */
	private final String containerJarPattern;

	/**
	 * Control which parts of the webinf’s classpath should be processed for things like annotations,
	 * META-INF/resources, META-INF/web-fragment.xml and tlds inside META-INF.
	 */
	private final String webInfJarPattern;

	/**
	 * If true, directory listings are returned if no welcome file is found.
	 * Else 403 Forbidden.
	 */
	private final boolean dirAllowed;

	/**
	 * The jetty temp directory.
	 */
	private final String tempDirectory;

	AbstractEmbeddedJettyConfiguration(
			AbstractEmbeddedJettyConfigurationBuilder<?, ?> builder
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

		this.stopTimeout = builder.getStopTimeout();
		this.stopAtShutdown = builder.isStopAtShutdown();
		this.baseResource = builder.getBaseResource();
		this.containerJarPattern = builder.getContainerJarPattern();
		this.webInfJarPattern = builder.getWebInfJarPattern();
		this.dirAllowed = builder.isDirAllowed();
		this.tempDirectory = builder.getTempDirectory();
	}

	/**
	 * Get {@code stopTimeout}.
	 *
	 * @return {@link #stopTimeout}
	 */
	public int getStopTimeout() {
		return stopTimeout;
	}

	/**
	 * Get {@code stopAtShutdown}.
	 *
	 * @return {@link #stopAtShutdown}
	 */
	public boolean isStopAtShutdown() {
		return stopAtShutdown;
	}

	/**
	 * Get {@code baseResource}.
	 *
	 * @return {@link #baseResource}
	 */
	public Resource getBaseResource() {
		return baseResource;
	}

	/**
	 * Get {@code containerJarPattern}
	 *
	 * @return {@link #containerJarPattern}
	 */
	public String getContainerJarPattern() {
		return containerJarPattern;
	}

	/**
	 * Get {@code webInfJarPattern}
	 *
	 * @return {@link #webInfJarPattern}
	 */
	public String getWebInfJarPattern() {
		return webInfJarPattern;
	}

	/**
	 * Get {@code dirAllowed}
	 *
	 * @return {@link #dirAllowed}
	 */
	public boolean isDirAllowed() {
		return dirAllowed;
	}

	/**
	 * Get {@code tempDirectory}
	 *
	 * @return {@link #tempDirectory}
	 */
	public String getTempDirectory() {
		return tempDirectory;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}

		if (o instanceof AbstractEmbeddedJettyConfiguration) {
			AbstractEmbeddedJettyConfiguration c = (AbstractEmbeddedJettyConfiguration) o;
			return c.canEqual(this)
				&& super.equals(c)
				&& Objects.equals(stopTimeout, c.stopTimeout)
				&& Objects.equals(stopAtShutdown, c.stopAtShutdown)
				&& Objects.equals(baseResource, c.baseResource)
				&& Objects.equals(containerJarPattern, c.containerJarPattern)
				&& Objects.equals(webInfJarPattern, c.webInfJarPattern)
				&& Objects.equals(dirAllowed, c.dirAllowed)
				&& Objects.equals(tempDirectory, c.tempDirectory);
		}

		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(
			super.hashCode(),
			stopTimeout,
			stopAtShutdown,
			baseResource,
			containerJarPattern,
			webInfJarPattern,
			dirAllowed,
			tempDirectory
		);
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
			.append("stopTimeout", stopTimeout)
			.append("stopAtShutdown", stopAtShutdown)
			.append("baseResource", baseResource)
			.append("containerJarPattern", containerJarPattern)
			.append("webInfJarPattern", webInfJarPattern)
			.append("dirAllowed", dirAllowed)
			.append("tempDirectory", tempDirectory)
			.build();
	}
}
