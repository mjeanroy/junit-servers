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

package com.github.mjeanroy.junit.servers.client.impl;

import static com.github.mjeanroy.junit.servers.client.HttpHeader.header;
import static com.github.mjeanroy.junit.servers.client.HttpHeaders.ACCEPT;
import static com.github.mjeanroy.junit.servers.client.HttpHeaders.ACCEPT_ENCODING;
import static com.github.mjeanroy.junit.servers.client.HttpHeaders.ACCEPT_LANGUAGE;
import static com.github.mjeanroy.junit.servers.client.HttpHeaders.APPLICATION_FORM_URL_ENCODED;
import static com.github.mjeanroy.junit.servers.client.HttpHeaders.APPLICATION_JSON;
import static com.github.mjeanroy.junit.servers.client.HttpHeaders.APPLICATION_XML;
import static com.github.mjeanroy.junit.servers.client.HttpHeaders.CONTENT_TYPE;
import static com.github.mjeanroy.junit.servers.client.HttpHeaders.IF_MATCH;
import static com.github.mjeanroy.junit.servers.client.HttpHeaders.IF_MODIFIED_SINCE;
import static com.github.mjeanroy.junit.servers.client.HttpHeaders.IF_NONE_MATCH;
import static com.github.mjeanroy.junit.servers.client.HttpHeaders.IF_UNMODIFIED_SINCE;
import static com.github.mjeanroy.junit.servers.client.HttpHeaders.MULTIPART_FORM_DATA;
import static com.github.mjeanroy.junit.servers.client.HttpHeaders.ORIGIN;
import static com.github.mjeanroy.junit.servers.client.HttpHeaders.REFERER;
import static com.github.mjeanroy.junit.servers.client.HttpHeaders.REQUESTED_WITH;
import static com.github.mjeanroy.junit.servers.client.HttpHeaders.USER_AGENT;
import static com.github.mjeanroy.junit.servers.client.HttpHeaders.XML_HTTP_REQUEST;
import static com.github.mjeanroy.junit.servers.client.HttpHeaders.X_CSRF_TOKEN;
import static com.github.mjeanroy.junit.servers.client.HttpHeaders.X_HTTP_METHOD_OVERRIDE;
import static com.github.mjeanroy.junit.servers.client.HttpMethod.DELETE;
import static com.github.mjeanroy.junit.servers.client.HttpMethod.PUT;
import static com.github.mjeanroy.junit.servers.client.HttpParameter.param;
import static com.github.mjeanroy.junit.servers.commons.Dates.format;
import static com.github.mjeanroy.junit.servers.commons.Preconditions.notBlank;
import static com.github.mjeanroy.junit.servers.commons.Preconditions.notNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.github.mjeanroy.junit.servers.client.Cookie;
import com.github.mjeanroy.junit.servers.client.Cookies;
import com.github.mjeanroy.junit.servers.client.HttpHeader;
import com.github.mjeanroy.junit.servers.client.HttpMethod;
import com.github.mjeanroy.junit.servers.client.HttpParameter;
import com.github.mjeanroy.junit.servers.client.HttpRequest;
import com.github.mjeanroy.junit.servers.client.HttpResponse;
import com.github.mjeanroy.junit.servers.client.HttpUrl;
import com.github.mjeanroy.junit.servers.exceptions.HttpClientException;

/**
 * Abstract skeleton of {@link HttpRequest} interface.
 *
 * <p>
 *
 * <strong>This abstract class is not part of the public API and should not be used publicly.</strong>
 */
public abstract class AbstractHttpRequest implements HttpRequest {

	/**
	 * The request URL.
	 */
	private final HttpUrl endpoint;

	/**
	 * The request method.
	 */
	private final HttpMethod method;

	/**
	 * Request parameters.
	 * Query parameters are the parameters following {@code ?} character in the URL.
	 */
	protected final Map<String, HttpParameter> queryParams;

	/**
	 * Form parameters, typically sent using a "classic" HTML form.
	 */
	protected final Map<String, HttpParameter> formParams;

	/**
	 * The request body.
	 */
	protected String body;

	/**
	 * Cookie elements.
	 */
	protected final List<Cookie> cookies;

	/**
	 * HTTP Headers.
	 */
	protected final Map<String, HttpHeader> headers;

	protected AbstractHttpRequest(HttpUrl endpoint, HttpMethod method) {
		this.endpoint = notNull(endpoint, "endpoint");
		this.method = notNull(method, "method");

		this.queryParams = new LinkedHashMap<>();
		this.formParams = new LinkedHashMap<>();
		this.headers = new LinkedHashMap<>();
		this.cookies = new ArrayList<>(10);
	}

	@Override
	public HttpUrl getEndpoint() {
		return endpoint;
	}

	@Override
	public HttpMethod getMethod() {
		return method;
	}

	@Override
	public HttpRequest addHeader(String name, String value) {
		notBlank(name, "name");
		notNull(value, "value");
		return addHeader(header(name, value));
	}

	@Override
	public HttpRequest addHeader(HttpHeader header) {
		notNull(header, "header");
		headers.put(header.getName(), header);
		return this;
	}

	@Override
	public HttpRequest asXmlHttpRequest() {
		return addHeader(REQUESTED_WITH, XML_HTTP_REQUEST);
	}

	@Override
	public HttpRequest acceptLanguage(String lang) {
		return addHeader(ACCEPT_LANGUAGE, notBlank(lang, "lang"));
	}

	@Override
	public HttpRequest addAcceptEncoding(String encoding) {
		return addHeader(ACCEPT_ENCODING, notBlank(encoding, "encoding"));
	}

	@Override
	public HttpRequest acceptGzip() {
		return addAcceptEncoding("gzip, deflate");
	}

	@Override
	public HttpRequest addOrigin(String origin) {
		return addHeader(ORIGIN, notBlank(origin, "origin"));
	}

	@Override
	public HttpRequest addReferer(String referer) {
		return addHeader(REFERER, notBlank(referer, "referer"));
	}

	@Override
	public HttpRequest addIfNoneMatch(String etag) {
		return addHeader(IF_NONE_MATCH, notBlank(etag, "etag"));
	}

	@Override
	public HttpRequest addIfMatch(String etag) {
		return addHeader(IF_MATCH, notBlank(etag, "etag"));
	}

	@Override
	public HttpRequest addIfModifiedSince(Date date) {
		String value = format(notNull(date, "date"), "EEE, dd MMM yyyy HH:mm:ss zzz");
		return addHeader(IF_MODIFIED_SINCE, value);
	}

	@Override
	public HttpRequest addIfUnmodifiedSince(Date date) {
		String value = format(notNull(date, "date"), "EEE, dd MMM yyyy HH:mm:ss zzz");
		return addHeader(IF_UNMODIFIED_SINCE, value);
	}

	@Override
	public HttpRequest withUserAgent(String userAgent) {
		return addHeader(USER_AGENT, notBlank(userAgent, "userAgent"));
	}

	@Override
	public HttpRequest asJson() {
		return addHeader(CONTENT_TYPE, APPLICATION_JSON);
	}

	@Override
	public HttpRequest asXml() {
		return addHeader(CONTENT_TYPE, APPLICATION_XML);
	}

	@Override
	public HttpRequest asFormUrlEncoded() {
		return addHeader(CONTENT_TYPE, APPLICATION_FORM_URL_ENCODED);
	}

	@Override
	public HttpRequest asMultipartFormData() {
		return addHeader(CONTENT_TYPE, MULTIPART_FORM_DATA);
	}

	@Override
	public HttpRequest addQueryParam(String name, String value) {
		return addQueryParams(param(name, value));
	}

	@Override
	public HttpRequest addQueryParams(HttpParameter parameter, HttpParameter... parameters) {
		notNull(parameter, "parameter");

		// Add first parameter.
		queryParams.put(parameter.getName(), parameter);

		// Add other parameters if available
		if (parameters != null) {
			for (HttpParameter p : parameters) {
				notNull(p, "parameter");
				queryParams.put(p.getName(), p);
			}
		}

		return this;
	}

	@Override
	public HttpRequest addFormParam(String name, String value) {
		return addFormParams(param(name, value));
	}

	@Override
	public HttpRequest addFormParams(HttpParameter parameter, HttpParameter... parameters) {
		notNull(parameter, "parameter");

		// Ensure request method allow body.
		if (!getMethod().isBodyAllowed()) {
			throw new UnsupportedOperationException("Http method " + getMethod() + " does not support body parameters");
		}

		// Ensure a body has not been previously set.
		if (body != null) {
			throw new IllegalStateException("Cannot add form parameter if a request body is already defined");
		}

		// Add first parameter.
		formParams.put(parameter.getName(), parameter);

		if (parameters != null) {
			for (HttpParameter p : parameters) {
				notNull(p, "parameter");
				formParams.put(p.getName(), p);
			}
		}

		return asFormUrlEncoded();
	}

	@Override
	public HttpRequest setBody(String body) {
		notNull(body, "body");

		// Ensure request body is allowed.
		if (!getMethod().isBodyAllowed()) {
			throw new UnsupportedOperationException("Http method " + getMethod() + " does not support request body");
		}

		// Ensure form parameters have not been previously set.
		if (!formParams.isEmpty()) {
			throw new IllegalStateException("Cannot add request body if form parameters are already defined");
		}

		// Set the request body.
		this.body = body;

		return this;
	}

	@Override
	public HttpRequest acceptJson() {
		return addHeader(ACCEPT, APPLICATION_JSON);
	}

	@Override
	public HttpRequest acceptXml() {
		return addHeader(ACCEPT, APPLICATION_XML);
	}

	@Override
	public HttpRequest addXHttpMethodOverride(String method) {
		return addHeader(X_HTTP_METHOD_OVERRIDE, notBlank(method, "method"));
	}

	@Override
	public HttpRequest addCsrfToken(String token) {
		return addHeader(X_CSRF_TOKEN, notBlank(token, "token"));
	}

	@Override
	public HttpRequest overridePut() {
		return addXHttpMethodOverride(PUT.getVerb());
	}

	@Override
	public HttpRequest overrideDelete() {
		return addXHttpMethodOverride(DELETE.getVerb());
	}

	@Override
	public HttpRequest addCookie(Cookie cookie) {
		notNull(cookie, "cookie");
		cookies.add(cookie);
		return this;
	}

	@Override
	public HttpRequest addCookie(String name, String value) {
		notBlank(name, "name");
		notNull(value, "value");
		return addCookie(Cookies.cookie(name, value));
	}

	@Override
	public HttpResponse execute() {
		try {
			return doExecute();
		}
		catch (Exception ex) {
			throw new HttpClientException(ex);
		}
	}

	@Override
	public HttpResponse executeJson() {
		return asJson().acceptJson().execute();
	}

	@Override
	public HttpResponse executeXml() {
		return asXml().acceptXml().execute();
	}

	/**
	 * Check if the request have a body content (form parameters or request body value).
	 *
	 * @return {@code true} if request has a body, {@code false} otherwise.
	 */
	protected boolean hasBody() {
		return body != null || !formParams.isEmpty();
	}

	/**
	 * Execute request.
	 * Exception will be automatically translated into
	 * an instance of {@link HttpClientException}.
	 *
	 * @return Http response.
	 * @throws Exception If an error occurred.
	 */
	protected abstract HttpResponse doExecute() throws Exception;
}
