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

package com.github.mjeanroy.junit.servers.client.it;

import static com.github.mjeanroy.junit.servers.utils.commons.TestUtils.urlEncode;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

final class HttpTestUtils {

	private HttpTestUtils() {
	}

	/**
	 * Encode URL path (i.e replace illegal characters with the percent encoded
	 * value). For example, a space is replaced by {@code "%20"} string).
	 *
	 * @param path The path to encode.
	 * @return The encoded path.
	 */
	static String encodePath(String path) {
		try {
			URI uri = new URI(null, null, path, null);
			return uri.getRawPath();
		} catch (URISyntaxException ex) {
			throw new AssertionError(ex);
		}
	}

	/**
	 * Encode query parameters: this is almost the same algorithm than {@link HttpTestUtils#encodeFormParam(String, String)}.
	 *
	 * @param name Parameter name.
	 * @param value Parameter value.
	 * @return The formatted value.
	 */
	static String encodeQueryParam(String name, String value) {
		return encodeFormParam(name, value);
	}

	/**
	 * Translates a key-value pair into {@code "application/x-www-form-urlencoded"}
	 * format using UTF-8 encoding scheme.
	 *
	 * @param name The key (a.k.a parameter name).
	 * @param value The value (a.k.a parameter value).
	 * @return The encoded string.
	 */
	static String encodeFormParam(String name, String value) {
		String encodedName = urlEncode(name);
		String encodedValue = value == null ? null : urlEncode(value);
		return encodedName + (encodedValue == null ? "" : "=" + encodedValue);
	}

	/**
	 * Translate date to UTC date that can be used as a HTTP header value.
	 *
	 * @param year The year.
	 * @param month The month.
	 * @param dayOfMonth The day of the month.
	 * @param hour The hour.
	 * @param minutes The minutes.
	 * @param second The second.
	 * @return The date created with UTC timezone.
	 */
	static Date utcDate(int year, int month, int dayOfMonth, int hour, int minutes, int second) {
		final Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, minutes);
		cal.set(Calendar.SECOND, second);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
}
