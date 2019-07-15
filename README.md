# JSON-P Test Suite
[![Build Status](https://travis-ci.org/leadpony/jsonp-test-suite.svg?branch=master)](https://travis-ci.org/leadpony/jsonp-test-suite)
[![Release](https://jitpack.io/v/org.leadpony/jsonp-test-suite.svg)](https://jitpack.io/#org.leadpony/jsonp-test-suite)

This project provides a set of tests and benchmarks for implementations of [Java API for JSON Processing (JSR 374)].

## JSON-P Test Suite

### Test Results

The latest test results of Reference implementation and Apache Johnzon are available in [JSON-P Test Suite Reports].

### How to Build

The following tools are required to build and run the artifacts.
* JDK 9 or higher
* [Apache Maven] 3.6.0 or higher

The commands below build and install the test suite into your local Maven repository.

```bash
mvn clean install
```

When testing with the Reference Implementation:

```bash
mvn test -P test-with-ri
```

When testing with [Apache Johnzon]:

```bash
mvn test -P test-with-johnzon
```

## Copyright Notice
Copyright &copy; 2019 JSON-P Test Suite Authors. This software is licensed under [Apache License, Versions 2.0][Apache 2.0 License].

[Apache 2.0 License]: https://www.apache.org/licenses/LICENSE-2.0
[Java API for JSON Processing (JSR 374)]: https://eclipse-ee4j.github.io/jsonp/
[Apache Maven]: https://maven.apache.org/
[Apache Johnzon]: https://johnzon.apache.org/
[JSON-P Test Suite Reports]: https://leadpony.github.io/jsonp-test-suite/test-suite/project-reports.html
