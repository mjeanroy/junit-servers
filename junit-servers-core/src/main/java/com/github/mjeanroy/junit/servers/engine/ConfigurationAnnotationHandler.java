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

package com.github.mjeanroy.junit.servers.engine;

import com.github.mjeanroy.junit.servers.annotations.TestServerConfiguration;
import com.github.mjeanroy.junit.servers.commons.lang.ToStringBuilder;
import com.github.mjeanroy.junit.servers.loggers.Logger;
import com.github.mjeanroy.junit.servers.loggers.LoggerFactory;
import com.github.mjeanroy.junit.servers.servers.AbstractConfiguration;

import java.lang.reflect.Field;

import static com.github.mjeanroy.junit.servers.commons.lang.Preconditions.notNull;
import static com.github.mjeanroy.junit.servers.commons.reflect.Reflections.setter;

/**
 * Annotation handler that will set configuration to a field
 * annotated with {@link com.github.mjeanroy.junit.servers.annotations.TestServerConfiguration}
 * on a given class instance.
 */
class ConfigurationAnnotationHandler extends AbstractAnnotationHandler {

	/**
	 * Class Logger.
	 */
	private static final Logger log = LoggerFactory.getLogger(ConfigurationAnnotationHandler.class);

	/**
	 * Create new handler.
	 * @param configuration Server configuration.
	 * @param <CONFIGURATION> Type of configuration instance.
	 * @return Handler.
	 * @throws NullPointerException if configuration is null.
	 */
	static <CONFIGURATION extends AbstractConfiguration> AnnotationHandler newConfigurationAnnotationHandler(CONFIGURATION configuration) {
		return new ConfigurationAnnotationHandler(notNull(configuration, "configuration"));
	}

	/**
	 * Server configuration.
	 */
	private final AbstractConfiguration configuration;

	// Use static factory instead
	private ConfigurationAnnotationHandler(AbstractConfiguration configuration) {
		super(TestServerConfiguration.class);
		this.configuration = configuration;
	}

	@Override
	public void before(Object target, Field field) {
		log.debug("Injecting embedded server configuration to {} # {}", target, field);
		setter(target, field, configuration);
	}

	@Override
	public String toString() {
		return ToStringBuilder.create(getClass())
			.append("annotationKlass", getAnnotationKlass())
			.append("configuration", configuration)
			.build();
	}
}
