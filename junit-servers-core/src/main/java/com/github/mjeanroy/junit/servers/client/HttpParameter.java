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

package com.github.mjeanroy.junit.servers.client;

import static com.github.mjeanroy.junit.servers.commons.Checks.notBlank;
import static com.github.mjeanroy.junit.servers.commons.Checks.notNull;
import static java.lang.String.format;

/**
 * Parameter object that could be sent in an http request.
 * It could be used to add:
 * - Query parameters.
 * - Form parameters.
 * - Header value.
 */
public class HttpParameter {

	/**
	 * Create new parameter object.
	 *
	 * @param name Parameter name.
	 * @param value Parameter value.
	 * @return Parameter object.
	 * @throws IllegalArgumentException if name is empty or blank.
	 * @@throws NullPointerException if name or value is null.
	 */
	public static HttpParameter param(String name, String value) {
		return new HttpParameter(name, value);
	}

	/**
	 * Parameter name.
	 * Must not be blank.
	 */
	private final String name;

	/**
	 * Parameter value.
	 * Must not be null.
	 */
	private final String value;

	private HttpParameter(String name, String value) {
		this.name = notBlank(name, "name");
		this.value = notNull(value, "value");
	}

	/**
	 * Get parameter name.
	 *
	 * @return Parameter name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get parameter value.
	 *
	 * @return Parameter value.
	 */
	public String getValue() {
		return value;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (o instanceof HttpParameter) {
			HttpParameter p = (HttpParameter) o;
			return getName().equals(p.getName()) &&
					getValue().equals(p.getValue());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return getName().hashCode() + getValue().hashCode();
	}

	@Override
	public String toString() {
		return format("%s{%s => %s}", getClass().getSimpleName(), getName(), getValue());
	}
}
