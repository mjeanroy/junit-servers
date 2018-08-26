##### JUnit Runner

The simplest way to start is to use the dedicated [JUnit runner](https://github.com/junit-team/junit4/wiki/test-runners), see the example below:

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
  static EmbeddedTomcat tomcat;

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
- The `JunitServerRunner` runner will automatically:
  - Start the embedded server **before all** test,
  - Shutdown the server **after all** test.
- The runner can inject the server instance using the `TestServer` annotation. This can be useful to get the URL to query or the chosen port.
- The default configuration is used.
- Note that:
  - I use [OkHttp](http://square.github.io/okhttp/) as HTTP client, but you are free to use your favorite library.
  - You may want to try the HTTP Client API documented [here]({{ '/httpclient' | prepend: site.baseurl }}).

The previous example use the default configuration but you can also provide the server configuration using the `TestServerConfiguration` annotation.
The runner will scan the tested class to see if this annotation is present and:
- If the annotation is present on a static method, the method is executed and the result is used as the configuration.
- If the annotation is present on a static field, the field value is used as the configuration.

See the example below:

```java
import com.github.mjeanroy.junit.servers.annotations.TestServer;
import com.github.mjeanroy.junit.servers.annotations.TestServerConfiguration;
import com.github.mjeanroy.junit.servers.junit4.JunitServerRunner;
import com.github.mjeanroy.junit.servers.tomcat.EmbeddedTomcat;
import com.github.mjeanroy.junit.servers.tomcat.EmbeddedTomcatConfiguration;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JunitServerRunner.class)
public class MyTest {

  // The server configuration that will be used.
  @TestServerConfiguration
  static EmbeddedTomcatConfiguration configuration = EmbeddedTomcatConfiguration.builder()
    .withPath("/app")
    .withPort(8080)
    .withProperty("spring.profiles.active", "test")
    .build();

  @TestServer
  static EmbeddedTomcat tomcat;

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

*Note: available options are documented [here]({{ '/tomcat/configuration' | prepend: site.baseurl }}).*

*We recommend using the JUnit runner since it is easy to use and a "classic way" to extend JUnit. But, since JUnit does not allow more than one runner, you may need to use the class rules describe in the next section: this will allow you to use a second runner (`Parameterized`, `MockitoJUnitRunner`, etc.).*

##### JUnit Rule

Using the [JUnit rule](https://github.com/junit-team/junit4/wiki/Rules) is relatively easy:

```java
import com.github.mjeanroy.junit.servers.tomcat.junit4.TomcatServerJunit4Rule;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;

public class MyTest {

  @ClassRule
  public static final TomcatServerJunit4Rule tomcatRule = new TomcatServerJunit4Rule();

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
- The `TomcatServerJunit4Rule` is used as a **class rule**:
  - The server will be started **before all** tests.
  - The server will be stopped **after all**.
  - If you want to start/stop server between each test, you can use the rule as a "simple" rule (i.e `@Rule`), but we don't recommend it.
- The default configuration is used.

Sometimes, you will have to change some configuration option, this is possible using the dedicated builder:

```java
import com.github.mjeanroy.junit.servers.tomcat.EmbeddedTomcatConfiguration;
import com.github.mjeanroy.junit.servers.tomcat.junit4.TomcatServerJunit4Rule;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;

public class MyTest {

  @ClassRule
  public static final TomcatServerJunit4Rule tomcatRule = new TomcatServerJunit4Rule(
    EmbeddedTomcatConfiguration.builder()
      .withPath("/app")
      .withPort(8080)
      .withProperty("spring.profiles.active", "test")
      .build()
  );

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

*Note: available options are documented [here]({{ '/tomcat/configuration' | prepend: site.baseurl }}).*
