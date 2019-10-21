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

package com.github.mjeanroy.junit.servers.commons.lang;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.stubbing.Answer;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singleton;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CollectionsTest {

	@Test
	void it_should_return_true_if_collection_is_null_or_empty() {
		assertThat(Collections.isEmpty(null)).isTrue();
		assertThat(Collections.isEmpty(emptyList())).isTrue();
	}

	@Test
	void it_should_return_false_if_collection_is_not_null_and_not_empty() {
		assertThat(Collections.isEmpty(singletonList("foo"))).isFalse();
	}

	@Test
	void it_should_join_elements() {
		final String separator = ";";
		assertThat(Collections.join(null, separator)).isEqualTo(null);
		assertThat(Collections.join(emptyList(), separator)).isEqualTo("");
		assertThat(Collections.join(singleton("foo"), separator)).isEqualTo("foo");
		assertThat(Collections.join(asList("foo", "bar"), separator)).isEqualTo("foo;bar");
		assertThat(Collections.join(asList("foo", "bar"), null)).isEqualTo("foobar");
	}

	@Test
	@SuppressWarnings("unchecked")
	void it_should_filter_list() {
		final List<Integer> numbers = asList(1, 2, 3, 4, 5, 6);
		final Predicate<Integer> predicate = mock(Predicate.class);

		when(predicate.apply(anyInt())).thenAnswer((Answer<Boolean>) invocation -> {
			int arg = invocation.getArgument(0);
			return arg % 2 == 0;
		});

		final List<Integer> results = Collections.filter(numbers, predicate);

		assertThat(results)
			.isNotNull()
			.isNotEmpty()
			.hasSize(3)
			.isEqualTo(asList(2, 4, 6));

		InOrder inOrder = inOrder(predicate);
		inOrder.verify(predicate).apply(1);
		inOrder.verify(predicate).apply(2);
		inOrder.verify(predicate).apply(3);
		inOrder.verify(predicate).apply(4);
		inOrder.verify(predicate).apply(5);
		inOrder.verify(predicate).apply(6);
	}

	@Test
	@SuppressWarnings("unchecked")
	void it_should_map_collection() {
		final List<Integer> numbers = asList(1, 2, 3);
		final Mapper<Integer, Integer> mapper = mock(Mapper.class);

		when(mapper.apply(anyInt())).thenAnswer((Answer<Integer>) invocation -> {
			int arg = invocation.getArgument(0);
			return arg * arg;
		});

		final List<Integer> results = Collections.map(numbers, mapper);

		assertThat(results)
			.isNotNull()
			.isNotEmpty()
			.hasSize(3)
			.isEqualTo(asList(1, 4, 9));

		InOrder inOrder = inOrder(mapper);
		inOrder.verify(mapper).apply(1);
		inOrder.verify(mapper).apply(2);
		inOrder.verify(mapper).apply(3);
	}

	@Test
	@SuppressWarnings("unchecked")
	void it_should_map_null_to_null() {
		assertThat(Collections.map(null, mock(Mapper.class))).isNull();
	}
}
