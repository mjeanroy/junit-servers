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

import java.net.URL;

import static com.github.mjeanroy.junit.servers.commons.Preconditions.notNull;

/**
 * A composite classloader is a classloader that has:
 * <ul>
 *   <li>A parent classloader.</li>
 *   <li>A fallback classloader.</li>
 * </ul>
 *
 * When resolving classes or resources, the parent classloader is consulted first,
 * and if that classloader cannot find the class (or resource), the fallback classloader
 * is tried.
 *
 * <p>
 *
 * <strong>Internal API</strong>: these methods are part of the internal API and may be removed, have their signature change,
 * or have their access level decreased from public to protected, package, or private in future versions without notice.
 */
public class CompositeClassLoader extends ClassLoader {

	/**
	 * Fallback classloader that will be tried after parent classloader.
	 */
	private final ClassLoader fallback;

	/**
	 * Create the classloader.
	 *
	 * @param parent Parent classloader.
	 * @param fallback Fallback classloader.
	 */
	public CompositeClassLoader(ClassLoader parent, ClassLoader fallback) {
		super(parent);
		this.fallback = notNull(fallback, "Fallback classloader");
	}

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		return fallback.loadClass(name);
	}

	@Override
	protected URL findResource(String name) {
		return fallback.getResource(name);
	}
}
