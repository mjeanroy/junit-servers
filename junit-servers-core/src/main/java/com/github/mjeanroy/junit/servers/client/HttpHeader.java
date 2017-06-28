/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2017 <mickael.jeanroy@gmail.com>
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

import com.github.mjeanroy.junit.servers.commons.CollectionUtils;
import com.github.mjeanroy.junit.servers.commons.ToStringBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static com.github.mjeanroy.junit.servers.commons.Preconditions.notBlank;
import static com.github.mjeanroy.junit.servers.commons.Preconditions.notEmpty;
import static com.github.mjeanroy.junit.servers.commons.Preconditions.notNull;
import static com.github.mjeanroy.junit.servers.commons.Strings.toLowerCase;
import static java.util.Collections.singletonList;
import static java.util.Collections.unmodifiableList;

/**
 * Http getHeader representation.
 * A getHeader is defined by:
 * - A name.
 * - A value or a list of values (since a getHeader can appear multiple times
 * in a request).
 */
public class HttpHeader {

	/**
	 * The separator for header values.
	 */
	private static final String HEADER_SEPARATOR = ",";

	/**
	 * Create a getHeader with a single value.
	 *
	 * @param name Header name, must not be blank.
	 * @param value Header value.
	 * @return Header.
	 */
	public static HttpHeader header(String name, String value) {
		return new HttpHeader(name, singletonList(value));
	}

	/**
	 * Create a getHeader with multiple values.
	 *
	 * @param name Header name.
	 * @param values Header values, must not be empty.
	 * @return Header.
	 */
	public static HttpHeader header(String name, Collection<String> values) {
		return new HttpHeader(name, values);
	}

	/**
	 * Header name.
	 */
	private final String name;

	/**
	 * Header values.
	 */
	private final List<String> values;

	// Use static factories
	private HttpHeader(String name, Collection<String> values) {
		this.name = notBlank(name, "name");
		this.values = new ArrayList<>(values.size());

		for (String val : notEmpty(values, "values")) {
			this.values.add(notNull(val, "value"));
		}
	}

	/**
	 * Get getHeader name.
	 *
	 * @return Header name.l
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get getHeader values.
	 * Not that returned list is not modifiable.
	 *
	 * @return Header values.
	 */
	public List<String> getValues() {
		return unmodifiableList(values);
	}

	/**
	 * Get first value of getHeader.
	 *
	 * @return Header value.
	 */
	public String getFirstValue() {
		return values.get(0);
	}

	/**
	 * Get last value of getHeader.
	 * If getHeader has only a single value, result will be the same as the
	 * result of {@link #getFirstValue()}.
	 *
	 * @return Last getHeader value.
	 */
	public String getLastValue() {
		return values.get(values.size() - 1);
	}

	/**
	 * Serialize header values using the default separator.
	 *
	 * @return Header values serialized as a string.
	 */
	public String serializeValues() {
		return CollectionUtils.join(values, HEADER_SEPARATOR);
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (o instanceof HttpHeader) {
			HttpHeader h = (HttpHeader) o;
			return Objects.equals(toLowerCase(name), toLowerCase(h.name)) && Objects.equals(values, h.values);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(toLowerCase(name), values);
	}

	@Override
	public String toString() {
		return ToStringBuilder.create(getClass())
			.append("name", name)
			.append("values", values)
			.build();
	}
}
