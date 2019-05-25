# JSON-P Test Suite

## Requirements

The following tools are required to build and run the artifacts.
* JDK 9 or higher
* [Apache Maven] 3.6.0 or higher

## How to Build

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

[Apache Maven]: https://maven.apache.org/
[Apache Johnzon]: https://johnzon.apache.org/
