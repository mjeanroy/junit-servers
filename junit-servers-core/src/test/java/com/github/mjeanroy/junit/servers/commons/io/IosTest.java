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

package com.github.mjeanroy.junit.servers.commons.io;

import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import static com.github.mjeanroy.junit.servers.utils.commons.TestUtils.classpathPath;
import static org.assertj.core.api.Assertions.assertThat;

public class IosTest {

	@Test
	public void it_should_expose_cr_lf() {
		final String strOutput = new String(Ios.CRLF, StandardCharsets.UTF_8);
		assertThat(strOutput).isEqualTo("\r\n");
	}

	@Test
	public void it_should_serialize_string_to_byte_array() {
		final String input = "test";
		final byte[] output = Ios.toUtf8Bytes(input);
		final String strOutput = new String(output, StandardCharsets.UTF_8);
		assertThat(strOutput).isEqualTo(input);
	}

	@Test
	public void it_should_serialize_null_string_to_empty_byte_array() {
		final String input = null;
		final byte[] output = Ios.toUtf8Bytes(input);
		final String strOutput = new String(output, StandardCharsets.UTF_8);
		assertThat(strOutput).isEqualTo("");
	}

	@Test
	public void it_should_serialize_file_to_byte_array() throws Exception {
		final Path path = classpathPath("/file1.txt");
		final byte[] output = Ios.toBytes(path);
		final String strOutput = new String(output, StandardCharsets.UTF_8);
		assertThat(strOutput).isEqualToIgnoringNewLines("Content of file1.txt");
	}

	@Test
	public void it_should_guess_content_type_files() {
		assertThat(Ios.guessContentType(null)).isNull();
		assertThat(Ios.guessContentType(classpathPath("/file1.txt"))).isEqualTo("text/plain");
		assertThat(Ios.guessContentType(classpathPath("/img1.jpg"))).isEqualTo("image/jpeg");
		assertThat(Ios.guessContentType(classpathPath("/img2.png"))).isEqualTo("image/png");
		assertThat(Ios.guessContentType(classpathPath("/file1.pdf"))).isEqualTo("application/pdf");
	}
}
