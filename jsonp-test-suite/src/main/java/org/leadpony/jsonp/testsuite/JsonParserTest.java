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

import static org.assertj.core.api.Assertions.assertThat;

import java.io.StringReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;
import javax.json.Json;
import javax.json.JsonValue;
import javax.json.stream.JsonParserFactory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * A test type to test {@link JsonParser}.
 *
 * @author leadpony
 */
public class JsonParserTest {

    private static JsonParserFactory factory;

    @BeforeAll
    public static void setUpOnce() {
        factory = Json.createParserFactory(null);
    }

    private static final Arguments[] parserEventFixtures = new Arguments[] {
            // true
            fixture("true", Event.VALUE_TRUE),
            // false
            fixture("false", Event.VALUE_FALSE),
            // null
            fixture("null", Event.VALUE_NULL),
            // string
            fixture("\"abc\"", Event.VALUE_STRING),
            // integer
            fixture("42", Event.VALUE_NUMBER),
            // number
            fixture("3.14", Event.VALUE_NUMBER),
            // empty array
            fixture("[]", Event.START_ARRAY, Event.END_ARRAY),
            // array with single item
            fixture("[42]", Event.START_ARRAY, Event.VALUE_NUMBER, Event.END_ARRAY),
            // array with multiple items
            fixture("[true,false,null,\"abc\",42]",
                    Event.START_ARRAY,
                    Event.VALUE_TRUE,
                    Event.VALUE_FALSE,
                    Event.VALUE_NULL,
                    Event.VALUE_STRING,
                    Event.VALUE_NUMBER,
                    Event.END_ARRAY),
            // array with array item
            fixture("[[]]",
                    Event.START_ARRAY,
                    Event.START_ARRAY,
                    Event.END_ARRAY,
                    Event.END_ARRAY),
            // array with object item
            fixture("[{}]",
                    Event.START_ARRAY,
                    Event.START_OBJECT,
                    Event.END_OBJECT,
                    Event.END_ARRAY),
            // empty object
            fixture("{}", Event.START_OBJECT, Event.END_OBJECT),
            // object with single property
            fixture("{\"a\":42}",
                    Event.START_OBJECT,
                    Event.KEY_NAME,
                    Event.VALUE_NUMBER,
                    Event.END_OBJECT),
            // object with multiple properties
            fixture("{\"a\":true,\"b\":false,\"c\":null,\"d\":\"abc\",\"e\":42}",
                    Event.START_OBJECT,
                    Event.KEY_NAME,
                    Event.VALUE_TRUE,
                    Event.KEY_NAME,
                    Event.VALUE_FALSE,
                    Event.KEY_NAME,
                    Event.VALUE_NULL,
                    Event.KEY_NAME,
                    Event.VALUE_STRING,
                    Event.KEY_NAME,
                    Event.VALUE_NUMBER,
                    Event.END_OBJECT),
            // object with array property
            fixture("{\"a\":[]}",
                    Event.START_OBJECT,
                    Event.KEY_NAME,
                    Event.START_ARRAY,
                    Event.END_ARRAY,
                    Event.END_OBJECT),
            // object with object property
            fixture("{\"a\":{}}",
                    Event.START_OBJECT,
                    Event.KEY_NAME,
                    Event.START_OBJECT,
                    Event.END_OBJECT,
                    Event.END_OBJECT),
    };

    public static Stream<Arguments> parserEventFixtures() {
        return Stream.of(parserEventFixtures);
    }

    @ParameterizedTest
    @MethodSource("parserEventFixtures")
    public void nextShouldReturnEvents(String json, Event... expected) {
        JsonParser parser = factory.createParser(new StringReader(json));
        List<Event> actual = new ArrayList<>();

        while (parser.hasNext()) {
            actual.add(parser.next());
        }
        parser.close();

        assertThat(actual).containsExactly(expected);
    }

    private static final Arguments[] stringFixtures = new Arguments[] {
            fixture("\"abc\"", "abc"),
            // empty string
            fixture("\"\"", ""),
            fixture("42", "42"),
            fixture("3.14", "3.14"),
    };

    public static Stream<Arguments> stringFixtures() {
        return Stream.of(stringFixtures);
    }

    @ParameterizedTest
    @MethodSource("stringFixtures")
    public void getStringShouldReturnExpected(String json, String expected) {
        JsonParser parser = factory.createParser(new StringReader(json));

        parser.next();
        String actual = parser.getString();
        parser.close();

        assertThat(actual).isEqualTo(expected);
    }

    private static final Arguments[] bigDecimalFixtures = new Arguments[] {
            fixture("42", new BigDecimal("42")),
            fixture("3.14", new BigDecimal("3.14")),
    };

    public static Stream<Arguments> bigDecimalFixtures() {
        return Stream.of(bigDecimalFixtures);
    }

    @ParameterizedTest
    @MethodSource("bigDecimalFixtures")
    public void getBigDecimalShouldReturnExpected(String json, BigDecimal expected) {
        JsonParser parser = factory.createParser(new StringReader(json));

        parser.next();
        BigDecimal actual = parser.getBigDecimal();
        parser.close();

        assertThat(actual).isEqualTo(expected);
    }

    private static final Arguments[] jsonValueFixtures = new Arguments[] {
            fixture("true", JsonValue.TRUE),
            fixture("false", JsonValue.FALSE),
            fixture("null", JsonValue.NULL),
            fixture("\"abc\"", Json.createValue("abc")),
            fixture("0", Json.createValue(0)),
            fixture("-0", Json.createValue(0)),
            fixture("1", Json.createValue(1)),
            fixture("-1", Json.createValue(-1)),
            fixture("2147483647", Json.createValue(2147483647)),
            fixture("-2147483648", Json.createValue(-2147483648)),
            fixture("9223372036854775807", Json.createValue(9223372036854775807L)),
            fixture("-9223372036854775808", Json.createValue(-9223372036854775808L)),
            fixture("2.718281828459045", Json.createValue(new BigDecimal("2.718281828459045"))),
            fixture("3.141592653589793", Json.createValue(new BigDecimal("3.141592653589793"))),
            fixture("-123.456789", Json.createValue(new BigDecimal("-123.456789"))),
    };

    public static Stream<Arguments> jsonValueFixtures() {
        return Stream.of(jsonValueFixtures);
    }

    @ParameterizedTest
    @MethodSource("jsonValueFixtures")
    public void getValueShouldReturnExpected(String json, JsonValue expected) {
        JsonParser parser = factory.createParser(new StringReader(json));

        parser.next();
        JsonValue actual = parser.getValue();
        parser.close();

        assertThat(actual).isEqualTo(expected);
    }

    private static final Arguments[] integralFixtures = new Arguments[] {
            fixture("0", true),
            fixture("123", true),
            fixture("-456", true),
            fixture("123.456", false),
            fixture("-123.456", false),
            fixture("1.0", false),
            fixture("-1.0", false),
    };

    public static Stream<Arguments> integralFixtures() {
        return Stream.of(integralFixtures);
    }

    @ParameterizedTest
    @MethodSource("integralFixtures")
    public void isIntegralNumberShouldReturnExpected(String json, boolean expected) {
        JsonParser parser = factory.createParser(new StringReader(json));

        parser.next();
        boolean actual = parser.isIntegralNumber();
        parser.close();

        assertThat(actual).isEqualTo(expected);
    }

    private static final Arguments[] intFixtures = new Arguments[] {
            fixture("0", 0),
            fixture("-0", 0),
            fixture("1", 1),
            fixture("-1", -1),
            fixture("10", 10),
            fixture("-10", -10),
            fixture("24", 24),
            fixture("-24", -24),
            fixture("365", 365),
            fixture("-365", -365),
            fixture("2147483647", 2147483647),
            fixture("-2147483648", -2147483648),
            fixture("2147483648", -2147483648),
            fixture("-2147483649", 2147483647),
    };

    public static Stream<Arguments> intFixtures() {
        return Stream.of(intFixtures);
    }

    @ParameterizedTest
    @MethodSource("intFixtures")
    public void getIntNumberShouldReturnExpected(String json, int expected) {
        JsonParser parser = factory.createParser(new StringReader(json));

        parser.next();
        int actual = parser.getInt();
        parser.close();

        assertThat(actual).isEqualTo(expected);
    }

    private static final Arguments[] longFixtures = new Arguments[] {
            fixture("0", 0L),
            fixture("-0", 0L),
            fixture("1", 1L),
            fixture("-1", -1L),
            fixture("10", 10L),
            fixture("-10", -10L),
            fixture("24", 24L),
            fixture("-24", -24L),
            fixture("365", 365L),
            fixture("-365", -365L),
            fixture("2147483647", 2147483647L),
            fixture("-2147483648", -2147483648L),
            fixture("2147483648", 2147483648L),
            fixture("-2147483649", -2147483649L),
            fixture("9223372036854775807", 9223372036854775807L),
            fixture("-9223372036854775808", -9223372036854775808L),
            fixture("9223372036854775808", -9223372036854775808L),
            fixture("-9223372036854775809", 9223372036854775807L),
    };

    public static Stream<Arguments> longFixtures() {
        return Stream.of(longFixtures);
    }

    @ParameterizedTest
    @MethodSource("longFixtures")
    public void getLongNumberShouldReturnExpected(String json, long expected) {
        JsonParser parser = factory.createParser(new StringReader(json));

        parser.next();
        long actual = parser.getLong();
        parser.close();

        assertThat(actual).isEqualTo(expected);
    }

    private static Arguments fixture(Object... args) {
        return Arguments.of(args);
    }

    private static Arguments fixture(String json, Event... expected) {
        return Arguments.of(json, expected);
    }
}
