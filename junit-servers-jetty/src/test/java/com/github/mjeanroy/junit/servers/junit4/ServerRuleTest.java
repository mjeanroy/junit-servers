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

import com.github.mjeanroy.junit.servers.jetty.EmbeddedJetty;
import com.github.mjeanroy.junit.servers.jetty.EmbeddedJettyConfiguration;
import org.junit.After;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ServerRuleTest {

	private ServerRule rule;

	@After
	public void tearDown() {
		if (rule != null) {
			rule.stop();
		}
	}

	@Test
	public void it_should_start_jetty_because_of_classpath_detection() {
		rule = new ServerRule();
		assertThat(rule.getServer()).isExactlyInstanceOf(EmbeddedJetty.class);
		assertThat(rule.getServer().getConfiguration()).isEqualTo(EmbeddedJettyConfiguration.defaultConfiguration());
	}

	@Test
	public void it_should_start_jetty_with_custom_configuration_because_of_classpath_detection() {
		EmbeddedJettyConfiguration configuration = EmbeddedJettyConfiguration.builder()
			.withPort(9000)
			.build();

		rule = new ServerRule(configuration);
		assertThat(rule.getServer()).isExactlyInstanceOf(EmbeddedJetty.class);
		assertThat(rule.getServer().getConfiguration()).isEqualTo(configuration);
	}

	@Test
	public void it_should_start_jetty_and_get_real_port() {
		rule = new ServerRule();
		assertThat(rule.getPort()).isZero();

		rule.start();
		assertThat(rule.getPort()).isNotZero();
	}

	@Test
	public void it_should_start_jetty_and_get_uri() {
		rule = new ServerRule();
		rule.start();
		assertThat(rule.getUrl()).isEqualTo(localUrl(rule.getPort()));
	}

	private static String localUrl(int port) {
		return "http://localhost:" + port + "/";
	}
}
