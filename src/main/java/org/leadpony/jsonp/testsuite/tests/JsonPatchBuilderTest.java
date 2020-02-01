/*
 * Copyright 2019-2020 the JSON-P Test Suite Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.leadpony.jsonp.testsuite.tests;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonPatchBuilder;
import jakarta.json.JsonValue;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * @author leadpony
 */
public class JsonPatchBuilderTest {

    public static Stream<Arguments> addShouldAppendAddOperationWithJsonValue() {
        return Stream.of(
            Arguments.of("/a~1b",
                JsonValue.EMPTY_JSON_ARRAY,
                "[{\"op\":\"add\",\"path\":\"/a~1b\",\"value\":[]}]"),
            Arguments.of("/c%d",
                JsonValue.EMPTY_JSON_OBJECT,
                "[{\"op\":\"add\",\"path\":\"/c%d\",\"value\":{}}]"),
            Arguments.of("/e^f",
                JsonValue.TRUE,
                "[{\"op\":\"add\",\"path\":\"/e^f\",\"value\":true}]"),
            Arguments.of("/g|h",
                JsonValue.FALSE,
                "[{\"op\":\"add\",\"path\":\"/g|h\",\"value\":false}]"),
            Arguments.of("/i\\j",
                JsonValue.NULL,
                "[{\"op\":\"add\",\"path\":\"/i\\\\j\",\"value\":null}]"),
            Arguments.of("/k\"l",
                Json.createValue("hello"),
                "[{\"op\":\"add\",\"path\":\"/k\\\"l\",\"value\":\"hello\"}]"),
            Arguments.of("/m~0n",
                Json.createValue(42),
                "[{\"op\":\"add\",\"path\":\"/m~0n\",\"value\":42}]")
            );
    }

    @ParameterizedTest
    @MethodSource
    public void addShouldAppendAddOperationWithJsonValue(String path, JsonValue value, String expected) {
        JsonPatchBuilder sut = Json.createPatchBuilder()
            .add(path, value);
        JsonArray actual = sut.build().toJsonArray();
        assertThat(actual).asString().isEqualTo(expected);
    }

    public static Stream<Arguments> addShouldAppendAddOperationWithString() {
        return Stream.of(
            Arguments.of("/a",
                "hello",
                "[{\"op\":\"add\",\"path\":\"/a\",\"value\":\"hello\"}]")
            );
    }

    @ParameterizedTest
    @MethodSource
    public void addShouldAppendAddOperationWithString(String path, String value, String expected) {
        JsonPatchBuilder sut = Json.createPatchBuilder()
            .add(path, value);
        JsonArray actual = sut.build().toJsonArray();
        assertThat(actual).asString().isEqualTo(expected);
    }

    public static Stream<Arguments> addShouldAppendAddOperationWithInt() {
        return Stream.of(
            Arguments.of("/a",
                42,
                "[{\"op\":\"add\",\"path\":\"/a\",\"value\":42}]")
            );
    }

    @ParameterizedTest
    @MethodSource
    public void addShouldAppendAddOperationWithInt(String path, int value, String expected) {
        JsonPatchBuilder sut = Json.createPatchBuilder()
            .add(path, value);
        JsonArray actual = sut.build().toJsonArray();
        assertThat(actual).asString().isEqualTo(expected);
    }

    public static Stream<Arguments> addShouldAppendAddOperationWithBoolean() {
        return Stream.of(
            Arguments.of("/a",
                true,
                "[{\"op\":\"add\",\"path\":\"/a\",\"value\":true}]"),
            Arguments.of("/b",
                false,
                "[{\"op\":\"add\",\"path\":\"/b\",\"value\":false}]")
            );
    }

    @ParameterizedTest
    @MethodSource
    public void addShouldAppendAddOperationWithBoolean(String path, boolean value, String expected) {
        JsonPatchBuilder sut = Json.createPatchBuilder()
            .add(path, value);
        JsonArray actual = sut.build().toJsonArray();
        assertThat(actual).asString().isEqualTo(expected);
    }

    public static Stream<Arguments> removeShouldAppendRemoveOperation() {
        return Stream.of(
            Arguments.of("/foo",
                "[{\"op\":\"remove\",\"path\":\"/foo\"}]"),
            Arguments.of("/foo/0",
                "[{\"op\":\"remove\",\"path\":\"/foo/0\"}]"),
            Arguments.of("/",
                "[{\"op\":\"remove\",\"path\":\"/\"}]"),
            Arguments.of("/a~1b",
                "[{\"op\":\"remove\",\"path\":\"/a~1b\"}]"),
            Arguments.of("/c%d",
                "[{\"op\":\"remove\",\"path\":\"/c%d\"}]"),
            Arguments.of("/e^f",
                "[{\"op\":\"remove\",\"path\":\"/e^f\"}]"),
            Arguments.of("/g|h",
                "[{\"op\":\"remove\",\"path\":\"/g|h\"}]"),
            Arguments.of("/i\\j",
                "[{\"op\":\"remove\",\"path\":\"/i\\\\j\"}]"),
            Arguments.of("/k\"l",
                "[{\"op\":\"remove\",\"path\":\"/k\\\"l\"}]"),
            Arguments.of("/ ",
                "[{\"op\":\"remove\",\"path\":\"/ \"}]"),
            Arguments.of("/m~0n",
                "[{\"op\":\"remove\",\"path\":\"/m~0n\"}]")
            );
    }

    @ParameterizedTest
    @MethodSource
    public void removeShouldAppendRemoveOperation(String path, String expected) {
        JsonPatchBuilder sut = Json.createPatchBuilder()
            .remove(path);
        JsonArray actual = sut.build().toJsonArray();
        assertThat(actual).asString().isEqualTo(expected);
    }

    public static Stream<Arguments> replaceShouldAppendReplaceOperationWithJsonValue() {
        return Stream.of(
            Arguments.of("/a~1b",
                JsonValue.EMPTY_JSON_ARRAY,
                "[{\"op\":\"replace\",\"path\":\"/a~1b\",\"value\":[]}]"),
            Arguments.of("/c%d",
                JsonValue.EMPTY_JSON_OBJECT,
                "[{\"op\":\"replace\",\"path\":\"/c%d\",\"value\":{}}]"),
            Arguments.of("/e^f",
                JsonValue.TRUE,
                "[{\"op\":\"replace\",\"path\":\"/e^f\",\"value\":true}]"),
            Arguments.of("/g|h",
                JsonValue.FALSE,
                "[{\"op\":\"replace\",\"path\":\"/g|h\",\"value\":false}]"),
            Arguments.of("/i\\j",
                JsonValue.NULL,
                "[{\"op\":\"replace\",\"path\":\"/i\\\\j\",\"value\":null}]"),
            Arguments.of("/k\"l",
                Json.createValue("hello"),
                "[{\"op\":\"replace\",\"path\":\"/k\\\"l\",\"value\":\"hello\"}]"),
            Arguments.of("/m~0n",
                Json.createValue(42),
                "[{\"op\":\"replace\",\"path\":\"/m~0n\",\"value\":42}]")
            );
    }

    @ParameterizedTest
    @MethodSource
    public void replaceShouldAppendReplaceOperationWithJsonValue(String path, JsonValue value, String expected) {
        JsonPatchBuilder sut = Json.createPatchBuilder()
            .replace(path, value);
        JsonArray actual = sut.build().toJsonArray();
        assertThat(actual).asString().isEqualTo(expected);
    }

    public static Stream<Arguments> replaceShouldAppendRepalceOperationWithString() {
        return Stream.of(
            Arguments.of("/a",
                "hello",
                "[{\"op\":\"replace\",\"path\":\"/a\",\"value\":\"hello\"}]")
            );
    }

    @ParameterizedTest
    @MethodSource
    public void replaceShouldAppendRepalceOperationWithString(String path, String value, String expected) {
        JsonPatchBuilder sut = Json.createPatchBuilder()
            .replace(path, value);
        JsonArray actual = sut.build().toJsonArray();
        assertThat(actual).asString().isEqualTo(expected);
    }

    public static Stream<Arguments> replaceShouldAppendReplaceOperationWithInt() {
        return Stream.of(
            Arguments.of("/a",
                42,
                "[{\"op\":\"replace\",\"path\":\"/a\",\"value\":42}]")
            );
    }

    @ParameterizedTest
    @MethodSource
    public void replaceShouldAppendReplaceOperationWithInt(String path, int value, String expected) {
        JsonPatchBuilder sut = Json.createPatchBuilder()
            .replace(path, value);
        JsonArray actual = sut.build().toJsonArray();
        assertThat(actual).asString().isEqualTo(expected);
    }

    public static Stream<Arguments> replaceShouldAppendReplaceOperationWithBoolean() {
        return Stream.of(
            Arguments.of("/a",
                true,
                "[{\"op\":\"replace\",\"path\":\"/a\",\"value\":true}]"),
            Arguments.of("/b",
                false,
                "[{\"op\":\"replace\",\"path\":\"/b\",\"value\":false}]")
            );
    }

    @ParameterizedTest
    @MethodSource
    public void replaceShouldAppendReplaceOperationWithBoolean(String path, boolean value, String expected) {
        JsonPatchBuilder sut = Json.createPatchBuilder()
            .replace(path, value);
        JsonArray actual = sut.build().toJsonArray();
        assertThat(actual).asString().isEqualTo(expected);
    }

    public static Stream<Arguments> moveShouldAppendMoveOperation() {
        return Stream.of(
            Arguments.of("/a/b/c",
                "/x/y/z",
                "[{\"op\":\"move\",\"path\":\"/a/b/c\",\"from\":\"/x/y/z\"}]")
            );
    }

    @ParameterizedTest
    @MethodSource
    public void moveShouldAppendMoveOperation(String path, String from, String expected) {
        JsonPatchBuilder sut = Json.createPatchBuilder()
            .move(path, from);
        JsonArray actual = sut.build().toJsonArray();
        assertThat(actual).asString().isEqualTo(expected);
    }

    public static Stream<Arguments> copyShouldAppendCopyOperation() {
        return Stream.of(
            Arguments.of("/a/b/c",
                "/x/y/z",
                "[{\"op\":\"copy\",\"path\":\"/a/b/c\",\"from\":\"/x/y/z\"}]")
            );
    }

    @ParameterizedTest
    @MethodSource
    public void copyShouldAppendCopyOperation(String path, String from, String expected) {
        JsonPatchBuilder sut = Json.createPatchBuilder()
            .copy(path, from);
        JsonArray actual = sut.build().toJsonArray();
        assertThat(actual).asString().isEqualTo(expected);
    }

    public static Stream<Arguments> testShouldAppendTestOperationWithJsonValue() {
        return Stream.of(
            Arguments.of("/a~1b",
                JsonValue.EMPTY_JSON_ARRAY,
                "[{\"op\":\"test\",\"path\":\"/a~1b\",\"value\":[]}]"),
            Arguments.of("/c%d",
                JsonValue.EMPTY_JSON_OBJECT,
                "[{\"op\":\"test\",\"path\":\"/c%d\",\"value\":{}}]"),
            Arguments.of("/e^f",
                JsonValue.TRUE,
                "[{\"op\":\"test\",\"path\":\"/e^f\",\"value\":true}]"),
            Arguments.of("/g|h",
                JsonValue.FALSE,
                "[{\"op\":\"test\",\"path\":\"/g|h\",\"value\":false}]"),
            Arguments.of("/i\\j",
                JsonValue.NULL,
                "[{\"op\":\"test\",\"path\":\"/i\\\\j\",\"value\":null}]"),
            Arguments.of("/k\"l",
                Json.createValue("hello"),
                "[{\"op\":\"test\",\"path\":\"/k\\\"l\",\"value\":\"hello\"}]"),
            Arguments.of("/m~0n",
                Json.createValue(42),
                "[{\"op\":\"test\",\"path\":\"/m~0n\",\"value\":42}]")
            );
    }

    @ParameterizedTest
    @MethodSource
    public void testShouldAppendTestOperationWithJsonValue(String path, JsonValue value, String expected) {
        JsonPatchBuilder sut = Json.createPatchBuilder()
            .test(path, value);
        JsonArray actual = sut.build().toJsonArray();
        assertThat(actual).asString().isEqualTo(expected);
    }

    public static Stream<Arguments> testShouldAppendTestOperationWithString() {
        return Stream.of(
            Arguments.of("/a",
                "hello",
                "[{\"op\":\"test\",\"path\":\"/a\",\"value\":\"hello\"}]")
            );
    }

    @ParameterizedTest
    @MethodSource
    public void testShouldAppendTestOperationWithString(String path, String value, String expected) {
        JsonPatchBuilder sut = Json.createPatchBuilder()
            .test(path, value);
        JsonArray actual = sut.build().toJsonArray();
        assertThat(actual).asString().isEqualTo(expected);
    }

    public static Stream<Arguments> testShouldAppendTestOperationWithInt() {
        return Stream.of(
            Arguments.of("/a",
                42,
                "[{\"op\":\"test\",\"path\":\"/a\",\"value\":42}]")
            );
    }

    @ParameterizedTest
    @MethodSource
    public void testShouldAppendTestOperationWithInt(String path, int value, String expected) {
        JsonPatchBuilder sut = Json.createPatchBuilder()
            .test(path, value);
        JsonArray actual = sut.build().toJsonArray();
        assertThat(actual).asString().isEqualTo(expected);
    }

    public static Stream<Arguments> testShouldAppendTestOperationWithBoolean() {
        return Stream.of(
            Arguments.of("/a",
                true,
                "[{\"op\":\"test\",\"path\":\"/a\",\"value\":true}]"),
            Arguments.of("/b",
                false,
                "[{\"op\":\"test\",\"path\":\"/b\",\"value\":false}]")
            );
    }

    @ParameterizedTest
    @MethodSource
    public void testShouldAppendTestOperationWithBoolean(String path, boolean value, String expected) {
        JsonPatchBuilder sut = Json.createPatchBuilder()
            .test(path, value);
        JsonArray actual = sut.build().toJsonArray();
        assertThat(actual).asString().isEqualTo(expected);
    }
}
