##### Installation

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.mjeanroy/junit-servers/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.mjeanroy/junit-servers)

First of all, you need to add JUnit-Servers dependency which is available on [maven repositories](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22junit-servers-tomcat%22).

Starting with version **3.0.0**, you can now choose the major Tomcat version: just import the right dependency:

- Tomcat 8: `junit-servers-tomcat-8`
- Tomcat 9: `junit-servers-tomcat-9`
- Tomcat 10: `junit-servers-tomcat-10`

The dependency `junit-servers-tomcat` still exists, mainly for backward compatibility, and will provide Tomcat 8.

If you are using Junit-Servers **< 3.0.0**, then just import `just-servers-tomcat` (use Tomcat 8 under the hood).

**Maven**

```xml
<dependency>
  <groupId>com.github.mjeanroy</groupId>
  <artifactId>junit-servers-tomcat-8</artifactId>
  <!-- <artifactId>junit-servers-tomcat-9</artifactId> -->
  <!-- <artifactId>junit-servers-tomcat-10</artifactId> -->
  <!-- <artifactId>junit-servers-tomcat</artifactId> -->
  <version>[LATEST VERSION]</version>
  <scope>test</scope>
</dependency>
```

**Gradle**

```groovy
testCompile 'com.github.mjeanroy:junit-servers-tomcat-8:[LATEST VERSION]'
// testCompile 'com.github.mjeanroy:junit-servers-tomcat-9:[LATEST VERSION]'
// testCompile 'com.github.mjeanroy:junit-servers-tomcat-10:[LATEST VERSION]'
// testCompile 'com.github.mjeanroy:junit-servers-tomcat:[LATEST VERSION]'
```

The Javadoc is available on [javadoc.io](https://javadoc.io/): see [here](https://javadoc.io/doc/com.github.mjeanroy/junit-servers-tomcat) and [here](https://javadoc.io/doc/com.github.mjeanroy/junit-servers-core) for the core library.

Once installed, let's see how to use it:
- [JUnit Jupiter]({{ '/tomcat/jupiter' | prepend: site.baseurl }}).
- [JUnit 4]({{ '/tomcat/junit4' | prepend: site.baseurl }}).
