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

package com.github.mjeanroy.junit.servers.jetty11.jupiter;

import org.junit.jupiter.api.extension.ExtensionContext.Store;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * A fake {@link Store} implementation using a map as internal implementation.
 */
class FakeStore implements Store {

	/**
	 * The internal map implementation.
	 */
	private final Map<Object, Object> map;

	FakeStore() {
		map = new HashMap<>();
	}

	@Override
	public Object get(Object key) {
		return map.get(key);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <V> V get(Object key, Class<V> requiredType) {
		return (V) get(key);
	}

	@Override
	public <K, V> Object getOrComputeIfAbsent(K key, Function<K, V> defaultCreator) {
		if (!map.containsKey(key)) {
			map.put(key, defaultCreator.apply(key));
		}

		return map.get(key);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <K, V> V getOrComputeIfAbsent(K key, Function<K, V> defaultCreator, Class<V> requiredType) {
		return (V) getOrComputeIfAbsent(key, defaultCreator);
	}

	@Override
	public void put(Object key, Object value) {
		map.put(key, value);
	}

	@Override
	public Object remove(Object key) {
		return map.remove(key);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <V> V remove(Object key, Class<V> requiredType) {
		return (V) remove(key);
	}
}
