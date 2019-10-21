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

package com.github.mjeanroy.junit.servers.utils.jupiter;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Parameter;

class CaptureSystemOutExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

	/**
	 * The extension namespace.
	 */
	private static final Namespace NAMESPACE = Namespace.create(CaptureSystemOutExtension.class);

	/**
	 * The key under which the original {@code System.out} stream is saved.
	 */
	private static final String ORIGINAL_OUT_STORE_KEY = "originalOut";

	/**
	 * The key under which the temporary out stream is saved.
	 */
	private static final String TEMPORARY_OUT_STORE_KEY = "out";

	@Override
	public void beforeEach(ExtensionContext context) {
		Store store = getStore(context);
		storeOriginalSystemOut(store);
		overrideSystemOut(store);
	}

	@Override
	public void afterEach(ExtensionContext context) throws Exception {
		Store store = getStore(context);

		try {
			closeTemporaryOut(store);
		}
		finally {
			restoreSystemOutAndClearStore(store);
		}
	}

	@Override
	public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
		Parameter parameter = parameterContext.getParameter();
		Class<?> parameterType = parameter.getType();
		return CaptureSystemOut.class.isAssignableFrom(parameterType);
	}

	@Override
	public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
		Store store = getStore(extensionContext);
		ByteArrayOutputStream out = store.get(TEMPORARY_OUT_STORE_KEY, ByteArrayOutputStream.class);
		return new CaptureSystemOut(out);
	}

	/**
	 * Save origin {@code System.out} stream in the test store, to be able
	 * to restore it after each test.
	 *
	 * @param store The extension store.
	 */
	private static void storeOriginalSystemOut(Store store) {
		PrintStream originalOut = System.out;
		store.put(ORIGINAL_OUT_STORE_KEY, originalOut);
	}

	/**
	 * Override {@code System.out} stream with custom out stream.
	 *
	 * @param store The extension store.
	 */
	private static void overrideSystemOut(Store store) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		PrintStream outStream = new PrintStream(out);
		System.setOut(outStream);
		store.put(TEMPORARY_OUT_STORE_KEY, out);
	}

	/**
	 * Restore {@code System.out} stream and clear extension store (even if an error
	 * occurred while restoring stream).
	 *
	 * @param store The extension store.
	 */
	private static void restoreSystemOutAndClearStore(Store store) {
		try {
			restoreSystemOut(store);
		}
		finally {
			clearStore(store);
		}
	}

	/**
	 * Flush and close the previously created temporary {@code System.out} stream.
	 *
	 * @param store The extension store.
	 * @throws Exception If an error occurred while closing the stream.
	 */
	private static void closeTemporaryOut(Store store) throws Exception {
		ByteArrayOutputStream out = store.get(TEMPORARY_OUT_STORE_KEY, ByteArrayOutputStream.class);
		out.flush();
		out.close();
	}

	/**
	 * Restore origin {@code System.out} stream.
	 *
	 * @param store The extension store.
	 */
	private static void restoreSystemOut(Store store) {
		System.setOut(store.get(ORIGINAL_OUT_STORE_KEY, PrintStream.class));
	}

	/**
	 * Clear extension store.
	 *
	 * @param store The store.
	 */
	private static void clearStore(Store store) {
		store.remove(TEMPORARY_OUT_STORE_KEY);
		store.remove(ORIGINAL_OUT_STORE_KEY);
	}

	/**
	 * Get extension store.
	 *
	 * @param context The extension context.
	 * @return The extension store.
	 */
	private static Store getStore(ExtensionContext context) {
		return context.getStore(NAMESPACE);
	}
}
