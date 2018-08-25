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

package com.github.mjeanroy.junit.servers.jupiter;

import com.github.mjeanroy.junit.servers.annotations.TestHttpClient;
import com.github.mjeanroy.junit.servers.client.HttpClient;
import com.github.mjeanroy.junit.servers.client.impl.ning.NingAsyncHttpClient;
import com.github.mjeanroy.junit.servers.engine.EmbeddedServerTestLifeCycleAdapter;
import com.github.mjeanroy.junit.servers.utils.builders.EmbeddedServerMockBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ParameterContext;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import static com.github.mjeanroy.junit.servers.client.HttpClientStrategy.NING_ASYNC_HTTP_CLIENT;
import static org.assertj.core.api.Assertions.assertThat;

public class HttpClientParameterResolverFunctionTest {

	private EmbeddedServerTestLifeCycleAdapter adapter;
	private HttpClientParameterResolverFunction resolver;

	@Before
	public void setUp() {
		resolver = HttpClientParameterResolverFunction.getInstance();
		adapter = new EmbeddedServerTestLifeCycleAdapter(new EmbeddedServerMockBuilder().build());
	}

	@Test
	public void it_should_resolve_http_client_with_default_strategy() throws Exception {
		final ParameterContext parameterContext = extractParameterContext("method_without_annotation");
		final Object result = resolver.resolve(parameterContext, adapter);

		assertThat(result).isInstanceOf(HttpClient.class);
	}

	@Test
	public void it_should_resolve_http_client_with_given_strategy() throws Exception {
		final ParameterContext parameterContext = extractParameterContext("method_with_annotation");
		final Object result = resolver.resolve(parameterContext, adapter);

		assertThat(result).isInstanceOf(HttpClient.class);
		assertThat(result).isExactlyInstanceOf(NingAsyncHttpClient.class);
	}

	private ParameterContext extractParameterContext(String methodName) throws Exception {
		Method method = getClass().getDeclaredMethod(methodName, HttpClient.class);
		Parameter parameter = method.getParameters()[0];
		return new FakeParameterContext(parameter);
	}

	@SuppressWarnings({ "unused", "WeakerAccess" })
	public void method_without_annotation(HttpClient client) {
	}

	@SuppressWarnings({ "unused", "WeakerAccess" })
	public void method_with_annotation(@TestHttpClient(strategy = NING_ASYNC_HTTP_CLIENT) HttpClient client) {
	}
}
