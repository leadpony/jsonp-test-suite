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
package org.leadpony.jsonp.testsuite.tests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Stream;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonStructure;
import javax.json.JsonValue;
import javax.json.JsonValue.ValueType;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;
import javax.json.stream.JsonParserFactory;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.leadpony.jsonp.testsuite.helper.LogHelper;

/**
 * @author leadpony
 */
public class ValueJsonParserTest {

    @SuppressWarnings("unused")
    private static final Logger log = LogHelper.getLogger(JsonParserTest.class);

    private static final JsonParserFactory parserFactory = Json.createParserFactory(null);
    private static final JsonBuilderFactory builderFactory = Json.createBuilderFactory(null);

    enum ArrayParserEventTestCase {
        EMPTY_ARRAY(
                JsonValue.EMPTY_JSON_ARRAY,
                Event.START_ARRAY,
                Event.END_ARRAY),

        SIMPLE_ARRAY(
                array(b -> {
                    b.add("hello").add(42).add(true).add(false).addNull();
                }),
                Event.START_ARRAY,
                Event.VALUE_STRING,
                Event.VALUE_NUMBER,
                Event.VALUE_TRUE,
                Event.VALUE_FALSE,
                Event.VALUE_NULL,
                Event.END_ARRAY),

        ARRAY_OF_EMPTY_ARRAYS(
                array(b -> {
                    b.add(JsonValue.EMPTY_JSON_ARRAY);
                    b.add(JsonValue.EMPTY_JSON_ARRAY);
                    b.add(JsonValue.EMPTY_JSON_ARRAY);
                }),
                Event.START_ARRAY,
                Event.START_ARRAY,
                Event.END_ARRAY,
                Event.START_ARRAY,
                Event.END_ARRAY,
                Event.START_ARRAY,
                Event.END_ARRAY,
                Event.END_ARRAY),

        ARRAY_OF_EMPTY_OBJECTS(
                array(b -> {
                    b.add(JsonValue.EMPTY_JSON_OBJECT);
                    b.add(JsonValue.EMPTY_JSON_OBJECT);
                    b.add(JsonValue.EMPTY_JSON_OBJECT);
                }),
                Event.START_ARRAY,
                Event.START_OBJECT,
                Event.END_OBJECT,
                Event.START_OBJECT,
                Event.END_OBJECT,
                Event.START_OBJECT,
                Event.END_OBJECT,
                Event.END_ARRAY),

        ARRAY_OF_ARRAYS(
                array(b -> {
                    b.add(array(b2 -> b2.add(1).add(2).add(3)));
                    b.add(array(b2 -> b2.add(4).add(5).add(6)));
                }),
                Event.START_ARRAY,
                Event.START_ARRAY,
                Event.VALUE_NUMBER,
                Event.VALUE_NUMBER,
                Event.VALUE_NUMBER,
                Event.END_ARRAY,
                Event.START_ARRAY,
                Event.VALUE_NUMBER,
                Event.VALUE_NUMBER,
                Event.VALUE_NUMBER,
                Event.END_ARRAY,
                Event.END_ARRAY),

        ARRAY_OF_OBJECTS(
                array(b -> {
                    b.add(object(b2 -> b2.add("a", "hello").add("b", 42)));
                    b.add(object(b2 -> b2.add("c", true).add("d", false).addNull("e")));
                }),
                Event.START_ARRAY,
                Event.START_OBJECT,
                Event.KEY_NAME,
                Event.VALUE_STRING,
                Event.KEY_NAME,
                Event.VALUE_NUMBER,
                Event.END_OBJECT,
                Event.START_OBJECT,
                Event.KEY_NAME,
                Event.VALUE_TRUE,
                Event.KEY_NAME,
                Event.VALUE_FALSE,
                Event.KEY_NAME,
                Event.VALUE_NULL,
                Event.END_OBJECT,
                Event.END_ARRAY),
                ;

        final JsonArray value;
        final Event[] events;

        ArrayParserEventTestCase(JsonArray value, Event... events) {
            this.value = value;
            this.events = events;
        }
    }

    @ParameterizedTest
    @EnumSource(ArrayParserEventTestCase.class)
    public void nextShouldReturnEventsAsExpected(ArrayParserEventTestCase test) {
        List<Event> actual = new ArrayList<>();
        try (JsonParser parser = createParser(test.value)) {
            while (parser.hasNext()) {
                actual.add(parser.next());
            }
        }

        assertThat(actual).containsExactly(test.events);
    }

    enum ObjectParserEventTestCase {
        EMPTY_OBJECT(
                JsonValue.EMPTY_JSON_OBJECT,
                Event.START_OBJECT,
                Event.END_OBJECT),

        SIMPLE_OBJECT(
                object(b -> b.add("a", "hello")
                        .add("b", 42)
                        .add("c", true)
                        .add("d", false)
                        .addNull("e")),
                Event.START_OBJECT,
                Event.KEY_NAME,
                Event.VALUE_STRING,
                Event.KEY_NAME,
                Event.VALUE_NUMBER,
                Event.KEY_NAME,
                Event.VALUE_TRUE,
                Event.KEY_NAME,
                Event.VALUE_FALSE,
                Event.KEY_NAME,
                Event.VALUE_NULL,
                Event.END_OBJECT),

        OBJECT_OF_EMPTY_ARRAYS(
                object(b -> b.add("a", JsonValue.EMPTY_JSON_ARRAY)
                        .add("b", JsonValue.EMPTY_JSON_ARRAY)),
                Event.START_OBJECT,
                Event.KEY_NAME,
                Event.START_ARRAY,
                Event.END_ARRAY,
                Event.KEY_NAME,
                Event.START_ARRAY,
                Event.END_ARRAY,
                Event.END_OBJECT),

        OBJECT_OF_EMPTY_OBJECTS(
                object(b -> b.add("a", JsonValue.EMPTY_JSON_OBJECT)
                        .add("b", JsonValue.EMPTY_JSON_OBJECT)),
                Event.START_OBJECT,
                Event.KEY_NAME,
                Event.START_OBJECT,
                Event.END_OBJECT,
                Event.KEY_NAME,
                Event.START_OBJECT,
                Event.END_OBJECT,
                Event.END_OBJECT),

        OBJECT_OF_ARRAYS(
                object(b -> b.add("a", array(b2 -> b2.add("hello").add(42)))
                        .add("b", array(b2 -> b2.add(true).add(false).addNull()))),
                Event.START_OBJECT,
                Event.KEY_NAME,
                Event.START_ARRAY,
                Event.VALUE_STRING,
                Event.VALUE_NUMBER,
                Event.END_ARRAY,
                Event.KEY_NAME,
                Event.START_ARRAY,
                Event.VALUE_TRUE,
                Event.VALUE_FALSE,
                Event.VALUE_NULL,
                Event.END_ARRAY,
                Event.END_OBJECT),

        OBJECT_OF_OBJECTS(
                object(b -> b.add("a", object(b2 -> b2.add("a1", "hello").add("a2", 42)))
                        .add("b", object(b2 -> b2.add("b1", true).add("b2", false).addNull("b3")))),
                Event.START_OBJECT,
                Event.KEY_NAME,
                Event.START_OBJECT,
                Event.KEY_NAME,
                Event.VALUE_STRING,
                Event.KEY_NAME,
                Event.VALUE_NUMBER,
                Event.END_OBJECT,
                Event.KEY_NAME,
                Event.START_OBJECT,
                Event.KEY_NAME,
                Event.VALUE_TRUE,
                Event.KEY_NAME,
                Event.VALUE_FALSE,
                Event.KEY_NAME,
                Event.VALUE_NULL,
                Event.END_OBJECT,
                Event.END_OBJECT),
                ;

        final JsonObject value;
        final Event[] events;

        ObjectParserEventTestCase(JsonObject value, Event... events) {
            this.value = value;
            this.events = events;
        }
    }

    @ParameterizedTest
    @EnumSource(ObjectParserEventTestCase.class)
    public void nextShouldReturnEventsAsExpected(ObjectParserEventTestCase test) {
        List<Event> actual = new ArrayList<>();
        try (JsonParser parser = createParser(test.value)) {
            while (parser.hasNext()) {
                actual.add(parser.next());
            }
        }

        assertThat(actual).containsExactly(test.events);
    }

    public static Stream<JsonStructure> nextShouldThrowNoSuchElementExceptionAfterFinalEvent() {
        return Stream.of(JsonValue.EMPTY_JSON_ARRAY, JsonValue.EMPTY_JSON_OBJECT);
    }

    @ParameterizedTest
    @MethodSource
    public void nextShouldThrowNoSuchElementExceptionAfterFinalEvent(JsonStructure value) {
        Throwable thrown = null;

        try (JsonParser parser = createParser(value)) {
            while (parser.hasNext()) {
                parser.next();
            }

            thrown = catchThrowable(()->parser.next());
        }

        assertThat(thrown).isInstanceOf(NoSuchElementException.class);
    }

    enum StringTestCase {
        STRING_AS_FIRST_ITEM(
                array(b->b.add("hello").add("world")),
                2,
                "hello"
                ),

        STRING_AS_LAST_ITEM(
                STRING_AS_FIRST_ITEM.json,
                3,
                "world"
                ),

        NUMBER_AS_FIRST_ITEM(
                array(b->b.add(42).add(new BigDecimal("3.14"))),
                2,
                "42"
                ),

        NUMBER_AS_LAST_ITEM(
                NUMBER_AS_FIRST_ITEM.json,
                3,
                "3.14"
                ),

        STRING_AS_FIRST_PROPERTY_VALUE(
                object(b->b.add("a", "hello").add("b", "world")),
                3,
                "hello"
                ),

        STRING_AS_LAST_PROPERTY_VALUE(
                STRING_AS_FIRST_PROPERTY_VALUE.json,
                5,
                "world"
                ),

        NUMBER_AS_FIRST_PROPERTY_VALUE(
                object(b->b.add("a", 42).add("b", new BigDecimal("3.14"))),
                3,
                "42"
                ),

        NUMBER_AS_LAST_PROPERTY_VALUE(
                NUMBER_AS_FIRST_PROPERTY_VALUE.json,
                5,
                "3.14"
                ),

        STRING_AS_FIRST_PROPERTY_KEY(
                object(b->b.add("hello", 1).add("world", 2)),
                2,
                "hello"
                ),

        STRING_AS_LAST_PROPERTY_KEY(
                STRING_AS_FIRST_PROPERTY_KEY.json,
                4,
                "world"
                ),
        ;

        final JsonStructure json;
        final int iterations;
        final String value;

        private StringTestCase(JsonStructure json, int iterations, String value) {
            this.json = json;
            this.iterations = iterations;
            this.value = value;
        }
    }

    @ParameterizedTest
    @EnumSource(StringTestCase.class)
    public void getStringShouldReturnStringAsExpected(StringTestCase test) {
        String actual = extractValue(test.json, test.iterations,
                JsonParser::getString);

        assertThat(actual).isEqualTo(test.value);
    }

    enum IsIntegralNumberTestCase {

        NUMBER_AS_FIRST_ITEM(
                array(b->b.add(42).add(new BigDecimal("3.14"))),
                2,
                true
                ),

        NUMBER_AS_LAST_ITEM(
                NUMBER_AS_FIRST_ITEM.json,
                3,
                false
                ),

        NUMBER_AS_FIRST_PROPERTY_VALUE(
                object(b->b.add("a", 42).add("b", new BigDecimal("3.14"))),
                3,
                true
                ),

        NUMBER_AS_LAST_PROPERTY_VALUE(
                NUMBER_AS_FIRST_PROPERTY_VALUE.json,
                5,
                false
                ),
        ;

        final JsonStructure json;
        final int iterations;
        final boolean value;

        private IsIntegralNumberTestCase(JsonStructure json, int iterations, boolean value) {
            this.json = json;
            this.iterations = iterations;
            this.value = value;
        }
    }

    @ParameterizedTest
    @EnumSource(IsIntegralNumberTestCase.class)
    public void isIntegralNumberShouldReturnStringAsExpected(IsIntegralNumberTestCase test) {
        boolean actual = extractValue(test.json, test.iterations,
                JsonParser::isIntegralNumber);

        assertThat(actual).isEqualTo(test.value);
    }

    enum IntTestCase {

        NUMBER_AS_FIRST_ITEM(
                array(b->b.add(Integer.MAX_VALUE).add(Integer.MIN_VALUE)),
                2,
                Integer.MAX_VALUE
                ),

        NUMBER_AS_LAST_ITEM(
                NUMBER_AS_FIRST_ITEM.json,
                3,
                Integer.MIN_VALUE
                ),

        NUMBER_AS_FIRST_PROPERTY_VALUE(
                object(b->b.add("a", Integer.MAX_VALUE).add("b", Integer.MIN_VALUE)),
                3,
                Integer.MAX_VALUE
                ),

        NUMBER_AS_LAST_PROPERTY_VALUE(
                NUMBER_AS_FIRST_PROPERTY_VALUE.json,
                5,
                Integer.MIN_VALUE
                ),
        ;

        final JsonStructure json;
        final int iterations;
        final int value;

        private IntTestCase(JsonStructure json, int iterations, int value) {
            this.json = json;
            this.iterations = iterations;
            this.value = value;
        }
    }

    @ParameterizedTest
    @EnumSource(IntTestCase.class)
    public void getIntShouldReturnStringAsExpected(IntTestCase test) {
        int actual = extractValue(test.json, test.iterations,
                JsonParser::getInt);

        assertThat(actual).isEqualTo(test.value);
    }

    enum LongTestCase {

        NUMBER_AS_FIRST_ITEM(
                array(b->b.add(Long.MAX_VALUE).add(Long.MIN_VALUE)),
                2,
                Long.MAX_VALUE
                ),

        NUMBER_AS_LAST_ITEM(
                NUMBER_AS_FIRST_ITEM.json,
                3,
                Long.MIN_VALUE
                ),

        NUMBER_AS_FIRST_PROPERTY_VALUE(
                object(b->b.add("a", Long.MAX_VALUE).add("b", Long.MIN_VALUE)),
                3,
                Long.MAX_VALUE
                ),

        NUMBER_AS_LAST_PROPERTY_VALUE(
                NUMBER_AS_FIRST_PROPERTY_VALUE.json,
                5,
                Long.MIN_VALUE
                ),
        ;

        final JsonStructure json;
        final int iterations;
        final long value;

        private LongTestCase(JsonStructure json, int iterations, long value) {
            this.json = json;
            this.iterations = iterations;
            this.value = value;
        }
    }

    @ParameterizedTest
    @EnumSource(LongTestCase.class)
    public void getLongShouldReturnStringAsExpected(LongTestCase test) {
        long actual = extractValue(test.json, test.iterations,
                JsonParser::getLong);

        assertThat(actual).isEqualTo(test.value);
    }

    enum BigDecimalTestCase {

        NUMBER_AS_FIRST_ITEM(
                array(b->b.add(42).add(new BigDecimal("3.14"))),
                2,
                new BigDecimal(42)
                ),

        NUMBER_AS_LAST_ITEM(
                NUMBER_AS_FIRST_ITEM.json,
                3,
                new BigDecimal("3.14")
                ),

        NUMBER_AS_FIRST_PROPERTY_VALUE(
                object(b->b.add("a", 42).add("b", new BigDecimal("3.14"))),
                3,
                new BigDecimal(42)
                ),

        NUMBER_AS_LAST_PROPERTY_VALUE(
                NUMBER_AS_FIRST_PROPERTY_VALUE.json,
                5,
                new BigDecimal("3.14")
                ),
        ;

        final JsonStructure json;
        final int iterations;
        final BigDecimal value;

        private BigDecimalTestCase(JsonStructure json, int iterations, BigDecimal value) {
            this.json = json;
            this.iterations = iterations;
            this.value = value;
        }
    }

    @ParameterizedTest
    @EnumSource(BigDecimalTestCase.class)
    public void getBigDecimalShouldReturnStringAsExpected(BigDecimalTestCase test) {
        BigDecimal actual = extractValue(test.json, test.iterations,
                JsonParser::getBigDecimal);

        assertThat(actual).isEqualTo(test.value);
    }

    private static <T> T extractValue(JsonStructure value, int iterations, Function<JsonParser, T> mapper) {
        try (JsonParser parser = createParser(value)) {
            while (iterations-- > 0) {
                parser.next();
            }
            return mapper.apply(parser);
        }
    }

    private static JsonParser createParser(JsonStructure value) {
        if (value.getValueType() == ValueType.ARRAY) {
            return parserFactory.createParser((JsonArray)value);
        } else {
            return parserFactory.createParser((JsonObject)value);
        }
    }

    private static JsonArray array(Consumer<JsonArrayBuilder> consumer) {
        JsonArrayBuilder builder = builderFactory.createArrayBuilder();
        consumer.accept(builder);
        return builder.build();
    }

    private static JsonObject object(Consumer<JsonObjectBuilder> consumer) {
        JsonObjectBuilder builder = builderFactory.createObjectBuilder();
        consumer.accept(builder);
        return builder.build();
    }
}
