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

package com.github.mjeanroy.junit.servers.servers;

import static org.assertj.core.api.Assertions.*;

import org.junit.Test;

public class AbstractEmbeddedServerTest {

	private TestServer server = new TestServer();

	@Test
	public void it_should_not_be_started() {
		assertThat(server.isStarted()).isFalse();
	}

	@Test
	public void it_should_have_default_configuration() {
		assertThat(server.path).isEqualTo("/");
		assertThat(server.webapp).isEqualTo("src/main/webapp");
		assertThat(server.port).isZero();
	}

	@Test
	public void it_should_have_custom_configuration() {
		String path = "/foo";
		int port = 8080;
		String webapp = "/foo/bar";
		AbstractEmbeddedServerConfiguration configuration = new EmbeddedConfiguration()
				.withPath(path)
				.withPort(port)
				.withWebapp(webapp);

		server = new TestServer(configuration);

		assertThat(server.path).isEqualTo(path);
		assertThat(server.webapp).isEqualTo(webapp);
		assertThat(server.port).isEqualTo(port);
	}

	@Test
	public void it_should_start_server() {
		assertThat(server.doStart).isZero();

		server.start();

		assertThat(server.isStarted()).isTrue();
		assertThat(server.doStart).isNotZero().isEqualTo(1);
	}

	@Test
	public void it_should_not_start_server_twice() {
		assertThat(server.doStart).isZero();

		server.start();
		server.start();

		assertThat(server.isStarted()).isTrue();
		assertThat(server.doStart).isNotZero().isEqualTo(1);
	}

	@Test
	public void it_should_stop_server() {
		assertThat(server.doStop).isZero();
		assertThat(server.isStarted()).isFalse();

		server.start();
		assertThat(server.isStarted()).isTrue();

		server.stop();

		assertThat(server.isStarted()).isFalse();
		assertThat(server.doStop).isNotZero().isEqualTo(1);
	}

	@Test
	public void it_should_not_stop_server_twice() {
		assertThat(server.doStop).isZero();
		assertThat(server.isStarted()).isFalse();

		server.start();
		assertThat(server.isStarted()).isTrue();

		server.stop();
		server.stop();

		assertThat(server.isStarted()).isFalse();
		assertThat(server.doStop).isNotZero().isEqualTo(1);
	}

	@Test
	public void it_should_restart_server() {
		assertThat(server.doStop).isZero();
		assertThat(server.doStart).isZero();
		assertThat(server.isStarted()).isFalse();

		server.start();
		assertThat(server.isStarted()).isTrue();
		assertThat(server.doStart).isNotZero().isEqualTo(1);

		server.restart();

		assertThat(server.isStarted()).isTrue();
		assertThat(server.doStop).isNotZero().isEqualTo(1);
		assertThat(server.doStart).isNotZero().isEqualTo(2);
	}

	private static class TestServer extends AbstractEmbeddedServer {

		public int doStart = 0;

		public int doStop = 0;

		public TestServer() {
			super(new EmbeddedConfiguration());
		}

		public TestServer(AbstractEmbeddedServerConfiguration configuration) {
			super(configuration);
		}

		@Override
		protected void doStart() {
			doStart++;
		}

		@Override
		protected void doStop() {
			doStop++;
		}

		@Override
		public int getPort() {
			return 0;
		}
	}

	private static class EmbeddedConfiguration extends AbstractEmbeddedServerConfiguration<EmbeddedConfiguration> {
	}
}
