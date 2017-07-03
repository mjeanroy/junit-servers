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

package com.github.mjeanroy.junit.servers.tomcat;

import static com.github.mjeanroy.junit.servers.commons.Preconditions.notNull;

import java.util.Objects;

import com.github.mjeanroy.junit.servers.commons.ToStringBuilder;
import com.github.mjeanroy.junit.servers.servers.configuration.AbstractConfiguration;
import com.github.mjeanroy.junit.servers.servers.configuration.AbstractConfigurationBuilder;

/**
 * Tomcat configuration settings.
 */
public class EmbeddedTomcatConfiguration extends AbstractConfiguration {

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
	public static EmbeddedTomcatConfiguration defaultConfiguration() {
		return new Builder().build();
	}

	/**
	 * Tomcat Base Directory.
	 */
	private final String baseDir;

	/**
	 * Flag used to enable / disable naming.
	 */
	private final boolean enableNaming;

	/**
	 * Flag used to force META-INF directory creation for additional classpath entries.
	 */
	private final boolean forceMetaInf;

	/**
	 * Build new tomcat configuration.
	 *
	 * @param builder Builder object.
	 */
	private EmbeddedTomcatConfiguration(Builder builder) {
		super(builder);
		this.baseDir = builder.getBaseDir();
		this.enableNaming = builder.isEnableNaming();
		this.forceMetaInf = builder.isForceMetaInf();
	}

	public String getBaseDir() {
		return baseDir;
	}

	public boolean isEnableNaming() {
		return enableNaming;
	}

	public boolean isForceMetaInf() {
		return forceMetaInf;
	}

	@Override
	public String toString() {
		return ToStringBuilder.create(getClass())
			.append("port", getPort())
			.append("path", getPath())
			.append("webapp", getWebapp())
			.append("classpath", getClasspath())
			.append("overrideDescriptor", getOverrideDescriptor())
			.append("parentClasspath", getParentClasspath())
			.append("baseDir", baseDir)
			.append("enableNaming", enableNaming)
			.append("forceMetaInf", forceMetaInf)
			.build();
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}

		if (o instanceof EmbeddedTomcatConfiguration) {
			EmbeddedTomcatConfiguration c = (EmbeddedTomcatConfiguration) o;
			return super.equals(c)
					&& Objects.equals(baseDir, c.baseDir)
					&& Objects.equals(enableNaming, c.enableNaming)
					&& Objects.equals(forceMetaInf, c.forceMetaInf);
		}

		return false;
	}

	@Override
	protected boolean canEqual(Object o) {
		return o instanceof EmbeddedTomcatConfiguration;
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), baseDir, enableNaming, forceMetaInf);
	}

	public static class Builder extends AbstractConfigurationBuilder<Builder, EmbeddedTomcatConfiguration> {

		private String baseDir;
		private boolean enableNaming;
		private boolean forceMetaInf;

		private Builder() {
			baseDir = "./tomcat-work";
			enableNaming = true;
			forceMetaInf = true;

			withClasspath("./target/classes");
		}

		@Override
		protected Builder self() {
			return this;
		}

		@Override
		public EmbeddedTomcatConfiguration build() {
			return new EmbeddedTomcatConfiguration(this);
		}

		public String getBaseDir() {
			return baseDir;
		}

		public boolean isEnableNaming() {
			return enableNaming;
		}

		public boolean isForceMetaInf() {
			return forceMetaInf;
		}

		/**
		 * Change tomcat base directory.
		 *
		 * @param baseDir Base directory.
		 * @return this.
		 * @throws NullPointerException if baseDir is null.
		 */
		public Builder withBaseDir(String baseDir) {
			this.baseDir = notNull(baseDir, "baseDir");
			return self();
		}

		/**
		 * Enable naming on tomcat server.
		 *
		 * @return this.
		 */
		public Builder enableNaming() {
			return toggleNaming(true);
		}

		/**
		 * Disable naming on tomcat server.
		 *
		 * @return this.
		 */
		public Builder disableNaming() {
			return toggleNaming(false);
		}

		/**
		 * Enable META-INF creation.
		 *
		 * @return this.
		 */
		public Builder enableForceMetaInf() {
			return toggleMetaInf(true);
		}

		/**
		 * Disable META-INF creation.
		 *
		 * @return this.
		 */
		public Builder disableForceMetaInf() {
			return toggleMetaInf(false);
		}

		private Builder toggleNaming(boolean enableNaming) {
			this.enableNaming = enableNaming;
			return self();
		}

		private Builder toggleMetaInf(boolean forceMetaInf) {
			this.forceMetaInf = forceMetaInf;
			return self();
		}
	}
}
