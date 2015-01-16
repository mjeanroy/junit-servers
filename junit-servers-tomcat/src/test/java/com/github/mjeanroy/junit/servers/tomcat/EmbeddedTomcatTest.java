/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 <mickael.jeanroy@gmail.com>
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

import org.junit.After;
import org.junit.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

public class EmbeddedTomcatTest {

	private static final String PATH = "junit-servers-tomcat/";

	private static EmbeddedTomcatConfiguration initConfiguration() {
		try {
			String current = new File(".").getCanonicalPath();
			if (!current.endsWith("/")) {
				current += "/";
			}

			String path = current.endsWith(PATH) ? current : current + PATH;

			return EmbeddedTomcatConfiguration.builder()
					.withWebapp(path + "src/test/resources")
					.withClasspath(path + "target/classes")
					.build();
		}
		catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	private EmbeddedTomcat tomcat;

	@After
	public void tearDown() {
		if (tomcat != null) {
			tomcat.stop();
		}
	}

	@Test
	public void it_should_start_tomcat() {
		tomcat = new EmbeddedTomcat();
		tomcat.start();

		assertThat(tomcat.isStarted()).isTrue();
		assertThat(tomcat.getPort()).isNotZero();
	}

	@Test
	public void it_should_stop_tomcat() {
		tomcat = new EmbeddedTomcat();
		tomcat.start();

		assertThat(tomcat.isStarted()).isTrue();
		assertThat(tomcat.getPort()).isNotZero();

		tomcat.stop();
		assertThat(tomcat.isStarted()).isFalse();
		assertThat(tomcat.getPort()).isNotZero();
	}

	@Test
	public void it_should_get_servlet_context() {
		tomcat = new EmbeddedTomcat(initConfiguration());
		tomcat.start();
		assertThat(tomcat.getServletContext()).isNotNull();
	}

	@Test
	public void it_should_get_original_tomcat() {
		tomcat = new EmbeddedTomcat();
		assertThat(tomcat.getDelegate()).isNotNull();
	}
}
