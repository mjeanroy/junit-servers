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

import static com.github.mjeanroy.junit.servers.commons.CollectionUtils.filter;
import static com.github.mjeanroy.junit.servers.commons.CollectionUtils.isEmpty;
import static com.github.mjeanroy.junit.servers.commons.CollectionUtils.join;
import static com.github.mjeanroy.junit.servers.commons.CollectionUtils.map;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singleton;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class CollectionUtilsTest {

	@Test
	public void it_should_return_true_if_collection_is_null_or_empty() {
		assertThat(isEmpty(null)).isTrue();
		assertThat(isEmpty(emptyList())).isTrue();
	}

	@Test
	public void it_should_return_false_if_collection_is_not_null_and_not_empty() {
		assertThat(isEmpty(singletonList("foo"))).isFalse();
	}

	@Test
	public void it_should_join_elements() {
		String separator = ";";
		assertThat(join(null, separator)).isEqualTo(null);
		assertThat(join(emptyList(), separator)).isEqualTo("");
		assertThat(join(singleton("foo"), separator)).isEqualTo("foo");
		assertThat(join(asList("foo", "bar"), separator)).isEqualTo("foo;bar");
		assertThat(join(asList("foo", "bar"), null)).isEqualTo("foobar");
	}

	@Test
	@SuppressWarnings("unchecked")
	public void it_should_filter_list() {
		List<Integer> numbers = asList(1, 2, 3, 4, 5, 6);
		Predicate<Integer> predicate = mock(Predicate.class);

		when(predicate.apply(anyInt())).thenAnswer(new Answer<Boolean>() {
			@Override
			public Boolean answer(InvocationOnMock invocation) throws Throwable {
				int arg = invocation.getArgument(0);
				return arg % 2 == 0;
			}
		});

		List<Integer> results = filter(numbers, predicate);

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
	public void it_should_map_collection() {
		List<Integer> numbers = asList(1, 2, 3);
		Mapper<Integer, Integer> mapper = mock(Mapper.class);

		when(mapper.apply(anyInt())).thenAnswer(new Answer<Integer>() {
			@Override
			public Integer answer(InvocationOnMock invocation) throws Throwable {
				int arg = invocation.getArgument(0);
				return arg * arg;
			}
		});

		List<Integer> results = map(numbers, mapper);

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
	public void it_should_map_null_to_null() {
		assertThat(map(null, mock(Mapper.class))).isNull();
	}
}
