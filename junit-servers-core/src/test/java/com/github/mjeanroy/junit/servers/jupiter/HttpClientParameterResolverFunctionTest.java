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
import com.github.mjeanroy.junit.servers.client.HttpClient;
import com.github.mjeanroy.junit.servers.client.HttpClientConfiguration;
import com.github.mjeanroy.junit.servers.client.HttpClientConfigurationFactory;
import com.github.mjeanroy.junit.servers.client.impl.ning.NingAsyncHttpClient;
import com.github.mjeanroy.junit.servers.engine.EmbeddedServerRunner;
import com.github.mjeanroy.junit.servers.utils.builders.EmbeddedServerMockBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ParameterContext;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import static com.github.mjeanroy.junit.servers.client.HttpClientStrategy.NING_ASYNC_HTTP_CLIENT;
import static org.assertj.core.api.Assertions.assertThat;

class HttpClientParameterResolverFunctionTest {

	private EmbeddedServerRunner adapter;
	private HttpClientParameterResolverFunction resolver;

	@BeforeEach
	void setUp() {
		resolver = HttpClientParameterResolverFunction.getInstance();
		adapter = new EmbeddedServerRunner(new EmbeddedServerMockBuilder().build());
	}

	@Test
	void it_should_resolve_http_client_with_default_strategy() throws Exception {
		final ParameterContext parameterContext = extractParameterContext("method_without_annotation");
		final Object result = resolver.resolve(parameterContext, adapter);

		assertThat(result).isInstanceOf(HttpClient.class);
	}

	@Test
	void it_should_resolve_http_client_with_given_strategy() throws Exception {
		final ParameterContext parameterContext = extractParameterContext("method_with_annotation");
		final Object result = resolver.resolve(parameterContext, adapter);

		assertThat(result).isInstanceOf(HttpClient.class);
		assertThat(result).isExactlyInstanceOf(NingAsyncHttpClient.class);
	}

	@Test
	public void it_should_resolve_http_client_with_given_configuration() throws Exception {
		final ParameterContext parameterContext = extractParameterContext("method_with_annotation_and_configuration_factory");
		final Object result = resolver.resolve(parameterContext, adapter);

		assertThat(result).isInstanceOf(HttpClient.class);

		final HttpClient httpClient = (HttpClient) result;
		assertThat(httpClient.getConfiguration()).isNotNull();
		assertThat(httpClient.getConfiguration().isFollowRedirect()).isFalse();
	}

	@Test
	public void it_should_resolve_http_client_with_given_configuration_on_meta_annotation() throws Exception {
		final ParameterContext parameterContext = extractParameterContext("method_with_meta_annotation");
		final Object result = resolver.resolve(parameterContext, adapter);

		assertThat(result).isInstanceOf(HttpClient.class);

		final HttpClient httpClient = (HttpClient) result;
		assertThat(httpClient.getConfiguration()).isNotNull();
		assertThat(httpClient.getConfiguration().isFollowRedirect()).isFalse();
	}

	private ParameterContext extractParameterContext(String methodName) throws Exception {
		Method method = getClass().getDeclaredMethod(methodName, HttpClient.class);
		Parameter parameter = method.getParameters()[0];
		return new FakeParameterContext(parameter);
	}

	public void method_without_annotation(HttpClient client) {
	}

	public void method_with_annotation(@TestHttpClient(strategy = NING_ASYNC_HTTP_CLIENT) HttpClient client) {
	}

	public void method_with_annotation_and_configuration_factory(
		@TestHttpClient(configuration = CustomHttpClientConfigurationFactory.class) HttpClient client) {
	}

	public void method_with_meta_annotation(
		@DefaultTestHttpClient HttpClient client) {
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.PARAMETER)
	@Inherited
	@Documented
	@TestHttpClient(configuration = CustomHttpClientConfigurationFactory.class)
	private @interface DefaultTestHttpClient {
	}

	private static class CustomHttpClientConfigurationFactory implements HttpClientConfigurationFactory {
		@Override
		public HttpClientConfiguration build() {
			return new HttpClientConfiguration.Builder().disableFollowRedirect().build();
		}
	}
}
