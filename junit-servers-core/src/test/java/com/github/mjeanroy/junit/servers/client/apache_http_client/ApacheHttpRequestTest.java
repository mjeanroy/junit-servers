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

import static org.apache.commons.lang3.reflect.FieldUtils.readField;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

import java.util.Map;

import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.assertj.core.api.Condition;
import org.mockito.ArgumentCaptor;

import com.github.mjeanroy.junit.servers.client.BaseHttpRequestTest;
import com.github.mjeanroy.junit.servers.client.HttpMethod;
import com.github.mjeanroy.junit.servers.client.HttpRequest;
import com.github.mjeanroy.junit.servers.client.HttpResponse;

public class ApacheHttpRequestTest extends BaseHttpRequestTest {

	private CloseableHttpClient client;
	private CloseableHttpResponse response;

	@Override
	protected void onSetUp() throws Exception {
		client = mock(CloseableHttpClient.class);
		response = mock(CloseableHttpResponse.class);
	}

	@Override
	protected HttpRequest createHttpRequest(HttpMethod httpMethod, String url) throws Exception {
		return new ApacheHttpRequest(client, httpMethod, url);
	}

	@Override
	protected void checkInternals(HttpRequest request, HttpMethod httpMethod, String url) throws Exception {
		CloseableHttpClient ningClient = (CloseableHttpClient) readField(request, "client", true);
		assertThat(ningClient)
				.isNotNull()
				.isSameAs(client);

		HttpMethod requestMethod = (HttpMethod) readField(request, "httpMethod", true);
		assertThat(requestMethod)
				.isNotNull()
				.isEqualTo(httpMethod);

		String requestUrl = (String) readField(request, "url", true);
		assertThat(requestUrl)
				.isNotNull()
				.isEqualTo(url);

		@SuppressWarnings("unchecked")
		Map<String, String> headers = (Map<String, String>) readField(request, "headers", true);
		assertThat(headers)
				.isNotNull()
				.isEmpty();

		@SuppressWarnings("unchecked")
		Map<String, String> queryParams = (Map<String, String>) readField(request, "queryParams", true);
		assertThat(queryParams)
				.isNotNull()
				.isEmpty();
	}

	@Override
	protected HttpRequest createDefaultRequest() {
		String url = "http://localhost:8080/foo";
		HttpMethod httpMethod = HttpMethod.POST;
		return new ApacheHttpRequest(client, httpMethod, url);
	}

	@Override
	protected HttpResponse fakeExecution(HttpRequest httpRequest, ExecutionStrategy executionStrategy) throws Exception {
		when(client.execute(any(HttpUriRequest.class))).thenReturn(response);
		return executionStrategy.execute(httpRequest);
	}

	@Override
	protected void checkExecution(HttpResponse httpResponse, HeaderEntry... headers) throws Exception {
		CloseableHttpResponse internalRsp = (CloseableHttpResponse) readField(httpResponse, "response", true);
		assertThat(internalRsp)
				.isNotNull()
				.isSameAs(response);

		ArgumentCaptor<HttpUriRequest> requestCaptor = ArgumentCaptor.forClass(HttpUriRequest.class);
		verify(client).execute(requestCaptor.capture());

		if (headers != null) {
			HttpUriRequest request = requestCaptor.getValue();
			for (HeaderEntry h : headers) {
				checkHeader(request, h.name, h.value);
			}
		}
	}

	@Override
	protected void checkQueryParam(HttpRequest httpRequest, String name, String value) throws Exception {
		@SuppressWarnings("unchecked")
		Map<String, String> queryParams = (Map<String, String>) readField(httpRequest, "queryParams", true);
		assertThat(queryParams)
				.isNotNull()
				.isNotEmpty()
				.contains(entry(name, value));
	}

	@Override
	protected void checkHeader(HttpRequest httpRequest, String name, String value) throws Exception {
		@SuppressWarnings("unchecked")
		Map<String, String> headers = (Map<String, String>) readField(httpRequest, "headers", true);
		assertThat(headers)
				.isNotNull()
				.isNotEmpty()
				.contains(entry(name, value));
	}

	@Override
	protected void checkFormParam(HttpRequest httpRequest, String name, String value) throws Exception {
		@SuppressWarnings("unchecked")
		Map<String, String> formParams = (Map<String, String>) readField(httpRequest, "formParams", true);
		assertThat(formParams)
				.isNotNull()
				.isNotEmpty()
				.contains(entry(name, value));

		checkHeader(httpRequest, "Content-Type", "application/x-www-form-urlencoded");
	}

	private void checkHeader(HttpUriRequest rq, final String name, final String value) {
		Header[] headers = rq.getHeaders(name);
		assertThat(headers)
				.isNotNull()
				.isNotEmpty()
				.areAtLeast(1, new Condition<Header>() {
					@Override
					public boolean matches(Header header) {
						return header.getValue().equals(value);
					}
				});
	}
}
