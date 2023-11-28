/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2023 <mickael.jeanroy@gmail.com>
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

package com.github.mjeanroy.junit.servers.utils.commons;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * Static Test Utilities.
 */
public final class TestUtils {

	// Ensure non instantiation.
	private TestUtils() {
	}

	/**
	 * URL encode string value.
	 * @param value String value.
	 * @return URL encoded value.
	 * @see URLEncoder#encode(String, String)
	 */
	public static String urlEncode(String value) {
		try {
			return URLEncoder.encode(value, StandardCharsets.UTF_8.displayName());
		}
		catch (UnsupportedEncodingException ex) {
			throw new AssertionError(ex);
		}
	}

	/**
	 * Translate array of bytes to a string using UTF-8 encoding.
	 *
	 * @param bytes Array of bytes.
	 * @return The result string.
	 */
	public static String toUtf8String(byte[] bytes) {
		return new String(bytes, StandardCharsets.UTF_8);
	}

	/**
	 * Read given file to an array of bytes.
	 *
	 * @param file The file to read.
	 * @return The result.
	 */
	public static byte[] readFile(File file) {
		try {
			return Files.readAllBytes(file.toPath());
		}
		catch (IOException ex) {
			throw new AssertionError(ex);
		}
	}
}
