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

package com.github.mjeanroy.junit.servers.commons;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Collection;
import java.util.List;

import static com.github.mjeanroy.junit.servers.commons.Preconditions.notBlank;
import static com.github.mjeanroy.junit.servers.commons.Preconditions.notEmpty;
import static com.github.mjeanroy.junit.servers.commons.Preconditions.notNull;
import static com.github.mjeanroy.junit.servers.commons.Preconditions.positive;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.rules.ExpectedException.none;

public class PreconditionsTest {

	@Rule
	public ExpectedException thrown = none();

	@Test
	public void it_should_throw_exception_if_value_is_null() {
		thrown.expect(NullPointerException.class);
		thrown.expectMessage("foo must not be null");
		notNull(null, "foo");
	}

	@Test
	public void it_should_throw_exception_if_collection_is_empty() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("foo must not be empty");

		List<String> collection = emptyList();
		notEmpty(collection, "foo");
	}

	@Test
	public void it_should_throw_exception_if_collection_is_null() {
		thrown.expect(NullPointerException.class);
		thrown.expectMessage("foo must not be null");
		notEmpty(null, "foo");
	}

	@Test
	public void it_should_not_throw_exception_if_collection_is_not_empty() {
		Collection<String> list = asList("foo", "bar");
		Collection<String> result = notEmpty(list, "foo");
		assertThat(result)
				.isNotNull()
				.isSameAs(list);
	}

	@Test
	public void it_should_throw_exception_if_string_is_null() {
		thrown.expect(NullPointerException.class);
		thrown.expectMessage("foo must not be null");
		notBlank(null, "foo");
	}

	@Test
	public void it_should_throw_exception_if_string_is_empty() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("foo must not be blank");
		notBlank("", "foo");
	}

	@Test
	public void it_should_throw_exception_if_string_is_blank() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("foo must not be blank");
		notBlank("   ", "foo");
	}

	@Test
	public void it_should_not_throw_exception_if_string_is_not_blank() {
		String input = "  foo  ";
		String output = notBlank(input, "foo");
		assertThat(output).isEqualTo(input);
	}

	@Test
	public void it_should_throw_exception_if_int_is_negative() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("foo must be positive");
		positive(-1, "foo");
	}

	@Test
	public void it_should_not_throw_exception_if_int_is_zero_or_positive() {
		assertThat(positive(0, "foo")).isZero();
		assertThat(positive(1, "foo")).isEqualTo(1);
	}

	@Test
	public void it_should_throw_exception_if_long_is_negative() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("foo must be positive");
		positive(-1L, "foo");
	}

	@Test
	public void it_should_not_throw_exception_if_long_is_zero_or_positive() {
		assertThat(positive(0L, "foo")).isZero();
		assertThat(positive(1L, "foo")).isEqualTo(1L);
	}
}
