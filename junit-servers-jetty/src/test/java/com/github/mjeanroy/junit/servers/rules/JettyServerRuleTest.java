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

import com.github.mjeanroy.junit.servers.jetty.EmbeddedJetty;
import com.github.mjeanroy.junit.servers.jetty.EmbeddedJettyConfiguration;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Description;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.runner.Description.createTestDescription;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class JettyServerRuleTest {

	private Description description;

	@Before
	public void setUp() {
		description = createTestDescription(JettyServerRuleTest.class, "name");
	}

	@Test
	public void it_should_create_rule_with_server() {
		int port = 8080;
		int configPort = 0;
		String path = "/";
		String url = "http://localhost:8080";

		EmbeddedJettyConfiguration config = mock(EmbeddedJettyConfiguration.class);
		when(config.getPort()).thenReturn(configPort);

		final EmbeddedJetty jetty = mock(EmbeddedJetty.class);
		when(jetty.getPort()).thenReturn(port);
		when(jetty.getPath()).thenReturn(path);
		when(jetty.getUrl()).thenReturn(url);
		when(jetty.getConfiguration()).thenReturn(config);
		when(jetty.isStarted()).thenReturn(false);
		doAnswer(isStarted(jetty, true)).when(jetty).start();
		doAnswer(isStarted(jetty, false)).when(jetty).stop();

		JettyServerRule rule = new JettyServerRule(jetty);
		Assertions.assertThat(rule.getServer()).isSameAs(jetty);
		assertThat(rule.getPort()).isEqualTo(configPort);
		assertThat(rule.getPath()).isEqualTo(path);
		assertThat(rule.getUrl()).isEqualTo(url);

		rule.before(description);
		verify(jetty).start();
		assertThat(rule.getPort()).isEqualTo(port);

		rule.after(description);
		verify(jetty).stop();
		assertThat(rule.getPort()).isEqualTo(configPort);
	}

	@Test
	public void it_should_create_server_from_configuration() {
		EmbeddedJettyConfiguration configuration = EmbeddedJettyConfiguration.defaultConfiguration();
		JettyServerRule rule = new JettyServerRule(configuration);

		assertThat(rule.getServer()).isNotNull();
		assertThat(rule.getPath()).isEqualTo(configuration.getPath());
		assertThat(rule.getPort()).isZero();
		assertThat(rule.getUrl()).isNotEmpty();
		assertRule(rule);
	}

	@Test
	public void it_should_create_server_with_default_configuration() {
		JettyServerRule rule = new JettyServerRule();

		assertThat(rule.getServer()).isNotNull();
		assertThat(rule.getPath()).isEqualTo("/");
		assertThat(rule.getPort()).isZero(); // not started
		assertThat(rule.getUrl()).isNotEmpty();
		assertRule(rule);
	}

	private void assertRule(JettyServerRule rule) {
		try {
			rule.before(description);
			assertThat(rule.getPath()).isEqualTo("/");
			assertThat(rule.getPort()).isGreaterThan(0);
			assertThat(rule.getUrl()).isEqualTo("http://localhost:" + rule.getPort() + rule.getPath());
		} finally {
			rule.after(description);
			assertThat(rule.getPort()).isZero(); // not started
		}
	}

	private static class IsStartedAnswer implements Answer<Void> {
		private final EmbeddedJetty jetty;
		private final boolean isStarted;

		private IsStartedAnswer(EmbeddedJetty jetty, boolean isStarted) {
			this.jetty = jetty;
			this.isStarted = isStarted;
		}

		@Override
		public Void answer(InvocationOnMock invocationOnMock) throws Throwable {
			when(jetty.isStarted()).thenReturn(isStarted);
			return null;
		}
	}

	private static IsStartedAnswer isStarted(EmbeddedJetty jetty, boolean isStarted) {
		return new IsStartedAnswer(jetty, isStarted);
	}
}
