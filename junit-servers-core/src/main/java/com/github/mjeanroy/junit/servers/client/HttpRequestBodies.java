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

package com.github.mjeanroy.junit.servers.client;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Request Body Factories.
 */
public final class HttpRequestBodies {

	private HttpRequestBodies() {
	}

	/**
	 * Create request body from given raw string.
	 *
	 * @param body The body string.
	 * @return The request body.
	 * @throws NullPointerException If {@code body} is {@code null}
	 */
	public static HttpRequestBody requestBody(String body) {
		return HttpRequestBodyString.of(body);
	}

	/**
	 * Create request body from given raw string and {@code "application/json"} content type.
	 *
	 * @param body The JSON body string.
	 * @return The request body.
	 * @throws NullPointerException If {@code body} is {@code null}
	 */
	public static HttpRequestBody jsonBody(String body) {
		return HttpRequestBodyString.of(body, MediaType.APPLICATION_JSON);
	}

	/**
	 * Create request body from given raw string and {@code "application/xml"} content type.
	 *
	 * @param body The XML body string.
	 * @return The request body.
	 * @throws NullPointerException If {@code body} is {@code null}
	 */
	public static HttpRequestBody xmlBody(String body) {
		return HttpRequestBodyString.of(body, MediaType.APPLICATION_XML);
	}

	/**
	 * Create request body from given raw string and {@code "text/plain"} content type.
	 *
	 * @param body The body string.
	 * @return The request body.
	 * @throws NullPointerException If {@code body} is {@code null}
	 */
	public static HttpRequestBody textBody(String body) {
		return HttpRequestBodyString.of(body, MediaType.TEXT_PLAIN);
	}

	/**
	 * Create request body from given {@code file}.
	 *
	 * @param file The body file.
	 * @return The request body.
	 * @throws NullPointerException If {@code file} is {@code null}
	 */
	public static HttpRequestBody fileBody(File file) {
		return HttpRequestBodyFile.of(file);
	}

	/**
	 * Create request body from given {@code file}, defined with {@code "image/jpeg"} content type.
	 *
	 * @param file The body file.
	 * @return The request body.
	 * @throws NullPointerException If {@code file} is {@code null}
	 */
	public static HttpRequestBody jpeg(File file) {
		return HttpRequestBodyFile.of(file, MediaType.IMAGE_JPG);
	}

	/**
	 * Create request body from given {@code path}, defined with {@code "image/jpeg"} content type.
	 *
	 * @param path The body path.
	 * @return The request body.
	 * @throws NullPointerException If {@code file} is {@code null}
	 */
	public static HttpRequestBody jpeg(Path path) {
		return HttpRequestBodyFile.of(path, MediaType.IMAGE_JPG);
	}

	/**
	 * Create request body from given {@code file}, defined with {@code "image/png"} content type.
	 *
	 * @param file The body file.
	 * @return The request body.
	 * @throws NullPointerException If {@code file} is {@code null}
	 */
	public static HttpRequestBody png(File file) {
		return HttpRequestBodyFile.of(file, MediaType.IMAGE_PNG);
	}

	/**
	 * Create request body from given {@code path}, defined with {@code "image/png"} content type.
	 *
	 * @param path The body file.
	 * @return The request body.
	 * @throws NullPointerException If {@code file} is {@code null}
	 */
	public static HttpRequestBody png(Path path) {
		return HttpRequestBodyFile.of(path, MediaType.IMAGE_PNG);
	}

	/**
	 * Create request body from given {@code file}, defined with {@code "application/pdf"} content type.
	 *
	 * @param file The body file.
	 * @return The request body.
	 * @throws NullPointerException If {@code file} is {@code null}
	 */
	public static HttpRequestBody pdf(File file) {
		return HttpRequestBodyFile.of(file, MediaType.APPLICATION_PDF);
	}

	/**
	 * Create request body from given {@code path}, defined with {@code "application/pdf"} content type.
	 *
	 * @param path The body file.
	 * @return The request body.
	 * @throws NullPointerException If {@code file} is {@code null}
	 */
	public static HttpRequestBody pdf(Path path) {
		return HttpRequestBodyFile.of(path, MediaType.APPLICATION_PDF);
	}

	/**
	 * Create request body from given {@code file}.
	 *
	 * @param file The body file.
	 * @param contentType The content type.
	 * @return The request body.
	 * @throws NullPointerException If {@code file} or {@code contentType} are {@code null}
	 * @throws IllegalArgumentException If {@code contentType} is empty or blank.
	 */
	public static HttpRequestBody fileBody(File file, String contentType) {
		return HttpRequestBodyFile.of(file, contentType);
	}

	/**
	 * Create request body from given {@code file}.
	 *
	 * @param path The body file.
	 * @return The request body.
	 * @throws NullPointerException If {@code path} is {@code null}
	 */
	public static HttpRequestBody fileBody(Path path) {
		return HttpRequestBodyFile.of(path);
	}

	/**
	 * Create request body from given {@code file}.
	 *
	 * @param path The body file.
	 * @param contentType The content type.
	 * @return The request body.
	 * @throws NullPointerException If {@code path} or {@code contentType} are {@code null}
	 * @throws IllegalArgumentException If {@code contentType} is empty or blank.
	 */
	public static HttpRequestBody fileBody(Path path, String contentType) {
		return HttpRequestBodyFile.of(path, contentType);
	}

	/**
	 * Create form urlencoded body from given parameters.
	 *
	 * @param parameters Body parameters.
	 * @return The request body.
	 */
	public static HttpRequestBody formUrlEncodedBody(Map<String, String> parameters) {
		return new HttpRequestBodyFormBuilder().addAll(parameters).build();
	}

	/**
	 * Create form urlencoded body from given parameters.
	 *
	 * @param parameters Body parameters.
	 * @return The request body.
	 */
	public static HttpRequestBody formUrlEncodedBody(Collection<HttpParameter> parameters) {
		return new HttpRequestBodyFormBuilder().addAll(parameters).build();
	}

	/**
	 * Create form urlencoded body from given parameters.
	 *
	 * @param parameter First parameter.
	 * @param others Other parameters.
	 * @return The request body.
	 */
	public static HttpRequestBody formUrlEncodedBody(HttpParameter parameter, HttpParameter... others) {
		List<HttpParameter> parameters = new ArrayList<>(others.length + 1);
		parameters.add(parameter);
		Collections.addAll(parameters, others);
		return new HttpRequestBodyFormBuilder().addAll(parameters).build();
	}

	/**
	 * Create builder that can be used to create form urlencoded request body.
	 *
	 * @return The builder.
	 */
	public static HttpRequestBodyFormBuilder formBuilder() {
		return new HttpRequestBodyFormBuilder();
	}

	/**
	 * Create builder that can be used to create multipart request body.
	 *
	 * @return The builder.
	 */
	public static HttpRequestBodyMultipartBuilder multipartBuilder() {
		return new HttpRequestBodyMultipartBuilder();
	}
}
