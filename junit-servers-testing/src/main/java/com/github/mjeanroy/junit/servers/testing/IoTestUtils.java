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

package com.github.mjeanroy.junit.servers.testing;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;

/**
 * Static IO Utilities, used only for testing.
 */
public final class IoTestUtils {

	private IoTestUtils() {
	}

	/**
	 * Get file from classpath.
	 *
	 * @param resourceName Resource name.
	 * @return The file.
	 */
	public static File getFileFromClasspath(String resourceName) {
		URL resource = IoTestUtils.class.getResource(resourceName);
		if (resource == null) {
			throw new AssertionError("Cannot find resource: " + resourceName);
		}

		String file = resource.getFile();
		return new File(file);
	}

	/**
	 * Get path instance from classpath.
	 *
	 * @param resourceName Resource name.
	 * @return The file.
	 */
	public static Path getPathFromClasspath(String resourceName) {
		return getFileFromClasspath(resourceName).toPath();
	}
}
