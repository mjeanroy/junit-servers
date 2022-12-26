[![Build Status](https://travis-ci.org/mjeanroy/junit-servers.svg?branch=master)](https://travis-ci.org/mjeanroy/junit-servers)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.mjeanroy/junit-servers/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.mjeanroy/junit-servers)

JUnit-Servers is an easy-to-use library that will allow you to test your web application inside a servlet container:
- If you use JUnit Jupiter, it is as easy as using a custom extension.
- If you use JUnit 4, it is as easy as using a custom JUnit runner or using a JUnit rule.

JUnit-Servers is simple, and self-contained: you don’t need any dependencies to use it. It is also open-source: if you find a bug or think some features are missing, feel free to submit an issue or, even better, open a pull request!

#### Upgrading to version 3.0.O

Starting with version 3.0.0, it's now possible to choose the major Tomcat/Jetty version to use, just check our upgrade guides for more information:
- Tomcat: [upgrade to 3.0.0]({{ '/tomcat/upgrade' | prepend: site.baseurl }})
- Jetty: [upgrade to 3.0.0]({{ '/jetty/upgrade' | prepend: site.baseurl }})

#### JUnit JUpiter

Here is a simple example that demonstrate how easy it is to test your application using [OkHttp](http://square.github.io/okhttp/) library:

```java
import com.github.mjeanroy.junit.servers.jetty9.EmbeddedJetty;
import com.github.mjeanroy.junit.servers.jupiter.JunitServerExtension;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.ExtendWith;
import org.junit.jupiter.api.Test;

@ExtendWith(JunitServerExtension.class)
class MyTest {

  @Test
  void shoud_respond_to_index(EmbeddedJetty server) {
    OkHttpClient client = new OkHttpClient();
    Request request = new Request.Builder()
      .url(server.getUrl())
      .build();

    Response response = client.newCall(request).execute();

    Assertions.assertEquals(200, response.code());
  }
}
```

#### JUnit 4

Here is a simple example that demonstrate how easy it is to test your application using [OkHttp](http://square.github.io/okhttp/) library:

```java
import com.github.mjeanroy.junit.servers.annotations.TestServer;
import com.github.mjeanroy.junit.servers.jetty9.EmbeddedJetty;
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
    private static EmbeddedJetty server;

    @Test
    public void shoud_respond_to_index() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
            .url(server.getUrl())
            .build();

        Response response = client.newCall(request).execute();

        Assert.assertEquals(200, response.code());
    }
}
```
