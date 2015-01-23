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

import com.github.mjeanroy.junit.servers.client.HttpParameter;
import com.github.mjeanroy.junit.servers.client.HttpRequest;
import com.github.mjeanroy.junit.servers.client.HttpResponse;
import com.github.mjeanroy.junit.servers.exceptions.HttpClientException;

import static com.github.mjeanroy.junit.servers.client.HttpHeaders.ACCEPT;
import static com.github.mjeanroy.junit.servers.client.HttpHeaders.APPLICATION_FORM_URL_ENCODED;
import static com.github.mjeanroy.junit.servers.client.HttpHeaders.APPLICATION_JSON;
import static com.github.mjeanroy.junit.servers.client.HttpHeaders.APPLICATION_XML;
import static com.github.mjeanroy.junit.servers.client.HttpHeaders.CONTENT_TYPE;
import static com.github.mjeanroy.junit.servers.client.HttpHeaders.MULTIPART_FORM_DATA;
import static com.github.mjeanroy.junit.servers.client.HttpHeaders.REQUESTED_WITH;
import static com.github.mjeanroy.junit.servers.client.HttpHeaders.XML_HTTP_REQUEST;
import static com.github.mjeanroy.junit.servers.client.HttpParameter.param;
import static com.github.mjeanroy.junit.servers.commons.Preconditions.notNull;

/**
 * Abstract skeleton of {HttpRequest} interface.
 */
public abstract class AbstractHttpRequest implements HttpRequest {

	@Override
	public HttpRequest asXmlHttpRequest() {
		return addHeader(REQUESTED_WITH, XML_HTTP_REQUEST);
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
		HttpRequest self = applyQueryParam(parameter.getName(), parameter.getValue());

		// Add other parameters if available
		if (parameters != null) {
			for (HttpParameter p : parameters) {
				notNull(p, "parameter");
				self = applyQueryParam(p.getName(), p.getValue());
			}
		}

		return self;
	}

	@Override
	public HttpRequest addFormParam(String name, String value) {
		return addFormParams(param(name, value));
	}

	@Override
	public HttpRequest addFormParams(HttpParameter parameter, HttpParameter... parameters) {
		if (!getMethod().isBodyAllowed()) {
			throw new UnsupportedOperationException("Http method " + getMethod() + " does not support body parameters");
		}

		notNull(parameter, "parameter");
		HttpRequest self = applyFormParameter(parameter.getName(), parameter.getValue());

		if (parameters != null) {
			for (HttpParameter p : parameters) {
				notNull(p, "parameter");
				self = applyFormParameter(p.getName(), p.getValue());
			}
		}

		return self.asFormUrlEncoded();
	}

	@Override
	public HttpRequest setBody(String body) {
		if (!getMethod().isBodyAllowed()) {
			throw new UnsupportedOperationException("Http method " + getMethod() + " does not support request body");
		}

		return applyBody(notNull(body, "body"));
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
	 * Execute request.
	 * Exception will be automatically catched and translated into
	 * an instance of {HttpClientException}.
	 *
	 * @return Http response.
	 * @throws Exception
	 */
	protected abstract HttpResponse doExecute() throws Exception;

	/**
	 * Add form parameters.
	 * This method will be called for POST or PUT request only.
	 * This method can assume that parameter name is not blank and
	 * parameter value is not null.
	 *
	 * @param name Parameter name, will never be blank.
	 * @param value Parameter value, will never be null.
	 * @return Current request.
	 */
	protected abstract HttpRequest applyFormParameter(String name, String value);

	/**
	 * Add query parameters.
	 * This method should not check for parameters validity since it will be already
	 * checked before.
	 *
	 * @param name Parameter name.
	 * @param value Parameter value.
	 * @return Current request.
	 */
	protected abstract HttpRequest applyQueryParam(String name, String value);

	/**
	 * Add request body.
	 * This method should not check for parameters validity since it will be already
	 * checked before.
	 *
	 * @param body Request body.
	 * @return Current request.
	 */
	protected abstract HttpRequest applyBody(String body);
}
