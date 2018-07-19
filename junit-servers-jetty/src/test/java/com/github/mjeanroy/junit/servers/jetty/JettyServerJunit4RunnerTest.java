/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2017 <mickael.jeanroy@gmail.com>
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

package com.github.mjeanroy.junit.servers.jetty;

import com.github.mjeanroy.junit.servers.annotations.TestServer;
import com.github.mjeanroy.junit.servers.annotations.TestServerConfiguration;
import com.github.mjeanroy.junit.servers.servers.EmbeddedServer;
import com.github.mjeanroy.junit.servers.servers.configuration.AbstractConfiguration;
import org.junit.Test;

import static com.github.mjeanroy.junit.servers.jetty.EmbeddedJettyConfiguration.defaultConfiguration;
import static org.apache.commons.lang3.reflect.FieldUtils.readField;
import static org.assertj.core.api.Assertions.assertThat;

public class JettyServerJunit4RunnerTest {

	private static final EmbeddedJettyConfiguration configuration = defaultConfiguration();

	@Test
	public void it_should_instantiate_jetty_with_default_configuration() throws Exception {
		JettyServerJunit4Runner runner = new JettyServerJunit4Runner(Foo.class);

		EmbeddedServer<?> server = (EmbeddedServer<?>) readField(runner, "server", true);
		assertThat(server)
				.isNotNull()
				.isInstanceOf(EmbeddedJetty.class);

		AbstractConfiguration conf = (AbstractConfiguration) readField(runner, "configuration", true);
		assertThat(conf)
				.isNotNull()
				.isInstanceOf(EmbeddedJettyConfiguration.class)
				.isNotSameAs(configuration);
	}

	@Test
	public void it_should_instantiate_jetty_with_configuration() throws Exception {
		JettyServerJunit4Runner runner = new JettyServerJunit4Runner(Bar.class);

		EmbeddedServer<?> server = (EmbeddedServer<?>) readField(runner, "server", true);
		assertThat(server)
				.isNotNull()
				.isInstanceOf(EmbeddedJetty.class);

		AbstractConfiguration conf = (AbstractConfiguration) readField(runner, "configuration", true);
		assertThat(conf)
				.isNotNull()
				.isInstanceOf(EmbeddedJettyConfiguration.class)
				.isSameAs(configuration);
	}

	public static class Foo {
		@TestServer
		private static EmbeddedServer<?> server;

		@TestServerConfiguration
		private static EmbeddedJettyConfiguration configuration;

		public Foo() {
		}

		@Test
		public void fooTest() {
		}
	}

	public static class Bar {

		@TestServer
		private static EmbeddedServer<?> server;

		@TestServerConfiguration
		private static EmbeddedJettyConfiguration initConfiguration() {
			return configuration;
		}

		public Bar() {
		}

		@Test
		public void fooTest() {
		}
	}
}
