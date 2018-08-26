##### Using the runner

If you use the `JunitServerRunner`, you can inject an HTTP client to your test suite:

```java
import com.github.mjeanroy.junit.servers.annotations.TestServer;
import com.github.mjeanroy.junit.servers.annotations.TestHttpClient;
import com.github.mjeanroy.junit.servers.client.HttpClient;
import com.github.mjeanroy.junit.servers.client.HttpResponse;
import com.github.mjeanroy.junit.servers.jetty.EmbeddedJetty;
import com.github.mjeanroy.junit.servers.junit4.JunitServerRunner;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JunitServerRunner.class)
public class MyTest {

  @TestServer
  static EmbeddedJetty jetty;

  // The client is automatically injected by the `JunitServerRunner` runner.
  @TestHttpClient
  HttpClient client;

  @Test
  public void should_have_index() {
    // Query the deployed application.
    HttpResponse response = client
      .prepareGet("/")
      .execute();

    Assert.assertEquals(200, response.status());
  }
}
```

What happens here:
1. An HTTP client is automatically instantiated, the "real" implementation is automatically detected using classpath detection.
2. A `GET` query is created (with the `prepareGet` method): the endpoint is automatically built using the embedded server URL (port and context path) initialized with the runner.
3. The request is executed, the response is synchronously returned.
4. When the test suite is fully executed, **the HTTP client is automatically destroyed**.

It is important to note that:
- If you want to **change the implementation** ([OkHttp](http://square.github.io/okhttp/), [AsyncHttpClient](https://github.com/AsyncHttpClient/async-http-client) or [Apache HTTPComponent](https://hc.apache.org/)), you only have to update your dependencies, **the test code does not have to change**.
- The HTTP client is **automatically instantiated and destroyed**: you don't need to manually call the `destroy` method.

##### Using the rule

You can also retrieve an HTTP client using the class rule:

```java
import com.github.mjeanroy.junit.servers.client.HttpClient;
import com.github.mjeanroy.junit.servers.client.HttpResponse;
import com.github.mjeanroy.junit.servers.jetty.junit4.JettyServerRule;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;

public class MyTest {
  @ClassRule
  public static final JettyServerRule jettyRule = new JettyServerRule();

  @Test
  public void should_have_index() {
    // Query the deployed application.
    HttpResponse response = jettyRule.getClient()
      .prepareGet("/")
      .execute();

    Assert.assertEquals(200, response.status());
  }
}
```

You can get an HTTP client instance using the `getClient` method: **the client will be automatically instantiated and destroyed** when the test suite is fully executed.
