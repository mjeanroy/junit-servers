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

package com.github.mjeanroy.junit.servers.tomcat.junit4;

import com.github.mjeanroy.junit.servers.tomcat.EmbeddedTomcat;
import com.github.mjeanroy.junit.servers.tomcat.EmbeddedTomcatConfiguration;
import com.github.mjeanroy.junit.servers.tomcat.tests.EmbeddedTomcatMockBuilder;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.mockito.InOrder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class TomcatServerJunit4RuleTest {

	@Test
	public void it_should_create_rule_with_server() throws Throwable {
		final EmbeddedTomcatConfiguration config = mock(EmbeddedTomcatConfiguration.class);
		final EmbeddedTomcat tomcat = new EmbeddedTomcatMockBuilder().withConfiguration(config).build();
		final TomcatServerJunit4Rule rule = createRule(tomcat);

		assertThat(rule.getServer()).isSameAs(tomcat);
		assertThat(rule.getScheme()).isEqualTo(tomcat.getScheme());
		assertThat(rule.getHost()).isEqualTo(tomcat.getHost());
		assertThat(rule.getPort()).isEqualTo(tomcat.getPort());
		assertThat(rule.getPath()).isEqualTo(tomcat.getPath());
		assertThat(rule.getUrl()).isEqualTo(tomcat.getUrl());

		verify(tomcat, never()).start();
		verify(tomcat, never()).stop();

		evaluateRule(rule);

		InOrder inOrder = inOrder(tomcat);
		inOrder.verify(tomcat).start();
		inOrder.verify(tomcat).stop();
	}

	@Test
	public void it_should_create_server_from_configuration() throws Throwable {
		final EmbeddedTomcatConfiguration configuration = EmbeddedTomcatConfiguration.defaultConfiguration();
		final TomcatServerJunit4Rule rule = createRule(configuration);

		assertThat(rule.getServer()).isNotNull();
		assertThat(rule.getScheme()).isEqualTo("http");
		assertThat(rule.getHost()).isEqualTo("localhost");
		assertThat(rule.getPath()).isEqualTo(configuration.getPath());
		assertThat(rule.getPort()).isZero(); // not started
		assertThat(rule.getUrl()).isNotEmpty();
		assertRule(rule);
	}

	@Test
	public void it_should_create_server_with_default_configuration() throws Throwable {
		final TomcatServerJunit4Rule rule = createRule();

		assertThat(rule.getServer()).isNotNull();
		assertThat(rule.getScheme()).isEqualTo("http");
		assertThat(rule.getHost()).isEqualTo("localhost");
		assertThat(rule.getPath()).isEqualTo("/");
		assertThat(rule.getPort()).isZero(); // not started
		assertThat(rule.getUrl()).isNotEmpty();
		assertRule(rule);
	}

	/**
	 * Create the rule to be tested.
	 *
	 * @return The rule to be tested.
	 */
	protected TomcatServerJunit4Rule createRule() {
		return new TomcatServerJunit4Rule();
	}

	/**
	 * Create the rule to be tested.
	 *
	 * @param configuration The internal configuration.
	 * @return The rule to be tested.
	 */
	protected TomcatServerJunit4Rule createRule(EmbeddedTomcatConfiguration configuration) {
		return new TomcatServerJunit4Rule(configuration);
	}

	/**
	 * Create the rule to be tested.
	 *
	 * @param tomcat The internal embedded server.
	 * @return The rule to be tested.
	 */
	protected TomcatServerJunit4Rule createRule(EmbeddedTomcat tomcat) {
		return new TomcatServerJunit4Rule(tomcat);
	}

	private static void assertRule(final TomcatServerJunit4Rule rule) throws Throwable {
		final Statement statement = spy(new FakeStatement(rule));

		evaluateRule(rule, statement);

		verify(statement).evaluate();
		assertThat(rule.getPort()).isZero();
	}

	private static void evaluateRule(TomcatServerJunit4Rule rule) throws Throwable {
		Statement statement = mock(Statement.class);
		evaluateRule(rule, statement);
	}

	private static void evaluateRule(TomcatServerJunit4Rule rule, Statement statement) throws Throwable {
		Description description = mock(Description.class);
		Statement test = rule.apply(statement, description);
		test.evaluate();
	}

	private static class FakeStatement extends Statement {
		private final TomcatServerJunit4Rule rule;

		private FakeStatement(TomcatServerJunit4Rule rule) {
			this.rule = rule;
		}

		@Override
		public void evaluate() {
			assertThat(rule.getScheme()).isEqualTo("http");
			assertThat(rule.getHost()).isEqualTo("localhost");
			assertThat(rule.getPath()).isEqualTo("/");
			assertThat(rule.getPort()).isGreaterThan(0);
			assertThat(rule.getUrl()).isNotEmpty();
		}
	}
}
