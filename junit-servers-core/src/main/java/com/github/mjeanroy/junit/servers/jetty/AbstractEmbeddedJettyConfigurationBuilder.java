/*
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

package com.github.mjeanroy.junit.servers.jetty;

import com.github.mjeanroy.junit.servers.servers.AbstractConfigurationBuilder;
import org.eclipse.jetty.util.resource.Resource;

import java.io.File;
import java.util.UUID;

import static com.github.mjeanroy.junit.servers.commons.lang.Preconditions.positive;
import static com.github.mjeanroy.junit.servers.jetty.AbstractEmbeddedJettyConfiguration.DEFAULT_STOP_AT_SHUTDOWN;
import static com.github.mjeanroy.junit.servers.jetty.AbstractEmbeddedJettyConfiguration.DEFAULT_STOP_TIMEOUT;

/**
 * Jetty configuration settings.
 */
abstract class AbstractEmbeddedJettyConfigurationBuilder<
	SELF extends AbstractEmbeddedJettyConfigurationBuilder<SELF, CONFIG>,
	CONFIG extends AbstractEmbeddedJettyConfiguration
> extends AbstractConfigurationBuilder<SELF, CONFIG> {

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

	/**
	 * If true, directory listings are returned if no welcome file is found.
	 * Else 403 Forbidden.
	 */
	private boolean dirAllowed;

	/**
	 * The jetty temp directory.
	 */
	private String tempDirectory;

	protected AbstractEmbeddedJettyConfigurationBuilder() {
		stopTimeout = DEFAULT_STOP_TIMEOUT;
		stopAtShutdown = DEFAULT_STOP_AT_SHUTDOWN;
		dirAllowed = true;

		// With jetty < 12.1.0, this was the default (i.e a `jsp` directory inside the current working directory).
		// With jetty >= 12.1.0, it seems it needs to be explicitely set.
		tempDirectory = new File("jsp").getAbsolutePath();;
	}

	/**
	 * Get the Jetty stop timeout (in ms).
	 *
	 * @return Stop timeout (in ms).
	 */
	public int getStopTimeout() {
		return stopTimeout;
	}

	/**
	 * Configure jetty embedded server to stop at shutdown.
	 *
	 * @return {@code true} if jetty is stopped when JVM shutdown, {@code false} otherwise.
	 */
	public boolean isStopAtShutdown() {
		return stopAtShutdown;
	}

	/**
	 * Jetty base resource.
	 *
	 * @return The base resource for the Jetty context that will be created.
	 */
	public Resource getBaseResource() {
		return baseResource;
	}

	/**
	 * The pattern to control which part of the container classpath will be processed.
	 *
	 * @return The pattern to control which part of the container classpath will be processed.
	 */
	public String getContainerJarPattern() {
		return containerJarPattern;
	}

	/**
	 * The pattern to control which part of the webinf directory classpath will be processed.
	 *
	 * @return The pattern to control which part of the webinf directory classpath will be processed.
	 */
	public String getWebInfJarPattern() {
		return webInfJarPattern;
	}

	/**
	 * The directory listing file:
	 *
	 * <ul>
	 *   <li>When {@code true}, directory listing is enabled.</li>
	 *   <li>When {@code false}, directory list is disabled.</li>
	 * </ul>
	 *
	 * @return {@code true} if directory listing is enabled, {@code false} otherwise.
	 */
	public boolean isDirAllowed() {
		return dirAllowed;
	}

	/**
	 * Get Jetty temp directory.
	 *
	 * @return Jetty temp directory.
	 */
	public String getTempDirectory() {
		return tempDirectory;
	}

	/**
	 * Update {@code stopTimeout} value.
	 *
	 * @param stopTimeout New stop timeout value.
	 * @return this
	 * @throws IllegalArgumentException If {@code stopTimeout} is not positive.
	 */
	public SELF withStopTimeout(int stopTimeout) {
		this.stopTimeout = positive(stopTimeout, "stopTimeout");
		return self();
	}

	/**
	 * Set {@code stopAtShutdown} to {@code false}.
	 *
	 * @return this
	 */
	public SELF disableStopAtShutdown() {
		return toggleStopAtShutdown(false);
	}

	/**
	 * Set {@code stopAtShutdown} to {@code true}.
	 * @return this
	 */
	public SELF enableStopAtShutdown() {
		return toggleStopAtShutdown(true);
	}

	/**
	 * Toggle {@code stopAtShutdown}.
	 *
	 * @param stopAtShutdown New {@link #stopAtShutdown} value.
	 * @return this
	 */
	private SELF toggleStopAtShutdown(boolean stopAtShutdown) {
		this.stopAtShutdown = stopAtShutdown;
		return self();
	}

	/**
	 * Change {@code baseResource} value.
	 *
	 * @param resource New jetty base resource.
	 * @return this
	 */
	public SELF withBaseResource(Resource resource) {
		this.baseResource = resource;
		return self();
	}

	/**
	 * Change {@code containerJarPattern} value.
	 *
	 * @param containerJarPattern The container JAR pattern.
	 * @return this
	 */
	public SELF withContainerJarPattern(String containerJarPattern) {
		this.containerJarPattern = containerJarPattern;
		return self();
	}

	/**
	 * Change {@code webInfJarPattern} value.
	 *
	 * @param webInfJarPattern The webinf JAR pattern.
	 * @return this
	 */
	public SELF withWebInfJarPattern(String webInfJarPattern) {
		this.webInfJarPattern = webInfJarPattern;
		return self();
	}

	/**
	 * Change {@code dirAllowed} value.
	 *
	 * @param dirAllowed Directory listing flag.
	 * @return this
	 */
	public SELF withDirAllowed(boolean dirAllowed) {
		this.dirAllowed = dirAllowed;
		return self();
	}

	/**
	 * Change {@code tempDirectory} value.
	 *
	 * @param tempDirectory Jetty Temp Directory.
	 * @return this
	 */
	public SELF withTempDirectory(String tempDirectory) {
		this.tempDirectory = tempDirectory;
		return self();
	}

	/**
	 * Change {@code tempDirectory} value to use a random directory inside `java.io.tmpdir`.
	 *
	 * @return this
	 */
	public SELF withRandomTempDirectory() {
		this.tempDirectory = System.getProperty("java.io.tmpdir") + "/" + UUID.randomUUID();
		return self();
	}
}
