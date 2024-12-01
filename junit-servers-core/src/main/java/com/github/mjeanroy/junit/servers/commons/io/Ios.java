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

package com.github.mjeanroy.junit.servers.commons.io;

import com.github.mjeanroy.junit.servers.loggers.Logger;
import com.github.mjeanroy.junit.servers.loggers.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Static I/O Utilities.
 */
public final class Ios {

	/**
	 * Class Logger.
	 */
	private static final Logger log = LoggerFactory.getLogger(Ios.class);

	/**
	 * The CRLF character (a.k.a {@code "\r\n"}).
	 */
	public static final byte[] CRLF = Ios.toUtf8Bytes("\r\n");

	// Ensure non instantiation.
	private Ios() {
	}

	/**
	 * Serialize string to an array of bytes using UTF-8 encoding.
	 *
	 * @param value The string to serialize.
	 * @return The serialization result.
	 */
	public static byte[] toUtf8Bytes(String value) {
		if (value == null) {
			return new byte[0];
		}

		return value.getBytes(StandardCharsets.UTF_8);
	}

	/**
	 * Serialize file to an array of bytes.
	 *
	 * @param path The path.
	 * @return The serialization result.
	 * @throws IOException If an I/O error occurs reading from the stream
	 * @see Files#readAllBytes(Path)
	 */
	public static byte[] toBytes(Path path) throws IOException {
		if (path == null) {
			return new byte[0];
		}

		return Files.readAllBytes(path);
	}

	/**
	 * Try to guess content type of given file.
	 *
	 * @param path The file.
	 * @return The content-type that has been guessed, may be {@code null}.
	 */
	public static String guessContentType(Path path) {
		if (path == null) {
			return null;
		}

		log.debug("Try to guess content type of path: {}", path);

		String mimeType = tryProbeContentType(path);

		if (mimeType == null) {
			mimeType = tryGuessContentTypeFromStream(path);
		}

		if (mimeType == null) {
			mimeType = tryGuessContentTypeFromFileName(path);
		}

		return mimeType;
	}

	/**
	 * Build file path using OS default file separator.
	 * @param path Root path.
	 * @param subPaths Sub paths.
	 * @return The full path.
	 */
	public static String toFilePath(String path, String... subPaths) {
		StringBuilder output = new StringBuilder(path);

		for (String subPath : subPaths) {
			output.append(File.separator).append(subPath);
		}

		return output.toString();
	}

	private static String tryProbeContentType(Path path) {
		try {
			return Files.probeContentType(path);
		}
		catch (IOException ex) {
			log.warn(ex.getMessage(), ex);
			return null;
		}
	}

	private static String tryGuessContentTypeFromStream(Path path) {
		try (InputStream is = new BufferedInputStream(Files.newInputStream(path))) {
			return URLConnection.guessContentTypeFromStream(is);
		}
		catch (IOException ex) {
			log.warn(ex.getMessage(), ex);
			return null;
		}
	}

	private static String tryGuessContentTypeFromFileName(Path path) {
		return URLConnection.guessContentTypeFromName(path.getFileName().toString());
	}
}
