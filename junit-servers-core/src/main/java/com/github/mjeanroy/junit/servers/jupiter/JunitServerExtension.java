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

package com.github.mjeanroy.junit.servers.jupiter;

import com.github.mjeanroy.junit.servers.annotations.TestHttpClient;
import com.github.mjeanroy.junit.servers.annotations.TestServer;
import com.github.mjeanroy.junit.servers.annotations.TestServerConfiguration;
import com.github.mjeanroy.junit.servers.client.HttpClient;
import com.github.mjeanroy.junit.servers.engine.EmbeddedServerRunner;
import com.github.mjeanroy.junit.servers.engine.Servers;
import com.github.mjeanroy.junit.servers.loggers.Logger;
import com.github.mjeanroy.junit.servers.loggers.LoggerFactory;
import com.github.mjeanroy.junit.servers.servers.AbstractConfiguration;
import com.github.mjeanroy.junit.servers.servers.EmbeddedServer;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

import static com.github.mjeanroy.junit.servers.commons.lang.Preconditions.notNull;
import static com.github.mjeanroy.junit.servers.jupiter.JunitServerExtensionLifecycle.PER_CLASS;
import static com.github.mjeanroy.junit.servers.jupiter.JunitServerExtensionLifecycle.PER_METHOD;

/**
 * Extension for Junit Jupiter.
 *
 * This jupiter will:
 *
 * <ul>
 *   <li>Inject an embedded server <strong>before all</strong> tests and stop it <strong>after all</strong> tests.</li>
 *   <li>Read server configuration annotated with {@link TestServerConfiguration}</li>
 *   <li>
 *     Resolve parameters of type (or any parameters inheriting from):
 *     <ul>
 *       <li>{@link EmbeddedServer}</li>
 *       <li>{@link AbstractConfiguration}</li>
 *       <li>{@link HttpClient}</li>
 *     </ul>
 *   </li>
 *   <li>
 *     Inject class field annotated with:
 *     <ul>
 *       <li>{@link TestServer}</li>
 *       <li>{@link TestServerConfiguration}</li>
 *       <li>{@link TestHttpClient}</li>
 *     </ul>
 *   </li>
 * </ul>
 *
 * <strong>Note that it is strongly recommended to use parameter instead instead of class injection.</strong>
 *
 * For example:
 *
 * <pre><code>
 *  &#064;ExtendWith(JunitServerExtension.class)
 *  public class MyTest {
 *
 *    &#064;Test
 *    void testGET(HttpClient client) {
 *      HttpResponse rsp = client.prepareGet("/path")
 *        .acceptJson()
 *        .execute();
 *
 *      Assertions.assertTrue(rsp.status() == 200);
 *    }
 *  }
 * </code></pre>
 *
 * The extension may also be used with {@link RegisterExtension}, in this case you can use it in two ways:
 *
 * <ul>
 *   <li>
 *     If the extension is declared as {@code static}, the server will be started <strong>before all</strong> tests
 *     and stopped <strong>after all</strong> tests (the recommended way).
 *   </li>
 *   <li>
 *     If the extension is not declared as {@code static}, the server will be started <strong>before each</strong> test
 *     and <strong>stopped after</strong> each test.
 *   </li>
 * </ul>
 *
 * For example:
 *
 * <pre><code>
 * public class MyTest {
 *
 *   // The `static` here means that the server will be started before all tests
 *   // and stopped after all tests.
 *   // Remove the `static` keyword to start/stop server before/after each test (not recommended).
 *   &#064;RegisterExtension
 *   static JunitServerExtension extension = new JunitServerExtension();
 *
 *   &#064;Test
 *   void testGET(HttpClient client) {
 *     HttpResponse rsp = client.prepareGet("/path")
 *       .acceptJson()
 *       .execute();
 *
 *     Assertions.assertTrue(rsp.status() == 200);
 *   }
 * }
 * </code></pre>
 */
public class JunitServerExtension implements BeforeAllCallback, AfterAllCallback, BeforeEachCallback, AfterEachCallback, ParameterResolver {

	/**
	 * Class Logger.
	 */
	private static final Logger log = LoggerFactory.getLogger(JunitServerExtension.class);

	/**
	 * The namespace where each jupiter variable will be stored.
	 */
	private static final Namespace NAMESPACE = Namespace.create(JunitServerExtension.class.getName());

	/**
	 * The list of parameter resolvers.
	 */
	private static final Map<Class<?>, ParameterResolverFunction> RESOLVERS = new HashMap<>();

	static {
		RESOLVERS.put(EmbeddedServer.class, EmbeddedServerParameterResolverFunction.getInstance());
		RESOLVERS.put(AbstractConfiguration.class, ConfigurationResolverFunction.getInstance());
		RESOLVERS.put(HttpClient.class, HttpClientParameterResolverFunction.getInstance());
	}

	/**
	 * The embedded server to use.
	 */
	private final EmbeddedServer<?> server;

	/**
	 * The embedded server configuration when instantiating it.
	 */
	private final AbstractConfiguration configuration;

	/**
	 * Create the jupiter with default server that will be automatically detected using the Service Provider
	 * API.
	 */
	public JunitServerExtension() {
		this.server = null;
		this.configuration = null;
	}

	/**
	 * Create the jupiter with given server to start/stop before/after tests.
	 *
	 * @param server The embedded server to use.
	 * @throws NullPointerException If {@code server} is {@code null}.
	 */
	public JunitServerExtension(EmbeddedServer<?> server) {
		this.server = notNull(server, "server");
		this.configuration = null;
	}

	/**
	 * Create the jupiter with given server configuration.
	 *
	 * @param configuration The embedded server configuration to use.
	 * @throws NullPointerException If {@code configuration} is {@code null}.
	 */
	public JunitServerExtension(AbstractConfiguration configuration) {
		this.server = null;
		this.configuration = configuration;
	}

	@Override
	public void beforeAll(ExtensionContext context) {
		// With nested class, the `beforeAll` is called, that could lead to multiple instances
		// being instantiated.
		JunitServerExtensionContext ctx = findContextInStore(context);
		if (ctx == null) {
			start(context, PER_CLASS);
		}
	}

	@Override
	public void afterAll(ExtensionContext context) {
		stopIfNecessary(context, PER_CLASS);
	}

	@Override
	public void beforeEach(ExtensionContext context) {
		JunitServerExtensionContext ctx = findContextInStore(context);

		// The extension was not declared as a static extension.
		if (ctx == null) {
			ctx = start(context, PER_METHOD);
		}

		ctx.getAnnotationsHandler().beforeEach(
			context.getRequiredTestInstance()
		);
	}

	@Override
	public void afterEach(ExtensionContext context) {
		try {
			findContextInStore(context).getAnnotationsHandler().afterEach(
				context.getRequiredTestClass()
			);
		}
		finally {
			stopIfNecessary(context, PER_METHOD);
		}
	}

	@Override
	public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
		ParameterResolverFunction resolver = findResolver(parameterContext);
		return resolver != null;
	}

	@Override
	public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
		ParameterResolverFunction resolver = findResolver(parameterContext);

		if (resolver == null) {
			return null;
		}

		JunitServerExtensionContext ctx = findContextInStore(extensionContext);

		if (ctx == null) {
			return null;
		}

		return resolver.resolve(
			parameterContext,
			ctx.getRunner()
		);
	}

	private static ParameterResolverFunction findResolver(ParameterContext parameterContext) {
		final Parameter parameter = parameterContext.getParameter();
		final Class<?> parameterClass = parameter.getType();

		// Fast lookup.
		if (RESOLVERS.containsKey(parameterClass)) {
			return RESOLVERS.get(parameterClass);
		}

		for (Class<?> klass : RESOLVERS.keySet()) {
			if (klass.isAssignableFrom(parameterClass)) {
				return RESOLVERS.get(klass);
			}
		}

		return null;
	}

	/**
	 * Instantiate server (implementation to use is automatically detected using the Service Provider
	 * API).
	 *
	 * @param testClass The test class instance.
	 * @param configuration The embedded server configuration to use.
	 * @return The embedded server.
	 */
	protected EmbeddedServer<?> instantiateServer(Class<?> testClass, AbstractConfiguration configuration) {
		log.debug("Instantiating embedded server for test class: {}", testClass);
		return Servers.instantiate(
			findConfiguration(testClass, configuration)
		);
	}

	private AbstractConfiguration findConfiguration(Class<?> testClass, AbstractConfiguration configuration) {
		if (configuration != null) {
			return configuration;
		}

		return Servers.findConfiguration(
			testClass
		);
	}

	private JunitServerExtensionContext start(ExtensionContext context, JunitServerExtensionLifecycle lifecycle) {
		log.debug("Register embedded server to junit extension context");

		Class<?> testClass = context.getRequiredTestClass();
		EmbeddedServer<?> server = this.server == null ? instantiateServer(testClass, configuration) : this.server;
		EmbeddedServerRunner runner = new EmbeddedServerRunner(server);
		JunitServerExtensionContext ctx = new JunitServerExtensionContext(
			runner,
			lifecycle,
			testClass
		);

		ctx.getRunner().beforeAll();

		putContextInStore(context, ctx);

		return ctx;
	}

	private void stopIfNecessary(ExtensionContext context, JunitServerExtensionLifecycle lifecycle) {
		log.debug("Attempt to unregister embedded server to junit extension context");
		JunitServerExtensionContext ctx = findContextInStore(context);

		if (ctx == null) {
			return;
		}

		if (ctx.getLifecycle() != lifecycle) {
			return;
		}

		if (ctx.getTestClass() != context.getRequiredTestClass()) {
			return;
		}

		stop(context, ctx);
	}

	private void stop(ExtensionContext context, JunitServerExtensionContext ctx) {
		log.debug("Unregister embedded server to junit extension context");
		try {
			ctx.getRunner().afterAll();
		}
		finally {
			removeContextInStore(context);
		}
	}

	private static JunitServerExtensionContext findContextInStore(ExtensionContext context) {
		return getExtensionStore(context).get(
			JunitServerExtensionContext.class.getName(),
			JunitServerExtensionContext.class
		);
	}

	private static void putContextInStore(ExtensionContext context, JunitServerExtensionContext ctx) {
		log.debug("Store context to junit extension context");
		getExtensionStore(context).put(ctx.getClass().getName(), ctx);
	}

	private static void removeContextInStore(ExtensionContext context) {
		log.debug("Clearing junit extension context");
		getExtensionStore(context).remove(JunitServerExtensionContext.class.getName());
	}

	private static Store getExtensionStore(ExtensionContext extensionContext) {
		return extensionContext.getStore(NAMESPACE);
	}
}
