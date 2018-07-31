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

package com.github.mjeanroy.junit.servers.utils.impl;

import com.github.mjeanroy.junit.servers.servers.AbstractEmbeddedServer;
import com.github.mjeanroy.junit.servers.servers.EmbeddedServer;

import javax.servlet.ServletContext;

import static org.mockito.Mockito.mock;

public class FakeEmbeddedServer extends AbstractEmbeddedServer<FakeServer, FakeEmbeddedServerConfiguration> implements EmbeddedServer<FakeEmbeddedServerConfiguration> {

	/**
	 * The number of times the fake server has been started.
	 */
	private int nbStart;

	/**
	 * The number of times the fake server has been stopped.
	 */
	private int nbStop;

	/**
	 * A fake servlet context.
	 */
	private ServletContext servletContext;

	/**
	 * A fake delegated server.
	 */
	private FakeServer delegate;

	public FakeEmbeddedServer() {
		this(new FakeEmbeddedServerConfiguration());
	}

	public FakeEmbeddedServer(FakeEmbeddedServerConfiguration configuration) {
		super(configuration);
		this.servletContext = mock(ServletContext.class);
		this.delegate = new FakeServer();
	}

	@Override
	protected void doStart() {
		this.nbStart++;
	}

	@Override
	protected void doStop() {
		this.nbStop++;
	}

	@Override
	protected int doGetPort() {
		return configuration.getPort();
	}

	@Override
	public FakeEmbeddedServerConfiguration getConfiguration() {
		return configuration;
	}

	@Override
	public ServletContext getServletContext() {
		return servletContext;
	}

	@Override
	public FakeServer getDelegate() {
		return delegate;
	}

	/**
	 * Get {@link #nbStart}
	 *
	 * @return {@link #nbStart}
	 */
	public int getNbStart() {
		return nbStart;
	}

	/**
	 * Get {@link #nbStop}
	 *
	 * @return {@link #nbStop}
	 */
	public int getNbStop() {
		return nbStop;
	}
}
