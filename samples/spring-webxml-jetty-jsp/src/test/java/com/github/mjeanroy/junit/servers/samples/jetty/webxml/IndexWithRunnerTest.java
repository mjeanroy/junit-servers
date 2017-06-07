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

package com.github.mjeanroy.junit.servers.samples.jetty.webxml;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.net.URL;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.web.client.RestTemplate;

import com.github.mjeanroy.junit.servers.annotations.TestServer;
import com.github.mjeanroy.junit.servers.annotations.TestServerConfiguration;
import com.github.mjeanroy.junit.servers.jetty.EmbeddedJettyConfiguration;
import com.github.mjeanroy.junit.servers.runner.JunitServerRunner;
import com.github.mjeanroy.junit.servers.servers.EmbeddedServer;

@RunWith(JunitServerRunner.class)
public class IndexWithRunnerTest {

	@TestServer
	private static EmbeddedServer jetty;

	@TestServerConfiguration
	private static EmbeddedJettyConfiguration configuration() throws Exception {
		String current = new File(".").getCanonicalPath();
		if (!current.endsWith("/")) {
			current += "/";
		}

		String subProjectPath = "samples/spring-webxml-jetty-jsp/";
		String path = current.endsWith(subProjectPath) ? current : current + subProjectPath;

		// note use of maven plugin to copy a maven dependency to this directory
		URL urlParentClasspath = new File("target/lib/").toURI().toURL();

		return EmbeddedJettyConfiguration.builder()
				.withWebapp(path + "src/main/webapp")
				.withParentClasspath(urlParentClasspath)
				.withClasspath(path + "target/classes")
				.build();
	}

	private static RestTemplate restTemplate = new RestTemplate();

	@Test
	public void it_should_have_an_index() {
		String url = jetty.getUrl();
		String message = restTemplate.getForObject(url, String.class);
		assertThat(message).isNotEmpty().contains("Hello");
	}

}
