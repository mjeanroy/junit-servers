/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2023 <mickael.jeanroy@gmail.com>
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
import com.github.mjeanroy.junit.servers.engine.AnnotationsHandlerRunner;
import com.github.mjeanroy.junit.servers.engine.EmbeddedServerRunner;
import com.github.mjeanroy.junit.servers.servers.AbstractConfiguration;
import com.github.mjeanroy.junit.servers.servers.EmbeddedServer;
import com.github.mjeanroy.junit.servers.testing.FakeJunitExtensionContext;
import com.github.mjeanroy.junit.servers.testing.FakeJunitParameterContext;
import com.github.mjeanroy.junit.servers.testing.FakeJunitStore;
import com.github.mjeanroy.junit.servers.utils.builders.EmbeddedServerMockBuilder;
import com.github.mjeanroy.junit.servers.utils.fixtures.FixtureClass;
import com.github.mjeanroy.junit.servers.utils.impl.FakeEmbeddedServer;
import com.github.mjeanroy.junit.servers.utils.impl.FakeEmbeddedServerConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ParameterContext;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import static com.github.mjeanroy.junit.servers.client.HttpClientStrategy.NING_ASYNC_HTTP_CLIENT;
import static org.assertj.core.api.Assertions.assertThat;

class JunitServerExtensionTest {

	@Test
	void it_should_initialize_extension_with_given_configuration_and_start_given_server_before_all_tests() {
		AbstractConfiguration configuration = new FakeEmbeddedServerConfiguration();
		JunitServerExtension extension = new JunitServerExtension(configuration);
		FixtureClass testInstance = new FixtureClass();
		FakeJunitExtensionContext context = new FakeJunitExtensionContext(testInstance);

		extension.beforeAll(context);

		FakeJunitStore store = context.getSingleStore();
		EmbeddedServerRunner serverAdapter = store.get("serverAdapter", EmbeddedServerRunner.class);

		assertThat(serverAdapter).isNotNull();
		assertThat(serverAdapter.getServer()).isNotNull().isInstanceOf(FakeEmbeddedServer.class);
		assertThat(serverAdapter.getServer().getConfiguration()).isSameAs(configuration);
		assertThat(serverAdapter.getServer().isStarted()).isTrue();
	}

	@Test
	void it_should_initialize_extension_with_given_server_and_start_given_server_before_all_tests() {
		EmbeddedServer<?> server = new EmbeddedServerMockBuilder().build();
		JunitServerExtension extension = new JunitServerExtension(server);
		FixtureClass testInstance = new FixtureClass();
		FakeJunitExtensionContext context = new FakeJunitExtensionContext(testInstance);

		extension.beforeAll(context);

		FakeJunitStore store = context.getSingleStore();
		EmbeddedServerRunner serverAdapter = store.get("serverAdapter", EmbeddedServerRunner.class);

		assertThat(serverAdapter).isNotNull();
		assertThat(serverAdapter.getServer()).isSameAs(server);
		assertThat(serverAdapter.getServer().isStarted()).isTrue();
	}

	@Test
	void it_should_start_server_before_all_tests() {
		JunitServerExtension extension = new JunitServerExtension();
		FixtureClass testInstance = new FixtureClass();
		FakeJunitExtensionContext context = new FakeJunitExtensionContext(testInstance);

		extension.beforeAll(context);

		FakeJunitStore store = context.getSingleStore();
		EmbeddedServerRunner serverAdapter = store.get("serverAdapter", EmbeddedServerRunner.class);

		assertThat(serverAdapter).isNotNull();
		assertThat(serverAdapter.getServer()).isNotNull();
		assertThat(serverAdapter.getServer().isStarted()).isTrue();
	}

	@Test
	void it_should_stop_server_after_all_tests() {
		JunitServerExtension extension = new JunitServerExtension();
		FixtureClass testInstance = new FixtureClass();
		FakeJunitExtensionContext context = new FakeJunitExtensionContext(testInstance);

		extension.beforeAll(context);

		FakeJunitStore store = context.getSingleStore();
		EmbeddedServerRunner serverAdapter = store.get("serverAdapter", EmbeddedServerRunner.class);

		extension.afterAll(context);

		assertThat(store.isEmpty()).isTrue();
		assertThat(serverAdapter.getServer().isStarted()).isFalse();
	}

	@Test
	void it_should_start_server_before_each_tests() {
		JunitServerExtension extension = new JunitServerExtension();
		FixtureClass testInstance = new FixtureClass();
		FakeJunitExtensionContext context = new FakeJunitExtensionContext(testInstance);

		extension.beforeEach(context);

		FakeJunitStore store = context.getSingleStore();
		EmbeddedServerRunner serverAdapter = store.get("serverAdapter", EmbeddedServerRunner.class);

		assertThat(serverAdapter).isNotNull();
		assertThat(serverAdapter.getServer()).isNotNull();
		assertThat(serverAdapter.getServer().isStarted()).isTrue();
	}

	@Test
	void it_should_stop_server_after_each_tests() {
		JunitServerExtension extension = new JunitServerExtension();
		FixtureClass testInstance = new FixtureClass();
		FakeJunitExtensionContext context = new FakeJunitExtensionContext(testInstance);

		extension.beforeEach(context);

		FakeJunitStore store = context.getSingleStore();
		EmbeddedServerRunner serverAdapter = store.get("serverAdapter", EmbeddedServerRunner.class);

		extension.afterEach(context);

		assertThat(store.isEmpty()).isTrue();
		assertThat(serverAdapter.getServer().isStarted()).isFalse();
	}

	@Test
	void it_should_detect_if_server_must_be_started_and_stopped_after_all_tests() {
		JunitServerExtension extension = new JunitServerExtension();
		FixtureClass testInstance = new FixtureClass();
		FakeJunitExtensionContext context = new FakeJunitExtensionContext(testInstance);

		extension.beforeAll(context);
		extension.beforeEach(context);

		FakeJunitStore store = context.getSingleStore();
		EmbeddedServerRunner serverAdapter = store.get("serverAdapter", EmbeddedServerRunner.class);

		extension.afterEach(context);

		assertThat(store.isEmpty()).isFalse();
		assertThat(serverAdapter.getServer().isStarted()).isTrue();

		extension.afterAll(context);

		assertThat(store.isEmpty()).isTrue();
		assertThat(serverAdapter.getServer().isStarted()).isFalse();
	}

	@Test
	void it_should_inject_annotated_field_before_each_test() {
		JunitServerExtension extension = new JunitServerExtension();
		FixtureClass testInstance = new FixtureClass();
		FakeJunitExtensionContext context = new FakeJunitExtensionContext(testInstance);

		extension.beforeAll(context);

		FakeJunitStore store = context.getSingleStore();
		EmbeddedServerRunner serverAdapter = store.get("serverAdapter", EmbeddedServerRunner.class);

		extension.beforeEach(context);

		AnnotationsHandlerRunner annotationsAdapter = store.get("annotationsAdapter", AnnotationsHandlerRunner.class);

		assertThat(annotationsAdapter).isNotNull();
		assertThat(testInstance.server).isNotNull().isSameAs(serverAdapter.getServer());
		assertThat(testInstance.configuration).isNotNull().isSameAs(serverAdapter.getServer().getConfiguration());
		assertThat(testInstance.client).isNotNull();
	}

	@Test
	void it_should_reset_injected_http_client_after_each_test() {
		JunitServerExtension extension = new JunitServerExtension();
		FixtureClass testInstance = new FixtureClass();
		FakeJunitExtensionContext context = new FakeJunitExtensionContext(testInstance);

		extension.beforeAll(context);
		extension.beforeEach(context);

		HttpClient client = testInstance.client;
		FakeJunitStore store = context.getSingleStore();

		extension.afterEach(context);

		assertThat(store.get("annotationsAdapter")).isNull();
		assertThat(testInstance.client).isNull();
		assertThat(client.isDestroyed()).isTrue();
	}

	@Test
	void it_should_support_resolution_of_embedded_server_parameter() throws Exception {
		verifySupportsParameter("method_server", EmbeddedServer.class);
	}

	@Test
	void it_should_support_resolution_of_configuration_parameter() throws Exception {
		verifySupportsParameter("method_configuration", AbstractConfiguration.class);
	}

	@Test
	void it_should_support_resolution_of_http_client() throws Exception {
		verifySupportsParameter("method_http_client", HttpClient.class);
	}

	@Test
	void it_should_support_resolution_of_extended_parameter() throws Exception {
		verifySupportsParameter("method_with_fake_embedded_configuration", FakeEmbeddedServerConfiguration.class);
	}

	private void verifySupportsParameter(String methodName, Class<?> klass) throws Exception {
		JunitServerExtension extension = new JunitServerExtension();
		FixtureClass testInstance = new FixtureClass();
		FakeJunitExtensionContext context = new FakeJunitExtensionContext(testInstance);

		extension.beforeAll(context);

		Method method = getClass().getMethod(methodName, klass);
		Parameter parameter = method.getParameters()[0];
		ParameterContext parameterContext = new FakeJunitParameterContext(parameter);

		assertThat(extension.supportsParameter(parameterContext, context)).isTrue();
	}

	@Test
	void it_should_resolve_embedded_server_parameter() throws Exception {
		FixtureClass testInstance = new FixtureClass();
		FakeJunitExtensionContext context = new FakeJunitExtensionContext(testInstance);
		JunitServerExtension extension = initializeExtension(context);
		FakeJunitStore store = context.getSingleStore();
		EmbeddedServerRunner serverAdapter = store.get("serverAdapter", EmbeddedServerRunner.class);
		ParameterContext parameterContext = createParameterContext("method_server", EmbeddedServer.class);

		Object result = extension.resolveParameter(parameterContext, context);

		assertThat(result).isNotNull().isSameAs(serverAdapter.getServer());
	}

	@Test
	void it_should_resolve_configuration_parameter() throws Exception {
		FixtureClass testInstance = new FixtureClass();
		FakeJunitExtensionContext context = new FakeJunitExtensionContext(testInstance);
		JunitServerExtension extension = initializeExtension(context);
		FakeJunitStore store = context.getSingleStore();
		EmbeddedServerRunner serverAdapter = store.get("serverAdapter", EmbeddedServerRunner.class);
		ParameterContext parameterContext = createParameterContext("method_configuration", AbstractConfiguration.class);

		Object result = extension.resolveParameter(parameterContext, context);

		assertThat(result).isNotNull().isSameAs(serverAdapter.getServer().getConfiguration());
	}

	@Test
	void it_should_resolve_http_client_parameter_with_auto_strategy() throws Exception {
		FixtureClass testInstance = new FixtureClass();
		FakeJunitExtensionContext context = new FakeJunitExtensionContext(testInstance);
		JunitServerExtension extension = initializeExtension(context);
		ParameterContext parameterContext = createParameterContext("method_http_client", HttpClient.class);

		Object result = extension.resolveParameter(parameterContext, context);

		assertThat(result).isNotNull().isInstanceOf(HttpClient.class);
	}

	@Test
	void it_should_resolve_http_client_parameter_with_custom_strategy() throws Exception {
		FixtureClass testInstance = new FixtureClass();
		FakeJunitExtensionContext context = new FakeJunitExtensionContext(testInstance);
		JunitServerExtension extension = initializeExtension(context);
		ParameterContext parameterContext = createParameterContext("method_http_client_custom", HttpClient.class);

		Object result = extension.resolveParameter(parameterContext, context);

		assertThat(result).isNotNull().isExactlyInstanceOf(NingAsyncHttpClient.class);
	}

	@Test
	void it_should_resolve_extended_parameter() throws Exception {
		FixtureClass testInstance = new FixtureClass();
		FakeJunitExtensionContext context = new FakeJunitExtensionContext(testInstance);
		JunitServerExtension extension = initializeExtension(context);
		ParameterContext parameterContext = createParameterContext("method_with_fake_embedded_configuration", FakeEmbeddedServerConfiguration.class);

		Object result = extension.resolveParameter(parameterContext, context);

		assertThat(result).isNotNull().isExactlyInstanceOf(FakeEmbeddedServerConfiguration.class);
	}

	private JunitServerExtension initializeExtension(FakeJunitExtensionContext context) {
		JunitServerExtension extension = new JunitServerExtension();
		extension.beforeAll(context);
		return extension;
	}

	private ParameterContext createParameterContext(String methodName, Class<?> klass) throws Exception {
		Method method = getClass().getMethod(methodName, klass);
		Parameter parameter = method.getParameters()[0];
		return new FakeJunitParameterContext(parameter);
	}

	public void method_server(EmbeddedServer<?> server) {
	}

	public void method_configuration(AbstractConfiguration configuration) {
	}

	public void method_with_fake_embedded_configuration(FakeEmbeddedServerConfiguration configuration) {
	}

	public void method_http_client(HttpClient client) {
	}

	public void method_http_client_custom(@TestHttpClient(strategy = NING_ASYNC_HTTP_CLIENT) HttpClient client) {
	}
}
