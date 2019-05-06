# JSON-P Test Suite & Benchmark

This project provides a set of tests and benchmarks for implementations of [Java API for JSON Processing (JSR 374)].

## Requirements

The following tools are required to build and run the artifacts.
* JDK 9 or higher
* [Apache Maven] 3.6.0 or higher

## Test Suite

### How to Build

The commands below build and install the test suite into your local Maven repository.

```
$ cd jsonp-test-suite
$ mvn clean install
```
## Benchmark

### How to Build

```
$ cd jsonp-benchmark
$ mvn clean package
```

Or if you try [Apache Johnzon] as the service provider:
```
$ mvn clean package -P with-johnzon
```

### How to Run

```
$ java -jar target/benchmarks.jar
```

## Copyright Notice
Copyright &copy; 2019 JSON-P Test Suite Authors. This software is licensed under [Apache License, Versions 2.0][Apache 2.0 License].

[Apache 2.0 License]: https://www.apache.org/licenses/LICENSE-2.0
[Java API for JSON Processing (JSR 374)]: https://eclipse-ee4j.github.io/jsonp/
[Apache Maven]: https://maven.apache.org/
[Apache Johnzon]: https://johnzon.apache.org/
