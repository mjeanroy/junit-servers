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

package com.github.mjeanroy.junit.servers.commons;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;

import static java.util.Collections.addAll;

/**
 * Static date utilities.
 *
 * <p>
 *
 * <strong>Internal API</strong>: these methods are part of the internal API and may be removed, have their signature change,
 * or have their access level decreased from public to protected, package, or private in future versions without notice.
 */
public final class Dates {

	private Dates() {
	}

	/**
	 * Parse date using given pattern.
	 * Note that this method will return null if pattern is not
	 * valid or throws a {@link ParseException}.
	 *
	 * @param date Date.
	 * @param pattern Pattern.
	 * @param patterns Other pattern to test.
	 * @return Date, null if pattern is not valid.
	 */
	static Date parse(String date, String pattern, String... patterns) {
		Set<String> set = new HashSet<>();
		set.add(pattern);
		if (patterns != null) {
			addAll(set, patterns);
		}

		for (String p : set) {
			try {
				return df(p).parse(date);
			}
			catch (ParseException ex) {
				// Skip pattern
			}
		}

		return null;
	}

	/**
	 * Parse date using given pattern and return time value.
	 * Note that this method will return null if pattern is not
	 * valid or throws a {@link ParseException}.
	 *
	 * @param date Date.
	 * @param pattern Pattern.
	 * @param patterns Other pattern to test.
	 * @return Time value, null if pattern is not valid.
	 */
	public static Long getTime(String date, String pattern, String... patterns) {
		Date d = parse(date, pattern, patterns);
		return d == null ? null : d.getTime();
	}

	/**
	 * Format date according to given pattern.
	 *
	 * @param date Date.
	 * @param pattern Pattern.
	 * @return Formatted date.
	 */
	public static String format(Date date, String pattern) {
		return df(pattern).format(date);
	}

	/**
	 * Format timestamp according to given pattern.
	 *
	 * @param time Timestamp.
	 * @param pattern Pattern.
	 * @return Formatted date.
	 */
	static String formatTime(long time, String pattern) {
		Date date = new Date();
		date.setTime(time);
		return format(date, pattern);
	}

	private static DateFormat df(String pattern) {
		DateFormat df = new SimpleDateFormat(pattern, Locale.ENGLISH);
		df.setTimeZone(TimeZone.getTimeZone("GMT"));
		return df;
	}
}
