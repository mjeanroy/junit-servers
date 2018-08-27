##### 0.11.0

- Add automatic module name (for Java 9 module), following module names are used:
  - For `junit-server-core`: `com.github.mjeanroy.junit.servers.core`
  - For `junit-server-jetty`: `com.github.mjeanroy.junit.servers.jetty`
  - For `junit-server-tomcat`: `com.github.mjeanroy.junit.servers.tomcat`
- Fix a bug when JUnit Jupiter extension was used with `RegisterExtension` but was not declared as `static`

##### 0.10.0

- Add JUnit Jupiter integration through the `JunitServerExtension`.

##### 0.9.0

- In order to prepare for the Junit-Jupiter extension, API related to Junit4 have been moved
  to a dedicated package, old classes have been deprecated. Note that you can continue to use it,
  but it will be removed in a next release. Note that documentation is up to date, and Javadoc should
  explain what to use instead of the deprecated class.

The following changes should be applied:
  - Replace `com.github.mjeanroy.junit.servers.runner.JunitServerRunner` with `com.github.mjeanroy.junit.servers.junit4.JunitServerRunner`.

  - Replace `com.github.mjeanroy.junit.servers.jetty.rules.JettyServerRule` with `com.github.mjeanroy.junit.servers.jetty.junit4.JettyServerRule`.
  - Replace `com.github.mjeanroy.junit.servers.jetty.JettyServerJunit4Runner` with `com.github.mjeanroy.junit.servers.jetty.junit4.JettyServerJunit4Runner`.
  - Replace `com.github.mjeanroy.junit.servers.jetty.utils.AbstractJettyTest` with `com.github.mjeanroy.junit.servers.jetty.junit4.AbstractJettyTest`.

  - Replace `com.github.mjeanroy.junit.servers.tomcat.rules.TomcatServerRule` with `com.github.mjeanroy.junit.servers.tomcat.junit4.TomcatServerRule`.
  - Replace `com.github.mjeanroy.junit.servers.tomcat.JettyServerJunit4Runner` with `com.github.mjeanroy.junit.servers.tomcat.junit4.JettyServerJunit4Runner`.
  - Replace `com.github.mjeanroy.junit.servers.tomcat.utils.AbstractTomcatTest` with `com.github.mjeanroy.junit.servers.tomcat.junit4.AbstractTomcatTest`.

##### 0.8.0

- The server implementation detection now use the Service Provider API available in the JDK and it should now be
  easier for anyone to implement and use custom implementation (instead of Jetty/Tomcat).
- Introduce `com.github.mjeanroy.junit.servers.jetty.JettyServerJunit4Runner` to be able to force
  jetty without using the service provider API.
- Introduce `com.github.mjeanroy.junit.servers.tomcat.TomcatServerJunit4Runner` to be able to force
  jetty without using the service provider API.
- Some internal refactoring in order to prepare the future junit-jupiter extension.

##### 0.7.0

- Add compatibility for Java 9: some methods have been deprecated as supporting it with Java 9 was
  impossible (due to changes on classloader API).
- Various dependency updates, mainly used in unit tests.

##### 0.6.1

###### Core

- In version 0.6.0, visibility of method `EmbeddedTomcat#createContext` went from `protected` to `private`. This was a breaking change and it has been reverted (see [issues #7](https://github.com/mjeanroy/junit-servers/issues/7)). The method has been deprecated since visibility may be changed to private again (as it should have been at the beginning). If this is a problem, please [submit an issue](https://github.com/mjeanroy/junit-servers/issues) to discuss it.

- Method `AbstractConfigurationBuilder#withParentClasspath(Class<?> baseClass, FileFilter filter)` (introduced in 0.5.0) has been deprecated. With JDK9, the application classloader does not inherit from `URLClassLoader`, so it become (almost) impossible to derive a new `URLClassLoader` from it by filtering some classpath entries. This method has been deprecated as it will become useless once JDK9 compatibility will be implemented. If this is a problem, please [submit an issue](https://github.com/mjeanroy/junit-servers/issues) to discuss it.

##### 0.6.0

###### Features

- Servers
  - [#ba89a9a](https://github.com/mjeanroy/junit-servers/commit/ba89a9a2ad94c9c5426485c656a25a1dfdc1ebfd) allow preserving tomcat base directory on server stop (thanks [@grzegorzt](https://github.com/grzegorzt)!).

- Http Client
  - [#b14749f](https://github.com/mjeanroy/junit-servers/commit/b14749f5fb4e7f8fa1d90b423b7887bc29d518b7) Support new [AsyncHttpClient](https://github.com/AsyncHttpClient/async-http-client) version.
  - [#5de640b](https://github.com/mjeanroy/junit-servers/commit/5de640b979aabeaea1950272e98fa39b5b7aebc1) Support [OkHttp](http://square.github.io/okhttp/).
  - [#f5afdd7](https://github.com/mjeanroy/junit-servers/commit/f5afdd7e7efc23f129dbf0533fb87f859b26fdc3) Support HTTP `PATCH` method.
  - [#13d68d3](https://github.com/mjeanroy/junit-servers/commit/13d68d3908d245dc9377f755b3665e5fcbe048ec) Support HTTP `HEAD` method.
  - [#7021d49](https://github.com/mjeanroy/junit-servers/commit/7021d49a7ed65d6bfa9a4ca90b0d5ba8a0c3c6ab) Rules can create HTTP client.

###### Fix

- Servers
  - [#114c17c](https://github.com/mjeanroy/junit-servers/commit/114c17ce8121d0e8ded39c95c1db33289a7e0708) Rules now inherits from JUnit `ExternalResource`.

- Http Client
  - [#d09f732](https://github.com/mjeanroy/junit-servers/commit/d09f732f86679f4103ca98904ddc3c4c1ba52995) Ensure HTTP parameters can have empty values.
  - [#e58e4c3](https://github.com/mjeanroy/junit-servers/commit/e58e4c3c4d3f768a89ccab11dc5d97edbbf2cd50) Ensure form parameters are URL encoded.
  - [#7b57536](https://github.com/mjeanroy/junit-servers/commit/7b57536937d27fa1ad18dfaa465e7016bc4aa900) Ensure URL path is URL encoded.
  - [#08f75e9](https://github.com/mjeanroy/junit-servers/commit/08f75e9e174bca0b0d52ae461dc3d37b3eac2193) Ensure URL query parameters are URL encoded.

###### Core

- Ensure JDK7 compatibility with `animal-sniffer-maven-plugin`.
- Various maven plugin updates.
- Improve Javadocs.

##### 0.5.0

###### Features

- Add new options to the server configuration builder (see [#6a9015e](https://github.com/mjeanroy/junit-servers/commit/6a9015ef17d3304e1f3a1a10e70a55670650ff46)).
  - Add `withOverrideDescriptor` to allow overriding the path to the `web.xml` file.
  - Add `withParentClasspath` to allow override parent classpath of the deployed webapp (may be useful to add JAR from a directory).

###### Fix
- Add missing default configuration for embedded jetty (see [#8ec08d9](https://github.com/mjeanroy/junit-servers/commit/8ec08d910f99624f5b6c953c1898706e6ce337d7)).
  - Add `WebInfConfiguration`.
  - Add `MetaInfConfiguration`.

###### Core

- Various dependency updates:
  - Embedded tomcat: 8.0.17 to 8.0.44
  - Test dependencies (commons-lang3, spring).
  - Dev dependencies (apache http-client, ning async-http-client).
- Maven plugin updates.
- Add continuous integration on travis.
