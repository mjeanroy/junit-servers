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

package com.github.mjeanroy.junit.servers.client.impl.apache_http_client;

import com.github.mjeanroy.junit.servers.client.HttpHeader;
import com.github.mjeanroy.junit.servers.client.impl.AbstractHttpResponse;
import com.github.mjeanroy.junit.servers.client.impl.DefaultHttpResponse;
import com.github.mjeanroy.junit.servers.exceptions.HttpClientException;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.mjeanroy.junit.servers.client.HttpHeader.header;
import static com.github.mjeanroy.junit.servers.commons.CollectionUtils.concat;
import static com.github.mjeanroy.junit.servers.commons.Preconditions.notNull;
import static com.github.mjeanroy.junit.servers.commons.Preconditions.positive;
import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;

/**
 * Factory to produce {@link DefaultHttpResponse} from {@link HttpResponse}.
 *
 * @see <a href="http://hc.apache.org/httpcomponents-client-ga/index.html">http://hc.apache.org/httpcomponents-client-ga/index.html</a>
 * @see com.github.mjeanroy.junit.servers.client.HttpClientStrategy#APACHE_HTTP_CLIENT
 */
final class ApacheHttpResponseFactory {

	// Ensure non instantiation.
	private ApacheHttpResponseFactory() {
	}

	/**
	 * Create the final {@link DefaultHttpResponse} instance.
	 *
	 * @param response The Apache response.
	 * @param duration The request duration.
	 * @return The HTTP response.
	 */
	static DefaultHttpResponse of(HttpResponse response, long duration) {
		int status = response.getStatusLine().getStatusCode();
		String body = extractBody(response);
		List<HttpHeader> headers = extractHeaders(response);
		return DefaultHttpResponse.of(duration, status, body, headers);
	}

	/**
	 * Extract response body of Apache response.
	 *
	 * @param response The Apache response.
	 * @return The response body, as a string.
	 */
	private static String extractBody(HttpResponse response) {
		try {
			HttpEntity entity = response.getEntity();
			return entity == null ? "" : EntityUtils.toString(entity);
		}
		catch (IOException ex) {
			throw new HttpClientException(ex);
		}
	}

	/**
	 * Extract headers from Apache response.
	 *
	 * @param response The Apache response.
	 * @return The final list of headers.
	 */
	private static List<HttpHeader> extractHeaders(HttpResponse response) {
		final Header[] responseHeaders = response.getAllHeaders();
		if (responseHeaders == null || responseHeaders.length == 0) {
			return emptyList();
		}

		final Map<String, HttpHeader.Builder> map = new HashMap<>(responseHeaders.length);

		for (Header h : responseHeaders) {
			final String name = h.getName();
			final String value = h.getValue();
			final String key = h.getName().toLowerCase();

			HttpHeader.Builder current = map.get(key);
			if (current == null) {
				current = HttpHeader.builder(name);
				map.put(key, current);
			}

			current.addValue(value);
		}

		final List<HttpHeader> headers = new ArrayList<>(map.size());
		for (HttpHeader.Builder header : map.values()) {
			headers.add(header.build());
		}

		return unmodifiableList(headers);
	}
}
