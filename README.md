# JSON-P Test Suite
[![Build Status](https://travis-ci.org/leadpony/jsonp-test-suite.svg?branch=master)](https://travis-ci.org/leadpony/jsonp-test-suite)
[![Release](https://jitpack.io/v/org.leadpony/jsonp-test-suite.svg)](https://jitpack.io/#org.leadpony/jsonp-test-suite)

This project provides a set of tests for implementations of [Java API for JSON Processing (JSR 374)].

## Project Goal

* Provides comprehensive test suite which is fairly available for all JSON-P implementations.
* Contributes to the quality of JSON-P implementations.
* Compensates the ambiguities existing in the API specification.

## Latest Test Results

[JSON-P Test Suite Reports] shows the latest test results of the following JSON-P implementations.
* [Jakarta JSON Processing] (Reference Implementation)
* [Apache Johnzon]
* [Joy]

## Using as a Test Dependency

The jar-packaged artifact of this test suite is available from [JitPack] repository.

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>org.leadpony</groupId>
        <artifactId>jsonp-test-suite</artifactId>
        <version>1.1.0</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```
The test suite can be run by using Maven Surefire Plugin.

```xml
<plguins>
    <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-surefire-plugin</artifactId>
        <configuration>
            <dependenciesToScan>
                <dependency>org.leadpony:jsonp-test-suite</dependency>
            </dependenciesToScan>
            <excludes>
                <exclude />
            </excludes>
        </configuration>
    </plugin>
</plguins>
```

## Building from Source

The following tools are required to build and run the artifacts.
* JDK 9 or higher
* [Apache Maven] 3.6.0 or higher

The commands below build and install the test suite into your local Maven repository.

```bash
mvn clean install
```

When testing with *Jakarta JSON Processing*:

```bash
mvn test -P test-with-jakarta
```

When testing with *Apache Johnzon*:

```bash
mvn test -P test-with-johnzon
```

When testing with *Joy*:

```bash
mvn test -P test-with-joy
```

## Copyright Notice
Copyright &copy; 2019 JSON-P Test Suite Authors. This software is licensed under [Apache License, Versions 2.0][Apache 2.0 License].

[Apache 2.0 License]: https://www.apache.org/licenses/LICENSE-2.0
[Java API for JSON Processing (JSR 374)]: https://eclipse-ee4j.github.io/jsonp/
[Jakarta JSON Processing]: https://eclipse-ee4j.github.io/jsonp/
[Apache Johnzon]: https://johnzon.apache.org/
[Joy]: https://github.com/leadpony/joy
[Apache Maven]: https://maven.apache.org/
[JSON-P Test Suite Reports]: https://leadpony.github.io/jsonp-test-suite/project-reports.html
[JitPack]: https://jitpack.io/#org.leadpony/jsonp-test-suite
