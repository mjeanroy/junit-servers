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

package com.github.mjeanroy.junit.servers.servers.configuration;

import com.github.mjeanroy.junit.servers.servers.Hook;

import java.util.List;
import java.util.Map;

/**
 * Generic configuration that should be extended for
 * each custom embedded server.
 *
 * @deprecated Use {@link com.github.mjeanroy.junit.servers.servers.AbstractConfiguration} instead.
 */
@Deprecated
public abstract class AbstractConfiguration extends com.github.mjeanroy.junit.servers.servers.AbstractConfiguration {

	/**
	 * Initialize default configuration.
	 */
	protected AbstractConfiguration() {
		super();
	}

	/**
	 * Initialize configuration.
	 *
	 * @param builder Configuration builder.
	 */
	protected AbstractConfiguration(AbstractConfigurationBuilder<?, ?> builder) {
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
	}

	/**
	 * Initialize configuration.
	 *
	 * @param classpath New {@link #classpath} value.
	 * @param path New {@link #path} value.
	 * @param webapp New {@link #webapp} value.
	 * @param port New {@link #port} value.
	 * @param envProperties New {@link #envProperties} value.
	 * @param hooks New {@link #hooks} value.
	 * @param parentClassLoader New {@link #parentClassLoader} value.
	 * @param overrideDescriptor New {@link #overrideDescriptor} value.
	 */
	protected AbstractConfiguration(
			String classpath,
			String path,
			String webapp,
			int port,
			Map<String, String> envProperties,
			List<Hook> hooks,
			ClassLoader parentClassLoader,
			String overrideDescriptor) {

		super(
			classpath,
			path,
			webapp,
			port,
			envProperties,
			hooks,
			parentClassLoader,
			overrideDescriptor
		);
	}
}
