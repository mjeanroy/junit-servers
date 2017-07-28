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

package com.github.mjeanroy.junit.servers.client.impl.okhttp;

import static com.github.mjeanroy.junit.servers.commons.EncoderUtils.urlEncode;

import com.github.mjeanroy.junit.servers.client.Cookies;
import com.github.mjeanroy.junit.servers.client.HttpHeader;
import com.github.mjeanroy.junit.servers.client.HttpHeaders;
import com.github.mjeanroy.junit.servers.client.HttpMethod;
import com.github.mjeanroy.junit.servers.client.HttpParameter;
import com.github.mjeanroy.junit.servers.client.HttpRequest;
import com.github.mjeanroy.junit.servers.client.HttpResponse;
import com.github.mjeanroy.junit.servers.client.impl.AbstractHttpRequest;
import com.github.mjeanroy.junit.servers.exceptions.HttpClientUrlException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Implementation of {@link HttpRequest} using OkHttp library.
 *
 * @see com.github.mjeanroy.junit.servers.client.HttpClientStrategy#OK_HTTP
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
	 * @param url Http request url.
	 */
	OkHttpRequest(okhttp3.OkHttpClient client, HttpMethod httpMethod, String url) {
		super(url, httpMethod);
		this.client = client;
	}

	@Override
	protected HttpResponse doExecute() throws Exception {
		String url = getUrl();
		HttpUrl baseUrl = HttpUrl.parse(url);
		if (baseUrl == null) {
			throw new HttpClientUrlException(url);
		}

		// Append all query parameters.
		HttpUrl.Builder httpUrlBuilder = baseUrl.newBuilder();
		for (HttpParameter queryParam : queryParams.values()) {
			httpUrlBuilder.addQueryParameter(queryParam.getName(), queryParam.getValue());
		}

		Request.Builder builder = new Request.Builder().url(httpUrlBuilder.build());
		handleCookies(builder);
		handleHeaders(builder);
		handleBody(builder);

		Call call = client.newCall(builder.build());

		long start = System.nanoTime();
		Response response = call.execute();
		long duration = System.nanoTime() - start;

		return new OkHttpResponse(response, duration);
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
				// Use the JDK url encoding
				String encodedName = urlEncode(parameter.getName());
				String encodedValue = urlEncode(parameter.getValue());
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
