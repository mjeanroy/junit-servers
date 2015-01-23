/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 <mickael.jeanroy@gmail.com>
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

package com.github.mjeanroy.junit.servers.client.apache_http_client;

import static com.github.mjeanroy.junit.servers.client.HttpHeader.header;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import com.github.mjeanroy.junit.servers.client.AbstractHttpResponse;
import com.github.mjeanroy.junit.servers.client.HttpHeader;
import com.github.mjeanroy.junit.servers.exceptions.HttpClientException;

/**
 * Implementation of {HttpResponse} using async-http-client
 * under the hood.
 * See: https://asynchttpclient.github.io/
 */
public class ApacheHttpResponse extends AbstractHttpResponse {

	/**
	 * Original response from apache http-client library.
	 */
	private final HttpResponse response;

	/**
	 * Create apache http response.
	 *
	 * @param response Original http response.
	 */
	ApacheHttpResponse(HttpResponse response) {
		this.response = response;
	}

	@Override
	public int status() {
		return response.getStatusLine().getStatusCode();
	}

	@Override
	public String body() {
		try {
			HttpEntity entity = response.getEntity();
			return EntityUtils.toString(entity);
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
