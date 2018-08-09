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

package com.github.mjeanroy.junit.servers.utils.builders;

import com.github.mjeanroy.junit.servers.servers.AbstractConfiguration;
import com.github.mjeanroy.junit.servers.servers.EmbeddedServer;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static com.github.mjeanroy.junit.servers.utils.commons.TestUtils.url;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Builder for mock instances of {@link EmbeddedServer}.
 */
public class EmbeddedServerMockBuilder {

	/**
	 * The embedded server configuration.
	 */
	private AbstractConfiguration configuration;

	/**
	 * Server scheme.
	 */
	private String scheme;

	/**
	 * Server host.
	 */
	private String host;

	/**
	 * Server port.
	 */
	private int port;

	/**
	 * Server path.
	 */
	private String path;

	/**
	 * Create new builder.
	 */
	public EmbeddedServerMockBuilder() {
		this.configuration = new AbstractConfigurationMockBuilder().build();
		this.scheme = "http";
		this.host = "localhost";
		this.port = 80;
		this.path = "/";
	}

	/**
	 * Update {@link #configuration}.
	 *
	 * @param configuration New {@link #configuration}
	 * @return The builder.
	 */
	public EmbeddedServerMockBuilder withConfiguration(AbstractConfiguration configuration) {
		this.configuration = configuration;
		return this;
	}

	/**
	 * Update {@link #scheme}.
	 *
	 * @param scheme New {@link #scheme}
	 * @return The builder.
	 */
	public EmbeddedServerMockBuilder withScheme(String scheme) {
		this.scheme = scheme;
		return this;
	}

	/**
	 * Update {@link #host}.
	 *
	 * @param host New {@link #host}
	 * @return The builder.
	 */
	public EmbeddedServerMockBuilder withHost(String host) {
		this.host = host;
		return this;
	}

	/**
	 * Update {@link #path}.
	 *
	 * @param path New {@link #path}
	 * @return The builder.
	 */
	public EmbeddedServerMockBuilder withPath(String path) {
		this.path = path;
		return this;
	}

	/**
	 * Update {@link #port}.
	 *
	 * @param port New {@link #port}
	 * @return The builder.
	 */
	public EmbeddedServerMockBuilder withPort(int port) {
		this.port = port;
		return this;
	}

	/**
	 * Create new mock instance of {@link EmbeddedServer}.
	 *
	 * @return The mock instance.
	 */
	public EmbeddedServer build() {
		EmbeddedServer server = mock(EmbeddedServer.class);
		when(server.getConfiguration()).thenReturn(configuration);
		when(server.getScheme()).thenReturn(scheme);
		when(server.getHost()).thenReturn(host);
		when(server.getPort()).thenReturn(port);
		when(server.getPath()).thenReturn(path);
		when(server.getUrl()).thenReturn(url(scheme, host, port, path));

		doAnswer(new IsStartedAnswer(server, true)).when(server).start();
		doAnswer(new IsStartedAnswer(server, false)).when(server).stop();

		return server;
	}

	private static class IsStartedAnswer implements Answer<Object> {
		private final EmbeddedServer server;
		private final boolean started;

		private IsStartedAnswer(EmbeddedServer server, boolean started) {
			this.server = server;
			this.started = started;
		}

		@Override
		public Object answer(InvocationOnMock invocation) {
			when(server.isStarted()).thenReturn(started);
			return null;
		}
	}
}
