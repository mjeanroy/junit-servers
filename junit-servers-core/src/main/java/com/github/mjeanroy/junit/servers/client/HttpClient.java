/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2017 <mickael.jeanroy@gmail.com>
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

package com.github.mjeanroy.junit.servers.client;

/**
 * Http client that can be used to query embedded server.
 * After test suite, client should be properly destroyed
 * using {#destroy} method.
 */
public interface HttpClient {

	/**
	 * Create GET request.
	 *
	 * @param url URL, full url or path relative to server url.
	 * @return GET request.
	 */
	HttpRequest prepareGet(String url);

	/**
	 * Create POST request.
	 *
	 * @param url URL, full url or path relative to server url.
	 * @return POST request.
	 */
	HttpRequest preparePost(String url);

	/**
	 * Create PUT request.
	 *
	 * @param url URL, full url or path relative to server url.
	 * @return PUT request.
	 */
	HttpRequest preparePut(String url);

	/**
	 * Create DELETE request.
	 *
	 * @param url URL, full url or path relative to server url.
	 * @return DELETE request.
	 */
	HttpRequest prepareDelete(String url);

	/**
	 * Create DELETE request.
	 *
	 * @param httpMethod Http method (i.e GET, POST, PUT, DELETE).
	 * @param url URL, full url or path relative to server url.
	 * @return DELETE request.
	 */
	HttpRequest prepareRequest(HttpMethod httpMethod, String url);

	/**
	 * Destroy client.
	 */
	void destroy();

	/**
	 * Check that HTTP client has been destroyed.
	 *
	 * @return {@code true} if client is closed, {@code false} otherwise.
	 */
	boolean isDestroyed();
}
