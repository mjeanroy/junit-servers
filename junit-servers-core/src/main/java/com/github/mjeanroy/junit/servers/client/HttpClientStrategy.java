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

package com.github.mjeanroy.junit.servers.client;

import com.github.mjeanroy.junit.servers.client.impl.apache_http_client.ApacheHttpClient;
import com.github.mjeanroy.junit.servers.client.impl.async_http_client.AsyncHttpClient;
import com.github.mjeanroy.junit.servers.client.impl.ning_async_http_client.NingAsyncHttpClient;
import com.github.mjeanroy.junit.servers.commons.ClassUtils;
import com.github.mjeanroy.junit.servers.servers.EmbeddedServer;

/**
 * Available strategies that can be used to build
 * appropriate implementation of http client.
 */
public enum HttpClientStrategy {

	/**
	 * Build http client using AsyncHttpClient library.
	 */
	ASYNC_HTTP_CLIENT {
		@Override
		public HttpClient build(EmbeddedServer server) {
			return AsyncHttpClient.defaultAsyncHttpClient(server);
		}
	},

	/**
	 * Build http client using NingAsyncHttpClient library.
	 */
	NING_ASYNC_HTTP_CLIENT {
		@Override
		public HttpClient build(EmbeddedServer server) {
			return NingAsyncHttpClient.defaultAsyncHttpClient(server);
		}
	},

	/**
	 * Build http client using ApacheHttpClient library.
	 */
	APACHE_HTTP_CLIENT {
		@Override
		public HttpClient build(EmbeddedServer server) {
			return ApacheHttpClient.defaultApacheHttpClient(server);
		}
	},

	/**
	 * Detect class available on classpath and use appropriate strategy to
	 * build http client client implementation.
	 */
	AUTO {
		@Override
		public HttpClient build(EmbeddedServer server) {
			if (ClassUtils.isPresent("org.asynchttpclient.DefaultAsyncHttpClient")) {
				return ASYNC_HTTP_CLIENT.build(server);
			}

			if (ClassUtils.isPresent("com.ning.http.client.AsyncHttpClient")) {
				return NING_ASYNC_HTTP_CLIENT.build(server);
			}

			if (ClassUtils.isPresent("org.apache.http.impl.client.CloseableHttpClient")) {
				return APACHE_HTTP_CLIENT.build(server);
			}

			throw new UnsupportedOperationException("Http client implementation cannot be found, please add AsyncHttpClient or ApacheHttpClient to your classpath");
		}
	};

	/**
	 * Return http client implementation.
	 *
	 * @param server Embedded server.
	 * @return Http client.
	 */
	public abstract HttpClient build(EmbeddedServer server);
}
