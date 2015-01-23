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

import com.github.mjeanroy.junit.servers.servers.configuration.AbstractConfiguration;
import com.github.mjeanroy.junit.servers.servers.configuration.AbstractConfigurationBuilder;

import static com.github.mjeanroy.junit.servers.commons.Preconditions.positive;

/**
 * Jetty configuration settings.
 */
public final class EmbeddedJettyConfiguration extends AbstractConfiguration {

	/**
	 * Configure the stop timeout in milliseconds.
	 * Default value is 30000 ms.
	 */
	private final int stopTimeout;

	/**
	 * Configure jetty embedded server to stop
	 * at shutdown.
	 */
	private final boolean stopAtShutdown;

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
	private EmbeddedJettyConfiguration(Builder builder) {
		super(builder);
		this.stopTimeout = builder.getStopTimeout();
		this.stopAtShutdown = builder.isStopAtShutdown();
	}

	public int getStopTimeout() {
		return stopTimeout;
	}

	public boolean isStopAtShutdown() {
		return stopAtShutdown;
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof EmbeddedJettyConfiguration && super.equals(o);
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	public static class Builder extends AbstractConfigurationBuilder<Builder, EmbeddedJettyConfiguration> {

		private int stopTimeout;

		private boolean stopAtShutdown;

		private Builder() {
			stopTimeout = 30000;
			stopAtShutdown = true;
		}

		@Override
		protected Builder self() {
			return this;
		}

		@Override
		public EmbeddedJettyConfiguration build() {
			return new EmbeddedJettyConfiguration(this);
		}

		public int getStopTimeout() {
			return stopTimeout;
		}

		public boolean isStopAtShutdown() {
			return stopAtShutdown;
		}

		/**
		 * Update stop timeout value.
		 * @param stopTimeout New stop timeout value.
		 * @return this
		 * @throws IllegalArgumentException if stop timeout is not positive.
		 */
		public Builder withStopTimeout(int stopTimeout) {
			this.stopTimeout = positive(stopTimeout, "stopTimeout");
			return this;
		}

		public Builder disableStopAtShutdown() {
			return toggleStopAtShutdown(false);
		}

		public Builder enableStopAtShutdown() {
			return toggleStopAtShutdown(true);
		}

		private Builder toggleStopAtShutdown(boolean stopAtShutdown) {
			this.stopAtShutdown = stopAtShutdown;
			return this;
		}
	}
}
