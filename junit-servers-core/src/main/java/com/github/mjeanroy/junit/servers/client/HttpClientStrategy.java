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

package com.github.mjeanroy.junit.servers.client;

import com.github.mjeanroy.junit.servers.client.impl.apache_http_client.ApacheHttpClient;
import com.github.mjeanroy.junit.servers.client.impl.async_http_client.AsyncHttpClient;
import com.github.mjeanroy.junit.servers.client.impl.ning_async_http_client.NingAsyncHttpClient;
import com.github.mjeanroy.junit.servers.client.impl.okhttp.OkHttpClient;
import com.github.mjeanroy.junit.servers.commons.ClassUtils;
import com.github.mjeanroy.junit.servers.servers.EmbeddedServer;
import com.github.mjeanroy.junit.servers.servers.configuration.AbstractConfiguration;

/**
 * Available strategies that can be used to build
 * appropriate implementation of http client.
 *
 * <p>
 *
 * Currently, following strategies are available:
 * <ul>
 *   <li>{@link HttpClientStrategy#ASYNC_HTTP_CLIENT}: use the <a href="https://github.com/AsyncHttpClient/async-http-client">async-http-client</a> library.</li>
 *   <li>{@link HttpClientStrategy#OK_HTTP}: use <a href="http://square.github.io/okhttp/">OkHttp</a> library.</li>
 *   <li>{@link HttpClientStrategy#NING_ASYNC_HTTP_CLIENT}: use <a href="https://github.com/ning/async-http-client">async-http-client from ning</a> library.</li>
 *   <li>{@link HttpClientStrategy#APACHE_HTTP_CLIENT}: use <a href="https://hc.apache.org/">apache http-client</a> library.</li>
 *   <li>{@link HttpClientStrategy#AUTO}: use classpath detection and choose the best available strategy (see below).</li>
 * </ul>
 *
 * <p>
 *
 * <strong>How the "best" strategy is selected:</strong>
 * <br>
 * Classpath detection is implemented in this order:
 * <ol>
 *   <li>If OkHttp library is detected, this strategy is automatically selected.</li>
 *   <li>
 *     If async-http-client is detected, this strategy is automatically (note that this strategy will be skipped
 *     with a jdk &lt; 8, since this library requires Java 8).
 *   </li>
 *   <li>Third test is ning-async-http-client, and it will be selected if library is detected.</li>
 *   <li>Finally, apache httpcomponent will be selected if available.</li>
 *   <li>If none of these libraries are available, an exception will be thrown.</li>
 * </ol>
 */
public enum HttpClientStrategy {
	/**
	 * Build http client using <a href="http://square.github.io/okhttp/">OkHttp</a> library.
	 */
	OK_HTTP {
		@Override
		public HttpClient build(EmbeddedServer<? extends AbstractConfiguration> server) {
			return OkHttpClient.defaultOkHttpClient(server);
		}
	},

	/**
	 * Build http client using <a href="https://github.com/AsyncHttpClient/async-http-client">AsyncHttpClient</a> library.
	 *
	 * <p>
	 *
	 * <strong>This strategy requires Java 8.</strong>
	 */
	ASYNC_HTTP_CLIENT {
		@Override
		public HttpClient build(EmbeddedServer<? extends AbstractConfiguration> server) {
			return AsyncHttpClient.defaultAsyncHttpClient(server);
		}
	},

	/**
	 * Build http client using <a href="https://github.com/ning/async-http-client">(Ning) AsyncHttpClient</a> library.
	 */
	NING_ASYNC_HTTP_CLIENT {
		@Override
		public HttpClient build(EmbeddedServer<? extends AbstractConfiguration> server) {
			return NingAsyncHttpClient.defaultAsyncHttpClient(server);
		}
	},

	/**
	 * Build http client using <a href="https://hc.apache.org/">ApacheHttpClient</a> library.
	 */
	APACHE_HTTP_CLIENT {
		@Override
		public HttpClient build(EmbeddedServer<? extends AbstractConfiguration> server) {
			return ApacheHttpClient.defaultApacheHttpClient(server);
		}
	},

	/**
	 * Detect class available on classpath and use appropriate strategy to
	 * build http client client implementation:
	 *
	 * <ol>
	 *   <li>Try {@link HttpClientStrategy#OK_HTTP}.</li>
	 *   <li>Try {@link HttpClientStrategy#ASYNC_HTTP_CLIENT}.</li>
	 *   <li>Try {@link HttpClientStrategy#NING_ASYNC_HTTP_CLIENT}.</li>
	 *   <li>Try {@link HttpClientStrategy#APACHE_HTTP_CLIENT}.</li>
	 * </ol>
	 */
	AUTO {
		@Override
		public HttpClient build(EmbeddedServer<? extends AbstractConfiguration> server) {
			if (SUPPORT_OK_HTTP_CLIENT) {
				return OK_HTTP.build(server);
			}

			if (SUPPORT_ASYNC_HTTP_CLIENT) {
				return ASYNC_HTTP_CLIENT.build(server);
			}

			if (SUPPORT_NING_ASYNC_HTTP_CLIENT) {
				return NING_ASYNC_HTTP_CLIENT.build(server);
			}

			if (SUPPORT_APACHE_HTTP_CLIENT) {
				return APACHE_HTTP_CLIENT.build(server);
			}

			throw new UnsupportedOperationException(
				"Http client implementation cannot be found, please add OkHttp, AsyncHttpClient or ApacheHttpClient to your classpath"
			);
		}
	};

	// The JAVA version.
	private static final int JAVA_VERSION;

	static {
		final String javaVersion = System.getProperty("java.specification.version");
		final String[] parts = javaVersion.split("\\.");

		// For JDK9, the return value will be "9", otherwise, it will be "1.8" or "1.7".
		// Lower Java version are not supported.
		final String majorVersion = parts[0];
		final String minorVersion = parts.length > 1 ? parts[1] : majorVersion;
		JAVA_VERSION = Integer.parseInt(minorVersion);
	}

	private static final String ASYNC_HTTP_CLIENT_CLASS = "org.asynchttpclient.DefaultAsyncHttpClient";
	private static final boolean SUPPORT_ASYNC_HTTP_CLIENT = JAVA_VERSION >= 8 && ClassUtils.isPresent(ASYNC_HTTP_CLIENT_CLASS);

	private static final String NING_ASYNC_HTTP_CLIENT_CLASS = "com.ning.http.client.AsyncHttpClient";
	private static final boolean SUPPORT_NING_ASYNC_HTTP_CLIENT = ClassUtils.isPresent(NING_ASYNC_HTTP_CLIENT_CLASS);

	private static final String APACHE_HTTP_CLIENT_CLASS = "org.apache.http.impl.client.CloseableHttpClient";
	private static final boolean SUPPORT_APACHE_HTTP_CLIENT = ClassUtils.isPresent(APACHE_HTTP_CLIENT_CLASS);

	private static final String OK_HTTP_CLIENT_CLASS = "okhttp3.OkHttpClient";
	private static final boolean SUPPORT_OK_HTTP_CLIENT = ClassUtils.isPresent(OK_HTTP_CLIENT_CLASS);

	/**
	 * Return the http client implementation.
	 *
	 * @param server Embedded server.
	 * @return Http client.
	 */
	public abstract HttpClient build(EmbeddedServer<? extends AbstractConfiguration> server);
}
