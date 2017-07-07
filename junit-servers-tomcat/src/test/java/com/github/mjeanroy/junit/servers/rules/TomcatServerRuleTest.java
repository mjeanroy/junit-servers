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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.github.mjeanroy.junit.servers.tomcat.EmbeddedTomcat;
import com.github.mjeanroy.junit.servers.tomcat.EmbeddedTomcatConfiguration;

public class TomcatServerRuleTest {

	@Test
	public void it_should_create_rule_with_server() {
		int port = 8080;
		int configPort = 0;
		String path = "/";
		String url = "http://localhost:8080";

		EmbeddedTomcatConfiguration config = mock(EmbeddedTomcatConfiguration.class);
		when(config.getPort()).thenReturn(configPort);

		EmbeddedTomcat tomcat = mock(EmbeddedTomcat.class);
		when(tomcat.getPort()).thenReturn(port);
		when(tomcat.getPath()).thenReturn(path);
		when(tomcat.getUrl()).thenReturn(url);
		when(tomcat.getConfiguration()).thenReturn(config);
		doAnswer(isStarted(tomcat, true)).when(tomcat).start();
		doAnswer(isStarted(tomcat, false)).when(tomcat).stop();

		TomcatServerRule rule = new TomcatServerRule(tomcat);
		assertThat(rule.getServer()).isSameAs(tomcat);
		assertThat(rule.getPort()).isEqualTo(configPort);
		assertThat(rule.getPath()).isEqualTo(path);
		assertThat(rule.getUrl()).isEqualTo(url);

		rule.before();
		verify(tomcat).start();
		assertThat(rule.getPort()).isEqualTo(port);

		rule.after();
		verify(tomcat).stop();
		assertThat(rule.getPort()).isEqualTo(configPort);
	}

	@Test
	public void it_should_create_server_from_configuration() {
		EmbeddedTomcatConfiguration configuration = EmbeddedTomcatConfiguration.defaultConfiguration();
		TomcatServerRule rule = new TomcatServerRule(configuration);

		assertThat(rule.getServer()).isNotNull();
		assertThat(rule.getPath()).isEqualTo(configuration.getPath());
		assertThat(rule.getPort()).isZero(); // not started
		assertThat(rule.getUrl()).isNotEmpty();
		assertRule(rule);
	}

	@Test
	public void it_should_create_server_with_default_configuration() {
		TomcatServerRule rule = new TomcatServerRule();

		assertThat(rule.getServer()).isNotNull();
		assertThat(rule.getPath()).isEqualTo("/");
		assertThat(rule.getPort()).isZero(); // not started
		assertThat(rule.getUrl()).isNotEmpty();
		assertRule(rule);
	}

	private void assertRule(TomcatServerRule rule) {
		try {
			rule.before();
			assertThat(rule.getPath()).isEqualTo("/");
			assertThat(rule.getPort()).isGreaterThan(0);
			assertThat(rule.getUrl()).isEqualTo("http://localhost:" + rule.getPort() + rule.getPath());
		} finally {
			rule.after();
			assertThat(rule.getPort()).isZero();
		}
	}

	private static class IsStartedAnswer implements Answer<Void> {
		private final EmbeddedTomcat tomcat;
		private final boolean isStarted;

		private IsStartedAnswer(EmbeddedTomcat tomcat, boolean isStarted) {
			this.tomcat = tomcat;
			this.isStarted = isStarted;
		}

		@Override
		public Void answer(InvocationOnMock invocationOnMock) throws Throwable {
			when(tomcat.isStarted()).thenReturn(isStarted);
			return null;
		}
	}

	private static IsStartedAnswer isStarted(EmbeddedTomcat tomcat, boolean isStarted) {
		return new IsStartedAnswer(tomcat, isStarted);
	}
}
