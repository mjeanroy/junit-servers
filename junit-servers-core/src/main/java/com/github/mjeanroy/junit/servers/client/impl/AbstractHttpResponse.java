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

package com.github.mjeanroy.junit.servers.client.impl;

import com.github.mjeanroy.junit.servers.client.HttpHeader;
import com.github.mjeanroy.junit.servers.client.HttpResponse;

import static com.github.mjeanroy.junit.servers.client.HttpHeaders.CACHE_CONTROL;
import static com.github.mjeanroy.junit.servers.client.HttpHeaders.CONTENT_ENCODING;
import static com.github.mjeanroy.junit.servers.client.HttpHeaders.CONTENT_TYPE;
import static com.github.mjeanroy.junit.servers.client.HttpHeaders.ETAG;
import static com.github.mjeanroy.junit.servers.client.HttpHeaders.LOCATION;

/**
 * Abstract skeleton of {HttpResponse} interface.
 */
public abstract class AbstractHttpResponse implements HttpResponse {

	@Override
	public long getRequestDurationInMillis() {
		return getRequestDuration() / 1000;
	}

	@Override
	public boolean containsHeader(String name) {
		return getHeader(name) != null;
	}

	@Override
	public boolean hasETagHeader() {
		return containsHeader(ETAG);
	}

	@Override
	public HttpHeader getETag() {
		return getHeader(ETAG);
	}

	@Override
	public HttpHeader getContentType() {
		return getHeader(CONTENT_TYPE);
	}

	@Override
	public HttpHeader getContentEncoding() {
		return getHeader(CONTENT_ENCODING);
	}

	@Override
	public HttpHeader getLocation() {
		return getHeader(LOCATION);
	}

	@Override
	public HttpHeader getCacheControl() {
		return getHeader(CACHE_CONTROL);
	}
}
