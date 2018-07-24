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

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.apache.catalina.Container;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleState;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.net.URL;

import static com.github.mjeanroy.junit.servers.tomcat.tests.Fields.readPrivate;
import static org.assertj.core.api.Assertions.assertThat;

public class EmbeddedTomcatTest {

	private static final String PATH = "junit-servers-tomcat/";

	@Rule
	public TemporaryFolder tmp = new TemporaryFolder();

	private EmbeddedTomcat tomcat;

	@After
	public void tearDown() {
		if (tomcat != null) {
			tomcat.stop();
		}
	}

	@Test
	public void it_should_start_tomcat() {
		tomcat = new EmbeddedTomcat(defaultConfiguration());
		tomcat.start();

		assertThat(tomcat.isStarted()).isTrue();
		assertThat(tomcat.getPort()).isNotZero();
		assertThat(tomcat.getScheme()).isEqualTo("http");
		assertThat(tomcat.getHost()).isEqualTo("localhost");
		assertThat(tomcat.getPath()).isEqualTo("/");
		assertThat(tomcat.getUrl()).isEqualTo(localUrl(tomcat.getPort()));
	}

	@Test
	public void it_should_stop_tomcat() {
		tomcat = new EmbeddedTomcat(defaultConfiguration());
		tomcat.start();

		assertThat(tomcat.isStarted()).isTrue();
		assertThat(tomcat.getPort()).isNotZero();
		assertThat(tomcat.getScheme()).isEqualTo("http");
		assertThat(tomcat.getHost()).isEqualTo("localhost");
		assertThat(tomcat.getPath()).isEqualTo("/");
		assertThat(tomcat.getUrl()).isEqualTo(localUrl(tomcat.getPort()));

		tomcat.stop();
		assertThat(tomcat.isStarted()).isFalse();
		assertThat(tomcat.getPort()).isZero();
		assertThat(tomcat.getScheme()).isEqualTo("http");
		assertThat(tomcat.getHost()).isEqualTo("localhost");
		assertThat(tomcat.getPath()).isEqualTo("/");
		assertThat(tomcat.getUrl()).isEqualTo(localUrl(tomcat.getPort()));
	}

	@Test
	public void it_should_destroy_context_on_stop() {
		tomcat = new EmbeddedTomcat(defaultConfiguration());
		assertThat(readPrivate(tomcat, "context")).isNull();

		tomcat.start();

		Context ctx = readPrivate(tomcat, "context");
		assertThat(ctx).isNotNull();
		assertThat(ctx.getState()).isEqualTo(LifecycleState.STARTED);

		tomcat.stop();
		assertThat(readPrivate(tomcat, "context")).isNull();
		assertThat(ctx.getState()).isEqualTo(LifecycleState.DESTROYED);
	}

	@Test
	public void it_should_delete_base_dir_on_stop() {
		tomcat = new EmbeddedTomcat(defaultConfigurationBuilder().deleteBaseDir().build());
		File baseDir = new File(tomcat.getConfiguration().getBaseDir());

		assertThat(baseDir).exists();

		tomcat.start();
		tomcat.stop();

		assertThat(baseDir).doesNotExist();
	}

	@Test
	public void it_should_keep_base_dir_on_stop() {
		tomcat = new EmbeddedTomcat(defaultConfigurationBuilder().keepBaseDir().build());
		File baseDir = new File(tomcat.getConfiguration().getBaseDir());

		assertThat(baseDir).exists();

		tomcat.start();
		tomcat.stop();

		assertThat(baseDir).exists();
	}

	@Test
	public void it_should_get_servlet_context() {
		tomcat = new EmbeddedTomcat(defaultConfiguration());
		tomcat.start();
		assertThat(tomcat.getServletContext()).isNotNull();
	}

	@Test
	public void it_should_get_original_tomcat() {
		tomcat = new EmbeddedTomcat();
		assertThat(tomcat.getDelegate()).isNotNull();
	}

	@Test
	public void it_should_create_meta_inf_directory_if_it_does_not_exist() throws Exception {
		File baseDir = tmp.newFolder();
		File metaInf = new File(baseDir, "META-INF");

		tomcat = new EmbeddedTomcat(EmbeddedTomcatConfiguration.builder()
				.withClasspath(baseDir.getAbsolutePath())
				.withWebapp(baseDir)
				.enableForceMetaInf()
				.build());

		assertThat(metaInf).doesNotExist();

		tomcat.start();
		assertThat(metaInf).exists();
	}

	@Test
	public void it_should_add_parent_classloader() throws Exception {
		File tmpFile = tmp.newFile();
		File dir = tmpFile.getParentFile();

		tomcat = new EmbeddedTomcat(EmbeddedTomcatConfiguration.builder()
				.withWebapp(dir)
				.withParentClasspath(dir.toURI().toURL())
				.build());

		tomcat.start();

		Container[] containers = tomcat.getDelegate().getHost().findChildren();
		ClassLoader cl = containers[0].getParentClassLoader();

		assertThat(cl).isNotNull();
		assertThat(cl.getResource("hello-world.html")).isNotNull();
		assertThat(cl.getResource(tmpFile.getName())).isNotNull();
	}

	@Test
	public void it_should_override_web_xml() throws Exception {
		URL resource = getClass().getResource("/custom-web.xml");
		String webXmlPath = resource.getFile();
		File descriptor = new File(webXmlPath);

		tomcat = new EmbeddedTomcat(defaultConfigurationBuilder()
				.withOverrideDescriptor(descriptor.getAbsolutePath())
				.build());

		tomcat.start();

		OkHttpClient client = new OkHttpClient();
		Request rq = new Request.Builder().url(tomcat.getUrl()).build();
		Response rsp = client.newCall(rq).execute();

		assertThat(rsp).isNotNull();
		assertThat(rsp.code()).isEqualTo(200);

		ResponseBody body = rsp.body();
		String content = body == null ? null : body.string();
		assertThat(content).isNotEmpty().contains("Hello World");
	}

	private static EmbeddedTomcatConfiguration defaultConfiguration() {
		return defaultConfigurationBuilder().build();
	}

	private static EmbeddedTomcatConfiguration.Builder defaultConfigurationBuilder() {
		try {
			String current = new File(".").getCanonicalPath();
			if (!current.endsWith("/")) {
				current += "/";
			}

			String path = current.endsWith(PATH) ? current : current + PATH;

			return EmbeddedTomcatConfiguration.builder()
					.withWebapp(path + "src/test/resources")
					.withClasspath(path + "target/classes");
		}
		catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	private static String localUrl(int port) {
		return "http://localhost:" + port + "/";
	}
}
