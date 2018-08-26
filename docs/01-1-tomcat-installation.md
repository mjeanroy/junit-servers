##### Installation

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.mjeanroy/junit-servers/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.mjeanroy/junit-servers)

First of all, you need to add JUnit-Servers dependency which is available on [maven repositories](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22junit-servers-tomcat%22):

**Maven**

```xml
<dependency>
  <groupId>com.github.mjeanroy</groupId>
  <artifactId>junit-servers-tomcat</artifactId>
  <version>[LATEST VERSION]</version>
  <scope>test</scope>
</dependency>
```

**Gradle**

```groovy
testCompile 'com.github.mjeanroy:junit-servers-tomcat:[LATEST VERSION]'
```

The Javadoc is available on [javadoc.io](https://javadoc.io/): see [here](https://javadoc.io/doc/com.github.mjeanroy/junit-servers-tomcat) and [here](https://javadoc.io/doc/com.github.mjeanroy/junit-servers-core) for the core library.

Once installed, let's see how to use it:
- [JUnit Jupiter]({{ '/tomcat/jupiter' | prepend: site.baseurl }}).
- [JUnit 4]({{ '/tomcat/junit4' | prepend: site.baseurl }}).
