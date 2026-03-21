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

/// Set of constants and utilities for HTTP headers.
public final class HttpHeaders {

	// Ensure non instantiation.
	private HttpHeaders() {
	}

	/// The `ETag` header name ([RFC 7232](https://tools.ietf.org/html/rfc7232#section-2.3)).
	public static final String ETAG = "ETag";

	/// The `Content-Type` header name ([RFC 1341](https://www.w3.org/Protocols/rfc1341/4_Content-Type.html)).
	public static final String CONTENT_TYPE = "Content-Type";

	/// The `Content-Disposition` header name ([RFC 6266](https://tools.ietf.org/html/rfc6266")).
	public static final String CONTENT_DISPOSITION = "Content-Disposition";

	/// The `Content-Encoding` header name ([RFC 7231](https://tools.ietf.org/html/rfc7231#section-3.1.2.2)).
	public static final String CONTENT_ENCODING = "Content-Encoding";

	/// The `Accept-Language` header name ([RFC 7231](https://tools.ietf.org/html/rfc7231#section-5.3.5)).
	public static final String ACCEPT_LANGUAGE = "Accept-Language";

	/// The `Accept-Encoding` header name ([RFC 7231](https://tools.ietf.org/html/rfc7231#section-5.3.4)).
	public static final String ACCEPT_ENCODING = "Accept-Encoding";

	/// The `Origin` header name ([RFC 6454](https://tools.ietf.org/html/rfc6454)).
	public static final String ORIGIN = "Origin";

	/// The `Referer` header name ([RFC 7231](https://tools.ietf.org/html/rfc7231#section-5.5.2)).
	public static final String REFERER = "Referer";

	/// The `Location` header name ([RFC 7231](https://tools.ietf.org/html/rfc7231#section-7.1.2)).
	public static final String LOCATION = "Location";

	/// The `Cache-Control` header name ([RFC 7234](https://tools.ietf.org/html/rfc7234#section-5.2)).
	public static final String CACHE_CONTROL = "Cache-Control";

	/// The `Last-Modified` header name ([RFC 7232](https://tools.ietf.org/html/rfc7232#section-2.2)).
	public static final String LAST_MODIFIED = "Last-Modified";

	/// The `User-Agent` header name ([RFC 2616](https://tools.ietf.org/html/rfc2616#section-14.43)).
	public static final String USER_AGENT = "User-Agent";

	/// The `Accept` header name ([RFC 7231](https://tools.ietf.org/html/rfc7231#section-5.3.2)).
	public static final String ACCEPT = "Accept";

	/// The `Cookie` header name [RFC 6265](https://tools.ietf.org/html/rfc6265#section-4.2)).
	public static final String COOKIE = "Cookie";

	/// The `Set-Cookie` header name [RFC 6265](https://tools.ietf.org/html/rfc6265#section-5.2)).
	public static final String SET_COOKIE = "Set-Cookie";

	/// The `If-None-Match` header name ([RFC 7232](https://tools.ietf.org/html/rfc7232#section-3.2)).
	public static final String IF_NONE_MATCH = "If-None-Match";

	/// The `If-Match` header name ([RFC 7232](https://tools.ietf.org/html/rfc7232#section-3.1)).
	public static final String IF_MATCH = "If-Match";

	/// The `If-Modified-Since` header name ([RFC 7232](https://tools.ietf.org/html/rfc7232#section-3.3)).
	public static final String IF_MODIFIED_SINCE = "If-Modified-Since";

	/// The `If-Unmodified-Since` header name ([RFC 7232](https://tools.ietf.org/html/rfc7232#section-3.4)).
	public static final String IF_UNMODIFIED_SINCE = "If-Unmodified-Since";

	/// The `Strict-Transport-Security` header name ([RFC 6797](https://tools.ietf.org/html/rfc6797)).
	public static final String STRICT_TRANSPORT_SECURITY = "Strict-Transport-Security";

	/// The `Content-Security-Policy` header name.
	///
	/// See:
	/// - [W3C](https://www.w3.org/TR/2011/WD-CSP-20111129)
	/// - [CSP](https://content-security-policy.com)
	public static final String CONTENT_SECURITY_POLICY = "Content-Security-Policy";

	/// The `X-Content-Security-Policy` header name: this is a non-official / non-standard header name for
	/// the `Content-Security-Policy` specification and was implemented in browsers before the standard
	/// CSP header. It was used by Firefox until version 23, and Internet Explorer version 10 (which partially
	/// implements Content Security Policy).
	///
	/// See:
	/// - [W3C](https://www.w3.org/TR/2011/WD-CSP-20111129)
	/// - [CSP](https://content-security-policy.com)
	public static final String X_CONTENT_SECURITY_POLICY = "X-Content-Security-Policy";

	/// The `X-Content-Security-Policy` header name: this is a non-official / non-standard header name for
	/// the `Content-Security-Policy` specification and was implemented in webkit browsers before the standard
	/// CSP header. It was used by Used by Chrome until version 25.
	///
	/// See:
	/// - [W3C](https://www.w3.org/TR/2011/WD-CSP-20111129)
	/// - [CSP](https://content-security-policy.com)
	public static final String X_WEBKIT_CSP = "X-Webkit-CSP";

	/// The `X-Content-Type-Options` header name. This is a non-standard header, but is implemented and used
	/// in all "modern" browsers ([MDN](https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/X-Content-Type-Options)).
	public static final String X_CONTENT_TYPE_OPTIONS = "X-Content-Type-Options";

	/// The `X-XSS-Protection` header name. This is a non-standard header, but is implemented and used
	/// in all "modern" browsers ([MDN](https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/X-XSS-Protection)).
	public static final String X_XSS_PROTECTION = "X-XSS-Protection";

	/// The `X-Requested-With` header name: this is non-standard header but is generally used by libraries and
	/// frameworks (such as jQuery) to give information about the HTTP request.
	public static final String REQUESTED_WITH = "X-Requested-With";

	/// The `X-Http-Method-Override` header name: this is non-standard header but is generally used by libraries and
	/// frameworks to override the method specified in the template. This can be used when a user agent or
	/// firewall prevents PUT or DELETE methods from being sent directly.
	public static final String X_HTTP_METHOD_OVERRIDE = "X-Http-Method-Override";

	/// The `X-Csrf-Token` header name.
	/// This header is generally used to prevent CSRF attack and should be added in `POST`, `PUT` or `DELETE`
	/// requests.
	public static final String X_CSRF_TOKEN = "X-Csrf-Token";

	/// The value for [HttpHeaders#REQUESTED_WITH] header used by Web libraries such as jQuery.
	public static final String XML_HTTP_REQUEST = "XMLHttpRequest";
}
