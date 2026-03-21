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

/// Set of constants and utilities for Media Type (a.k.a Content Type).
public final class MediaType {

	// Ensure non instantiation.
	private MediaType() {
	}

	/// The media type for file upload ([RFC 1867](https://www.ietf.org/rfc/rfc1867.txt)).
	public static final String TEXT_PLAIN = "text/plain";

	/// The JSON media type ([RFC 4627](https://tools.ietf.org/html/rfc4627)).
	public static final String APPLICATION_JSON = "application/json";

	/// The XML media type ([RFC 7303](https://tools.ietf.org/html/rfc7303)).
	public static final String APPLICATION_XML = "application/xml";

	/// The media type for HTML forms ([RFC](https://tools.ietf.org/html/draft-hoehrmann-urlencoded-01)).
	public static final String APPLICATION_FORM_URL_ENCODED = "application/x-www-form-urlencoded";

	/// The media type for file upload ([RFC 1867](https://www.ietf.org/rfc/rfc1867.txt)).
	public static final String MULTIPART_FORM_DATA = "multipart/form-data";

	/// A media type for multipart request ([RFC 1341](https://www.w3.org/Protocols/rfc1341/7_2_Multipart.html.txt)).
	public static final String MULTIPART_MIXED = "multipart/mixed";

	/// A media type for multipart request ([RFC 1341](https://www.w3.org/Protocols/rfc1341/7_2_Multipart.html.txt)).
	public static final String MULTIPART_ALTERNATIVE = "multipart/alternative";

	/// A media type for multipart request ([RFC 1341](https://www.w3.org/Protocols/rfc1341/7_2_Multipart.html.txt)).
	public static final String MULTIPART_DIGEST = "multipart/digest";

	/// A media type for multipart request ([RFC 1341](https://www.w3.org/Protocols/rfc1341/7_2_Multipart.html.txt)).
	public static final String MULTIPART_PARALLEL = "multipart/parallel";

	/// The media type for JPEG Images ([MDN](https://developer.mozilla.org/fr/docs/Web/HTTP/Basics_of_HTTP/MIME_types/Complete_list_of_MIME_types)).
	public static final String IMAGE_JPG = "image/jpeg";

	/// The media type for PNG Images ([MDN](https://developer.mozilla.org/fr/docs/Web/HTTP/Basics_of_HTTP/MIME_types/Complete_list_of_MIME_types)).
	public static final String IMAGE_PNG = "image/png";

	/// The media type for PDF Documents ([MDN](https://developer.mozilla.org/fr/docs/Web/HTTP/Basics_of_HTTP/MIME_types/Complete_list_of_MIME_types)).
	public static final String APPLICATION_PDF = "application/pdf";

	/// The media type for binary data ([MDN](https://developer.mozilla.org/fr/docs/Web/HTTP/Basics_of_HTTP/MIME_types/Complete_list_of_MIME_types)).
	public static final String APPLICATION_OCTET_STREAM = "application/octet-stream";
}
