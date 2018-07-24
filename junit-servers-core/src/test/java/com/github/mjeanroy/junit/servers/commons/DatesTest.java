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

import static com.github.mjeanroy.junit.servers.commons.Dates.format;
import static com.github.mjeanroy.junit.servers.commons.Dates.formatTime;
import static com.github.mjeanroy.junit.servers.commons.Dates.getTime;
import static com.github.mjeanroy.junit.servers.commons.Dates.parse;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DatesTest {

	private TimeZone tz;

	@Before
	public void setUp() {
		tz = TimeZone.getDefault();
		TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
	}

	@After
	public void tearDown() {
		TimeZone.setDefault(tz);
	}

	@Test
	public void it_should_parse_date() {
		String value = "Wed, 13 Jan 2021 22:23:01 GMT";
		String pattern = "EEE, d MMM yyyy HH:mm:ss Z";

		Date date = parse(value, pattern);

		assertThat(date)
				.isNotNull()
				.hasYear(2021)
				.hasMonth(Calendar.JANUARY + 1)
				.hasDayOfMonth(13)
				.hasHourOfDay(22)
				.hasMinute(23)
				.hasSecond(1);
	}

	@Test
	public void it_should_parse_date_using_appropriate_pattern() {
		String value = "Wed, 13-Jan-2021 22:23:01 GMT";
		String pattern1 = "EEE, d MMM yyyy HH:mm:ss Z";
		String pattern2 = "EEE, d-MMM-yyyy HH:mm:ss Z";

		Date date = parse(value, pattern1, pattern2);

		assertThat(date)
				.isNotNull()
				.hasYear(2021)
				.hasMonth(Calendar.JANUARY + 1)
				.hasDayOfMonth(13)
				.hasHourOfDay(22)
				.hasMinute(23)
				.hasSecond(1);
	}

	@Test
	public void it_should_parse_date_and_return_null_without_matching_pattern() {
		String value = "2021-01-01";
		String pattern1 = "EEE, d MMM yyyy HH:mm:ss Z";
		String pattern2 = "EEE, d-MMM-yyyy HH:mm:ss Z";

		Date date = parse(value, pattern1, pattern2);

		assertThat(date).isNull();
	}

	@Test
	public void it_should_parse_date_and_get_time() {
		String value = "Wed, 13 Jan 2021 22:23:01 GMT";
		String pattern = "EEE, dd MMM yyyy HH:mm:ss zzz";

		Long time = getTime(value, pattern);

		assertThat(time)
				.isNotNull()
				.isEqualTo(1610576581000L);
	}

	@Test
	public void it_should_format_time() {
		long time = 1610576581000L;
		String pattern = "EEE, dd MMM yyyy HH:mm:ss zzz";

		String value = formatTime(time, pattern);

		assertThat(value)
				.isNotNull()
				.isEqualTo("Wed, 13 Jan 2021 22:23:01 GMT");
	}

	@Test
	public void it_should_format_date() {
		String pattern = "EEE, dd MMM yyyy HH:mm:ss zzz";
		Date date = new Date();
		date.setTime(1610576581000L);

		String value = format(date, pattern);

		assertThat(value)
				.isNotNull()
				.isEqualTo("Wed, 13 Jan 2021 22:23:01 GMT");
	}
}
