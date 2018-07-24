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

package com.github.mjeanroy.junit.servers.jetty;

import com.github.mjeanroy.junit.servers.jetty.tests.EmbeddedJettyConfigurationMockBuilder;
import org.junit.Test;

import static com.github.mjeanroy.junit.servers.jetty.EmbeddedJettyConfiguration.defaultConfiguration;
import static org.assertj.core.api.Assertions.assertThat;

public class EmbeddedJettyProviderTest {

	@Test
	public void it_should_instantiate_jetty_with_default_configuration() {
		final EmbeddedJettyProvider provider = new EmbeddedJettyProvider();
		final EmbeddedJetty jetty = provider.instantiate();

		assertThat(jetty).isNotNull();
		assertThat(jetty.getConfiguration()).isNotNull().isEqualTo(defaultConfiguration());
	}

	@Test
	public void it_should_instantiate_jetty_with_custom_configuration() {
		final EmbeddedJettyProvider provider = new EmbeddedJettyProvider();
		final EmbeddedJettyConfiguration configuration = new EmbeddedJettyConfigurationMockBuilder().build();
		final EmbeddedJetty jetty = provider.instantiate(configuration);

		assertThat(jetty).isNotNull();
		assertThat(jetty.getConfiguration()).isNotNull().isSameAs(configuration);
	}
}
