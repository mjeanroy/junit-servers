##### Introduction

Now that you have an embedded server running with your JUnit tests, there are great chances that you need to query it.
The embedded server expose useful methods (especially: `getPort()`, `getPath()`, `getUrl()`) that you can use with your favorite HTTP client library (I personally like [OkHttp](http://square.github.io/okhttp/), [AsyncHttpClient](https://github.com/AsyncHttpClient/async-http-client) or [Apache HTTPComponent](https://hc.apache.org/)).

For example, using [AsyncHttpClient](https://github.com/AsyncHttpClient/async-http-client):

```java
import com.github.mjeanroy.junit.servers.annotations.TestServer;
import com.github.mjeanroy.junit.servers.jetty.EmbeddedJetty;
import com.github.mjeanroy.junit.servers.jupiter.JunitServerExtension;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.Response;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.ExtendWith;
import org.junit.jupiter.api.Test;

@ExtendWith(JunitServerExtension.class)
class MyTest {

  private static AsyncHttpClient client;

  @BeforeAll
  static void beforeAll() {
    client = new DefaultAsyncHttpClient();
  }

  @AfterAll
  static void afterAll() throws Exception {
    client.close();
  }

  @Test
  void should_have_index(EmbeddedJetty jetty) {
    Response response = client
      .prepareGet(jetty.getUrl())
      .execute()
      .get();

    Assertions.assertEquals(200, response.getStatusCode());
  }
}
```

Note that depending on your HTTP client library, you may have to destroy the client after running your test suite (the `client.destroy()` call in the `afterAll` static method).

This is a lot of boilerplate code, let's see how we can **do better**!

##### Installation

First of all, `junit-servers` does not embed an internal HTTP client written from scratch, so you will need to add a dependency on your favorite HTTP client library. Currently, three libraries are supported (feel free to open issue if your favorite library is not currently supported):

- [http://square.github.io/okhttp/](OkHttp)
- [https://github.com/AsyncHttpClient/async-http-client](Async-Http-Client)
- [https://hc.apache.org/](Apache HTTPComponent)

**OkHttp**

```xml
<dependency>
  <groupId>com.squareup.okhttp3</groupId>
  <artifactId>okhttp</artifactId>
  <version>3.11.0</version>
  <scope>test</scope>
</dependency>
```

**AsyncHttpClient**

```xml
<dependency>
  <groupId>org.asynchttpclient</groupId>
  <artifactId>async-http-client</artifactId>
  <version>2.5.2</version>
  <scope>test</scope>
</dependency>
```

**Apache HTTPComponent**

```xml
<dependency>
  <groupId>org.apache.httpcomponents</groupId>
  <artifactId>httpclient</artifactId>
  <version>4.5.6</version>
  <scope>test</scope>
</dependency>
```

Let's see how to use it!
