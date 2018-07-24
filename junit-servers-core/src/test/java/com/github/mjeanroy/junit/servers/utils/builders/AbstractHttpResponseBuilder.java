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

package com.github.mjeanroy.junit.servers.utils.builders;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Builder for HTTP Response instances.
 */
public abstract class AbstractHttpResponseBuilder<T, U> {

	/**
	 * The HTTP Response status code.
	 */
	int status;

	/**
	 * The HTTP Response body.
	 */
	String body;

	/**
	 * The list of headers.
	 */
	final Map<String, List<String>> headers;

	/**
	 * Create builder with default values.
	 */
	AbstractHttpResponseBuilder() {
		this.status = 200;
		this.body = "";
		this.headers = new LinkedHashMap<>();
	}

	/**
	 * Set {@link #status}
	 *
	 * @param status New {@link #status}
	 * @return The builder.
	 */
	@SuppressWarnings("unchecked")
	public U withStatus(int status) {
		this.status = status;
		return (U) this;
	}

	/**
	 * Set {@link #body}
	 *
	 * @param body New {@link #body}
	 * @return The builder.
	 */
	@SuppressWarnings("unchecked")
	public U withBody(String body) {
		this.body = body;
		return (U) this;
	}

	/**
	 * Add new header.
	 *
	 * @param name Header name.
	 * @param value Header value.
	 * @return The builder.
	 */
	@SuppressWarnings("unchecked")
	public U withHeader(String name, String value) {
		if (!headers.containsKey(name)) {
			headers.put(name, new ArrayList<String>());
		}

		headers.get(name).add(value);
		return (U) this;
	}

	/**
	 * Build final HTTP response instance.
	 *
	 * @return The final instance.
	 */
	public abstract T build();
}
