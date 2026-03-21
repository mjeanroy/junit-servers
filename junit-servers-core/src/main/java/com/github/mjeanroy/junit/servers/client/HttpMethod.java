/*
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

package com.github.mjeanroy.junit.servers.client;

/// Http methods available with abstract
/// client.
public enum HttpMethod {

	/// The `GET` HTTP method ([RFC 2616](https://tools.ietf.org/html/rfc2616#section-9.3)).
	GET(false),

	/// The `POST` HTTP method ([RFC 2616](https://tools.ietf.org/html/rfc2616#section-9.5)).
	POST(true),

	/// The `PUT` HTTP method ([RFC 2616](https://tools.ietf.org/html/rfc2616#section-9.6)).
	PUT(true),

	/// The `PATCH` HTTP method ([RFC 2068](https://tools.ietf.org/html/rfc2068#section-19.6.1.1)).
	PATCH(true),

	/// The `DELETE` HTTP method ([RFC 2616](https://tools.ietf.org/html/rfc2616#section-9.7)).
	DELETE(true),

	/// The `HEAD` HTTP method ([RFC 2616](https://tools.ietf.org/html/rfc2616#section-9.4)).
	HEAD(false);

	/// Flag to know if http method allow body request.
	private final boolean allowBody;

	// Enum private constructor
	HttpMethod(boolean allowBody) {
		this.allowBody = allowBody;
	}

	/// Get formatted http verb:
	/// - Always in upper case.
	/// - Related to given HTTP method.
	///
	/// @return Verb.
	public String getVerb() {
		return name();
	}

	/// Check if request method allow body request (as form url encoded parameter or
	/// as json / xml body).
	///
	/// @return `true` if method allow body request, `false` otherwise.
	public boolean isBodyAllowed() {
		return allowBody;
	}
}
