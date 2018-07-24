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

package com.github.mjeanroy.junit.servers.client;

/**
 * Http methods available with abstract
 * client.
 */
public enum HttpMethod {

	/**
	 * The {@code GET} HTTP method.
	 * @see <a href="https://tools.ietf.org/html/rfc2616#section-9.3">https://tools.ietf.org/html/rfc2616#section-9.3</a>
	 */
	GET(false),

	/**
	 * The {@code POST} HTTP method.
	 * @see <a href="https://tools.ietf.org/html/rfc2616#section-9.5">https://tools.ietf.org/html/rfc2616#section-9.5</a>
	 */
	POST(true),

	/**
	 * The {@code PUT} HTTP method.
	 * @see <a href="https://tools.ietf.org/html/rfc2616#section-9.6">https://tools.ietf.org/html/rfc2616#section-9.6</a>
	 */
	PUT(true),

	/**
	 * The {@code PATCH} HTTP method.
	 * @see <a href="https://tools.ietf.org/html/rfc2068#section-19.6.1.1">https://tools.ietf.org/html/rfc2068#section-19.6.1.1</a>
	 */
	PATCH(true),

	/**
	 * The {@code DELETE} HTTP method.
	 * @see <a href="https://tools.ietf.org/html/rfc2616#section-9.7">https://tools.ietf.org/html/rfc2616#section-9.7</a>
	 */
	DELETE(false),

	/**
	 * The {@code HEAD} HTTP method.
	 * @see <a href="https://tools.ietf.org/html/rfc2616#section-9.4">https://tools.ietf.org/html/rfc2616#section-9.4</a>
	 */
	HEAD(false);

	/**
	 * Flag to know if http method allow body request.
	 */
	private final boolean allowBody;

	// Enum private constructor
	HttpMethod(boolean allowBody) {
		this.allowBody = allowBody;
	}

	/**
	 * Get formatted http verb:
	 *
	 * <ul>
	 *   <li>Always in upper case.</li>
	 *   <li>Related to given HTTP method.</li>
	 * </ul>
	 *
	 * @return Verb.
	 */
	public String getVerb() {
		return name();
	}

	/**
	 * Check if request method allow body request (as form url encoded parameter or
	 * as json / xml body).
	 *
	 * @return {@code true} if method allow body request, {@code false} otherwise.
	 */
	public boolean isBodyAllowed() {
		return allowBody;
	}
}
