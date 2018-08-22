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

package com.github.mjeanroy.junit.servers.engine;

import com.github.mjeanroy.junit.servers.annotations.TestServerConfiguration;
import com.github.mjeanroy.junit.servers.engine.Servers;
import com.github.mjeanroy.junit.servers.servers.AbstractConfiguration;
import com.github.mjeanroy.junit.servers.servers.EmbeddedServer;
import com.github.mjeanroy.junit.servers.utils.impl.FakeEmbeddedServer;
import com.github.mjeanroy.junit.servers.utils.impl.FakeEmbeddedServerConfiguration;
import com.github.mjeanroy.junit.servers.utils.impl.FakeEmbeddedServerConfigurationBuilder;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ServersTest {

	@Test
	public void it_should_instantiate_server_using_service_loader() {
		final EmbeddedServer<?> server = Servers.instantiate(FixtureClass.class);
		assertThat(server).isNotNull().isExactlyInstanceOf(FakeEmbeddedServer.class);
		assertThat(server.getConfiguration()).isNotNull();
	}

	@Test
	public void it_should_instantiate_server_using_service_loader_with_custom_configuration() {
		final EmbeddedServer<?> server = Servers.instantiate(FixtureClassWithConfiguration.class);
		assertThat(server).isNotNull().isExactlyInstanceOf(FakeEmbeddedServer.class);
		assertThat(server.getConfiguration()).isNotNull().isSameAs(FixtureClassWithConfiguration.configuration);
	}

	@Test
	public void it_should_instantiate_server_with_configuration() {
		final FakeEmbeddedServerConfiguration configuration = new FakeEmbeddedServerConfigurationBuilder().build();
		final EmbeddedServer<?> server = Servers.instantiate(configuration);
		assertThat(server).isNotNull().isExactlyInstanceOf(FakeEmbeddedServer.class);
		assertThat(server.getConfiguration()).isNotNull().isSameAs(configuration);
	}

	@Test
	public void it_should_return_null_without_configuration_field() {
		final AbstractConfiguration configuration = Servers.findConfiguration(FixtureClass.class);
		assertThat(configuration).isNull();
	}

	@Test
	public void it_should_return_configuration_as_result_of_method() {
		final AbstractConfiguration configuration = Servers.findConfiguration(FixtureClassWithConfigurationAsMethod.class);
		assertThat(configuration).isNotNull();
	}

	private static class FixtureClass {
	}

	private static class FixtureClassWithConfiguration {
		@TestServerConfiguration
		private static FakeEmbeddedServerConfiguration configuration = new FakeEmbeddedServerConfigurationBuilder().build();
	}

	private static class FixtureClassWithConfigurationAsMethod {
		@TestServerConfiguration
		private static FakeEmbeddedServerConfiguration configuration() {
			return new FakeEmbeddedServerConfigurationBuilder().build();
		}
	}
}
