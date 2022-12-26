##### Upgrade to version 3.0.0

###### Dependency Upgrade

Starting with version 3.0.0, it's now possible to choose the major version used under the hood by importing the right package:
- Tomcat 8: `junit-servers-tomcat-8`
- Tomcat 9: `junit-servers-tomcat-9`
- Tomcat 10: `junit-servers-tomcat-10`

For backward compatibility reason, the module `junit-servers-tomcat` still exists, but is now deprecated. To upgrade to version 3.0.0, just use `junit-servers-tomcat-8` instead (`junit-servers-tomcat` uses Tomcat 8 under the hood).

###### Package Upgrade

Some packages have been changed, and you should now replace:

**`com.github.mjeanroy.junit.servers.tomcat.EmbeddedTomcat`**:
  - Tomcat 8: `com.github.mjeanroy.junit.servers.tomcat8.EmbeddedTomcat`
  - Tomcat 9: `com.github.mjeanroy.junit.servers.tomcat9.EmbeddedTomcat`
  - Tomcat 10: `com.github.mjeanroy.junit.servers.tomcat10.EmbeddedTomcat`

**`com.github.mjeanroy.junit.servers.tomcat.EmbeddedTomcatFactory`**:
  - Tomcat 8: `com.github.mjeanroy.junit.servers.tomcat8.EmbeddedTomcatFactory`
  - Tomcat 9: `com.github.mjeanroy.junit.servers.tomcat9.EmbeddedTomcatFactory`
  - Tomcat 10: `com.github.mjeanroy.junit.servers.tomcat10.EmbeddedTomcatFactory`

**`com.github.mjeanroy.junit.servers.tomcat.EmbeddedTomcatProvider`**:
  - Tomcat 8: `com.github.mjeanroy.junit.servers.tomcat8.EmbeddedTomcatProvider`
  - Tomcat 9: `com.github.mjeanroy.junit.servers.tomcat9.EmbeddedTomcatProvider`
  - Tomcat 10: `com.github.mjeanroy.junit.servers.tomcat10.EmbeddedTomcatProvider`


**`com.github.mjeanroy.junit.servers.tomcat.junit4.AbstractTomcatJunit4Test`**:
  - Tomcat 8: `com.github.mjeanroy.junit.servers.tomcat8.junit4.AbstractTomcatJunit4Test`
  - Tomcat 9: `com.github.mjeanroy.junit.servers.tomcat9.junit4.AbstractTomcatJunit4Test`
  - Tomcat 10: `com.github.mjeanroy.junit.servers.tomcat10.junit4.AbstractTomcatJunit4Test`

**`com.github.mjeanroy.junit.servers.tomcat.junit4.TomcatServerJunit4Rule`**:
  - Tomcat 8: `com.github.mjeanroy.junit.servers.tomcat8.junit4.TomcatServerJunit4Rule`
  - Tomcat 9: `com.github.mjeanroy.junit.servers.tomcat9.junit4.TomcatServerJunit4Rule`
  - Tomcat 10: `com.github.mjeanroy.junit.servers.tomcat10.junit4.TomcatServerJunit4Rule`

**`com.github.mjeanroy.junit.servers.tomcat.junit4.TomcatServerJunit4Runner`**:
  - Tomcat 8: `com.github.mjeanroy.junit.servers.tomcat8.junit4.TomcatServerJunit4Runner`
  - Tomcat 9: `com.github.mjeanroy.junit.servers.tomcat9.junit4.TomcatServerJunit4Runner`
  - Tomcat 10: `com.github.mjeanroy.junit.servers.tomcat10.junit4.TomcatServerJunit4Runner`


**`com.github.mjeanroy.junit.servers.tomcat.jupiter.AbstractTomcatJunit4Test`**:
  - Tomcat 8: `com.github.mjeanroy.junit.servers.tomcat8.jupiter.TomcatServerExtension`
  - Tomcat 9: `com.github.mjeanroy.junit.servers.tomcat9.jupiter.TomcatServerExtension`
  - Tomcat 10: `com.github.mjeanroy.junit.servers.tomcat10.jupiter.TomcatServerExtension`

**`com.github.mjeanroy.junit.servers.tomcat.jupiter.TomcatTest`**:
  - Tomcat 8: `com.github.mjeanroy.junit.servers.tomcat8.jupiter.TomcatTest`
  - Tomcat 9: `com.github.mjeanroy.junit.servers.tomcat9.jupiter.TomcatTest`
  - Tomcat 10: `com.github.mjeanroy.junit.servers.tomcat10.jupiter.TomcatTest`