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

package com.github.mjeanroy.junit.servers.samples.jetty.jupiter;

import com.github.mjeanroy.junit.servers.annotations.TestServerConfiguration;
import com.github.mjeanroy.junit.servers.client.HttpClient;
import com.github.mjeanroy.junit.servers.jetty.EmbeddedJettyConfiguration;
import com.github.mjeanroy.junit.servers.jetty12.EmbeddedJetty;
import com.github.mjeanroy.junit.servers.jetty12.jupiter.JettyTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.github.mjeanroy.junit.servers.samples.utils.WebTestUtils.ensureIndexIsOk;
import static com.github.mjeanroy.junit.servers.samples.utils.jetty.JettyTestUtils.createJettyConfiguration;
import static org.assertj.core.api.Assertions.assertThat;

@JettyTest
class IndexWithNestedClassTest {

	@TestServerConfiguration
	private static EmbeddedJettyConfiguration configuration = createJettyConfiguration();

	private static EmbeddedJetty jettyInstance;

	@BeforeAll
	static void beforeAll(EmbeddedJetty jetty) {
		jettyInstance = jetty;
	}

	@Nested
	class Test1 {
		@Test
		void it_should_have_an_index(HttpClient client, EmbeddedJetty jetty) {
			ensureIndexIsOk(client);

			// We should have one, and only one, instance.
			assertThat(jetty).isSameAs(jettyInstance);
		}
	}

	@Nested
	class Test2 {
		@Test
		void it_should_have_an_index(HttpClient client, EmbeddedJetty jetty) {
			ensureIndexIsOk(client);

			// We should have one, and only one, instance.
			assertThat(jetty).isSameAs(jettyInstance);
		}
	}
}
