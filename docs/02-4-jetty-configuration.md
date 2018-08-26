##### Configuration

The following options can be customized:

{:.table .table-bordered .table-sm}
|                  | Description                                                                         | Default           |
|------------------|-------------------------------------------------------------------------------------|-------------------|
| Port             | The port used by the embedded server.                                               | Random.           |
| Path             | The context path.                                                                   | `/`               |
| Webapp           | The webapp path (relative to project root path).                                    | `src/main/webapp` |
| Hooks            | Server listener function (see details below).                                       | Empty list.       |
| Properties       | Environment properties set before starting server and resetted after shutdown.      | Empty list.       |
| Parent classpath | Override the parent classpath for the deployed application.                         | `null`            |
| Descriptor       | Custom `web.xml`.                                                                   | `null`            |
| Stop timeout     | Set a graceful stop time.                                                           | `30000`           |
| Stop at shutdown | Enable/Disable server stop at shutdown.                                             | `true`            |
| Base resource    | The Jetty base resource (contains the static resources).                            | `null`            |

Here is an example that creates a rule using a configuration object:

```java
import com.github.mjeanroy.junit.servers.jetty.EmbeddedJettyConfiguration;
import com.github.mjeanroy.junit.servers.jetty.junit4.JettyServerJunit4Rule;

import org.junit.ClassRule;

public class MyTest {
  private static final EmbeddedJettyConfiguration configuration = EmbeddedJettyConfiguration.builder()
    .withPath("/app")
    .withPort(8080)
    .withWebapp("/src/webapp")
    .withProperty("spring.profiles.active", "test")
    .withOverrideDescriptor("src/test/resources/WEB-INF/web.xml")
    .withParentClasspath(MyTest.class)
    .withStopTimeout(5000)
    .disableStopAtShutdown()
    .build());

  @ClassRule
  public static final JettyServerJunit4Rule jettyRule = new JettyServerJunit4Rule(configuration);

  // Test suite...
}
```
