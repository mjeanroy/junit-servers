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

package com.github.mjeanroy.junit.servers.samples.tomcat.java;

import com.github.mjeanroy.junit.servers.annotations.TestHttpClient;
import com.github.mjeanroy.junit.servers.annotations.TestServer;
import com.github.mjeanroy.junit.servers.annotations.TestServerConfiguration;
import com.github.mjeanroy.junit.servers.client.HttpClient;
import com.github.mjeanroy.junit.servers.junit4.JunitServerRunner;
import com.github.mjeanroy.junit.servers.tomcat.EmbeddedTomcat;
import com.github.mjeanroy.junit.servers.tomcat.EmbeddedTomcatConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.github.mjeanroy.junit.servers.samples.utils.EmbeddedWebAppTestUtils.ensureWebAppIsOk;
import static com.github.mjeanroy.junit.servers.samples.utils.tomcat.TomcatTestUtils.createTomcatConfiguration;

@RunWith(JunitServerRunner.class)
public class IndexWithGenericRunnerTest {

	@TestServerConfiguration
	private static EmbeddedTomcatConfiguration configuration = createTomcatConfiguration();

	@TestServer
	private static EmbeddedTomcat tomcat;

	@TestHttpClient
	private HttpClient client;

	@Test
	public void it_should_have_an_index() {
		ensureWebAppIsOk(client, tomcat);
	}
}
