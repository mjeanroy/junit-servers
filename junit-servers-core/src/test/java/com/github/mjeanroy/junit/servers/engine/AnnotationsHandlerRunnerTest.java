/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2019 <mickael.jeanroy@gmail.com>
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

package com.github.mjeanroy.junit.servers.engine;

import com.github.mjeanroy.junit.servers.servers.AbstractConfiguration;
import com.github.mjeanroy.junit.servers.servers.EmbeddedServer;
import com.github.mjeanroy.junit.servers.utils.builders.AbstractConfigurationMockBuilder;
import com.github.mjeanroy.junit.servers.utils.builders.EmbeddedServerMockBuilder;
import com.github.mjeanroy.junit.servers.utils.fixtures.FixtureClass;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AnnotationsHandlerRunnerTest {

	@Test
	void it_should_process_handlers() {
		final AbstractConfiguration configuration = new AbstractConfigurationMockBuilder().build();
		final EmbeddedServer<?> embeddedServer = new EmbeddedServerMockBuilder().build();
		final FixtureClass target = new FixtureClass();
		final AnnotationsHandlerRunner adapter = new AnnotationsHandlerRunner(embeddedServer, configuration);

		verifyBeforeTest(configuration, embeddedServer, target, adapter);
		verifyAfterTest(configuration, embeddedServer, target, adapter);
	}

	@Test
	void it_should_implement_to_string() {
		final AbstractConfiguration configuration = new AbstractConfigurationMockBuilder().build();
		final EmbeddedServer<?> embeddedServer = new EmbeddedServerMockBuilder().build();
		final AnnotationsHandlerRunner adapter = new AnnotationsHandlerRunner(embeddedServer, configuration);

		assertThat(adapter).hasToString(
			"AnnotationsHandlerRunner{" +
				"handlers: [" +
					"ServerAnnotationHandler{" +
						"annotationKlass: interface com.github.mjeanroy.junit.servers.annotations.TestServer, " +
						"server: MockEmbeddedServer" +
					"}, " +
					"ConfigurationAnnotationHandler{" +
						"annotationKlass: interface com.github.mjeanroy.junit.servers.annotations.TestServerConfiguration, " +
						"configuration: MockAbstractConfiguration" +
					"}, " +
					"HttpClientAnnotationHandler{" +
						"annotationKlass: interface com.github.mjeanroy.junit.servers.annotations.TestHttpClient, " +
						"server: MockEmbeddedServer" +
					"}" +
				"]" +
			"}"
		);
	}

	private static void verifyAfterTest(AbstractConfiguration configuration, EmbeddedServer<?> embeddedServer, FixtureClass target, AnnotationsHandlerRunner adapter) {
		adapter.afterEach(target);

		assertThat(target.server).isSameAs(embeddedServer);
		assertThat(target.configuration).isSameAs(configuration);
		assertThat(target.client).isNull();
	}

	private static void verifyBeforeTest(AbstractConfiguration configuration, EmbeddedServer<?> embeddedServer, FixtureClass target, AnnotationsHandlerRunner adapter) {
		adapter.beforeEach(target);

		assertThat(target.server).isSameAs(embeddedServer);
		assertThat(target.configuration).isSameAs(configuration);
		assertThat(target.client).isNotNull();
	}
}
