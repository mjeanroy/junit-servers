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

package com.github.mjeanroy.junit.servers.client.impl.okhttp3;

import static com.github.mjeanroy.junit.servers.commons.ObjectUtils.firstNonNull;

import com.github.mjeanroy.junit.servers.client.Cookies;
import com.github.mjeanroy.junit.servers.client.HttpHeader;
import com.github.mjeanroy.junit.servers.client.HttpHeaders;
import com.github.mjeanroy.junit.servers.client.HttpMethod;
import com.github.mjeanroy.junit.servers.client.HttpParameter;
import com.github.mjeanroy.junit.servers.client.HttpRequest;
import com.github.mjeanroy.junit.servers.client.HttpResponse;
import com.github.mjeanroy.junit.servers.client.HttpUrl;
import com.github.mjeanroy.junit.servers.client.impl.AbstractHttpRequest;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Implementation of {@link HttpRequest} using OkHttp library.
 *
 * @see com.github.mjeanroy.junit.servers.client.HttpClientStrategy#OK_HTTP3
 * @see <a href="http://square.github.io/okhttp">http://square.github.io/okhttp</a>
 */
class OkHttpRequest extends AbstractHttpRequest implements HttpRequest {

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
		handleCookies(builder);
		handleHeaders(builder);
		handleBody(builder);

		final Call call = client.newCall(builder.build());

		final long start = System.nanoTime();
		final Response response = call.execute();
		final long duration = System.nanoTime() - start;

		return OkHttpResponseFactory.of(response, duration);
	}

	/**
	 * Add all HTTP headers to the final request.
	 *
	 * @param builder The OkHttp request builder.
	 * @see Request.Builder#addHeader(String, String)
	 */
	private void handleHeaders(Request.Builder builder) {
		for (HttpHeader h : headers.values()) {
			builder.addHeader(h.getName(), h.serializeValues());
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
	 * Add request body if appropriate:
	 * <ul>
	 *   <li>Add request body if {@link #body} is defined.</li>
	 *   <li>Add form parameters ({@link #formParams} otherwise if it is not empty.</li>
	 * </ul>
	 *
	 * @param builder The native OkHttp request builder.
	 * @see Request.Builder#method(String, RequestBody)
	 * @see RequestBody
	 */
	private void handleBody(Request.Builder builder) {
		HttpMethod method = getMethod();
		RequestBody body = hasBody() ? createBody() : null;

		// Force an empty body, as POST & PUT methods requires
		// a body element.
		if (body == null && method.isBodyAllowed()) {
			body = createEmptyBody();
		}

		builder.method(method.getVerb(), body);
	}

	/**
	 * Create the OkHttp request body:
	 * <ul>
	 *   <li>Create body from {@link #body} value if it is defined.</li>
	 *   <li>Create {@link FormBody} from {@link #formParams} otherwise if it is not empty.</li>
	 *   <li>Returns {@code null} otherwise.</li>
	 * </ul>
	 *
	 * @return OkHttp {@link RequestBody} instance.
	 * @see RequestBody#create(MediaType, String)
	 * @see FormBody
	 */
	private RequestBody createBody() {
		RequestBody rqBody = null;

		// Try request body first.
		if (body != null && !body.isEmpty()) {
			rqBody = RequestBody.create(null, body);
		}

		else if (!formParams.isEmpty()) {
			FormBody.Builder builder = new FormBody.Builder();
			for (HttpParameter parameter : formParams.values()) {
				String encodedName = parameter.getEncodedName();
				String encodedValue = firstNonNull(parameter.getEncodedValue(), "");
				builder.addEncoded(encodedName, encodedValue);
			}

			rqBody = builder.build();
		}

		return rqBody;
	}

	/**
	 * Create a new empty request body.
	 *
	 * @return Request body element.
	 * @see RequestBody
	 */
	private static RequestBody createEmptyBody() {
		return RequestBody.create(null, "");
	}
}
