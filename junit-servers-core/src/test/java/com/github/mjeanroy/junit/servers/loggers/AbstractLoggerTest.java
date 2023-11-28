/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2023 <mickael.jeanroy@gmail.com>
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

package com.github.mjeanroy.junit.servers.loggers;

import com.github.mjeanroy.junit.servers.utils.jupiter.CaptureSystemOut;
import com.github.mjeanroy.junit.servers.utils.jupiter.CaptureSystemOutTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@CaptureSystemOutTest
abstract class AbstractLoggerTest {

	private Logger log;

	@BeforeEach
	void setUp() {
		log = createLogger();
	}

	@Test
	void it_should_log_trace_message_with_one_argument(CaptureSystemOut sysout) {
		log.trace("Message with placeholder: {}", "arg1");
		verifyOutput(sysout, "TRACE", "Message with placeholder: arg1");
	}

	@Test
	void it_should_log_trace_message_with_two_arguments(CaptureSystemOut sysout) {
		log.trace("Message with placeholder: {} {}", "arg1", "arg2");
		verifyOutput(sysout, "TRACE", "Message with placeholder: arg1 arg2");
	}

	@Test
	void it_should_log_trace_message_without_argument(CaptureSystemOut sysout) {
		log.trace("Message with placeholder");
		verifyOutput(sysout, "TRACE", "Message with placeholder");
	}

	@Test
	void it_should_log_debug_message_with_one_argument(CaptureSystemOut sysout) {
		log.debug("Message with placeholder: {}", "arg1");
		verifyOutput(sysout, "DEBUG", "Message with placeholder: arg1");
	}

	@Test
	void it_should_log_debug_message_with_two_arguments(CaptureSystemOut sysout) {
		log.debug("Message with placeholder: {} {}", "arg1", "arg2");
		verifyOutput(sysout, "DEBUG", "Message with placeholder: arg1 arg2");
	}

	@Test
	void it_should_log_debug_message_without_argument(CaptureSystemOut sysout) {
		log.debug("Message with placeholder");
		verifyOutput(sysout, "DEBUG", "Message with placeholder");
	}

	@Test
	void it_should_log_info_message_with_one_argument(CaptureSystemOut sysout) {
		log.info("Message with placeholder: {}", "arg1");
		verifyOutput(sysout, "INFO", "Message with placeholder: arg1");
	}

	@Test
	void it_should_log_info_message_with_two_arguments(CaptureSystemOut sysout) {
		log.info("Message with placeholder: {} {}", "arg1", "arg2");
		verifyOutput(sysout, "INFO", "Message with placeholder: arg1 arg2");
	}

	@Test
	void it_should_log_info_message_without_argument(CaptureSystemOut sysout) {
		log.info("Message with placeholder");
		verifyOutput(sysout, "INFO", "Message with placeholder");
	}

	@Test
	void it_should_log_warn_message_with_one_argument(CaptureSystemOut sysout) {
		log.warn("Message with placeholder: {}", "arg1");
		verifyOutput(sysout, "WARN", "Message with placeholder: arg1");
	}

	@Test
	void it_should_log_warn_message_with_two_arguments(CaptureSystemOut sysout) {
		log.warn("Message with placeholder: {} {}", "arg1", "arg2");
		verifyOutput(sysout, "WARN", "Message with placeholder: arg1 arg2");
	}

	@Test
	void it_should_log_warn_message_without_argument(CaptureSystemOut sysout) {
		log.warn("Message with placeholder");
		verifyOutput(sysout, "WARN", "Message with placeholder");
	}

	@Test
	void it_should_log_error_message_with_one_argument(CaptureSystemOut sysout) {
		log.error("Message with placeholder: {}", "arg1");
		verifyOutput(sysout, "ERROR", "Message with placeholder: arg1");
	}

	@Test
	void it_should_log_error_message_with_two_arguments(CaptureSystemOut sysout) {
		log.error("Message with placeholder: {} {}", "arg1", "arg2");
		verifyOutput(sysout, "ERROR", "Message with placeholder: arg1 arg2");
	}

	@Test
	void it_should_log_error_message_without_argument(CaptureSystemOut sysout) {
		log.error("Message with placeholder");
		verifyOutput(sysout, "ERROR", "Message with placeholder");
	}

	@Test
	void it_should_log_throwable(CaptureSystemOut sysout) {
		Exception ex = new RuntimeException("A runtime exception");
		String message = "error message";
		log.error(message, ex);

		verifyOutput(sysout, "ERROR", ex.getMessage());
	}

	private void verifyOutput(CaptureSystemOut sysout, String logLevel, String message) {
		String out = sysout.getOut();
		assertThat(out).contains(logLevel);
		assertThat(out).contains(message);
	}

	abstract Logger createLogger();
}
