/**
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

package com.github.mjeanroy.junit.servers.client.impl.apache;

import com.github.mjeanroy.junit.servers.client.Cookies;
import com.github.mjeanroy.junit.servers.client.HttpHeader;
import com.github.mjeanroy.junit.servers.client.HttpMethod;
import com.github.mjeanroy.junit.servers.client.HttpParameter;
import com.github.mjeanroy.junit.servers.client.HttpRequest;
import com.github.mjeanroy.junit.servers.client.HttpResponse;
import com.github.mjeanroy.junit.servers.client.HttpUrl;
import com.github.mjeanroy.junit.servers.client.impl.AbstractHttpRequest;
import com.github.mjeanroy.junit.servers.loggers.Logger;
import com.github.mjeanroy.junit.servers.loggers.LoggerFactory;
import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

import static com.github.mjeanroy.junit.servers.client.HttpHeaders.COOKIE;
import static java.lang.System.nanoTime;

/**
 * Implementation for {@link HttpRequest} that use apache http-client
 * under the hood.
 *
 * @see <a href="http://hc.apache.org/httpcomponents-client-ga/index.html">http://hc.apache.org/httpcomponents-client-ga/index.html</a>
 * @see com.github.mjeanroy.junit.servers.client.HttpClientStrategy#APACHE_HTTP_CLIENT
 */
class ApacheHttpRequest extends AbstractHttpRequest {

	/**
	 * Class Logger.
	 */
	private static final Logger log = LoggerFactory.getLogger(ApacheHttpRequest.class);

	/**
	 * A factory that creates {@link HttpRequestBase} from given {@link HttpMethod}.
	 */
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
		handleBody(httpRequest);
		handleHeaders(httpRequest);
		handleCookies(httpRequest);

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
	private void handleBody(HttpRequestBase httpRequest) throws IOException {
		if (!hasBody()) {
			log.debug("HTTP Request does not have body, skip.");
			return;
		}

		log.debug("Set HTTP Entity from given body: {}", body);

		ContentType contentType = ContentType.getByMimeType(body.getContentType());
		ByteArrayEntity entity = new ByteArrayEntity(body.getBody(), contentType);

		log.debug("Found contentType={} and entity={}", contentType, entity);
		((HttpEntityEnclosingRequestBase) httpRequest).setEntity(entity);

		if (body.getContentType() != null) {
			httpRequest.setHeader("Content-Type", body.getContentType());
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
			httpRequest.setHeader(header.getName(), header.serializeValues());
		}
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
				return new HttpDeleteEntityEnclosingRequest();
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

	private static final class HttpDeleteEntityEnclosingRequest extends HttpEntityEnclosingRequestBase {
		public HttpDeleteEntityEnclosingRequest() {
			super();
		}

		@Override
		public String getMethod() {
			return HttpDelete.METHOD_NAME;
		}
	}
}
