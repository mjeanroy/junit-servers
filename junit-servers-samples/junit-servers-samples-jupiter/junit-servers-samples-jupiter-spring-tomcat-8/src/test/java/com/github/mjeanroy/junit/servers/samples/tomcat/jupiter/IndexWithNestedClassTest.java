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

package com.github.mjeanroy.junit.servers.samples.tomcat.jupiter;

import com.github.mjeanroy.junit.servers.annotations.TestServerConfiguration;
import com.github.mjeanroy.junit.servers.client.HttpClient;
import com.github.mjeanroy.junit.servers.tomcat.EmbeddedTomcatConfiguration;
import com.github.mjeanroy.junit.servers.tomcat8.EmbeddedTomcat;
import com.github.mjeanroy.junit.servers.tomcat8.jupiter.TomcatTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.github.mjeanroy.junit.servers.samples.utils.EmbeddedWebAppTestUtils.ensureWebAppIsOk;
import static com.github.mjeanroy.junit.servers.samples.utils.tomcat.TomcatTestUtils.createTomcatConfiguration;
import static org.assertj.core.api.Assertions.assertThat;

@TomcatTest
class IndexWithNestedClassTest {

	@TestServerConfiguration
	private static EmbeddedTomcatConfiguration configuration = createTomcatConfiguration();

	private static EmbeddedTomcat tomcatInstance;

	@BeforeAll
	static void beforeAll(EmbeddedTomcat tomcat) {
		tomcatInstance = tomcat;
	}

	@Nested
	class Test1 {
		@Test
		void it_should_have_an_index(HttpClient client, EmbeddedTomcat tomcat) {
			ensureWebAppIsOk(client, tomcat);

			// We should have one, and only one, instance.
			assertThat(tomcat).isSameAs(tomcatInstance);
		}
	}

	@Nested
	class Test2 {
		@Test
		void it_should_have_an_index(HttpClient client, EmbeddedTomcat tomcat) {
			ensureWebAppIsOk(client, tomcat);

			// We should have one, and only one, instance.
			assertThat(tomcat).isSameAs(tomcatInstance);
		}
	}
}
