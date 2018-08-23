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

package com.github.mjeanroy.junit.servers.junit4;

import com.github.mjeanroy.junit.servers.servers.AbstractConfiguration;
import com.github.mjeanroy.junit.servers.servers.EmbeddedServer;
import org.junit.rules.TestRule;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

import java.util.List;

import static com.github.mjeanroy.junit.servers.engine.Servers.instantiate;

/**
 * Runner that will start and stop embedded server before tests.
 * This runner will also add some custom rules to inject data to test classes.
 *
 * <h3>How to use?</h3>
 *
 * Simply set the {@link JunitServerRunner} class with the JUnit {@link org.junit.runner.RunWith} annotation:
 *
 * <pre><code>
 *   &#064;RunWith(JunitServerRunner.class)
 *   public class MyTest {
 *
 *     // Get the server
 *     &#064;TestHttpServer
 *     private static EmbeddedServer server;
 *
 *     // Get a client to query embedded server
 *     &#064;TestHttpServer
 *     private HttpClient client;
 *
 *     &#064;Test
 *     public void testGET() {
 *       HttpResponse rsp = client.prepareGet("/path")
 *         .acceptJson()
 *         .execute();
 *
 *       Assert.assertTrue(rsp.status() == 200);
 *     }
 *   }
 * </code></pre>
 *
 * <h3>Should I use the {@link ServerRule} or the runner?</h3>
 *
 * The runner should be used, but due to the limitation of JUnit (only one runner can be used), the rule can
 * be used if you need to use a custom runner.
 *
 * @see ServerRule
 */
public class JunitServerRunner extends BlockJUnit4ClassRunner {

	/**
	 * Embedded server defined before and after tests.
	 */
	private final EmbeddedServer<?> server;

	/**
	 * Server configuration.
	 */
	private final AbstractConfiguration configuration;

	/**
	 * Create runner starting an embedded server.
	 *
	 * <p/>
	 *
	 * <strong>The embedded server implementation is automatically detected using classpath
	 * detection, so one of {@code junit-servers-jetty} or {@code junit-servers-tomcat} dependency must
	 * be added to the classpath. In case of a conflict (i.e if both dependency are available in the
	 * classpath), {@code junit-servers-jetty} will be used.</strong>
	 *
	 * @param klass Running class.
	 * @throws InitializationError If an error occurred while starting embedded server.
	 */
	public JunitServerRunner(Class<?> klass) throws InitializationError {
		super(klass);
		this.server = instantiate(klass);
		this.configuration = this.server.getConfiguration();
	}

	/**
	 * Create runner with given embedded server.
	 *
	 * @param klass Running class.
	 * @param server The embedded server to use.
	 * @throws InitializationError If an error occurred while starting embedded server.
	 */
	protected JunitServerRunner(Class<?> klass, EmbeddedServer<?> server) throws InitializationError {
		super(klass);
		this.server = server;
		this.configuration = server.getConfiguration();
	}

	@Override
	protected List<TestRule> classRules() {
		List<TestRule> classRules = super.classRules();
		classRules.add(new ServerRule(server));
		return classRules;
	}

	@Override
	protected List<TestRule> getTestRules(Object target) {
		List<TestRule> testRules = super.getTestRules(target);
		testRules.add(new AnnotationsHandlerRule(target, server, configuration));
		return testRules;
	}
}
