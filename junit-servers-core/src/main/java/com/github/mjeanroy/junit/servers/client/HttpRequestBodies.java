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

package com.github.mjeanroy.junit.servers.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class HttpRequestBodies {

	/**
	 * Create request body from given raw string.
	 *
	 * @param body The body string.
	 * @return The request body.
	 */
	public static HttpRequestBody requestBody(String body) {
		return new HttpRequestBodyString(null, body);
	}

	/**
	 * Create request body from given raw string and {@code "application/json"} content type.
	 *
	 * @param body The JSON body string.
	 * @return The request body.
	 */
	public static HttpRequestBody jsonBody(String body) {
		return new HttpRequestBodyString(MediaType.APPLICATION_JSON, body);
	}

	/**
	 * Create request body from given raw string and {@code "application/xml"} content type.
	 *
	 * @param body The XML body string.
	 * @return The request body.
	 */
	public static HttpRequestBody xmlBody(String body) {
		return new HttpRequestBodyString(MediaType.APPLICATION_XML, body);
	}

	/**
	 * Create request body from given raw string and {@code "text/plain"} content type.
	 *
	 * @param body The body string.
	 * @return The request body.
	 */
	public static HttpRequestBody textBody(String body) {
		return new HttpRequestBodyString(MediaType.TEXT_PLAIN, body);
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
}
