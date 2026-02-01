/**
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

package com.github.mjeanroy.junit.servers.testing;

import org.junit.jupiter.api.Test;
import org.junit.platform.engine.discovery.ClassSelector;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.testkit.engine.EngineExecutionResults;
import org.junit.platform.testkit.engine.EngineTestKit;

import java.lang.reflect.Method;

public final class JupiterExtensionTesting {

	private JupiterExtensionTesting() {
	}

	public static void runTests(Class<?> klass, Class<?> ...otherKlasses) {
		ClassSelector[] classSelectors = new ClassSelector[otherKlasses.length + 1];
		classSelectors[0] = DiscoverySelectors.selectClass(klass);
		for (int i = 0; i < otherKlasses.length; i++) {
			classSelectors[i + 1] = DiscoverySelectors.selectClass(otherKlasses[i]);
		}

		EngineExecutionResults results = EngineTestKit.engine("junit-jupiter")
			.selectors(classSelectors)
			.execute();

		int nbTests = countTests(klass, otherKlasses);
		results.testEvents().assertStatistics(stats ->
			stats.started(nbTests).succeeded(nbTests)
		);
	}

	private static int countTests(Class<?> klass, Class<?> ...otherKlasses) {
		int nbTests = countClassTests(klass);

		for (Class<?> otherClass : otherKlasses) {
			nbTests += countClassTests(otherClass);
		}

		return nbTests;
	}

	private static int countClassTests(Class<?> klass) {
		int count = 0;

		Class<?> currentKlass = klass;

		while (currentKlass != null && currentKlass != Object.class) {
			for (Method method : klass.getDeclaredMethods()) {
				if (method.isAnnotationPresent(Test.class)) {
					count++;
				}
			}

			currentKlass = currentKlass.getSuperclass();
		}

		return count;
	}
}
