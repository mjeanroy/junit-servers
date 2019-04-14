/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2019 <mickael.jeanroy@gmail.com>
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

package com.github.mjeanroy.junit.servers.jupiter;

import com.github.mjeanroy.junit.servers.annotations.TestHttpClient;
import com.github.mjeanroy.junit.servers.client.HttpClientStrategy;
import com.github.mjeanroy.junit.servers.engine.EmbeddedServerTestAdapter;
import org.junit.jupiter.api.extension.ParameterContext;

import java.util.function.Function;

/**
 * Resolve {@link com.github.mjeanroy.junit.servers.client.HttpClient} parameter.
 */
class HttpClientParameterResolverFunction implements ParameterResolverFunction {

	/**
	 * The singleton instance.
	 */
	private static final HttpClientParameterResolverFunction INSTANCE = new HttpClientParameterResolverFunction();

	/**
	 * Get instance of {@link HttpClientParameterResolverFunction} resolver.
	 *
	 * @return The instance.
	 */
	static HttpClientParameterResolverFunction getInstance() {
		return INSTANCE;
	}

	// Ensure non public instantiation.
	private HttpClientParameterResolverFunction() {
	}

	@Override
	public Object resolve(ParameterContext parameterContext, EmbeddedServerTestAdapter serverAdapter) {
		HttpClientStrategy strategy = getStrategy(parameterContext);
		return serverAdapter.getClient(strategy);
	}

	private static HttpClientStrategy getStrategy(ParameterContext parameterContext) {
		return parameterContext.findAnnotation(TestHttpClient.class)
			.map(new Function<TestHttpClient, HttpClientStrategy>() {
				@Override
				public HttpClientStrategy apply(TestHttpClient testHttpClient) {
					return testHttpClient.strategy();
				}
			})
			.orElse(
				defaultStrategy()
			);
	}

	private static HttpClientStrategy defaultStrategy() {
		return HttpClientStrategy.AUTO;
	}
}
