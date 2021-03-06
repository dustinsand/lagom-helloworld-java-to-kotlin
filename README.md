[![Build Status](https://travis-ci.org/dustinsand/lagom-helloworld-java-to-kotlin.svg?branch=master)](https://travis-ci.org/dustinsand/lagom-helloworld-java-to-kotlin)

# lagom-helloworld-java-to-kotlin
Proof of technology (POT) to evaluate Kotlin as the implementation language for Lagom microservices. [Hello Lagom project (1.4.9) from Get Started with Lightbend Technologies](https://developer.lightbend.com/start/?group=lagom&project=lagom-java-maven) was ported from Java to Kotlin (1.3.11) with 1 to 1 matching functionality.

# Goal
Show Kotlin can be used for Lagom microservices to gain the productivity and language advancements compared to Java. Why not Scala?  Scala would have been the logical choice and a good one, but I've found less resistance from team members to learn Kotlin compared to Scala.   


# Key Changes
* Maven POMs to include Kotlin dependencies
* Kotlin equivalent classes in src/kotlin and equivalent Java classes removed from src/java

# What did not port?

1. Lagom service interfaces ported to Kotlin caused this runtime error:

Caused by: java.lang.IllegalArgumentException: Service.descriptor must be implemented as a default method

Workaround explained here https://github.com/lagom/lagom/issues/1180.
   

# Run

```bash
mvn lagom:runAll
```

1) Get the hello resource for World
```bash
curl  http://localhost:9000/api/hello/World
```
Default response will be
```
Hello, World!
```

2) Change the hello resource
```bash
curl -H "Content-Type: application/json" -X POST -d '{"message": "Hi"}' http://localhost:9000/api/hello/World
```
Response will be
```
Hi, World!
```

3) Run the integration test with lagom running, integration-tests/src/test/kotlin/.../StreamIT, to assert streaming works.


# Summary

Lightbend's 'Hello Lagom' Java/Maven project was successfully ported to Kotlin. Unit and integration tests pass and the services are runnable.  Hope this gives confidence for other developers to try Kotlin with Lagom.
