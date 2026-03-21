/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2026 <mickael.jeanroy@gmail.com>
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
import com.github.mjeanroy.junit.servers.commons.reflect.Classes;
import com.github.mjeanroy.junit.servers.servers.AbstractConfiguration;
import com.github.mjeanroy.junit.servers.servers.EmbeddedServer;

import java.util.Iterator;
import java.util.ServiceLoader;

/// Available strategies that can be used to build
/// appropriate implementation of http client.
///
/// Currently, following strategies are available:
/// - [HttpClientStrategy#ASYNC_HTTP_CLIENT]: use the [async-http-client](https://github.com/AsyncHttpClient/async-http-client) library.
/// - [HttpClientStrategy#OK_HTTP3]: use [OkHttp](http://square.github.io/okhttp) library.
/// - [HttpClientStrategy#NING_ASYNC_HTTP_CLIENT]: use [async-http-client from ning](https://github.com/ning/async-http-client) library.
/// - [HttpClientStrategy#APACHE_HTTP_CLIENT]: use [apache http-client](https://hc.apache.org/) library.
/// - [HttpClientStrategy#AUTO]: use classpath detection and choose the best available strategy (see below).
///
/// **How the "best" strategy is selected:**
///
/// Classpath detection is implemented in this order:
/// 1. If OkHttp library is detected, this strategy is automatically selected.
/// 2. If async-http-client is detected, this strategy is automatically (note that this strategy will be skipped
///    with a jdk &lt; 8, since this library requires Java 8).
/// 3. Third test is ning-async-http-client, and it will be selected if library is detected.
/// 4. Finally, apache httpcomponent will be selected if available.
/// 5. If none of these libraries are available, an exception will be thrown.
public enum HttpClientStrategy {
	/// Build http client using [OkHttp](http://square.github.io/okhttp/) library.
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

	/// Build http client using [AsyncHttpClient](https://github.com/AsyncHttpClient/async-http-client) library.
	///
	/// **This strategy requires Java 8.**
	ASYNC_HTTP_CLIENT("AsyncHttpClient") {
		@Override
		public boolean support() {
			return SUPPORT_ASYNC_HTTP_CLIENT;
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

	/// Build http client using [(Ning) AsyncHttpClient](https://github.com/ning/async-http-client) library.
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

	/// Build http client using [ApacheHttpClient](https://hc.apache.org/) library.
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

	/// Detect class available on classpath and use appropriate strategy to
	/// build http client client implementation:
	/// 1. Try [HttpClientStrategy#OK_HTTP3].
	/// 2. Try [HttpClientStrategy#ASYNC_HTTP_CLIENT].
	/// 3. Try [HttpClientStrategy#NING_ASYNC_HTTP_CLIENT].
	/// 4. Try [HttpClientStrategy#APACHE_HTTP_CLIENT].
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
			// First, look into available providers.
			HttpClientProvider provider = findProviders();
			if (provider != null) {
				return provider.instantiate(server);
			}

			// Then, use classpath detection.
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
			// First, look into available providers.
			HttpClientProvider provider = findProviders();
			if (provider != null) {
				return provider.instantiate(configuration, server);
			}

			// Then, use classpath detection.
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

	/// Look into [HttpClientProvider] available using the standard Service Provider Interface.
	///
	/// @return Custom [HttpClientProvider], `null` if no one exists.
	private static HttpClientProvider findProviders() {
		ServiceLoader<HttpClientProvider> providers = ServiceLoader.load(HttpClientProvider.class);
		Iterator<HttpClientProvider> it = providers.iterator();
		return it.hasNext() ? it.next() : null;
	}

	/// The FQN entry point for the [async-http-client](http://www.javadoc.io/doc/org.asynchttpclient/async-http-client) library.
	private static final String ASYNC_HTTP_CLIENT_CLASS = "org.asynchttpclient.DefaultAsyncHttpClient";

	/// A flag that can be used to know if async-http-client is available.
	///
	/// The following conditions must be met:
	/// - The runtime must be executed on, at least, **Java 8**.
	/// - The async-http-client library must be available on the classpath.
	///
	/// @see HttpClientStrategy#ASYNC_HTTP_CLIENT_CLASS
	private static final boolean SUPPORT_ASYNC_HTTP_CLIENT = Classes.isPresent(ASYNC_HTTP_CLIENT_CLASS);

	/// The FQN entry point for the (ning) async-http-client library.
	private static final String NING_ASYNC_HTTP_CLIENT_CLASS = "com.ning.http.client.AsyncHttpClient";

	/// A flag that can be used to know if (ning) async-http-client is available.
	///
	/// For now, the only condition is that the (ning) async-http-client library is available
	/// on the classpath.
	///
	/// @see HttpClientStrategy#NING_ASYNC_HTTP_CLIENT_CLASS
	private static final boolean SUPPORT_NING_ASYNC_HTTP_CLIENT = Classes.isPresent(NING_ASYNC_HTTP_CLIENT_CLASS);

	/// The FQN entry point for the apache http-component library.
	private static final String APACHE_HTTP_CLIENT_CLASS = "org.apache.http.impl.client.CloseableHttpClient";

	/// A flag that can be used to know if apache http-component library is available.
	///
	/// For now, the only condition is that the library is available
	/// on the classpath.
	///
	/// @see HttpClientStrategy#APACHE_HTTP_CLIENT_CLASS
	private static final boolean SUPPORT_APACHE_HTTP_CLIENT = Classes.isPresent(APACHE_HTTP_CLIENT_CLASS);

	/// The FQN entry point for the square okhttp library.
	private static final String OK_HTTP3_CLIENT_CLASS = "okhttp3.OkHttpClient";

	/// A flag that can be used to know if okhttp library is available.
	///
	/// For now, the only condition is that the library is available
	/// on the classpath.
	///
	/// @see HttpClientStrategy#OK_HTTP3_CLIENT_CLASS
	private static final boolean SUPPORT_OK_HTTP3_CLIENT = Classes.isPresent(OK_HTTP3_CLIENT_CLASS);

	/// The name of the underlying library.
	private final String library;

	/// Create strategy.
	///
	/// @param library Name of underlying library.
	HttpClientStrategy(String library) {
		this.library = library;
	}

	/// Return the http client implementation.
	///
	/// @param server Embedded server.
	/// @return Http client.
	/// @throws UnsupportedOperationException If the runtime environment does not allow the strategy (such as: the library has not been imported).
	public HttpClient build(EmbeddedServer<? extends AbstractConfiguration> server) {
		checkSupport();
		return instantiate(server);
	}

	/// Return the http client implementation.
	///
	/// @param configuration HTTP Client configuration.
	/// @param server Embedded server.
	/// @return Http client.
	/// @throws UnsupportedOperationException If the runtime environment does not allow the strategy (such as: the library has not been imported).
	public HttpClient build(HttpClientConfiguration configuration, EmbeddedServer<? extends AbstractConfiguration> server) {
		checkSupport();
		return instantiate(configuration, server);
	}

	/// Ensure that the strategy is supported, throw [UnsupportedOperationException] otherwise.
	///
	/// @throws UnsupportedOperationException If the strategy is not supported by the runtime environment.
	void checkSupport() {
		if (!support()) {
			throw new UnsupportedOperationException(
				"HTTP Client cannot be created because it is not supported by the runtime environment, please import " + library
			);
		}
	}

	/// Check if the strategy can be used as it is supported by the runtime environment.
	///
	/// @return `true` if the strategy can be instantiated, `false` otherwise.
	public abstract boolean support();

	/// Instantiate strategy.
	///
	/// @param server The target server.
	/// @return The new http client instance.
	abstract HttpClient instantiate(EmbeddedServer<? extends AbstractConfiguration> server);

	/// Instantiate strategy with custom configuration.
	///
	/// @param configuration HTTP Client configuration.
	/// @param server The target server.
	/// @return The new http client instance.
	abstract HttpClient instantiate(HttpClientConfiguration configuration, EmbeddedServer<? extends AbstractConfiguration> server);
}
