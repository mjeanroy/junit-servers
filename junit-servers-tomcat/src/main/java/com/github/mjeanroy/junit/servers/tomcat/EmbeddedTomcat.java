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

package com.github.mjeanroy.junit.servers.tomcat;

import com.github.mjeanroy.junit.servers.commons.CompositeClassLoader;
import com.github.mjeanroy.junit.servers.exceptions.ServerInitializationException;
import com.github.mjeanroy.junit.servers.exceptions.ServerStartException;
import com.github.mjeanroy.junit.servers.exceptions.ServerStopException;
import com.github.mjeanroy.junit.servers.servers.AbstractEmbeddedServer;
import org.apache.catalina.Context;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.StandardRoot;
import org.apache.tomcat.util.scan.StandardJarScanner;

import javax.servlet.ServletContext;
import java.io.File;

import static com.github.mjeanroy.junit.servers.commons.Strings.isNotBlank;
import static com.github.mjeanroy.junit.servers.tomcat.EmbeddedTomcatConfiguration.defaultConfiguration;

/**
 * Embedded server using tomcat as implementation.
 */
public class EmbeddedTomcat extends AbstractEmbeddedServer<Tomcat, EmbeddedTomcatConfiguration> {

	/**
	 * Tomcat instance.
	 */
	private final Tomcat tomcat;

	/**
	 * Tomcat context.
	 */
	private volatile Context context;

	/**
	 * Build embedded tomcat with default configuration.
	 */
	public EmbeddedTomcat() {
		this(defaultConfiguration());
	}

	/**
	 * Build embedded tomcat.
	 *
	 * @param configuration Tomcat configuration.
	 */
	public EmbeddedTomcat(EmbeddedTomcatConfiguration configuration) {
		super(configuration);
		this.tomcat = initServer();
	}

	private Tomcat initServer() {
		Tomcat tomcat = new Tomcat();
		tomcat.setBaseDir(configuration.getBaseDir());
		tomcat.setPort(configuration.getPort());

		tomcat.getHost().setAutoDeploy(true);
		tomcat.getHost().setDeployOnStartup(true);

		if (configuration.isEnableNaming()) {
			tomcat.enableNaming();
		}

		return tomcat;
	}

	private Context initContext() {
		try {
			return createContext();
		}
		catch (Exception ex) {
			throw new ServerInitializationException(ex);
		}
	}

	/**
	 * Create tomcat context.
	 * May be override by subclasses.
	 *
	 * @return Tomcat context.
	 * @throws Exception Exception May be thrown by web app context initialization (will be wrapped later).
	 * @deprecated Protected visibility may be removed in a next release, please submit an issue <a href="https://github.com/mjeanroy/junit-servers/issues">here</a> to discuss it.
	 */
	@Deprecated
	protected Context createContext() throws Exception {
		Context context = null;

		final String webapp = configuration.getWebapp();
		final String path = configuration.getPath();
		final String classpath = configuration.getClasspath();
		final boolean forceMetaInf = configuration.isForceMetaInf();
		final ClassLoader parentClassLoader = configuration.getParentClasspath();
		final String descriptor = configuration.getOverrideDescriptor();

		File webappDirectory = new File(webapp);
		if (webappDirectory.exists()) {
			String webappAbsolutePath = webappDirectory.getAbsolutePath();
			tomcat.getHost().setAppBase(webappAbsolutePath);
			context = tomcat.addWebapp(path, webappAbsolutePath);

			// Add additional classpath entry
			if (isNotBlank(classpath)) {
				File file = new File(classpath);
				if (file.exists()) {

					// Check that additional classpath entry contains META-INF directory
					File metaInf = new File(file, "META-INF");
					if (!metaInf.exists() && forceMetaInf) {
						metaInf.mkdir();
					}

					// == Tomcat 8
					String absolutePath = file.getAbsolutePath();
					StandardRoot root = new StandardRoot(context);
					root.createWebResourceSet(
							WebResourceRoot.ResourceSetType.PRE,
							"/WEB-INF/classes",
							absolutePath,
							null,
							path
					);

					context.setResources(root);

					// == Tomcat 8

					// == Tomcat 7
					// String s = file.toURI().toString();
					// loader.addRepository(s);
					// == Tomcat 7

					// Used to scan additional classpath directory
					// https://issues.apache.org/bugzilla/show_bug.cgi?id=52853
					((StandardJarScanner) context.getJarScanner()).setScanAllDirectories(true);
				}
			}

			// Custom parent classloader.
			final ClassLoader threadCl = Thread.currentThread().getContextClassLoader();
			final ClassLoader tomcatParentClassLoader;

			if (parentClassLoader != null) {
				tomcatParentClassLoader = new CompositeClassLoader(parentClassLoader, threadCl);
			} else {
				tomcatParentClassLoader = threadCl;
			}

			// Set the parent class loader that will be given
			// to the created loader.
			//
			// Setting the parent class loader here is a shortcut for (code in previous versions):
			//
			//   Loader loader = context.getLoader();
			//   if (loader == null) {
			//     loader = new WebappLoader(parentClassLoader);
			//   }
			//
			context.setParentClassLoader(tomcatParentClassLoader);

			// Override web.xml path
			if (descriptor != null) {
				context.setAltDDName(descriptor);
			}
		}

		return context;
	}

	@Override
	public Tomcat getDelegate() {
		return tomcat;
	}

	@Override
	protected void doStart() {
		try {
			context = initContext();
			tomcat.start();
		}
		catch (Exception ex) {
			throw new ServerStartException(ex);
		}
	}

	@Override
	protected void doStop() {
		try {
			tomcat.stop();

			// Do not forget to destroy context
			if (context != null) {
				context.destroy();
				context = null;
			}

			if (!configuration.isKeepBaseDir()) {
				deleteDirectory(configuration.getBaseDir());
			}
		}
		catch (Exception ex) {
			throw new ServerStopException(ex);
		}
	}

	@Override
	public String getScheme() {
		return getConnector().getScheme();
	}

	@Override
	public ServletContext getServletContext() {
		return context == null ? null : context.getServletContext();
	}

	@Override
	protected int doGetPort() {
		return getConnector().getLocalPort();
	}

	private Connector getConnector() {
		return tomcat.getConnector();
	}

	private static void deleteDirectory(String path) {
		if (path == null) {
			return;
		}

		File file = new File(path);
		if (file.exists()) {
			File[] files = file.listFiles();
			if (files != null && files.length > 0) {
				for (File f : files) {
					if (f.isDirectory()) {
						deleteDirectory(f.getAbsolutePath());
					}

					f.delete();
				}
			}

			file.delete();
		}
	}
}
