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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ToStringBuilderTest {

	@Test
	public void it_should_create_to_string_value() {
		String f1 = "v1";
		String f2 = null;
		int f3 = 10;
		boolean f4 = true;
		List<String> f5 = Arrays.asList("foo", "bar");
		List<String> f6 = Collections.emptyList();
		Value f7 = new Value("val");
		Value f8 = null;

		String toString = ToStringBuilder.create(ToStringBuilderTest.class)
			.append("f1", f1)
			.append("f2", f2)
			.append("f3", f3)
			.append("f4", f4)
			.append("f5", f5)
			.append("f6", f6)
			.append("f7", f7)
			.append("f8", f8)
			.build();

		assertThat(toString)
			.isNotNull()
			.isEqualTo(
				"ToStringBuilderTest{" +
					"f1: \"v1\", " +
					"f2: null, " +
					"f3: 10, " +
					"f4: true, " +
					"f5: [\"foo\", \"bar\"], " +
					"f6: [], " +
					"f7: val, " +
					"f8: null" +
				"}");
	}

	private static class Value {
		private final String value;

		private Value(String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return value;
		}
	}
}
