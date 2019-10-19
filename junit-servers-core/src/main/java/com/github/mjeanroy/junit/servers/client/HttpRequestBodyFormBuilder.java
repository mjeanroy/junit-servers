/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2019 <mickael.jeanroy@gmail.com>
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

import com.github.mjeanroy.junit.servers.commons.lang.ToStringBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.github.mjeanroy.junit.servers.commons.lang.Preconditions.doesNotContainNull;
import static com.github.mjeanroy.junit.servers.commons.lang.Preconditions.notNull;
import static java.util.Collections.singleton;

/**
 * A builder for {@link HttpRequestBodyForm}.
 */
public final class HttpRequestBodyFormBuilder {

	/**
	 * Request body parameters.
	 */
	private final Map<String, HttpParameter> parameters;

	/**
	 * Create builder without any parameters.
	 */
	HttpRequestBodyFormBuilder() {
		this.parameters = new LinkedHashMap<>();
	}

	/**
	 * Add parameter.
	 *
	 * @param parameter The Parameter.
	 * @return The builder.
	 * @throws NullPointerException If {@code parameter} is {@code null}
	 */
	public HttpRequestBodyFormBuilder add(HttpParameter parameter) {
		notNull(parameter, "HTTP Parameter must not be null");
		return doAddAll(
			singleton(parameter)
		);
	}

	/**
	 * Add parameter.
	 *
	 * @param name Parameter name.
	 * @param value Parameter value.
	 * @return The builder.
	 * @throws NullPointerException If {@code name} is {@code null}
	 * @throws IllegalArgumentException If {@code name} is empty or blank.
	 */
	public HttpRequestBodyFormBuilder add(String name, String value) {
		return add(
			HttpParameter.of(name, value)
		);
	}

	/**
	 * Add all parameters.
	 *
	 * @param parameters Parameters.
	 * @return The builder.
	 * @throws NullPointerException If one of given parameters name is {@code null}
	 * @throws IllegalArgumentException If one of given parameters name is empty or blank.
	 */
	public HttpRequestBodyFormBuilder addAll(Map<String, String> parameters) {
		List<HttpParameter> params = new ArrayList<>(parameters.size());

		for (Map.Entry<String, String> parameter : parameters.entrySet()) {
			params.add(
				HttpParameter.of(parameter.getKey(), parameter.getValue())
			);
		}

		return doAddAll(params);
	}

	/**
	 * Add all parameters.
	 *
	 * @param parameters Parameters.
	 * @return The builder.
	 * @throws NullPointerException If one of given parameters {@code null}
	 */
	public HttpRequestBodyFormBuilder addAll(Collection<HttpParameter> parameters) {
		return doAddAll(parameters);
	}

	/**
	 * Add all parameters.
	 *
	 * @param parameters Parameters.
	 * @return The builder.
	 * @throws NullPointerException If one of given parameters {@code null}
	 */
	private HttpRequestBodyFormBuilder doAddAll(Collection<HttpParameter> parameters) {
		doesNotContainNull(parameters, "parameters");

		for (HttpParameter parameter : parameters) {
			this.parameters.put(parameter.getName(), parameter);
		}

		return this;
	}

	/**
	 * Build request body.
	 *
	 * @return The request body.
	 */
	public HttpRequestBody build() {
		return new HttpRequestBodyForm(parameters.values());
	}

	@Override
	public String toString() {
		return ToStringBuilder.create(getClass())
			.append("parameters", parameters)
			.build();
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}

		if (o instanceof HttpRequestBodyFormBuilder) {
			HttpRequestBodyFormBuilder b = (HttpRequestBodyFormBuilder) o;
			return Objects.equals(parameters, b.parameters);
		}

		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(parameters);
	}
}
