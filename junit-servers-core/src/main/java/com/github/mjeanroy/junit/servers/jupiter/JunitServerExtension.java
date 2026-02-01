/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2026 <mickael.jeanroy@gmail.com>
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
import java.util.Optional;

import static com.github.mjeanroy.junit.servers.commons.lang.Preconditions.notNull;
import static com.github.mjeanroy.junit.servers.jupiter.JunitServerExtensionLifecycle.PER_CLASS;
import static com.github.mjeanroy.junit.servers.jupiter.JunitServerExtensionLifecycle.PER_METHOD;
import static org.junit.platform.commons.support.AnnotationSupport.findAnnotation;

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
	 * The extension lifecycle.
	 */
	private final JunitServerExtensionLifecycle lifecycle;

	/**
	 * The extension namespace.
	 */
	private final Namespace namespace;

	/**
	 * Create the jupiter with default server that will be automatically detected using the Service Provider
	 * API.
	 */
	public JunitServerExtension() {
		this(null, null, null);
	}

	/**
	 * Create the jupiter with default server that will be automatically detected using the Service Provider
	 * API.
	 *
	 * @param lifecycle The extension lifecycle.
	 * @throws NullPointerException If {@code lifecycle} is {@code null}.
	 */
	public JunitServerExtension(JunitServerExtensionLifecycle lifecycle) {
		this(notNull(lifecycle, "lifecycle"), null, null);
	}

	/**
	 * Create the jupiter with given server to start/stop before/after tests.
	 *
	 * @param server The embedded server to use.
	 * @throws NullPointerException If {@code server} is {@code null}.
	 */
	public JunitServerExtension(EmbeddedServer<?> server) {
		this(null, null, notNull(server, "server"));
	}

	/**
	 * Create the jupiter with given server to start/stop before/after tests.
	 *
	 * @param lifecycle The extension lifecycle.
	 * @param server The embedded server to use.
	 * @throws NullPointerException If {@code server} is {@code null}.
	 * @throws NullPointerException If {@code lifecycle} is {@code null}.
	 */
	public JunitServerExtension(JunitServerExtensionLifecycle lifecycle, EmbeddedServer<?> server) {
		this(notNull(lifecycle, "lifecycle"), null, notNull(server, "server"));
	}

	/**
	 * Create the jupiter with given server configuration.
	 *
	 * @param configuration The embedded server configuration to use.
	 * @throws NullPointerException If {@code configuration} is {@code null}.
	 */
	public JunitServerExtension(AbstractConfiguration configuration) {
		this(null, configuration, null);
	}

	/**
	 * Create the jupiter with given server configuration.
	 *
	 * @param lifecycle The extension lifecycle.
	 * @param configuration The embedded server configuration to use.
	 * @throws NullPointerException If {@code configuration} is {@code null}.
	 * @throws NullPointerException If {@code lifecycle} is {@code null}.
	 */
	public JunitServerExtension(JunitServerExtensionLifecycle lifecycle, AbstractConfiguration configuration) {
		this(notNull(lifecycle, "lifecycle"), configuration, null);
	}

	private JunitServerExtension(
		JunitServerExtensionLifecycle lifecycle,
		AbstractConfiguration configuration,
		EmbeddedServer<?> server
	) {
		this.lifecycle = lifecycle;
		this.configuration = configuration;
		this.server = server;
		this.namespace = Namespace.create(getClass().getName());
	}

	@Override
	public void beforeAll(ExtensionContext context) {
		Class<?> testClass = context.getRequiredTestClass();
		JunitServerExtensionLifecycle actualLifecycle = getLifecycle(testClass, PER_CLASS);
		if (actualLifecycle == PER_METHOD) {
			return;
		}

		JunitServerExtensionContext ctx = findContextInStore(context);
		if (ctx != null) {
			return;
		}

		start(
			actualLifecycle.getExtensionContext(context),
			testClass,
			actualLifecycle
		);
	}

	@Override
	public void afterAll(ExtensionContext context) {
		// According to JUnit documentation:
		//
		// ExtensionContext.Store.CloseableResource
		//   An extension context store is bound to its extension context lifecycle.
		//   When an extension context lifecycle ends it closes its associated store.
		//   All stored values that are instances of CloseableResource are notified by an invocation of their close()
		//   method in the inverse order they were added in.
		//
		// Said differently: since JunitServerExtensionContext implements CloseableResource, no need to explicitely stop
		// and close it, it's automatically done by JUnit when the store is closed.
	}

	@Override
	public void beforeEach(ExtensionContext context) {
		JunitServerExtensionContext ctx = findContextInStore(context);

		if (ctx == null) {
			Class<?> testClass = context.getRequiredTestClass();
			JunitServerExtensionLifecycle actualLifecycle = getLifecycle(testClass, PER_METHOD);
			ctx = start(
				actualLifecycle.getExtensionContext(context),
				testClass,
				actualLifecycle
			);
		}

		ctx.getAnnotationsHandler().beforeEach(
			context.getRequiredTestInstance()
		);
	}

	@Override
	public void afterEach(ExtensionContext context) {
		findContextInStore(context).getAnnotationsHandler().afterEach(
			context.getRequiredTestClass()
		);

		// According to JUnit documentation:
		//
		// ExtensionContext.Store.CloseableResource
		//   An extension context store is bound to its extension context lifecycle.
		//   When an extension context lifecycle ends it closes its associated store.
		//   All stored values that are instances of CloseableResource are notified by an invocation of their close()
		//   method in the inverse order they were added in.
		//
		// Said differently: since JunitServerExtensionContext implements CloseableResource, no need to explicitely stop
		// and close it, it's automatically done by JUnit when the store is closed.
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

	private JunitServerExtensionContext start(
		ExtensionContext context,
		Class<?> testClass,
		JunitServerExtensionLifecycle lifecycle
	) {
		log.debug("Register embedded server to junit extension context using lifecycle: {}", lifecycle);

		EmbeddedServer<?> server = this.server == null ? instantiateServer(testClass, configuration) : this.server;
		EmbeddedServerRunner runner = new EmbeddedServerRunner(server);
		JunitServerExtensionContext ctx = new JunitServerExtensionContext(runner);

		ctx.getRunner().beforeAll();

		putContextInStore(context, ctx);

		return ctx;
	}

	private JunitServerExtensionLifecycle getLifecycle(Class<?> testClass, JunitServerExtensionLifecycle defaults) {
		if (lifecycle != null) {
			return lifecycle;
		}

		return findLifecycle(testClass).orElse(defaults);
	}

	/**
	 * Find configured embedded server {@link JunitServerExtensionLifecycle lifecycle}, defaults
	 * to {@link JunitServerExtensionLifecycle#PER_CLASS} for backward compatibility reasons,
	 * but {@link JunitServerExtensionLifecycle#GLOBAL} is the recommended setup and will become the
	 * default in the next major release.
	 *
	 * @param testClass The tested class.
	 * @return The lifecycle configuration, may be empty and will default to {@link JunitServerExtensionLifecycle#PER_CLASS} in this case.
	 */
	protected Optional<JunitServerExtensionLifecycle> findLifecycle(Class<?> testClass) {
		return findAnnotation(testClass, JunitServerTest.class).map(JunitServerTest::lifecycle);
	}

	private JunitServerExtensionContext findContextInStore(ExtensionContext context) {
		return getExtensionStore(context).get(
			JunitServerExtensionContext.class.getName(),
			JunitServerExtensionContext.class
		);
	}

	private void putContextInStore(ExtensionContext context, JunitServerExtensionContext ctx) {
		log.debug("Store context to junit extension context");
		getExtensionStore(context).put(ctx.getClass().getName(), ctx);
	}

	private Store getExtensionStore(ExtensionContext extensionContext) {
		return extensionContext.getStore(namespace);
	}
}
