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

package com.github.mjeanroy.junit.servers.jetty;

import com.github.mjeanroy.junit.servers.commons.CompositeClassLoader;
import com.github.mjeanroy.junit.servers.commons.JavaUtils;
import com.github.mjeanroy.junit.servers.exceptions.ServerInitializationException;
import com.github.mjeanroy.junit.servers.exceptions.ServerStartException;
import com.github.mjeanroy.junit.servers.exceptions.ServerStopException;
import com.github.mjeanroy.junit.servers.servers.AbstractEmbeddedServer;
import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.util.resource.PathResource;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.FragmentConfiguration;
import org.eclipse.jetty.webapp.JettyWebXmlConfiguration;
import org.eclipse.jetty.webapp.MetaInfConfiguration;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.webapp.WebInfConfiguration;
import org.eclipse.jetty.webapp.WebXmlConfiguration;

import javax.servlet.ServletContext;
import java.io.File;

import static com.github.mjeanroy.junit.servers.commons.Strings.isNotBlank;
import static com.github.mjeanroy.junit.servers.jetty.EmbeddedJettyConfiguration.defaultConfiguration;
import static org.eclipse.jetty.util.resource.Resource.newResource;

/**
 * Jetty Embedded Server.
 */
public class EmbeddedJetty extends AbstractEmbeddedServer<Server, EmbeddedJettyConfiguration> {

	/**
	 * Instance of Jetty Server.
	 */
	private final Server server;

	/**
	 * Jetty Web App Context.
	 */
	private volatile WebAppContext webAppContext;

	/**
	 * Server Connector, lazily initialized.
	 */
	private volatile ServerConnector connector;

	/**
	 * Build default embedded jetty server.
	 */
	public EmbeddedJetty() {
		this(defaultConfiguration());
	}

	/**
	 * Build embedded jetty server.
	 *
	 * @param configuration Server configuration.
	 */
	public EmbeddedJetty(EmbeddedJettyConfiguration configuration) {
		super(configuration);
		this.server = initServer();
	}

	private Server initServer() {
		Server server = new Server(configuration.getPort());
		server.setStopAtShutdown(configuration.isStopAtShutdown());
		server.setStopTimeout(configuration.getStopTimeout());
		return server;
	}

	private WebAppContext initContext() {
		try {
			return createdWebAppContext();
		}
		catch (Exception ex) {
			throw new ServerInitializationException(ex);
		}
	}

	@Override
	public Server getDelegate() {
		return server;
	}

	@Override
	protected void doStart() {
		try {
			webAppContext = initContext();
			server.start();
			connector = findConnector();
		}
		catch (Exception ex) {
			throw new ServerStartException(ex);
		}
	}

	/**
	 * Build web app context used to launch server.
	 * May be override by subclasses.
	 *
	 * @throws Exception May be thrown by web app context initialization (will be wrapped later).
	 */
	private WebAppContext createdWebAppContext() throws Exception {
		final String path = configuration.getPath();
		final String webapp = configuration.getWebapp();
		final String classpath = configuration.getClasspath();
		final ClassLoader parentClassLoader = configuration.getParentClassLoader();
		final String overrideDescriptor = configuration.getOverrideDescriptor ();
		final Resource baseResource = configuration.getBaseResource();
		final String containerJarPattern = configuration.getContainerJarPattern();
		final String webInfJarPattern = configuration.getWebInfJarPattern();

		WebAppContext ctx = new WebAppContext();

		if (containerJarPattern != null) {
			ctx.setAttribute(WebInfConfiguration.CONTAINER_JAR_PATTERN, containerJarPattern);
		} else if (JavaUtils.isPostJdk9()) {
			// Fix to make TLD scanning works with Java >= 9
			ctx.setAttribute(WebInfConfiguration.CONTAINER_JAR_PATTERN, ".*\\.jar");
		}

		if (webInfJarPattern != null) {
			ctx.setAttribute(WebInfConfiguration.WEBINF_JAR_PATTERN, webInfJarPattern);
		}

		final ClassLoader systemClassLoader = Thread.currentThread().getContextClassLoader();
		final ClassLoader classLoader;

		if (parentClassLoader != null) {
			classLoader = new CompositeClassLoader(parentClassLoader, systemClassLoader);
		} else {
			classLoader = systemClassLoader;
		}

		ctx.setClassLoader(classLoader);
		ctx.setContextPath(path);

		if (baseResource == null) {
			// use default base resource
			ctx.setBaseResource(newResource(webapp));
		} else {
			ctx.setBaseResource(baseResource);
		}

		if (overrideDescriptor != null) {
			ctx.setOverrideDescriptor(overrideDescriptor);
		}

		ctx.setConfigurations(new Configuration[]{
				new WebInfConfiguration(),
				new WebXmlConfiguration(),
				new AnnotationConfiguration(),
				new JettyWebXmlConfiguration(),
				new MetaInfConfiguration(),
				new FragmentConfiguration()
		});

		if (isNotBlank(classpath)) {
			// Fix to scan Spring WebApplicationInitializer
			// This will add compiled classes to jetty classpath
			// See: http://stackoverflow.com/questions/13222071/spring-3-1-webapplicationinitializer-embedded-jetty-8-annotationconfiguration
			// And more precisely: http://stackoverflow.com/a/18449506/1215828
			File classes = new File(classpath);
			PathResource containerResources = new PathResource(classes.toURI());
			ctx.getMetaData().addContainerResource(containerResources);
		}

		ctx.setParentLoaderPriority(true);
		ctx.setWar(webapp);
		ctx.setServer(server);

		// Add server context
		server.setHandler(ctx);

		return ctx;
	}

	@Override
	protected void doStop() {
		try {
			server.stop();
			webAppContext = null;
			connector = null;
		}
		catch (Exception ex) {
			throw new ServerStopException(ex);
		}
	}

	@Override
	public String getScheme() {
		return isStarted() ? server.getURI().getScheme() : super.getScheme();
	}

	@Override
	public ServletContext getServletContext() {
		return webAppContext == null ? null : webAppContext.getServletContext();
	}

	@Override
	protected int doGetPort() {
		return connector.getLocalPort();
	}

	private ServerConnector findConnector() {
		for (Connector connector : server.getConnectors()) {
			if (connector instanceof ServerConnector) {
				return (ServerConnector) connector;
			}
		}
		return null;
	}
}
