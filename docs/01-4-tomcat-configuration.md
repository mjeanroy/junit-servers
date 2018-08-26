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
| Base directory   | The base directory, where tomcat will store temporary files.                        | `./tomcat-work`   |
| Naming           | [Tomcat naming](https://tomcat.apache.org/tomcat-8.0-doc/jndi-resources-howto.html) | `true`            |
| Force META-INF   | Force creation of `META-INF` directory if it does not exist.                        | `true`            |

Here is an example (using [JUnit Jupiter](https://junit.org/junit5/docs/current/user-guide/) API) that creates a rule using a configuration object:

```java
import com.github.mjeanroy.junit.servers.jupiter.JunitServerExtension;
import com.github.mjeanroy.junit.servers.tomcat.EmbeddedTomcatConfiguration;

import org.junit.jupiter.api.RegisterExtension;

class MyTest {

  private static final EmbeddedTomcatConfiguration configuration = EmbeddedTomcatConfiguration.builder()
    .withPath("/app")
    .withPort(8080)
    .withWebapp("/src/webapp")
    .withProperty("spring.profiles.active", "test")
    .withOverrideDescriptor("src/test/resources/WEB-INF/web.xml")
    .withParentClasspath(MyTest.class)
    .withBaseDir("/tmp/tomcat")
    .disableNaming()
    .disableForceMetaInf()
    .build());

  @RegisterExtension
  static final JunitServerExtension extension = new JunitServerExtension(configuration);

  // Test suite...
}
```
