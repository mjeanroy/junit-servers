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

package com.github.mjeanroy.junit.servers.client.apache_http_client;

import com.github.mjeanroy.junit.servers.client.AbstractHttpRequest;
import com.github.mjeanroy.junit.servers.client.HttpMethod;
import com.github.mjeanroy.junit.servers.client.HttpRequest;
import com.github.mjeanroy.junit.servers.client.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static com.github.mjeanroy.junit.servers.commons.Checks.notBlank;
import static com.github.mjeanroy.junit.servers.commons.Checks.notNull;

/**
 * Implementation for {HttpRequest} that use apache http-client
 * under the hood.
 * See: http://hc.apache.org/httpcomponents-client-ga/index.html
 */
public class ApacheHttpRequest extends AbstractHttpRequest {

	/**
	 * Original http client.
	 * It will be used to execute http request.
	 */
	private final HttpClient client;

	/**
	 * Http request method (i.e GET, POST,
	 * PUT, DELETE).
	 */
	private final HttpMethod httpMethod;

	/**
	 * Http request url.
	 */
	private final String url;

	/**
	 * Map of query parameters.
	 */
	private final Map<String, String> queryParams;

	/**
	 * Map of headers.
	 */
	private final Map<String, String> headers;

	/**
	 * Create apache http request.
	 *
	 * @param client Apache http client.
	 * @param httpMethod Http method.
	 * @param url Http request url.
	 */
	ApacheHttpRequest(HttpClient client, HttpMethod httpMethod, String url) {
		this.client = client;
		this.httpMethod = httpMethod;
		this.url = url;
		this.queryParams = new HashMap<>();
		this.headers = new HashMap<>();
	}

	@Override
	public HttpRequest addHeader(String name, String value) {
		headers.put(
				notBlank(name, "name"),
				notBlank(value, "value")
		);
		return this;
	}

	@Override
	public HttpRequest addQueryParam(String name, String value) {
		queryParams.put(
				notBlank(name, "name"),
				notNull(value, "value") // Empty values should be allowed
		);
		return this;
	}

	@Override
	protected HttpResponse doExecute() throws Exception {
		URIBuilder uriBuilder = new URIBuilder(url);
		for (Map.Entry<String, String> p : queryParams.entrySet()) {
			uriBuilder = uriBuilder.addParameter(p.getKey(), p.getValue());
		}

		HttpRequestBase httpRequest = new ParameterizedHttpRequest(httpMethod, uriBuilder.build());
		for (Map.Entry<String, String> h : headers.entrySet()) {
			httpRequest.addHeader(h.getKey(), h.getValue());
		}

		org.apache.http.HttpResponse httpResponse = client.execute(httpRequest);
		return new ApacheHttpResponse(httpResponse);
	}

	private static class ParameterizedHttpRequest extends HttpRequestBase {
		private final HttpMethod httpMethod;

		private ParameterizedHttpRequest(HttpMethod httpMethod, URI uri) {
			super();
			this.httpMethod = httpMethod;
			setURI(uri);
		}

		@Override
		public String getMethod() {
			return httpMethod.getVerb();
		}
	}
}
