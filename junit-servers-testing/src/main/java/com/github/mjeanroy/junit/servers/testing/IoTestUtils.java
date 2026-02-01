/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2026 <mickael.jeanroy@gmail.com>
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
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
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

	/**
	 * Create temporary file in given directory.
	 *
	 * @param root Root directory.
	 * @return The temporary file.
	 */
	public static TempFile createTempFile(Path root) {
		try {
			File tmpFile = Files.createTempFile(root.toRealPath(), null, null).toFile();
			tmpFile.deleteOnExit();
			return new TempFile(tmpFile);
		}
		catch (IOException ex) {
			throw new AssertionError(ex);
		}
	}

	/**
	 * Temporary file, created with {@link #createTempFile(Path)}, used for
	 * testing only.
	 */
	public static final class TempFile {
		/**
		 * The temporary file.
		 */
		private final File file;

		private TempFile(File file) {
			this.file = file;
		}

		/**
		 * Temporary file name.
		 *
		 * @return File name.
		 */
		public String getName() {
			return file.getName();
		}

		/**
		 * Get temporary file parent directory.
		 *
		 * @return The parent directory.
		 */
		public File getParentDir() {
			return file.getParentFile();
		}

		/**
		 * Get temporary file parent directory URL.
		 *
		 * @return Parent directory URL.
		 */
		public URL getParentDirURL() {
			try {
				return getParentDir().toURI().toURL();
			}
			catch (MalformedURLException ex) {
				throw new AssertionError(ex);
			}
		}
	}
}
