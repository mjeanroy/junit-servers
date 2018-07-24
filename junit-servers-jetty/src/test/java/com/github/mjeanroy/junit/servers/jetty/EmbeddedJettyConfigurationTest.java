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

package com.github.mjeanroy.junit.servers.jetty;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.eclipse.jetty.util.resource.Resource;
import org.junit.Test;
import org.mockito.Mockito;

import java.net.URL;
import java.net.URLClassLoader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class EmbeddedJettyConfigurationTest {

	@Test
	public void it_should_build_default_configuration() {
		EmbeddedJettyConfiguration result = EmbeddedJettyConfiguration.defaultConfiguration();

		assertThat(result.getPort()).isEqualTo(0);
		assertThat(result.getPath()).isEqualTo("/");
		assertThat(result.getClasspath()).isEqualTo(".");
		assertThat(result.getWebapp()).isEqualTo("src/main/webapp");
	}

	@Test
	public void it_should_build_configuration() {
		final int port = 8080;
		final String path = "/foo";
		final String webapp = "foo";
		final String classpath = "/target/classes";
		final int stopTimeout = 50;
		final Resource resource = mock(Resource.class);
		final String containerJarPattern = ".*\\.jar";
		final String webInfJarPattern = ".*";

		EmbeddedJettyConfiguration result = EmbeddedJettyConfiguration.builder()
				.withPort(port)
				.withClasspath(classpath)
				.withWebapp(webapp)
				.withPath(path)
				.withStopTimeout(stopTimeout)
				.disableStopAtShutdown()
				.withBaseResource(resource)
				.withContainerJarPattern(containerJarPattern)
				.withWebInfJarPattern(webInfJarPattern)
				.build();

		assertThat(result.getPort()).isEqualTo(port);
		assertThat(result.getPath()).isEqualTo(path);
		assertThat(result.getClasspath()).isEqualTo(classpath);
		assertThat(result.getWebapp()).isEqualTo(webapp);
		assertThat(result.getStopTimeout()).isEqualTo(stopTimeout);
		assertThat(result.isStopAtShutdown()).isFalse();
		assertThat(result.getBaseResource()).isSameAs(resource);
		assertThat(result.getContainerJarPattern()).isEqualTo(containerJarPattern);
		assertThat(result.getWebInfJarPattern()).isEqualTo(webInfJarPattern);
	}

	@Test
	public void it_should_implement_equals_hashCode() {
		ClassLoader red = new URLClassLoader(new URL[0]);
		ClassLoader black = new URLClassLoader(new URL[0]);
		EqualsVerifier.forClass(EmbeddedJettyConfiguration.class)
			.suppress(Warning.STRICT_INHERITANCE)
			.withRedefinedSuperclass()
			.withPrefabValues(ClassLoader.class, red, black)
			.verify();
	}

	@Test
	public void it_should_implement_to_string() {
		EmbeddedJettyConfiguration result = EmbeddedJettyConfiguration.defaultConfiguration();
		assertThat(result.toString()).isEqualTo(
			"EmbeddedJettyConfiguration{" +
				"port: 0, " +
				"path: \"/\", " +
				"webapp: \"src/main/webapp\", " +
				"classpath: \".\", " +
				"overrideDescriptor: null, " +
				"parentClassLoader: null, " +
				"stopTimeout: 30000, " +
				"stopAtShutdown: true, " +
				"baseResource: null, " +
				"containerJarPattern: null, " +
				"webInfJarPattern: null" +
			"}"
		);
	}
}
