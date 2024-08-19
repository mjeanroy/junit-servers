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

package com.github.mjeanroy.junit.servers.jupiter;

import com.github.mjeanroy.junit.servers.commons.lang.ToStringBuilder;
import com.github.mjeanroy.junit.servers.engine.AnnotationsHandlerRunner;
import com.github.mjeanroy.junit.servers.engine.EmbeddedServerRunner;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.Objects;

import static com.github.mjeanroy.junit.servers.commons.lang.Preconditions.notNull;

final class JunitServerExtensionContext implements ExtensionContext.Store.CloseableResource {

	private final EmbeddedServerRunner runner;
	private final JunitServerExtensionLifecycle lifecycle;
	private final Class<?> testClass;
	private final AnnotationsHandlerRunner annotationsHandler;

	JunitServerExtensionContext(
		EmbeddedServerRunner runner,
		JunitServerExtensionLifecycle lifecycle,
		Class<?> testClass
	) {
		this.runner = notNull(runner, "runner");
		this.lifecycle = notNull(lifecycle, "lifecycle");
		this.testClass = notNull(testClass, "testClass");
		this.annotationsHandler = new AnnotationsHandlerRunner(
			runner.getServer(),
			runner.getServer().getConfiguration()
		);
	}

	EmbeddedServerRunner getRunner() {
		return runner;
	}

	JunitServerExtensionLifecycle getLifecycle() {
		return lifecycle;
	}

	Class<?> getTestClass() {
		return testClass;
	}

	AnnotationsHandlerRunner getAnnotationsHandler() {
		return annotationsHandler;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (o instanceof JunitServerExtensionContext) {
			JunitServerExtensionContext that = (JunitServerExtensionContext) o;
			return Objects.equals(runner, that.runner)
				&& Objects.equals(lifecycle, that.lifecycle)
				&& Objects.equals(testClass, that.testClass)
				&& Objects.equals(annotationsHandler, that.annotationsHandler);
		}

		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(runner, lifecycle, testClass, annotationsHandler);
	}

	@Override
	public String toString() {
		return ToStringBuilder.create(getClass())
			.append("runner", runner)
			.append("lifecycle", lifecycle)
			.append("testClass", testClass)
			.append("annotationsHandler", annotationsHandler)
			.build();
	}

	@Override
	public void close() {
		runner.stop();
	}
}
