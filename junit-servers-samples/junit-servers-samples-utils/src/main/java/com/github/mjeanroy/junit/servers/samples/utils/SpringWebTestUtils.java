/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2015-2019 <mickael.jeanroy@gmail.com>
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

package com.github.mjeanroy.junit.servers.samples.utils;

import com.github.mjeanroy.junit.servers.servers.EmbeddedServer;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.web.context.support.WebApplicationContextUtils.getWebApplicationContext;

/**
 * Static Spring-Web Utilities for various samples.
 */
final class SpringWebTestUtils {

	// Ensure non instantiation.
	private SpringWebTestUtils() {
	}

	/**
	 * Ensure the servlet context can be retrieved from embedded server, and spring web application
	 * context can be retrieved from it.
	 *
	 * @param server The embedded server.
	 */
	static void verifySpringWebContext(EmbeddedServer<?> server) {
		// Try to get servlet context
		ServletContext servletContext = (ServletContext) server.getServletContext();
		assertThat(servletContext).isNotNull();

		// Try to retrieve spring webApplicationContext
		WebApplicationContext webApplicationContext = getWebApplicationContext(servletContext);
		assertThat(webApplicationContext).isNotNull();
	}
}
