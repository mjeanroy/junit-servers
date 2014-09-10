/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 <mickael.jeanroy@gmail.com>
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

import static com.github.mjeanroy.junit.servers.commons.Strings.isNotBlank;

import javax.servlet.ServletContext;
import java.io.File;

import org.apache.catalina.Context;
import org.apache.catalina.Loader;
import org.apache.catalina.loader.WebappLoader;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.scan.StandardJarScanner;

import com.github.mjeanroy.junit.servers.exceptions.ServerInitializationException;
import com.github.mjeanroy.junit.servers.exceptions.ServerStartException;
import com.github.mjeanroy.junit.servers.exceptions.ServerStopException;
import com.github.mjeanroy.junit.servers.servers.AbstractEmbeddedServer;

/**
 * Embedded server using tomcat as implementation.
 */
public class EmbeddedTomcat extends AbstractEmbeddedServer {

	/**
	 * Tomcat instance.
	 */
	private final Tomcat tomcat;

	/**
	 * Tomcat context.
	 */
	private volatile Context context;

	/**
	 * Tomcat base directory.
	 */
	private final String baseDir;

	/**
	 * Flag to enable naming.
	 */
	private final boolean enableNaming;

	/**
	 * Flag used to force META-INF directory creation for additional classpath entry.
	 */
	private final boolean forceMetaInf;

	/**
	 * Build embedded tomcat with default configuration.
	 */
	public EmbeddedTomcat() {
		this(new EmbeddedTomcatConfiguration());
	}

	/**
	 * Build embedded tomcat.
	 *
	 * @param configuration Tomcat configuration.
	 */
	public EmbeddedTomcat(EmbeddedTomcatConfiguration configuration) {
		super(configuration);
		this.baseDir = configuration.getBaseDir();
		this.enableNaming = configuration.getEnableNaming();
		this.forceMetaInf = configuration.getForceMetaInf();
		this.tomcat = initServer();
	}

	private Tomcat initServer() {
		Tomcat tomcat = new Tomcat();
		tomcat.setBaseDir(baseDir);
		tomcat.setPort(port);

		tomcat.getHost().setAutoDeploy(true);
		tomcat.getHost().setDeployOnStartup(true);

		if (enableNaming) {
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
	 */
	protected Context createContext() throws Exception {
		Context context = null;

		File webappDirectory = new File(webapp);
		if (webappDirectory.exists()) {
			String webappAbsolutePath = webappDirectory.getAbsolutePath();
			tomcat.getHost().setAppBase(webappAbsolutePath);
			context = tomcat.addWebapp(path, webappAbsolutePath);

			Loader loader = context.getLoader();
			if (loader == null) {
				loader = new WebappLoader(Thread.currentThread().getContextClassLoader());
			}

			// Add additional classpath entry
			if (isNotBlank(classpath)) {
				File file = new File(classpath);
				if (file.exists()) {

					// Check that additional classpath entry contains META-INF directory
					File metaInf = new File(file.getAbsolutePath() + "/META-INF");
					if (!metaInf.exists() && forceMetaInf) {
						metaInf.mkdir();
					}

					String s = file.toURI().toString();
					loader.addRepository(s);

					// Used to scan additional classpath directory
					// https://issues.apache.org/bugzilla/show_bug.cgi?id=52853
					((StandardJarScanner) context.getJarScanner()).setScanAllDirectories(true);
				}
			}

			context.setLoader(loader);
		}

		return context;
	}

	@Override
	protected void doStart() {
		try {
			context = initContext();
			tomcat.start();
		}
		catch (Throwable ex) {
			throw new ServerStartException(ex);
		}
	}

	@Override
	protected void doStop() {
		try {
			tomcat.stop();

			if (context != null) {
				context.destroy();
				context = null;
			}

			tomcat.destroy();
			deleteDirectory(baseDir);
		}
		catch (Throwable ex) {
			throw new ServerStopException(ex);
		}
	}

	@Override
	public int getPort() {
		return tomcat.getConnector().getLocalPort();
	}

	@Override
	public ServletContext getServletContext() {
		return context == null ? null : context.getServletContext();
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
