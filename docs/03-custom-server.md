##### Introduction

Currently, Jetty 9 and Tomcat 8 are supported out of the box (through the specific artifacts). But, what if you want to provide a custom implementation for a specific
embedded server?

Starting with version 0.8.0, `junit-servers` use the [service provider interface](https://docs.oracle.com/javase/7/docs/api/java/util/ServiceLoader.html) available since Java 6: it means that it is now possible
to provide any custom implementation!

##### Example

###### 1- The custom implementation

Suppose this implementation of `EmbeddedServer`:

```java
package com.myapp;

import com.github.mjeanroy.junit.servers.servers.EmbeddedServer;
import javax.servlet.ServletContext;

public class MyCustomEmbeddedServer implements EmbeddedServer<MyCustomConfiguration> {

  private final MyCustomConfiguration configuration;
  private boolean started;

  public MyCustomEmbeddedServer() {
    this.configuration = new MyCustomConfiguration();
    this.started = false;
  }

  public MyCustomEmbeddedServer(MyCustomConfiguration configuration) {
    this.configuration = configuration;
    this.started = false;
  }

  @Override
  public void start() {
    this.started = true;
    System.out.println("Starting Embedded Server");
  }

  @Override
  public void stop() {
    this.started = false;
    System.out.println("Stopping Embedded Server");
  }
  
  @Override
  public void restart() {
    stop();
    start();
  }
  
  @Override
  public MyCustomConfiguration getConfiguration() {
    return configuration;
  }
  
  @Override
  public boolean isStarted() {
    return started;
  }
  
  @Override
  public String getScheme() {
    return "http";
  }
  
  @Override
  public String getHost() {
  	return "localhost";
  }
  
  @Override
  public int getPort() {
    return 80;
  }
  
  @Override
  public String getPath() {
    return "/";
  }
  
  @Override
  public String getUrl() {
    return getScheme() + "://" + getHost() + ":" + getPort() + getPath();
  }
  
  @Override
  public ServletContext getServletContext() {
    throw new UnsupportedOperationException("ServletContext is not supported by this implementation");
  }
}
```

Here is the configuration class:

```java
package com.myapp;

import com.github.mjeanroy.junit.servers.servers.AbstractConfiguration;

public class MyCustomConfiguration extends AbstractConfiguration {
  public MyCustomConfiguration() {
    super();
  }
}
```

Note that this is not a "real" implementation, since it does not really starts/stop a real embedded server... but you got the main idea ;)

###### 2- The test
Here is our unit test:

```java
package com.myapp;

import com.github.mjeanroy.junit.servers.annotations.TestServer;
import com.github.mjeanroy.junit.servers.junit4.JunitServerRunner;
import com.github.mjeanroy.junit.servers.servers.EmbeddedServer;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JunitServerRunner.class)
public class MyTest {
  @TestServer
  public static EmbeddedServer server;

  @Test
  public void should_be_started() {
    Assert.assertTrue(server.isStarted());
  }
}
```

###### 3- Register the custom implementation

So, how can we use our custom `MyCustomEmbeddedServer` in our unit test?

First of all, you need to implement an instance of `EmbeddedServerProvider`:

```java
package com.myapp;

import com.github.mjeanroy.junit.servers.servers.EmbeddedServer;
import com.github.mjeanroy.junit.servers.servers.EmbeddedServerProvider;

public class MyCustomEmbeddedServerProvider implements EmbeddedServerProvider<MyCustomConfiguration> {

  @Override
  public EmbeddedServer<MyCustomConfiguration> instantiate() {
    return new MyCustomEmbeddedServer();
  }
  
  @Override
  public EmbeddedServer<MyCustomConfiguration> instantiate(MyCustomConfiguration configuration) {
    return new MyCustomEmbeddedServer(configuration);
  }
}
```

Once done, create a file named `com.github.mjeanroy.junit.servers.servers.EmbeddedServerProvider` in `META-INF/services` to register our custom implementation.
This file should contains a single line: the fully qualified name of your provider implementation.

```
com.myapp.MyCustomEmbeddedServerProvider
```

You're done! Now, the `JunitServerRunner` annotation will automatically detect your custom implementation and will use it in your unit test ;)

Note that `junit-servers-jetty` and `junit-servers-tomcat` now use the exact same api:

- You can find the provider implementation [here](https://github.com/mjeanroy/junit-servers/blob/junit-servers-0.8.0/junit-servers-jetty/src/main/java/com/github/mjeanroy/junit/servers/jetty/EmbeddedJettyProvider.java) (jetty) and [here](https://github.com/mjeanroy/junit-servers/blob/junit-servers-0.8.0/junit-servers-tomcat/src/main/java/com/github/mjeanroy/junit/servers/tomcat/EmbeddedTomcatProvider.java) (tomcat).
- You can also find the service loader configuration [here](https://github.com/mjeanroy/junit-servers/blob/junit-servers-0.8.0/junit-servers-jetty/src/main/resources/META-INF/services/com.github.mjeanroy.junit.servers.servers.EmbeddedServerProvider) (jetty) and [here](https://github.com/mjeanroy/junit-servers/blob/junit-servers-0.8.0/junit-servers-tomcat/src/main/resources/META-INF/services/com.github.mjeanroy.junit.servers.servers.EmbeddedServerProvider) (here).
