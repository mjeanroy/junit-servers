/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 <mickael.jeanroy@gmail.com>, <fernando.ney@gmail.com>
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

package com.github.mjeanroy.junit.servers.samples.tomcat.webxml;

import com.github.mjeanroy.junit.servers.client.HttpClient;
import com.github.mjeanroy.junit.servers.servers.EmbeddedServer;
import com.github.mjeanroy.junit.servers.tomcat.EmbeddedTomcatConfiguration;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Static Test Utilities.
 */
class TestUtils {

	// Ensure non instantiation.
	private TestUtils() {
	}

	/**
	 * Create configuration for Embedded Tomcat in Unit Test.
	 *
	 * @return The configuration.
	 * @throws AssertionError If an error occurred while creating configuration.
	 */
	static EmbeddedTomcatConfiguration createTomcatConfiguration() {
		try {

			String absolutePath = new File(".").getCanonicalPath();
			if (!absolutePath.endsWith("/")) {
				absolutePath += "/";
			}

			return EmbeddedTomcatConfiguration.builder()
					.withWebapp(absolutePath + "src/main/webapp")
					.withClasspath(absolutePath + "target/classes")
					.build();

		} catch (Exception ex) {
			throw new AssertionError(ex);
		}
	}

	/**
	 * Ensure the home page can be rendered with expected message.
	 *
	 * @param client The HTTP client.
	 * @param tomcat The started embedded server.
	 */
	static void ensureIndexIsOk(HttpClient client, EmbeddedServer tomcat) {
		String message = client
				.prepareGet("/index")
				.execute()
				.body();

		assertThat(message).isNotEmpty().isEqualTo("Hello World");

		// Try to get servlet context
		ServletContext servletContext = tomcat.getServletContext();
		assertThat(servletContext).isNotNull();

		// Try to retrieve spring webApplicationContext
		WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		assertThat(webApplicationContext).isNotNull();
	}
}
