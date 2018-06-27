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

package com.github.mjeanroy.junit.servers.client.impl.async_http_client;

import com.github.mjeanroy.junit.servers.client.HttpHeader;
import com.github.mjeanroy.junit.servers.client.HttpResponse;
import com.github.mjeanroy.junit.servers.client.impl.AbstractHttpResponse;
import com.github.mjeanroy.junit.servers.client.impl.DefaultHttpResponse;
import com.github.mjeanroy.junit.servers.commons.CollectionUtils;
import com.github.mjeanroy.junit.servers.commons.Mapper;
import io.netty.handler.codec.http.HttpHeaders;
import org.asynchttpclient.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.mjeanroy.junit.servers.client.HttpHeader.header;
import static com.github.mjeanroy.junit.servers.commons.CollectionUtils.isEmpty;
import static com.github.mjeanroy.junit.servers.commons.Preconditions.notNull;
import static com.github.mjeanroy.junit.servers.commons.Preconditions.positive;
import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;

/**
 * Factory that produce {@link DefaultHttpResponse} from {@link HttpResponse}.
 *
 * @see <a href="https://asynchttpclient.github.io/">https://asynchttpclient.github.io/</a>
 * @see com.github.mjeanroy.junit.servers.client.HttpClientStrategy#ASYNC_HTTP_CLIENT
 */
final class AsyncHttpResponseFactory {

	// Ensure non instantiation.
	private AsyncHttpResponseFactory() {
	}

	/**
	 * Create the final {@link DefaultHttpResponse} instance.
	 *
	 * @param response The AsyncHttpClient response.
	 * @param duration The request duration.
	 * @return The HTTP response.
	 */
	static DefaultHttpResponse of(Response response, long duration) {
		final int status = response.getStatusCode();
		final String body = response.getResponseBody();
		final List<HttpHeader> headers = extractHeaders(response);
		return DefaultHttpResponse.of(duration, status, body, headers);
	}

	/**
	 * Extract headers from AsyncHttpClient response.
	 *
	 * @param response The AsyncHttpClient response.
	 * @return The final list of headers.
	 */
	private static List<HttpHeader> extractHeaders(Response response) {
		final HttpHeaders responseHeaders = response.getHeaders();
		if (responseHeaders.isEmpty()) {
			return emptyList();
		}

		final Map<String, HttpHeader.Builder> map = new HashMap<>();

		for (Map.Entry<String, String> entry : responseHeaders) {
			final String name = entry.getKey();
			final String value = entry.getValue();
			final String key = name.toLowerCase();

			HttpHeader.Builder builder = map.get(key);
			if (builder == null) {
				builder = HttpHeader.builder(name);
				map.put(key, builder);
			}

			builder.addValue(value);
		}

		final List<HttpHeader> headers = new ArrayList<>(map.size());
		for (HttpHeader.Builder builder : map.values()) {
			headers.add(builder.build());
		}

		return unmodifiableList(headers);
	}
}
