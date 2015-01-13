/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 <mickael.jeanroy@gmail.com>
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

package com.github.mjeanroy.junit.servers.jetty;

import com.github.mjeanroy.junit.servers.servers.AbstractEmbeddedServerConfiguration;

import java.util.Objects;

public class EmbeddedJettyConfiguration extends AbstractEmbeddedServerConfiguration<EmbeddedJettyConfiguration> {

	public EmbeddedJettyConfiguration() {
		super();
	}

	/**
	 * Build new configuration object from existing jetty configuration.
	 *
	 * @param configuration Jetty Configuration.
	 */
	public EmbeddedJettyConfiguration(EmbeddedJettyConfiguration configuration) {
		super(configuration);
	}

	@Override
	public String toString() {
		return String.format(
				"%s {path=%s, webapp=%s, port=%s, classpath=%s}",
				getClass().getSimpleName(),
				getPath(), getWebapp(), getPort(), getClasspath()
		);
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}

		if (o instanceof EmbeddedJettyConfiguration) {
			EmbeddedJettyConfiguration c = (EmbeddedJettyConfiguration) o;
			return Objects.equals(getPath(), c.getPath())
					&& Objects.equals(getWebapp(), c.getWebapp())
					&& Objects.equals(getPort(), c.getPort())
					&& Objects.equals(getClasspath(), c.getClasspath());
		}

		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(getPath(), getWebapp(), getPort(), getClasspath());
	}
}
