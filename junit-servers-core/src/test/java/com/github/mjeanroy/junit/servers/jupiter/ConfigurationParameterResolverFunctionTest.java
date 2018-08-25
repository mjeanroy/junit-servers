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

import com.github.mjeanroy.junit.servers.engine.EmbeddedServerTestLifeCycleAdapter;
import com.github.mjeanroy.junit.servers.servers.AbstractConfiguration;
import com.github.mjeanroy.junit.servers.servers.EmbeddedServer;
import com.github.mjeanroy.junit.servers.utils.builders.EmbeddedServerMockBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ParameterContext;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import static org.assertj.core.api.Assertions.assertThat;

public class ConfigurationParameterResolverFunctionTest {

	private EmbeddedServer<?> server;
	private EmbeddedServerTestLifeCycleAdapter adapter;
	private ConfigurationResolverFunction resolver;

	@Before
	public void setUp() {
		resolver = ConfigurationResolverFunction.getInstance();
		server = new EmbeddedServerMockBuilder().build();
		adapter = new EmbeddedServerTestLifeCycleAdapter(server);
	}

	@Test
	public void it_should_resolve_configuration() throws Exception {
		final ParameterContext parameterContext = createParameterContext();
		final Object result = resolver.resolve(parameterContext, adapter);

		assertThat(result).isSameAs(server.getConfiguration());
	}

	private ParameterContext createParameterContext() throws Exception {
		Method method = getClass().getDeclaredMethod("method", AbstractConfiguration.class);
		Parameter parameter = method.getParameters()[0];
		return new FakeParameterContext(parameter);
	}

	@SuppressWarnings({ "unused", "WeakerAccess" })
	public void method(AbstractConfiguration configuration) {
	}
}
