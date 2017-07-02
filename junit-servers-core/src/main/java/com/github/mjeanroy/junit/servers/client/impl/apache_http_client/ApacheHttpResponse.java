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
import com.github.mjeanroy.junit.servers.exceptions.HttpClientException;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.github.mjeanroy.junit.servers.client.HttpHeader.header;
import static com.github.mjeanroy.junit.servers.commons.Preconditions.notNull;
import static com.github.mjeanroy.junit.servers.commons.Preconditions.positive;

/**
 * Implementation of {@link HttpResponse} using apache http-client
 * under the hood.
 *
 * @see <a href="http://hc.apache.org/httpcomponents-client-ga/index.html">http://hc.apache.org/httpcomponents-client-ga/index.html</a>
 * @see com.github.mjeanroy.junit.servers.client.HttpClientStrategy#APACHE_HTTP_CLIENT
 */
class ApacheHttpResponse extends AbstractHttpResponse {

	/**
	 * Original response from apache http-client library.
	 */
	private final HttpResponse response;

	/**
	 * Request duration in nano seconds: this is the time to execute http request and
	 * produce http response. This duration will always be a positive number.
	 */
	private final long duration;

	/**
	 * Create apache http response.
	 *
	 * @param response Original http response.
	 * @param duration Request duration.
	 * @throws NullPointerException If {@code response} is null.
	 * @throws IllegalArgumentException If {@code duration} is not positive.
	 */
	ApacheHttpResponse(HttpResponse response, long duration) {
		this.response = notNull(response, "response");
		this.duration = positive(duration, "duration");
	}

	@Override
	public long getRequestDuration() {
		return duration;
	}

	@Override
	public int status() {
		return response.getStatusLine().getStatusCode();
	}

	@Override
	public String body() {
		try {
			HttpEntity entity = response.getEntity();
			return entity == null ? "" : EntityUtils.toString(entity);
		}
		catch (IOException ex) {
			throw new HttpClientException(ex);
		}
	}

	@Override
	public HttpHeader getHeader(String name) {
		Header[] headers = response.getHeaders(name);
		if (headers == null || headers.length == 0) {
			return null;
		}

		List<String> values = new ArrayList<>(headers.length);
		for (Header h : headers) {
			values.add(h.getValue());
		}

		return header(name, values);
	}
}
