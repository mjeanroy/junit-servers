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

package com.github.mjeanroy.junit.servers.testing;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * Static HTTP Utilities, used only for testing.
 */
public final class HttpTestUtils {

	private HttpTestUtils() {
	}

	/**
	 * Create URL string.
	 *
	 * @param scheme URL Scheme (HTTP, HTTPS).
	 * @param host URL Host.
	 * @param port URL port.
	 * @param path URL path.
	 * @return Full URL.
	 */
	public static String url(String scheme, String host, int port, String path) {
		return new StringBuilder()
			.append(scheme)
			.append("://")
			.append(host)
			.append(":")
			.append(port)
			.append(path)
			.toString();
	}

	/**
	 * Create URL string with HTTP scheme and "localhost" host.
	 *
	 * @param port URL port.
	 * @param path URL path.
	 * @return Full URL.
	 */
	public static String localhost(int port, String path) {
		return url("http", "localhost", port, path);
	}

	/**
	 * Create URL string with HTTP scheme and "localhost" host.
	 *
	 * @param port URL port.
	 * @return Full URL.
	 */
	public static String localhost(int port) {
		return "http://localhost:" + port + "/";
	}

	/**
	 * Run GET HTTP query and returns response.
	 *
	 * @param url HTTP URL.
	 * @return HTTP Response.
	 */
	public static HttpResponse get(String url) {
		OkHttpClient client = new OkHttpClient();
		Request rq = new Request.Builder().url(url).build();
		try (Response rsp = client.newCall(rq).execute()) {
			int statusCode = rsp.code();
			String responseBody = rsp.body() == null ? null : rsp.body().string();
			return new HttpResponse(statusCode, responseBody);
		}
		catch (IOException ex) {
			throw new AssertionError(ex);
		}
	}

	/**
	 * Basic HTTP response, used only for testing.
	 */
	public static final class HttpResponse {
		/**
		 * Response code (a.k.a HTTP Status Code).
		 */
		private final int statusCode;

		/**
		 * Response body, may be {@code null}.
		 */
		private final String responseBody;

		private HttpResponse(int statusCode, String responseBody) {
			this.statusCode = statusCode;
			this.responseBody = responseBody;
		}

		/**
		 * Get {@link #statusCode}
		 *
		 * @return {@link #statusCode}
		 */
		public int getStatusCode() {
			return statusCode;
		}

		/**
		 * Get {@link #responseBody}
		 *
		 * @return {@link #responseBody}
		 */
		public String getResponseBody() {
			return responseBody;
		}
	}
}
