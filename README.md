# JSON-P Test Suite
[![Build Status](https://travis-ci.org/leadpony/jsonp-test-suite.svg?branch=master)](https://travis-ci.org/leadpony/jsonp-test-suite)
[![Release](https://jitpack.io/v/org.leadpony/jsonp-test-suite.svg)](https://jitpack.io/#org.leadpony/jsonp-test-suite)

This project provides a set of tests for implementations of [Jakarta JSON Processing API] (JSON-P).

## Project Goals

* Provides comprehensive test suite which is fairly available for all JSON-P implementations.
* Contributes to the quality of JSON-P implementations.
* Compensates the ambiguities existing in the API specification.

## Latest Test Results

The following JSON-P implementations are tested by this test suite.
* [Jakarta JSON Processing] (Reference Implementation)
* [Joy]

The latest test results are presented in [JSON-P Test Suite Reports].

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
        <version>2.1.0</version>
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
            <excludedGroups>ambiguous</excludedGroups>
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

When testing with *Joy*:

```bash
mvn test -P test-with-joy
```

## Copyright Notice
Copyright 2019-2021 the original author or authors. All rights reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this product except in compliance with the License.
You may obtain a copy of the License at
http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

[Apache 2.0 License]: https://www.apache.org/licenses/LICENSE-2.0
[Jakarta JSON Processing API]: https://eclipse-ee4j.github.io/jsonp/
[Jakarta JSON Processing]: https://eclipse-ee4j.github.io/jsonp/
[Joy]: https://github.com/leadpony/joy
[Apache Maven]: https://maven.apache.org/
[JSON-P Test Suite Reports]: https://leadpony.github.io/jsonp-test-suite/project-reports.html
[JitPack]: https://jitpack.io/#org.leadpony/jsonp-test-suite
