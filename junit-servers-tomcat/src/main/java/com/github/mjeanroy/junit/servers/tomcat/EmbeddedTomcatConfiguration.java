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

package com.github.mjeanroy.junit.servers.tomcat;

import com.github.mjeanroy.junit.servers.commons.ToStringBuilder;
import com.github.mjeanroy.junit.servers.servers.AbstractConfiguration;
import com.github.mjeanroy.junit.servers.servers.AbstractConfigurationBuilder;
import org.apache.catalina.startup.Tomcat;

import java.util.Objects;

import static com.github.mjeanroy.junit.servers.commons.Preconditions.notNull;

/**
 * Tomcat configuration settings.
 */
public class EmbeddedTomcatConfiguration extends AbstractConfiguration {

	/**
	 * The default value for {@link Builder#classpath}.
	 */
	private static final String DEFAULT_CLASSPATH = "./target/classes";

	/**
	 * The default tomcat base directory.
	 */
	private static final String DEFAULT_BASE_DIR = "./tomcat-work";

	/**
	 * The default value for the {@link Builder#keepBaseDir} flag.
	 */
	private static final boolean DEFAULT_KEEP_BASE_DIR = false;

	/**
	 * The default value for the {@link Builder#enableNaming} flag.
	 */
	private static final boolean DEFAULT_ENABLE_NAMING = true;

	/**
	 * The default value for the {@link Builder#forceMetaInf} flag.
	 */
	private static final boolean DEFAULT_FORCE_META_INF = true;

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
	 * Tomcat Base Directory: this directory is where tomcat will store
	 * temporary files.
	 *
	 * @see Tomcat#setBaseDir(String)
	 */
	private final String baseDir;

	/**
	 * Keep Tomcat Base Directory content on server stop.
	 */
	private final boolean keepBaseDir;

	/**
	 * Flag used to enable / disable naming.
	 * This is a flag to enables JNDI naming.
	 *
	 * @see Tomcat#enableNaming()
	 */
	private final boolean enableNaming;

	/**
	 * Flag used to force META-INF directory creation
	 * for additional classpath entries.
	 */
	private final boolean forceMetaInf;

	/**
	 * Build new tomcat configuration.
	 *
	 * @param builder Builder object.
	 */
	private EmbeddedTomcatConfiguration(Builder builder) {
		super(
			builder.getClasspath(),
			builder.getPath(),
			builder.getWebapp(),
			builder.getPort(),
			builder.getEnvProperties(),
			builder.getHooks(),
			builder.getParentClassLoader(),
			builder.getOverrideDescriptor()
		);

		this.baseDir = builder.getBaseDir();
		this.keepBaseDir = builder.isKeepBaseDir();
		this.enableNaming = builder.isEnableNaming();
		this.forceMetaInf = builder.isForceMetaInf();
	}

	/**
	 * Get {@link #baseDir}.
	 *
	 * @return {@link #baseDir}
	 */
	public String getBaseDir() {
		return baseDir;
	}

	/**
	 * Get current {@link #keepBaseDir} value.
	 *
	 * @return {@link #keepBaseDir}
	 */
	public boolean isKeepBaseDir() {
		return keepBaseDir;
	}

	/**
	 * Get {@link #enableNaming}.
	 *
	 * @return {@link #enableNaming}
	 */
	public boolean isEnableNaming() {
		return enableNaming;
	}

	/**
	 * Get {@link #forceMetaInf}.
	 *
	 * @return {@link #forceMetaInf}
	 */
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
			.append("parentClassLoader", getParentClassLoader())
			.append("baseDir", baseDir)
			.append("keepBaseDir", keepBaseDir)
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
					&& Objects.equals(keepBaseDir, c.keepBaseDir)
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
		return Objects.hash(super.hashCode(), baseDir, keepBaseDir, enableNaming, forceMetaInf);
	}

	/**
	 * Builder for {@link EmbeddedTomcatConfiguration}.
	 */
	public static class Builder extends AbstractConfigurationBuilder<Builder, EmbeddedTomcatConfiguration> {

		/**
		 * The tomcat baseDir configuration: this directory is where tomcat will store
		 * temporary files.
		 * Default is {@link EmbeddedTomcatConfiguration#DEFAULT_BASE_DIR}.
		 *
		 * @see EmbeddedTomcatConfiguration#DEFAULT_BASE_DIR
		 * @see Tomcat#setBaseDir(String)
		 */
		private String baseDir;

		/**
		 * Keep tomcat base directory content on server stop.
		 * Default is {@link EmbeddedTomcatConfiguration#DEFAULT_KEEP_BASE_DIR}.
		 *
		 * @see EmbeddedTomcatConfiguration#DEFAULT_KEEP_BASE_DIR
		 */
		private boolean keepBaseDir = DEFAULT_KEEP_BASE_DIR;

		/**
		 * Enable/Disable naming: this is a flag to enables JNDI naming.
		 * Default is {@link EmbeddedTomcatConfiguration#DEFAULT_ENABLE_NAMING}.
		 *
		 * @see EmbeddedTomcatConfiguration#DEFAULT_ENABLE_NAMING}
		 * @see Tomcat#enableNaming()
		 */
		private boolean enableNaming;

		/**
		 * Force the creation of the {@code META-INF} directory if it does not exist
		 * in the classpath.
		 */
		private boolean forceMetaInf;

		private Builder() {
			baseDir = DEFAULT_BASE_DIR;
			enableNaming = DEFAULT_ENABLE_NAMING;
			forceMetaInf = DEFAULT_FORCE_META_INF;

			withClasspath(DEFAULT_CLASSPATH);
		}

		@Override
		protected Builder self() {
			return this;
		}

		@Override
		public EmbeddedTomcatConfiguration build() {
			return new EmbeddedTomcatConfiguration(this);
		}

		/**
		 * Get current {@link #baseDir} value.
		 *
		 * @return {@link #baseDir}
		 */
		public String getBaseDir() {
			return baseDir;
		}

		/**
		 * Get current {@link #keepBaseDir} value.
		 *
		 * @return {@link #keepBaseDir}
		 */
		public boolean isKeepBaseDir() {
			return keepBaseDir;
		}

		/**
		 * Get current {@link #enableNaming} value.
		 *
		 * @return {@link #enableNaming}
		 */
		public boolean isEnableNaming() {
			return enableNaming;
		}

		/**
		 * Get current {@link #forceMetaInf} value.
		 *
		 * @return {@link #forceMetaInf}
		 */
		public boolean isForceMetaInf() {
			return forceMetaInf;
		}

		/**
		 * Change tomcat base directory.
		 *
		 * @param baseDir Base directory.
		 * @return this.
		 * @throws NullPointerException If {@code baseDir} is {@code null}.
		 */
		public Builder withBaseDir(String baseDir) {
			this.baseDir = notNull(baseDir, "baseDir");
			return self();
		}

		/**
		 * Keep tomcat base directory content on server stop.
		 *
		 * @return this.
		 */
		public Builder keepBaseDir() {
			this.keepBaseDir = true;
			return self();
		}

		/**
		 * Delete tomcat base directory on server stop.
		 *
		 * @return this.
		 */
		public Builder deleteBaseDir() {
			this.keepBaseDir = false;
			return self();
		}

		/**
		 * Enable naming (i.e enable JNDI) on tomcat server.
		 *
		 * @return this.
		 */
		public Builder enableNaming() {
			return toggleNaming(true);
		}

		/**
		 * Disable naming (i.e disable JNDI) on tomcat server.
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
