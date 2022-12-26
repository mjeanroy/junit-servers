##### Installation

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.mjeanroy/junit-servers/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.mjeanroy/junit-servers)

First of all, you need to add JUnit-Servers dependency which is available on [maven repositories](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22junit-servers-jetty%22).

Starting with version **3.0.0**, you can now choose the major Tomcat version: just import the right dependency:

- Jetty 9: `junit-servers-jetty-9`
- Jetty 10: `junit-servers-jetty-10`
- Jetty 11: `junit-servers-jetty-11`

The dependency `junit-servers-jetty` still exists, mainly for backward compatibility, and will provide Jetty 9 under the hood.

If you are using Junit-Servers **< 3.0.0**, then just import `just-servers-jetty` (use Jetty 9 under the hood).

**Maven**

```xml
<dependency>
  <groupId>com.github.mjeanroy</groupId>
  <artifactId>junit-servers-jetty-9</artifactId>
  <!-- <artifactId>junit-servers-jetty-10</artifactId> -->
  <!-- <artifactId>junit-servers-jetty-11</artifactId> -->
  <!-- <artifactId>junit-servers-jetty</artifactId> -->
  <version>[LATEST VERSION]</version>
  <scope>test</scope>
</dependency>
```

**Gradle**

```groovy
testCompile 'com.github.mjeanroy:junit-servers-jetty-9:[LATEST VERSION]'
// testCompile 'com.github.mjeanroy:junit-servers-jetty-10:[LATEST VERSION]'
// testCompile 'com.github.mjeanroy:junit-servers-jetty-11:[LATEST VERSION]'
// testCompile 'com.github.mjeanroy:junit-servers-jetty:[LATEST VERSION]'
```

The Javadoc is available on [javadoc.io](https://javadoc.io/): see [here](https://javadoc.io/doc/com.github.mjeanroy/junit-servers-jetty) and [here](https://javadoc.io/doc/com.github.mjeanroy/junit-servers-core) for the core library.

Once installed, let's see how to use it with:
- [JUnit Jupiter]({{ '/jetty/jupiter' | prepend: site.baseurl }})
- [JUnit 4]({{ '/jetty/junit4' | prepend: site.baseurl }})

