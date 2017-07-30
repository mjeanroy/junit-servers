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

import java.net.URI;

import org.junit.Test;

import com.github.mjeanroy.junit.servers.tomcat.EmbeddedTomcat;
import com.github.mjeanroy.junit.servers.tomcat.EmbeddedTomcatConfiguration;

public class TomcatServerRuleTest {

	@Test
	public void it_should_create_rule_with_server() throws Exception {
		int port = 8080;
		String scheme = "http";
		String host = "localhost";
		String path = "/";
		String url = scheme + "://" + host + ":" + port + path;
		URI uri = new URI(url);

		final EmbeddedTomcatConfiguration config = mock(EmbeddedTomcatConfiguration.class);
		final EmbeddedTomcat tomcat = mock(EmbeddedTomcat.class);
		when(tomcat.getScheme()).thenReturn(scheme);
		when(tomcat.getHost()).thenReturn(host);
		when(tomcat.getPort()).thenReturn(port);
		when(tomcat.getPath()).thenReturn(path);
		when(tomcat.getUrl()).thenReturn(url);
		when(tomcat.getUri()).thenReturn(uri);
		when(tomcat.getConfiguration()).thenReturn(config);
		doAnswer(isStarted(tomcat, true)).when(tomcat).start();
		doAnswer(isStarted(tomcat, false)).when(tomcat).stop();

		TomcatServerRule rule = new TomcatServerRule(tomcat);
		assertThat(rule.getServer()).isSameAs(tomcat);
		assertThat(rule.getScheme()).isEqualTo(scheme);
		assertThat(rule.getHost()).isEqualTo(host);
		assertThat(rule.getPort()).isEqualTo(port);
		assertThat(rule.getPath()).isEqualTo(path);
		assertThat(rule.getUrl()).isEqualTo(url);
		assertThat(rule.getUri()).isEqualTo(uri);

		verify(tomcat).getScheme();
		verify(tomcat).getHost();
		verify(tomcat).getPort();
		verify(tomcat).getPort();
		verify(tomcat).getUrl();
		verify(tomcat).getUri();

		verify(tomcat, never()).start();
		verify(tomcat, never()).stop();

		rule.before();
		verify(tomcat).start();

		rule.after();
		verify(tomcat).stop();
	}

	@Test
	public void it_should_create_server_from_configuration() throws Exception {
		EmbeddedTomcatConfiguration configuration = EmbeddedTomcatConfiguration.defaultConfiguration();
		TomcatServerRule rule = new TomcatServerRule(configuration);

		assertThat(rule.getServer()).isNotNull();
		assertThat(rule.getScheme()).isEqualTo("http");
		assertThat(rule.getHost()).isEqualTo("localhost");
		assertThat(rule.getPath()).isEqualTo(configuration.getPath());
		assertThat(rule.getPort()).isZero(); // not started
		assertThat(rule.getUrl()).isNotEmpty();
		assertRule(rule);
	}

	@Test
	public void it_should_create_server_with_default_configuration() throws Exception {
		TomcatServerRule rule = new TomcatServerRule();

		assertThat(rule.getServer()).isNotNull();
		assertThat(rule.getScheme()).isEqualTo("http");
		assertThat(rule.getHost()).isEqualTo("localhost");
		assertThat(rule.getPath()).isEqualTo("/");
		assertThat(rule.getPort()).isZero(); // not started
		assertThat(rule.getUrl()).isNotEmpty();
		assertRule(rule);
	}

	private void assertRule(TomcatServerRule rule) throws Exception {
		try {
			rule.before();
			assertThat(rule.getScheme()).isEqualTo("http");
			assertThat(rule.getHost()).isEqualTo("localhost");
			assertThat(rule.getPath()).isEqualTo("/");
			assertThat(rule.getPort()).isGreaterThan(0);

			String url = url(rule.getScheme(), rule.getHost(), rule.getPort(), rule.getPath());
			assertThat(rule.getUrl()).isEqualTo(url);
			assertThat(rule.getUri()).isEqualTo(new URI(url));
		} finally {
			rule.after();
			assertThat(rule.getPort()).isZero();
		}
	}

	private static String url(String scheme, String host, int port, String path) {
		return scheme + "://" + host + ":" + port + path;
	}
}
