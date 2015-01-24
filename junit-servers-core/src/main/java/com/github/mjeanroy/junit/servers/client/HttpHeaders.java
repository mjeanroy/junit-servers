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

package com.github.mjeanroy.junit.servers.client;

/**
 * Set of constants for http headers.
 */
public final class HttpHeaders {

	private HttpHeaders() {
	}

	public static final String ETAG = "ETag";
	public static final String CONTENT_TYPE = "Content-Type";
	public static final String CONTENT_ENCODING = "Content-Encoding";
	public static final String LOCATION = "Location";
	public static final String CACHE_CONTROL = "Cache-Control";
	public static final String LAST_MODIFIED = "Last-Modified";
	public static final String STRICT_TRANSPORT_SECURITY = "Strict-Transport-Security";
	public static final String CONTENT_SECURITY_POLICY = "Content-Security-Policy";
	public static final String X_CONTENT_SECURITY_POLICY = "X-Content-Security-Policy";
	public static final String X_WEBKIT_CSP = "X-Webkit-CSP";
	public static final String X_CONTENT_TYPE_OPTIONS = "X-Content-Type-Options";
	public static final String USER_AGENT = "User-Agent";
	public static final String REQUESTED_WITH = "X-Requested-With";
	public static final String ACCEPT = "Accept";

	public static final String XML_HTTP_REQUEST = "XMLHttpRequest";
	public static final String APPLICATION_JSON = "application/json";
	public static final String APPLICATION_XML = "application/xml";
	public static final String APPLICATION_FORM_URL_ENCODED = "application/x-www-form-urlencoded";
	public static final String MULTIPART_FORM_DATA = "multipart/form-data";
}
