/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2018 <mickael.jeanroy@gmail.com>
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

package com.github.mjeanroy.junit.servers.client.it;

final class HeaderTestUtils {

	private HeaderTestUtils() {
	}

	static final String APPLICATION_JSON = "application/json";
	static final String APPLICATION_XML = "application/xml";
	static final String MULTIPART_FORM_DATA = "multipart/form-data";
	static final String X_CSRF_TOKEN = "X-Csrf-Token";
	static final String X_HTTP_METHOD_OVERRIDE = "X-Http-Method-Override";
	static final String X_REQUESTED_WITH = "X-Requested-With";
	static final String ACCEPT = "Accept";
	static final String ACCEPT_LANGUAGE = "Accept-Language";
	static final String ACCEPT_ENCODING = "Accept-Encoding";
	static final String IF_MATCH = "If-Match";
	static final String IF_NONE_MATCH = "If-None-Match";
	static final String IF_MODIFIED_SINCE = "If-Modified-Since";
	static final String IF_UNMODIFIED_SINCE = "If-Unmodified-Since";
	static final String REFERER = "Referer";
	static final String ORIGIN = "Origin";
	static final String USER_AGENT = "User-Agent";
	static final String CONTENT_TYPE = "Content-Type";
	static final String CONTENT_ENCODING = "Content-Encoding";
	static final String LOCATION = "Location";
	static final String ETAG = "ETag";
	static final String CACHE_CONTROL = "Cache-Control";
	static final String LAST_MODIFIED = "Last-Modified";
	static final String STRICT_TRANSPORT_SECURITY = "Strict-Transport-Security";
	static final String X_CONTENT_SECURITY_POLICY = "X-Content-Security-Policy";
	static final String X_WEBKIT_CSP = "X-Webkit-CSP";
	static final String CONTENT_SECURITY_POLICY = "Content-Security-Policy";
	static final String X_XSS_PROTECTION = "X-Xss-Protection";
	static final String X_CONTENT_TYPE_OPTIONS = "X-Content-Type-Options";
	static final String SET_COOKIE = "Set-Cookie";
}
