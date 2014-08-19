package com.github.mjeanroy.junit.servers.tomcat;

import static com.github.mjeanroy.junit.servers.commons.Strings.isNotBlank;

import javax.servlet.ServletException;
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

		if (port > 0) {
			tomcat.setPort(port);
		}

		tomcat.getHost().setAutoDeploy(true);
		tomcat.getHost().setDeployOnStartup(true);

		if (enableNaming) {
			tomcat.enableNaming();
		}

		File webappDirectory = new File(webapp);
		if (webappDirectory.exists()) {
			try {
				String webappAbsolutePath = webappDirectory.getAbsolutePath();
				tomcat.getHost().setAppBase(webappAbsolutePath);
				Context context = tomcat.addWebapp(path, webappAbsolutePath);

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
			catch (ServletException ex) {
				throw new ServerInitializationException(ex);
			}
		}

		return tomcat;
	}

	@Override
	protected void doStart() {
		try {
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
			tomcat.destroy();
			deleteDirectory(baseDir);
		}
		catch (Throwable ex) {
			throw new ServerStopException(ex);
		}
	}

	@Override
	public int getPort() {
		return tomcat.getConnector().getPort();
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
