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
