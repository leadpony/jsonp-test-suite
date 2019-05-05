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
enum JsonValueFixture {
    TRUE(JsonValue.TRUE, "true"),
    FALSE(JsonValue.FALSE, "false"),
    NULL(JsonValue.NULL, "null"),

    EMPTY_STRING("", "\"\""),
    BLANK_STRING(" ", "\" \""),
    STRING_WORD("hello", "\"hello\""),
    STRING_CONTAINING_SPACE("hello world", "\"hello world\""),
    STRING_CONTAINING_QUOTATION("hello\"world", "\"hello\\\"world\""),
    STRING_CONTAINING_REVERSE_SOLIDUS("hello\\world", "\"hello\\\\world\""),
    STRING_CONTAINING_BACKSPACE("hello\bworld", "\"hello\\bworld\""),
    STRING_CONTAINING_FF("hello\fworld", "\"hello\\fworld\""),
    STRING_CONTAINING_LF("hello\nworld", "\"hello\\nworld\""),
    STRING_CONTAINING_CR("hello\rworld", "\"hello\\rworld\""),
    STRING_CONTAINING_TAB("hello\tworld", "\"hello\\tworld\""),

    STRING_CONTAINING_NULL("hello\u0000world", "\"hello\\u0000world\""),
    STRING_CONTAINING_VT("hello\u000bworld", "\"hello\\u000bworld\""),
    STRING_CONTAINING_ESC("hello\u001bworld", "\"hello\\u001bworld\""),

    // surrogate pair
    G_CLEF("\ud834\udd1e", "\"\ud834\udd1e\""),

    ZERO(0, "0"),
    ONE(1, "1"),
    MINUS_ONE(-1, "-1"),
    TEN(10, "10"),
    MINUS_TEN(-10, "-10"),
    HUNDRED(100, "100"),
    MINUS_HUNDRED(-100, "-100"),
    THOUSAND(1000, "1000"),
    MINUS_THOUSAND(1000, "1000"),
    HOURS_PER_DAY(24, "24"),
    MINUS_HOURS_PER_DAY(-24, "-24"),
    DAYS_PER_YEAR(365, "365"),
    MINUS_DAYS_PER_YEAR(-365, "-365"),

    MAX_INTEGER(2147483647, "2147483647"),
    MIN_INTEGER(-2147483648, "-2147483648"),
    MAX_LONG(9223372036854775807L, "9223372036854775807"),
    MIN_LONG(-9223372036854775808L, "-9223372036854775808"),

    BASE_OF_NATURAL_LOGARITHM(new BigDecimal("2.718281828459045"), "2.718281828459045"),

    PI(new BigDecimal("3.141592653589793"), "3.141592653589793"),
    MINUS_PI(new BigDecimal("-3.141592653589793"), "-3.141592653589793"),

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

    JsonValueFixture(JsonValue value, String string) {
        this.value = value;
        this.string = string;
    }

    JsonValueFixture(String value, String string) {
        this(Json.createValue(value), string);
    }

    JsonValueFixture(int value, String string) {
        this(Json.createValue(value), string);
    }

    JsonValueFixture(long value, String string) {
        this(Json.createValue(value), string);
    }

    JsonValueFixture(BigDecimal value, String string) {
        this(Json.createValue(value), string);
    }

    JsonValue getJsonValue() {
        return value;
    }

    String getString() {
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
    public static Stream<JsonValueFixture> getArraysAsStream() {
        return Stream.of(values())
                .filter(JsonValueFixture::isArray);
    }

    /**
     * Returns all objects as a stream.
     *
     * @return all objects as a stream.
     */
    public static Stream<JsonValueFixture> getObjectsAsStream() {
        return Stream.of(values())
                .filter(JsonValueFixture::isObject);
    }

    public static Stream<JsonValueFixture> getStructuresAsStream() {
        return Stream.of(values())
                .filter(JsonValueFixture::isStructure);
    }

    public static Stream<JsonValueFixture> getStringsAsStream() {
        return Stream.of(values())
                .filter(JsonValueFixture::isString);
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
