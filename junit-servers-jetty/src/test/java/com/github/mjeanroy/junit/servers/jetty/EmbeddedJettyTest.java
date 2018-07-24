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

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

import static org.assertj.core.api.Assertions.assertThat;

public class EmbeddedJettyTest {

	@Rule
	public TemporaryFolder tmp = new TemporaryFolder();

	private volatile EmbeddedJetty jetty;

	@After
	public void tearDown() {
		jetty.stop();
	}

	@Test
	public void it_should_start_jetty() {
		jetty = new EmbeddedJetty();
		assertThat(jetty.getScheme()).isEqualTo("http");
		assertThat(jetty.getHost()).isEqualTo("localhost");
		assertThat(jetty.getPath()).isEqualTo("/");

		jetty.start();
		assertThat(jetty.isStarted()).isTrue();
		assertThat(jetty.getPort()).isNotZero();
		assertThat(jetty.getUrl()).isEqualTo(localUrl(jetty.getPort()));
	}

	@Test
	public void it_should_stop_jetty() {
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
	public void it_should_get_configuration_port_until_jetty_is_started() {
		jetty = new EmbeddedJetty();
		assertThat(jetty.getPort()).isZero();

		jetty.start();
		assertThat(jetty.getPort()).isNotZero();

		jetty.stop();
		assertThat(jetty.getPort()).isZero();
	}

	@Test
	public void it_should_get_servlet_context() {
		jetty = new EmbeddedJetty();
		jetty.start();
		assertThat(jetty.getServletContext()).isNotNull();
	}

	@Test
	public void it_should_get_original_jetty() {
		jetty = new EmbeddedJetty();
		assertThat(jetty.getDelegate()).isNotNull();
	}

	@Test
	public void it_should_add_parent_classloader() throws Exception {
		File tmpFile = tmp.newFile();
		File dir = tmpFile.getParentFile();
		URL url = dir.toURI().toURL();
		String name = tmpFile.getName();

		URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{url});
		assertThat(urlClassLoader.getResource(name)).isNotNull();

		jetty = new EmbeddedJetty(EmbeddedJettyConfiguration.builder()
				.withWebapp(dir)
				.withParentClasspath(url)
				.build());

		jetty.start();

		WebAppContext ctx = (WebAppContext) jetty.getDelegate().getHandler();
		ClassLoader cl = ctx.getClassLoader();

		assertThat(cl).isNotNull();
		assertThat(cl.getResource("custom-web.xml")).isNotNull();
		assertThat(cl.getResource(name)).isNotNull();
	}

	@Test
	public void it_should_override_web_xml() throws Exception {
		URL resource = getClass().getResource("/custom-web.xml");
		String webXmlPath = resource.getFile();
		File descriptor = new File(webXmlPath);

		jetty = new EmbeddedJetty(EmbeddedJettyConfiguration.builder()
				.withWebapp(descriptor.getParentFile())
				.withOverrideDescriptor(descriptor.getAbsolutePath())
				.build());

		jetty.start();

		String url = jetty.getUrl() + "hello";
		OkHttpClient client = new OkHttpClient();
		Request rq = new Request.Builder().url(url).build();
		Response rsp = client.newCall(rq).execute();

		assertThat(rsp).isNotNull();
		assertThat(rsp.code()).isEqualTo(200);

		ResponseBody body = rsp.body();
		String content = body == null ? null : body.string();
		assertThat(content).isNotEmpty().contains("Hello World");
	}

	private static String localUrl(int port) {
		return "http://localhost:" + port + "/";
	}
}
