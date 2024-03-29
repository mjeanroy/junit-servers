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

package com.github.mjeanroy.junit.servers.tomcat.junit4;

import com.github.mjeanroy.junit.servers.annotations.TestServer;
import com.github.mjeanroy.junit.servers.annotations.TestServerConfiguration;
import com.github.mjeanroy.junit.servers.servers.AbstractConfiguration;
import com.github.mjeanroy.junit.servers.servers.EmbeddedServer;
import com.github.mjeanroy.junit.servers.tomcat.EmbeddedTomcatConfiguration;
import com.github.mjeanroy.junit.servers.tomcat.EmbeddedTomcat;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;

import static com.github.mjeanroy.junit.servers.tomcat.EmbeddedTomcatConfiguration.defaultConfiguration;
import static org.apache.commons.lang3.reflect.FieldUtils.readField;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("deprecation")
class TomcatServerJunit4RunnerTest {

	private static final EmbeddedTomcatConfiguration configuration = defaultConfiguration();

	@Test
	void it_should_instantiate_tomcat_with_default_configuration() throws Exception {
		TomcatServerJunit4Runner runner = createRunner(TestClassWithInjectedConfiguration.class);
		EmbeddedServer<?> server = (EmbeddedServer<?>) readField(runner, "server", true);
		AbstractConfiguration conf = (AbstractConfiguration) readField(runner, "configuration", true);

		assertThat(server).isInstanceOf(EmbeddedTomcat.class);
		assertThat(conf).isInstanceOf(EmbeddedTomcatConfiguration.class).isNotSameAs(configuration);
	}

	@Test
	void it_should_instantiate_tomcat_with_configuration() throws Exception {
		TomcatServerJunit4Runner runner = createRunner(TestClassWithConfigurationInitializer.class);
		EmbeddedServer<?> server = (EmbeddedServer<?>) readField(runner, "server", true);
		AbstractConfiguration conf = (AbstractConfiguration) readField(runner, "configuration", true);

		assertThat(server).isInstanceOf(EmbeddedTomcat.class);
		assertThat(conf).isInstanceOf(EmbeddedTomcatConfiguration.class).isSameAs(configuration);
	}

	private static TomcatServerJunit4Runner createRunner(Class<?> klass) throws Exception {
		return new TomcatServerJunit4Runner(klass);
	}

	@Ignore
	public static class TestClassWithInjectedConfiguration {

		@TestServer
		private static EmbeddedServer<?> server;

		@TestServerConfiguration
		private static EmbeddedTomcatConfiguration configuration;

		public TestClassWithInjectedConfiguration() {
		}

		@org.junit.Test
		public void fooTest() {
		}
	}

	@Ignore
	public static class TestClassWithConfigurationInitializer {

		@TestServer
		private static EmbeddedServer<?> server;

		@TestServerConfiguration
		private static EmbeddedTomcatConfiguration initConfiguration() {
			return configuration;
		}

		public TestClassWithConfigurationInitializer() {
		}

		@org.junit.Test
		public void fooTest() {
		}
	}
}
