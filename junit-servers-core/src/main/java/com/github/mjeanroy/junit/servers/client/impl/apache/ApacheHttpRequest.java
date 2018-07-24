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

package com.github.mjeanroy.junit.servers.client.impl.apache;

import static com.github.mjeanroy.junit.servers.client.HttpHeaders.COOKIE;
import static com.github.mjeanroy.junit.servers.commons.CollectionUtils.map;
import static java.lang.System.nanoTime;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;

import com.github.mjeanroy.junit.servers.client.Cookies;
import com.github.mjeanroy.junit.servers.client.HttpHeader;
import com.github.mjeanroy.junit.servers.client.HttpMethod;
import com.github.mjeanroy.junit.servers.client.HttpParameter;
import com.github.mjeanroy.junit.servers.client.HttpRequest;
import com.github.mjeanroy.junit.servers.client.HttpResponse;
import com.github.mjeanroy.junit.servers.client.HttpUrl;
import com.github.mjeanroy.junit.servers.client.impl.AbstractHttpRequest;
import com.github.mjeanroy.junit.servers.commons.Mapper;

/**
 * Implementation for {@link HttpRequest} that use apache http-client
 * under the hood.
 *
 * @see <a href="http://hc.apache.org/httpcomponents-client-ga/index.html">http://hc.apache.org/httpcomponents-client-ga/index.html</a>
 * @see com.github.mjeanroy.junit.servers.client.HttpClientStrategy#APACHE_HTTP_CLIENT
 */
class ApacheHttpRequest extends AbstractHttpRequest implements HttpRequest {

	private static final ApacheHttpRequestFactory FACTORY = new ApacheHttpRequestFactory();

	/**
	 * Original http client, will be used to execute http request.
	 */
	private final HttpClient client;

	/**
	 * Create apache http request.
	 *
	 * @param client Apache http client.
	 * @param httpMethod Http method.
	 * @param endpoint Http request url.
	 */
	ApacheHttpRequest(HttpClient client, HttpMethod httpMethod, HttpUrl endpoint) {
		super(endpoint, httpMethod);
		this.client = client;
	}

	@Override
	protected HttpResponse doExecute() throws Exception {
		final HttpMethod method = getMethod();

		final HttpRequestBase httpRequest = FACTORY.create(method);

		httpRequest.setURI(createRequestURI());
		handleHeaders(httpRequest);
		handleCookies(httpRequest);
		handleBody(httpRequest);

		final long start = nanoTime();
		final org.apache.http.HttpResponse httpResponse = client.execute(httpRequest);
		final long duration = nanoTime() - start;

		return ApacheHttpResponseFactory.of(httpResponse, duration);
	}

	/**
	 * Add request body.
	 *
	 * @param httpRequest The HTTP request.
	 */
	private void handleBody(HttpRequestBase httpRequest) {
		if (hasBody()) {
			HttpEntityEnclosingRequestBase rq = (HttpEntityEnclosingRequestBase) httpRequest;
			if (!formParams.isEmpty()) {
				handleFormParameters(rq);
			} else if (body != null) {
				handleRequestBody(rq);
			}
		}
	}

	/**
	 * Create request URI.
	 * Each additional query parameters will be appended to final URI.
	 *
	 * @return Created URI.
	 * @throws URISyntaxException If an error occurred while building URI.
	 * @see URIBuilder
	 */
	private URI createRequestURI() throws URISyntaxException {
		URI uri = getEndpoint().toURI();
		URIBuilder builder = new URIBuilder(uri).setCharset(StandardCharsets.UTF_8);
		for (HttpParameter parameter : queryParams.values()) {
			builder.addParameter(parameter.getName(), parameter.getValue());
		}

		return builder.build();
	}

	/**
	 * Add headers to http request.
	 *
	 * @param httpRequest Http request in creation.
	 * @see org.apache.http.HttpRequest#addHeader(Header)
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
	 */
	private void handleFormParameters(HttpEntityEnclosingRequestBase httpRequest) {
		List<NameValuePair> pairs = map(formParams.values(), PARAM_MAPPER);
		HttpEntity entity = new UrlEncodedFormEntity(pairs, Charset.defaultCharset());
		httpRequest.setEntity(entity);
	}

	/**
	 * Set request body value to http request.
	 *
	 * @param httpRequest Http request in creation.
	 */
	private void handleRequestBody(HttpEntityEnclosingRequestBase httpRequest) {
		HttpEntity entity = new StringEntity(body, Charset.defaultCharset());
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

			if (httpMethod == HttpMethod.PATCH) {
				return new HttpPatch();
			}

			if (httpMethod == HttpMethod.HEAD) {
				return new HttpHead();
			}

			throw new UnsupportedOperationException("Method " + httpMethod + " is not supported by apache http-client");
		}
	}

	private static final Mapper<HttpParameter, NameValuePair> PARAM_MAPPER = new Mapper<HttpParameter, NameValuePair>() {
		@Override
		public NameValuePair apply(HttpParameter parameter) {
			return new BasicNameValuePair(parameter.getName(), parameter.getValue());
		}
	};
}
