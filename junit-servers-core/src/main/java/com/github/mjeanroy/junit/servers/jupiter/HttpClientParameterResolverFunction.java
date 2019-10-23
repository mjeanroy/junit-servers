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
import com.github.mjeanroy.junit.servers.client.HttpClientConfiguration;
import com.github.mjeanroy.junit.servers.client.HttpClientConfigurationFactory;
import com.github.mjeanroy.junit.servers.client.HttpClientStrategy;
import com.github.mjeanroy.junit.servers.commons.reflect.Classes;
import com.github.mjeanroy.junit.servers.engine.EmbeddedServerRunner;
import com.github.mjeanroy.junit.servers.loggers.Logger;
import com.github.mjeanroy.junit.servers.loggers.LoggerFactory;
import org.junit.jupiter.api.extension.ParameterContext;

import java.util.Optional;

import static com.github.mjeanroy.junit.servers.client.HttpClientConfiguration.defaultConfiguration;

/**
 * Resolve {@link com.github.mjeanroy.junit.servers.client.HttpClient} parameter.
 */
class HttpClientParameterResolverFunction implements ParameterResolverFunction {

	/**
	 * Class Logger.
	 */
	private static final Logger log = LoggerFactory.getLogger(HttpClientParameterResolverFunction.class);

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
	public Object resolve(ParameterContext parameterContext, EmbeddedServerRunner serverAdapter) {
		log.debug("Resolving HTTP Client for parameter: {}", parameterContext);
		final Optional<TestHttpClient> maybeAnnotation = parameterContext.findAnnotation(TestHttpClient.class);

		final HttpClientConfiguration configuration;
		final HttpClientStrategy strategy;

		if (maybeAnnotation.isPresent()) {
			TestHttpClient annotation = maybeAnnotation.get();
			strategy = getStrategy(annotation);
			configuration = getConfiguration(annotation);
		} else {
			strategy = defaultStrategy();
			configuration = defaultConfiguration();
		}

		return serverAdapter.getClient(strategy, configuration);
	}

	private static HttpClientStrategy getStrategy(TestHttpClient client) {
		return client.strategy();
	}

	private static HttpClientConfiguration getConfiguration(TestHttpClient client) {
		final Class<? extends HttpClientConfigurationFactory> factoryClass = client.configuration();
		final HttpClientConfigurationFactory factory = Classes.instantiate(factoryClass);
		return factory.build();
	}

	private static HttpClientStrategy defaultStrategy() {
		return HttpClientStrategy.AUTO;
	}
}
