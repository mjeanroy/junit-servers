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

import com.github.mjeanroy.junit.servers.exceptions.HttpClientException;

/**
 * Abstract skeleton of {HttpRequest} interface.
 */
public abstract class AbstractHttpRequest implements HttpRequest {

	@Override
	public HttpRequest asXmlHttpRequest() {
		return addHeader("X-Requested-With", "XMLHttpRequest");
	}

	@Override
	public HttpRequest asJson() {
		return addHeader("Content-Type", "application/json");
	}

	@Override
	public HttpRequest asXml() {
		return addHeader("Content-Type", "application/xml");
	}

	@Override
	public HttpRequest asFormUrlEncoded() {
		return addHeader("Content-Type", "application/x-www-form-urlencoded");
	}

	@Override
	public HttpRequest acceptJson() {
		return addHeader("Accept", "application/json");
	}

	@Override
	public HttpRequest acceptXml() {
		return addHeader("Accept", "application/xml");
	}

	@Override
	public HttpResponse execute() {
		try {
			return doExecute();
		} catch (Exception ex) {
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
}
