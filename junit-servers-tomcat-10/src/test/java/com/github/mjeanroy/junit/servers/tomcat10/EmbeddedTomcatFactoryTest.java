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

package com.github.mjeanroy.junit.servers.tomcat10;

import com.github.mjeanroy.junit.servers.annotations.TestServerConfiguration;
import com.github.mjeanroy.junit.servers.servers.AbstractConfiguration;
import com.github.mjeanroy.junit.servers.tomcat.EmbeddedTomcatConfiguration;
import com.github.mjeanroy.junit.servers.tomcat.EmbeddedTomcatConfigurationProvider;
import com.github.mjeanroy.junit.servers.tomcat.IllegalTomcatConfigurationException;
import com.github.mjeanroy.junit.servers.tomcat.TomcatConfiguration;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EmbeddedTomcatFactoryTest {

	@Test
	void it_should_create_embedded_tomcat_from_class_using_default_configuration() {
		EmbeddedTomcat tomcat = EmbeddedTomcatFactory.createFrom(ClassUsingDefaultConfiguration.class);
		assertThat(tomcat).isNotNull();
		assertThat(tomcat.getConfiguration()).isEqualTo(EmbeddedTomcatConfiguration.defaultConfiguration());
	}

	@Test
	void it_should_create_embedded_tomcat_from_class_using_provided_configuration() {
		EmbeddedTomcatConfiguration providedConfiguration = EmbeddedTomcatConfiguration.builder().withPath("/test").build();
		EmbeddedTomcat tomcat = EmbeddedTomcatFactory.createFrom(ClassUsingDefaultConfiguration.class, providedConfiguration);

		assertThat(tomcat).isNotNull();
		assertThat(tomcat.getConfiguration()).isEqualTo(providedConfiguration);
	}

	@Test
	void it_should_create_embedded_tomcat_from_class_using_configuration_provider() {
		EmbeddedTomcat tomcat = EmbeddedTomcatFactory.createFrom(ClassAnnotatedWithConfigurationProvider.class);
		assertThat(tomcat).isNotNull();
		assertThat(tomcat.getConfiguration()).isSameAs(DefaultEmbeddedTomcatConfigurationProvider.CONFIGURATION);
	}

	@Test
	void it_should_create_embedded_tomcat_from_class_using_custom_configuration() {
		EmbeddedTomcat tomcat = EmbeddedTomcatFactory.createFrom(ClassUsingCustomConfiguration.class);
		assertThat(tomcat).isNotNull();
		assertThat(tomcat.getConfiguration()).isSameAs(ClassUsingCustomConfiguration.configuration);
	}

	@Test
	void it_should_fail_to_create_embedded_tomcat_from_class_using_non_valid_configuration() {
		assertThatThrownBy(() -> EmbeddedTomcatFactory.createFrom(ClassUsingNonTomcatConfiguration.class))
			.isInstanceOf(IllegalTomcatConfigurationException.class)
			.hasMessage(
				"Embedded tomcat server requires a configuration that is an instance of com.github.mjeanroy.junit.servers.tomcat.EmbeddedTomcatConfiguration, please fix it."
			);
	}

	private static class ClassUsingDefaultConfiguration {
	}

	@TomcatConfiguration(providedBy = DefaultEmbeddedTomcatConfigurationProvider.class)
	private static class ClassAnnotatedWithConfigurationProvider {
	}

	private static class ClassUsingCustomConfiguration {
		@TestServerConfiguration
		private static EmbeddedTomcatConfiguration configuration = EmbeddedTomcatConfiguration.builder()
			.withBaseDir(".")
			.build();
	}

	private static class ClassUsingNonTomcatConfiguration {
		@TestServerConfiguration
		private static CustomConfiguration configuration = new CustomConfiguration();
	}

	private static class CustomConfiguration extends AbstractConfiguration {
	}

	private static class DefaultEmbeddedTomcatConfigurationProvider implements EmbeddedTomcatConfigurationProvider {
		private static final EmbeddedTomcatConfiguration CONFIGURATION = EmbeddedTomcatConfiguration.builder().build();

		@Override
		public EmbeddedTomcatConfiguration build(Class<?> testClass) {
			return CONFIGURATION;
		}
	}
}
