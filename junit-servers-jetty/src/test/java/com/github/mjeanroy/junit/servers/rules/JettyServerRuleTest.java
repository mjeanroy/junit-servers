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

import static com.github.mjeanroy.junit.servers.rules.IsStartedAnswer.isStarted;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.github.mjeanroy.junit.servers.jetty.EmbeddedJetty;
import com.github.mjeanroy.junit.servers.jetty.EmbeddedJettyConfiguration;

public class JettyServerRuleTest {

	@Test
	public void it_should_create_rule_with_server() throws Exception {
		int port = 8080;
		String path = "/";
		String scheme = "http";
		String host = "localhost";
		String url = url(scheme, host, port, path);

		final EmbeddedJettyConfiguration config = mock(EmbeddedJettyConfiguration.class);
		final EmbeddedJetty jetty = mock(EmbeddedJetty.class);
		when(jetty.getScheme()).thenReturn(scheme);
		when(jetty.getHost()).thenReturn(host);
		when(jetty.getPort()).thenReturn(port);
		when(jetty.getPath()).thenReturn(path);
		when(jetty.getUrl()).thenReturn(url);
		when(jetty.getConfiguration()).thenReturn(config);
		when(jetty.isStarted()).thenReturn(false);

		doAnswer(isStarted(jetty, true)).when(jetty).start();
		doAnswer(isStarted(jetty, false)).when(jetty).stop();

		JettyServerRule rule = new JettyServerRule(jetty);

		assertThat(rule.getServer()).isSameAs(jetty);
		assertThat(rule.getScheme()).isEqualTo(scheme);
		assertThat(rule.getHost()).isEqualTo(host);
		assertThat(rule.getPort()).isEqualTo(port);
		assertThat(rule.getPath()).isEqualTo(path);
		assertThat(rule.getUrl()).isEqualTo(url);

		verify(jetty).getScheme();
		verify(jetty).getHost();
		verify(jetty).getPort();
		verify(jetty).getPath();
		verify(jetty).getUrl();

		verify(jetty, never()).start();
		verify(jetty, never()).stop();

		rule.before();
		verify(jetty).start();

		rule.after();
		verify(jetty).stop();
	}

	@Test
	public void it_should_create_server_from_configuration()  throws Exception {
		EmbeddedJettyConfiguration configuration = EmbeddedJettyConfiguration.defaultConfiguration();
		JettyServerRule rule = new JettyServerRule(configuration);

		assertThat(rule.getServer()).isNotNull();
		assertThat(rule.getScheme()).isEqualTo("http");
		assertThat(rule.getHost()).isEqualTo("localhost");
		assertThat(rule.getPath()).isEqualTo(configuration.getPath());
		assertThat(rule.getPort()).isZero();
		assertThat(rule.getUrl()).isNotEmpty();
		assertRule(rule);
	}

	@Test
	public void it_should_create_server_with_default_configuration() throws Exception {
		JettyServerRule rule = new JettyServerRule();

		assertThat(rule.getServer()).isNotNull();
		assertThat(rule.getScheme()).isEqualTo("http");
		assertThat(rule.getHost()).isEqualTo("localhost");
		assertThat(rule.getPath()).isEqualTo("/");
		assertThat(rule.getPort()).isZero(); // not started
		assertThat(rule.getUrl()).isNotEmpty();
		assertRule(rule);
	}

	private void assertRule(JettyServerRule rule) throws Exception {
		try {
			rule.before();
			assertThat(rule.getScheme()).isEqualTo("http");
			assertThat(rule.getHost()).isEqualTo("localhost");
			assertThat(rule.getPath()).isEqualTo("/");
			assertThat(rule.getPort()).isGreaterThan(0);

			String url = url(rule.getScheme(), rule.getHost(), rule.getPort(), rule.getPath());
			assertThat(rule.getUrl()).isEqualTo(url);
		} finally {
			rule.after();
			assertThat(rule.getPort()).isZero(); // not started
		}
	}

	private static String url(String scheme, String host, int port, String path) {
		return scheme + "://" + host + ":" + port + path;
	}
}
