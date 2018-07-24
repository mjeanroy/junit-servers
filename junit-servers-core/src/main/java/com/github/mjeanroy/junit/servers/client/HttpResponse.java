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

package com.github.mjeanroy.junit.servers.client;

import java.util.Collection;
import java.util.List;

/**
 * Http response, defined by:
 *
 * <ul>
 *   <li>A status code: http return code (i.e 200, 400, 500 etc.).</li>
 *   <li>A response body: this is the body of the http response as textual representation.</li>
 *   <li>A set of headers.</li>
 *   <li>Duration: time to produce http response.</li>
 * </ul>
 *
 * @see <a href="https://tools.ietf.org/html/rfc2616#section-6">https://tools.ietf.org/html/rfc2616#section-6</a>
 */
public interface HttpResponse {

	/**
	 * Get duration of request execution in nano seconds.
	 *
	 * @return Request execution duration.
	 */
	long getRequestDuration();

	/**
	 * Get duration of request execution in milli seconds (shortcut for {@code getRequestDuration() / 100}).
	 *
	 * @return Request execution duration.
	 * @see #getRequestDuration()
	 */
	long getRequestDurationInMillis();

	/**
	 * Http status code.
	 *
	 * @return Status code.
	 * @see <a href="https://tools.ietf.org/html/rfc2616#section-6.1">https://tools.ietf.org/html/rfc2616#section-6.1</a>
	 * @see <a href="https://tools.ietf.org/html/rfc2616#section-10">https://tools.ietf.org/html/rfc2616#section-10</a>
	 */
	int status();

	/**
	 * Http response body.
	 *
	 * @return Body.
	 * @see <a href="https://tools.ietf.org/html/rfc2616#section-7.2">https://tools.ietf.org/html/rfc2616#section-7.2</a>
	 */
	String body();

	/**
	 * Get the list of headers.
	 *
	 * @return Header list.
	 * @see <a href="https://tools.ietf.org/html/rfc2616#section-6.2">https://tools.ietf.org/html/rfc2616#section-6.2</a>
	 */
	Collection<HttpHeader> getHeaders();

	/**
	 * Check that given header is available.
	 *
	 * @param name Header name.
	 * @return {@code true} if header is in response, {@code false} otherwise.
	 * @see <a href="https://tools.ietf.org/html/rfc2616#section-6.2">https://tools.ietf.org/html/rfc2616#section-6.2</a>
	 */
	boolean containsHeader(String name);

	/**
	 * Get header from HTTP response (if header is missing, {@code null} will be returned).
	 *
	 * @param name Header name.
	 * @return Header, {@code null} if header is not in http response.
	 * @throws NullPointerException If name is {@code null}.
	 * @see <a href="https://tools.ietf.org/html/rfc2616#section-6.2">https://tools.ietf.org/html/rfc2616#section-6.2</a>
	 */
	HttpHeader getHeader(String name);

	/**
	 * Get cookie by its name (if cookie is missing, {@code null} is returned).
	 *
	 * @param name Cookie name.
	 * @return Cookie.
	 * @see HttpHeaders#SET_COOKIE
	 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Set-Cookie">https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Set-Cookie</a>
	 * @see <a href="https://tools.ietf.org/html/rfc6265#section-4.1">https://tools.ietf.org/html/rfc6265#section-4.1</a>
	 */
	Cookie getCookie(String name);

	/**
	 * Get all cookies sent by server.
	 *
	 * @return Cookies.
	 * @see HttpHeaders#SET_COOKIE
	 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Set-Cookie">https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Set-Cookie</a>
	 * @see <a href="https://tools.ietf.org/html/rfc6265#section-4.1">https://tools.ietf.org/html/rfc6265#section-4.1</a>
	 */
	List<Cookie> getCookies();

	/**
	 * Get {@code ETag} header from http response.
	 *
	 * @return ETag header.
	 * @see #getHeader(String)
	 * @see HttpHeaders#ETAG
	 * @see <a href="https://tools.ietf.org/html/rfc7232#section-2.3">https://tools.ietf.org/html/rfc7232#section-2.3</a>
	 */
	HttpHeader getETag();

	/**
	 * Get {@code Content-Type} header from http response.
	 *
	 * @return Content-Type header.
	 * @see #getHeader(String)
	 * @see HttpHeaders#CONTENT_TYPE
	 * @see <a href="https://www.w3.org/Protocols/rfc1341/4_Content-Type.html">https://www.w3.org/Protocols/rfc1341/4_Content-Type.html</a>
	 */
	HttpHeader getContentType();

	/**
	 * Get {@code Content-Encoding} header from http response.
	 *
	 * @return Content-Encoding header.
	 * @see HttpHeaders#CONTENT_ENCODING
	 * @see <a href="https://tools.ietf.org/html/rfc7231#section-3.1.2.2">https://tools.ietf.org/html/rfc7231#section-3.1.2.2</a>
	 */
	HttpHeader getContentEncoding();

	/**
	 * Get {@code Location} header from http response.
	 *
	 * @return Location header.
	 * @see #getHeader(String)
	 * @see HttpHeaders#LOCATION
	 * @see <a href="https://tools.ietf.org/html/rfc7231#section-7.1.2">https://tools.ietf.org/html/rfc7231#section-7.1.2</a>
	 */
	HttpHeader getLocation();

	/**
	 * Get {@code Cache-Control} header from http response.
	 *
	 * @return Cache-Control header.
	 * @see #getHeader(String)
	 * @see HttpHeaders#CACHE_CONTROL
	 * @see <a href="https://tools.ietf.org/html/rfc7234#section-5.2">https://tools.ietf.org/html/rfc7234#section-5.2</a>
	 */
	HttpHeader getCacheControl();

	/**
	 * Get {@code Last-Modified} header from http response.
	 *
	 * @return Last-Modified header.
	 * @see #getHeader(String)
	 * @see HttpHeaders#LAST_MODIFIED
	 * @see <a href="https://tools.ietf.org/html/rfc7232#section-2.2">https://tools.ietf.org/html/rfc7232#section-2.2</a>
	 */
	HttpHeader getLastModified();

	/**
	 * Get {@code Strict-Transport-Security} header from http response.
	 *
	 * @return Strict-Transport-Security header.
	 * @see #getHeader(String)
	 * @see HttpHeaders#STRICT_TRANSPORT_SECURITY
	 * @see <a href="https://tools.ietf.org/html/rfc6797">https://tools.ietf.org/html/rfc6797</a>
	 */
	HttpHeader getStrictTransportSecurity();

	/**
	 * Get {@code Content-Security-Policy} header from http response.
	 *
	 * @return Content-Security-Policy header.
	 * @see #getHeader(String)
	 * @see HttpHeaders#CONTENT_SECURITY_POLICY
	 * @see <a href="https://www.w3.org/TR/2011/WD-CSP-20111129/">https://www.w3.org/TR/2011/WD-CSP-20111129/</a>
	 * @see <a href="https://content-security-policy.com/">https://content-security-policy.com/</a>
	 */
	HttpHeader getContentSecurityPolicy();

	/**
	 * Get {@code X-Content-Security-Policy} header from http response: this header was initially an experimental
	 * header implemented in Firefox.
	 *
	 * @return X-Content-Security-Policy header.
	 * @see #getHeader(String)
	 * @see HttpHeaders#X_CONTENT_SECURITY_POLICY
	 * @see <a href="https://www.w3.org/TR/2011/WD-CSP-20111129/">https://www.w3.org/TR/2011/WD-CSP-20111129/</a>
	 * @see <a href="https://content-security-policy.com/">https://content-security-policy.com/</a>
	 */
	HttpHeader getXContentSecurityPolicy();

	/**
	 * Get {@code X-Webkit-CSP} header from http response: this header was initially an experimental header implemented
	 * in Webkit browser (Chrome and Safari).
	 *
	 * @return }X-Content-Security-Policy header.
	 * @see #getHeader(String)
	 * @see HttpHeaders#X_WEBKIT_CSP
	 * @see <a href="https://www.w3.org/TR/2011/WD-CSP-20111129/">https://www.w3.org/TR/2011/WD-CSP-20111129/</a>
	 * @see <a href="https://content-security-policy.com/">https://content-security-policy.com/</a>
	 */
	HttpHeader getXWebkitCSP();

	/**
	 * Get {@code X-Content-Type-Options} header from http response.
	 *
	 * @return X-Content-Type-Options header.
	 * @see #getHeader(String)
	 * @see HttpHeaders#X_CONTENT_TYPE_OPTIONS
	 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/X-Content-Type-Options">https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/X-Content-Type-Options</a>
	 */
	HttpHeader getXContentTypeOptions();

	/**
	 * Get {@code X-XSS-Protection} header from http response.
	 *
	 * @return X-XSS-Protection header.
	 * @see #getHeader(String)
	 * @see HttpHeaders#X_XSS_PROTECTION
	 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/X-XSS-Protection">https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/X-XSS-Protection</a>
	 */
	HttpHeader getXXSSProtection();
}
