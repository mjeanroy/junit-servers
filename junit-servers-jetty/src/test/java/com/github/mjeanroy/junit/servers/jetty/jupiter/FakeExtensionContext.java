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

package com.github.mjeanroy.junit.servers.jetty.jupiter;

import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * A fake {@link ExtensionContext}
 */
class FakeExtensionContext implements ExtensionContext {

	/**
	 * The test context stores.
	 */
	private final Map<Namespace, FakeStore> stores;

	/**
	 * The test unique identifier.
	 */
	private final String id;

	/**
	 * The tested instance.
	 */
	private final Object testInstance;

	FakeExtensionContext(Object testInstance) {
		this.stores = new HashMap<>();
		this.id = UUID.randomUUID().toString();
		this.testInstance = testInstance;
	}

	@Override
	public Optional<ExtensionContext> getParent() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ExtensionContext getRoot() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getUniqueId() {
		return id;
	}

	@Override
	public String getDisplayName() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<String> getTags() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Optional<AnnotatedElement> getElement() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Optional<Class<?>> getTestClass() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Class<?> getRequiredTestClass() {
		return testInstance.getClass();
	}

	@Override
	public Optional<TestInstance.Lifecycle> getTestInstanceLifecycle() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Optional<Object> getTestInstance() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object getRequiredTestInstance() {
		return testInstance;
	}

	@Override
	public Optional<Method> getTestMethod() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Optional<Throwable> getExecutionException() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Optional<String> getConfigurationParameter(String key) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void publishReportEntry(Map<String, String> map) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Store getStore(Namespace namespace) {
		if (!stores.containsKey(namespace)) {
			stores.put(namespace, new FakeStore());
		}

		return stores.get(namespace);
	}

	/**
	 * Get the single created store, or fail with {@link AssertionError} otherwise (no store, or more than
	 * one store created).
	 *
	 * @return The single created store.
	 */
	FakeStore getSingleStore() {
		int size = stores.size();
		if (size != 1) {
			throw new AssertionError("Cannot get single store, found " + size + " stores instead.");
		}

		return stores.values().iterator().next();
	}
}
