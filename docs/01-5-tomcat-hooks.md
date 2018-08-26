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

The example below (using [JUnit Jupiter](https://junit.org/junit5/docs/current/user-guide/) API) use a hook to log server events:

```java
import com.github.mjeanroy.junit.servers.jupiter.JunitServerExtension;
import com.github.mjeanroy.junit.servers.servers.EmbeddedServer;
import com.github.mjeanroy.junit.servers.servers.Hook;
import com.github.mjeanroy.junit.servers.tomcat.EmbeddedTomcatConfiguration;

import javax.servlet.ServletContext;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.junit.jupiter.api.RegisterExtension;
import org.junit.jupiter.api.Test;

class MyTest {

  @RegisterExtension
  static final JunitServerExtension extension = new JunitServerExtension(
    EmbeddedTomcatConfiguration.builder()
      .withHook(new LogHook())
      .build()
  );

  @Test
  void should_have_index(EmbeddedTomcat tomcat) {
    OkHttpClient client = new OkHttpClient();
    Request request = new Request.Builder()
      .url(tomcat.getUrl())
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
