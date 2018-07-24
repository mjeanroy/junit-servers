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

/**
 * Set of constants and utilities for HTTP headers.
 */
public final class HttpHeaders {

	// Ensure non instantiation.
	private HttpHeaders() {
	}

	/**
	 * The {@code ETag} header name.
	 * @see <a href="https://tools.ietf.org/html/rfc7232#section-2.3">https://tools.ietf.org/html/rfc7232#section-2.3</a>
	 */
	public static final String ETAG = "ETag";

	/**
	 * The {@code Content-Type} header name.
	 * @see <a href="https://www.w3.org/Protocols/rfc1341/4_Content-Type.html">https://www.w3.org/Protocols/rfc1341/4_Content-Type.html</a>
	 */
	public static final String CONTENT_TYPE = "Content-Type";

	/**
	 * The {@code Content-Encoding} header name.
	 * @see <a href="https://tools.ietf.org/html/rfc7231#section-3.1.2.2">https://tools.ietf.org/html/rfc7231#section-3.1.2.2</a>
	 */
	public static final String CONTENT_ENCODING = "Content-Encoding";

	/**
	 * The {@code Accept-Language} header name.
	 * @see <a href="https://tools.ietf.org/html/rfc7231#section-5.3.5">https://tools.ietf.org/html/rfc7231#section-5.3.5</a>
	 */
	public static final String ACCEPT_LANGUAGE = "Accept-Language";

	/**
	 * The {@code Accept-Encoding} header name.
	 * @see <a href="https://tools.ietf.org/html/rfc7231#section-5.3.4">https://tools.ietf.org/html/rfc7231#section-5.3.4</a>
	 */
	public static final String ACCEPT_ENCODING = "Accept-Encoding";

	/**
	 * The {@code Origin} header name.
	 * @see <a href="https://tools.ietf.org/html/rfc6454">https://tools.ietf.org/html/rfc6454</a>
	 */
	public static final String ORIGIN = "Origin";

	/**
	 * The {@code Referer} header name.
	 * @see <a href="https://tools.ietf.org/html/rfc7231#section-5.5.2">https://tools.ietf.org/html/rfc7231#section-5.5.2</a>
	 */
	public static final String REFERER = "Referer";

	/**
	 * The {@code Location} header name.
	 * @see <a href="https://tools.ietf.org/html/rfc7231#section-7.1.2">https://tools.ietf.org/html/rfc7231#section-7.1.2</a>
	 */
	public static final String LOCATION = "Location";

	/**
	 * The {@code Cache-Control} header name.
	 * @see <a href="https://tools.ietf.org/html/rfc7234#section-5.2">https://tools.ietf.org/html/rfc7234#section-5.2</a>
	 */
	public static final String CACHE_CONTROL = "Cache-Control";

	/**
	 * The {@code Last-Modified} header name.
	 * @see <a href="https://tools.ietf.org/html/rfc7232#section-2.2">https://tools.ietf.org/html/rfc7232#section-2.2</a>
	 */
	public static final String LAST_MODIFIED = "Last-Modified";

	/**
	 * The {@code User-Agent} header name.
	 * @see <a href="https://tools.ietf.org/html/rfc2616#section-14.43">https://tools.ietf.org/html/rfc2616#section-14.43</a>
	 */
	public static final String USER_AGENT = "User-Agent";

	/**
	 * The {@code Accept} header name.
	 * @see <a href="https://tools.ietf.org/html/rfc7231#section-5.3.2">https://tools.ietf.org/html/rfc7231#section-5.3.2</a>
	 */
	public static final String ACCEPT = "Accept";

	/**
	 * The {@code Cookie} header name.
	 * @see <a href="https://tools.ietf.org/html/rfc6265#section-4.2">https://tools.ietf.org/html/rfc6265#section-4.2</a>
	 */
	public static final String COOKIE = "Cookie";

	/**
	 * The {@code Set-Cookie} header name.
	 * @see <a href="https://tools.ietf.org/html/rfc6265#section-5.2">https://tools.ietf.org/html/rfc6265#section-5.2</a>
	 */
	public static final String SET_COOKIE = "Set-Cookie";

	/**
	 * The {@code If-None-Match} header name.
	 * @see <a href="https://tools.ietf.org/html/rfc7232#section-3.2">https://tools.ietf.org/html/rfc7232#section-3.2</a>
	 */
	public static final String IF_NONE_MATCH = "If-None-Match";

	/**
	 * The {@code If-Match} header name.
	 * @see <a href="https://tools.ietf.org/html/rfc7232#section-3.1">https://tools.ietf.org/html/rfc7232#section-3.1</a>
	 */
	public static final String IF_MATCH = "If-Match";

	/**
	 * The {@code If-Modified-Since} header name.
	 * @see <a href="https://tools.ietf.org/html/rfc7232#section-3.3">https://tools.ietf.org/html/rfc7232#section-3.3</a>
	 */
	public static final String IF_MODIFIED_SINCE = "If-Modified-Since";

	/**
	 * The {@code If-Unmodified-Since} header name.
	 * @see <a href="https://tools.ietf.org/html/rfc7232#section-3.4">https://tools.ietf.org/html/rfc7232#section-3.4</a>
	 */
	public static final String IF_UNMODIFIED_SINCE = "If-Unmodified-Since";

	/**
	 * The {@code Strict-Transport-Security} header name.
	 * @see <a href="https://tools.ietf.org/html/rfc6797">https://tools.ietf.org/html/rfc6797</a>
	 */
	public static final String STRICT_TRANSPORT_SECURITY = "Strict-Transport-Security";

	/**
	 * The {@code Content-Security-Policy} header name.
	 * @see <a href="https://www.w3.org/TR/2011/WD-CSP-20111129/">https://www.w3.org/TR/2011/WD-CSP-20111129/</a>
	 * @see <a href="https://content-security-policy.com/">https://content-security-policy.com/</a>
	 */
	public static final String CONTENT_SECURITY_POLICY = "Content-Security-Policy";

	/**
	 * The {@code X-Content-Security-Policy} header name: this is a non-official / non-standard header name for
	 * the {@code Content-Security-Policy} specification and was implemented in browsers before the standard
	 * CSP header. It was used by Firefox until version 23, and Internet Explorer version 10 (which partially
	 * implements Content Security Policy).
	 *
	 * @see <a href="https://www.w3.org/TR/2011/WD-CSP-20111129/">https://www.w3.org/TR/2011/WD-CSP-20111129/</a>
	 * @see <a href="https://content-security-policy.com/">https://content-security-policy.com/</a>
	 */
	public static final String X_CONTENT_SECURITY_POLICY = "X-Content-Security-Policy";

	/**
	 * The {@code X-Content-Security-Policy} header name: this is a non-official / non-standard header name for
	 * the {@code Content-Security-Policy} specification and was implemented in webkit browsers before the standard
	 * CSP header. It was used by Used by Chrome until version 25.
	 *
	 * @see <a href="https://www.w3.org/TR/2011/WD-CSP-20111129/">https://www.w3.org/TR/2011/WD-CSP-20111129/</a>
	 * @see <a href="https://content-security-policy.com/">https://content-security-policy.com/</a>
	 */
	public static final String X_WEBKIT_CSP = "X-Webkit-CSP";

	/**
	 * The {@code X-Content-Type-Options} header name. This is a non-standard header, but is implemented and used
	 * in all "modern" browsers.
	 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/X-Content-Type-Options">https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/X-Content-Type-Options</a>
	 */
	public static final String X_CONTENT_TYPE_OPTIONS = "X-Content-Type-Options";

	/**
	 * The {@code X-XSS-Protection} header name. This is a non-standard header, but is implemented and used
	 * in all "modern" browsers.
	 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/X-XSS-Protection">https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/X-XSS-Protection</a>
	 */
	public static final String X_XSS_PROTECTION = "X-XSS-Protection";

	/**
	 * The {@code X-Requested-With} header name: this is non-standard header but is generally used by libraries and
	 * frameworks (such as jQuery) to give information about the HTTP request.
	 */
	public static final String REQUESTED_WITH = "X-Requested-With";

	/**
	 * The {@code X-Http-Method-Override} header name: this is non-standard header but is generally used by libraries and
	 * frameworks to override the method specified in the template. This can be used when a user agent or
	 * firewall prevents PUT or DELETE methods from being sent directly.
	 */
	public static final String X_HTTP_METHOD_OVERRIDE = "X-Http-Method-Override";

	/**
	 * The {@code X-Csrf-Token} header name.
	 * This header is generally used to prevent CSRF attack and should be added in {@code POST}, {@code PUT} or {@code DELETE}
	 * requests.
	 */
	public static final String X_CSRF_TOKEN = "X-Csrf-Token";

	/**
	 * The value for {@link HttpHeaders#REQUESTED_WITH} header used by Web libraries such as jQuery.
	 */
	public static final String XML_HTTP_REQUEST = "XMLHttpRequest";

	/**
	 * The JSON media type.
	 * @see <a href="https://tools.ietf.org/html/rfc4627">https://tools.ietf.org/html/rfc4627</a>
	 */
	public static final String APPLICATION_JSON = "application/json";

	/**
	 * The XML media type.
	 * @see <a href="https://tools.ietf.org/html/rfc7303">https://tools.ietf.org/html/rfc7303</a>
	 */
	public static final String APPLICATION_XML = "application/xml";

	/**
	 * The media type for HTML forms.
	 * @see <a href="https://tools.ietf.org/html/draft-hoehrmann-urlencoded-01">https://tools.ietf.org/html/draft-hoehrmann-urlencoded-01</a>
	 */
	public static final String APPLICATION_FORM_URL_ENCODED = "application/x-www-form-urlencoded";

	/**
	 * The media type for file upload.
	 * @see <a href="https://www.ietf.org/rfc/rfc1867.txt">https://www.ietf.org/rfc/rfc1867.txt</a>
	 */
	public static final String MULTIPART_FORM_DATA = "multipart/form-data";
}
