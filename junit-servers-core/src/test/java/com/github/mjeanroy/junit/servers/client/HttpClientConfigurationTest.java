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

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

public class HttpClientConfigurationTest {

	private static final String COOKIE_NAME = "JSESSIONID";
	private static final String USER_AGENT_NAME = "User-Agent";

	@Test
	public void it_should_create_default_configuration() {
		HttpClientConfiguration configuration = HttpClientConfiguration.defaultConfiguration();
		assertThat(configuration).isNotNull();
		assertThat(configuration.isFollowRedirect()).isTrue();
		assertThat(configuration.getDefaultCookies()).isNotNull().isEmpty();
		assertThat(configuration.getDefaultHeaders()).isNotNull().isEmpty();
	}

	@Test
	public void it_should_create_custom_configuration() {
		String ua = "Mozilla/5.0 (X11; U; Linux x86_64; en-US) AppleWebKit/532.0 (KHTML, like Gecko) Chrome/4.0.202.0 Safari/532.0";
		String jsessionId = UUID.randomUUID().toString();

		HttpClientConfiguration configuration = new HttpClientConfiguration.Builder()
			.disableFollowRedirect()
			.addDefaultCookie(COOKIE_NAME, jsessionId)
			.addDefaultHeader(USER_AGENT_NAME, ua)
			.build();

		assertThat(configuration).isNotNull();
		assertThat(configuration.isFollowRedirect()).isFalse();

		assertThat(configuration.getDefaultCookies())
			.isNotEmpty()
			.hasSize(1)
			.containsOnly(Cookies.cookie(COOKIE_NAME, jsessionId));

		assertThat(configuration.getDefaultHeaders())
			.isNotEmpty()
			.hasSize(1)
			.containsOnly(
				entry(USER_AGENT_NAME, HttpHeader.header(USER_AGENT_NAME, ua))
			);
	}

	@Test
	public void it_should_create_custom_configuration_with_follow_redirect() {
		HttpClientConfiguration configuration = new HttpClientConfiguration.Builder()
			.enableFollowRedirect()
			.build();

		assertThat(configuration).isNotNull();
		assertThat(configuration.isFollowRedirect()).isTrue();
	}

	@Test
	public void it_should_create_custom_configuration_without_follow_redirect() {
		HttpClientConfiguration configuration = new HttpClientConfiguration.Builder()
			.disableFollowRedirect()
			.build();

		assertThat(configuration).isNotNull();
		assertThat(configuration.isFollowRedirect()).isFalse();
	}

	@Test
	public void it_should_create_custom_configuration_with_cookie_name_value() {
		String jsessionId = UUID.randomUUID().toString();

		HttpClientConfiguration configuration = new HttpClientConfiguration.Builder()
			.addDefaultCookie(COOKIE_NAME, jsessionId)
			.build();

		assertThat(configuration).isNotNull();
		assertThat(configuration.getDefaultCookies())
			.hasSize(1)
			.containsOnly(Cookies.cookie(COOKIE_NAME, jsessionId));
	}

	@Test
	public void it_should_create_custom_configuration_with_cookie() {
		String jsessionId = UUID.randomUUID().toString();
		Cookie cookie = Cookies.cookie(COOKIE_NAME, jsessionId);

		HttpClientConfiguration configuration = new HttpClientConfiguration.Builder()
			.addDefaultCookie(cookie)
			.build();

		assertThat(configuration).isNotNull();
		assertThat(configuration.getDefaultCookies())
			.hasSize(1)
			.containsOnly(cookie);
	}

	@Test
	public void it_should_create_custom_configuration_with_header_name_value() {
		String ua = "Mozilla/5.0 (X11; U; Linux x86_64; en-US) AppleWebKit/532.0 (KHTML, like Gecko) Chrome/4.0.202.0 Safari/532.0";

		HttpClientConfiguration configuration = new HttpClientConfiguration.Builder()
			.addDefaultHeader(USER_AGENT_NAME, ua)
			.build();

		assertThat(configuration).isNotNull();
		assertThat(configuration.getDefaultHeaders())
			.hasSize(1)
			.containsOnly(
				entry(USER_AGENT_NAME, HttpHeader.header(USER_AGENT_NAME, ua))
			);
	}

	@Test
	public void it_should_create_custom_configuration_with_header() {
		String ua = "Mozilla/5.0 (X11; U; Linux x86_64; en-US) AppleWebKit/532.0 (KHTML, like Gecko) Chrome/4.0.202.0 Safari/532.0";
		HttpHeader header = HttpHeader.header(USER_AGENT_NAME, ua);

		HttpClientConfiguration configuration = new HttpClientConfiguration.Builder()
			.addDefaultHeader(header)
			.build();

		assertThat(configuration).isNotNull();
		assertThat(configuration.getDefaultHeaders())
			.hasSize(1)
			.containsOnly(
				entry(USER_AGENT_NAME, header)
			);
	}

	@Test
	public void it_should_implement_equals_hashCode() {
		EqualsVerifier.forClass(HttpClientConfiguration.class)
			.suppress(Warning.STRICT_INHERITANCE)
			.verify();
	}

	@Test
	public void it_should_implement_to_string() {
		String ua = "Mozilla/5.0 (X11; U; Linux x86_64; en-US) AppleWebKit/532.0 (KHTML, like Gecko) Chrome/4.0.202.0 Safari/532.0";
		String jsessionId = UUID.randomUUID().toString();

		HttpClientConfiguration configuration = new HttpClientConfiguration.Builder()
			.disableFollowRedirect()
			.addDefaultCookie(COOKIE_NAME, jsessionId)
			.addDefaultHeader(USER_AGENT_NAME, ua)
			.build();

		assertThat(configuration.toString()).isEqualTo(
			"HttpClientConfiguration{" +
				"followRedirect: false, " +
				"defaultHeaders: {" +
					"User-Agent: HttpHeader{name: \"User-Agent\", values: [\"" + ua + "\"]}" +
				"}, " +
				"defaultCookies: [" +
					"Cookie{name: \"JSESSIONID\", value: \"" + jsessionId + "\", domain: null, path: null, expires: null, maxAge: null, secure: false, httpOnly: false}" +
				"]" +
			"}");
	}
}
