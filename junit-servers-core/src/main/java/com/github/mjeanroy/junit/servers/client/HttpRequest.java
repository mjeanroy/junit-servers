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

import java.util.Date;

/**
 * HTTP request.
 * @see <a href="https://tools.ietf.org/html/rfc2616#section-5">https://tools.ietf.org/html/rfc2616#section-5</a>
 */
public interface HttpRequest {

	/**
	 * Request URL.
	 *
	 * @return The request endpoint.
	 */
	HttpUrl getEndpoint();

	/**
	 * Return http request method.
	 *
	 * @return Method.
	 */
	HttpMethod getMethod();

	/**
	 * Add header.
	 *
	 * @param name Header name.
	 * @param value Header value.
	 * @return Http request that can be used for chaining.
	 * @throws NullPointerException If {@code name} or {@code value} is {@code null}.
	 * @throws IllegalArgumentException if {@code name} is blank.
	 * @see HttpRequest#addHeader(HttpHeader)
	 */
	HttpRequest addHeader(String name, String value);

	/**
	 * Add header.
	 *
	 * @param header The header.
	 * @return Http request that can be used for chaining.
	 * @throws NullPointerException If {@code header} is {@code null}.
	 */
	HttpRequest addHeader(HttpHeader header);

	/**
	 * Add query parameters: a query parameter is a parameter that will
	 * follow the {@code ?} character in the request URL.
	 *
	 * @param name Parameter name.
	 * @param value Parameter value.
	 * @return Http request that can be used for chaining.
	 * @see <a href="https://tools.ietf.org/html/rfc3986#section-3.4">https://tools.ietf.org/html/rfc3986#section-3.4</a>
	 */
	HttpRequest addQueryParam(String name, String value);

	/**
	 * Add collection of query parameters: a query parameter is a parameter that will
	 * follow the {@code ?} character in the request URL.
	 *
	 * @param parameter Parameter.
	 * @param parameters Optional next parameters.
	 * @return Http request that can be used for chaining.
	 * @see <a href="https://tools.ietf.org/html/rfc3986#section-3.4">https://tools.ietf.org/html/rfc3986#section-3.4</a>
	 */
	HttpRequest addQueryParams(HttpParameter parameter, HttpParameter... parameters);

	/**
	 * Add form parameters.
	 * This method should be used for POST or PUT request only, otherwise
	 * it will throw {@link UnsupportedOperationException} exception.
	 *
	 * @param name Parameter name.
	 * @param value Parameter value.
	 * @return Http request that can be used for chaining.
	 * @throws UnsupportedOperationException If request method does not allow body (i.e {@code GET}, {@code HEAD} or {@code DELETE}).
	 */
	HttpRequest addFormParam(String name, String value);

	/**
	 * Add collection of parameters.
	 * This method should be used for POST or PUT request only, otherwise
	 * it will throw {@link UnsupportedOperationException} exception.
	 *
	 * @param parameter Parameter.
	 * @param parameters Optional next parameters.
	 * @return Http request that can be used for chaining.
	 * @throws UnsupportedOperationException If request method does not allow body (i.e {@code GET}, {@code HEAD} or {@code DELETE}).
	 */
	HttpRequest addFormParams(HttpParameter parameter, HttpParameter... parameters);

	/**
	 * Set request body.
	 * This method should be used for POST or PUT request only, otherwise
	 * it will throw {@link UnsupportedOperationException} exception.
	 *
	 * @param body Body request.
	 * @return Http request that can be used for chaining.
	 * @throws UnsupportedOperationException If request method does not allow body (i.e {@code GET}, {@code HEAD} or {@code DELETE}).
	 */
	HttpRequest setBody(String body);

	/**
	 * Most library (such as jQuery) add automatically header
	 * named "X-Requested-With" with value "XMLHttpRequest", this
	 * method add this header and can be used to simulate ajax
	 * call.
	 *
	 * @return Http request that can be used for chaining.
	 * @see HttpHeaders#REQUESTED_WITH
	 * @see HttpHeaders#XML_HTTP_REQUEST
	 */
	HttpRequest asXmlHttpRequest();

	/**
	 * Add header to specify that content type
	 * is {@code "application/x-www-form-urlencoded"}.
	 *
	 * @return Http request that can be used for chaining.
	 * @see HttpHeaders#APPLICATION_FORM_URL_ENCODED
	 * @see <a href="https://tools.ietf.org/html/draft-hoehrmann-urlencoded-01">https://tools.ietf.org/html/draft-hoehrmann-urlencoded-01</a>
	 */
	HttpRequest asFormUrlEncoded();

	/**
	 * Add header to specify that content type
	 * is {@code "multipart/form-data"}.
	 *
	 * @return Http request that can be used for chaining.
	 * @see HttpHeaders#MULTIPART_FORM_DATA
	 * @see <a href="https://www.ietf.org/rfc/rfc1867.txt">https://www.ietf.org/rfc/rfc1867.txt</a>
	 */
	HttpRequest asMultipartFormData();

	/**
	 * Add header to specify that content type
	 * is {@code "application/json"}.
	 *
	 * @return Http request that can be used for chaining.
	 * @see HttpHeaders#CONTENT_TYPE
	 * @see HttpHeaders#APPLICATION_JSON
	 * @see <a href="https://www.w3.org/Protocols/rfc1341/4_Content-Type.html">https://www.w3.org/Protocols/rfc1341/4_Content-Type.html</a>
	 */
	HttpRequest asJson();

	/**
	 * Add header to specify that content type
	 * is {@code "application/xml"}.
	 *
	 * @return Http request that can be used for chaining.
	 * @see HttpHeaders#CONTENT_TYPE
	 * @see HttpHeaders#APPLICATION_XML
	 * @see <a href="https://www.w3.org/Protocols/rfc1341/4_Content-Type.html">https://www.w3.org/Protocols/rfc1341/4_Content-Type.html</a>
	 */
	HttpRequest asXml();

	/**
	 * Add header to specify that accept type
	 * is {@code "application/json"}.
	 *
	 * @return Http request that can be used for chaining.
	 * @see HttpHeaders#ACCEPT
	 * @see HttpHeaders#APPLICATION_JSON
	 * @see <a href="https://tools.ietf.org/html/rfc7231#section-5.3.2">https://tools.ietf.org/html/rfc7231#section-5.3.2</a>
	 */
	HttpRequest acceptJson();

	/**
	 * Add header to specify that accept type
	 * is {@code "application/xml"}.
	 *
	 * @return Http request that can be used for chaining.
	 * @see HttpHeaders#ACCEPT
	 * @see HttpHeaders#APPLICATION_XML
	 * @see <a href="https://tools.ietf.org/html/rfc7231#section-5.3.2">https://tools.ietf.org/html/rfc7231#section-5.3.2</a>
	 */
	HttpRequest acceptXml();

	/**
	 * Add {@code Accept-Language} header.
	 *
	 * @param lang Accepted languages.
	 * @return Http request that can be used for chaining.
	 * @see HttpHeaders#ACCEPT_LANGUAGE
	 * @see <a href="https://tools.ietf.org/html/rfc7231#section-5.3.5">https://tools.ietf.org/html/rfc7231#section-5.3.5</a>
	 */
	HttpRequest acceptLanguage(String lang);

	/**
	 * Add {@code Origin} header.
	 *
	 * @param origin Origin value.
	 * @return Http request that can be used for chaining.
	 * @see HttpHeaders#ORIGIN
	 * @see <a href="https://tools.ietf.org/html/rfc6454">https://tools.ietf.org/html/rfc6454</a>
	 */
	HttpRequest addOrigin(String origin);

	/**
	 * Add {@code Referer} header.
	 *
	 * @param referer Referer value.
	 * @return Http request that can be used for chaining.
	 * @see HttpHeaders#REFERER
	 * @see <a href="https://tools.ietf.org/html/rfc7231#section-5.5.2">https://tools.ietf.org/html/rfc7231#section-5.5.2</a>
	 */
	HttpRequest addReferer(String referer);

	/**
	 * Add {@code Accept-Encoding} header.
	 *
	 * @param encoding Encoding value.
	 * @return Http request that can be used for chaining.
	 * @see HttpHeaders#ACCEPT_ENCODING
	 * @see <a href="https://tools.ietf.org/html/rfc7231#section-5.3.4">https://tools.ietf.org/html/rfc7231#section-5.3.4</a>
	 */
	HttpRequest addAcceptEncoding(String encoding);

	/**
	 * Add {@code Accept-Encoding} header with {@code "gzip, deflate"} value.
	 *
	 * @return Http request that can be used for chaining.
	 * @see HttpHeaders#ACCEPT_ENCODING
	 * @see <a href="https://tools.ietf.org/html/rfc7231#section-5.3.4">https://tools.ietf.org/html/rfc7231#section-5.3.4</a>
	 * @see #addAcceptEncoding(String)
	 */
	HttpRequest acceptGzip();

	/**
	 * Add {@code User-Agent} header to http request.
	 *
	 * @param userAgent User-Agent value.
	 * @return Http request that can be used for chaining.
	 * @see HttpHeaders#USER_AGENT
	 * @see <a href="https://tools.ietf.org/html/rfc2616#section-14.43">https://tools.ietf.org/html/rfc2616#section-14.43</a>
	 */
	HttpRequest withUserAgent(String userAgent);

	/**
	 * Add cookie to http request.
	 *
	 * @param cookie Cookie.
	 * @return Http request that can be used for chaining.
	 * @see HttpHeaders#COOKIE
	 * @see <a href="https://developer.mozilla.org/fr/docs/Web/HTTP/Headers/Cookie">https://developer.mozilla.org/fr/docs/Web/HTTP/Headers/Cookie</a>
	 * @see <a href="https://tools.ietf.org/html/rfc6265#section-5.4">https://tools.ietf.org/html/rfc6265#section-5.4</a>
	 */
	HttpRequest addCookie(Cookie cookie);

	/**
	 * Add cookie to http request.
	 *
	 * @param name Cookie name.
	 * @param value Cookie value.
	 * @return Http request that can be used for chaining.
	 * @throws NullPointerException If {@code name} or {@code value} are {@code null}.
	 * @throws IllegalArgumentException If {@code name} is empty or blank.
	 * @see HttpHeaders#COOKIE
	 * @see #addCookie(Cookie)
	 * @see <a href="https://developer.mozilla.org/fr/docs/Web/HTTP/Headers/Cookie">https://developer.mozilla.org/fr/docs/Web/HTTP/Headers/Cookie</a>
	 * @see <a href="https://tools.ietf.org/html/rfc6265#section-5.4">https://tools.ietf.org/html/rfc6265#section-5.4</a>
	 */
	HttpRequest addCookie(String name, String value);

	/**
	 * Add {@code If-None-Match} header with expected value.
	 * This header should work with {@code ETag} header sent by server
	 * response.
	 *
	 * @param etag ETag value.
	 * @return Http request that can be used for chaining.
	 * @see HttpHeaders#IF_NONE_MATCH
	 * @see <a href="https://tools.ietf.org/html/rfc7232#section-3.2">https://tools.ietf.org/html/rfc7232#section-3.2</a>
	 */
	HttpRequest addIfNoneMatch(String etag);

	/**
	 * Add {@code If-Match} header with expected value.
	 * This header should work with ETag header sent by server
	 * response.
	 *
	 * @param etag ETag value.
	 * @return Http request that can be used for chaining.
	 * @see HttpHeaders#IF_MATCH
	 * @see <a href="https://tools.ietf.org/html/rfc7232#section-3.1">https://tools.ietf.org/html/rfc7232#section-3.1</a>
	 */
	HttpRequest addIfMatch(String etag);

	/**
	 * Add {@code If-Modified-Since} value with expected date (date will be translated
	 * as GMT raw).
	 *
	 * @param date Date.
	 * @return Http request that can be used for chaining.
	 * @see HttpHeaders#IF_MODIFIED_SINCE
	 * @see <a href="https://tools.ietf.org/html/rfc7232#section-3.3">https://tools.ietf.org/html/rfc7232#section-3.3</a>
	 */
	HttpRequest addIfModifiedSince(Date date);

	/**
	 * Add {@code If-Unmodified-Since} value with expected date (date will be translated
	 * as GMT raw).
	 *
	 * @param date Date.
	 * @return Http request that can be used for chaining.
	 * @see HttpHeaders#IF_UNMODIFIED_SINCE
	 * @see <a href="https://tools.ietf.org/html/rfc7232#section-3.4">https://tools.ietf.org/html/rfc7232#section-3.4</a>
	 */
	HttpRequest addIfUnmodifiedSince(Date date);

	/**
	 * Add {@code X-Http-Method-Override} value with HTTP verb to override.
	 * This method is generally used with {@code POST} request to override
	 * {@code PUT} or {@code DELETE} requests.
	 *
	 * @param method Http method to override.
	 * @return Http request that can be used for chaining.
	 * @see HttpHeaders#X_HTTP_METHOD_OVERRIDE
	 */
	HttpRequest addXHttpMethodOverride(String method);

	/**
	 * Add CSRF Token to http header ({@code X-Csrf-Token} header).
	 *
	 * @param token Token value.
	 * @return Http request that can be used for chaining.
	 * @see HttpHeaders#X_CSRF_TOKEN
	 */
	HttpRequest addCsrfToken(String token);

	/**
	 * Add @{code X-Http-Method-Override} value with {@code PUT} value.
	 *
	 * @return Http request that can be used for chaining.
	 * @see HttpHeaders#X_HTTP_METHOD_OVERRIDE
	 * @see #addXHttpMethodOverride(String)
	 */
	HttpRequest overridePut();

	/**
	 * Add {@code X-Http-Method-Override} value with {@code DELETE} value.
	 *
	 * @return Http request that can be used for chaining.
	 * @see HttpHeaders#X_HTTP_METHOD_OVERRIDE
	 * @see #addXHttpMethodOverride(String)
	 */
	HttpRequest overrideDelete();

	/**
	 * Execute request and return http response: execution is synchronous and will block until
	 * response is available.
	 *
	 * @return Http response.
	 */
	HttpResponse execute();

	/**
	 * Execute request and return http response: execution is synchronous and will block until
	 * response is available.
	 *
	 * <p>
	 *
	 * This method automatically add json header (i.e
	 * methods {@link #asJson} and {@link #acceptJson} will be automatically
	 * be called before execution).
	 *
	 * @return Http response.
	 * @see #acceptJson()
	 * @see #asJson()
	 */
	HttpResponse executeJson();

	/**
	 * Execute request and return http response: execution is synchronous and will block until
	 * response is available.
	 *
	 * <p>
	 *
	 * This method automatically add xml header (i.e
	 * methods {@link #asXml} and {@link #acceptXml} will be automatically
	 * called before execution).
	 *
	 * @return Http response.
	 * @see #acceptXml()
	 * @see #asXml()
	 */
	HttpResponse executeXml();
}
