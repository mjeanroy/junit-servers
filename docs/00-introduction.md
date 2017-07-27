JUnit-Servers is an easy-to-use library that will allow you to test your web application inside a real container: it is as easy as using a custom JUnit runner or using a JUnit rule.

[![Build Status](https://travis-ci.org/mjeanroy/junit-servers.svg?branch=master)](https://travis-ci.org/mjeanroy/junit-servers)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.mjeanroy/junit-servers/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.mjeanroy/junit-servers)

Here is a simple example that demonstrate how easy it is to test your application using [OkHttp](http://square.github.io/okhttp/) library:

```java
import com.github.mjeanroy.junit.servers.annotations.TestServer;
import com.github.mjeanroy.junit.servers.jetty.EmbeddedJetty;
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

JUnit-Servers is simple and self contained: you don't need any dependencies to use it. It is also open-source: if you find a bug or if you think some features are missing, feel free to submit an issue or, even better, open a pull request!
