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

package com.github.mjeanroy.junit.servers.jetty12;

import com.github.mjeanroy.junit.servers.jetty.AbstractBaseEmbeddedJetty;
import com.github.mjeanroy.junit.servers.jetty.EmbeddedJettyConfiguration;
import org.eclipse.jetty.ee10.annotations.AnnotationConfiguration;
import org.eclipse.jetty.ee10.webapp.FragmentConfiguration;
import org.eclipse.jetty.ee10.webapp.JettyWebXmlConfiguration;
import org.eclipse.jetty.ee10.webapp.MetaInfConfiguration;
import org.eclipse.jetty.ee10.webapp.WebAppContext;
import org.eclipse.jetty.ee10.webapp.WebInfConfiguration;
import org.eclipse.jetty.ee10.webapp.WebXmlConfiguration;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.resource.ResourceFactory;

import java.net.URI;

import static com.github.mjeanroy.junit.servers.jetty.EmbeddedJettyConfiguration.defaultConfiguration;

/**
 * Jetty 12 Embedded Server.
 */
public class EmbeddedJetty extends AbstractBaseEmbeddedJetty<WebAppContext, EmbeddedJettyConfiguration> {

	/**
	 * Build default embedded jetty server.
	 */
	public EmbeddedJetty() {
		super(defaultConfiguration());
	}

	/**
	 * Build embedded jetty server.
	 *
	 * @param configuration Server configuration.
	 */
	public EmbeddedJetty(EmbeddedJettyConfiguration configuration) {
		super(configuration);
	}

	@Override
	protected final WebAppContext newWebAppContext() {
		return new WebAppContext();
	}

	@Override
	protected final String containerJarPatternPropertyName() {
		return MetaInfConfiguration.CONTAINER_JAR_PATTERN;
	}

	@Override
	protected final String webInfJarPatternPropertyName() {
		return MetaInfConfiguration.WEBINF_JAR_PATTERN;
	}

	@Override
	protected final void setOverrideDescriptor(WebAppContext webAppContext, String overrideDescriptor) {
		webAppContext.setOverrideDescriptor(overrideDescriptor);
	}

	@Override
	protected final void configure(WebAppContext webAppContext) {
		webAppContext.addConfiguration(new WebInfConfiguration());
		webAppContext.addConfiguration(new WebXmlConfiguration());
		webAppContext.addConfiguration(new AnnotationConfiguration());
		webAppContext.addConfiguration(new JettyWebXmlConfiguration());
		webAppContext.addConfiguration(new MetaInfConfiguration());
		webAppContext.addConfiguration(new FragmentConfiguration());

		if (webAppContext.getBaseResource() == null) {
			webAppContext.removeConfiguration(WebInfConfiguration.class);
		}
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
	protected final void addContainerResources(WebAppContext webAppContext, Resource containerResources) {
		webAppContext.getMetaData().addContainerResource(containerResources);
	}

	@Override
	public final Object getServletContext() {
		WebAppContext webAppContext = getWebAppContext();
		return webAppContext == null ? null : webAppContext.getServletContext();
	}

	@Override
	protected final void setAttribute(WebAppContext webAppContext, String name, String value) {
		webAppContext.setAttribute(name, value);
	}

	@Override
	protected final Resource newResource(WebAppContext webAppContext, String resource) {
		return ResourceFactory.of(webAppContext).newResource(resource);
	}

	@Override
	protected final Resource newResource(WebAppContext webAppContext, URI resource) {
		return ResourceFactory.of(webAppContext).newResource(resource);
	}

	@Override
	protected final void setInitParameter(WebAppContext webAppContext, String name, Object value) {
		webAppContext.setInitParameter(name, String.valueOf(value));
	}
}
