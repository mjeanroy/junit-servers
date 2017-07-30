/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2017 <mickael.jeanroy@gmail.com>
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

package com.github.mjeanroy.junit.servers.rules;

import static org.mockito.Mockito.when;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.github.mjeanroy.junit.servers.tomcat.EmbeddedTomcat;

/**
 * This is a mockito {@link Answer} to simulate {@link EmbeddedTomcat#isStarted()} method.
 */
class IsStartedAnswer implements Answer<Void> {

	static IsStartedAnswer isStarted(EmbeddedTomcat tomcat, boolean isStarted) {
		return new IsStartedAnswer(tomcat, isStarted);
	}

	private final EmbeddedTomcat tomcat;
	private final boolean isStarted;

	private IsStartedAnswer(EmbeddedTomcat tomcat, boolean isStarted) {
		this.tomcat = tomcat;
		this.isStarted = isStarted;
	}

	@Override
	public Void answer(InvocationOnMock invocationOnMock) throws Throwable {
		when(tomcat.isStarted()).thenReturn(isStarted);
		return null;
	}
}
