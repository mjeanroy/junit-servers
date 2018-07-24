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

package com.github.mjeanroy.junit.servers.commons;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.Test;

import java.util.Collection;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PreconditionsTest {

	@Test
	public void it_should_throw_exception_if_value_is_null() {
		assertThatThrownBy(notNull(null, "foo"))
			.isExactlyInstanceOf(NullPointerException.class)
			.hasMessage("foo must not be null");
	}

	@Test
	public void it_should_throw_exception_if_collection_is_empty() {
		assertThatThrownBy(notEmpty(emptyList(), "foo"))
			.isExactlyInstanceOf(IllegalArgumentException.class)
			.hasMessage("foo must not be empty");
	}

	@Test
	public void it_should_throw_exception_if_collection_is_null() {
		assertThatThrownBy(notEmpty(null, "foo"))
			.isExactlyInstanceOf(NullPointerException.class)
			.hasMessage("foo must not be null");
	}

	@Test
	public void it_should_not_throw_exception_if_collection_is_not_empty() {
		Collection<String> list = asList("foo", "bar");
		Collection<String> result = Preconditions.notEmpty(list, "foo");
		assertThat(result).isNotNull().isSameAs(list);
	}

	@Test
	public void it_should_throw_exception_if_string_is_null() {
		assertThatThrownBy(notBlank(null, "foo"))
			.isExactlyInstanceOf(NullPointerException.class)
			.hasMessage("foo must not be null");
	}

	@Test
	public void it_should_throw_exception_if_string_is_empty() {
		assertThatThrownBy(notBlank("", "foo"))
			.isExactlyInstanceOf(IllegalArgumentException.class)
			.hasMessage("foo must not be blank");
	}

	@Test
	public void it_should_throw_exception_if_string_is_blank() {
		assertThatThrownBy(notBlank("   ", "foo"))
			.isExactlyInstanceOf(IllegalArgumentException.class)
			.hasMessage("foo must not be blank");
	}

	@Test
	public void it_should_not_throw_exception_if_string_is_not_blank() {
		String input = "  foo  ";
		String output = Preconditions.notBlank(input, "foo");
		assertThat(output).isEqualTo(input);
	}

	@Test
	public void it_should_throw_exception_if_int_is_negative() {
		assertThatThrownBy(positive(-1, "foo"))
			.isExactlyInstanceOf(IllegalArgumentException.class)
			.hasMessage("foo must be positive");
	}

	@Test
	public void it_should_not_throw_exception_if_int_is_zero_or_positive() {
		assertThat(Preconditions.positive(0, "foo")).isZero();
		assertThat(Preconditions.positive(1, "foo")).isEqualTo(1);
	}

	@Test
	public void it_should_throw_exception_if_long_is_negative() {
		assertThatThrownBy(positive(-1L, "foo"))
			.isExactlyInstanceOf(IllegalArgumentException.class)
			.hasMessage("foo must be positive");
	}

	@Test
	public void it_should_not_throw_exception_if_long_is_zero_or_positive() {
		assertThat(Preconditions.positive(0L, "foo")).isZero();
		assertThat(Preconditions.positive(1L, "foo")).isEqualTo(1L);
	}

	private static ThrowingCallable notNull(final Object value, final String message) {
		return new ThrowingCallable() {
			@Override
			public void call() {
				Preconditions.notNull(value, message);
			}
		};
	}

	private static <T> ThrowingCallable notEmpty(final Collection<T> value, final String message) {
		return new ThrowingCallable() {
			@Override
			public void call() {
				Preconditions.notEmpty(value, message);
			}
		};
	}

	private static ThrowingCallable notBlank(final String value, final String message) {
		return new ThrowingCallable() {
			@Override
			public void call() {
				Preconditions.notBlank(value, message);
			}
		};
	}

	private static ThrowingCallable positive(final int value, final String message) {
		return new ThrowingCallable() {
			@Override
			public void call() {
				Preconditions.positive(value, message);
			}
		};
	}

	private static ThrowingCallable positive(final long value, final String message) {
		return new ThrowingCallable() {
			@Override
			public void call() {
				Preconditions.positive(value, message);
			}
		};
	}
}
