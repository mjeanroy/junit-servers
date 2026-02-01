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

package com.github.mjeanroy.junit.servers.jupiter;

import org.junit.jupiter.api.extension.ExtensionContext;

/**
 * The embedded server lifecycle:
 * <ul>
 *   <li>Global: only one instance is shared and re-used across all tests.</li>
 *   <li>Per class: Embedded server is started/stopped before all/after all tests in a given class.</li>
 * </ul>
 */
public enum JunitServerExtensionLifecycle {
	/**
	 * Start/stop embedded server before/after all tests in all classes.
	 */
	GLOBAL {
		@Override
		ExtensionContext getExtensionContext(ExtensionContext context) {
			return context.getRoot();
		}
	},

	/**
	 * Start/stop embedded server before/after all tests in a class.
	 */
	PER_CLASS {
		@Override
		ExtensionContext getExtensionContext(ExtensionContext context) {
			ExtensionContext current = context;
			ExtensionContext root = context.getRoot();

			while (current != root) {
				ExtensionContext parent = current.getParent().orElse(null);
				if (parent == null || parent == root) {
					return current;
				}

				current = parent;
			}

			return context;
		}
	},

	/**
	 * Start/stop embedded server before/after any tests in a class.
	 */
	PER_METHOD {
		@Override
		ExtensionContext getExtensionContext(ExtensionContext context) {
			return context;
		}
	};

	abstract ExtensionContext getExtensionContext(ExtensionContext context);
}
