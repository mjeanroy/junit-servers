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

import java.util.Date;

/// HTTP request ([RFC 2616](https://tools.ietf.org/html/rfc2616#section-5)).
public interface HttpRequest {

	/// Request URL.
	///
	/// @return The request endpoint.
	HttpUrl getEndpoint();

	/// Return http request method.
	///
	/// @return Method.
	HttpMethod getMethod();

	/// Add header.
	///
	/// @param name Header name.
	/// @param value Header value.
	/// @return Http request that can be used for chaining.
	/// @throws NullPointerException If `name` or `value` is `null`.
	/// @throws IllegalArgumentException if `name` is blank.
	/// @see HttpRequest#addHeader(HttpHeader)
	HttpRequest addHeader(String name, String value);

	/// Add header.
	///
	/// @param header The header.
	/// @return Http request that can be used for chaining.
	/// @throws NullPointerException If `header` is `null`.
	HttpRequest addHeader(HttpHeader header);

	/// Add query parameters: a query parameter is a parameter that will
	/// follow the `?` character in the request URL ([RFC 3986](https://tools.ietf.org/html/rfc3986#section-3.4)).
	///
	/// @param name Parameter name.
	/// @param value Parameter value.
	/// @return Http request that can be used for chaining.
	HttpRequest addQueryParam(String name, String value);

	/// Add collection of query parameters: a query parameter is a parameter that will
	/// follow the `?` character in the request URL ([RFC 3986](https://tools.ietf.org/html/rfc3986#section-3.4)).
	///
	/// @param parameter Parameter.
	/// @param parameters Optional next parameters.
	/// @return Http request that can be used for chaining.
	HttpRequest addQueryParams(HttpParameter parameter, HttpParameter... parameters);

	/// Set request body.
	/// This method should be used for `"POST"`, `"PUT"` or `"PATCH"` request only, otherwise
	/// it will throw [UnsupportedOperationException] exception.
	///
	/// @param body Body request.
	/// @return Http request that can be used for chaining.
	/// @throws UnsupportedOperationException If request method does not allow body (i.e `GET`, `HEAD` or `DELETE`).
	HttpRequest setBody(HttpRequestBody body);

	/// Most library (such as jQuery) add automatically header
	/// named "X-Requested-With" with value "XMLHttpRequest", this
	/// method add this header and can be used to simulate ajax
	/// call.
	///
	/// @return Http request that can be used for chaining.
	/// @see HttpHeaders#REQUESTED_WITH
	/// @see HttpHeaders#XML_HTTP_REQUEST
	HttpRequest asXmlHttpRequest();

	/// Add header to specify that content type is `"application/x-www-form-urlencoded"` ([RFC](https://tools.ietf.org/html/draft-hoehrmann-urlencoded-01)).
	///
	/// @return Http request that can be used for chaining.
	/// @see MediaType#APPLICATION_FORM_URL_ENCODED
	HttpRequest asFormUrlEncoded();

	/// Add header to specify that content type is `"application/json"` ([RFC 1341](https://www.w3.org/Protocols/rfc1341/4_Content-Type.html)).
	///
	/// @return Http request that can be used for chaining.
	/// @see HttpHeaders#CONTENT_TYPE
	/// @see MediaType#APPLICATION_JSON
	HttpRequest asJson();

	/// Add header to specify that content type is `"application/xml"` ([RFC 1341](https://www.w3.org/Protocols/rfc1341/4_Content-Type.html)).
	///
	/// @return Http request that can be used for chaining.
	/// @see HttpHeaders#CONTENT_TYPE
	/// @see MediaType#APPLICATION_XML
	HttpRequest asXml();

	/// Add header to specify that accept type is `"application/json"` ([RFC 1341](https://www.w3.org/Protocols/rfc1341/4_Content-Type.html)).
	///
	/// @return Http request that can be used for chaining.
	/// @see HttpHeaders#ACCEPT
	/// @see MediaType#APPLICATION_JSON
	HttpRequest acceptJson();

	/// Add header to specify that accept type is `"application/xml"` ([RFC 1341](https://www.w3.org/Protocols/rfc1341/4_Content-Type.html)).
	///
	/// @return Http request that can be used for chaining.
	/// @see HttpHeaders#ACCEPT
	/// @see MediaType#APPLICATION_XML
	HttpRequest acceptXml();

	/// Add `Accept-Language` header ([RFC 7231](https://tools.ietf.org/html/rfc7231#section-5.3.5)).
	///
	/// @param lang Accepted languages.
	/// @return Http request that can be used for chaining.
	/// @see HttpHeaders#ACCEPT_LANGUAGE
	HttpRequest acceptLanguage(String lang);

	/// Add `Origin` header ([RFC 6454](https://tools.ietf.org/html/rfc6454)).
	///
	/// @param origin Origin value.
	/// @return Http request that can be used for chaining.
	/// @see HttpHeaders#ORIGIN
	HttpRequest addOrigin(String origin);

	/// Add `Referer` header ([RFC 7231](https://tools.ietf.org/html/rfc7231#section-5.5.2)).
	///
	/// @param referer Referer value.
	/// @return Http request that can be used for chaining.
	/// @see HttpHeaders#REFERER
	HttpRequest addReferer(String referer);

	/// Add `Accept-Encoding` header ([RCC 7231](https://tools.ietf.org/html/rfc7231#section-5.3.4)).
	///
	/// @param encoding Encoding value.
	/// @return Http request that can be used for chaining.
	/// @see HttpHeaders#ACCEPT_ENCODING
	HttpRequest addAcceptEncoding(String encoding);

	/// Add `Accept-Encoding` header with `"gzip, deflate"` value ([RCC 7231](https://tools.ietf.org/html/rfc7231#section-5.3.4)).
	///
	/// @return Http request that can be used for chaining.
	/// @see HttpHeaders#ACCEPT_ENCODING
	/// @see #addAcceptEncoding(String)
	HttpRequest acceptGzip();

	/// Add `User-Agent` header to http request ([RFC 2616](https://tools.ietf.org/html/rfc2616#section-14.43)).
	///
	/// @param userAgent User-Agent value.
	/// @return Http request that can be used for chaining.
	/// @see HttpHeaders#USER_AGENT
	HttpRequest withUserAgent(String userAgent);

	/// Add cookie to http request.
	///
	/// See:
	/// - [MDN](https://developer.mozilla.org/fr/docs/Web/HTTP/Headers/Cookie)
	/// - [RFC 6265](https://tools.ietf.org/html/rfc6265#section-5.4)
	///
	/// @param cookie Cookie.
	/// @return Http request that can be used for chaining.
	/// @see HttpHeaders#COOKIE
	HttpRequest addCookie(Cookie cookie);

	/// Add cookie to http request.
	///
	/// See:
	/// - [MDN](https://developer.mozilla.org/fr/docs/Web/HTTP/Headers/Cookie)
	/// - [RFC 6265](https://tools.ietf.org/html/rfc6265#section-5.4)
	///
	/// @param name Cookie name.
	/// @param value Cookie value.
	/// @return Http request that can be used for chaining.
	/// @throws NullPointerException If `name` or `value` are `null`.
	/// @throws IllegalArgumentException If `name` is empty or blank.
	/// @see HttpHeaders#COOKIE
	/// @see #addCookie(Cookie)
	HttpRequest addCookie(String name, String value);

	/// Add `If-None-Match` header with expected value ([RFC 7232](https://tools.ietf.org/html/rfc7232#section-3.2)).
	/// This header should work with `ETag` header sent by server
	/// response.
	///
	/// @param etag ETag value.
	/// @return Http request that can be used for chaining.
	/// @see HttpHeaders#IF_NONE_MATCH
	HttpRequest addIfNoneMatch(String etag);

	/// Add `If-Match` header with expected value ([RFC 7232](https://tools.ietf.org/html/rfc7232#section-3.1)).
	/// This header should work with ETag header sent by server
	/// response.
	///
	/// @param etag ETag value.
	/// @return Http request that can be used for chaining.
	/// @see HttpHeaders#IF_MATCH
	HttpRequest addIfMatch(String etag);

	/// Add `If-Modified-Since` value with expected date, date will be translated
	/// as GMT raw ([RFC 7232](https://tools.ietf.org/html/rfc7232#section-3.3)).
	///
	/// @param date Date.
	/// @return Http request that can be used for chaining.
	/// @see HttpHeaders#IF_MODIFIED_SINCE
	HttpRequest addIfModifiedSince(Date date);

	/// Add `If-Unmodified-Since` value with expected date, date will be translated
	/// as GMT raw ([RFC 7232](https://tools.ietf.org/html/rfc7232#section-3.4)).
	///
	/// @param date Date.
	/// @return Http request that can be used for chaining.
	/// @see HttpHeaders#IF_UNMODIFIED_SINCE
	HttpRequest addIfUnmodifiedSince(Date date);

	/// Add `X-Http-Method-Override` value with HTTP verb to override.
	/// This method is generally used with `POST` request to override
	/// `PUT` or `DELETE` requests.
	///
	/// @param method Http method to override.
	/// @return Http request that can be used for chaining.
	/// @see HttpHeaders#X_HTTP_METHOD_OVERRIDE
	HttpRequest addXHttpMethodOverride(String method);

	/// Add CSRF Token to http header (`X-Csrf-Token` header).
	///
	/// @param token Token value.
	/// @return Http request that can be used for chaining.
	/// @see HttpHeaders#X_CSRF_TOKEN
	HttpRequest addCsrfToken(String token);

	/// Add @{code X-Http-Method-Override} value with `PUT` value.
	///
	/// @return Http request that can be used for chaining.
	/// @see HttpHeaders#X_HTTP_METHOD_OVERRIDE
	/// @see #addXHttpMethodOverride(String)
	HttpRequest overridePut();

	/// Add `X-Http-Method-Override` value with `DELETE` value.
	///
	/// @return Http request that can be used for chaining.
	/// @see HttpHeaders#X_HTTP_METHOD_OVERRIDE
	/// @see #addXHttpMethodOverride(String)
	HttpRequest overrideDelete();

	/// Execute request and return http response: execution is synchronous and will block until
	/// response is available.
	///
	/// @return Http response.
	HttpResponse execute();

	/// Execute request and return http response: execution is synchronous and will block until
	/// response is available.
	///
	/// This method automatically add json header (i.e
	/// methods [#asJson] and [#acceptJson] will be automatically
	/// be called before execution).
	///
	/// @return Http response.
	/// @see #acceptJson()
	/// @see #asJson()
	HttpResponse executeJson();

	/// Execute request and return http response: execution is synchronous and will block until
	/// response is available.
	///
	/// This method automatically add xml header (i.e
	/// methods [#asXml] and [#acceptXml] will be automatically
	/// called before execution).
	///
	/// @return Http response.
	/// @see #acceptXml()
	/// @see #asXml()
	HttpResponse executeXml();
}
