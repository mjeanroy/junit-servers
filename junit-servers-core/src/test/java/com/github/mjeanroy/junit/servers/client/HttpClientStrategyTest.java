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

package com.github.mjeanroy.junit.servers.client;

import com.github.mjeanroy.junit.servers.client.impl.apache.ApacheHttpClient;
import com.github.mjeanroy.junit.servers.client.impl.async.AsyncHttpClient;
import com.github.mjeanroy.junit.servers.client.impl.ning.NingAsyncHttpClient;
import com.github.mjeanroy.junit.servers.client.impl.okhttp3.OkHttpClient;
import com.github.mjeanroy.junit.servers.servers.EmbeddedServer;
import com.github.mjeanroy.junit.servers.utils.builders.EmbeddedServerMockBuilder;
import com.github.mjeanroy.junit.servers.utils.commons.Fields;
import com.github.mjeanroy.junit4.runif.RunIf;
import com.github.mjeanroy.junit4.runif.RunIfRunner;
import com.github.mjeanroy.junit4.runif.conditions.AtLeastJava8Condition;
import com.github.mjeanroy.junit4.runif.conditions.Java7Condition;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@RunWith(RunIfRunner.class)
public class HttpClientStrategyTest {

	private static final String OK_HTTP_FLAG = "SUPPORT_OK_HTTP3_CLIENT";
	private static final String ASYNC_HTTP_FLAG = "SUPPORT_ASYNC_HTTP_CLIENT";
	private static final String NING_ASYNC_HTTP_FLAG = "SUPPORT_NING_ASYNC_HTTP_CLIENT";
	private static final String APACHE_HTTP_FLAG = "SUPPORT_APACHE_HTTP_CLIENT";

	private static final boolean OK_HTTP = Fields.readPrivateStatic(HttpClientStrategy.class, OK_HTTP_FLAG);
	private static final boolean ASYNC_HTTP = Fields.readPrivateStatic(HttpClientStrategy.class, ASYNC_HTTP_FLAG);
	private static final boolean NING_ASYNC_HTTP = Fields.readPrivateStatic(HttpClientStrategy.class, NING_ASYNC_HTTP_FLAG);
	private static final boolean APACHE_HTTP = Fields.readPrivateStatic(HttpClientStrategy.class, APACHE_HTTP_FLAG);

	private EmbeddedServer<?> server;

	@Before
	public void setUp() {
		server = new EmbeddedServerMockBuilder().build();
	}

	@After
	public void tearDown() {
		setDetection(OK_HTTP, ASYNC_HTTP, NING_ASYNC_HTTP, APACHE_HTTP);
	}

	@Test
	public void it_should_create_apache_http_client() {
		testHttpClient(HttpClientStrategy.APACHE_HTTP_CLIENT, ApacheHttpClient.class);
	}

	@Test
	@RunIf(AtLeastJava8Condition.class)
	public void it_should_create_async_http_client() {
		testHttpClient(HttpClientStrategy.ASYNC_HTTP_CLIENT, AsyncHttpClient.class);
	}

	@Test
	public void it_should_create_ning_async_http_client() {
		testHttpClient(HttpClientStrategy.NING_ASYNC_HTTP_CLIENT, NingAsyncHttpClient.class);
	}

	@Test
	public void it_should_create_ok_http_client() {
		testHttpClient(HttpClientStrategy.OK_HTTP3, OkHttpClient.class);
	}

	@Test
	public void it_should_fail_create_apache_http_client_if_it_does_not_exist() {
		setApacheHttpFlag(false);
		testHttpClientWithoutImpl(HttpClientStrategy.APACHE_HTTP_CLIENT, "Apache HttpComponent");
	}

	@Test
	@RunIf(AtLeastJava8Condition.class)
	public void it_should_fail_to_create_async_http_client_if_it_does_not_exist() {
		setAsyncHttpFlag(false);
		testHttpClientWithoutImpl(HttpClientStrategy.ASYNC_HTTP_CLIENT, "AsyncHttpClient");
	}

	@Test
	public void it_should_fail_to_create_ning_async_http_client_if_it_does_not_exist() {
		setNingAsyncHttpFlag(false);
		testHttpClientWithoutImpl(HttpClientStrategy.NING_ASYNC_HTTP_CLIENT, "(Ning) AsyncHttpClient");
	}

	@Test
	public void it_should_fail_to_create_ok_http_client_if_it_does_not_exist() {
		setOkHttpFlag(false);
		testHttpClientWithoutImpl(HttpClientStrategy.OK_HTTP3, "OkHttp");
	}

	@Test
	public void it_should_fail_create_apache_http_client_with_custom_configuration_if_it_does_not_exist() {
		setApacheHttpFlag(false);
		testHttpClientWithConfigurationWithoutImpl(HttpClientStrategy.APACHE_HTTP_CLIENT, "Apache HttpComponent");
	}

	@Test
	@RunIf(AtLeastJava8Condition.class)
	public void it_should_fail_to_create_async_http_client_with_custom_configuration_if_it_does_not_exist() {
		setAsyncHttpFlag(false);
		testHttpClientWithConfigurationWithoutImpl(HttpClientStrategy.ASYNC_HTTP_CLIENT, "AsyncHttpClient");
	}

	@Test
	public void it_should_fail_to_create_ning_async_http_client_with_custom_configuration_if_it_does_not_exist() {
		setNingAsyncHttpFlag(false);
		testHttpClientWithConfigurationWithoutImpl(HttpClientStrategy.NING_ASYNC_HTTP_CLIENT, "(Ning) AsyncHttpClient");
	}

	@Test
	public void it_should_fail_to_create_ok_http_client_with_custom_configuration_if_it_does_not_exist() {
		setOkHttpFlag(false);
		testHttpClientWithConfigurationWithoutImpl(HttpClientStrategy.OK_HTTP3, "OkHttp");
	}

	@Test
	@RunIf(Java7Condition.class)
	public void it_should_fail_to_create_async_http_client_if_runtime_is_java7() {
		String error =
			"HTTP Client cannot be created because it is not supported by the runtime environment, " +
			"please import AsyncHttpClient";

		assertThatThrownBy(build(HttpClientStrategy.ASYNC_HTTP_CLIENT, server))
				.isExactlyInstanceOf(UnsupportedOperationException.class)
				.hasMessage(error);
	}

	@Test
	@RunIf(Java7Condition.class)
	public void it_should_fail_to_create_async_http_client_with_custom_configuration_if_runtime_is_java7() {
		HttpClientConfiguration configuration = new HttpClientConfiguration.Builder()
			.disableFollowRedirect()
			.build();

		String error =
			"HTTP Client cannot be created because it is not supported by the runtime environment, " +
			"please import AsyncHttpClient";

		assertThatThrownBy(build(HttpClientStrategy.ASYNC_HTTP_CLIENT, configuration, server))
				.isExactlyInstanceOf(UnsupportedOperationException.class)
				.hasMessage(error);
	}

	@Test
	public void it_should_create_automatic_http_client_with_ok_http_client() {
		setDetection(true, true, true, true);
		testAuto(OkHttpClient.class);
	}

	@Test
	@RunIf(AtLeastJava8Condition.class)
	public void it_should_create_automatic_http_client_with_async_http_client_on_java_8() {
		setOkHttpFlag(false);
		testAuto(AsyncHttpClient.class);
	}

	@Test
	@RunIf(Java7Condition.class)
	public void it_should_create_automatic_http_client_with_ning_async_http_client_on_java_7() {
		setOkHttpFlag(false);
		testAuto(NingAsyncHttpClient.class);
	}

	@Test
	public void it_should_create_automatic_http_client_with_ning_async_http_client() {
		setDetection(false, false, true, true);
		testAuto(NingAsyncHttpClient.class);
	}

	@Test
	public void it_should_create_automatic_http_client_with_apache_http_client() {
		setDetection(false, false, false, true);
		testAuto(ApacheHttpClient.class);
	}

	@Test
	public void it_should_not_create_automatic_http_client_and_fail_without_implementation() {
		setDetection(false, false, false, false);
		testHttpClientWithoutImpl(HttpClientStrategy.AUTO, "OkHttp OR AsyncHttpClient OR Apache HttpComponent");
	}

	@Test
	public void it_should_not_create_automatic_http_client_with_custom_configuration_and_fail_without_implementation() {
		setDetection(false, false, false, false);
		testHttpClientWithConfigurationWithoutImpl(HttpClientStrategy.AUTO, "OkHttp OR AsyncHttpClient OR Apache HttpComponent");
	}

	private void testHttpClient(HttpClientStrategy strategy, Class<?> expectedImpl) {
		assertThat(strategy.support()).isTrue();

		// --- With default configuration.

		HttpClient c1 = strategy.build(server);
		assertThat(c1).isExactlyInstanceOf(expectedImpl);

		// --- With custom configuration.

		HttpClientConfiguration configuration = new HttpClientConfiguration.Builder()
			.disableFollowRedirect()
			.build();

		HttpClient c2 = strategy.build(configuration, server);

		assertThat(c2).isExactlyInstanceOf(expectedImpl);
		assertThat(c2.getConfiguration()).isSameAs(configuration);
	}

	private void testAuto(Class<?> expectedImpl) {
		assertThat(HttpClientStrategy.AUTO.support()).isTrue();

		// --- With default configuration.

		HttpClient c1 = HttpClientStrategy.AUTO.build(server);
		assertThat(c1).isExactlyInstanceOf(expectedImpl);
		assertThat(c1.getConfiguration()).isEqualTo(HttpClientConfiguration.defaultConfiguration());

		// --- With custom configuration.

		HttpClientConfiguration configuration = new HttpClientConfiguration.Builder()
			.disableFollowRedirect()
			.build();

		HttpClient c2 = HttpClientStrategy.AUTO.build(configuration, server);
		assertThat(c2).isExactlyInstanceOf(expectedImpl);
		assertThat(c2.getConfiguration()).isSameAs(configuration);
	}

	private void testHttpClientWithoutImpl(HttpClientStrategy strategy, String library) {
		assertThat(strategy.support()).isFalse();

		String error =
			"HTTP Client cannot be created because it is not supported by the runtime environment, " +
			"please import " + library;

		assertThatThrownBy(build(strategy, server))
				.isExactlyInstanceOf(UnsupportedOperationException.class)
				.hasMessage(error);
	}

	private void testHttpClientWithConfigurationWithoutImpl(HttpClientStrategy strategy, String library) {
		assertThat(strategy.support()).isFalse();

		HttpClientConfiguration configuration = new HttpClientConfiguration.Builder()
			.disableFollowRedirect()
			.build();

		String error =
			"HTTP Client cannot be created because it is not supported by the runtime environment, " +
			"please import " + library;

		assertThatThrownBy(build(strategy, configuration, server))
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

	private static ThrowingCallable build(final HttpClientStrategy strategy, final EmbeddedServer<?> server) {
		return new ThrowingCallable() {
			@Override
			public void call() {
				strategy.build(server);
			}
		};
	}

	private static ThrowingCallable build(final HttpClientStrategy strategy, final HttpClientConfiguration configuration, final EmbeddedServer<?> server) {
		return new ThrowingCallable() {
			@Override
			public void call() {
				strategy.build(configuration, server);
			}
		};
	}
}
