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

import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.github.mjeanroy.junit.servers.jupiter.JunitServerExtensionLifecycle.PER_CLASS;

/**
 * Marker annotation that enable {@link JunitServerExtension} and allows configuring
 * the embedded server {@link JunitServerExtensionLifecycle lifecycle}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({
	ElementType.TYPE,
})
@Documented
@Inherited
@ExtendWith(JunitServerExtension.class)
public @interface JunitServerTest {
	/**
	 * Lifecycle, defaults to {@link JunitServerExtensionLifecycle#PER_CLASS} for backward
	 * compatibility reasons, but {@link JunitServerExtensionLifecycle#GLOBAL} is the recommended
	 * settings.
	 *
	 * @return Lifecycle.
	 */
	JunitServerExtensionLifecycle lifecycle() default PER_CLASS;
}
