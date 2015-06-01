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

package com.github.mjeanroy.junit.servers.samples.tomcat.java;

import javax.servlet.ServletContext;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class IndexWithRulesWithParent01Test
    extends AbstractTest {

    private static RestTemplate restTemplate = new RestTemplate();

    @Test
    public void it_should_have_an_index() {

        final String url = url() + "index";
        final String message = IndexWithRulesWithParent01Test.restTemplate.getForObject(url, String.class);
        Assertions.assertThat(message).isNotEmpty().isEqualTo("Hello World");

        // Try to get servlet context
        final ServletContext servletContext = AbstractTest.serverRule.getServer().getServletContext();
        Assertions.assertThat(servletContext).isNotNull();

        // Try to retrieve spring webApplicationContext
        final WebApplicationContext webApplicationContext =
            WebApplicationContextUtils.getWebApplicationContext(servletContext);
        Assertions.assertThat(webApplicationContext).isNotNull();
    }

    public String url() {

        return String.format("http://%s:%s/", "localhost", AbstractTest.tomcat.getPort());
    }
}
