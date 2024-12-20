/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2023 <mickael.jeanroy@gmail.com>
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

package com.github.mjeanroy.junit.servers.servers;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

class EmbeddedConfigurationTest {

	@Test
	void it_should_build_configuration() {
		EmbeddedConfiguration result = createConfiguration();
		assertThat(result.getPort()).isEqualTo(0);
		assertThat(result.getPath()).isEqualTo("/");
		assertThat(result.getClasspath()).isEqualTo(".");
		assertThat(result.getWebapp()).isEqualTo(
			String.join(File.separator, asList("src", "main", "webapp"))
		);
	}

	@Test
	void it_should_have_to_string() {
		EmbeddedConfiguration result = createConfiguration();
		assertThat(result.toString()).isEqualTo(
			EmbeddedConfiguration.class.getSimpleName() + "{" +
				"port: 0, " +
				"path: \"/\", " +
				"webapp: \"" + String.join(File.separator, asList("src", "main", "webapp")) + "\", " +
				"classpath: \".\", " +
				"overrideDescriptor: null, " +
				"parentClassLoader: null" +
			"}"
		);
	}

	@Test
	void it_should_implement_equals_hashCode() {
		ClassLoader red = new URLClassLoader(new URL[0]);
		ClassLoader black = new URLClassLoader(new URL[0]);
		EqualsVerifier.forClass(EmbeddedConfiguration.class)
			.suppress(Warning.STRICT_INHERITANCE)
			.withPrefabValues(ClassLoader.class, red, black)
			.verify();
	}

	private static EmbeddedConfiguration createConfiguration() {
		return new EmbeddedConfiguration();
	}
}
