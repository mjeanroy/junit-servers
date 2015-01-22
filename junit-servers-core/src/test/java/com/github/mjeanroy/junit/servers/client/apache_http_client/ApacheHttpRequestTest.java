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

import java.io.StringWriter;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
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
		CloseableHttpClient apacheClient = extract(request, "client");
		assertThat(apacheClient).isSameAs(client);

		HttpMethod requestMethod = (HttpMethod) readField(request, "httpMethod", true);
		assertThat(requestMethod).isEqualTo(httpMethod);

		String requestUrl = (String) readField(request, "url", true);
		assertThat(requestUrl).isEqualTo(url);

		Map<String, String> headers = extract(request, "headers");
		assertThat(headers).isEmpty();

		Map<String, String> queryParams = extract(request, "formParams");
		assertThat(queryParams).isEmpty();
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
		CloseableHttpResponse internalRsp = extract(httpResponse, "response");
		assertThat(internalRsp).isSameAs(response);

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
		Map<String, String> queryParams = extract(httpRequest, "queryParams");
		assertThat(queryParams).contains(entry(name, value));

		reset(client);
		httpRequest.execute();

		ArgumentCaptor<HttpRequestBase> rqCaptor = ArgumentCaptor.forClass(HttpRequestBase.class);
		verify(client).execute(rqCaptor.capture());

		HttpRequestBase rq = rqCaptor.getValue();

		String uri = rq.getURI().toString();
		assertThat(uri)
				.matches("(.*)\\?(([a-z0-9]*)=(([a-z0-9]*))(&?))+")
				.contains(name + "=" + value);
	}

	@Override
	protected void checkHeader(HttpRequest httpRequest, String name, String value) throws Exception {
		Map<String, String> headers = extract(httpRequest, "headers");
		assertThat(headers).contains(entry(name, value));

		reset(client);
		httpRequest.execute();

		ArgumentCaptor<HttpRequestBase> rqCaptor = ArgumentCaptor.forClass(HttpRequestBase.class);
		verify(client).execute(rqCaptor.capture());

		HttpRequestBase rq = rqCaptor.getValue();
		assertThat(rq.getFirstHeader(name).getValue()).isEqualTo(value);
	}

	@Override
	protected void checkFormParam(HttpRequest httpRequest, String name, String value) throws Exception {
		Map<String, String> formParams = extract(httpRequest, "formParams");
		assertThat(formParams).contains(entry(name, value));
		checkHeader(httpRequest, "Content-Type", "application/x-www-form-urlencoded");

		reset(client);
		httpRequest.execute();

		ArgumentCaptor<HttpEntityEnclosingRequest> rqCaptor = ArgumentCaptor.forClass(HttpEntityEnclosingRequest.class);
		verify(client).execute((HttpRequestBase) rqCaptor.capture());

		HttpEntityEnclosingRequest rq = rqCaptor.getValue();
		assertThat(rq.getEntity())
				.isNotNull()
				.isExactlyInstanceOf(UrlEncodedFormEntity.class);

		UrlEncodedFormEntity entity = (UrlEncodedFormEntity) rq.getEntity();
		StringWriter writer = new StringWriter();
		IOUtils.copy(entity.getContent(), writer);

		String body = writer.toString();
		String[] parts = body.split("&");
		assertThat(parts).contains(name + "=" + value);

		assertThat(rq.getFirstHeader("Content-Type").getValue()).isEqualTo("application/x-www-form-urlencoded");
	}

	@Override
	protected void checkRequestBody(HttpRequest httpRequest, String body) throws Exception {
		String requestBody = extract(httpRequest, "body");
		assertThat(requestBody).isEqualTo(body);
	}

	@SuppressWarnings("unchecked")
	private <T> T extract(Object source, String name) throws IllegalAccessException {
		return (T) readField(source, name, true);
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
