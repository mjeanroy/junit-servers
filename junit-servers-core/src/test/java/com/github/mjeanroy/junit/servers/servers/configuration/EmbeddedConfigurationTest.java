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

package com.github.mjeanroy.junit.servers.servers.configuration;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Before;
import org.junit.Test;

import java.net.URL;
import java.net.URLClassLoader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EmbeddedConfigurationTest {

	private EmbeddedConfigurationBuilder builder;

	@Before
	public void setUp() {
		builder = mock(EmbeddedConfigurationBuilder.class);
		when(builder.getPath()).thenReturn("/foo");
		when(builder.getClasspath()).thenReturn("/target/classes");
		when(builder.getPort()).thenReturn(8080);
		when(builder.getWebapp()).thenReturn("src/main/webapp");
	}

	@Test
	public void it_should_build_configuration() {
		EmbeddedConfiguration result = new EmbeddedConfiguration(builder);

		assertThat(result.getPort()).isEqualTo(builder.getPort());
		assertThat(result.getPath()).isEqualTo(builder.getPath());
		assertThat(result.getClasspath()).isEqualTo(builder.getClasspath());
		assertThat(result.getWebapp()).isEqualTo(builder.getWebapp());
	}

	@Test
	public void it_should_have_to_string() {
		EmbeddedConfiguration result = new EmbeddedConfiguration(builder);
		assertThat(result.toString()).isEqualTo(
				"EmbeddedConfiguration{" +
						"port: 8080, " +
						"path: \"/foo\", " +
						"webapp: \"src/main/webapp\", " +
						"classpath: \"/target/classes\", " +
						"overrideDescriptor: null, " +
						"parentClassLoader: null" +
				"}");
	}

	@Test
	public void it_should_implement_equals_hashCode() {
		ClassLoader red = new URLClassLoader(new URL[0]);
		ClassLoader black = new URLClassLoader(new URL[0]);
		EqualsVerifier.forClass(EmbeddedConfiguration.class)
			.withPrefabValues(ClassLoader.class, red, black)
			.verify();
	}

	private static final class EmbeddedConfiguration extends AbstractConfiguration {
		private EmbeddedConfiguration(EmbeddedConfigurationBuilder builder) {
			super(builder);
		}
	}

	protected static class EmbeddedConfigurationBuilder extends AbstractConfigurationBuilder<EmbeddedConfigurationBuilder, EmbeddedConfiguration> {
		@Override
		protected EmbeddedConfigurationBuilder self() {
			return this;
		}

		@Override
		public EmbeddedConfiguration build() {
			return new EmbeddedConfiguration(this);
		}
	}
}
