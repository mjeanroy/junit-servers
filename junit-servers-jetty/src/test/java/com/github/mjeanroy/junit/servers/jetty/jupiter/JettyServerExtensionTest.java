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

package com.github.mjeanroy.junit.servers.jetty.jupiter;

import com.github.mjeanroy.junit.servers.engine.EmbeddedServerTestAdapter;
import com.github.mjeanroy.junit.servers.jetty.EmbeddedJetty;
import com.github.mjeanroy.junit.servers.jetty.EmbeddedJettyConfiguration;
import com.github.mjeanroy.junit.servers.jetty.tests.EmbeddedJettyMockBuilder;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class JettyServerExtensionTest {

	@Test
	public void it_should_start_given_jetty_server_before_all_tests() {
		final EmbeddedJetty jetty = new EmbeddedJettyMockBuilder().build();
		final JettyServerExtension extension = new JettyServerExtension(jetty);
		final FixtureClass testInstance = new FixtureClass();
		final FakeExtensionContext context = new FakeExtensionContext(testInstance);

		extension.beforeAll(context);

		final FakeStore store = context.getSingleStore();
		final EmbeddedServerTestAdapter serverAdapter = store.get("serverAdapter", EmbeddedServerTestAdapter.class);

		assertThat(serverAdapter).isNotNull();
		assertThat(serverAdapter.getServer()).isSameAs(jetty);
		assertThat(serverAdapter.getServer().isStarted()).isTrue();
	}

	@Test
	public void it_should_start_jetty_server_using_given_configuration_before_all_tests() {
		final EmbeddedJettyConfiguration configuration = EmbeddedJettyConfiguration.defaultConfiguration();
		final JettyServerExtension extension = new JettyServerExtension(configuration);
		final FixtureClass testInstance = new FixtureClass();
		final FakeExtensionContext context = new FakeExtensionContext(testInstance);

		extension.beforeAll(context);

		final FakeStore store = context.getSingleStore();
		final EmbeddedServerTestAdapter serverAdapter = store.get("serverAdapter", EmbeddedServerTestAdapter.class);

		assertThat(serverAdapter).isNotNull();
		assertThat(serverAdapter.getServer()).isNotNull().isExactlyInstanceOf(EmbeddedJetty.class);
		assertThat(serverAdapter.getServer().getConfiguration()).isSameAs(configuration);
		assertThat(serverAdapter.getServer().isStarted()).isTrue();
	}

	@Test
	public void it_should_start_server_with_default_configuration_before_all_tests() {
		final JettyServerExtension extension = new JettyServerExtension();
		final FixtureClass testInstance = new FixtureClass();
		final FakeExtensionContext context = new FakeExtensionContext(testInstance);

		extension.beforeAll(context);

		final FakeStore store = context.getSingleStore();
		final EmbeddedServerTestAdapter serverAdapter = store.get("serverAdapter", EmbeddedServerTestAdapter.class);

		assertThat(serverAdapter).isNotNull();
		assertThat(serverAdapter.getServer()).isNotNull().isExactlyInstanceOf(EmbeddedJetty.class);
		assertThat(serverAdapter.getServer().isStarted()).isTrue();
	}
}
