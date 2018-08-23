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

package com.github.mjeanroy.junit.servers.jetty.junit4;

import com.github.mjeanroy.junit.servers.jetty.EmbeddedJetty;
import com.github.mjeanroy.junit.servers.jetty.EmbeddedJettyConfiguration;
import com.github.mjeanroy.junit.servers.jetty.tests.EmbeddedJettyConfigurationMockBuilder;
import com.github.mjeanroy.junit.servers.jetty.tests.EmbeddedJettyMockBuilder;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.mockito.InOrder;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class JettyServerJunit4RuleTest {

	@Test
	public void it_should_create_rule_with_server() throws Throwable {
		final EmbeddedJettyConfiguration config = new EmbeddedJettyConfigurationMockBuilder().build();
		final EmbeddedJetty jetty = new EmbeddedJettyMockBuilder().withConfiguration(config).build();
		final JettyServerJunit4Rule rule = createRule(jetty);

		assertThat(rule.getServer()).isSameAs(jetty);
		assertThat(rule.getScheme()).isEqualTo(jetty.getScheme());
		assertThat(rule.getHost()).isEqualTo(jetty.getHost());
		assertThat(rule.getPort()).isEqualTo(jetty.getPort());
		assertThat(rule.getPath()).isEqualTo(jetty.getPath());
		assertThat(rule.getUrl()).isEqualTo(jetty.getUrl());

		verify(jetty, never()).start();
		verify(jetty, never()).stop();

		evaluateRule(rule);

		InOrder inOrder = Mockito.inOrder(jetty);
		inOrder.verify(jetty).start();
		inOrder.verify(jetty).stop();
	}

	@Test
	public void it_should_create_server_from_configuration() throws Throwable  {
		final EmbeddedJettyConfiguration configuration = EmbeddedJettyConfiguration.defaultConfiguration();
		final JettyServerJunit4Rule rule = createRule(configuration);

		assertThat(rule.getPort()).isZero();
		assertRule(rule);
	}

	@Test
	public void it_should_create_server_with_default_configuration() throws Throwable {
		final JettyServerJunit4Rule rule = createRule();

		assertThat(rule.getPort()).isZero(); // not started
		assertRule(rule);
	}

	/**
	 * Create the rule to be tested.
	 *
	 * @return The rule to be tested.
	 */
	protected JettyServerJunit4Rule createRule() {
		return new JettyServerJunit4Rule();
	}

	/**
	 * Create the rule to be tested.
	 *
	 * @param configuration The internal configuration.
	 * @return The rule to be tested.
	 */
	protected JettyServerJunit4Rule createRule(EmbeddedJettyConfiguration configuration) {
		return new JettyServerJunit4Rule(configuration);
	}

	/**
	 * Create the rule to be tested.
	 *
	 * @param jetty The internal embedded server.
	 * @return The rule to be tested.
	 */
	protected JettyServerJunit4Rule createRule(EmbeddedJetty jetty) {
		return new JettyServerJunit4Rule(jetty);
	}

	private static void assertRule(final JettyServerJunit4Rule rule) throws Throwable {
		final Statement statement = spy(new FakeStatement(rule));

		evaluateRule(rule, statement);

		verify(statement).evaluate();
		assertThat(rule.getPort()).isZero(); // not started
	}

	private static void evaluateRule(JettyServerJunit4Rule rule) throws Throwable {
		final Statement statement = mock(Statement.class);
		evaluateRule(rule, statement);
	}

	private static void evaluateRule(JettyServerJunit4Rule rule, Statement statement) throws Throwable {
		final Description description = mock(Description.class);
		final Statement testStatement = rule.apply(statement, description);
		testStatement.evaluate();
	}

	private static class FakeStatement extends Statement {
		private final JettyServerJunit4Rule rule;

		private FakeStatement(JettyServerJunit4Rule rule) {
			this.rule = rule;
		}

		@Override
		public void evaluate() {
			assertThat(rule.getScheme()).isEqualTo("http");
			assertThat(rule.getHost()).isEqualTo("localhost");
			assertThat(rule.getPath()).isEqualTo("/");
			assertThat(rule.getPort()).isGreaterThan(0);
			assertThat(rule.getUrl()).isEqualTo(
					rule.getScheme() + "://" + rule.getHost() + ":" + rule.getPort() + rule.getPath()
			);
		}
	}
}
