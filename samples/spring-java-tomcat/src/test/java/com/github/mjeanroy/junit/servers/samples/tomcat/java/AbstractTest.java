package com.github.mjeanroy.junit.servers.samples.tomcat.java;

import java.io.File;

import org.junit.ClassRule;

import com.github.mjeanroy.junit.servers.rules.TomcatServerRule;
import com.github.mjeanroy.junit.servers.tomcat.EmbeddedTomcat;
import com.github.mjeanroy.junit.servers.tomcat.EmbeddedTomcatConfiguration;

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
