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

package com.github.mjeanroy.junit.servers.tomcat.tests;

import com.github.mjeanroy.junit.servers.tomcat.EmbeddedTomcat;
import com.github.mjeanroy.junit.servers.tomcat.EmbeddedTomcatConfiguration;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Builder for mock instances of {@link EmbeddedTomcat}.
 */
public class EmbeddedTomcatMockBuilder {

	/**
	 * Server scheme (for example, {@code "http"} or {@code "https"}).
	 * Default is {@code "http"}
	 */
	private String scheme;

	/**
	 * Server host (default is {@code "localhost"}).
	 */
	private String host;

	/**
	 * Server port (default is {@code 80}).
	 */
	private int port;

	/**
	 * Server path (default is {@code "/"}).
	 */
	private String path;

	/**
	 * Server URL (default is an URL built using {@link #scheme}, {@link #host}, {@link #port} and {@link #path}).
	 */
	private String url;

	/**
	 * Server Configuration.
	 */
	private EmbeddedTomcatConfiguration configuration;

	/**
	 * Create builder with default initial values.
	 */
	public EmbeddedTomcatMockBuilder() {
		this.scheme = "http";
		this.host = "localhost";
		this.port = 80;
		this.path = "/";
	}

	/**
	 * Set new {@link #scheme}.
	 *
	 * @param scheme New {@link #scheme}
	 * @return The builder.
	 */
	public EmbeddedTomcatMockBuilder withScheme(String scheme) {
		this.scheme = scheme;
		return this;
	}

	/**
	 * Set new {@link #host}.
	 *
	 * @param host New {@link #host}
	 * @return The builder.
	 */
	public EmbeddedTomcatMockBuilder withHost(String host) {
		this.host = host;
		return this;
	}

	/**
	 * Set new {@link #port}.
	 *
	 * @param port New {@link #port}
	 * @return The builder.
	 */
	public EmbeddedTomcatMockBuilder withPort(int port) {
		this.port = port;
		return this;
	}

	/**
	 * Set new {@link #path}.
	 *
	 * @param path New {@link #path}
	 * @return The builder.
	 */
	public EmbeddedTomcatMockBuilder withPath(String path) {
		this.path = path;
		return this;
	}

	/**
	 * Set new {@link #url}.
	 *
	 * @param url New {@link #url}
	 * @return The builder.
	 */
	public EmbeddedTomcatMockBuilder withUrl(String url) {
		this.url = url;
		return this;
	}

	/**
	 * Set new {@link #configuration}.
	 *
	 * @param configuration New {@link #configuration}
	 * @return The builder.
	 */
	public EmbeddedTomcatMockBuilder withConfiguration(EmbeddedTomcatConfiguration configuration) {
		this.configuration = configuration;
		return this;
	}

	/**
	 * Build mock instance of {@link EmbeddedTomcat}.
	 *
	 * @return The mock instance.
	 */
	public EmbeddedTomcat build() {
		final EmbeddedTomcat tomcat = mock(EmbeddedTomcat.class);
		when(tomcat.getScheme()).thenReturn(scheme);
		when(tomcat.getHost()).thenReturn(host);
		when(tomcat.getPort()).thenReturn(port);
		when(tomcat.getPath()).thenReturn(path);
		when(tomcat.isStarted()).thenReturn(false);

		String url = this.url == null ? url(scheme, host, port, path) : this.url;
		when(tomcat.getUrl()).thenReturn(url);

		EmbeddedTomcatConfiguration configuration = this.configuration == null ? new EmbeddedTomcatConfigurationMockBuilder().build() : this.configuration;
		when(tomcat.getConfiguration()).thenReturn(configuration);

		doAnswer(new IsStartedAnswer(tomcat, true)).when(tomcat).start();
		doAnswer(new IsStartedAnswer(tomcat, false)).when(tomcat).stop();

		return tomcat;
	}

	private static String url(String scheme, String host, int port, String path) {
		return scheme + "://" + host + ":" + port + path;
	}

	private static class IsStartedAnswer implements Answer<Void> {
		private final EmbeddedTomcat tomcat;
		private final boolean isStarted;

		private IsStartedAnswer(EmbeddedTomcat tomcat, boolean isStarted) {
			this.tomcat = tomcat;
			this.isStarted = isStarted;
		}

		@Override
		public Void answer(InvocationOnMock invocationOnMock) {
			when(tomcat.isStarted()).thenReturn(isStarted);
			return null;
		}
	}
}
