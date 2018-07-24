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

package com.github.mjeanroy.junit.servers.client.impl.apache;

import com.github.mjeanroy.junit.servers.client.impl.AbstractHttpResponseImplTest;
import com.github.mjeanroy.junit.servers.utils.builders.ApacheHttpResponseBuilder;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.apache.http.HttpResponse;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ApacheHttpResponseTest extends AbstractHttpResponseImplTest<ApacheHttpResponseBuilder, HttpResponse, ApacheHttpResponse> {

	@Override
	protected ApacheHttpResponseBuilder getBuilder() {
		return new ApacheHttpResponseBuilder();
	}

	@Override
	protected ApacheHttpResponse createHttpResponse(HttpResponse delegate, long duration) {
		return new ApacheHttpResponse(delegate, duration);
	}

	@Test
	public void it_should_implement_to_string() {
		HttpResponse apacheHttpResponse = new ApacheHttpResponseBuilder().build();
		long duration = 1000L;
		ApacheHttpResponse response = new ApacheHttpResponse(apacheHttpResponse, duration);

		assertThat(response.toString()).isEqualTo(
				"ApacheHttpResponse{" +
						"duration: 1000, " +
						"response: HTTP/1.1 200  [] [Chunked: false]" +
				"}"
		);
	}

	@Test
	public void it_should_implement_equal_and_hash_code() {
		EqualsVerifier.forClass(ApacheHttpResponse.class)
				.withRedefinedSuperclass()
				.withIgnoredFields("readResponseBodyLock", "_body")
				.verify();
	}
}
