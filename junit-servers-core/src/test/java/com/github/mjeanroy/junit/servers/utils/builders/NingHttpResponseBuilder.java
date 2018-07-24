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

package com.github.mjeanroy.junit.servers.utils.builders;

import com.ning.http.client.AsyncHttpClientConfig;
import com.ning.http.client.AsyncHttpProvider;
import com.ning.http.client.HttpResponseBodyPart;
import com.ning.http.client.HttpResponseHeaders;
import com.ning.http.client.HttpResponseStatus;
import com.ning.http.client.Response;
import com.ning.http.client.providers.jdk.JDKAsyncHttpProvider;
import com.ning.http.client.providers.jdk.JDKResponse;
import com.ning.http.client.providers.jdk.ResponseBodyPart;
import com.ning.http.client.providers.jdk.ResponseHeaders;
import com.ning.http.client.providers.jdk.ResponseStatus;
import com.ning.http.client.uri.Uri;

import java.net.HttpURLConnection;
import java.util.LinkedHashMap;
import java.util.List;

import static java.nio.charset.Charset.defaultCharset;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Builder for {@link Response} instances.
 */
public class NingHttpResponseBuilder extends AbstractHttpResponseBuilder<Response, NingHttpResponseBuilder> {

	@Override
	public Response build() {
		Uri uri = Uri.create("http://localhost:8080/");

		AsyncHttpClientConfig config = new AsyncHttpClientConfig.Builder().build();
		AsyncHttpProvider provider = new JDKAsyncHttpProvider(config);
		HttpURLConnection conn = createFakeUrlConnection();
		HttpResponseStatus status = new ResponseStatus(uri, config, conn);
		HttpResponseHeaders headers = new ResponseHeaders(uri, conn, provider);

		final List<HttpResponseBodyPart> bodyParts;
		if (body != null) {
			byte[] bodyBytes = body.getBytes(defaultCharset());
			HttpResponseBodyPart part = new ResponseBodyPart(bodyBytes, true);
			bodyParts = singletonList(part);
		} else {
			bodyParts = emptyList();
		}

		return new JDKResponse(status, headers, bodyParts);
	}

	private HttpURLConnection createFakeUrlConnection() {
		try {
			HttpURLConnection conn = mock(HttpURLConnection.class);

			when(conn.getHeaderFields()).thenReturn(new LinkedHashMap<>(headers));
			when(conn.getResponseCode()).thenReturn(status);

			return conn;
		} catch (Exception ex) {
			throw new AssertionError(ex);
		}
	}
}
