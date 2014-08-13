package com.github.mjeanroy.junit.servers.jetty;

import static org.eclipse.jetty.util.resource.Resource.newResource;

import java.io.File;

import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.util.resource.FileResource;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.webapp.WebXmlConfiguration;

import com.github.mjeanroy.junit.servers.exceptions.ServerStartException;
import com.github.mjeanroy.junit.servers.exceptions.ServerStopException;
import com.github.mjeanroy.junit.servers.servers.AbstractEmbeddedServer;
import com.github.mjeanroy.junit.servers.servers.EmbeddedServerConfiguration;

/**
 * Jetty Embedded Server.
 */
public class EmbeddedJetty extends AbstractEmbeddedServer {

	/** Instance of Jetty Server. */
	private final Server server;

	/** Server Connector, lazily initialized. */
	private ServerConnector connector;

	/** Build default embedded jetty server. */
	public EmbeddedJetty() {
		super();
		this.server = initServer();
	}

	/**
	 * Build embedded jetty server.
	 *
	 * @param configuration Server configuration.
	 */
	public EmbeddedJetty(EmbeddedServerConfiguration configuration) {
		super(configuration);
		this.server = initServer();
	}

	private Server initServer() {
		Server server = new Server(port);
		server.setStopAtShutdown(true);
		server.setStopTimeout(10000);
		return server;
	}

	@Override
	protected void doStart() {
		try {
			WebAppContext ctx = new WebAppContext();
			ctx.setClassLoader(Thread.currentThread().getContextClassLoader());
			ctx.setContextPath(path);
			// Useful for WebXmlConfiguration
			ctx.setBaseResource(newResource(webapp));

			ctx.setConfigurations(new Configuration[]{
					new WebXmlConfiguration(),
					new AnnotationConfiguration()
			});

			File classes = new File("./target/classes");
			FileResource containerResources = new FileResource(classes.toURI());
			ctx.getMetaData().addContainerResource(containerResources);

			ctx.setParentLoaderPriority(true);
			ctx.setWar(webapp);
			ctx.setServer(server);

			server.setHandler(ctx);
			server.start();
		}
		catch (Exception ex) {
			throw new ServerStartException(ex);
		}
	}

	@Override
	protected void doStop() {
		try {
			server.stop();
		}
		catch (Exception ex) {
			throw new ServerStopException(ex);
		}
	}

	@Override
	public int getPort() {
		if (connector == null) {
			connector = findConnector();
		}

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
