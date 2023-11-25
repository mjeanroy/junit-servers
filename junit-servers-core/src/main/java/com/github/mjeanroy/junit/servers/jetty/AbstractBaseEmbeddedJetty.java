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
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.util.resource.Resource;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import static com.github.mjeanroy.junit.servers.commons.lang.Strings.isNotBlank;

/**
 * Jetty Embedded Server.mv
 */
public abstract class AbstractBaseEmbeddedJetty<
	CONTEXT extends ContextHandler,
	CONFIGURATION extends AbstractEmbeddedJettyConfiguration
> extends AbstractEmbeddedServer<Server, CONFIGURATION> {

	/**
	 * Class Logger.
	 */
	private static final Logger log = LoggerFactory.getLogger(AbstractBaseEmbeddedJetty.class);

	/**
	 * Instance of Jetty Server.
	 */
	private final Server server;

	/**
	 * Jetty Web App Context.
	 */
	private volatile CONTEXT webAppContext;

	/**
	 * Server Connector, lazily initialized.
	 */
	private volatile ServerConnector connector;

	/**
	 * Build embedded jetty server.
	 *
	 * @param configuration Server configuration.
	 */
	protected AbstractBaseEmbeddedJetty(CONFIGURATION configuration) {
		super(configuration);
		this.server = initServer();
	}

	private Server initServer() {
		log.debug("Initialize jetty server");
		Server server = new Server(configuration.getPort());
		server.setStopAtShutdown(configuration.isStopAtShutdown());
		server.setStopTimeout(configuration.getStopTimeout());
		return server;
	}

	private CONTEXT initContext() {
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
	public final Server getDelegate() {
		return server;
	}

	@Override
	protected final void doStart() {
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
	private CONTEXT createdWebAppContext() throws Exception {
		final String path = configuration.getPath();
		final String webapp = configuration.getWebapp();
		final String classpath = configuration.getClasspath();
		final ClassLoader parentClassLoader = configuration.getParentClassLoader();
		final String overrideDescriptor = configuration.getOverrideDescriptor();
		final Resource baseResource = configuration.getBaseResource();
		final String containerJarPattern = configuration.getContainerJarPattern();
		final String webInfJarPattern = configuration.getWebInfJarPattern();

		final CONTEXT ctx = newWebAppContext();

		if (containerJarPattern != null) {
			log.debug("Setting jetty 'containerJarPattern' attribute: {}", containerJarPattern);
			setAttribute(ctx, containerJarPatternPropertyName(), containerJarPattern);
		}
		else if (Java.isPostJdk9()) {
			// Fix to make TLD scanning works with Java >= 9
			log.debug("Setting default jetty 'containerJarPattern' for JRE >= 9: {}");
			setAttribute(ctx, containerJarPatternPropertyName(), ".*\\.jar");
		}

		if (webInfJarPattern != null) {
			log.debug("Setting jetty 'WebInfJarPattern' attribute: {}", webInfJarPattern);
			setAttribute(ctx, webInfJarPatternPropertyName(), webInfJarPattern);
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
			actualBaseResource = newResource(ctx, webapp);
		}

		if (actualBaseResource != null) {
			// use default base resource
			log.debug("Initializing jetty base resource: {}", actualBaseResource);
			ctx.setBaseResource(actualBaseResource);
		}

		if (overrideDescriptor != null) {
			log.debug("Set jetty descriptor: {}", overrideDescriptor);
			setOverrideDescriptor(ctx, overrideDescriptor);
		}

		log.debug("Initializing jetty configuration classes");
		configure(ctx);

		if (isNotBlank(classpath)) {
			log.debug("Adding jetty container resource: {}", classpath);

			// Fix to scan Spring WebApplicationInitializer
			// This will add compiled classes to jetty classpath
			// See: http://stackoverflow.com/questions/13222071/spring-3-1-webapplicationinitializer-embedded-jetty-8-annotationconfiguration
			// And more precisely: http://stackoverflow.com/a/18449506/1215828
			File classes = new File(classpath);
			Resource containerResources = newResource(ctx, classes.toURI());
			addContainerResources(ctx, containerResources);
		}

		setInitParameter(ctx, "org.eclipse.jetty.servlet.Default.dirAllowed", configuration.isDirAllowed());
		setParentLoaderPriority(ctx, true);
		setWar(ctx, webapp);
		ctx.setServer(server);

		// Add server context
		server.setHandler(ctx);

		return ctx;
	}

	@Override
	protected final void doStop() {
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
	protected final int doGetPort() {
		return connector.getLocalPort();
	}

	/**
	 * Get Jetty WebAppContext.
	 *
	 * @return WebAppContext, may be {@code null} if Jetty has not been started yet.
	 */
	protected final CONTEXT getWebAppContext() {
		return webAppContext;
	}

	/**
	 * Initialize new instance of Jetty WebAppContext.
	 *
	 * @return New WebAppContext instance.
	 */
	protected abstract CONTEXT newWebAppContext();

	/**
	 * Attribute name for the container JAR pattern configuration flag.
	 *
	 * @return Attribute name.
	 */
	protected abstract String containerJarPatternPropertyName();

	/**
	 * Attribute name for the WebInf pattern configuration flag.
	 *
	 * @return Attribute name.
	 */
	protected abstract String webInfJarPatternPropertyName();

	/**
	 * Set WebAppContext {@code overrideDescriptor} value.
	 *
	 * @param context Jetty WebAppContext.
	 * @param overrideDescriptor The {@code overrideDescriptor} value.
	 */
	protected abstract void setOverrideDescriptor(CONTEXT context, String overrideDescriptor);

	/**
	 * Initialize Jetty WebAppContext configuration classes.
	 *
	 * @param context Jetty WebAppContext.
	 */
	protected abstract void configure(CONTEXT context);

	/**
	 * Set WebAppContext {@code parentLoaderPriority} value.
	 *
	 * @param context Jetty WebAppContext.
	 * @param parentLoaderPriority The {@code parentLoaderPriority} value.
	 */
	protected abstract void setParentLoaderPriority(CONTEXT context, boolean parentLoaderPriority);

	/**
	 * Set WebAppContext {@code war} value.
	 *
	 * @param context Jetty WebAppContext.
	 * @param war The {@code war} value.
	 */
	protected abstract void setWar(CONTEXT context, String war);

	/**
	 * Configure Jetty container resource.
	 *
	 * @param context Jetty WebAppContext.
	 * @param containerResources Container resource.
	 */
	protected abstract void addContainerResources(CONTEXT context, Resource containerResources);

	/**
	 * Set WebAppContext attribute value.
	 *
	 * @param context Jetty WebAppContext.
	 * @param name Attribute name.
	 * @param value Attribute value.
	 */
	protected abstract void setAttribute(CONTEXT context, String name, String value);

	/**
	 * Create new Jetty {@link Resource} for given WebAppContext.
	 *
	 * @param ctx Jetty WebAppContext.
	 * @param resource The resource path.
	 * @return Create {@link Resource}.
	 * @throws IOException In case the resource cannot be created.
	 */
	protected abstract Resource newResource(CONTEXT ctx, String resource) throws IOException;

	/**
	 * Create new Jetty {@link Resource} for given WebAppContext.
	 *
	 * @param ctx Jetty WebAppContext.
	 * @param resource The resource URI.
	 * @return Create {@link Resource}.
	 * @throws IOException In case the resource cannot be created.
	 */
	protected abstract Resource newResource(CONTEXT ctx, URI resource) throws IOException;

	/**
	 * Set WebAppContext initialization parameter value.
	 *
	 * @param ctx Jetty WebAppContext.
	 * @param name Parameter name.
	 * @param value Parameter value.
	 */
	protected abstract void setInitParameter(CONTEXT ctx, String name, Object value);

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
