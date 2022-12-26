##### Upgrade to version 3.0.0

###### Dependency Upgrade

Starting with version 3.0.0, it's now possible to choose the major version used under the hood by importing the right package:
- Jetty 8: `junit-servers-jetty-9`
- Jetty 9: `junit-servers-jetty-10`
- Jetty 10: `junit-servers-jetty-11`

For backward compatibility reason, the module `junit-servers-jetty` still exists, but is now deprecated. To upgrade to version 3.0.0, just use `junit-servers-jetty-9` instead (`junit-servers-jetty` uses Jetty 9 under the hood).

###### Package Upgrade

Some packages have been changed, and you should now replace:

**`com.github.mjeanroy.junit.servers.jetty.EmbeddedJetty`**:
  - Jetty 9: `com.github.mjeanroy.junit.servers.jetty9.EmbeddedJetty`
  - Jetty 10: `com.github.mjeanroy.junit.servers.jetty10.EmbeddedJetty`
  - Jetty 11: `com.github.mjeanroy.junit.servers.jetty11.EmbeddedJetty`

**`com.github.mjeanroy.junit.servers.jetty.EmbeddedJettyFactory`**:
  - Jetty 9: `com.github.mjeanroy.junit.servers.jetty9.EmbeddedJettyFactory`
  - Jetty 10: `com.github.mjeanroy.junit.servers.jetty10.EmbeddedJettyFactory`
  - Jetty 11: `com.github.mjeanroy.junit.servers.jetty11.EmbeddedJettyFactory`

**`com.github.mjeanroy.junit.servers.jetty.EmbeddedJettyProvider`**:
  - Jetty 9: `com.github.mjeanroy.junit.servers.jetty9.EmbeddedJettyProvider`
  - Jetty 10: `com.github.mjeanroy.junit.servers.jetty10.EmbeddedJettyProvider`
  - Jetty 11: `com.github.mjeanroy.junit.servers.jetty11.EmbeddedJettyProvider`


**`com.github.mjeanroy.junit.servers.jetty.junit4.AbstractJettyJunit4Test`**:
  - Jetty 9: `com.github.mjeanroy.junit.servers.jetty9.junit4.AbstractJettyJunit4Test`
  - Jetty 10: `com.github.mjeanroy.junit.servers.jetty10.junit4.AbstractJettyJunit4Test`
  - Jetty 11: `com.github.mjeanroy.junit.servers.jetty11.junit4.AbstractJettyJunit4Test`

**`com.github.mjeanroy.junit.servers.jetty.junit4.JettyServerJunit4Rule`**:
  - Jetty 9: `com.github.mjeanroy.junit.servers.jetty9.junit4.JettyServerJunit4Rule`
  - Jetty 10: `com.github.mjeanroy.junit.servers.jetty10.junit4.JettyServerJunit4Rule`
  - Jetty 11: `com.github.mjeanroy.junit.servers.jetty11.junit4.JettyServerJunit4Rule`

**`com.github.mjeanroy.junit.servers.jetty.junit4.JettyServerJunit4Runner`**:
  - Jetty 9: `com.github.mjeanroy.junit.servers.jetty9.junit4.JettyServerJunit4Runner`
  - Jetty 10: `com.github.mjeanroy.junit.servers.jetty10.junit4.JettyServerJunit4Runner`
  - Jetty 11: `com.github.mjeanroy.junit.servers.jetty11.junit4.JettyServerJunit4Runner`


**`com.github.mjeanroy.junit.servers.jetty.jupiter.AbstractJettyJunit4Test`**:
  - Jetty 9: `com.github.mjeanroy.junit.servers.jetty9.jupiter.JettyServerExtension`
  - Jetty 10: `com.github.mjeanroy.junit.servers.jetty10.jupiter.JettyServerExtension`
  - Jetty 11: `com.github.mjeanroy.junit.servers.jetty11.jupiter.JettyServerExtension`

**`com.github.mjeanroy.junit.servers.jetty.jupiter.JettyTest`**:
  - Jetty 9: `com.github.mjeanroy.junit.servers.jetty9.jupiter.JettyTest`
  - Jetty 10: `com.github.mjeanroy.junit.servers.jetty10.jupiter.JettyTest`
  - Jetty 11: `com.github.mjeanroy.junit.servers.jetty11.jupiter.JettyTest`