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

package com.github.mjeanroy.junit.servers.client;

/// Http client that can be used to query embedded server.
///
/// After test suite, client should be properly destroyed
/// using [HttpClient#destroy] method.
public interface HttpClient extends AutoCloseable {

	/// Get the client configuration.
	///
	/// @return HTTP Client configuration.
	HttpClientConfiguration getConfiguration();

	/// Create `GET` request.
	///
	/// @param endpoint URL, full url or path relative to server url.
	/// @return GET request.
	/// @see #prepareRequest(HttpMethod, String)
	HttpRequest prepareGet(String endpoint);

	/// Create `POST` request.
	///
	/// @param endpoint URL, full url or path relative to server url.
	/// @return POST request.
	/// @see #prepareRequest(HttpMethod, String)
	HttpRequest preparePost(String endpoint);

	/// Create `POST` request.
	///
	/// @param endpoint URL, full url or path relative to server url.
	/// @param body Request body.
	/// @return POST request.
	/// @see #prepareRequest(HttpMethod, String)
	HttpRequest preparePost(String endpoint, HttpRequestBody body);

	/// Create `PUT` request.
	///
	/// @param url URL, full url or path relative to server url.
	/// @return PUT request.
	/// @see #prepareRequest(HttpMethod, String)
	HttpRequest preparePut(String url);

	/// Create `PUT` request.
	///
	/// @param url URL, full url or path relative to server url.
	/// @param body Request body.
	/// @return PUT request.
	/// @see #prepareRequest(HttpMethod, String)
	HttpRequest preparePut(String url, HttpRequestBody body);

	/// Create `DELETE` request.
	///
	/// @param endpoint URL, full url or path relative to server url.
	/// @return DELETE request.
	/// @see #prepareRequest(HttpMethod, String)
	HttpRequest prepareDelete(String endpoint);

	/// Create `DELETE` request.
	///
	/// @param endpoint URL, full url or path relative to server url.
	/// @param body Request body.
	/// @return DELETE request.
	/// @see #prepareRequest(HttpMethod, String)
	HttpRequest prepareDelete(String endpoint, HttpRequestBody body);

	/// Create `PATCH` request.
	///
	/// @param endpoint URL, full url or path relative to server url.
	/// @return PATCH request.
	/// @see #prepareRequest(HttpMethod, String)
	HttpRequest preparePatch(String endpoint);

	/// Create `PATCH` request.
	///
	/// @param endpoint URL, full url or path relative to server url.
	/// @param body Request body.
	/// @return PATCH request.
	/// @see #prepareRequest(HttpMethod, String)
	HttpRequest preparePatch(String endpoint, HttpRequestBody body);

	/// Create `HEAD` request.
	///
	/// @param endpoint URL, full url or path relative to server url.
	/// @return HEAD request.
	/// @see #prepareRequest(HttpMethod, String)
	HttpRequest prepareHead(String endpoint);

	/// Create request.
	///
	/// Once destroyed, this client should not be able to create HTTP request and should
	/// throw an instance of [IllegalStateException].
	///
	/// @param httpMethod Http method (i.e `GET`, `POST`, `PUT`, `DELETE`).
	/// @param endpoint URL, full url or path relative to server url.
	/// @return The request.
	/// @throws IllegalStateException If client has already been destroyed.
	/// @see #destroy()
	/// @see #isDestroyed()
	HttpRequest prepareRequest(HttpMethod httpMethod, String endpoint);

	/// Destroy client.
	void destroy();

	/// Check that HTTP client has been destroyed.
	///
	/// @return `true` if client is closed, `false` otherwise.
	boolean isDestroyed();

	@Override
	default void close() {
		destroy();
	}
}
