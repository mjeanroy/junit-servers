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

package com.github.mjeanroy.junit.servers.tomcat.junit4;

import com.github.mjeanroy.junit.servers.junit4.ServerRule;
import com.github.mjeanroy.junit.servers.loggers.Logger;
import com.github.mjeanroy.junit.servers.loggers.LoggerFactory;
import com.github.mjeanroy.junit.servers.tomcat.EmbeddedTomcatConfiguration;
import com.github.mjeanroy.junit.servers.tomcat.EmbeddedTomcat;

/**
 * Rule that can be used to start and stop embedded tomcat server.
 *
 * @deprecated Use {@code junit-servers-tomcat-8} instead.
 */
@Deprecated
public class TomcatServerJunit4Rule extends ServerRule {

	private static final Logger log = LoggerFactory.getLogger(TomcatServerJunit4Rule.class);

	/**
	 * Create rule.
	 *
	 * @param tomcat Tomcat Embedded Server.
	 * @throws NullPointerException If {@code tomcat} is {@code null}.
	 */
	public TomcatServerJunit4Rule(EmbeddedTomcat tomcat) {
		super(tomcat);
		log.warn("{} is deprecated and will be removed in the next major release, use junit-servers-tomcat-8 instead, see https://mjeanroy.dev/junit-servers", TomcatServerJunit4Rule.class);
	}

	/**
	 * Create rule using tomcat as embedded server.
	 */
	public TomcatServerJunit4Rule() {
		this(new EmbeddedTomcat());
		log.warn("{} is deprecated and will be removed in the next major release, use junit-servers-tomcat-8 instead, see https://mjeanroy.dev/junit-servers", TomcatServerJunit4Rule.class);
	}

	/**
	 * Create rule.
	 *
	 * @param configuration Tomcat Configuration.
	 */
	public TomcatServerJunit4Rule(EmbeddedTomcatConfiguration configuration) {
		this(new EmbeddedTomcat(configuration));
		log.warn("{} is deprecated and will be removed in the next major release, use junit-servers-tomcat-8 instead, see https://mjeanroy.dev/junit-servers", TomcatServerJunit4Rule.class);
	}
}
