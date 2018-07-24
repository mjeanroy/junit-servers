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

import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * Builder for {@link HttpResponse} instances.
 */
public class ApacheHttpResponseBuilder extends AbstractHttpResponseBuilder<HttpResponse, ApacheHttpResponseBuilder> {

	@Override
	public HttpResponse build() {
		ProtocolVersion protocolVersion = new ProtocolVersion("HTTP", 1, 1);
		StatusLine statusLine = new BasicStatusLine(protocolVersion, status, "");
		HttpResponse response = new BasicHttpResponse(statusLine);

		InputStream is = new ByteArrayInputStream(body.getBytes(StandardCharsets.UTF_8));
		BasicHttpEntity entity = new BasicHttpEntity();
		entity.setContent(is);
		response.setEntity(entity);

		for (Map.Entry<String, List<String>> header : headers.entrySet()) {
			for (String value : header.getValue()) {
				response.addHeader(header.getKey(), value);
			}
		}

		return response;
	}
}
