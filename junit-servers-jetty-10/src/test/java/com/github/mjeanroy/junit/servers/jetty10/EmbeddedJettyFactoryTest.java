/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2026 <mickael.jeanroy@gmail.com>
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

package com.github.mjeanroy.junit.servers.jetty10;

import com.github.mjeanroy.junit.servers.annotations.TestServerConfiguration;
import com.github.mjeanroy.junit.servers.jetty.JettyConfiguration;
import com.github.mjeanroy.junit.servers.servers.AbstractConfiguration;
import com.github.mjeanroy.junit.servers.jetty.EmbeddedJettyConfiguration;
import com.github.mjeanroy.junit.servers.jetty.EmbeddedJettyConfigurationProvider;
import com.github.mjeanroy.junit.servers.jetty.IllegalJettyConfigurationException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EmbeddedJettyFactoryTest {

	@Test
	void it_should_create_embedded_jetty_from_class_using_default_configuration() {
		EmbeddedJetty jetty = EmbeddedJettyFactory.createFrom(ClassUsingDefaultConfiguration.class);
		assertThat(jetty).isNotNull();
		assertThat(jetty.getConfiguration()).isEqualTo(EmbeddedJettyConfiguration.defaultConfiguration());
	}

	@Test
	void it_should_create_embedded_jetty_from_class_using_provided_configuration() {
		EmbeddedJettyConfiguration providedConfiguration = EmbeddedJettyConfiguration.builder().withPath("/test").build();
		EmbeddedJetty jetty = EmbeddedJettyFactory.createFrom(ClassUsingDefaultConfiguration.class, providedConfiguration);

		assertThat(jetty).isNotNull();
		assertThat(jetty.getConfiguration()).isEqualTo(providedConfiguration);
	}

	@Test
	void it_should_create_embedded_tomcat_from_class_using_configuration_provider() {
		EmbeddedJetty jetty = EmbeddedJettyFactory.createFrom(ClassAnnotatedWithConfigurationProvider.class);
		assertThat(jetty).isNotNull();
		assertThat(jetty.getConfiguration()).isSameAs(DefaultEmbeddedJettyConfigurationProvider.CONFIGURATION);
	}

	@Test
	void it_should_create_embedded_jetty_from_class_using_custom_configuration() {
		EmbeddedJetty jetty = EmbeddedJettyFactory.createFrom(ClassUsingCustomConfiguration.class);
		assertThat(jetty).isNotNull();
		assertThat(jetty.getConfiguration()).isSameAs(ClassUsingCustomConfiguration.configuration);
	}

	@Test
	void it_should_fail_to_create_embedded_jetty_from_class_using_non_valid_configuration() {
		assertThatThrownBy(() -> EmbeddedJettyFactory.createFrom(ClassUsingNonJettyConfiguration.class))
			.isInstanceOf(IllegalJettyConfigurationException.class)
			.hasMessage(
				"Embedded jetty server requires a configuration that is an instance of com.github.mjeanroy.junit.servers.jetty.EmbeddedJettyConfiguration, please fix it."
			);
	}

	private static class ClassUsingDefaultConfiguration {
	}

	@JettyConfiguration(providedBy = DefaultEmbeddedJettyConfigurationProvider.class)
	private static class ClassAnnotatedWithConfigurationProvider {
	}

	private static class ClassUsingCustomConfiguration {
		@TestServerConfiguration
		private static EmbeddedJettyConfiguration configuration = EmbeddedJettyConfiguration.builder()
			.withStopTimeout(100)
			.build();
	}

	private static class ClassUsingNonJettyConfiguration {
		@TestServerConfiguration
		private static CustomConfiguration configuration = new CustomConfiguration();
	}

	private static class CustomConfiguration extends AbstractConfiguration {
	}

	private static class DefaultEmbeddedJettyConfigurationProvider implements EmbeddedJettyConfigurationProvider {
		private static final EmbeddedJettyConfiguration CONFIGURATION = EmbeddedJettyConfiguration.builder().build();

		@Override
		public EmbeddedJettyConfiguration build(Class<?> testClass) {
			return CONFIGURATION;
		}
	}
}
