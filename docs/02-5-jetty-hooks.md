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
import com.github.mjeanroy.junit.servers.servers.Hook;
import com.github.mjeanroy.junit.servers.jetty.EmbeddedJettyConfiguration;
import com.github.mjeanroy.junit.servers.jetty.junit4.JettyServerJunit4Rule;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;

public class MyTest {
  @ClassRule
  public static final JettyServerJunit4Rule jettyRule = new JettyServerJunit4Rule(EmbeddedJettyConfiguration.builder()
    .withHook(new LogHook())
    .build());

  @Test
  public void should_have_index() {
    OkHttpClient client = new OkHttpClient();
    Request request = new Request.Builder()
      .url(jettyRule.getUrl())
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
