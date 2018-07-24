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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.addAll;

/**
 * Static collection utilities.
 *
 * <p>
 *
 * <strong>Internal API</strong>: these methods are part of the internal API and may be removed, have their signature change,
 * or have their access level decreased from public to protected, package, or private in future versions without notice.
 */
public final class CollectionUtils {

	// Ensure non instantiation
	private CollectionUtils() {
	}

	/**
	 * Check if a collection is null or empty.
	 *
	 * @param collection Collection to check.
	 * @return True if collection is null or empty, false otherwise.
	 */
	public static boolean isEmpty(Collection<?> collection) {
		return collection == null || collection.isEmpty();
	}

	/**
	 * Join elements of a collection to a final string using a given separator. Note that:
	 * <ul>
	 *   <li>If {@code values} is {@code null}, {@code null} will be returned.</li>
	 *   <li>If {@code separator} is {@code null}, an empty string is used instead.</li>
	 * </ul>
	 *
	 * @param values The values to join.
	 * @param separator Value separator.
	 * @param <T> Type of elements in the collection.
	 * @return The joined elements.
	 */
	public static <T> String join(Iterable<T> values, String separator) {
		if (values == null) {
			return null;
		}

		String sep = separator == null ? "" : separator;
		StringBuilder sb = new StringBuilder();
		boolean first = true;

		for (T value : values) {
			if (!first) {
				sb.append(sep);
			}

			sb.append(value == null ? null : value.toString());
			first = false;
		}

		return sb.toString();
	}

	/**
	 * Map input values to output values.
	 *
	 * @param inputs Input values.
	 * @param mapper Mapper function.
	 * @param <T> Type of input values.
	 * @param <U> Type of output values.
	 * @return Output values.
	 */
	public static <T, U> List<U> map(Collection<T> inputs, Mapper<T, U> mapper) {
		if (inputs == null) {
			return null;
		}

		List<U> outputs = new ArrayList<>(inputs.size());
		for (T input : inputs) {
			outputs.add(mapper.apply(input));
		}

		return outputs;
	}

	/**
	 * Concat new value to existing inputs and returns new outputs.
	 * Note that input is not modified.
	 *
	 * @param inputs Input list.
	 * @param newValue The new value to add.
	 * @param <T> The type of object.
	 * @return The outputs.
	 */
	public static <T> List<T> concat(List<T> inputs, T newValue) {
		List<T> outputs = new ArrayList<>(inputs.size() + 1);
		outputs.addAll(inputs);
		outputs.add(newValue);
		return outputs;
	}

	/**
	 * Filter input by using given predicate and return
	 * filtered outputs.
	 *
	 * @param list List input.
	 * @param predicate Predicate.
	 * @param <T> Type of elements.
	 * @return Filtered outputs.
	 */
	static <T> List<T> filter(List<T> list, Predicate<T> predicate) {
		List<T> results = new ArrayList<>();
		for (T current : list) {
			if (predicate.apply(current)) {
				results.add(current);
			}
		}
		return results;
	}
}
