/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2026 <mickael.jeanroy@gmail.com>
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

import com.github.mjeanroy.junit.servers.loggers.Logger;
import com.github.mjeanroy.junit.servers.loggers.LoggerFactory;
import org.apache.catalina.Context;

import javax.servlet.ServletContext;

import static com.github.mjeanroy.junit.servers.tomcat.EmbeddedTomcatConfiguration.defaultConfiguration;

/**
 * Embedded server using tomcat as implementation.
 *
 * @deprecated Use {@code junit-servers-tomcat-8} instead.
 */
@Deprecated
public class EmbeddedTomcat extends AbstractEmbeddedTomcat<EmbeddedTomcatConfiguration> {

	private static final Logger log = LoggerFactory.getLogger(EmbeddedTomcat.class);

	/**
	 * Build embedded tomcat with default configuration.
	 */
	public EmbeddedTomcat() {
		this(defaultConfiguration());
		log.warn("{} is deprecated and will be removed in the next major release, use junit-servers-tomcat-8 instead, see https://mjeanroy.dev/junit-servers", EmbeddedTomcat.class);
	}

	/**
	 * Build embedded tomcat.
	 *
	 * @param configuration Tomcat configuration.
	 */
	public EmbeddedTomcat(EmbeddedTomcatConfiguration configuration) {
		super(configuration);
		log.warn("{} is deprecated and will be removed in the next major release, use junit-servers-tomcat-8 instead, see https://mjeanroy.dev/junit-servers", EmbeddedTomcat.class);
	}

	@Override
	public ServletContext getServletContext() {
		Context context = getContext();
		return context == null ? null : context.getServletContext();
	}
}
