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

package com.github.mjeanroy.junit.servers.tomcat;

import com.github.mjeanroy.junit.servers.servers.AbstractConfiguration;
import com.github.mjeanroy.junit.servers.tomcat.exceptions.IllegalTomcatConfigurationException;
import org.assertj.core.api.ThrowableAssert;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class EmbeddedTomcatConfigurationUtilsTest {

	@Test
	public void it_should_check_configuration_and_returns_it() {
		EmbeddedTomcatConfiguration configuration = EmbeddedTomcatConfiguration.defaultConfiguration();
		assertThat(EmbeddedTomcatConfigurationUtils.checkConfiguration(configuration)).isSameAs(configuration);
	}

	@Test
	public void it_should_check_configuration_and_fail_if_configuration_is_not_instance_of_EmbeddedTomcatConfiguration() {
		AbstractConfiguration configuration = new CustomConfiguration();

		assertThatThrownBy(checkConfiguration(configuration))
			.isInstanceOf(IllegalTomcatConfigurationException.class)
			.hasMessage(
				"Embedded tomcat server requires a configuration that is an instance of com.github.mjeanroy.junit.servers.tomcat.EmbeddedTomcatConfiguration, please fix it."
			);
	}

	private static ThrowableAssert.ThrowingCallable checkConfiguration(final AbstractConfiguration configuration) {
		return new ThrowableAssert.ThrowingCallable() {
			@Override
			public void call() {
				EmbeddedTomcatConfigurationUtils.checkConfiguration(configuration);
			}
		};
	}

	private static class CustomConfiguration extends AbstractConfiguration {
	}
}
