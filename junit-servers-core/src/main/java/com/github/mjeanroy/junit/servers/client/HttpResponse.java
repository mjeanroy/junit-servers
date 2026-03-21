/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2026 <mickael.jeanroy@gmail.com>
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

import java.util.Collection;
import java.util.List;

/// Http response, defined by:
/// - A status code: http return code (i.e 200, 400, 500 etc.).
/// - A response body: this is the body of the http response as textual representation.
/// - A set of headers.
/// - Duration: time to produce http response.
///
/// See [RFC 2616](https://tools.ietf.org/html/rfc2616#section-6")
public interface HttpResponse {

	/// Get duration of request execution in nano seconds.
	///
	/// @return Request execution duration.
	long getRequestDuration();

	/// Get duration of request execution in milli seconds (shortcut for `getRequestDuration() / 100`).
	///
	/// @return Request execution duration.
	/// @see #getRequestDuration()
	long getRequestDurationInMillis();

	/// Http status code.
	///
	/// See:
	/// - [RFC 2616 - Section 6.1](https://tools.ietf.org/html/rfc2616#section-6.1)
	/// - [RFC 2616 - Section 10](https://tools.ietf.org/html/rfc2616#section-10)
	///
	/// @return Status code.
	int status();

	/// Http response body ([RFC 2616](https://tools.ietf.org/html/rfc2616#section-7.2)).
	///
	/// @return Body.
	String body();

	/// Get the list of headers ([RFC 2616](https://tools.ietf.org/html/rfc2616#section-6.2)).
	///
	/// @return Header list.
	Collection<HttpHeader> getHeaders();

	/// Check that given header is available ([RFC 2616](https://tools.ietf.org/html/rfc2616#section-6.2)).
	///
	/// @param name Header name.
	/// @return `true` if header is in response, `false` otherwise.
	boolean containsHeader(String name);

	/// Get header from HTTP response ([RFC 2616](https://tools.ietf.org/html/rfc2616#section-6.2)).
	/// If header is missing, `null` will be returned.
	///
	/// @param name Header name.
	/// @return Header, `null` if header is not in http response.
	/// @throws NullPointerException If name is `null`.
	HttpHeader getHeader(String name);

	/// Get cookie by its name.
	/// If cookie is missing, `null` is returned.
	///
	/// See:
	/// - [MDN](https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Set-Cookie)
	/// - [RFC 6265](https://tools.ietf.org/html/rfc6265#section-4.1)
	///
	/// @param name Cookie name.
	/// @return Cookie.
	/// @see HttpHeaders#SET_COOKIE
	Cookie getCookie(String name);

	/// Get all cookies sent by server.
	///
	/// See:
	/// - [MDN](https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Set-Cookie)
	/// - [RFC 6265](https://tools.ietf.org/html/rfc6265#section-4.1)
	///
	/// @return Cookies.
	/// @see HttpHeaders#SET_COOKIE
	List<Cookie> getCookies();

	/// Get `ETag` header from http response ([RFC 7232](https://tools.ietf.org/html/rfc7232#section-2.3)).
	///
	/// @return ETag header.
	/// @see #getHeader(String)
	/// @see HttpHeaders#ETAG
	HttpHeader getETag();

	/// Get `Content-Type` header from http response ([RFC 1341](https://www.w3.org/Protocols/rfc1341/4_Content-Type.html)).
	///
	/// @return Content-Type header.
	/// @see #getHeader(String)
	/// @see HttpHeaders#CONTENT_TYPE
	HttpHeader getContentType();

	/// Get `Content-Encoding` header from http response ([RFC 7231](https://tools.ietf.org/html/rfc7231#section-3.1.2.2)).
	///
	/// @return Content-Encoding header.
	/// @see HttpHeaders#CONTENT_ENCODING
	HttpHeader getContentEncoding();

	/// Get `Location` header from http response ([RFC 7231](https://tools.ietf.org/html/rfc7231#section-7.1.2)).
	///
	/// @return Location header.
	/// @see #getHeader(String)
	/// @see HttpHeaders#LOCATION
	HttpHeader getLocation();

	/// Get `Cache-Control` header from http response ([RFC 7234](https://tools.ietf.org/html/rfc7234#section-5.2)).
	///
	/// @return Cache-Control header.
	/// @see #getHeader(String)
	/// @see HttpHeaders#CACHE_CONTROL
	HttpHeader getCacheControl();

	/// Get `Last-Modified` header from http response ([RFC 7232](https://tools.ietf.org/html/rfc7232#section-2.2)).
	///
	/// @return Last-Modified header.
	/// @see #getHeader(String)
	/// @see HttpHeaders#LAST_MODIFIED
	HttpHeader getLastModified();

	/// Get `Strict-Transport-Security` header from http response ([RFC 6797](https://tools.ietf.org/html/rfc6797)).
	///
	/// @return Strict-Transport-Security header.
	/// @see #getHeader(String)
	/// @see HttpHeaders#STRICT_TRANSPORT_SECURITY
	HttpHeader getStrictTransportSecurity();

	/// Get `Content-Security-Policy` header from http response.
	///
	/// See:
	/// - [W3C](https://www.w3.org/TR/2011/WD-CSP-20111129)
	/// - [CSP](https://content-security-policy.com/)
	///
	/// @return Content-Security-Policy header.
	/// @see #getHeader(String)
	/// @see HttpHeaders#CONTENT_SECURITY_POLICY
	HttpHeader getContentSecurityPolicy();

	/// Get `X-Content-Security-Policy` header from http response: this header was initially an experimental
	/// header implemented in Firefox.
	///
	/// See:
	/// - [W3C](https://www.w3.org/TR/2011/WD-CSP-20111129)
	/// - [CSP](https://content-security-policy.com/)
	///
	/// @return X-Content-Security-Policy header.
	/// @see #getHeader(String)
	/// @see HttpHeaders#X_CONTENT_SECURITY_POLICY
	HttpHeader getXContentSecurityPolicy();

	/// Get `X-Webkit-CSP` header from http response: this header was initially an experimental header implemented
	/// in Webkit browser (Chrome and Safari).
	///
	/// See:
	/// - [W3C](https://www.w3.org/TR/2011/WD-CSP-20111129)
	/// - [CSP](https://content-security-policy.com/)
	///
	/// @return }X-Content-Security-Policy header.
	/// @see #getHeader(String)
	/// @see HttpHeaders#X_WEBKIT_CSP
	HttpHeader getXWebkitCSP();

	/// Get `X-Content-Type-Options` header from http response ([MDN](https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/X-Content-Type-Options)).
	///
	/// @return X-Content-Type-Options header.
	/// @see #getHeader(String)
	/// @see HttpHeaders#X_CONTENT_TYPE_OPTIONS
	HttpHeader getXContentTypeOptions();

	/// Get `X-XSS-Protection` header from http response ([MDN](https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/X-XSS-Protection)).
	///
	/// @return X-XSS-Protection header.
	/// @see #getHeader(String)
	/// @see HttpHeaders#X_XSS_PROTECTION
	HttpHeader getXXSSProtection();
}
