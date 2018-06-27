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

package com.github.mjeanroy.junit.servers.client.impl.okhttp3;

import com.github.mjeanroy.junit.servers.client.HttpHeader;
import com.github.mjeanroy.junit.servers.client.HttpResponse;
import com.github.mjeanroy.junit.servers.client.impl.AbstractHttpResponse;
import com.github.mjeanroy.junit.servers.client.impl.DefaultHttpResponse;
import com.github.mjeanroy.junit.servers.exceptions.HttpClientException;
import okhttp3.Headers;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.github.mjeanroy.junit.servers.client.HttpHeader.header;
import static java.util.Collections.unmodifiableList;

/**
 * Factory that produce {@link DefaultHttpResponse} from {@link HttpResponse}.
 *
 * @see com.github.mjeanroy.junit.servers.client.HttpClientStrategy#OK_HTTP3
 * @see <a href="http://square.github.io/okhttp">http://square.github.io/okhttp</a>
 */
final class OkHttpResponseFactory {

	// Ensure non instantiation.
	private OkHttpResponseFactory() {
	}

	/**
	 * Create the final {@link DefaultHttpResponse} instance.
	 *
	 * @param response The OkHttp response.
	 * @param duration The request duration.
	 * @return The HTTP response.
	 */
	static DefaultHttpResponse of(Response response, long duration) {
		int status = response.code();
		String body = extractBody(response);
		List<HttpHeader> headers = extractHeaders(response);
		return DefaultHttpResponse.of(duration, status, body, headers);
	}

	/**
	 * Extract headers from OkHttp response.
	 *
	 * @param response The OkHttp response.
	 * @return The final list of headers.
	 */
	private static List<HttpHeader> extractHeaders(Response response) {
		final Headers responseHeaders = response.headers();
		final List<HttpHeader> headers = new ArrayList<>(responseHeaders.size());

		for (String name : responseHeaders.names()) {
			headers.add(header(name, responseHeaders.values(name)));
		}

		return unmodifiableList(headers);
	}

	/**
	 * Extract response body of OkHttp response.
	 *
	 * @param response The OkHttp response.
	 * @return The response body, as a string.
	 */
	private static String extractBody(Response response) {
		try {
			ResponseBody body = response.body();
			return body == null ? "" : body.string();
		} catch (IOException ex) {
			throw new HttpClientException(ex);
		}
	}
}
