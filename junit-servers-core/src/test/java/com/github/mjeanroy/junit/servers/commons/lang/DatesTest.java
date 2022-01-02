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

package com.github.mjeanroy.junit.servers.commons.lang;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static com.github.mjeanroy.junit.servers.commons.lang.Dates.*;
import static org.assertj.core.api.Assertions.assertThat;

class DatesTest {

	private TimeZone tz;

	@BeforeEach
	void setUp() {
		tz = TimeZone.getDefault();
		TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
	}

	@AfterEach
	void tearDown() {
		TimeZone.setDefault(tz);
	}

	@Test
	void it_should_parse_date() {
		final String value = "Wed, 13 Jan 2021 22:23:01 GMT";
		final String pattern = "EEE, d MMM yyyy HH:mm:ss Z";

		final Date date = parse(value, pattern);

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
	void it_should_parse_date_using_appropriate_pattern() {
		final String value = "Wed, 13-Jan-2021 22:23:01 GMT";
		final String pattern1 = "EEE, d MMM yyyy HH:mm:ss Z";
		final String pattern2 = "EEE, d-MMM-yyyy HH:mm:ss Z";

		final Date date = parse(value, pattern1, pattern2);

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
	void it_should_parse_date_and_return_null_without_matching_pattern() {
		final String value = "2021-01-01";
		final String pattern1 = "EEE, d MMM yyyy HH:mm:ss Z";
		final String pattern2 = "EEE, d-MMM-yyyy HH:mm:ss Z";

		final Date date = parse(value, pattern1, pattern2);

		assertThat(date).isNull();
	}

	@Test
	void it_should_parse_date_and_get_time() {
		final String value = "Wed, 13 Jan 2021 22:23:01 GMT";
		final String pattern = "EEE, dd MMM yyyy HH:mm:ss zzz";

		final Long time = getTime(value, pattern);

		assertThat(time)
			.isNotNull()
			.isEqualTo(1610576581000L);
	}

	@Test
	void it_should_format_time() {
		final long time = 1610576581000L;
		final String pattern = "EEE, dd MMM yyyy HH:mm:ss zzz";

		final String value = formatTime(time, pattern);

		assertThat(value)
			.isNotNull()
			.isEqualTo("Wed, 13 Jan 2021 22:23:01 GMT");
	}

	@Test
	void it_should_format_date() {
		final String pattern = "EEE, dd MMM yyyy HH:mm:ss zzz";
		final Date date = new Date();
		date.setTime(1610576581000L);

		final String value = format(date, pattern);

		assertThat(value)
			.isNotNull()
			.isEqualTo("Wed, 13 Jan 2021 22:23:01 GMT");
	}
}
