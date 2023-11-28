/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2023 <mickael.jeanroy@gmail.com>
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

package com.github.mjeanroy.junit.servers.utils.jupiter;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.lang.reflect.Parameter;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

/**
 * A JUnit Jupiter extension for {@link WireMockServer}.
 */
class WireMockExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

	/**
	 * The extension namespace identifier.
	 */
	private static final Namespace NAMESPACE = Namespace.create(WireMockExtension.class);

	/**
	 * The key for the wiremock server in the internal store.
	 */
	private static final String STORE_KEY = "wireMockServer";

	@Override
	public void beforeEach(ExtensionContext context) {
		WireMockServer wireMockServer = new WireMockServer(wireMockConfig()
			.dynamicPort()
		);

		wireMockServer.start();

		WireMock.configureFor("localhost", wireMockServer.port());

		getStore(context).put(STORE_KEY, wireMockServer);
	}

	@Override
	public void afterEach(ExtensionContext context) {
		Store store = getStore(context);
		WireMockServer wireMockServer = store.get(STORE_KEY, WireMockServer.class);

		try {
			wireMockServer.stop();
		}
		finally {
			store.remove(STORE_KEY);
		}
	}

	@Override
	public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
		Parameter parameter = parameterContext.getParameter();
		Class<?> parameterType = parameter.getType();
		return WireMockServer.class.isAssignableFrom(parameterType);
	}

	@Override
	public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
		return getStore(extensionContext).get(STORE_KEY);
	}

	private static Store getStore(ExtensionContext context) {
		return context.getStore(NAMESPACE);
	}
}
