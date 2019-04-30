/*
 * Copyright 2019 the JSON-P Test Suite Authors.
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
package org.leadpony.jsonp.testsuite;

import static javax.json.Json.createValue;

import java.math.BigDecimal;
import java.util.function.Consumer;
import java.util.stream.Stream;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import javax.json.JsonValue.ValueType;

/**
 * Predefined JSON values.
 *
 * @author leadpony
 */
enum JsonValueSample {
    TRUE(JsonValue.TRUE, "true"),
    FALSE(JsonValue.FALSE, "false"),
    NULL(JsonValue.NULL, "null"),

    EMPTY_STRING(createValue(""), "\"\""),
    BLANK_STRING(createValue(" "), "\" \""),
    STRING_WORD(createValue("hello"), "\"hello\""),
    STRING_CONTAINING_SPACE(createValue("hello world"), "\"hello world\""),
    STRING_CONTAINING_QUOTATION(createValue("hello\"world"), "\"hello\\\"world\""),
    STRING_CONTAINING_REVERSE_SOLIDUS(createValue("hello\\world"), "\"hello\\\\world\""),
    STRING_CONTAINING_BACKSPACE(createValue("hello\bworld"), "\"hello\\bworld\""),
    STRING_CONTAINING_FF(createValue("hello\fworld"), "\"hello\\fworld\""),
    STRING_CONTAINING_LF(createValue("hello\nworld"), "\"hello\\nworld\""),
    STRING_CONTAINING_CR(createValue("hello\rworld"), "\"hello\\rworld\""),
    STRING_CONTAINING_TAB(createValue("hello\tworld"), "\"hello\\tworld\""),

    STRING_CONTAINING_NULL(createValue("hello\u0000world"), "\"hello\\u0000world\""),
    STRING_CONTAINING_VT(createValue("hello\u000bworld"), "\"hello\\u000bworld\""),
    STRING_CONTAINING_ESC(createValue("hello\u001bworld"), "\"hello\\u001bworld\""),

    // surrogate pair
    G_CLEF(createValue("\ud834\udd1e"), "\"\ud834\udd1e\""),

    ZERO(createValue(0), "0"),
    ONE(createValue(1), "1"),
    TEN(createValue(10), "10"),
    MINUS_ONE(createValue(-1), "-1"),
    MINUS_TEN(createValue(-10), "-10"),
    MAX_INTEGER(createValue(2147483647), "2147483647"),
    MIN_INTEGER(createValue(-2147483648), "-2147483648"),
    MAX_LONG(createValue(9223372036854775807L), "9223372036854775807"),
    MIN_LONG(createValue(-9223372036854775808L), "-9223372036854775808"),

    PI(createValue(new BigDecimal("3.141592653589793")), "3.141592653589793"),
    MINUS_PI(createValue(new BigDecimal("-3.141592653589793")), "-3.141592653589793"),

    EMPTY_ARRAY(JsonValue.EMPTY_JSON_ARRAY, "[]"),

    ARRAY_OF_INTEGER(
            array(b->b.add(42)),
            "[42]"),

    ARRAY_OF_INTEGERS(
            array(b->b.add(1).add(2).add(4)),
            "[1,2,4]"),

    ARRAY_OF_STRING(
            array(b->b.add("hello")),
            "[\"hello\"]"),

    ARRAY_OF_STRINGS(
            array(b->b.add("hello").add("world")),
            "[\"hello\",\"world\"]"),

    ARRAY_OF_TRUE(
            array(b->b.add(true)),
            "[true]"),

    ARRAY_OF_FALSE(
            array(b->b.add(false)),
            "[false]"),

    ARRAY_OF_BOOLEANS(
            array(b->b.add(true).add(false)),
            "[true,false]"),

    ARRAY_OF_NULL(
            array(b->b.addNull()),
            "[null]"),

    ARRAY_OF_NULLS(
            array(b->b.addNull().addNull()),
            "[null,null]"),

    ARRAY_OF_ARRAY(
            array(b->b.add(
                    array(b2->b2.add(1).add(2).add(3)))),
            "[[1,2,3]]"),

    ARRAY_OF_EMPTY_ARRAY(
            array(b->b.add(JsonValue.EMPTY_JSON_ARRAY)),
            "[[]]"),

    ARRAY_OF_EMPTY_OBJECT(
            array(b->b.add(JsonValue.EMPTY_JSON_OBJECT)),
            "[{}]"),

    ARRAY_OF_ARRAYS(
            array(b->{
                b.add(array(b2->b2.add(1).add(2).add(3)));
                b.add(array(b2->b2.add(4).add(5).add(6)));
            }),
            "[[1,2,3],[4,5,6]]"),

    ARRAY_OF_EMPTY_ARRAYS(
            array(b->{
                b.add(JsonValue.EMPTY_JSON_ARRAY);
                b.add(JsonValue.EMPTY_JSON_ARRAY);
            }),
            "[[],[]]"),

    ARRAY_OF_EMPTY_OBJECTS(
            array(b->{
                b.add(JsonValue.EMPTY_JSON_OBJECT);
                b.add(JsonValue.EMPTY_JSON_OBJECT);
            }),
            "[{},{}]"),

    EMPTY_OBJECT(JsonValue.EMPTY_JSON_OBJECT, "{}"),

    OBJECT_OF_INTEGERS(
            object(b->{
                b.add("a", 1);
                b.add("b", 2);
            }),
            "{\"a\":1,\"b\":2}"),

    OBJECT_OF_STRING(
            object(b->{
                b.add("a", "hello");
                b.add("b", "world");
            }),
            "{\"a\":\"hello\",\"b\":\"world\"}"),

    OBJECT_OF_ARRAYS(
            object(b->{
                b.add("a", array(b2->b2.add(1).add(2)));
                b.add("b", array(b2->b2.add(3).add(4)));
            }),
            "{\"a\":[1,2],\"b\":[3,4]}"),

    OBJECT_OF_EMPTY_ARRAYS(
            object(b->{
                b.add("a", JsonValue.EMPTY_JSON_ARRAY);
                b.add("b", JsonValue.EMPTY_JSON_ARRAY);
            }),
            "{\"a\":[],\"b\":[]}"),

    OBJECT_OF_OBJECTS(
            object(b->{
                b.add("a",
                        object(b2->{
                            b2.add("a1", 1);
                            b2.add("a2", 2);
                        }));
                b.add("b",
                        object(b2->{
                            b2.add("b1", 3);
                            b2.add("b2", 4);
                        }));
            }),
            "{\"a\":{\"a1\":1,\"a2\":2},\"b\":{\"b1\":3,\"b2\":4}}"),

    OBJECT_OF_EMPTY_OBJECTS(
            object(b->{
                b.add("a", JsonValue.EMPTY_JSON_OBJECT);
                b.add("b", JsonValue.EMPTY_JSON_OBJECT);
            }),
            "{\"a\":{},\"b\":{}}"),

    ;

    private final JsonValue value;
    private final String string;

    private static JsonBuilderFactory builderFactory;

    JsonValueSample(JsonValue value, String string) {
        this.value = value;
        this.string = string;
    }

    JsonValue asJsonValue() {
        return value;
    }

    String asString() {
        return string;
    }

    ValueType getType() {
        return value.getValueType();
    }

    boolean isString() {
        return getType() == ValueType.STRING;
    }

    boolean isArray() {
        return getType() == ValueType.ARRAY;
    }

    boolean isObject() {
        return getType() == ValueType.OBJECT;
    }

    boolean isStructure() {
        return isArray() || isObject();
    }

    /**
     * Returns all arrays as a stream.
     *
     * @return all arrays as a stream.
     */
    public static Stream<JsonValueSample> getArraysAsStream() {
        return Stream.of(values())
                .filter(JsonValueSample::isArray);
    }

    /**
     * Returns all objects as a stream.
     *
     * @return all objects as a stream.
     */
    public static Stream<JsonValueSample> getObjectsAsStream() {
        return Stream.of(values())
                .filter(JsonValueSample::isObject);
    }

    public static Stream<JsonValueSample> getStructuresAsStream() {
        return Stream.of(values())
                .filter(JsonValueSample::isStructure);
    }

    public static Stream<JsonValueSample> getStringsAsStream() {
        return Stream.of(values())
                .filter(JsonValueSample::isString);
    }

    private static JsonArray array(Consumer<JsonArrayBuilder> consumer) {
        JsonArrayBuilder builder = getBuilderFactory().createArrayBuilder();
        consumer.accept(builder);
        return builder.build();
    }

    private static JsonObject object(Consumer<JsonObjectBuilder> consumer) {
        JsonObjectBuilder builder = getBuilderFactory().createObjectBuilder();
        consumer.accept(builder);
        return builder.build();
    }

    private static JsonBuilderFactory getBuilderFactory() {
        if (builderFactory == null) {
            builderFactory = Json.createBuilderFactory(null);
        }
        return builderFactory;
    }
}
