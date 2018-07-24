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

package com.github.mjeanroy.junit.servers.tomcat;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

import java.net.URL;
import java.net.URLClassLoader;

import static org.assertj.core.api.Assertions.assertThat;

public class EmbeddedTomcatConfigurationTest {

	@Test
	public void it_should_build_default_configuration() {
		EmbeddedTomcatConfiguration result = EmbeddedTomcatConfiguration.defaultConfiguration();

		assertThat(result.getPort()).isEqualTo(0);
		assertThat(result.getPath()).isEqualTo("/");
		assertThat(result.getClasspath()).isEqualTo("./target/classes");
		assertThat(result.getWebapp()).isEqualTo("src/main/webapp");
		assertThat(result.isForceMetaInf()).isTrue();
		assertThat(result.isEnableNaming()).isTrue();
		assertThat(result.isKeepBaseDir()).isFalse();
	}

	@Test
	public void it_should_build_configuration() {
		int port = 8080;
		String path = "/foo";
		String webapp = "foo";
		String classpath = "/target/classes";

		EmbeddedTomcatConfiguration result = EmbeddedTomcatConfiguration.builder()
				.withPort(port)
				.withClasspath(classpath)
				.withWebapp(webapp)
				.withPath(path)
				.disableNaming()
				.disableForceMetaInf()
				.keepBaseDir()
				.build();

		assertThat(result.getPort()).isEqualTo(port);
		assertThat(result.getPath()).isEqualTo(path);
		assertThat(result.getClasspath()).isEqualTo(classpath);
		assertThat(result.getWebapp()).isEqualTo(webapp);
		assertThat(result.isForceMetaInf()).isFalse();
		assertThat(result.isEnableNaming()).isFalse();
		assertThat(result.isKeepBaseDir()).isTrue();
	}

	@Test
	public void it_should_implement_equals_hashCode() {
		ClassLoader red = new URLClassLoader(new URL[0]);
		ClassLoader black = new URLClassLoader(new URL[0]);
		EqualsVerifier.forClass(EmbeddedTomcatConfiguration.class)
			.suppress(Warning.STRICT_INHERITANCE)
			.withRedefinedSuperclass()
			.withPrefabValues(ClassLoader.class, red, black)
			.verify();
	}

	@Test
	public void it_should_have_to_string() {
		EmbeddedTomcatConfiguration result = EmbeddedTomcatConfiguration.defaultConfiguration();
		assertThat(result.toString()).isEqualTo(
			"EmbeddedTomcatConfiguration{" +
				"port: 0, " +
				"path: \"/\", " +
				"webapp: \"src/main/webapp\", " +
				"classpath: \"./target/classes\", " +
				"overrideDescriptor: null, " +
				"parentClassLoader: null, " +
				"baseDir: \"./tomcat-work\", " +
				"keepBaseDir: false, " +
				"enableNaming: true, " +
				"forceMetaInf: true" +
			"}");
	}
}
