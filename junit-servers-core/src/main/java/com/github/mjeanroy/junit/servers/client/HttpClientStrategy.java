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

package com.github.mjeanroy.junit.servers.client;

import com.github.mjeanroy.junit.servers.client.impl.apache.ApacheHttpClient;
import com.github.mjeanroy.junit.servers.client.impl.async.AsyncHttpClient;
import com.github.mjeanroy.junit.servers.client.impl.ning.NingAsyncHttpClient;
import com.github.mjeanroy.junit.servers.client.impl.okhttp3.OkHttpClient;
import com.github.mjeanroy.junit.servers.commons.ClassUtils;
import com.github.mjeanroy.junit.servers.servers.AbstractConfiguration;
import com.github.mjeanroy.junit.servers.servers.EmbeddedServer;

/**
 * Available strategies that can be used to build
 * appropriate implementation of http client.
 *
 * <p>
 *
 * Currently, following strategies are available:
 * <ul>
 *   <li>{@link HttpClientStrategy#ASYNC_HTTP_CLIENT}: use the <a href="https://github.com/AsyncHttpClient/async-http-client">async-http-client</a> library.</li>
 *   <li>{@link HttpClientStrategy#OK_HTTP3}: use <a href="http://square.github.io/okhttp/">OkHttp</a> library.</li>
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
	OK_HTTP3("OkHttp") {
		@Override
		public boolean support() {
			return SUPPORT_OK_HTTP3_CLIENT;
		}

		@Override
		HttpClient instantiate(EmbeddedServer<? extends AbstractConfiguration> server) {
			return OkHttpClient.defaultOkHttpClient(server);
		}

		@Override
		HttpClient instantiate(HttpClientConfiguration configuration, EmbeddedServer<? extends AbstractConfiguration> server) {
			return OkHttpClient.newOkHttpClient(configuration, server);
		}
	},

	/**
	 * Build http client using <a href="https://github.com/AsyncHttpClient/async-http-client">AsyncHttpClient</a> library.
	 *
	 * <p>
	 *
	 * <strong>This strategy requires Java 8.</strong>
	 */
	ASYNC_HTTP_CLIENT("AsyncHttpClient") {
		@Override
		public boolean support() {
			return SUPPORT_JAVA_8 && SUPPORT_ASYNC_HTTP_CLIENT;
		}

		@Override
		HttpClient instantiate(EmbeddedServer<? extends AbstractConfiguration> server) {
			return AsyncHttpClient.defaultAsyncHttpClient(server);
		}

		@Override
		HttpClient instantiate(HttpClientConfiguration configuration, EmbeddedServer<? extends AbstractConfiguration> server) {
			return AsyncHttpClient.newAsyncHttpClient(configuration, server);
		}
	},

	/**
	 * Build http client using <a href="https://github.com/ning/async-http-client">(Ning) AsyncHttpClient</a> library.
	 */
	NING_ASYNC_HTTP_CLIENT("(Ning) AsyncHttpClient") {
		@Override
		public boolean support() {
			return SUPPORT_NING_ASYNC_HTTP_CLIENT;
		}

		@Override
		HttpClient instantiate(EmbeddedServer<? extends AbstractConfiguration> server) {
			return NingAsyncHttpClient.defaultAsyncHttpClient(server);
		}

		@Override
		HttpClient instantiate(HttpClientConfiguration configuration, EmbeddedServer<? extends AbstractConfiguration> server) {
			return NingAsyncHttpClient.newAsyncHttpClient(configuration, server);
		}
	},

	/**
	 * Build http client using <a href="https://hc.apache.org/">ApacheHttpClient</a> library.
	 */
	APACHE_HTTP_CLIENT("Apache HttpComponent") {
		@Override
		public boolean support() {
			return SUPPORT_APACHE_HTTP_CLIENT;
		}

		@Override
		HttpClient instantiate(EmbeddedServer<? extends AbstractConfiguration> server) {
			return ApacheHttpClient.defaultApacheHttpClient(server);
		}

		@Override
		HttpClient instantiate(HttpClientConfiguration configuration, EmbeddedServer<? extends AbstractConfiguration> server) {
			return ApacheHttpClient.newApacheHttpClient(configuration, server);
		}
	},

	/**
	 * Detect class available on classpath and use appropriate strategy to
	 * build http client client implementation:
	 *
	 * <ol>
	 *   <li>Try {@link HttpClientStrategy#OK_HTTP3}.</li>
	 *   <li>Try {@link HttpClientStrategy#ASYNC_HTTP_CLIENT}.</li>
	 *   <li>Try {@link HttpClientStrategy#NING_ASYNC_HTTP_CLIENT}.</li>
	 *   <li>Try {@link HttpClientStrategy#APACHE_HTTP_CLIENT}.</li>
	 * </ol>
	 */
	AUTO("OkHttp OR AsyncHttpClient OR Apache HttpComponent") {
		@Override
		public boolean support() {
			for (HttpClientStrategy strategy : HttpClientStrategy.values()) {
				if (strategy != this && strategy.support()) {
					return true;
				}
			}

			return false;
		}

		@Override
		HttpClient instantiate(EmbeddedServer<? extends AbstractConfiguration> server) {
			for (HttpClientStrategy strategy : HttpClientStrategy.values()) {
				if (strategy.support()) {
					return strategy.instantiate(server);
				}
			}

			throw new UnsupportedOperationException(
				"Http client implementation cannot be found, please add OkHttp, AsyncHttpClient or ApacheHttpClient to your classpath"
			);
		}

		@Override
		HttpClient instantiate(HttpClientConfiguration configuration, EmbeddedServer<? extends AbstractConfiguration> server) {
			for (HttpClientStrategy strategy : HttpClientStrategy.values()) {
				if (strategy.support()) {
					return strategy.instantiate(configuration, server);
				}
			}

			throw new UnsupportedOperationException(
				"Http client implementation cannot be found, please add OkHttp, AsyncHttpClient or ApacheHttpClient to your classpath"
			);
		}
	};

	/**
	 * The CompletableFuture FQN that has been added in JDK8.
	 */
	private static final String COMPLETABLE_FUTURE_CLASS = "java.util.concurrent.CompletableFuture";

	/**
	 * This is a flag that can be used to ensure that the runtime environment support Java 8 API.
	 * For now, the only verification is the support of the new JDK8 CompletableFuture class.
	 *
	 * @see HttpClientStrategy#COMPLETABLE_FUTURE_CLASS
	 */
	private static final boolean SUPPORT_JAVA_8 = ClassUtils.isPresent(COMPLETABLE_FUTURE_CLASS);

	/**
	 * The FQN entry point for the async-http-client library.
	 *
	 * @see <a href="http://www.javadoc.io/doc/org.asynchttpclient/async-http-client">http://www.javadoc.io/doc/org.asynchttpclient/async-http-client</a>
	 */
	private static final String ASYNC_HTTP_CLIENT_CLASS = "org.asynchttpclient.DefaultAsyncHttpClient";

	/**
	 * A flag that can be used to know if async-http-client is available.
	 * The following conditions must be met:
	 * <ul>
	 *   <li>The runtime must be executed on, at least, <strong>Java 8</strong>.</li>
	 *   <li>The async-http-client library must be available on the classpath.</li>
	 * </ul>
	 *
	 * @see HttpClientStrategy#SUPPORT_JAVA_8
	 * @see HttpClientStrategy#ASYNC_HTTP_CLIENT_CLASS
	 * @see <a href="https://github.com/AsyncHttpClient/async-http-client">https://github.com/AsyncHttpClient/async-http-client</a>
	 * @see <a href="http://www.javadoc.io/doc/org.asynchttpclient/async-http-client">http://www.javadoc.io/doc/org.asynchttpclient/async-http-client</a>
	 */
	private static final boolean SUPPORT_ASYNC_HTTP_CLIENT = SUPPORT_JAVA_8 && ClassUtils.isPresent(ASYNC_HTTP_CLIENT_CLASS);

	/**
	 * The FQN entry point for the (ning) async-http-client library.
	 *
	 * @see <a href="http://www.javadoc.io/doc/com.ning/async-http-client">http://www.javadoc.io/doc/com.ning/async-http-client</a>
	 */
	private static final String NING_ASYNC_HTTP_CLIENT_CLASS = "com.ning.http.client.AsyncHttpClient";

	/**
	 * A flag that can be used to know if (ning) async-http-client is available.
	 * For now, the only condition is that the (ning) async-http-client library is available
	 * on the classpath.
	 *
	 * @see HttpClientStrategy#NING_ASYNC_HTTP_CLIENT_CLASS
	 * @see <a href="https://github.com/ning/async-http-client">https://github.com/ning/async-http-client</a>
	 * @see <a href="http://www.javadoc.io/doc/com.ning/async-http-client">http://www.javadoc.io/doc/com.ning/async-http-client</a>
	 */
	private static final boolean SUPPORT_NING_ASYNC_HTTP_CLIENT = ClassUtils.isPresent(NING_ASYNC_HTTP_CLIENT_CLASS);

	/**
	 * The FQN entry point for the apache http-component library.
	 *
	 * @see <a href="http://www.javadoc.io/doc/org.apache.httpcomponents/httpclient">http://www.javadoc.io/doc/org.apache.httpcomponents/httpclient</a>
	 */
	private static final String APACHE_HTTP_CLIENT_CLASS = "org.apache.http.impl.client.CloseableHttpClient";

	/**
	 * A flag that can be used to know if apache http-component library is available.
	 * For now, the only condition is that the library is available
	 * on the classpath.
	 *
	 * @see HttpClientStrategy#APACHE_HTTP_CLIENT_CLASS
	 * @see <a href="https://hc.apache.org/">https://hc.apache.org/</a>
	 * @see <a href="http://www.javadoc.io/doc/org.apache.httpcomponents/httpclient">http://www.javadoc.io/doc/org.apache.httpcomponents/httpclient</a>
	 */
	private static final boolean SUPPORT_APACHE_HTTP_CLIENT = ClassUtils.isPresent(APACHE_HTTP_CLIENT_CLASS);

	/**
	 * The FQN entry point for the square okhttp library.
	 *
	 * @see <a href="http://www.javadoc.io/doc/com.squareup.okhttp3/okhttp/3.8.1>http://www.javadoc.io/doc/com.squareup.okhttp3/okhttp/3.8.1</a>
	 */
	private static final String OK_HTTP3_CLIENT_CLASS = "okhttp3.OkHttpClient";

	/**
	 * A flag that can be used to know if okhttp library is available.
	 * For now, the only condition is that the library is available
	 * on the classpath.
	 *
	 * @see HttpClientStrategy#OK_HTTP3_CLIENT_CLASS
	 * @see <a href="http://square.github.io/okhttp/">http://square.github.io/okhttp/</a>
	 * @see <a href="http://www.javadoc.io/doc/com.squareup.okhttp3/okhttp/3.8.1>http://www.javadoc.io/doc/com.squareup.okhttp3/okhttp/3.8.1</a>
	 */
	private static final boolean SUPPORT_OK_HTTP3_CLIENT = ClassUtils.isPresent(OK_HTTP3_CLIENT_CLASS);

	/**
	 * The name of the underlying library.
	 */
	private final String library;

	/**
	 * Create strategy.
	 *
	 * @param library Name of underlying library.
	 */
	HttpClientStrategy(String library) {
		this.library = library;
	}

	/**
	 * Return the http client implementation.
	 *
	 * @param server Embedded server.
	 * @return Http client.
	 * @throws UnsupportedOperationException If the runtime environment does not allow the strategy (such as: the library has not been imported).
	 */
	public HttpClient build(EmbeddedServer<? extends AbstractConfiguration> server) {
		checkSupport();
		return instantiate(server);
	}

	/**
	 * Return the http client implementation.
	 *
	 * @param configuration HTTP Client configuration.
	 * @param server Embedded server.
	 * @return Http client.
	 * @throws UnsupportedOperationException If the runtime environment does not allow the strategy (such as: the library has not been imported).
	 */
	public HttpClient build(HttpClientConfiguration configuration, EmbeddedServer<? extends AbstractConfiguration> server) {
		checkSupport();
		return instantiate(configuration, server);
	}

	/**
	 * Ensure that the strategy is supported, throw {@link UnsupportedOperationException} otherwise.
	 *
	 * @throws UnsupportedOperationException If the strategy is not supported by the runtime environment.
	 */
	void checkSupport() {
		if (!support()) {
			throw new UnsupportedOperationException(
				"HTTP Client cannot be created because it is not supported by the runtime environment, " +
				"please import " + library
			);
		}
	}

	/**
	 * Check if the strategy can be used as it is supported by the runtime environment.
	 *
	 * @return {@code true} if the strategy can be instantiated, {@code false} otherwise.
	 */
	public abstract boolean support();

	/**
	 * Instantiate strategy.
	 *
	 * @param server The target server.
	 * @return The new http client instance.
	 */
	abstract HttpClient instantiate(EmbeddedServer<? extends AbstractConfiguration> server);

	/**
	 * Instantiate strategy with custom configuration.
	 *
	 * @param configuration HTTP Client configuration.
	 * @param server The target server.
	 * @return The new http client instance.
	 */
	abstract HttpClient instantiate(HttpClientConfiguration configuration, EmbeddedServer<? extends AbstractConfiguration> server);
}
