##### JUnit Jupiter Extension

**Notes:**

The [JUnit Jupiter](https://junit.org/junit5/docs/current/user-guide) extension is available since version `0.10.0`.

Starting with version 3.0.0, it's possible to choose the major Tomcat version:
  - It means that code samples below might be a bit different if you're using `junit-servers-tomcat-8`, `junit-servers-tomcat-9` or `junit-servers-tomcat-10`.
  - The following will assume you are using `junit-servers-tomcat-8`, but please check commented code for instructions to adapt.


Here is a simple example using the JUnit Jupiter extension:

```java
import com.github.mjeanroy.junit.servers.jupiter.JunitServerExtension;
import com.github.mjeanroy.junit.servers.tomcat.EmbeddedTomcatConfiguration;

// If you're using `junit-servers-tomcat-8`
import com.github.mjeanroy.junit.servers.tomcat8.EmbeddedTomcat;

// If you're using `junit-servers-tomcat-9`
// import com.github.mjeanroy.junit.servers.tomcat9.EmbeddedTomcat;

// If you're using `junit-servers-tomcat-10`
// import com.github.mjeanroy.junit.servers.tomcat10.EmbeddedTomcat;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.ExtendWith;
import org.junit.jupiter.api.Test;

@ExtendWith(JunitServerExtension.class)
class MyTest {

  @Test
  void should_have_index(EmbeddedTomcat tomcat, EmbeddedTomcatConfiguration configuration) {
    // Note how the tomcat embedded server has been injected as a parameter of the test method.
    // Note also how the configuration can be injected.
  
    OkHttpClient client = new OkHttpClient();
    Request request = new Request.Builder()
      .url(tomcat.getUrl())
      .build();

    Response response = client.newCall(request).execute();

    Assertions.assertNotNull(configuration);
    Assertions.assertEquals(200, response.code());
  }
}
```

What happens here:

- The `JunitServerExtension` extension:
  - Will automatically start the embedded server **before all** tests and shutdown the server **after all** tests.
  - Automatically inject `EmbeddedTomcat` parameter into the test method (also works with `BeforeEach` methods, etc.). The extension can also inject
    the server instance using the `TestServer` annotation, but it is recommended to use the parameter injection instead.
  - Note that the configuration can also be injected into test methods, exactly like the `EmbeddedTomcat` parameter in the previous example.
- The default configuration is used.
- Note that:
  - I use [OkHttp](http://square.github.io/okhttp/) as HTTP client, but you are free to use your favorite library.
  - You may want to try the HTTP Client API documented [here]({{ '/httpclient/jupiter' | prepend: site.baseurl }}).

The previous example use the default configuration but you can also provide the server configuration using the `TestServerConfiguration` annotation. The extension will scan the tested class to see if this annotation is present and:
- If the annotation is present on a static method, the method is executed and the result is used as the configuration.
- If the annotation is present on a static field, the field value is used as the configuration.

See the example below:

```java
import com.github.mjeanroy.junit.servers.annotations.TestServer;
import com.github.mjeanroy.junit.servers.annotations.TestServerConfiguration;
import com.github.mjeanroy.junit.servers.jupiter.JunitServerExtension;
import com.github.mjeanroy.junit.servers.tomcat.EmbeddedTomcatConfiguration;

// If you're using `junit-servers-tomcat-8`
import com.github.mjeanroy.junit.servers.tomcat8.EmbeddedTomcat;

// If you're using `junit-servers-tomcat-9`
// import com.github.mjeanroy.junit.servers.tomcat9.EmbeddedTomcat;

// If you're using `junit-servers-tomcat-10`
// import com.github.mjeanroy.junit.servers.tomcat10.EmbeddedTomcat;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.ExtendWith;
import org.junit.jupiter.api.Test;

@ExtendWith(JunitServerExtension.class)
class MyTest {

  // The custom configuration that will be used to start the embedded tomcat server.
  @TestServerConfiguration
  static EmbeddedTomcatConfiguration configuration = EmbeddedTomcatConfiguration.builder()
    .withPath("/app")
    .withPort(8080)
    .withProperty("spring.profiles.active", "test")
    .build();

  @Test
  void should_have_index(EmbeddedTomcat tomcat) {
    OkHttpClient client = new OkHttpClient();
    Request request = new Request.Builder()
      .url(tomcat.getUrl())
      .build();

    Response response = client.newCall(request).execute();

    Assertions.assertEquals(200, response.code());
  }
}
```

Alternatively, you can use the `RegisterExtension` [API](https://junit.org/junit5/docs/current/user-guide/#extensions-registration) to provide a custom configuration:

Note that:
- The extension, if used with `static`, will start/stop server before all/after all tests (recommended).
- The extension, if not used with `static`, will start/stop server before each/after each tests.

```java
import com.github.mjeanroy.junit.servers.annotations.TestServer;
import com.github.mjeanroy.junit.servers.annotations.TestServerConfiguration;
import com.github.mjeanroy.junit.servers.jupiter.JunitServerExtension;
import com.github.mjeanroy.junit.servers.tomcat.EmbeddedTomcatConfiguration;

// If you're using `junit-servers-tomcat-8`
import com.github.mjeanroy.junit.servers.tomcat8.EmbeddedTomcat;

// If you're using `junit-servers-tomcat-9`
// import com.github.mjeanroy.junit.servers.tomcat9.EmbeddedTomcat;

// If you're using `junit-servers-tomcat-10`
// import com.github.mjeanroy.junit.servers.tomcat10.EmbeddedTomcat;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RegisterExtension;
import org.junit.jupiter.api.Test;

class MyTest {

  // The extension is used with `static`.
  // Remove it to start/stop server before each/after each test (not recommended).
  @RegisterExtension
  static JunitServerExtension extension = new JunitServerExtension(
    EmbeddedTomcatConfiguration.builder()
      .withPath("/app")
      .withPort(8080)
      .withProperty("spring.profiles.active", "test")
      .build()
  );

  @Test
  void should_have_index(EmbeddedTomcat tomcat) {
    OkHttpClient client = new OkHttpClient();
    Request request = new Request.Builder()
      .url(tomcat.getUrl())
      .build();

    Response response = client.newCall(request).execute();

    Assertions.assertEquals(200, response.code());
  }
}
```

*Note: available options are documented [here]({{ '/tomcat/configuration' | prepend: site.baseurl }}).*
