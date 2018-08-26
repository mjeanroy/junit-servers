##### JUnit Jupiter

If you use the `JunitServerExtension`, there are two ways to get an HTTP client:
- Inject the `HttpClient` to your test methods (recommended).
- Inject the http client as a class field.

For example, here is how you can inject the HTTP Client in your test methods:

```java
import com.github.mjeanroy.junit.servers.client.HttpClient;
import com.github.mjeanroy.junit.servers.client.HttpResponse;
import com.github.mjeanroy.junit.servers.jupiter.JunitServerExtension;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.ExtendWith;
import org.junit.jupiter.api.Test;

@ExtendWith(JunitServerExtension.class)
class MyTest {

  @Test
  void should_have_index(HttpClient client) {
    HttpResponse response = client
      .prepareGet("/")
      .execute();

    Assertions.assertEquals(200, response.status());
  }
}
```

What happens here:
1. An HTTP client is automatically instantiated, the "real" implementation is automatically detected using classpath detection.
2. The HTTP Client is automatically injected as a parameter of your test method.
3. A `GET` query is created (with the `prepareGet` method): the endpoint is automatically built using the embedded server URL (port and context path) initialized with the runner.
4. The request is executed, the response is synchronously returned.
5. When the test suite is fully executed, **the HTTP client is automatically destroyed**.

It is important to note that:
- If you want to **change the implementation** ([OkHttp](http://square.github.io/okhttp/), [AsyncHttpClient](https://github.com/AsyncHttpClient/async-http-client) or [Apache HTTPComponent](https://hc.apache.org/)), you only have to update your dependencies, **the test code does not have to change**.
- The HTTP client is **automatically instantiated and destroyed**: you don't need to manually call the `destroy` method.

You can also inject the HTTP Client as a class field:

```java
import com.github.mjeanroy.junit.servers.annotations.TestHttpClient;
import com.github.mjeanroy.junit.servers.client.HttpClient;
import com.github.mjeanroy.junit.servers.client.HttpResponse;
import com.github.mjeanroy.junit.servers.jupiter.JunitServerExtension;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.ExtendWith;
import org.junit.jupiter.api.Test;

@ExtendWith(JunitServerExtension.class)
class MyTest {

  @TestHttpClient
  HttpClient client;

  @Test
  void should_have_index() {
    HttpResponse response = client
      .prepareGet("/")
      .execute();

    Assertions.assertEquals(200, response.status());
  }
}
```
