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
