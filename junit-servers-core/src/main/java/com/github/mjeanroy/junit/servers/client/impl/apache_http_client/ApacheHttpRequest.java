/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2017 <mickael.jeanroy@gmail.com>
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

import com.github.mjeanroy.junit.servers.client.Cookies;
import com.github.mjeanroy.junit.servers.client.HttpHeader;
import com.github.mjeanroy.junit.servers.client.HttpMethod;
import com.github.mjeanroy.junit.servers.client.HttpParameter;
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
import java.util.List;

import static com.github.mjeanroy.junit.servers.client.HttpHeaders.COOKIE;
import static java.lang.System.nanoTime;

/**
 * Implementation for {HttpRequest} that use apache http-client
 * under the hood.
 * See: http://hc.apache.org/httpcomponents-client-ga/index.html
 */
class ApacheHttpRequest extends AbstractHttpRequest {

	private static final ApacheHttpRequestFactory FACTORY = new ApacheHttpRequestFactory();

	/**
	 * Original http client.
	 * It will be used to execute http request.
	 */
	private final HttpClient client;

	/**
	 * Create apache http request.
	 *
	 * @param client Apache http client.
	 * @param httpMethod Http method.
	 * @param url Http request url.
	 */
	ApacheHttpRequest(HttpClient client, HttpMethod httpMethod, String url) {
		super(url, httpMethod);
		this.client = client;
	}

	@Override
	protected HttpResponse doExecute() throws Exception {
		HttpMethod method = getMethod();
		HttpRequestBase httpRequest = FACTORY.create(method);

		// Create request URI with additional query params
		httpRequest.setURI(createRequestURI());

		// Add http headers
		handleHeaders(httpRequest);

		// Add http cookies
		handleCookies(httpRequest);

		// Add request body if allowed
		if (hasBody()) {
			HttpEntityEnclosingRequestBase rq = (HttpEntityEnclosingRequestBase) httpRequest;

			// Add form parameters or request body.
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
	 * @throws URISyntaxException If an error occurred while building URI.
	 */
	private URI createRequestURI() throws URISyntaxException {
		String url = getUrl();
		URIBuilder uriBuilder = new URIBuilder(url);
		for (HttpParameter p : queryParams.values()) {
			uriBuilder = uriBuilder.addParameter(p.getName(), p.getValue());
		}

		return uriBuilder.build();
	}

	/**
	 * Add headers to http request.
	 *
	 * @param httpRequest Http request in creation.
	 */
	private void handleHeaders(HttpRequestBase httpRequest) {
		for (HttpHeader header : headers.values()) {
			httpRequest.addHeader(header.getName(), header.serializeValues());
		}
	}

	/**
	 * Add parameters as form url encoded content.
	 * Each parameter is set as a key value entry to request
	 * body.
	 *
	 * @param httpRequest Http request in creation.
	 * @throws UnsupportedEncodingException If an encoding error occurred while create body request.
	 */
	private void handleFormParameters(HttpEntityEnclosingRequestBase httpRequest) throws UnsupportedEncodingException {
		List<NameValuePair> pairs = new ArrayList<>(formParams.size());

		for (HttpParameter p : formParams.values()) {
			NameValuePair pair = new BasicNameValuePair(p.getName(), p.getValue());
			pairs.add(pair);
		}

		HttpEntity entity = new UrlEncodedFormEntity(pairs);
		httpRequest.setEntity(entity);
	}

	/**
	 * Set request body value to http request.
	 *
	 * @param httpRequest Http request in creation.
	 * @throws UnsupportedEncodingException If an encoding error occurred while creating request body.
	 */
	private void handleRequestBody(HttpEntityEnclosingRequestBase httpRequest) throws UnsupportedEncodingException {
		HttpEntity entity = new StringEntity(body);
		httpRequest.setEntity(entity);
	}

	/**
	 * Add cookies to http request.
	 *
	 * @param httpRequest Http request in creation.
	 */
	private void handleCookies(HttpRequestBase httpRequest) {
		if (!cookies.isEmpty()) {
			httpRequest.addHeader(COOKIE, Cookies.serialize(cookies));
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
