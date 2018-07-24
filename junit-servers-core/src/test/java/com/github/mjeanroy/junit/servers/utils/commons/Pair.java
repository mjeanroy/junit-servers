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

package com.github.mjeanroy.junit.servers.utils.commons;

import java.util.Collections;
import java.util.List;

import static java.util.Collections.singletonList;

/**
 * Pair of strings to use in unit tests.
 */
public class Pair {

	/**
	 * Create new pair of strings.
	 *
	 * @param o1 String 1.
	 * @param o2 String 2.
	 * @return Pair object.
	 */
	public static Pair pair(String o1, String o2) {
		return new Pair(o1, singletonList(o2));
	}

	/**
	 * Create new pair of strings.
	 *
	 * @param o1 String 1.
	 * @param o2 String 2.
	 * @return Pair object.
	 */
	public static Pair pair(String o1, List<String> o2) {
		return new Pair(o1, o2);
	}

	/**
	 * First string.
	 */
	private final String o1;

	/**
	 * Second string.
	 */
	private final List<String> o2;

	// Use static factory
	private Pair(String o1, List<String> o2) {
		this.o1 = o1;
		this.o2 = o2;
	}

	public String getO1() {
		return o1;
	}

	public List<String> getO2() {
		return o2;
	}
}
