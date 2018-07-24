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

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpVersion;
import org.asynchttpclient.HttpResponseBodyPart;
import org.asynchttpclient.HttpResponseStatus;
import org.asynchttpclient.Response;
import org.asynchttpclient.netty.EagerResponseBodyPart;
import org.asynchttpclient.netty.NettyResponse;
import org.asynchttpclient.netty.NettyResponseStatus;
import org.asynchttpclient.uri.Uri;

import java.util.List;
import java.util.Map;

import static java.nio.charset.Charset.defaultCharset;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

/**
 * Builder for {@link HttpResponse} instances.
 */
public class AsyncHttpResponseBuilder extends AbstractHttpResponseBuilder<Response, AsyncHttpResponseBuilder> {

	@Override
	public Response build() {
		Uri uri = new Uri("http", null, "localhost", 8080, "/", "");

		io.netty.handler.codec.http.HttpResponseStatus rspStatus = io.netty.handler.codec.http.HttpResponseStatus.valueOf(status);
		HttpResponse httpResponse = new DefaultHttpResponse(HttpVersion.HTTP_1_1, rspStatus);
		HttpResponseStatus status = new NettyResponseStatus(uri, httpResponse, null);

		HttpHeaders headers = new DefaultHttpHeaders();
		for (Map.Entry<String, List<String>> entry : this.headers.entrySet()) {
			headers.add(entry.getKey(), entry.getValue());
		}

		final List<HttpResponseBodyPart> bodyParts;
		if (body != null) {
			final byte[] bodyBytes = body.getBytes(defaultCharset());
			ByteBuf buf = Unpooled.copiedBuffer(bodyBytes);
			HttpResponseBodyPart part = new EagerResponseBodyPart(buf, true);
			bodyParts = singletonList(part);
		} else {
			bodyParts = emptyList();
		}

		return new NettyResponse(status, headers, bodyParts);
	}
}
