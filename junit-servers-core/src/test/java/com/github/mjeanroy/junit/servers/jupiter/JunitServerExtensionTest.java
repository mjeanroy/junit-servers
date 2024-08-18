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
import com.github.mjeanroy.junit.servers.annotations.TestServer;
import com.github.mjeanroy.junit.servers.annotations.TestServerConfiguration;
import com.github.mjeanroy.junit.servers.client.HttpClient;
import com.github.mjeanroy.junit.servers.client.impl.ning.NingAsyncHttpClient;
import com.github.mjeanroy.junit.servers.servers.AbstractConfiguration;
import com.github.mjeanroy.junit.servers.servers.EmbeddedServer;
import com.github.mjeanroy.junit.servers.utils.impl.FakeEmbeddedServer;
import com.github.mjeanroy.junit.servers.utils.impl.FakeEmbeddedServerConfiguration;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;

import static com.github.mjeanroy.junit.servers.client.HttpClientStrategy.NING_ASYNC_HTTP_CLIENT;
import static com.github.mjeanroy.junit.servers.testing.JupiterExtensionTesting.runTests;
import static org.assertj.core.api.Assertions.assertThat;

class JunitServerExtensionTest {
	@BeforeEach
	void setUp() {
		FakeEmbeddedServer.servers.clear();
	}

	@Test
	void it_should_initialize_extension_with_given_configuration_and_start_given_server_before_all_tests() {
		runTests(
			ItShouldInitializeExtensionWithGivenConfigurationAndStartGivenServerBeforeAllTests.class
		);

		assertThat(FakeEmbeddedServer.servers).hasSize(1)
			.are(new Condition<>(srv -> !srv.isStarted(), "isStopped"))
			.are(new Condition<>(srv -> srv.getNbStart() == 1, "started once"))
			.are(new Condition<>(srv -> srv.getNbStop() == 1, "stopped once"));
	}

	@Test
	void it_should_initialize_extension_with_given_server_and_start_given_server_before_all_tests() {
		runTests(
			ItShouldInitializeExtensionWithGivenServerAndStartGivenServerBeforeAllTests.class
		);

		assertThat(FakeEmbeddedServer.servers).hasSize(1)
			.are(new Condition<>(srv -> !srv.isStarted(), "isStopped"))
			.are(new Condition<>(srv -> srv.getNbStart() == 1, "started once"))
			.are(new Condition<>(srv -> srv.getNbStop() == 1, "stopped once"));
	}

	@Test
	void it_should_start_server_before_each_tests() {
		runTests(
			ItShouldStartServerBeforeEachTests.class
		);

		assertThat(FakeEmbeddedServer.servers).hasSize(2)
			.are(new Condition<>(srv -> !srv.isStarted(), "isStopped"))
			.are(new Condition<>(srv -> srv.getNbStart() == 1, "started once"))
			.are(new Condition<>(srv -> srv.getNbStop() == 1, "stopped once"));
	}

	@Test
	void it_should_inject_annotated_field_before_each_test() {
		runTests(
			ItShouldInjectAnnotatedFieldBeforeEachTest.class
		);
	}

	@Test
	void it_should_resolve_parameters() {
		runTests(
			ItShouldSupportResolutionOfParameters.class
		);
	}

	@SuppressWarnings("JUnitMalformedDeclaration")
	static class ItShouldInitializeExtensionWithGivenConfigurationAndStartGivenServerBeforeAllTests {
		private static final AbstractConfiguration configuration = new FakeEmbeddedServerConfiguration();

		@RegisterExtension
		static JunitServerExtension extension = new JunitServerExtension(configuration);

		@BeforeAll
		static void beforeAll(EmbeddedServer<?> server) {
			verifyEmbeddedServerState(server);
		}

		@AfterAll
		static void afterAll(EmbeddedServer<?> server) {
			verifyEmbeddedServerState(server);
		}

		@BeforeEach
		void beforeEach(EmbeddedServer<?> server) {
			verifyEmbeddedServerState(server);
		}

		@AfterEach
		void afterEach(EmbeddedServer<?> server) {
			verifyEmbeddedServerState(server);
		}

		@Test
		void test1(EmbeddedServer<?> server) {
			verifyEmbeddedServerState(server);
		}

		@Test
		void test2(EmbeddedServer<?> server) {
			verifyEmbeddedServerState(server);
		}

		private static void verifyEmbeddedServerState(EmbeddedServer<?> server) {
			assertThat(server).isInstanceOf(FakeEmbeddedServer.class);
			assertThat(server.isStarted()).isTrue();
			assertThat(server.getConfiguration()).isSameAs(configuration);
			assertThat(((FakeEmbeddedServer) server).getNbStart()).isOne();
			assertThat(((FakeEmbeddedServer) server).getNbStop()).isZero();
		}
	}

	@SuppressWarnings("JUnitMalformedDeclaration")
	static class ItShouldInitializeExtensionWithGivenServerAndStartGivenServerBeforeAllTests {
		private static final EmbeddedServer<?> server = new FakeEmbeddedServer();

		@RegisterExtension
		static JunitServerExtension extension = new JunitServerExtension(server);

		@BeforeAll
		static void beforeAll(EmbeddedServer<?> server) {
			verifyEmbeddedServerState(server);
		}

		@AfterAll
		static void afterAll(EmbeddedServer<?> server) {
			verifyEmbeddedServerState(server);
		}

		@BeforeEach
		void beforeEach(EmbeddedServer<?> server) {
			verifyEmbeddedServerState(server);
		}

		@AfterEach
		void afterEach(EmbeddedServer<?> server) {
			verifyEmbeddedServerState(server);
		}

		@Test
		void test1(EmbeddedServer<?> server) {
			verifyEmbeddedServerState(server);
		}

		@Test
		void test2(EmbeddedServer<?> server) {
			verifyEmbeddedServerState(server);
		}

		private static void verifyEmbeddedServerState(EmbeddedServer<?> testServer) {
			assertThat(testServer).isInstanceOf(FakeEmbeddedServer.class);
			assertThat(testServer).isSameAs(server);
			assertThat(testServer.isStarted()).isTrue();
			assertThat(((FakeEmbeddedServer) testServer).getNbStart()).isOne();
			assertThat(((FakeEmbeddedServer) testServer).getNbStop()).isZero();
		}
	}

	@SuppressWarnings("JUnitMalformedDeclaration")
	static class ItShouldStartServerBeforeEachTests {
		@RegisterExtension
		JunitServerExtension extension = new JunitServerExtension();

		@BeforeEach
		void beforeEach(EmbeddedServer<?> server) {
			verifyEmbeddedServerState(server);
		}

		@AfterEach
		void afterEach(EmbeddedServer<?> server) {
			verifyEmbeddedServerState(server);
		}

		@Test
		void test1(EmbeddedServer<?> server) {
			verifyEmbeddedServerState(server);
		}

		@Test
		void test2(EmbeddedServer<?> server) {
			verifyEmbeddedServerState(server);
		}

		private static void verifyEmbeddedServerState(EmbeddedServer<?> testServer) {
			assertThat(testServer).isInstanceOf(FakeEmbeddedServer.class);
			assertThat(testServer.isStarted()).isTrue();
			assertThat(((FakeEmbeddedServer) testServer).getNbStart()).isOne();
			assertThat(((FakeEmbeddedServer) testServer).getNbStop()).isZero();
		}
	}

	@SuppressWarnings("JUnitMalformedDeclaration")
	@ExtendWith(JunitServerExtension.class)
	static class ItShouldInjectAnnotatedFieldBeforeEachTest {
		@TestServer
		private FakeEmbeddedServer server;

		@TestServerConfiguration
		private FakeEmbeddedServerConfiguration configuration;

		@TestHttpClient
		private HttpClient httpClient;

		@BeforeEach
		void setUp() {
			verifyAnnotatedFields();
		}

		@Test
		void test1() {
			verifyAnnotatedFields();
		}

		private void verifyAnnotatedFields() {
			assertThat(server).isNotNull();
			assertThat(configuration).isNotNull().isSameAs(server.getConfiguration());
			assertThat(httpClient).isNotNull();
		}
	}

	@SuppressWarnings({"JUnitMalformedDeclaration", "NewClassNamingConvention"})
	@ExtendWith(JunitServerExtension.class)
	static class ItShouldSupportResolutionOfParameters {
		@Test
		void test_embedded_server(FakeEmbeddedServer server) {
			assertThat(server).isNotNull();
		}

		@Test
		void test_embedded_server_configuration(FakeEmbeddedServerConfiguration configuration) {
			assertThat(configuration).isNotNull();
		}

		@Test
		void test_http_client(HttpClient httpClient) {
			assertThat(httpClient).isNotNull();
		}

		@Test
		void test_custom_http_client(@TestHttpClient(strategy = NING_ASYNC_HTTP_CLIENT) HttpClient httpClient) {
			assertThat(httpClient).isExactlyInstanceOf(NingAsyncHttpClient.class);
		}
	}
}
