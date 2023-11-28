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

package com.github.mjeanroy.junit.servers.tomcat10;

import com.github.mjeanroy.junit.servers.testing.HttpTestUtils.HttpResponse;
import com.github.mjeanroy.junit.servers.tomcat.EmbeddedTomcatConfiguration;
import org.apache.catalina.Container;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleState;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Files;
import java.util.function.Consumer;

import static com.github.mjeanroy.junit.servers.testing.HttpTestUtils.get;
import static com.github.mjeanroy.junit.servers.testing.HttpTestUtils.localhost;
import static com.github.mjeanroy.junit.servers.testing.IoTestUtils.getFileFromClasspath;
import static com.github.mjeanroy.junit.servers.testing.ReflectionTestUtils.readPrivate;
import static org.assertj.core.api.Assertions.assertThat;

class EmbeddedTomcatTest {

	private static final String PATH = "junit-servers-tomcat-10/";

	@Test
	void it_should_start_tomcat() {
		run(defaultConfiguration(), (tomcat) -> {
			assertThat(tomcat.isStarted()).isTrue();
			assertThat(tomcat.getPort()).isNotZero();
			assertThat(tomcat.getScheme()).isEqualTo("http");
			assertThat(tomcat.getHost()).isEqualTo("localhost");
			assertThat(tomcat.getPath()).isEqualTo("/");
			assertThat(tomcat.getUrl()).isEqualTo(localhost(tomcat.getPort()));
		});
	}

	@Test
	void it_should_stop_tomcat() {
		EmbeddedTomcat tomcat = new EmbeddedTomcat(defaultConfiguration());

		doRun(tomcat, () -> {
			assertThat(tomcat.isStarted()).isTrue();
			assertThat(tomcat.getPort()).isNotZero();
			assertThat(tomcat.getScheme()).isEqualTo("http");
			assertThat(tomcat.getHost()).isEqualTo("localhost");
			assertThat(tomcat.getPath()).isEqualTo("/");
			assertThat(tomcat.getUrl()).isEqualTo(localhost(tomcat.getPort()));
		});

		assertThat(tomcat.isStarted()).isFalse();
		assertThat(tomcat.getPort()).isZero();
		assertThat(tomcat.getScheme()).isEqualTo("http");
		assertThat(tomcat.getHost()).isEqualTo("localhost");
		assertThat(tomcat.getPath()).isEqualTo("/");
		assertThat(tomcat.getUrl()).isEqualTo(localhost(tomcat.getPort()));
	}

	@Test
	void it_should_destroy_context_on_stop() {
		EmbeddedTomcat tomcat = new EmbeddedTomcat(defaultConfiguration());
		assertThat((Context) readPrivate(tomcat, "context")).isNull();

		WrappedContext startedContext = new WrappedContext();

		doRun(tomcat, () -> {
			startedContext.ctx = readPrivate(tomcat, "context");
			assertThat(startedContext.ctx).isNotNull();
			assertThat(startedContext.ctx.getState()).isEqualTo(LifecycleState.STARTED);
		});

		assertThat((Context) readPrivate(tomcat, "context")).isNull();
		assertThat(startedContext.ctx.getState()).isEqualTo(LifecycleState.DESTROYED);
	}

	@Test
	void it_should_delete_base_dir_on_stop() {
		EmbeddedTomcatConfiguration configuration = defaultConfigurationBuilder().deleteBaseDir().build();
		File baseDir = new File(configuration.getBaseDir());

		run(configuration, (tomcat) ->
			assertThat(baseDir).exists()
		);

		assertThat(baseDir).doesNotExist();
	}

	@Test
	void it_should_keep_base_dir_on_stop() {
		EmbeddedTomcatConfiguration configuration = defaultConfigurationBuilder().keepBaseDir().build();
		File baseDir = new File(configuration.getBaseDir());

		run(configuration, (tomcat) ->
			assertThat(baseDir).exists()
		);

		assertThat(baseDir).exists();
	}

	@Test
	void it_should_get_servlet_context() {
		run(defaultConfiguration(), (tomcat) ->
			assertThat(tomcat.getServletContext()).isNotNull()
		);
	}

	@Test
	void it_should_get_original_tomcat() {
		run((tomcat) ->
			assertThat(tomcat.getDelegate()).isNotNull()
		);
	}

	@Test
	void it_should_create_meta_inf_directory_if_it_does_not_exist(@TempDir File baseDir) throws Exception {
		File metaInf = new File(baseDir, "META-INF");
		EmbeddedTomcatConfiguration configuration = EmbeddedTomcatConfiguration.builder()
			.withClasspath(baseDir.getAbsolutePath())
			.withWebapp(baseDir)
			.enableForceMetaInf()
			.build();

		assertThat(metaInf).doesNotExist();

		run(configuration, (tomcat) ->
			assertThat(metaInf).exists()
		);
	}

	@Test
	void it_should_add_parent_classloader(@TempDir File tmpDir) throws Exception {
		File tmpFile = Files.createTempFile(tmpDir.toPath(), null, null).toFile();
		File dir = tmpFile.getParentFile();
		EmbeddedTomcatConfiguration configuration = EmbeddedTomcatConfiguration.builder()
			.withWebapp(dir)
			.withParentClasspath(dir.toURI().toURL())
			.build();

		run(configuration, (tomcat) -> {
			Container[] containers = tomcat.getDelegate().getHost().findChildren();
			ClassLoader cl = containers[0].getParentClassLoader();

			assertThat(cl).isNotNull();
			assertThat(cl.getResource("hello-world.html")).isNotNull();
			assertThat(cl.getResource(tmpFile.getName())).isNotNull();
		});
	}

	@Test
	void it_should_override_web_xml() {
		File descriptor = getFileFromClasspath("/custom-web.xml");
		EmbeddedTomcatConfiguration configuration = defaultConfigurationBuilder()
			.withOverrideDescriptor(descriptor.getAbsolutePath())
			.build();

		run(configuration, (tomcat) -> {
			HttpResponse rsp = get(tomcat.getUrl());
			assertThat(rsp).isNotNull();
			assertThat(rsp.getStatusCode()).isEqualTo(200);
			assertThat(rsp.getResponseBody()).isNotEmpty().contains("Hello World");
		});
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

	private static void run(Consumer<EmbeddedTomcat> testFn) {
		EmbeddedTomcat tomcat = new EmbeddedTomcat();
		doRun(tomcat, () ->
			testFn.accept(tomcat)
		);
	}

	private static void run(EmbeddedTomcatConfiguration configuration, Consumer<EmbeddedTomcat> testFn) {
		EmbeddedTomcat tomcat = new EmbeddedTomcat(configuration);
		doRun(tomcat, () ->
			testFn.accept(tomcat)
		);
	}

	private static void doRun(EmbeddedTomcat tomcat, Runnable testFn) {
		tomcat.start();
		try {
			testFn.run();
		}
		finally {
			tomcat.stop();
		}
	}

	private static final class WrappedContext {
		Context ctx = null;
	}
}
