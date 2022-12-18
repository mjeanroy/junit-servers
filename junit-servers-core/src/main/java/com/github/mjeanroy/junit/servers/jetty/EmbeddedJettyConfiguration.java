/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2022 <mickael.jeanroy@gmail.com>
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

/**
 * Jetty configuration settings.
 */
public class EmbeddedJettyConfiguration extends AbstractEmbeddedJettyConfiguration {

	/**
	 * Get configuration builder.
	 *
	 * @return Builder.
	 */
	public static Builder builder() {
		return new Builder();
	}

	/**
	 * Get default configuration.
	 *
	 * @return Default configuration.
	 */
	public static EmbeddedJettyConfiguration defaultConfiguration() {
		return new Builder().build();
	}

	// Private constructor, use static builder.
	private EmbeddedJettyConfiguration(AbstractEmbeddedJettyConfigurationBuilder<?, ?> builder) {
		super(builder);
	}

	@Override
	protected boolean canEqual(Object o) {
		return o instanceof EmbeddedJettyConfiguration;
	}

	/**
	 * Builder for {@link EmbeddedJettyConfiguration} instances.
	 */
	public static class Builder extends AbstractEmbeddedJettyConfigurationBuilder<Builder, EmbeddedJettyConfiguration> {

		private Builder() {
			super();
		}

		@Override
		protected Builder self() {
			return this;
		}

		@Override
		public EmbeddedJettyConfiguration build() {
			return new EmbeddedJettyConfiguration(this);
		}
	}
}
