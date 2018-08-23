# Junit-Servers

[![Build Status](https://travis-ci.org/mjeanroy/junit-servers.svg?branch=master)](https://travis-ci.org/mjeanroy/junit-servers)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.mjeanroy/junit-servers/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.mjeanroy/junit-servers)

Simple library that will allow you to use a custom rule to start and stop an embedded
container inside your Junit tests (currently Jetty and Tomcat supported out of the box).

[Documentation](https://mjeanroy.github.io/junit-servers/) - [Javadoc](http://javadoc.io/doc/com.github.mjeanroy/junit-servers/) - [Changelog](https://mjeanroy.github.io/junit-servers/changelogs)

## Installation

With Maven, add explicit dependency:

**For Jetty:**

```xml
    <dependency>
        <groupId>com.github.mjeanroy</groupId>
        <artifactId>junit-servers-jetty</artifactId>
        <version>[LATEST VERSION]</version>
        <scope>test</scope>
    </dependency>
```

**For Tomcat:**

```xml
    <dependency>
        <groupId>com.github.mjeanroy</groupId>
        <artifactId>junit-servers-tomcat</artifactId>
        <version>[LATEST VERSION]</version>
        <scope>test</scope>
    </dependency>
```

## Getting Started -- Jetty

[Documentation](https://mjeanroy.github.io/junit-servers/jetty) - [Javadoc](http://javadoc.io/doc/com.github.mjeanroy/junit-servers-jetty)

For most projects, default configuration will be enough, all you have to do is:

- Use JUnit runner `JettyServerRunner` (recommended).
- Use JUnit rule `JettyServerJunit4Rule` to start and stop container in your tests.
- Extend `AbstractJettyJunit4Test` (abstract class that is configured with the `JettyServerRunner` runner).

Default configuration is:

- A random port is used to start embedded jetty.
- Webapp is set to `"src/main/webapp"` (relative to root project)
- Context path is set to `'/'`

**Using the runner:**

```java
import com.github.mjeanroy.junit.servers.annotations.TestServer;
import com.github.mjeanroy.junit.servers.jetty.EmbeddedJetty;
import com.github.mjeanroy.junit.servers.junit4.JunitServerRunner;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JunitServerRunner.class)
public class MyTest {
  @TestServer
  public static EmbeddedJetty jetty;

  @Test
  public void should_have_index() {
    OkHttpClient client = new OkHttpClient();
    Request request = new Request.Builder()
      .url(jetty.getUrl())
      .build();

    Response response = client.newCall(request).execute();

    Assert.assertEquals(200, response.code());
  }
}
```

**Using the rule:**

```java
import com.github.mjeanroy.junit.servers.annotations.TestServer;
import com.github.mjeanroy.junit.servers.annotations.TestServerConfiguration;
import com.github.mjeanroy.junit.servers.jetty.EmbeddedJetty;
import com.github.mjeanroy.junit.servers.jetty.EmbeddedJettyConfiguration;
import com.github.mjeanroy.junit.servers.jetty.junit4.JettyServerJunit4Rule;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;

public class MyTest {
  @ClassRule
  public static JettyServerJunit4Rule jetty = new JettyServerJunit4Rule(EmbeddedJettyConfiguration.build()
      .withPath("/app")
      .withPort(8080)
      .withProperty("spring.profiles.active", "test")
      .build());

  @Test
  public void should_have_index() {
    OkHttpClient client = new OkHttpClient();
    Request request = new Request.Builder()
      .url(jetty.getUrl())
      .build();

    Response response = client.newCall(request).execute();

    Assert.assertEquals(200, response.code());
  }
}
```

**Using the abstract class `AbstractJettyJunit4Test`:**

```java
import com.github.mjeanroy.junit.servers.annotations.TestServer;
import com.github.mjeanroy.junit.servers.jetty.EmbeddedJetty;
import com.github.mjeanroy.junit.servers.jetty.junit4.AbstractJettyJunit4Test;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

public class MyTest extends AbstractJettyJunit4Test {
  @Test
  public void should_have_index() {
    OkHttpClient client = new OkHttpClient();
    Request request = new Request.Builder()
      .url(server.getUrl())
      .build();

    Response response = client.newCall(request).execute();

    Assert.assertEquals(200, response.code());
  }
}
```

For more information about the configuration, see the [documentation](https://mjeanroy.github.io/junit-servers/jetty).

## Getting Started -- Tomcat

[Documentation](https://mjeanroy.github.io/junit-servers/tomcat) - [Javadoc](http://javadoc.io/doc/com.github.mjeanroy/junit-servers-tomcat)

For most projects, default configuration will be enough, all you have to do is:

- Use JUnit runner `JunitServerRunner` (recommended).
- Use JUnit rule `TomcatServerJunit4Rule` to start and stop container in your tests.
- Extend default `AbstractTomcatTest` that run your test with `JunitServerRunner` runner.

Default configuration is:

- A random port is used to start embedded tomcat.
- Webapp is set to `"src/main/webapp"` (relative to root project)
- Context path is set to `"/"`
- Base directory is set to `"tomcat-work"` (relative to root project). By default, base directory will be deleted after tests
  but when the configuration flag `keepBaseDir` is set to `true` the content of this directory will be preserved.
- Naming is enable.

**Using the runner:**

```java
import com.github.mjeanroy.junit.servers.annotations.TestServer;
import com.github.mjeanroy.junit.servers.junit4.JunitServerRunner;
import com.github.mjeanroy.junit.servers.tomcat.EmbeddedTomcat;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JunitServerRunner.class)
public class MyTest {
  @TestServer
  public static EmbeddedTomcat tomcat;

  @Test
  public void should_have_index() {
    OkHttpClient client = new OkHttpClient();
    Request request = new Request.Builder()
      .url(tomcat.getUrl())
      .build();

    Response response = client.newCall(request).execute();

    Assert.assertEquals(200, response.code());
  }
}
```

**Using the rule:**

```java
import com.github.mjeanroy.junit.servers.annotations.TestServer;
import com.github.mjeanroy.junit.servers.annotations.TestServerConfiguration;
import com.github.mjeanroy.junit.servers.tomcat.EmbeddedTomcat;
import com.github.mjeanroy.junit.servers.tomcat.EmbeddedTomcatConfiguration;
import com.github.mjeanroy.junit.servers.tomcat.junit4.TomcatServerJunit4Rule;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;

public class MyTest {
  @ClassRule
  public static TomcatServerJunit4Rule tomcat = new TomcatServerJunit4Rule(EmbeddedTomcatConfiguration.builder()
      .withPath("/app")
      .withPort(8080)
      .withProperty("spring.profiles.active", "test")
      .build());

  @Test
  public void should_have_index() {
    OkHttpClient client = new OkHttpClient();
    Request request = new Request.Builder()
      .url(tomcat.getUrl())
      .build();

    Response response = client.newCall(request).execute();

    Assert.assertEquals(200, response.code());
  }
}
```

**Using the abstract class `AbstractTomcatTest`:**

```java
import com.github.mjeanroy.junit.servers.annotations.TestServer;
import com.github.mjeanroy.junit.servers.tomcat.EmbeddedTomcat;
import com.github.mjeanroy.junit.servers.tomcat.junit4.AbstractTomcatJunit4Test;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

public class MyTest extends AbstractTomcatJunit4Test {
  @Test
  public void should_have_index() {
    OkHttpClient client = new OkHttpClient();
    Request request = new Request.Builder()
      .url(server.getUrl())
      .build();

    Response response = client.newCall(request).execute();

    Assert.assertEquals(200, response.code());
  }
}
```

For more information about the configuration, please see the [documentation](https://mjeanroy.github.io/junit-servers/tomcat).

## Licence

MIT License (MIT)

## Contributing

Thanks to [@skjolber](https://github.com/skjolber) for his [contribution](https://github.com/mjeanroy/junit-servers/pull/4)!

If you found a bug or you thing something is missing, feel free to contribute and submit an issue or a pull request.
