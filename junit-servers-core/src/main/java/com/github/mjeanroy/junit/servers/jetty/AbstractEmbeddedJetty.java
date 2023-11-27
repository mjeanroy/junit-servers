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

import com.github.mjeanroy.junit.servers.commons.core.CompositeClassLoader;
import com.github.mjeanroy.junit.servers.commons.core.Java;
import com.github.mjeanroy.junit.servers.exceptions.ServerInitializationException;
import com.github.mjeanroy.junit.servers.exceptions.ServerStartException;
import com.github.mjeanroy.junit.servers.exceptions.ServerStopException;
import com.github.mjeanroy.junit.servers.loggers.Logger;
import com.github.mjeanroy.junit.servers.loggers.LoggerFactory;
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

import java.io.File;

import static com.github.mjeanroy.junit.servers.commons.lang.Strings.isNotBlank;
import static org.eclipse.jetty.util.resource.Resource.newResource;

/**
 * Jetty Embedded Server.mv
 */
public abstract class AbstractEmbeddedJetty<
	CONFIGURATION extends AbstractEmbeddedJettyConfiguration
> extends AbstractEmbeddedServer<Server, CONFIGURATION> {

	/**
	 * Class Logger.
	 */
	private static final Logger log = LoggerFactory.getLogger(AbstractEmbeddedJetty.class);

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
	 * Build embedded jetty server.
	 *
	 * @param configuration Server configuration.
	 */
	protected AbstractEmbeddedJetty(CONFIGURATION configuration) {
		super(configuration);
		this.server = initServer();
	}

	protected abstract String containerJarPatternPropertyName();

	protected abstract String webInfJarPatternPropertyName();

	private Server initServer() {
		log.debug("Initialize jetty server");
		Server server = new Server(configuration.getPort());
		server.setStopAtShutdown(configuration.isStopAtShutdown());
		server.setStopTimeout(configuration.getStopTimeout());
		return server;
	}

	private WebAppContext initContext() {
		try {
			log.debug("Initialize jetty webapp context");
			return createdWebAppContext();
		}
		catch (Exception ex) {
			log.error(ex.getMessage(), ex);
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
			log.debug("Initializing embedded jetty context");
			webAppContext = initContext();

			log.debug("Starting embedded jetty");
			server.start();

			log.debug("Looking for embedded jetty server connector");
			connector = findConnector();
		}
		catch (Exception ex) {
			log.error(ex.getMessage(), ex);
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
		final String overrideDescriptor = configuration.getOverrideDescriptor();
		final Resource baseResource = configuration.getBaseResource();
		final String containerJarPattern = configuration.getContainerJarPattern();
		final String webInfJarPattern = configuration.getWebInfJarPattern();

		final WebAppContext ctx = new WebAppContext();

		if (containerJarPattern != null) {
			log.debug("Setting jetty 'containerJarPattern' attribute: {}", containerJarPattern);
			ctx.setAttribute(containerJarPatternPropertyName(), containerJarPattern);
		}
		else if (Java.isPostJdk9()) {
			// Fix to make TLD scanning works with Java >= 9
			log.debug("Setting default jetty 'containerJarPattern' for JRE >= 9: {}");
			ctx.setAttribute(containerJarPatternPropertyName(), ".*\\.jar");
		}

		if (webInfJarPattern != null) {
			log.debug("Setting jetty 'WebInfJarPattern' attribute: {}", webInfJarPattern);
			ctx.setAttribute(webInfJarPatternPropertyName(), webInfJarPattern);
		}

		final ClassLoader systemClassLoader = Thread.currentThread().getContextClassLoader();
		final ClassLoader classLoader;

		if (parentClassLoader != null) {
			log.debug("Overriding jetty parent classloader");
			classLoader = new CompositeClassLoader(parentClassLoader, systemClassLoader);
		}
		else {
			log.debug("Using current thread classloader as jetty parent classloader");
			classLoader = systemClassLoader;
		}

		log.debug("Set jetty classloader");
		ctx.setClassLoader(classLoader);

		log.debug("Set jetty context path to: {}", path);
		ctx.setContextPath(path);

		Resource actualBaseResource = baseResource;
		if (actualBaseResource == null) {
			log.debug("Initializing default jetty base resource from: {}", webapp);
			actualBaseResource = newResource(webapp);
		}

		if (actualBaseResource != null) {
			// use default base resource
			log.debug("Initializing jetty base resource: {}", actualBaseResource);
			ctx.setBaseResource(actualBaseResource);
		}

		if (overrideDescriptor != null) {
			log.debug("Set jetty descriptor: {}", overrideDescriptor);
			ctx.setOverrideDescriptor(overrideDescriptor);
		}

		log.debug("Initializing jetty configuration classes");
		ctx.setConfigurations(new Configuration[] {
			new WebInfConfiguration(),
			new WebXmlConfiguration(),
			new AnnotationConfiguration(),
			new JettyWebXmlConfiguration(),
			new MetaInfConfiguration(),
			new FragmentConfiguration()
		});

		if (isNotBlank(classpath)) {
			log.debug("Adding jetty container resource: {}", classpath);

			// Fix to scan Spring WebApplicationInitializer
			// This will add compiled classes to jetty classpath
			// See: http://stackoverflow.com/questions/13222071/spring-3-1-webapplicationinitializer-embedded-jetty-8-annotationconfiguration
			// And more precisely: http://stackoverflow.com/a/18449506/1215828
			final File classes = new File(classpath);
			final PathResource containerResources = new PathResource(classes.toURI());
			ctx.getMetaData().addContainerResource(containerResources);
		}

		ctx.setInitParameter("org.eclipse.jetty.servlet.Default.dirAllowed", Boolean.toString(configuration.isDirAllowed()));
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
			log.debug("Stopping embedded jettu");
			server.stop();

			log.debug("Clearing jetty webapp context");
			webAppContext = null;

			log.debug("Clearing jetty server connector");
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
	protected int doGetPort() {
		return connector.getLocalPort();
	}

	protected WebAppContext getWebAppContext() {
		return webAppContext;
	}

	private ServerConnector findConnector() {
		log.debug("Extracting jetty server connector");
		for (Connector connector : server.getConnectors()) {
			if (connector instanceof ServerConnector) {
				return (ServerConnector) connector;
			}
		}

		log.warn("Cannot find jetty server connector");
		return null;
	}
}
