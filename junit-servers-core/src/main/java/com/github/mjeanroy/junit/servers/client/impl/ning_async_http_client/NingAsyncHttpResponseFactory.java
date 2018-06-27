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

package com.github.mjeanroy.junit.servers.client.impl.ning_async_http_client;

import com.github.mjeanroy.junit.servers.client.HttpHeader;
import com.github.mjeanroy.junit.servers.client.HttpResponse;
import com.github.mjeanroy.junit.servers.client.impl.AbstractHttpResponse;
import com.github.mjeanroy.junit.servers.client.impl.DefaultHttpResponse;
import com.github.mjeanroy.junit.servers.exceptions.HttpClientException;
import com.ning.http.client.FluentCaseInsensitiveStringsMap;
import com.ning.http.client.Response;
import org.apache.http.client.methods.HttpHead;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.github.mjeanroy.junit.servers.client.HttpHeader.header;
import static com.github.mjeanroy.junit.servers.commons.CollectionUtils.isEmpty;
import static com.github.mjeanroy.junit.servers.commons.Preconditions.notNull;
import static com.github.mjeanroy.junit.servers.commons.Preconditions.positive;
import static java.util.Collections.unmodifiableList;

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
	static DefaultHttpResponse of(Response response, long duration) {
		int status = response.getStatusCode();
		String body = extractBody(response);
		List<HttpHeader> headers = extractHeaders(response);
		return DefaultHttpResponse.of(duration, status, body, headers);
	}

	/**
	 * Extract headers from Ning HTTP response.
	 *
	 * @param response The Ning HTTP response.
	 * @return The final list of headers.
	 */
	private static List<HttpHeader> extractHeaders(Response response) {
		final FluentCaseInsensitiveStringsMap responseHeaders = response.getHeaders();
		final List<HttpHeader> headers = new ArrayList<>(responseHeaders.size());

		for (Map.Entry<String, List<String>> entry : responseHeaders.entrySet()) {
			headers.add(header(entry.getKey(), entry.getValue()));
		}

		return unmodifiableList(headers);
	}

	/**
	 * Extract response body of Ning HTTP response.
	 *
	 * @param response The Ning HTTP response.
	 * @return The response body, as a string.
	 */
	private static String extractBody(Response response) {
		try {
			return response.getResponseBody();
		}
		catch (IOException ex) {
			throw new HttpClientException(ex);
		}
	}
}
