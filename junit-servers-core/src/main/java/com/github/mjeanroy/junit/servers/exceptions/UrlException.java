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

package com.github.mjeanroy.junit.servers.exceptions;

/**
 * Exception thrown when URL cannot be built because of syntax error.
 */
public class UrlException extends AbstractException {

	/**
	 * Scheme used to build malformed URL.
	 */
	private final String scheme;

	/**
	 * Host used to build malformed URL.
	 */
	private final String host;

	/**
	 * Port used to build malformed URL.
	 */
	private final int port;

	/**
	 * Path used to build malformed URL.
	 */
	private final String path;

	/**
	 * Create the exception.
	 *
	 * @param scheme HTTP Url scheme.
	 * @param host HTTP Url host.
	 * @param port HTTP Url port.
	 * @param path HTTP Url path.
	 * @param cause Original cause.
	 */
	public UrlException(String scheme, String host, int port, String path, Throwable cause) {
		super(cause);

		this.scheme = scheme;
		this.host = host;
		this.port = port;
		this.path = path;
	}

	/**
	 * The scheme used to build malformed URL.
	 *
	 * @return URL scheme.
	 */
	public String getScheme() {
		return scheme;
	}

	/**
	 * The host used to build malformed URL.
	 *
	 * @return URL host.
	 */
	public String getHost() {
		return host;
	}

	/**
	 * The port used to build malformed URL.
	 *
	 * @return URL port.
	 */
	public int getPort() {
		return port;
	}

	/**
	 * The path used to build malformed URL.
	 *
	 * @return URL path.
	 */
	public String getPath() {
		return path;
	}
}
