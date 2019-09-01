# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## 1.3.0 - 2019-09-01
### Added
- Tests for `JsonParser.skipArray()` and `JsonParser.skipObject()` in the middle of array/object.
- Tests for building JSON with collection/map.

## 1.2.0 - 2019-08-04
### Added
- JSON examples with various kinds of indentations.

## 1.1.0 - 2019-07-28
### Added
- Tests for `getConfigInUse()` method.
- Tests for `JsonParser.skipArray()` and `JsonParser.skipObject()` against JSON array and object not properly closed.

## 1.0.0 - 2019-07-23
### Added
- Reporting for Joy.

## 0.13.0 - 2019-07-22
### Added
- Support of Java 8.

## 0.12.0 - 2019-07-21
### Added
- Tests of next event after calling `getValue()`, `getArray()`, and `getObject()`.
- Tests of reading JSON followed by garbages.
- Add tests for `read()`, `readArray()`, and `readObject()` of `JsonReader`.

## 0.11.0 - 2019-07-20
### Added
- More tests for `JsonParser.hasNext()`.

## 0.10.0 - 2019-07-19
### Added
- Tests for `JsonGenerator.write()` which takes a double parameter.
- Tests for malformed JSON patch.

## 0.9.1 - 2019-07-15
### Added
- Initial release to JitPack.
