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

import static com.github.mjeanroy.junit.servers.commons.EncoderUtils.urlEncode;
import static com.github.mjeanroy.junit.servers.commons.Preconditions.notBlank;

import java.util.Objects;

import com.github.mjeanroy.junit.servers.commons.ToStringBuilder;

/**
 * Parameter object that could be sent in an http request as:
 * <ul>
 *   <li>Query parameters (following {@code ?} character in URL.</li>
 *   <li>Form parameters (such as HTML forms, with {@link HttpHeaders#APPLICATION_FORM_URL_ENCODED} media type).</li>
 * </ul>
 */
public class HttpParameter {

	/**
	 * Create new parameter object.
	 *
	 * @param name Parameter name.
	 * @param value Parameter value.
	 * @return Parameter object.
	 * @throws NullPointerException if {@code name} is {@code null}.
	 * @throws IllegalArgumentException if {@code name} is empty or blank.
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
		this.value = value;
	}

	/**
	 * Get {@link #name}.
	 *
	 * @return {@link #name}.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get {@link #value}.
	 *
	 * @return {@link #value}.
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Get the URL encoded parameter name.
	 *
	 * @return URL encoded parameter name.
	 */
	public String getEncodedName() {
		return urlEncode(name);
	}

	/**
	 * Get the URL encoded parameter value.
	 *
	 * @return URL encoded parameter value.
	 */
	public String getEncodedValue() {
		return value == null ? null : urlEncode(value);
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}

		if (o instanceof HttpParameter) {
			HttpParameter p = (HttpParameter) o;
			return Objects.equals(name, p.name) && Objects.equals(value, p.value);
		}

		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, value);
	}

	@Override
	public String toString() {
		return ToStringBuilder.create(getClass())
			.append("name", name)
			.append("value", value)
			.build();
	}
}
