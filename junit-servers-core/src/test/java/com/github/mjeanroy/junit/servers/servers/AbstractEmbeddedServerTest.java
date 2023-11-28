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

import com.github.mjeanroy.junit.servers.utils.impl.FakeEmbeddedServer;
import com.github.mjeanroy.junit.servers.utils.impl.FakeEmbeddedServerConfiguration;
import com.github.mjeanroy.junit.servers.utils.impl.FakeEmbeddedServerConfigurationBuilder;
import com.github.mjeanroy.junit.servers.utils.impl.FakeServer;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;

import java.util.concurrent.CountDownLatch;

import static com.github.mjeanroy.junit.servers.servers.FakeWorker.startWorker;
import static com.github.mjeanroy.junit.servers.servers.FakeWorker.stopWorker;
import static com.github.mjeanroy.junit.servers.testing.HttpTestUtils.localhost;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class AbstractEmbeddedServerTest {

	private FakeEmbeddedServer server = new FakeEmbeddedServer();

	@Test
	void it_should_not_be_started() {
		assertThat(server.isStarted()).isFalse();
		assertThat(server.getNbStart()).isZero();
		assertThat(server.getNbStop()).isZero();
	}

	@Test
	void it_should_have_default_configuration() {
		assertThat(server.getConfiguration()).isNotNull();
	}

	@Test
	void it_should_have_custom_configuration() {
		String path = "/foo";
		int port = 8080;
		String webapp = "/foo/bar";
		FakeEmbeddedServerConfiguration configuration = new FakeEmbeddedServerConfigurationBuilder()
			.withPath(path)
			.withPort(port)
			.withWebapp(webapp)
			.build();

		server = new FakeEmbeddedServer(configuration);

		assertThat(server.getConfiguration()).isSameAs(configuration);
	}

	@Test
	void it_should_start_server() {
		assertThat(server.getNbStart()).isZero();
		assertThat(server.getNbStop()).isZero();

		server.start();

		assertThat(server.isStarted()).isTrue();
		assertThat(server.getNbStart()).isOne();
		assertThat(server.getNbStop()).isZero();
	}

	@Test
	void it_should_not_start_server_twice() {
		assertThat(server.getNbStart()).isZero();
		assertThat(server.getNbStop()).isZero();

		server.start();
		server.start();

		assertThat(server.isStarted()).isTrue();
		assertThat(server.getNbStart()).isOne();
		assertThat(server.getNbStop()).isZero();
	}

	@Test
	void it_should_stop_server() {
		assertThat(server.getNbStop()).isZero();
		assertThat(server.getNbStart()).isZero();
		assertThat(server.isStarted()).isFalse();

		server.start();

		assertThat(server.isStarted()).isTrue();
		assertThat(server.getNbStart()).isOne();
		assertThat(server.getNbStop()).isZero();

		server.stop();

		assertThat(server.isStarted()).isFalse();
		assertThat(server.getNbStart()).isOne();
		assertThat(server.getNbStop()).isOne();
	}

	@Test
	void it_should_not_stop_server_twice() {
		assertThat(server.isStarted()).isFalse();
		assertThat(server.getNbStart()).isZero();
		assertThat(server.getNbStop()).isZero();

		server.start();

		assertThat(server.isStarted()).isTrue();
		assertThat(server.getNbStart()).isOne();
		assertThat(server.getNbStop()).isZero();

		server.stop();
		server.stop();

		assertThat(server.isStarted()).isFalse();
		assertThat(server.getNbStart()).isOne();
		assertThat(server.getNbStop()).isOne();
	}

	@Test
	void it_should_restart_server() {
		assertThat(server.isStarted()).isFalse();
		assertThat(server.getNbStart()).isZero();
		assertThat(server.getNbStop()).isZero();

		server.start();

		assertThat(server.isStarted()).isTrue();
		assertThat(server.getNbStart()).isOne();
		assertThat(server.getNbStop()).isZero();

		server.restart();

		assertThat(server.isStarted()).isTrue();
		assertThat(server.getNbStart()).isEqualTo(2);
		assertThat(server.getNbStop()).isOne();
	}

	@Test
	void it_should_block_until_server_is_started() throws Exception {
		assertThat(server.isStarted()).isFalse();
		assertThat(server.getNbStart()).isZero();
		assertThat(server.getNbStop()).isZero();

		CountDownLatch startSignal = new CountDownLatch(1);
		CountDownLatch doneSignal1 = new CountDownLatch(1);
		CountDownLatch doneSignal2 = new CountDownLatch(1);

		Thread th1 = new Thread(startWorker(server, startSignal, doneSignal1));
		Thread th2 = new Thread(startWorker(server, startSignal, doneSignal2));

		th1.start();
		th2.start();

		assertThat(server.isStarted()).isFalse();
		assertThat(server.getNbStart()).isZero();
		assertThat(server.getNbStop()).isZero();

		startSignal.countDown();

		doneSignal2.await();
		assertThat(server.isStarted()).isTrue();
		assertThat(server.getNbStart()).isOne();
		assertThat(server.getNbStop()).isZero();

		doneSignal1.await();
		assertThat(server.isStarted()).isTrue();
		assertThat(server.getNbStart()).isOne();
		assertThat(server.getNbStop()).isZero();
	}

	@Test
	void it_should_block_until_server_is_stopped() throws Exception {
		assertThat(server.isStarted()).isFalse();
		assertThat(server.getNbStart()).isZero();
		assertThat(server.getNbStop()).isZero();

		server.start();

		assertThat(server.isStarted()).isTrue();
		assertThat(server.getNbStart()).isOne();
		assertThat(server.getNbStop()).isZero();

		CountDownLatch stopSignal = new CountDownLatch(1);
		CountDownLatch doneSignal1 = new CountDownLatch(1);
		CountDownLatch doneSignal2 = new CountDownLatch(1);

		Thread th1 = new Thread(stopWorker(server, stopSignal, doneSignal1));
		Thread th2 = new Thread(stopWorker(server, stopSignal, doneSignal2));

		th1.start();
		th2.start();

		assertThat(server.isStarted()).isTrue();
		assertThat(server.getNbStart()).isOne();
		assertThat(server.getNbStop()).isZero();

		stopSignal.countDown();

		doneSignal2.await();
		assertThat(server.isStarted()).isFalse();
		assertThat(server.getNbStart()).isOne();
		assertThat(server.getNbStop()).isOne();

		doneSignal1.await();
		assertThat(server.isStarted()).isFalse();
		assertThat(server.getNbStart()).isOne();
		assertThat(server.getNbStop()).isOne();
	}

	@Test
	void it_should_set_environment_properties() {
		String name1 = "foo";
		String oldValue1 = "foo";
		String newValue1 = "bar";
		System.setProperty(name1, oldValue1);

		String name2 = "foo1";
		String newValue2 = "bar1";

		FakeEmbeddedServerConfiguration configuration = new FakeEmbeddedServerConfigurationBuilder()
			.withProperty(name1, newValue1)
			.withProperty(name2, newValue2)
			.build();

		server = new FakeEmbeddedServer(configuration);

		server.start();

		assertThat(System.getProperty(name1)).isEqualTo(newValue1);
		assertThat(System.getProperty(name2)).isEqualTo(newValue2);

		server.stop();

		assertThat(System.getProperty(name1)).isEqualTo(oldValue1);
		assertThat(System.getProperty(name2)).isNull();

		System.clearProperty(name1);
		System.clearProperty(name2);
	}

	@Test
	void it_should_execute_hook() {
		Hook hook = mock(Hook.class);
		FakeEmbeddedServerConfiguration configuration = new FakeEmbeddedServerConfigurationBuilder().withHook(hook).build();

		server = new FakeEmbeddedServer(configuration);

		server.start();

		verify(hook).pre(server);
		verify(hook).onStarted(server, server.getServletContext());
		verify(hook, never()).post(server);

		server.stop();

		verify(hook).post(server);
		verify(hook, times(1)).pre(server);
		verify(hook, times(1)).pre(server);
	}

	@Test
	void it_should_execute_hook_before_doStop() {
		Hook hook = mock(Hook.class);
		FakeEmbeddedServerConfiguration configuration = new FakeEmbeddedServerConfigurationBuilder().withHook(hook).build();

		Answer<Object> ensureIsStillStartedAnswer = invocation -> {
			FakeEmbeddedServer server = invocation.getArgument(0);
			assertThat(server).isNotNull();
			assertThat(server.getNbStop()).isZero();
			return null;
		};

		doAnswer(ensureIsStillStartedAnswer).when(hook).post(any(EmbeddedServer.class));

		server = spy(new FakeEmbeddedServer(configuration));
		server.start();
		assertThat(server.isStarted()).isTrue();
		verify(hook, never()).post(any(EmbeddedServer.class));

		server.stop();
		assertThat(server.isStarted()).isFalse();
		assertThat(server.getNbStop()).isEqualTo(1);
	}

	@Test
	void it_should_get_path() {
		assertThat(server.getPath()).isEqualTo("/");
	}

	@Test
	void it_should_get_custom_path() {
		FakeEmbeddedServer server = new FakeEmbeddedServer(new FakeEmbeddedServerConfigurationBuilder().withPath("/foo").build());
		assertThat(server.getPath()).isEqualTo("/foo");
	}

	@Test
	void it_should_get_url() {
		assertThat(server.getUrl()).isEqualTo(localhost(0));
	}

	@Test
	void it_should_get_url_with_custom_path() {
		FakeEmbeddedServer server = new FakeEmbeddedServer(new FakeEmbeddedServerConfigurationBuilder().withPath("/foo").build());
		assertThat(server.getUrl()).isEqualTo(localhost(server.getPort(), "/foo"));
	}

	@Test
	void it_should_get_url_with_custom_path_pre_pending_slash() {
		FakeEmbeddedServer server = new FakeEmbeddedServer(new FakeEmbeddedServerConfigurationBuilder().withPath("foo").build());
		assertThat(server.getUrl()).isEqualTo(localhost(0, "/foo"));
	}

	@Test
	void it_should_get_url_and_do_not_encode_custom_path() {
		FakeEmbeddedServerConfiguration configuration = new FakeEmbeddedServerConfigurationBuilder().withPath("/foo bar").build();
		FakeEmbeddedServer server = new FakeEmbeddedServer(configuration);
		String url = server.getUrl();

		assertThat(url).isEqualTo(localhost(server.getPort(), "/foo bar"));
	}

	@Test
	void it_should_get_original_server_implementation() {
		FakeEmbeddedServerConfiguration configuration = new FakeEmbeddedServerConfigurationBuilder().withPath("foo").build();
		FakeEmbeddedServer server = new FakeEmbeddedServer(configuration);
		FakeServer delegate = server.getDelegate();
		assertThat(delegate).isNotNull();
	}
}
