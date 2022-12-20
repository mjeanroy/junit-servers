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

package com.github.mjeanroy.junit.servers.samples.utils.tomcat;

import com.github.mjeanroy.junit.servers.tomcat.EmbeddedTomcatConfiguration;

import java.io.File;
import java.net.URL;

/**
 * Static Tomcat Utilities for various samples.
 */
public final class TomcatTestUtils {

	// Ensure non instantiation.
	private TomcatTestUtils() {
	}

	/**
	 * Create configuration for Embedded Tomcat in Unit Test.
	 *
	 * @return The configuration.
	 * @throws AssertionError If an error occurred while creating configuration.
	 */
	public static EmbeddedTomcatConfiguration createTomcatConfiguration() {
		return createBasicConfiguration().build();
	}

	/**
	 * Create configuration for Embedded Tomcat in Unit Test.
	 *
	 * @return The configuration.
	 * @throws AssertionError If an error occurred while creating configuration.
	 */
	public static EmbeddedTomcatConfiguration createTomcatConfigurationWithWebXml() {
		return createBasicConfiguration()
			.withOverrideDescriptor("src/test/resources/web.xml")
			.build();
	}

	private static EmbeddedTomcatConfiguration.Builder createBasicConfiguration() {
		try {
			EmbeddedTomcatConfiguration.Builder builder = EmbeddedTomcatConfiguration.builder()
				.withWebapp("src/main/webapp")
				.withClasspath("target/classes");

			// note use of maven plugin to copy a maven dependency to this directory
			File lib = new File("target/lib/");
			if (lib.exists()) {
				URL parentClasspath = lib.toURI().toURL();
				builder.withParentClasspath(parentClasspath);
			}

			return builder;
		}
		catch (Exception ex) {
			throw new AssertionError(ex);
		}
	}
}
