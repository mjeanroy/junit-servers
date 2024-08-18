/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2022 <mickael.jeanroy@gmail.com>
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

package com.github.mjeanroy.junit.servers.jetty12.jupiter;

import com.github.mjeanroy.junit.servers.jetty.EmbeddedJettyConfiguration;
import com.github.mjeanroy.junit.servers.jetty12.EmbeddedJetty;
import com.github.mjeanroy.junit.servers.jetty12.tests.EmbeddedJettyMockBuilder;
import com.github.mjeanroy.junit.servers.jupiter.JunitServerExtension;
import com.github.mjeanroy.junit.servers.servers.EmbeddedServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static com.github.mjeanroy.junit.servers.testing.JupiterExtensionTesting.runTests;
import static org.assertj.core.api.Assertions.assertThat;

class JettyServerExtensionTest {

	@Test
	void it_should_start_given_jetty_server_before_all_tests() {
		runTests(
			ItShouldStartGivenJettyServerBeforeAllTests.class
		);
	}

	@Test
	void it_should_initialize_extension_with_given_configuration_and_start_given_server_before_all_tests() {
		runTests(
			ItShouldInitializeExtensionWithGivenConfigurationAndStartGivenServerBeforeAllTests.class
		);
	}

	@Test
	void it_should_start_server_with_default_configuration_before_all_tests() {
		runTests(
			ItShouldStartServerWithDefaultConfigurationBeforeAllTests.class
		);
	}

	@SuppressWarnings("JUnitMalformedDeclaration")
	static class ItShouldStartGivenJettyServerBeforeAllTests {
		private static final EmbeddedJetty server = new EmbeddedJettyMockBuilder().build();

		@RegisterExtension
		static JunitServerExtension extension = new JettyServerExtension(server);

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
			assertThat(testServer).isSameAs(server);
			assertThat(testServer.isStarted()).isTrue();
		}
	}

	@SuppressWarnings("JUnitMalformedDeclaration")
	static class ItShouldInitializeExtensionWithGivenConfigurationAndStartGivenServerBeforeAllTests {
		private static final EmbeddedJettyConfiguration configuration = EmbeddedJettyConfiguration.defaultConfiguration();

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

		private static void verifyEmbeddedServerState(EmbeddedServer<?> testServer) {
			assertThat(testServer).isExactlyInstanceOf(EmbeddedJetty.class);
			assertThat(testServer.isStarted()).isTrue();
			assertThat(testServer.getConfiguration()).isSameAs(configuration);
		}
	}

	@SuppressWarnings("JUnitMalformedDeclaration")
	@JettyTest
	static class ItShouldStartServerWithDefaultConfigurationBeforeAllTests {
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
			assertThat(testServer).isExactlyInstanceOf(EmbeddedJetty.class);
			assertThat(testServer.isStarted()).isTrue();
		}
	}
}
