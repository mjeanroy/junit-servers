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

package com.github.mjeanroy.junit.servers.client.impl.async_http_client;

import static java.util.Arrays.asList;
import static org.apache.commons.lang3.reflect.FieldUtils.readField;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Map;

import com.github.mjeanroy.junit.servers.client.BaseHttpResponseTest;
import com.github.mjeanroy.junit.servers.client.HttpResponse;
import com.ning.http.client.Response;

public class AsyncHttpResponseTest extends BaseHttpResponseTest {

	private Response response;

	@Override
	protected void onSetUp() throws Exception {
		response = mock(Response.class);
	}

	@Override
	protected HttpResponse createHttpResponse() throws Exception {
		return new AsyncHttpResponse(response);
	}

	@Override
	protected void checkInternals(HttpResponse rsp) throws Exception {
		Response internalRsp = (Response) readField(rsp, "response", true);
		assertThat(internalRsp)
				.isNotNull()
				.isSameAs(response);
	}

	@Override
	protected void mockInternals(int status, String body, Map<String, String> headers) throws Exception {
		when(response.getStatusCode()).thenReturn(status);
		when(response.getResponseBody()).thenReturn(body);

		for (Map.Entry<String, String> entry : headers.entrySet()) {
			String headerName = entry.getKey();
			String headerValue = entry.getValue();
			when(response.getHeader(headerName)).thenReturn(headerValue);
			when(response.getHeaders(headerName)).thenReturn(asList(headerValue));
		}
	}
}
