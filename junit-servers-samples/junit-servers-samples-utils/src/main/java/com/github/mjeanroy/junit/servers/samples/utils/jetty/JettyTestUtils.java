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

package com.github.mjeanroy.junit.servers.samples.utils.jetty;

import com.github.mjeanroy.junit.servers.jetty9.EmbeddedJettyConfiguration;

import java.io.File;
import java.net.URL;

/**
 * Static Jetty Utilities for various samples.
 */
public class JettyTestUtils {

	// Ensure non instantiation.
	private JettyTestUtils() {
	}

	/**
	 * Create the Jetty Embedded configuration to use in unit tests.
	 *
	 * @return The Jetty Embedded Configuration.
	 * @throws AssertionError If an error occurred while creation the configuration object.
	 */
	public static EmbeddedJettyConfiguration createJettyConfiguration() {
		try {
			EmbeddedJettyConfiguration.Builder builder = EmbeddedJettyConfiguration.builder()
				.withWebapp("src/main/webapp")
				.withClasspath("target/classes")
				.withContainerJarPattern(".*\\.jar");

			// Note use of maven plugin to copy a maven dependency to this directory
			File file = new File("target/lib/");
			if (file.exists()) {
				URL parentClasspath = file.toURI().toURL();
				builder.withParentClasspath(parentClasspath);
			}

			return builder.build();
		}
		catch (Exception ex) {
			throw new AssertionError(ex);
		}
	}
}
