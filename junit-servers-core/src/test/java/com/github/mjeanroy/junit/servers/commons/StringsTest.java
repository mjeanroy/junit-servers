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

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class StringsTest {

	@Test
	public void it_should_turn_string_to_lower_case() {
		assertThat(Strings.toLowerCase(null)).isNull();
		assertThat(Strings.toLowerCase("")).isEqualTo("");
		assertThat(Strings.toLowerCase("FOO")).isEqualTo("foo");
		assertThat(Strings.toLowerCase("foo")).isEqualTo("foo");
	}

	@Test
	public void it_should_check_if_string_is_not_blank() {
		assertThat(Strings.isNotBlank(null)).isFalse();
		assertThat(Strings.isNotBlank("")).isFalse();
		assertThat(Strings.isNotBlank("   ")).isFalse();
		assertThat(Strings.isNotBlank("  foo  ")).isTrue();
	}

	@Test
	public void it_should_check_if_string_is_blank() {
		assertThat(Strings.isBlank(null)).isTrue();
		assertThat(Strings.isBlank("")).isTrue();
		assertThat(Strings.isBlank("   ")).isTrue();
		assertThat(Strings.isBlank("  foo  ")).isFalse();
	}

	@Test
	public void it_should_remove_string_prefix() {
		assertThat(Strings.removePrefix(null, null)).isNull();
		assertThat(Strings.removePrefix("", "")).isEqualTo("");
		assertThat(Strings.removePrefix("foo", "bar")).isEqualTo("foo");
		assertThat(Strings.removePrefix("foo", "foobar")).isEqualTo("foo");
		assertThat(Strings.removePrefix("/foo", "/")).isEqualTo("foo");
		assertThat(Strings.removePrefix("/foo", "/foo")).isEqualTo("");
	}
}
