/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2018 <mickael.jeanroy@gmail.com>
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

package com.github.mjeanroy.junit.servers.servers;

import javax.servlet.ServletContext;

/**
 * Specification of embedded server.
 *
 * <p>
 *
 * An embedded server:
 * <ul>
 *   <li>Can be started, stopped or restarted.</li>
 *   <li>Must provide port that can be used to query resources.</li>
 * </ul>
 *
 * @param <T> The type of configuration used by the embedded server implementation.
 */
public interface EmbeddedServer<T extends AbstractConfiguration> {

	/**
	 * Start embedded server.
	 * If server is already started, this method should do nothing.
	 */
	void start();

	/**
	 * Stop embedded server.
	 * If server is already stopped, this method should do nothing.
	 */
	void stop();

	/**
	 * Restart embedded server.
	 */
	void restart();

	/**
	 * Return server configuration.
	 *
	 * @return Configuration.
	 */
	T getConfiguration();

	/**
	 * Check if embedded server is started.
	 *
	 * @return {@code true} if embedded server is started, {@code false} otherwise.
	 */
	boolean isStarted();

	/**
	 * Get the protocol scheme ({@code "http"}, {@code "https"}).
	 *
	 * @return Protocol scheme.
	 */
	String getScheme();

	/**
	 * Get the host, should be {@code "localhost"}, unless specific configuration.
	 *
	 * @return The server hostname.
	 */
	String getHost();

	/**
	 * Get port used by embedded server.
	 *
	 * <p>
	 *
	 * Note that:
	 * <ul>
	 *   <li>If the server is not started, the returned port should be the one set in the configuration.</li>
	 *   <li>Otherwise, the "real" port should be returned (the port used by the embedded server)</li>
	 * </ul>
	 *
	 * @return Port.
	 */
	int getPort();

	/**
	 * Get server context path.
	 *
	 * @return Server context path.
	 */
	String getPath();

	/**
	 * Get URL to query embedded server.
	 *
	 * @return URL.
	 */
	String getUrl();

	/**
	 * Get servlet context used within container.
	 * If container is not a servlet container, this method should return null.
	 *
	 * @return Servlet Context from container.
	 */
	ServletContext getServletContext();
}
