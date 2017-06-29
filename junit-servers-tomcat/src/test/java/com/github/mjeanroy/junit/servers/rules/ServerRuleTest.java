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

package com.github.mjeanroy.junit.servers.rules;

import com.github.mjeanroy.junit.servers.tomcat.EmbeddedTomcat;
import com.github.mjeanroy.junit.servers.tomcat.EmbeddedTomcatConfiguration;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ServerRuleTest {

	@Test
	public void it_should_start_jetty_because_of_classpath_detection() {
		ServerRule rule = new ServerRule();
		assertThat(rule.getServer()).isExactlyInstanceOf(EmbeddedTomcat.class);
		assertThat(rule.getServer().getConfiguration()).isEqualTo(EmbeddedTomcatConfiguration.defaultConfiguration());
	}

	@Test
	public void it_should_start_jetty_with_custom_configuration_because_of_classpath_detection() {
		EmbeddedTomcatConfiguration configuration = EmbeddedTomcatConfiguration.builder()
			.withPort(9000)
			.build();

		ServerRule rule = new ServerRule(configuration);
		assertThat(rule.getServer()).isExactlyInstanceOf(EmbeddedTomcat.class);
		assertThat(rule.getServer().getConfiguration()).isEqualTo(configuration);
	}
}
