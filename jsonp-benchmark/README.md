# JSON-P Benchmark

## Requirements

The following tools are required to build and run the artifacts.
* JDK 9 or higher
* [Apache Maven] 3.6.0 or higher

## How to Build

```bash
mvn clean package
```

When using [Apache Johnzon] as the service provider:
```bash
mvn clean package -P with-johnzon
```

## How to Run

```bash
java -jar target/benchmarks.jar
```

[Apache Maven]: https://maven.apache.org/
[Apache Johnzon]: https://johnzon.apache.org/
