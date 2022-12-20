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

package com.github.mjeanroy.junit.servers.tomcat10.jupiter;

import com.github.mjeanroy.junit.servers.engine.EmbeddedServerRunner;
import com.github.mjeanroy.junit.servers.tomcat.EmbeddedTomcatConfiguration;
import com.github.mjeanroy.junit.servers.tomcat10.EmbeddedTomcat;
import com.github.mjeanroy.junit.servers.tomcat10.tests.builders.EmbeddedTomcatMockBuilder;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TomcatServerExtensionTest {

	@Test
	void it_should_start_given_tomcat_server_before_all_tests() {
		final EmbeddedTomcat tomcat = new EmbeddedTomcatMockBuilder().build();
		final TomcatServerExtension extension = new TomcatServerExtension(tomcat);
		final FixtureClass testInstance = new FixtureClass();
		final FakeExtensionContext context = new FakeExtensionContext(testInstance);

		extension.beforeAll(context);

		final FakeStore store = context.getSingleStore();
		final EmbeddedServerRunner serverAdapter = store.get("serverAdapter", EmbeddedServerRunner.class);

		assertThat(serverAdapter).isNotNull();
		assertThat(serverAdapter.getServer()).isSameAs(tomcat);
		assertThat(serverAdapter.getServer().isStarted()).isTrue();
	}

	@Test
	void it_should_start_tomcat_server_using_given_configuration_before_all_tests() {
		final EmbeddedTomcatConfiguration configuration = EmbeddedTomcatConfiguration.defaultConfiguration();
		final TomcatServerExtension extension = new TomcatServerExtension(configuration);
		final FixtureClass testInstance = new FixtureClass();
		final FakeExtensionContext context = new FakeExtensionContext(testInstance);

		extension.beforeAll(context);

		final FakeStore store = context.getSingleStore();
		final EmbeddedServerRunner serverAdapter = store.get("serverAdapter", EmbeddedServerRunner.class);

		assertThat(serverAdapter).isNotNull();
		assertThat(serverAdapter.getServer()).isNotNull().isExactlyInstanceOf(EmbeddedTomcat.class);
		assertThat(serverAdapter.getServer().getConfiguration()).isSameAs(configuration);
		assertThat(serverAdapter.getServer().isStarted()).isTrue();
	}

	@Test
	void it_should_start_server_with_default_configuration_before_all_tests() {
		final TomcatServerExtension extension = new TomcatServerExtension();
		final FixtureClass testInstance = new FixtureClass();
		final FakeExtensionContext context = new FakeExtensionContext(testInstance);

		extension.beforeAll(context);

		final FakeStore store = context.getSingleStore();
		final EmbeddedServerRunner serverAdapter = store.get("serverAdapter", EmbeddedServerRunner.class);

		assertThat(serverAdapter).isNotNull();
		assertThat(serverAdapter.getServer()).isNotNull().isExactlyInstanceOf(EmbeddedTomcat.class);
		assertThat(serverAdapter.getServer().isStarted()).isTrue();
	}
}
