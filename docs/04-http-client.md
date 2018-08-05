##### Introduction

Now that you have an embedded server running with your JUnit tests, there are great chances that you need to query it. The embedded server expose useful methods (especially: `getPort()`, `getPath()`, `getUrl()`) that you can use with your favorite HTTP client library (I personally like [OkHttp](http://square.github.io/okhttp/), [AsyncHttpClient](https://github.com/AsyncHttpClient/async-http-client) or [Apache HTTPComponent](https://hc.apache.org/)).

For example, using [AsyncHttpClient](https://github.com/AsyncHttpClient/async-http-client):

```java
@RunWith(JunitServerRunner.class)
public class MyTest {
  @TestServer
  public static EmbeddedJetty jetty;

  private AsyncHttpClient client;

  @Before
  public static void beforeAll() {
    client = new DefaultAsyncHttpClient();
  }

  @After
  public static void afterAll() {
    client.close();
  }

  @Test
  public void should_have_index() {
    OkHttpClient client = new OkHttpClient();
    Response response = client
      .prepareGet(jetty.getUrl())
      .execute()
      .get();

    Assert.assertEquals(200, response.getStatusCode());
  }
}
```

Note that depending on your HTTP client library, you have to destroy the client after running your test suite (the `client.destroy()` call in the `afterAll` static method).

This is a lot of boilerplate code, let's see how we can **do better**!

##### Installation

First of all, `junit-servers` does not embed a new HTTP client written from scratch, so you need to add a dependency on your favorite HTTP client library. Currently, three libraries are supported (feel free to open issue if your favorite library is not currently supported):

**OkHttp**

```xml
<dependency>
  <groupId>com.squareup.okhttp3</groupId>
  <artifactId>okhttp</artifactId>
  <version>3.8.1</version>
  <scope>test</scope>
</dependency>
```

**AsyncHttpClient**

```xml
<dependency>
  <groupId>org.asynchttpclient</groupId>
  <artifactId>async-http-client</artifactId>
  <version>2.0.33</version>
  <scope>test</scope>
</dependency>
```

**Apache HTTPComponent**

```xml
<dependency>
  <groupId>org.apache.httpcomponents</groupId>
  <artifactId>httpclient</artifactId>
  <version>4.5.3</version>
  <scope>test</scope>
</dependency>
```

Let's see how to use it!

##### Using the runner

If you use the `JunitServerRunner`, you can inject an HTTP client to your test suite:

```java
import com.github.mjeanroy.junit.servers.annotations.TestServer;
import com.github.mjeanroy.junit.servers.annotations.TestHttpClient;
import com.github.mjeanroy.junit.servers.client.HttpClient;
import com.github.mjeanroy.junit.servers.client.HttpResponse;
import com.github.mjeanroy.junit.servers.runner.JunitServerRunner;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JunitServerRunner.class)
public class MyTest {
  @TestServer
  public static EmbeddedJetty jetty;

  // The client is automatically injected by the `JunitServerRunner` runner.
  @TestHttpClient
  public HttpClient client;

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
import com.github.mjeanroy.junit.servers.rules.JettyServerRule;

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

##### The HTTP client

The HTTP client allow you to:
- Execute `GET`, `POST`, `PUT`, `DELETE`, `PATCH` and `HEAD` requests.
- Add query params (i.e URL parameters following `?`).
- Send body (only with `POST`, `PUT` and `PATCH` methods).
- Send headers and cookies.

Here is an example of a `POST` request sending a JSON body:

```java
import com.github.mjeanroy.junit.servers.client.HttpClient;
import com.github.mjeanroy.junit.servers.client.HttpResponse;
import com.github.mjeanroy.junit.servers.rules.JettyServerRule;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;

public class MyTest {
  @ClassRule
  public static final JettyServerRule jettyRule = new JettyServerRule();

  @Test
  public void should_have_index() {
    HttpResponse response = jettyRule.getClient()
      .preparePost("/people")
      .setBody("{\"firstName\": \"John\", \"lastName\": \"Doe\"}")
      .asJson()     // Automatically send the `Content-Type` header to `application/json`
      .acceptJson() // Automatically send the `Accept` header to `application/json`
      .addHeader("X-Auth-Token", TOKEN_VALUE)
      .addCookie("X-Auth-Token", TOKEN_VALUE)
      .execute();

    Assert.assertEquals(204, response.status());
  }
}
```
