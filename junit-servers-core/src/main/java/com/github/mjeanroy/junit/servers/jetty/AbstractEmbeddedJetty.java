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

package com.github.mjeanroy.junit.servers.jetty;

import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.FragmentConfiguration;
import org.eclipse.jetty.webapp.JettyWebXmlConfiguration;
import org.eclipse.jetty.webapp.MetaInfConfiguration;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.webapp.WebInfConfiguration;
import org.eclipse.jetty.webapp.WebXmlConfiguration;

import java.io.File;
import java.io.IOException;
import java.net.URI;

/**
 * Jetty Embedded Server.
 *
 * @param <CONFIGURATION> The jetty configuration implementation.
 */
public abstract class AbstractEmbeddedJetty<
	CONFIGURATION extends AbstractEmbeddedJettyConfiguration
> extends AbstractBaseEmbeddedJetty<WebAppContext, CONFIGURATION> {

	/**
	 * Build embedded jetty server.
	 *
	 * @param configuration Server configuration.
	 */
	protected AbstractEmbeddedJetty(CONFIGURATION configuration) {
		super(configuration);
	}

	@Override
	protected final WebAppContext newWebAppContext() {
		return new WebAppContext();
	}

	@Override
	protected final void setOverrideDescriptor(WebAppContext webAppContext, String overrideDescriptor) {
		webAppContext.setOverrideDescriptor(overrideDescriptor);
	}

	@Override
	protected final void setTempDirectory(WebAppContext webAppContext, String tempDirectory) {
		webAppContext.setTempDirectory(new File(tempDirectory));
	}

	@Override
	protected final void configure(WebAppContext webAppContext) {
		webAppContext.setConfigurations(
			new Configuration[] {
				new WebInfConfiguration(),
				new WebXmlConfiguration(),
				new AnnotationConfiguration(),
				new JettyWebXmlConfiguration(),
				new MetaInfConfiguration(),
				new FragmentConfiguration()
			}
		);
	}

	@Override
	protected final void setParentLoaderPriority(WebAppContext webAppContext, boolean parentLoaderPriority) {
		webAppContext.setParentLoaderPriority(parentLoaderPriority);
	}

	@Override
	protected final void setWar(WebAppContext webAppContext, Resource war) {
		webAppContext.setWarResource(war);
	}

	@Override
	public final Object getServletContext() {
		WebAppContext webAppContext = getWebAppContext();
		return webAppContext == null ? null : webAppContext.getServletContext();
	}

	@Override
	protected final void addContainerResources(WebAppContext webAppContext, Resource containerResources) {
		webAppContext.getMetaData().addContainerResource(containerResources);
	}

	@Override
	protected final void setAttribute(WebAppContext webAppContext, String name, String value) {
		webAppContext.setAttribute(containerJarPatternPropertyName(), value);
	}

	@Override
	protected final Resource newResource(WebAppContext webAppContext, String resource) throws IOException {
		return Resource.newResource(resource);
	}

	@Override
	protected final Resource newResource(WebAppContext webAppContext, URI resource) throws IOException {
		return Resource.newResource(resource);
	}

	@Override
	protected final void setInitParameter(WebAppContext webAppContext, String name, Object value) {
		webAppContext.setInitParameter(name, String.valueOf(value));
	}
}
