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

package com.github.mjeanroy.junit.servers.client.impl.ning;

import com.github.mjeanroy.junit.servers.client.HttpResponse;
import com.github.mjeanroy.junit.servers.client.impl.DefaultHttpResponse;
import com.ning.http.client.Response;

/**
 * Implementation of {@link HttpResponse} using (ning) async-http-client
 * under the hood.
 *
 * @see <a href="https://github.com/ning/async-http-client">https://github.com/ning/async-http-client</a>
 * @see com.github.mjeanroy.junit.servers.client.HttpClientStrategy#NING_ASYNC_HTTP_CLIENT
 */
final class NingAsyncHttpResponseFactory {

	// Ensure non instantiation.
	private NingAsyncHttpResponseFactory() {
	}

	/**
	 * Create the final {@link DefaultHttpResponse} instance.
	 *
	 * @param response The Ning HTTP response.
	 * @param duration The request duration.
	 * @return The HTTP response.
	 */
	static HttpResponse of(Response response, long duration) {
		return new NingAsyncHttpResponse(response, duration);
	}
}
