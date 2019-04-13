/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2015-2019 <mickael.jeanroy@gmail.com>
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

package com.github.mjeanroy.junit.servers.samples.utils;

import com.github.mjeanroy.junit.servers.client.HttpClient;
import com.github.mjeanroy.junit.servers.servers.EmbeddedServer;

import static com.github.mjeanroy.junit.servers.samples.utils.SpringWebTestUtils.verifySpringWebContext;
import static com.github.mjeanroy.junit.servers.samples.utils.WebTestUtils.ensureIndexIsOk;

/**
 * Static Embedded WebApp Utilities for various samples.
 */
public final class EmbeddedWebAppTestUtils {

	// Ensure non instantiation.
	private EmbeddedWebAppTestUtils() {
	}

	/**
	 * Ensure that:
	 * <ul>
	 *   <li>Index view is OK.</li>
	 *   <li>Web Application is correctly started and spring web-context can be retrieved.</li>
	 * </ul>
	 *
	 * @param client The HTTP client used to query embedded server.
	 * @param server The embedded server.
	 */
	public static void ensureWebAppIsOk(HttpClient client, EmbeddedServer server) {
		ensureIndexIsOk(client);
		verifySpringWebContext(server);
	}
}
