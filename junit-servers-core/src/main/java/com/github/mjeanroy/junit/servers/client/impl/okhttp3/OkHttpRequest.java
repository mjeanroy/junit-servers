/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2022 <mickael.jeanroy@gmail.com>
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

package com.github.mjeanroy.junit.servers.client.impl.okhttp3;

import com.github.mjeanroy.junit.servers.client.Cookies;
import com.github.mjeanroy.junit.servers.client.HttpHeader;
import com.github.mjeanroy.junit.servers.client.HttpHeaders;
import com.github.mjeanroy.junit.servers.client.HttpMethod;
import com.github.mjeanroy.junit.servers.client.HttpParameter;
import com.github.mjeanroy.junit.servers.client.HttpRequest;
import com.github.mjeanroy.junit.servers.client.HttpResponse;
import com.github.mjeanroy.junit.servers.client.HttpUrl;
import com.github.mjeanroy.junit.servers.client.impl.AbstractHttpRequest;
import com.github.mjeanroy.junit.servers.loggers.Logger;
import com.github.mjeanroy.junit.servers.loggers.LoggerFactory;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;

/**
 * Implementation of {@link HttpRequest} using OkHttp library.
 *
 * @see com.github.mjeanroy.junit.servers.client.HttpClientStrategy#OK_HTTP3
 * @see <a href="http://square.github.io/okhttp">http://square.github.io/okhttp</a>
 */
class OkHttpRequest extends AbstractHttpRequest {

	/**
	 * Class Logger.
	 */
	private static final Logger log = LoggerFactory.getLogger(OkHttpRequest.class);

	/**
	 * The native OkHttp client.
	 */
	private final okhttp3.OkHttpClient client;

	/**
	 * Create apache http request.
	 *
	 * @param client Apache http client.
	 * @param httpMethod Http method.
	 * @param endpoint Http request url.
	 */
	OkHttpRequest(okhttp3.OkHttpClient client, HttpMethod httpMethod, HttpUrl endpoint) {
		super(endpoint, httpMethod);
		this.client = client;
	}

	@Override
	protected HttpResponse doExecute() throws Exception {
		final HttpUrl endpoint = getEndpoint();
		final okhttp3.HttpUrl.Builder httpUrlBuilder = new okhttp3.HttpUrl.Builder()
			.scheme(endpoint.getScheme())
			.host(endpoint.getHost())
			.port(endpoint.getPort())
			.addPathSegments(endpoint.getPath().substring(1));

		// Append all query parameters.
		for (HttpParameter queryParam : queryParams.values()) {
			httpUrlBuilder.addEncodedQueryParameter(queryParam.getEncodedName(), queryParam.getEncodedValue());
		}

		final Request.Builder builder = new Request.Builder().url(httpUrlBuilder.build());
		handleBody(builder);
		handleCookies(builder);
		handleHeaders(builder);

		final Call call = client.newCall(builder.build());

		final long start = System.nanoTime();
		try (Response response = call.execute()) {
			final long duration = System.nanoTime() - start;
			return OkHttpResponseFactory.of(response, duration);
		}
	}

	/**
	 * Add all HTTP headers to the final request.
	 *
	 * @param builder The OkHttp request builder.
	 * @see Request.Builder#addHeader(String, String)
	 */
	private void handleHeaders(Request.Builder builder) {
		for (HttpHeader h : headers.values()) {
			builder.header(h.getName(), h.serializeValues());
		}
	}

	/**
	 * Add all cookies to the final request (i.e add the {@code Cookie} header).
	 *
	 * @param builder The OkHttp request builder.
	 * @see Request.Builder#addHeader(String, String)
	 */
	private void handleCookies(Request.Builder builder) {
		if (!cookies.isEmpty()) {
			builder.addHeader(HttpHeaders.COOKIE, Cookies.serialize(cookies));
		}
	}

	/**
	 * Add request body if appropriate.
	 *
	 * @param builder The native OkHttp request builder.
	 * @see Request.Builder#method(String, RequestBody)
	 * @see RequestBody
	 */
	private void handleBody(Request.Builder builder) throws IOException {
		log.debug("Adding request body");

		RequestBody okhttpRequestBody = hasBody() ? createBody() : null;

		log.debug("Created body: {}", okhttpRequestBody);

		// Force an empty body, as POST & PUT methods requires
		// a body element.
		HttpMethod method = getMethod();
		if (okhttpRequestBody == null && method.isBodyAllowed()) {
			log.debug("Request method {} requires a body, but no one found, adding empty request body)", method);
			okhttpRequestBody = createEmptyBody();
		}

		builder.method(method.getVerb(), okhttpRequestBody);

		if (body != null && body.getContentType() != null) {
			builder.header("Content-Type", body.getContentType());
		}
	}

	/**
	 * Create the OkHttp request body.
	 *
	 * @return OkHttp {@link RequestBody} instance.
	 * @see RequestBody#create(MediaType, String)
	 * @see FormBody
	 */
	@SuppressWarnings("deprecation")
	private RequestBody createBody() throws IOException {
		if (body == null) {
			return null;
		}

		log.debug("Creating OkHTTP request body from: {}", body);
		String rawContentType = body.getContentType();
		MediaType mediaType = rawContentType == null ? null : MediaType.parse(rawContentType);
		return RequestBody.create(mediaType, body.getBody());
	}

	/**
	 * Create a new empty request body.
	 *
	 * @return Request body element.
	 * @see RequestBody
	 */
	@SuppressWarnings("deprecation")
	private static RequestBody createEmptyBody() {
		return RequestBody.create(null, "");
	}
}
