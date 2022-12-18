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

import com.github.mjeanroy.junit.servers.servers.AbstractConfigurationBuilder;
import org.eclipse.jetty.util.resource.Resource;

import static com.github.mjeanroy.junit.servers.commons.lang.Preconditions.positive;
import static com.github.mjeanroy.junit.servers.jetty.AbstractEmbeddedJettyConfiguration.DEFAULT_STOP_AT_SHUTDOWN;
import static com.github.mjeanroy.junit.servers.jetty.AbstractEmbeddedJettyConfiguration.DEFAULT_STOP_TIMEOUT;

/**
 * Jetty configuration settings.
 */
abstract class AbstractEmbeddedJettyConfigurationBuilder<
		BUILDER extends AbstractEmbeddedJettyConfigurationBuilder<BUILDER, CONFIG>,
		CONFIG extends AbstractEmbeddedJettyConfiguration
> extends AbstractConfigurationBuilder<BUILDER, CONFIG> {

	/**
	 * Configure the stop timeout in milliseconds: set a graceful stop time.
	 *
	 * @see AbstractEmbeddedJettyConfiguration#DEFAULT_STOP_TIMEOUT
	 * @see org.eclipse.jetty.server.Server#setStopTimeout(long)
	 */
	private int stopTimeout;

	/**
	 * Configure jetty embedded server to stop at shutdown.
	 *
	 * <p>
	 *
	 * If true, the server instance will be explicitly stopped when the
	 * JVM is shutdown. Otherwise the JVM is stopped with the server running.
	 *
	 * @see AbstractEmbeddedJettyConfiguration#DEFAULT_STOP_AT_SHUTDOWN
	 * @see org.eclipse.jetty.server.Server#setStopAtShutdown(boolean)
	 */
	private boolean stopAtShutdown;

	/**
	 * The base resource for the Jetty context that will be created.
	 */
	private Resource baseResource;

	/**
	 * The pattern to control which part of the container classpath will
	 * be processed.
	 */
	private String containerJarPattern;

	/**
	 * The pattern to control which part of the webinf directory classpath will
	 * be processed.
	 */
	private String webInfJarPattern;

	protected AbstractEmbeddedJettyConfigurationBuilder() {
		stopTimeout = DEFAULT_STOP_TIMEOUT;
		stopAtShutdown = DEFAULT_STOP_AT_SHUTDOWN;
	}

	/**
	 * Get current {@link #stopTimeout} value.
	 *
	 * @return {@link #stopTimeout}.
	 */
	public int getStopTimeout() {
		return stopTimeout;
	}

	/**
	 * Get current {@link #stopAtShutdown} value.
	 *
	 * @return {@link #stopAtShutdown}.
	 */
	public boolean isStopAtShutdown() {
		return stopAtShutdown;
	}

	/**
	 * Get current {@link #baseResource} value.
	 *
	 * @return {@link #baseResource}.
	 */
	public Resource getBaseResource() {
		return baseResource;
	}

	/**
	 * Get current {@link #containerJarPattern} value.
	 *
	 * @return  {@link #containerJarPattern}
	 */
	public String getContainerJarPattern() {
		return containerJarPattern;
	}

	/**
	 * Get current {@link #webInfJarPattern} value.
	 *
	 * @return {@link #webInfJarPattern}
	 */
	public String getWebInfJarPattern() {
		return webInfJarPattern;
	}

	/**
	 * Update {@link #stopTimeout} value.
	 *
	 * @param stopTimeout New {@link #stopTimeout} value.
	 * @return this
	 * @throws IllegalArgumentException If {@code stopTimeout} is not positive.
	 */
	public BUILDER withStopTimeout(int stopTimeout) {
		this.stopTimeout = positive(stopTimeout, "stopTimeout");
		return self();
	}

	/**
	 * Set {@link #stopAtShutdown} to {@code false}.
	 * @return this
	 */
	public BUILDER disableStopAtShutdown() {
		return toggleStopAtShutdown(false);
	}

	/**
	 * Set {@link #stopAtShutdown} to {@code true}.
	 * @return this
	 */
	public BUILDER enableStopAtShutdown() {
		return toggleStopAtShutdown(true);
	}

	/**
	 * Toggle {@link #stopAtShutdown}.
	 *
	 * @param stopAtShutdown New {@link #stopAtShutdown} value.
	 * @return this
	 */
	private BUILDER toggleStopAtShutdown(boolean stopAtShutdown) {
		this.stopAtShutdown = stopAtShutdown;
		return self();
	}

	/**
	 * Change {@link #baseResource} value.
	 *
	 * @param resource New {@link #baseResource} value.
	 * @return this
	 */
	public BUILDER withBaseResource(Resource resource) {
		this.baseResource = resource;
		return self();
	}

	/**
	 * Change {@link #containerJarPattern} value.
	 *
	 * @param containerJarPattern The container JAR pattern.
	 * @return this
	 */
	public BUILDER withContainerJarPattern(String containerJarPattern) {
		this.containerJarPattern = containerJarPattern;
		return self();
	}

	/**
	 * Change {@link #webInfJarPattern} value.
	 *
	 * @param webInfJarPattern The webinf JAR pattern.
	 * @return this
	 */
	public BUILDER withWebInfJarPattern(String webInfJarPattern) {
		this.webInfJarPattern = webInfJarPattern;
		return self();
	}
}
