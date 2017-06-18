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

package com.github.mjeanroy.junit.servers.client.impl.ning_async_http_client;

import com.github.mjeanroy.junit.servers.client.BaseHttpRequestTest;
import com.github.mjeanroy.junit.servers.client.Cookie;
import com.github.mjeanroy.junit.servers.client.HttpMethod;
import com.github.mjeanroy.junit.servers.client.HttpRequest;
import com.github.mjeanroy.junit.servers.client.HttpResponse;
import com.github.mjeanroy.junit.servers.utils.Pair;
import com.ning.http.client.ListenableFuture;
import com.ning.http.client.Param;
import com.ning.http.client.Request;
import com.ning.http.client.RequestBuilder;
import com.ning.http.client.Response;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.apache.commons.lang3.reflect.FieldUtils.readField;
import static org.apache.commons.lang3.reflect.FieldUtils.writeField;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class NingAsyncHttpRequestTest extends BaseHttpRequestTest {

	private com.ning.http.client.AsyncHttpClient client;

	private Response response;

	@Override
	protected void onSetUp() throws Exception {
		client = mock(com.ning.http.client.AsyncHttpClient.class);
		response = mock(Response.class);
	}

	@Override
	protected HttpRequest createHttpRequest(HttpMethod httpMethod, String url) throws Exception {
		return new NingAsyncHttpRequest(client, httpMethod, url);
	}

	@Override
	protected void checkInternals(HttpRequest request, HttpMethod httpMethod, String url) throws Exception {
		// Check internal client
		com.ning.http.client.AsyncHttpClient ningClient = (com.ning.http.client.AsyncHttpClient) readField(request, "client", true);
		assertThat(ningClient).isSameAs(client);

		RequestBuilder builder = (RequestBuilder) readField(request, "builder", true);
		assertThat(builder).isNotNull();

		Request rq = builder.build();
		assertThat(rq.getUrl()).isEqualTo(url);
		assertThat(rq.getMethod()).isEqualTo(httpMethod.getVerb());

		Map<String, List<String>> headers = rq.getHeaders();
		assertThat(headers).isEmpty();

		List<Param> queryParams = rq.getQueryParams();
		assertThat(queryParams).isEmpty();
	}

	@Override
	protected HttpRequest createDefaultRequest() throws Exception {
		String url = "http://localhost:8080/foo";
		HttpMethod httpMethod = HttpMethod.POST;
		NingAsyncHttpRequest httpRequest = new NingAsyncHttpRequest(client, httpMethod, url);

		// Use a spy on request builder
		RequestBuilder builder = spy((RequestBuilder) readField(httpRequest, "builder", true));
		writeField(httpRequest, "builder", builder, true);

		return httpRequest;
	}

	@Override
	protected void checkQueryParam(HttpRequest httpRequest, String name, String value) throws Exception {
		RequestBuilder builder = (RequestBuilder) readField(httpRequest, "builder", true);
		verify(builder).addQueryParam(name, value);

		Request rq = builder.build();
		assertThat(rq.getQueryParams()).contains(new Param(name, value));
	}

	@Override
	protected void checkHeader(HttpRequest httpRequest, String name, String value) throws Exception {
		RequestBuilder builder = (RequestBuilder) readField(httpRequest, "builder", true);
		verify(builder).addHeader(name, value);

		Request rq = builder.build();
		checkHeader(rq, name, value);
	}

	@Override
	protected void checkFormParam(HttpRequest httpRequest, String name, String value) throws Exception {
		RequestBuilder builder = (RequestBuilder) readField(httpRequest, "builder", true);
		verify(builder).addFormParam(name, value);

		// Check produced request
		Request rq = builder.build();
		assertThat(rq.getFormParams()).contains(new Param(name, value));

		// Check that content-type is automatically set form-urlencoded
		checkHeader(httpRequest, "Content-Type", "application/x-www-form-urlencoded");
	}

	@Override
	protected void checkRequestBody(HttpRequest httpRequest, String body) throws Exception {
		RequestBuilder builder = (RequestBuilder) readField(httpRequest, "builder", true);
		verify(builder).setBody(body);
	}

	@Override
	protected void checkCookie(HttpRequest httpRequest, Cookie cookie) throws Exception {
		RequestBuilder builder = (RequestBuilder) readField(httpRequest, "builder", true);

		ArgumentCaptor<com.ning.http.client.cookie.Cookie> cookieCaptor = ArgumentCaptor.forClass(com.ning.http.client.cookie.Cookie.class);
		verify(builder).addCookie(cookieCaptor.capture());

		com.ning.http.client.cookie.Cookie c = cookieCaptor.getValue();
		assertThat(c.getName()).isEqualTo(cookie.getName());
		assertThat(c.getValue()).isEqualTo(cookie.getValue());
		assertThat(c.getDomain()).isEqualTo(cookie.getDomain());
		assertThat(c.getPath()).isEqualTo(cookie.getPath());
		assertThat(c.isSecure()).isEqualTo(cookie.isSecure());
		assertThat(c.isHttpOnly()).isEqualTo(cookie.isHttpOnly());
		assertThat(c.getMaxAge()).isEqualTo(Long.valueOf(cookie.getMaxAge()));

		// Check produced request
		Request rq = builder.build();
		assertThat(rq.getCookies())
				.isNotNull()
				.isNotEmpty()
				.contains(c);
	}

	@Override
	protected HttpResponse fakeExecution(HttpRequest httpRequest, ExecutionStrategy executionStrategy) throws Exception {
		@SuppressWarnings("unchecked")
		ListenableFuture<Response> future = mock(ListenableFuture.class);

		when(future.get()).thenReturn(response);
		when(client.executeRequest(any(Request.class))).thenReturn(future);
		return executionStrategy.execute(httpRequest);
	}

	@Override
	protected void checkExecution(HttpResponse httpResponse, Pair... headers) throws Exception {
		Response internalRsp = (Response) readField(httpResponse, "response", true);
		assertThat(internalRsp).isSameAs(response);

		ArgumentCaptor<Request> requestCaptor = ArgumentCaptor.forClass(Request.class);
		verify(client).executeRequest(requestCaptor.capture());

		if (headers != null) {
			Request request = requestCaptor.getValue();
			for (Pair h : headers) {
				checkHeader(request, h.getO1(), h.getO2());
			}
		}
	}

	private void checkHeader(Request rq, String name, String value) {
		Map<String, List<String>> headers = rq.getHeaders();
		assertThat(headers)
				.isNotNull()
				.isNotEmpty()
				.containsEntry(name, asList(value));
	}
}
