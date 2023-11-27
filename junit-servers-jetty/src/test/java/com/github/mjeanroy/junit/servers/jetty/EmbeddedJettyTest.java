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

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("deprecation")
class EmbeddedJettyTest {

	private volatile EmbeddedJetty jetty;

	@AfterEach
	void tearDown() {
		jetty.stop();
	}

	@Test
	void it_should_start_jetty() throws Exception {
		jetty = new EmbeddedJetty();
		assertThat(jetty.getScheme()).isEqualTo("http");
		assertThat(jetty.getHost()).isEqualTo("localhost");
		assertThat(jetty.getPath()).isEqualTo("/");

		jetty.start();
		assertThat(jetty.isStarted()).isTrue();
		assertThat(jetty.getPort()).isNotZero();
		assertThat(jetty.getUrl()).isEqualTo(localUrl(jetty.getPort()));

		// Since there is no webapp deployed, we should get a 404
		HttpResponse rsp = get(jetty.getUrl());
		assertThat(rsp.statusCode).isEqualTo(404);
		assertThat(rsp.responseBody).isNotEmpty();
	}

	@Test
	void it_should_stop_jetty() {
		jetty = new EmbeddedJetty();
		assertThat(jetty.getScheme()).isEqualTo("http");
		assertThat(jetty.getHost()).isEqualTo("localhost");
		assertThat(jetty.getPath()).isEqualTo("/");

		jetty.start();
		assertThat(jetty.isStarted()).isTrue();
		assertThat(jetty.getPort()).isNotZero();
		assertThat(jetty.getUrl()).isEqualTo(localUrl(jetty.getPort()));

		jetty.stop();
		assertThat(jetty.isStarted()).isFalse();
		assertThat(jetty.getPort()).isZero();
		assertThat(jetty.getUrl()).isEqualTo(localUrl(jetty.getPort()));
	}

	@Test
	void it_should_get_configuration_port_until_jetty_is_started() {
		jetty = new EmbeddedJetty();
		assertThat(jetty.getPort()).isZero();

		jetty.start();
		assertThat(jetty.getPort()).isNotZero();

		jetty.stop();
		assertThat(jetty.getPort()).isZero();
	}

	@Test
	void it_should_get_servlet_context() {
		jetty = new EmbeddedJetty();
		jetty.start();
		assertThat(jetty.getServletContext()).isNotNull();
	}

	@Test
	void it_should_get_original_jetty() {
		jetty = new EmbeddedJetty();
		assertThat(jetty.getDelegate()).isNotNull();
	}

	@Test
	void it_should_add_parent_classloader(@TempDir Path tmp) throws Exception {
		final File tmpFile = Files.createTempFile(tmp, null, null).toFile();
		final File dir = tmpFile.getParentFile();
		final URL url = dir.toURI().toURL();
		final String name = tmpFile.getName();

		try (URLClassLoader urlClassLoader = new URLClassLoader(new URL[] { url })) {
			assertThat(urlClassLoader.getResource(name)).isNotNull();

			jetty = new EmbeddedJetty(EmbeddedJettyConfiguration.builder()
				.withWebapp(dir)
				.withParentClasspath(url)
				.build());

			jetty.start();

			HttpResponse rsp = get(jetty.getUrl());
			assertThat(rsp.statusCode).isEqualTo(200);
			assertThat(rsp.responseBody).isNotEmpty();

			WebAppContext ctx = (WebAppContext) jetty.getDelegate().getHandler();
			ClassLoader cl = ctx.getClassLoader();
			assertThat(cl).isNotNull();
			assertThat(cl.getResource("custom-web.xml")).isNotNull();
			assertThat(cl.getResource(name)).isNotNull();
		}
	}

	@Test
	void it_should_override_web_xml() throws Exception {
		File customWebXml = getCustomWebXml();

		jetty = new EmbeddedJetty(EmbeddedJettyConfiguration.builder()
			.withWebapp(customWebXml.getParentFile())
			.withOverrideDescriptor(customWebXml.getAbsolutePath())
			.build());

		jetty.start();

		HttpResponse rsp = get(jetty.getUrl() + "hello");
		assertThat(rsp.statusCode).isEqualTo(200);
		assertThat(rsp.responseBody).isNotEmpty().contains("Hello World");
	}

	private static File getCustomWebXml() {
		String resourceName = "/custom-web.xml";
		URL resource = EmbeddedJettyTest.class.getResource("/custom-web.xml");
		if (resource == null) {
			throw new AssertionError("Cannot find resource: " + resourceName);
		}

		String webXmlPath = resource.getFile();
		return new File(webXmlPath);
	}

	private static HttpResponse get(String url) throws IOException {
		OkHttpClient client = new OkHttpClient();
		Request rq = new Request.Builder().url(url).build();
		try (Response rsp = client.newCall(rq).execute()) {
			int statusCode = rsp.code();
			String responseBody = rsp.body() == null ? null : rsp.body().string();
			return new HttpResponse(statusCode, responseBody);
		}
	}

	private static String localUrl(int port) {
		return "http://localhost:" + port + "/";
	}

	private static final class HttpResponse {
		private final int statusCode;
		private final String responseBody;

		private HttpResponse(int statusCode, String responseBody) {
			this.statusCode = statusCode;
			this.responseBody = responseBody;
		}
	}
}
