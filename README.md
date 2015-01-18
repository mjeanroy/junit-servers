# Junit-Servers
---------------------

Simple library that will allow you to use a custom rule to start and stop an embedded
container inside your Junit tests (currently Jetty and Tomcat supported out of the box).

## Installation

With Maven, add explicit dependency:

```xml
    <dependency>
        <groupId>com.github.mjeanroy</groupId>
        <artifactId>junit-servers-jetty</artifactId>
        <version>0.3.1</version>
        <scope>test</scope>
    </dependency>
```

```xml
    <dependency>
        <groupId>com.github.mjeanroy</groupId>
        <artifactId>junit-servers-tomcat</artifactId>
        <version>0.3.1</version>
        <scope>test</scope>
    </dependency>
```

## Jetty

### Default Configuration

For most projects, default configuration will be enough, all you have to do is:

- Extend default AbstractJettyTest that define a class rule used to start and stop container.
- Or use junit rule JettyServerRule to start and stop container in your tests.

Default configuration is:

- A random port is used to start embedded jetty.
- Webapp is set to src/main/webapp (relative to root project)
- Context path is set to '/'

```java
package com.myApp;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.web.client.RestTemplate;
import com.github.mjeanroy.junit.servers.utils.AbstractJettyTest;

public class MyTest extends AbstractJettyTest {
    @Test
    public void myUnitTest() {
        RestTemplate restTemplate = new RestTemplate();
        String url = String.format("http://localhost:%s", server.getPort());

        String response = restTemplate.getForObject(url, String.class);

        assertEquals("Hello World", response);
    }
}
```

```java
package com.myApp;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.web.client.RestTemplate;
import com.github.mjeanroy.junit.servers.jetty.EmbeddedJetty;
import com.github.mjeanroy.junit.servers.rules.JettyServerRule;

public class MyTest {

    @ClassRule
    public static JettyServerRule server = new JettyServerRule(new EmbeddedJetty());

    @Test
    public void myUnitTest() {
        RestTemplate restTemplate = new RestTemplate();
        String url = String.format("http://localhost:%s", server.getPort());

        String response = restTemplate.getForObject(url, String.class);

        assertEquals("Hello World", response);
    }
}
```

You can also use dedicated runner:

```java
package com.myApp;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.web.client.RestTemplate;
import com.github.mjeanroy.junit.servers.annotations.Server;
import com.github.mjeanroy.junit.servers.jetty.EmbeddedJetty;
import com.github.mjeanroy.junit.servers.rules.JettyServerRule;
import com.github.mjeanroy.junit.servers.runner.JunitServerRunner;
import com.github.mjeanroy.junit.servers.servers.EmbeddedServer;

@RunWith(JunitServerRunner.class)
public class MyTest {

    @Server
    private static EmbeddedServer jetty;

    @Test
    public void myUnitTest() {
        RestTemplate restTemplate = new RestTemplate();
        String url = String.format("http://localhost:%s", jetty.getPort());

        String response = restTemplate.getForObject(url, String.class);

        assertEquals("Hello World", response);
    }
}
```

### Custom Configuration

When embedded jetty is created, you can easyly use a custom configuration using a fluent API:

- Define custom enironment properties that will be initialized before server startup and removed after server shutdown.
- Define custom hooks to execute custom code before server startup and after server shutdown.

```java
package com.myApp;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.web.client.RestTemplate;
import com.github.mjeanroy.junit.servers.jetty.EmbeddedJetty;
import com.github.mjeanroy.junit.servers.jetty.EmbeddedJettyConfiguration;
import com.github.mjeanroy.junit.servers.rules.JettyServerRule;
import com.github.mjeanroy.junit.servers.servers.Hook;

public class MyTest {

    private static EmbeddedJettyConfiguration configuration = EmbeddedJettyConfiguration.builder()
        .withPath("/myApp")
        .withWebapp("webapp")
        .withPort(9090)
        .withProperty("spring.profiles.active", "test")
        .withHook(new Hook() {
            @Override
            public void pre(EmbeddedServer server) {
                System.out.println("Server Startup");
            }

            @Override
            public void post(EmbeddedServer server) {
                System.out.println("Server Shutdown");
            }
        })
        .build();

    private static EmbeddedJetty jetty = new EmbeddedJetty(configuration);

    @ClassRule
    public static JettyServerRule server = new JettyServerRule(jetty);

    @Test
    public void myUnitTest() {
        RestTemplate restTemplate = new RestTemplate();
        String url = String.format("http://localhost:%s", server.getPort());

        String response = restTemplate.getForObject(url, String.class);

        assertEquals("Hello World", response);
    }
}
```

If you use junit runner, just annotate configuration with @Configuration and it will be automatically detected:

```java
package com.myApp;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.web.client.RestTemplate;
import com.github.mjeanroy.junit.servers.jetty.EmbeddedJettyConfiguration;
import com.github.mjeanroy.junit.servers.annotations.Configuration;
import com.github.mjeanroy.junit.servers.annotations.Server;
import com.github.mjeanroy.junit.servers.servers.EmbeddedServer;

@RunWith(JunitServerRunner.class)
public class MyTest {

    @Server
	private static EmbeddedServer jetty;

    @Configuration
    private static EmbeddedJettyConfiguration configuration = EmbeddedJettyConfiguration.builder()
        .withPath("/myApp")
        .build();

    @Test
    public void myUnitTest() {
        RestTemplate restTemplate = new RestTemplate();
        String url = String.format("http://localhost:%s", jetty.getPort());

        String response = restTemplate.getForObject(url, String.class);

        assertEquals("Hello World", response);
    }
}
```

## Tomcat

### Default Configuration

For most projects, default configuration will be enough, all you have to do is:

- Extend default AbstractTomcatTest that define a class rule used to start and stop container.
- Or use junit rule TomcatServerRule to start and stop container in your tests.

Default configuration is:

- A random port is used to start embedded tomcat.
- Webapp is set to src/main/webapp (relative to root project)
- Context path is set to "/"
- Base directory is set to tomcat-work (relative to root project). Base directory will be deleted after tests.
- Naming is enable.

```java
package com.myApp;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.web.client.RestTemplate;
import com.github.mjeanroy.junit.servers.utils.AbstractTomcatTest;

public class MyTest extends AbstractTomcatTest {
    @Test
    public void myUnitTest() {
        RestTemplate restTemplate = new RestTemplate();
        String url = String.format("http://localhost:%s", server.getPort());

        String response = restTemplate.getForObject(url, String.class);

        assertEquals("Hello World", response);
    }
}
```

```java
package com.myApp;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.web.client.RestTemplate;
import com.github.mjeanroy.junit.servers.tomcat.EmbeddedTomcat;
import com.github.mjeanroy.junit.servers.rules.TomcatServerRule;

public class MyTest {

    @ClassRule
    public static TomcatServerRule server = new TomcatServerRule(new EmbeddedTomcat());

    @Test
    public void myUnitTest() {
        RestTemplate restTemplate = new RestTemplate();
        String url = String.format("http://localhost:%s", server.getPort());

        String response = restTemplate.getForObject(url, String.class);

        assertEquals("Hello World", response);
    }
}
```

You can also use dedicated runner and inject tomcat using @Server annotation:

```java
package com.myApp;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.web.client.RestTemplate;
import com.github.mjeanroy.junit.servers.annotations.Server;
import com.github.mjeanroy.junit.servers.runner.JunitServerRunner;
import com.github.mjeanroy.junit.servers.servers.EmbeddedServer;
import com.github.mjeanroy.junit.servers.tomcat.EmbeddedTomcatConfiguration;

@RunWith(JunitServerRunner.class)
public class MyTest {

    @Server
	private static EmbeddedServer tomcat;

    @Test
    public void myUnitTest() {
        RestTemplate restTemplate = new RestTemplate();
        String url = String.format("http://localhost:%s", tomcat.getPort());

        String response = restTemplate.getForObject(url, String.class);

        assertEquals("Hello World", response);
    }
}
```

### Custom Configuration

When embedded tomcat is created, you can easyly use a custom configuration using a fluent API:

- Define custom enironment properties that will be initialized before server startup and removed after server shutdown.
- Define custom hooks to execute custom code before server startup and after server shutdown.

```java
package com.myApp;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.web.client.RestTemplate;
import com.github.mjeanroy.junit.servers.tomcat.EmbeddedTomcat;
import com.github.mjeanroy.junit.servers.tomcat.EmbeddedTomcatConfiguration;
import com.github.mjeanroy.junit.servers.rules.TomcatServerRule;
import com.github.mjeanroy.junit.servers.servers.EmbeddedServer;
import com.github.mjeanroy.junit.servers.servers.Hook;

public class MyTest {

    private static EmbeddedTomcatConfiguration configuration = EmbeddedTomcatConfiguration.builder()
        .withPath("/myApp")
        .withWebapp("webapp")
        .withPort(9090)
        .withBaseDir("/tmp/tomcat")
        .disableNaming()
        .withProperty("spring.profiles.active", "test")
        .withHook(new Hook() {
            @Override
            public void pre(EmbeddedServer server) {
                System.out.println("Server Startup");
            }

            @Override
            public void post(EmbeddedServer server) {
                System.out.println("Server Shutdown");
            }
        })
        .build();

    private static EmbeddedServer tomcat = new EmbeddedTomcat(configuration);

    @ClassRule
    public static TomcatServerRule server = new TomcatServerRule(tomcat);

    @Test
    public void myUnitTest() {
        RestTemplate restTemplate = new RestTemplate();
        String url = String.format("http://localhost:%s", server.getPort());

        String response = restTemplate.getForObject(url, String.class);

        assertEquals("Hello World", response);
    }
}
```

With junit runner, use @Configuration to automatically use configuration object:

```java
package com.myApp;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.web.client.RestTemplate;
import com.github.mjeanroy.junit.servers.annotations.Configuration;
import com.github.mjeanroy.junit.servers.annotations.Server;
import com.github.mjeanroy.junit.servers.runner.JunitServerRunner;
import com.github.mjeanroy.junit.servers.servers.EmbeddedServer;
import com.github.mjeanroy.junit.servers.tomcat.EmbeddedTomcatConfiguration;

@RunWith(JunitServerRunner.class)
public class MyTest {

    @Server
    private EmbeddedServer tomcat;

    @Configuration
    private static EmbeddedTomcatConfiguration configuration = EmbeddedTomcatConfiguration.builder()
        .withPath("/myApp")
        .build();

    @Test
    public void myUnitTest() {
        RestTemplate restTemplate = new RestTemplate();
        String url = String.format("http://localhost:%s", tomcat.getPort());

        String response = restTemplate.getForObject(url, String.class);

        assertEquals("Hello World", response);
    }
}
```

## Licence

MIT License (MIT)

## Contributing

If you found a bug or you thing something is missing, feel free to contribute and submit an issue or a pull request.
