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

package com.github.mjeanroy.junit.servers.junit4;

import com.github.mjeanroy.junit.servers.annotations.TestServer;
import com.github.mjeanroy.junit.servers.annotations.TestServerConfiguration;
import com.github.mjeanroy.junit.servers.jetty.EmbeddedJetty;
import com.github.mjeanroy.junit.servers.jetty.EmbeddedJettyConfiguration;
import com.github.mjeanroy.junit.servers.servers.AbstractConfiguration;
import com.github.mjeanroy.junit.servers.servers.EmbeddedServer;
import org.junit.Test;

import static com.github.mjeanroy.junit.servers.jetty.EmbeddedJettyConfiguration.defaultConfiguration;
import static org.apache.commons.lang3.reflect.FieldUtils.readField;
import static org.assertj.core.api.Assertions.assertThat;

public class JunitServerRunnerTest {

	private static final EmbeddedJettyConfiguration configuration = defaultConfiguration();

	@Test
	public void it_should_instantiate_jetty_with_configuration() throws Exception {
		JunitServerRunner runner = new JunitServerRunner(TestClassWithConfigurationMethod.class);

		EmbeddedServer<?> server = (EmbeddedServer<?>) readField(runner, "server", true);
		assertThat(server).isInstanceOf(EmbeddedJetty.class);

		AbstractConfiguration conf = (AbstractConfiguration) readField(runner, "configuration", true);
		assertThat(conf).isSameAs(configuration);
	}

	@Test
	public void it_should_instantiate_jetty_with_default_configuration() throws Exception {
		JunitServerRunner runner = new JunitServerRunner(TestClassWithInjectedConfiguration.class);

		EmbeddedServer<?> server = (EmbeddedServer<?>) readField(runner, "server", true);
		assertThat(server).isInstanceOf(EmbeddedJetty.class);

		AbstractConfiguration conf = (AbstractConfiguration) readField(runner, "configuration", true);
		assertThat(conf).isNotSameAs(configuration).isEqualTo(configuration);
	}

	public static class TestClassWithInjectedConfiguration {
		@TestServer
		private static EmbeddedServer<?> server;

		@TestServerConfiguration
		private static EmbeddedJettyConfiguration configuration;

		public TestClassWithInjectedConfiguration() {
		}

		@Test
		public void fooTest() {
		}
	}

	public static class TestClassWithConfigurationMethod {
		@TestServer
		private static EmbeddedServer<?> server;

		@TestServerConfiguration
		private static EmbeddedJettyConfiguration initConfiguration() {
			return configuration;
		}

		public TestClassWithConfigurationMethod() {
		}

		@Test
		public void fooTest() {
		}
	}
}
