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

package com.github.mjeanroy.junit.servers.commons.lang;

import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PreconditionsTest {

	@Test
	void it_should_throw_exception_if_value_is_null() {
		String input = null;
		String name = "foo";

		assertThatThrownBy(() -> Preconditions.notNull(input, name))
			.isExactlyInstanceOf(NullPointerException.class)
			.hasMessage("foo must not be null");
	}

	@Test
	void it_should_throw_exception_if_one_value_is_null() {
		List<String> inputs = asList("1", null, "3");
		String name = "inputs";

		assertThatThrownBy(() -> Preconditions.doesNotContainNull(inputs, name))
			.isExactlyInstanceOf(NullPointerException.class)
			.hasMessage("inputs[1] must not be null");
	}

	@Test
	void it_should_return_iterable_if_no_value_are_null() {
		List<String> inputs = asList("1", "2", "3");
		String name = "inputs";
		Iterable<String> outputs = Preconditions.doesNotContainNull(inputs, name);
		assertThat(outputs).isSameAs(inputs);
	}

	@Test
	void it_should_throw_exception_if_collection_is_empty() {
		List<Object> inputs = emptyList();
		String name = "foo";

		assertThatThrownBy(() -> Preconditions.notEmpty(inputs, name))
			.isExactlyInstanceOf(IllegalArgumentException.class)
			.hasMessage("foo must not be empty");
	}

	@Test
	void it_should_throw_exception_if_collection_is_null() {
		Collection<String> inputs = null;
		String name = "foo";

		assertThatThrownBy(() -> Preconditions.notEmpty(inputs, name))
			.isExactlyInstanceOf(NullPointerException.class)
			.hasMessage("foo must not be null");
	}

	@Test
	void it_should_not_throw_exception_if_collection_is_not_empty() {
		Collection<String> list = asList("foo", "bar");
		String name = "foo";
		Collection<String> result = Preconditions.notEmpty(list, name);
		assertThat(result).isNotNull().isSameAs(list);
	}

	@Test
	void it_should_throw_exception_if_string_is_null() {
		String value = null;
		String name = "foo";

		assertThatThrownBy(() -> Preconditions.notBlank(value, name))
			.isExactlyInstanceOf(NullPointerException.class)
			.hasMessage("foo must not be null");
	}

	@Test
	void it_should_throw_exception_if_string_is_empty() {
		String value = "";
		String name = "foo";

		assertThatThrownBy(() -> Preconditions.notBlank(value, name))
			.isExactlyInstanceOf(IllegalArgumentException.class)
			.hasMessage("foo must not be blank");
	}

	@Test
	void it_should_throw_exception_if_string_is_blank() {
		String value = "   ";
		String name = "foo";

		assertThatThrownBy(() -> Preconditions.notBlank(value, name))
			.isExactlyInstanceOf(IllegalArgumentException.class)
			.hasMessage("foo must not be blank");
	}

	@Test
	void it_should_not_throw_exception_if_string_is_not_blank() {
		String input = "  foo  ";
		String output = Preconditions.notBlank(input, "foo");
		assertThat(output).isEqualTo(input);
	}

	@Test
	void it_should_throw_exception_if_int_is_negative() {
		int value = -1;
		String name = "foo";

		assertThatThrownBy(() -> Preconditions.positive(value, name))
			.isExactlyInstanceOf(IllegalArgumentException.class)
			.hasMessage("foo must be positive");
	}

	@Test
	void it_should_not_throw_exception_if_int_is_zero_or_positive() {
		assertThat(Preconditions.positive(0, "foo")).isZero();
		assertThat(Preconditions.positive(1, "foo")).isEqualTo(1);
	}

	@Test
	void it_should_throw_exception_if_long_is_negative() {
		long value = -1L;
		String name = "foo";

		assertThatThrownBy(() -> Preconditions.positive(value, name))
			.isExactlyInstanceOf(IllegalArgumentException.class)
			.hasMessage("foo must be positive");
	}

	@Test
	void it_should_not_throw_exception_if_long_is_zero_or_positive() {
		assertThat(Preconditions.positive(0L, "foo")).isZero();
		assertThat(Preconditions.positive(1L, "foo")).isEqualTo(1L);
	}
}
