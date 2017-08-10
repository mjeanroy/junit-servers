##### Installation

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.mjeanroy/junit-servers/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.mjeanroy/junit-servers)

First of all, you need to add JUnit-Servers dependency which is available on [maven repositories](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22junit-servers-tomcat%22):

**Maven**

```xml
<dependency>
  <groupId>com.github.mjeanroy</groupId>
  <artifactId>junit-servers-tomcat</artifactId>
  <version>[LATEST VERSION]</version>
  <scope>test</scope>
</dependency>
```

**Gradle**

```groovy
testCompile 'com.github.mjeanroy:junit-servers-tomcat:[LATEST VERSION]'
```

The Javadoc is available on [javadoc.io](https://javadoc.io/): see [here](https://javadoc.io/doc/com.github.mjeanroy/junit-servers-tomcat) and [here](https://javadoc.io/doc/com.github.mjeanroy/junit-servers-core) for the core library.

Once installed, let's see how to use it.

##### JUnit Runner

The simplest way to start is to use the dedicated [JUnit runner](https://github.com/junit-team/junit4/wiki/test-runners), see the example below:

```java
import com.github.mjeanroy.junit.servers.annotations.TestServer;
import com.github.mjeanroy.junit.servers.tomcat.EmbeddedTomcat;
import com.github.mjeanroy.junit.servers.runner.JunitServerRunner;

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

What happens here:
- The `JunitServerRunner` runner will automatically start the embedded server **before all** test and shutdown the server **after all** test (under the hood, the `TomcatServerRule` class rule is automatically added to the test suite).
- The runner can inject the server instance using the `TestServer` annotation. This can be useful to get the URL to query.
- The default configuration is used.
- Note that I use [OkHttp](http://square.github.io/okhttp/) as HTTP client, but you are free to use your favorite library.

The previous code is equivalent to:

```java
import com.github.mjeanroy.junit.servers.tomcat.EmbeddedTomcat;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.junit.Assert;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

public class MyTest {
  private static EmbeddedTomcat tomcat;

  @BeforeClass
  public static void beforeAll() {
    tomcat = new EmbeddedTomcat();
    tomcat.start();
  }

  @AfterClass
  public static void afterAll() {
    tomcat.stop();
  }

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

The previous example use the default configuration but you can also provide the server configuration using the `TestServerConfiguration` annotation. The runner will scan the tested class to see if this annotation is present and:
- If the annotation is present on a static method, the method is executed and the result is used as the configuration.
- If the annotation is present on a static field, the field value is used as the configuration.

See the example below:

```java
import com.github.mjeanroy.junit.servers.annotations.TestServer;
import com.github.mjeanroy.junit.servers.annotations.TestServerConfiguration;
import com.github.mjeanroy.junit.servers.tomcat.EmbeddedTomcat;
import com.github.mjeanroy.junit.servers.tomcat.EmbeddedTomcatConfiguration;
import com.github.mjeanroy.junit.servers.runner.JunitServerRunner;

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

  @TestServerConfiguration
  public static EmbeddedTomcatConfiguration configuration() {
    return EmbeddedTomcatConfiguration.builder()
      .withPath("/app")
      .withPort(8080)
      .withProperty("spring.profiles.active", "test")
      .build();
  }

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

*Note: available options are documented below.*

*We recommend using the JUnit runner since it is easy to use and a "classic way" to extend JUnit. But, since JUnit does not allow more than one runner, you may need to use the class rules describe in the next section: this will allow you to use a second runner (`Parameterized`, `MockitoJUnitRunner`, etc.).*

##### JUnit Rule

Using the [JUnit rule](https://github.com/junit-team/junit4/wiki/Rules) is relatively easy:

```java
import com.github.mjeanroy.junit.servers.rules.TomcatServerRule;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;

public class MyTest {
  @ClassRule
  public static final TomcatServerRule tomcatRule = new TomcatServerRule();

  @Test
  public void should_have_index() {
    OkHttpClient client = new OkHttpClient();
    Request request = new Request.Builder()
      .url(tomcatRule.getUrl())
      .build();

    Response response = client.newCall(request).execute();

    Assert.assertEquals(200, response.code());
  }
}
```

What happens here:
- The `TomcatServerRule` is used as a **class rule**: the server will start **before all** tests, and will stop **after all** (if you want to start/stop server between each test, you can use the rule as a "simple" rule (i.e `@Rule`), but we don't recommend it).
- The default configuration is used.

Sometimes, you will have to change some configuration option, this is possible using the dedicated builder:

```java
import com.github.mjeanroy.junit.servers.rules.TomcatServerRule;
import com.github.mjeanroy.junit.servers.tomcat.EmbeddedTomcatConfiguration;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;

public class MyTest {
  @ClassRule
  public static final TomcatServerRule tomcatRule = new TomcatServerRule(EmbeddedTomcatConfiguration.builder()
    .withPath("/app")
    .withPort(8080)
    .withProperty("spring.profiles.active", "test")
    .build());

  @Test
  public void should_have_index() {
    OkHttpClient client = new OkHttpClient();
    Request request = new Request.Builder()
      .url(tomcatRule.getUrl())
      .build();

    Response response = client.newCall(request).execute();

    Assert.assertEquals(200, response.code());
  }
}
```

*Note: available options are documented below.*

##### Configuration

The following options can be customized:

{:.table .table-bordered .table-sm}
|                  | Description                                                                         | Default           |
|------------------|-------------------------------------------------------------------------------------|-------------------|
| Port             | The port used by the embedded server.                                               | Random.           |
| Path             | The context path.                                                                   | `/`               |
| Webapp           | The webapp path (relative to project root path).                                    | `src/main/webapp` |
| Hooks            | Server listener function (see details below).                                       | Empty list.       |
| Properties       | Environment properties set before starting server and resetted after shutdown.      | Empty list.       |
| Parent classpath | Override the parent classpath for the deployed application.                         | `null`            |
| Descriptor       | Custom `web.xml`.                                                                   | `null`            |
| Base directory   | The base directory, where tomcat will store temporary files.                        | `./tomcat-work`   |
| Naming           | [Tomcat naming](https://tomcat.apache.org/tomcat-8.0-doc/jndi-resources-howto.html) | `true`            |
| Force META-INF   | Force creation of `META-INF` directory if it does not exist.                        | `true`            |

Here is an example that creates a rule using a configuration object:

```java
import com.github.mjeanroy.junit.servers.rules.TomcatServerRule;
import com.github.mjeanroy.junit.servers.tomcat.EmbeddedTomcatConfiguration;

import org.junit.ClassRule;

// Other imports...

public class MyTest {
  private static final EmbeddedTomcatConfiguration configuration = EmbeddedTomcatConfiguration.builder()
    .withPath("/app")
    .withPort(8080)
    .withWebapp("/src/webapp")
    .withProperty("spring.profiles.active", "test")
    .withOverrideDescriptor("src/test/resources/WEB-INF/web.xml")
    .withParentClasspath(MyTest.class)
    .withBaseDir("/tmp/tomcat")
    .disableNaming()
    .disableForceMetaInf()
    .build());

  @ClassRule
  public static final TomcatServerRule tomcatRule = new TomcatServerRule(configuration);

  // Test suite...
}
```

##### Using hooks

Hooks are class that can execute code during server lifecyle:
- Before server starts.
- When server is started.
- After server shutdown.

For example, this can be useful:
- To manage external resources (temporary directories, external storage, etc.).
- To populate a database before starting server.
- To initialize an HTTP client.
- Etc.

The example below use a hook to log server events:

```java
import com.github.mjeanroy.junit.servers.rules.TomcatServerRule;
import com.github.mjeanroy.junit.servers.servers.Hook;
import com.github.mjeanroy.junit.servers.tomcat.EmbeddedTomcatConfiguration;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;

public class MyTest {
  @ClassRule
  public static final TomcatServerRule tomcatRule = new TomcatServerRule(EmbeddedTomcatConfiguration.builder()
    .withHook(new LogHook())
    .build());

  @Test
  public void should_have_index() {
    OkHttpClient client = new OkHttpClient();
    Request request = new Request.Builder()
      .url(tomcatRule.getUrl())
      .build();

    Response response = client.newCall(request).execute();

    Assert.assertEquals(200, response.code());
  }

  private static class LogHook implements Hook {
    @Override
    public void pre(EmbeddedServer<?> server) {
      System.out.println("PRE");
    }

    @Override
    public void post(EmbeddedServer<?> server) {
      System.out.println("POST");
    }

    @Override
    public void onStarted(EmbeddedServer<?> server, ServletContext servletContext) {
      System.out.println("ON STARTED");
    }
  }
}
```
