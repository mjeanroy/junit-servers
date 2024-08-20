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

package com.github.mjeanroy.junit.servers.tomcat10.jupiter;

import com.github.mjeanroy.junit.servers.jupiter.JunitServerExtension;
import com.github.mjeanroy.junit.servers.jupiter.JunitServerExtensionLifecycle;
import com.github.mjeanroy.junit.servers.loggers.Logger;
import com.github.mjeanroy.junit.servers.loggers.LoggerFactory;
import com.github.mjeanroy.junit.servers.servers.AbstractConfiguration;
import com.github.mjeanroy.junit.servers.tomcat.EmbeddedTomcatConfiguration;
import com.github.mjeanroy.junit.servers.tomcat10.EmbeddedTomcat;
import com.github.mjeanroy.junit.servers.tomcat10.EmbeddedTomcatFactory;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.Optional;

import static org.junit.platform.commons.support.AnnotationSupport.findAnnotation;

/**
 * A specialized {@link JunitServerExtension} that will instantiate an {@link EmbeddedTomcat}
 * server automatically instead of using the Service Provider API.
 *
 * Since this jupiter extends {@link JunitServerExtension}, it has exactly the same features (parameter
 * injections, etc.).
 *
 * @see JunitServerExtension
 */
public class TomcatServerExtension extends JunitServerExtension {

	/**
	 * Class Logger.
	 */
	private static final Logger log = LoggerFactory.getLogger(TomcatServerExtension.class);

	/**
	 * Create the jupiter with default behavior.
	 */
	public TomcatServerExtension() {
		super();
	}


	/**
	 * Create the jupiter with default behavior.
	 *
	 * @param lifecycle The extension lifecycle.
	 * @throws NullPointerException If {@code lifecycle} is {@code null}.
	 */
	public TomcatServerExtension(JunitServerExtensionLifecycle lifecycle) {
		super(lifecycle);
	}

	/**
	 * Create the jupiter and specify the embedded tomcat instance to use.
	 *
	 * @param tomcat The embedded tomcat instance to use.
	 * @throws NullPointerException If {@code tomcat} is {@code null}.
	 */
	public TomcatServerExtension(EmbeddedTomcat tomcat) {
		super(tomcat);
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
	}

	@Override
	protected EmbeddedTomcat instantiateServer(Class<?> testClass, AbstractConfiguration configuration) {
		log.debug("Instantiating embedded tomcat for test class: {}", testClass);
		return EmbeddedTomcatFactory.createFrom(testClass, configuration);
	}

	@Override
	protected Optional<JunitServerExtensionLifecycle> findLifecycle(Class<?> testClass) {
		return findAnnotation(testClass, TomcatTest.class).map(TomcatTest::lifecycle);
	}
}
