/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 <mickael.jeanroy@gmail.com>, <fernando.ney@gmail.com>
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

import com.github.mjeanroy.junit.servers.rules.TomcatServerRule;
import com.github.mjeanroy.junit.servers.tomcat.EmbeddedTomcat;
import com.github.mjeanroy.junit.servers.tomcat.EmbeddedTomcatConfiguration;
import org.junit.ClassRule;

import java.io.File;

public class AbstractTest {

    private static final String PATH = "samples/spring-java-tomcat/";

    private static EmbeddedTomcatConfiguration configuration = AbstractTest.initConfiguration();

    private static EmbeddedTomcatConfiguration initConfiguration() {

        try {
            String current = new File(".").getCanonicalPath();
            if (!current.endsWith("/")) {
                current += "/";
            }

            final String path = current.endsWith(AbstractTest.PATH) ? current : current + AbstractTest.PATH;

            return EmbeddedTomcatConfiguration.builder().withWebapp(path + "src/main/webapp")
                .withClasspath(path + "target/classes").build();
        } catch (final Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    protected static EmbeddedTomcat tomcat = new EmbeddedTomcat(AbstractTest.configuration);

    @ClassRule
    public static TomcatServerRule serverRule = new TomcatServerRule(AbstractTest.tomcat);

}
