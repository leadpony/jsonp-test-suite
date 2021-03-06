# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## 2.1.0 - 2021-01-23
### Added
* Tests for JSON patch move operation on nonexistent value.
* Test case for failure of character encoding detection.

### Changed
* Add operation on empty JSON pointer to structure type now accepts both array and object. This change is due to the requirement in the TCK. 

## 2.0.0 - 2020-10-29
### Changed
* Update the version of Jakarta JSON Processing API to 2.0.0.

## 2.0.0-RC2 - 2020-04-10
### Changed
* Update the version of Jakarta JSON Processing API to 2.0.0-RC2.

## 2.0.0-RC1 - 2020-02-02
### Changed
* The API namespace was changed from `javax.json` to `jakarta.json`.
* Update the version of Jakarta JSON Processing API to 2.0.0-RC1.

## 1.5.0 - 2019-11-23
### Added
* More tests for `JsonBuilderFactory`.

## 1.4.0 - 2019-09-29
### Added
* Add the automatic module name in the manifest file.

## Fixed
* Now allows both upper and lower cases in the hexadecimal letters of unicode escaping. (Issue #1 reported by @toddobryan)

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
