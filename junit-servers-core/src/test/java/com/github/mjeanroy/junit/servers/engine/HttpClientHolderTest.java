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

package com.github.mjeanroy.junit.servers.engine;

import com.github.mjeanroy.junit.servers.client.HttpClient;
import com.github.mjeanroy.junit.servers.client.HttpClientConfiguration;
import com.github.mjeanroy.junit.servers.client.HttpClientStrategy;
import com.github.mjeanroy.junit.servers.servers.AbstractConfiguration;
import com.github.mjeanroy.junit.servers.servers.EmbeddedServer;
import com.github.mjeanroy.junit.servers.utils.builders.EmbeddedServerMockBuilder;
import com.github.mjeanroy.junit.servers.utils.commons.Fields;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static com.github.mjeanroy.junit.servers.utils.commons.Fields.readPrivate;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class HttpClientHolderTest {

	private static final String OK_HTTP_FLAG = "SUPPORT_OK_HTTP3_CLIENT";
	private static final String ASYNC_HTTP_FLAG = "SUPPORT_ASYNC_HTTP_CLIENT";
	private static final String NING_ASYNC_HTTP_FLAG = "SUPPORT_NING_ASYNC_HTTP_CLIENT";
	private static final String APACHE_HTTP_FLAG = "SUPPORT_APACHE_HTTP_CLIENT";

	private static final boolean OK_HTTP = Fields.readPrivateStatic(HttpClientStrategy.class, OK_HTTP_FLAG);
	private static final boolean ASYNC_HTTP = Fields.readPrivateStatic(HttpClientStrategy.class, ASYNC_HTTP_FLAG);
	private static final boolean NING_ASYNC_HTTP = Fields.readPrivateStatic(HttpClientStrategy.class, NING_ASYNC_HTTP_FLAG);
	private static final boolean APACHE_HTTP = Fields.readPrivateStatic(HttpClientStrategy.class, APACHE_HTTP_FLAG);

	private HttpClientStrategy strategy;
	private HttpClientConfiguration configuration;
	private EmbeddedServer<? extends AbstractConfiguration> server;

	@Before
	@SuppressWarnings("unchecked")
	public void setUp() {
		this.strategy = HttpClientStrategy.AUTO;
		this.configuration = new HttpClientConfiguration.Builder().build();
		this.server = new EmbeddedServerMockBuilder().build();
	}

	@After
	public void tearDown() {
		setDetection(OK_HTTP, ASYNC_HTTP, NING_ASYNC_HTTP, APACHE_HTTP);
	}


	@Test
	public void it_should_create_client() {
		HttpClientHolder holder = new HttpClientHolder(strategy, configuration, server);
		HttpClient client = holder.get();
		assertThat(client).isNotNull();
		assertThat(client.getConfiguration()).isSameAs(configuration);
		assertThat(client.isDestroyed()).isFalse();
	}

	@Test
	public void it_should_create_client_and_return_previous_client_on_next_call() {
		HttpClientHolder holder = new HttpClientHolder(strategy, configuration, server);
		HttpClient c1 = holder.get();
		assertThat(c1).isNotNull();
		assertThat(c1.isDestroyed()).isFalse();

		HttpClient c2 = holder.get();
		assertThat(c2).isNotNull();
		assertThat(c2).isSameAs(c1);
	}

	@Test
	public void it_should_create_client_and_return_new_client_if_destroyed() {
		HttpClientHolder holder = new HttpClientHolder(strategy, configuration, server);
		HttpClient c1 = holder.get();
		assertThat(c1).isNotNull();
		assertThat(c1.isDestroyed()).isFalse();

		c1.destroy();

		HttpClient c2 = holder.get();
		assertThat(c2).isNotNull();
		assertThat(c2).isNotSameAs(c1);
		assertThat(c2.isDestroyed()).isFalse();
	}

	@Test
	public void it_should_destroy_client() {
		HttpClientHolder holder = new HttpClientHolder(strategy, configuration, server);
		HttpClient client = holder.get();
		assertThat(client.isDestroyed()).isFalse();

		holder.destroy();
		assertThat(client.isDestroyed()).isTrue();
	}

	@Test
	public void it_should_ensure_destroyed_client_is_set_to_null() {
		HttpClientHolder holder = new HttpClientHolder(strategy, configuration, server);
		HttpClient client = holder.get();
		client.destroy();
		assertThat(readPrivate(holder, "client")).isNotNull();

		holder.destroy();
		assertThat(readPrivate(holder, "client")).isNull();
	}

	@Test
	public void it_should_fail_to_get_client_without_valid_implementations() {
		setDetection(false, false, false, false);

		HttpClientHolder holder = new HttpClientHolder(strategy, configuration, server);

		String error =
			"HTTP Client cannot be created because it is not supported by the runtime environment, " +
			"please import OkHttp OR AsyncHttpClient OR Apache HttpComponent";

		assertThatThrownBy(holderGet(holder))
				.isExactlyInstanceOf(UnsupportedOperationException.class)
				.hasMessage(error);
	}

	private static void setDetection(boolean okHttp, boolean asyncHttp, boolean ningAsyncHttp, boolean apacheHttp) {
		setOkHttpFlag(okHttp);
		setAsyncHttpFlag(asyncHttp);
		setNingAsyncHttpFlag(ningAsyncHttp);
		setApacheHttpFlag(apacheHttp);
	}

	private static void setOkHttpFlag(boolean value) {
		Fields.writeStaticFinal(HttpClientStrategy.class, OK_HTTP_FLAG, value);
	}

	private static void setAsyncHttpFlag(boolean value) {
		Fields.writeStaticFinal(HttpClientStrategy.class, ASYNC_HTTP_FLAG, value);
	}

	private static void setNingAsyncHttpFlag(boolean value) {
		Fields.writeStaticFinal(HttpClientStrategy.class, NING_ASYNC_HTTP_FLAG, value);
	}

	private static void setApacheHttpFlag(boolean value) {
		Fields.writeStaticFinal(HttpClientStrategy.class, APACHE_HTTP_FLAG, value);
	}

	private ThrowingCallable holderGet(final HttpClientHolder holder) {
		return new ThrowingCallable() {
			@Override
			public void call() {
				holder.get();
			}
		};
	}
}
