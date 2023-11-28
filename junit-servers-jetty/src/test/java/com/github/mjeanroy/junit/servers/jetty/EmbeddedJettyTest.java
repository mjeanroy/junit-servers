/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2022 <mickael.jeanroy@gmail.com>
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

import com.github.mjeanroy.junit.servers.testing.HttpTestUtils.HttpResponse;
import com.github.mjeanroy.junit.servers.testing.IoTestUtils;
import com.github.mjeanroy.junit.servers.testing.IoTestUtils.TempFile;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

import static com.github.mjeanroy.junit.servers.testing.HttpTestUtils.get;
import static com.github.mjeanroy.junit.servers.testing.HttpTestUtils.localhost;
import static com.github.mjeanroy.junit.servers.testing.IoTestUtils.createTempFile;
import static com.github.mjeanroy.junit.servers.testing.IoTestUtils.getFileFromClasspath;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("deprecation")
class EmbeddedJettyTest {

	@Test
	void it_should_start_jetty() {
		EmbeddedJetty jetty = new EmbeddedJetty();
		assertThat(jetty.getScheme()).isEqualTo("http");
		assertThat(jetty.getHost()).isEqualTo("localhost");
		assertThat(jetty.getPath()).isEqualTo("/");

		doRun(jetty, () -> {
			assertThat(jetty.isStarted()).isTrue();
			assertThat(jetty.getPort()).isNotZero();
			assertThat(jetty.getUrl()).isEqualTo(localhost(jetty.getPort()));

			// Since there is no webapp deployed, we should get a 404
			HttpResponse rsp = get(jetty.getUrl());
			assertThat(rsp.getStatusCode()).isEqualTo(404);
			assertThat(rsp.getResponseBody()).isNotEmpty();
		});
	}

	@Test
	void it_should_stop_jetty() {
		EmbeddedJetty jetty = new EmbeddedJetty();
		assertThat(jetty.getScheme()).isEqualTo("http");
		assertThat(jetty.getHost()).isEqualTo("localhost");
		assertThat(jetty.getPath()).isEqualTo("/");

		doRun(jetty, () -> {
			assertThat(jetty.isStarted()).isTrue();
			assertThat(jetty.getPort()).isNotZero();
			assertThat(jetty.getUrl()).isEqualTo(localhost(jetty.getPort()));
		});

		assertThat(jetty.isStarted()).isFalse();
		assertThat(jetty.getPort()).isZero();
		assertThat(jetty.getUrl()).isEqualTo(localhost(jetty.getPort()));
	}

	@Test
	void it_should_get_configuration_port_until_jetty_is_started() {
		EmbeddedJetty jetty = new EmbeddedJetty();
		assertThat(jetty.getPort()).isZero();

		doRun(jetty, () ->
			assertThat(jetty.getPort()).isNotZero()
		);

		assertThat(jetty.getPort()).isZero();
	}

	@Test
	void it_should_get_servlet_context() {
		run((jetty) ->
			assertThat(jetty.getServletContext()).isNotNull()
		);
	}

	@Test
	void it_should_get_original_jetty() {
		run((jetty) ->
			assertThat(jetty.getDelegate()).isNotNull()
		);
	}

	@Test
	void it_should_add_parent_classloader(@TempDir Path tmp) {
		TempFile tmpFile = createTempFile(tmp);

		EmbeddedJettyConfiguration configuration = EmbeddedJettyConfiguration.builder()
			.withWebapp(tmpFile.getParentDir())
			.withParentClasspath(tmpFile.getParentDirURL())
			.build();

		run(configuration, (jetty) -> {
			HttpResponse rsp = get(jetty.getUrl());
			assertThat(rsp.getStatusCode()).isEqualTo(200);
			assertThat(rsp.getResponseBody()).isNotEmpty();

			WebAppContext ctx = (WebAppContext) jetty.getDelegate().getHandler();
			ClassLoader cl = ctx.getClassLoader();
			assertThat(cl).isNotNull();
			assertThat(cl.getResource("custom-web.xml")).isNotNull();
			assertThat(cl.getResource(tmpFile.getName())).isNotNull();
		});
	}

	@Test
	void it_should_override_web_xml() {
		File customWebXml = getFileFromClasspath("/custom-web.xml");
		EmbeddedJettyConfiguration configuration = EmbeddedJettyConfiguration.builder()
			.withWebapp(customWebXml.getParentFile())
			.withOverrideDescriptor(customWebXml.getAbsolutePath())
			.build();

		run(configuration, (jetty) -> {
			HttpResponse rsp = get(jetty.getUrl() + "hello");
			assertThat(rsp.getStatusCode()).isEqualTo(200);
			assertThat(rsp.getResponseBody()).isNotEmpty().contains("Hello World");
		});
	}

	private static void run(Consumer<EmbeddedJetty> testFn) {
		EmbeddedJetty jetty = new EmbeddedJetty();
		doRun(jetty, () -> testFn.accept(jetty));
	}

	private static void run(EmbeddedJettyConfiguration configuration, Consumer<EmbeddedJetty> testFn) {
		EmbeddedJetty jetty = new EmbeddedJetty(configuration);
		doRun(jetty, () -> testFn.accept(jetty));
	}

	private static void doRun(EmbeddedJetty jetty, Runnable testFn) {
		jetty.start();

		try {
			testFn.run();
		}
		finally {
			jetty.stop();
		}
	}
}
