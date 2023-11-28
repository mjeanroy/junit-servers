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

package com.github.mjeanroy.junit.servers.jetty11.jupiter;

import com.github.mjeanroy.junit.servers.engine.EmbeddedServerRunner;
import com.github.mjeanroy.junit.servers.jetty11.EmbeddedJetty;
import com.github.mjeanroy.junit.servers.jetty11.tests.EmbeddedJettyMockBuilder;
import com.github.mjeanroy.junit.servers.jetty.EmbeddedJettyConfiguration;
import com.github.mjeanroy.junit.servers.testing.FakeJunitExtensionContext;
import com.github.mjeanroy.junit.servers.testing.FakeJunitStore;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JettyServerExtensionTest {

	@Test
	void it_should_start_given_jetty_server_before_all_tests() {
		EmbeddedJetty jetty = new EmbeddedJettyMockBuilder().build();
		JettyServerExtension extension = new JettyServerExtension(jetty);
		FixtureClass testInstance = new FixtureClass();
		FakeJunitExtensionContext context = new FakeJunitExtensionContext(testInstance);

		extension.beforeAll(context);

		FakeJunitStore store = context.getSingleStore();
		EmbeddedServerRunner serverAdapter = store.get("serverAdapter", EmbeddedServerRunner.class);

		assertThat(serverAdapter).isNotNull();
		assertThat(serverAdapter.getServer()).isSameAs(jetty);
		assertThat(serverAdapter.getServer().isStarted()).isTrue();
	}

	@Test
	void it_should_start_jetty_server_using_given_configuration_before_all_tests() {
		EmbeddedJettyConfiguration configuration = EmbeddedJettyConfiguration.defaultConfiguration();
		JettyServerExtension extension = new JettyServerExtension(configuration);
		FixtureClass testInstance = new FixtureClass();
		FakeJunitExtensionContext context = new FakeJunitExtensionContext(testInstance);

		extension.beforeAll(context);

		FakeJunitStore store = context.getSingleStore();
		EmbeddedServerRunner serverAdapter = store.get("serverAdapter", EmbeddedServerRunner.class);

		assertThat(serverAdapter).isNotNull();
		assertThat(serverAdapter.getServer()).isNotNull().isExactlyInstanceOf(EmbeddedJetty.class);
		assertThat(serverAdapter.getServer().getConfiguration()).isSameAs(configuration);
		assertThat(serverAdapter.getServer().isStarted()).isTrue();
	}

	@Test
	void it_should_start_server_with_default_configuration_before_all_tests() {
		JettyServerExtension extension = new JettyServerExtension();
		FixtureClass testInstance = new FixtureClass();
		FakeJunitExtensionContext context = new FakeJunitExtensionContext(testInstance);

		extension.beforeAll(context);

		FakeJunitStore store = context.getSingleStore();
		EmbeddedServerRunner serverAdapter = store.get("serverAdapter", EmbeddedServerRunner.class);

		assertThat(serverAdapter).isNotNull();
		assertThat(serverAdapter.getServer()).isNotNull().isExactlyInstanceOf(EmbeddedJetty.class);
		assertThat(serverAdapter.getServer().isStarted()).isTrue();
	}
}
