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

package com.github.mjeanroy.junit.servers.tomcat.jupiter;

import com.github.mjeanroy.junit.servers.jupiter.JunitServerExtension;
import com.github.mjeanroy.junit.servers.jupiter.JunitServerExtensionLifecycle;
import com.github.mjeanroy.junit.servers.loggers.Logger;
import com.github.mjeanroy.junit.servers.loggers.LoggerFactory;
import com.github.mjeanroy.junit.servers.servers.AbstractConfiguration;
import com.github.mjeanroy.junit.servers.tomcat.EmbeddedTomcat;
import com.github.mjeanroy.junit.servers.tomcat.EmbeddedTomcatConfiguration;
import com.github.mjeanroy.junit.servers.tomcat.EmbeddedTomcatFactory;
import org.junit.jupiter.api.extension.RegisterExtension;

/**
 * A specialized {@link JunitServerExtension} that will instantiate an {@link EmbeddedTomcat}
 * server automatically instead of using the Service Provider API.
 *
 * Since this jupiter extends {@link JunitServerExtension}, it has exactly the same features (parameter
 * injections, etc.).
 *
 * @see JunitServerExtension
 * @deprecated Use {@code junit-servers-tomcat-8} instead.
 */
@Deprecated
public class TomcatServerExtension extends JunitServerExtension {

	/**
	 * Class Logger.
	 */
	private static final Logger log = LoggerFactory.getLogger(TomcatServerExtension.class);

	static {
		log.warn("{} is deprecated and will be removed in the next major release, use junit-servers-tomcat-8 instead, see https://mjeanroy.dev/junit-servers", TomcatServerExtension.class);
	}

	/**
	 * Create the jupiter with default behavior.
	 */
	public TomcatServerExtension() {
		super();
		logDeprecationWarning();
	}

	/**
	 * Create the jupiter with default behavior.
	 *
	 * @param lifecycle The extension lifecycle.
	 * @throws NullPointerException If {@code lifecycle} is {@code null}.
	 */
	public TomcatServerExtension(JunitServerExtensionLifecycle lifecycle) {
		super(lifecycle);
		logDeprecationWarning();
	}

	/**
	 * Create the jupiter and specify the embedded tomcat instance to use.
	 *
	 * @param tomcat The embedded tomcat instance to use.
	 * @throws NullPointerException If {@code tomcat} is {@code null}.
	 */
	public TomcatServerExtension(EmbeddedTomcat tomcat) {
		super(tomcat);
		logDeprecationWarning();
	}

	/**
	 * Create the jupiter and specify the embedded tomcat instance to use.
	 *
	 * @param tomcat The embedded tomcat instance to use.
	 * @throws NullPointerException If {@code tomcat} is {@code null}.
	 * @throws NullPointerException If {@code lifecycle} is {@code null}.
	 */
	public TomcatServerExtension(JunitServerExtensionLifecycle lifecycle, EmbeddedTomcat tomcat) {
		super(lifecycle, tomcat);
		logDeprecationWarning();
	}

	/**
	 * Create the jupiter and specify the embedded tomcat configuration to use (when using
	 * jupiter with {@link RegisterExtension}).
	 *
	 * @param configuration The embedded tomcat configuration to use.
	 * @throws NullPointerException If {@code configuration} is {@code null}.
	 */
	public TomcatServerExtension(EmbeddedTomcatConfiguration configuration) {
		super(configuration);
		logDeprecationWarning();
	}

	/**
	 * Create the jupiter and specify the embedded tomcat configuration to use (when using
	 * jupiter with {@link RegisterExtension}).
	 *
	 * @param lifecycle The extension lifecycle.
	 * @param configuration The embedded tomcat configuration to use.
	 * @throws NullPointerException If {@code configuration} is {@code null}.
	 * @throws NullPointerException If {@code lifecycle} is {@code null}.
	 */
	public TomcatServerExtension(JunitServerExtensionLifecycle lifecycle, EmbeddedTomcatConfiguration configuration) {
		super(lifecycle, configuration);
		logDeprecationWarning();
	}

	private static void logDeprecationWarning() {
		log.warn("{} is deprecated and will be removed in the next major release, use junit-servers-tomcat-8 instead, see https://mjeanroy.dev/junit-servers", TomcatServerExtension.class);
	}

	@Override
	protected EmbeddedTomcat instantiateServer(Class<?> testClass, AbstractConfiguration configuration) {
		log.debug("Instantiating embedded tomcat for test class: {}", testClass);
		return EmbeddedTomcatFactory.createFrom(testClass, configuration);
	}
}
