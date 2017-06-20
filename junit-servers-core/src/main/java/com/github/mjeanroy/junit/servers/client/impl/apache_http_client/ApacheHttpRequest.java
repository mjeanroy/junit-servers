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

package com.github.mjeanroy.junit.servers.client.impl.apache_http_client;

import com.github.mjeanroy.junit.servers.client.Cookie;
import com.github.mjeanroy.junit.servers.client.HttpMethod;
import com.github.mjeanroy.junit.servers.client.HttpRequest;
import com.github.mjeanroy.junit.servers.client.HttpResponse;
import com.github.mjeanroy.junit.servers.client.impl.AbstractHttpRequest;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.github.mjeanroy.junit.servers.client.HttpHeaders.COOKIE;
import static com.github.mjeanroy.junit.servers.commons.Preconditions.notBlank;
import static java.lang.System.nanoTime;

/**
 * Implementation for {HttpRequest} that use apache http-client
 * under the hood.
 * See: http://hc.apache.org/httpcomponents-client-ga/index.html
 */
public class ApacheHttpRequest extends AbstractHttpRequest {

	private static final ApacheHttpRequestFactory FACTORY = new ApacheHttpRequestFactory();

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
	 * Map of form parameters.
	 */
	private final Map<String, String> formParams;

	/**
	 * Map of headers.
	 */
	private final Map<String, String> headers;

	/**
	 * List of cookies.
	 */
	private final List<Cookie> cookies;

	/**
	 * Request body.
	 */
	private String body;

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
		this.formParams = new HashMap<>();
		this.headers = new HashMap<>();
		this.cookies = new LinkedList<>();
	}

	@Override
	public String getUrl() {
		return url;
	}

	@Override
	public HttpMethod getMethod() {
		return httpMethod;
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
	protected HttpRequest applyQueryParam(String name, String value) {
		queryParams.put(name, value);
		return this;
	}

	@Override
	protected HttpRequest applyFormParameter(String name, String value) {
		formParams.put(name, value);
		return this;
	}

	@Override
	protected HttpRequest applyBody(String body) {
		this.body = body;
		return this;
	}

	@Override
	protected HttpRequest applyCookie(Cookie cookie) {
		this.cookies.add(cookie);
		return this;
	}

	@Override
	protected HttpResponse doExecute() throws Exception {
		HttpRequestBase httpRequest = FACTORY.create(httpMethod);

		// Create request URI with additional query params
		httpRequest.setURI(createRequestURI());

		// Add http headers
		handleHeaders(httpRequest);

		// Add http cookies
		handleCookies(httpRequest);

		// Add request body if allowed
		if (httpMethod.isBodyAllowed()) {
			HttpEntityEnclosingRequestBase rq = (HttpEntityEnclosingRequestBase) httpRequest;

			// Add form parameters or request body
			if (!formParams.isEmpty()) {
				handleFormParameters(rq);
			} else if (body != null) {
				handleRequestBody(rq);
			}
		}

		long start = nanoTime();
		org.apache.http.HttpResponse httpResponse = client.execute(httpRequest);
		return new ApacheHttpResponse(httpResponse, nanoTime() - start);
	}

	/**
	 * Create request URI.
	 * Each additional query parameters will be appended to final URI.
	 *
	 * @return Created URI.
	 * @throws URISyntaxException
	 */
	private URI createRequestURI() throws URISyntaxException {
		URIBuilder uriBuilder = new URIBuilder(url);
		for (Map.Entry<String, String> p : queryParams.entrySet()) {
			uriBuilder = uriBuilder.addParameter(p.getKey(), p.getValue());
		}

		return uriBuilder.build();
	}

	/**
	 * Add headers to http request.
	 *
	 * @param httpRequest Http request in creation.
	 */
	private void handleHeaders(HttpRequestBase httpRequest) {
		for (Map.Entry<String, String> h : headers.entrySet()) {
			httpRequest.addHeader(h.getKey(), h.getValue());
		}
	}

	/**
	 * Add parameters as form url encoded content.
	 * Each parameter is set as a key value entry to request
	 * body.
	 *
	 * @param httpRequest Http request in creation.
	 * @throws UnsupportedEncodingException
	 */
	private void handleFormParameters(HttpEntityEnclosingRequestBase httpRequest) throws UnsupportedEncodingException {
		List<NameValuePair> pairs = new ArrayList<>(formParams.size());

		for (Map.Entry<String, String> p : formParams.entrySet()) {
			NameValuePair pair = new BasicNameValuePair(p.getKey(), p.getValue());
			pairs.add(pair);
		}

		HttpEntity entity = new UrlEncodedFormEntity(pairs);
		httpRequest.setEntity(entity);
	}

	/**
	 * Set request body value to http request.
	 *
	 * @param httpRequest Http request in creation.
	 * @throws UnsupportedEncodingException
	 */
	private void handleRequestBody(HttpEntityEnclosingRequestBase httpRequest) throws UnsupportedEncodingException {
		HttpEntity entity = new StringEntity(body);
		httpRequest.setEntity(entity);
	}

	/**
	 * Set request body value to http request.
	 *
	 * @param httpRequest Http request in creation.
	 * @throws UnsupportedEncodingException
	 */
	private void handleCookies(HttpRequestBase httpRequest) throws UnsupportedEncodingException {
		if (!cookies.isEmpty()) {
			int size = cookies.size();

			// Build header value
			StringBuilder builder = new StringBuilder();
			for (int i = 0; i < size; ++i) {
				Cookie cookie = cookies.get(i);
				builder.append(cookie.raw());
				if (i != (size - 1)) {
					builder.append("; ");
				}
			}

			httpRequest.addHeader(COOKIE, builder.toString());
		}
	}

	private static class ApacheHttpRequestFactory {

		HttpRequestBase create(HttpMethod httpMethod) {
			if (httpMethod == HttpMethod.GET) {
				return new HttpGet();
			}

			if (httpMethod == HttpMethod.POST) {
				return new HttpPost();
			}

			if (httpMethod == HttpMethod.PUT) {
				return new HttpPut();
			}

			if (httpMethod == HttpMethod.DELETE) {
				return new HttpDelete();
			}

			throw new UnsupportedOperationException("Method " + httpMethod + " is not supported by apache http-client");
		}
	}
}
