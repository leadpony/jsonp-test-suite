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

import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;
import javax.json.Json;
import javax.json.JsonValue;
import javax.json.stream.JsonParserFactory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

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

    static enum ParserEventFixture {
        TRUE("true", Event.VALUE_TRUE),
        FALSE("false", Event.VALUE_FALSE),
        NULL("null", Event.VALUE_NULL),
        WORD("\"abc\"", Event.VALUE_STRING),
        INTEGER("42", Event.VALUE_NUMBER),
        NUMBER("3.14", Event.VALUE_NUMBER),

        EMPTY_ARRAY("[]", Event.START_ARRAY, Event.END_ARRAY),
        ARRAY_OF_ITEM("[42]", Event.START_ARRAY, Event.VALUE_NUMBER, Event.END_ARRAY),
        ARRAY_OF_MULTIPLE_ITEMS("[true,false,null,\"abc\",42]",
                Event.START_ARRAY,
                Event.VALUE_TRUE,
                Event.VALUE_FALSE,
                Event.VALUE_NULL,
                Event.VALUE_STRING,
                Event.VALUE_NUMBER,
                Event.END_ARRAY),
        ARRAY_OF_ARRAY("[[]]",
                Event.START_ARRAY,
                Event.START_ARRAY,
                Event.END_ARRAY,
                Event.END_ARRAY),
        ARRAY_OF_OBJECT("[{}]",
                Event.START_ARRAY,
                Event.START_OBJECT,
                Event.END_OBJECT,
                Event.END_ARRAY),

        EMPTY_OBJECT("{}", Event.START_OBJECT, Event.END_OBJECT),
        OBJECT_OF_SINGLE_PROPERTY("{\"a\":42}",
                Event.START_OBJECT,
                Event.KEY_NAME,
                Event.VALUE_NUMBER,
                Event.END_OBJECT),
        OBJECT_OF_MULTIPLE_PROPERTIES("{\"a\":true,\"b\":false,\"c\":null,\"d\":\"abc\",\"e\":42}",
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
        OBJECT_OF_ARRAY_PROPERTY("{\"a\":[]}",
                Event.START_OBJECT,
                Event.KEY_NAME,
                Event.START_ARRAY,
                Event.END_ARRAY,
                Event.END_OBJECT),
        OBJECT_OF_OBJECT_PROPERTY("{\"a\":{}}",
                Event.START_OBJECT,
                Event.KEY_NAME,
                Event.START_OBJECT,
                Event.END_OBJECT,
                Event.END_OBJECT),
        ;

        private final String json;
        private final Event[] events;

        ParserEventFixture(String json, Event... events) {
            this.json = json;
            this.events = events;
        }
    };

    @ParameterizedTest
    @EnumSource(ParserEventFixture.class)
    public void nextShouldReturnEvents(ParserEventFixture fixture) {
        JsonParser parser = factory.createParser(new StringReader(fixture.json));
        List<Event> actual = new ArrayList<>();

        while (parser.hasNext()) {
            actual.add(parser.next());
        }
        parser.close();

        assertThat(actual).containsExactly(fixture.events);
    }

    static enum StringFixture {
        EMPTY_STRING("\"\"", ""),
        BLANK_STRING("\" \"", " "),
        TWO_SPACES("\"  \"", "  "),
        FOUR_SPACES("\"    \"", "    "),
        SINGLE_WORD("\"hello\"", "hello"),

        SENTENCE("\"The quick brown fox jumps over the lazy dog\"", "The quick brown fox jumps over the lazy dog"),

        CONTAINING_SPACE("\"hello world\"", "hello world"),
        CONTAINING_QUOTATION("\"hello\\\"world\"", "hello\"world"),
        CONTAINING_SOLIDUS("\"hello\\/world\"", "hello/world"),
        CONTAINING_REVERSE_SOLIDUS("\"hello\\\\world\"", "hello\\world"),

        CONTAINING_BACK_SPACE("\"hello\\bworld\"", "hello\bworld"),
        CONTAINING_FORM_FEED("\"hello\\fworld\"", "hello\fworld"),
        CONTAINING_LINE_FEED("\"hello\\nworld\"", "hello\nworld"),
        CONTAINING_CARIAGE_RETURN("\"hello\\rworld\"", "hello\rworld"),
        CONTAINING_TAB("\"hello\\tworld\"", "hello\tworld"),

        INTEGRAL_NUMBER("42", "42"),
        MINUS_INTEGRAL_NUMBER("-42", "-42"),
        DECIMAL_NUMBER("3.14", "3.14"),
        MINUS_DECIMAL_NUMBER("-3.14", "-3.14"),
        ;

        private final String json;
        private final String value;

        private StringFixture(String json, String value) {
            this.json = json;
            this.value = value;
        }
    }

    @ParameterizedTest
    @EnumSource(StringFixture.class)
    public void getStringShouldReturnStringAsExpected(StringFixture fixture) {
        JsonParser parser = factory.createParser(new StringReader(fixture.json));

        parser.next();
        String actual = parser.getString();
        parser.close();

        assertThat(actual).isEqualTo(fixture.value);
    }

    static enum BigDecimalFixture {
        ZERO("0"),
        MINUS_ZERO("-0"),
        ONE("1"),
        MINUS_ONE("-1"),

        TEN("10"),
        MINUS_TEN("-10"),
        MAX_INTEGER("2147483647"),
        MIN_INTEGER("-2147483648"),
        MAX_LONG("9223372036854775807"),
        MIN_LONG("-9223372036854775808"),
        GREATER_THAN_MAX_LONG("9223372036854775808"),
        LESS_THAN_MIN_LONG("-9223372036854775809"),

        PI("3.14"),
        MINUS_PI("-3.14"),

        TENTH("0.1"),
        MINUS_TENTH("-0.1"),

        HUNDREDTH("0.01"),
        MINUS_HUNDREDTH("-0.01"),

        ONE_WITH_FRACTIONLAL_PART("1.0"),
        MINS_ONE_WITH_FRACTIONLAL_PART("-1.0"),

        TEN_WITH_FRACTIONLAL_PART("10.0"),
        MINS_TEN_WITH_FRACTIONLAL_PART("-10.0"),

        HUNDRED_BY_SCIENTIFIC_NOTATION("1e+2"),
        HUNDRED_BY_SCIENTIFIC_NOTATION_CAPITAL("1E+2"),

        MINUS_HUNDRED_BY_SCIENTIFIC_NOTATION("-1e+2"),
        MINUS_HUNDRED_BY_SCIENTIFIC_NOTATION_CAPITAL("-1E+2"),

        TENTH_BY_SCIENTIFIC_NOTATION("1e-1"),
        HUNDREDTH_BY_SCIENTIFIC_NOTATION("1e-2"),
        ;

        private final String json;
        private final BigDecimal value;

        private BigDecimalFixture(String json) {
            this.json = json;
            this.value = new BigDecimal(json);
        }
    }

    @ParameterizedTest
    @EnumSource(BigDecimalFixture.class)
    public void getBigDecimalShouldReturnExpected(BigDecimalFixture fixture) {
        JsonParser parser = factory.createParser(new StringReader(fixture.json));

        parser.next();
        BigDecimal actual = parser.getBigDecimal();
        parser.close();

        assertThat(actual).isEqualTo(fixture.value);
    }

    @ParameterizedTest
    @EnumSource(JsonFixture.class)
    public void getValueShouldReturnValueAsExpected(JsonFixture fixture) {
        JsonParser parser = factory.createParser(new StringReader(fixture.getJson()));

        parser.next();
        JsonValue actual = parser.getValue();
        parser.close();

        assertThat(actual).isEqualTo(fixture.getValue());
    }

    static enum IntegralFixture {
        ZERO("0", true),
        MINUS_ZERO("-0", true),
        ONE("1", true),
        MINUS_ONE("-1", true),
        TEN("10", true),
        MINUS_TEN("-10", true),
        MAX_INTEGER("2147483647", true),
        MIN_INTEGER("-2147483648", true),
        MAX_LONG("9223372036854775807", true),
        MIN_LONG("-9223372036854775808", true),
        GREATER_THAN_MAX_LONG("9223372036854775808", true),
        LESS_THAN_MIN_LONG("-9223372036854775809", true),

        PI("3.14", false),
        MINUS_PI("-3.14", false),

        TENTH("0.1", false),
        MINUS_TENTH("-0.1", false),

        HUNDREDTH("0.01", false),
        MINUS_HUNDREDTH("-0.01", false),

        ONE_WITH_FRACTIONLAL_PART("1.0", false),
        MINS_ONE_WITH_FRACTIONLAL_PART("-1.0", false),

        TEN_WITH_FRACTIONLAL_PART("10.0", false),
        MINS_TEN_WITH_FRACTIONLAL_PART("-10.0", false),

        HUNDRED_BY_SCIENTIFIC_NOTATION("1e+2", true),
        HUNDRED_BY_SCIENTIFIC_NOTATION_CAPITAL("1E+2", true),

        MINUS_HUNDRED_BY_SCIENTIFIC_NOTATION("-1e+2", true),
        MINUS_HUNDRED_BY_SCIENTIFIC_NOTATION_CAPITAL("-1E+2", true),

        TENTH_BY_SCIENTIFIC_NOTATION("1e-1", false),
        HUNDREDTH_BY_SCIENTIFIC_NOTATION("1e-2", false),
        ;

        final String json;
        final boolean isIntegral;

        IntegralFixture(String json, boolean isIntegral) {
            this.json = json;
            this.isIntegral = isIntegral;
        }
    }

    @ParameterizedTest
    @EnumSource(IntegralFixture.class)
    public void isIntegralNumberShouldReturnAsExpected(IntegralFixture fixture) {
        JsonParser parser = factory.createParser(new StringReader(fixture.json));

        parser.next();
        boolean actual = parser.isIntegralNumber();
        parser.close();

        assertThat(actual).isEqualTo(fixture.isIntegral);
    }

    static enum IntFixture {
        ZERO("0", 0),
        MINUS_ZERO("-0", 0),
        ONE("1", 1),
        MINUS_ONE("-1", -1),
        TEN("10", 10),
        MINUS_TEN("-10", -10),
        HUNDRED("100", 100),
        MINUS_HUNDRED("-100", -100),
        THOUNSAND("1000", 1000),
        MINUS_THOUNSAND("-1000", -1000),
        HOURS_PER_DAY("24", 24),
        MINUS_HOURS_PER_DAY("-24", -24),
        DAYS_PER_YEAR("365", 365),
        MINUS_DAYS_PER_YEAR("-365", -365),

        MAX_INTEGER("2147483647", 2147483647),
        MIN_INTEGER("-2147483648", -2147483648),
        GREATER_THAN_MAX_INTEGER("2147483648", -2147483648),
        LESS_THAN_MIN_INTEGER("-2147483649", 2147483647),

        ONE_WITH_SCIENTIFIC_NOTATION("1e+0", 1),
        ONE_WITH_SCIENTIFIC_NOTATION_CAPITAL("1E+0", 1),
        MINUS_ONE_WITH_SCIENTIFIC_NOTATION("-1e+0", -1),
        MINUS_ONE_WITH_SCIENTIFIC_NOTATION_CAPITAL("-1E+0", -1),
        TEN_WITH_SCIENTIFIC_NOTATION("1e+1", 10),
        TEN_WITH_SCIENTIFIC_NOTATION_CAPITAL("1E+1", 10),
        MINUS_TEN_WITH_SCIENTIFIC_NOTATION("-1e+1", -10),
        MINUS_TEN_WITH_SCIENTIFIC_NOTATION_CAPITAL("-1E+1", -10),
        HUNDRED_WITH_SCIENTIFIC_NOTATION("1e+2", 100),
        HUNDRED_WITH_SCIENTIFIC_NOTATION_CAPITAL("1E+2", 100),
        MINUS_HUNDRED_WITH_SCIENTIFIC_NOTATION("-1e+2", -100),
        MINUS_HUNDRED_WITH_SCIENTIFIC_NOTATION_CAPITAL("-1E+2", -100),
        ;

        final String json;
        final int intValue;

        private IntFixture(String json, int value) {
            this.json = json;
            this.intValue = value;
        }
    }

    @ParameterizedTest
    @EnumSource(IntFixture.class)
    public void getIntShouldReturnIntegerAsExpected(IntFixture fixture) {
        JsonParser parser = factory.createParser(new StringReader(fixture.json));

        parser.next();
        int actual = parser.getInt();
        parser.close();

        assertThat(actual).isEqualTo(fixture.intValue);
    }

    static enum LongFixture {
        ZERO("0", 0),
        MINUS_ZERO("-0", 0),
        ONE("1", 1),
        MINUS_ONE("-1", -1),
        TEN("10", 10),
        MINUS_TEN("-10", -10),
        HUNDRED("100", 100),
        MINUS_HUNDRED("-100", -100),
        THOUNSAND("1000", 1000),
        MINUS_THOUNSAND("-1000", -1000),
        HOURS_PER_DAY("24", 24),
        MINUS_HOURS_PER_DAY("-24", -24),
        DAYS_PER_YEAR("365", 365),
        MINUS_DAYS_PER_YEAR("-365", -365),

        MAX_INTEGER("2147483647", 2147483647),
        MIN_INTEGER("-2147483648", -2147483648),
        GREATER_THAN_MAX_INTEGER("2147483648", 2147483648L),
        LESS_THAN_MIN_INTEGER("-2147483649", -2147483649L),

        MAX_LONG("9223372036854775807", 9223372036854775807L),
        MIN_LONG("-9223372036854775808", -9223372036854775808L),
        GREATER_THAN_MAX_LONG("9223372036854775808", -9223372036854775808L),
        LESS_THAN_MIN_LONG("-9223372036854775809", 9223372036854775807L),

        ONE_WITH_SCIENTIFIC_NOTATION("1e+0", 1),
        ONE_WITH_SCIENTIFIC_NOTATION_CAPITAL("1E+0", 1),
        MINUS_ONE_WITH_SCIENTIFIC_NOTATION("-1e+0", -1),
        MINUS_ONE_WITH_SCIENTIFIC_NOTATION_CAPITAL("-1E+0", -1),
        TEN_WITH_SCIENTIFIC_NOTATION("1e+1", 10),
        TEN_WITH_SCIENTIFIC_NOTATION_CAPITAL("1E+1", 10),
        MINUS_TEN_WITH_SCIENTIFIC_NOTATION("-1e+1", -10),
        MINUS_TEN_WITH_SCIENTIFIC_NOTATION_CAPITAL("-1E+1", -10),
        HUNDRED_WITH_SCIENTIFIC_NOTATION("1e+2", 100),
        HUNDRED_WITH_SCIENTIFIC_NOTATION_CAPITAL("1E+2", 100),
        MINUS_HUNDRED_WITH_SCIENTIFIC_NOTATION("-1e+2", -100),
        MINUS_HUNDRED_WITH_SCIENTIFIC_NOTATION_CAPITAL("-1E+2", -100),
        ;

        final String json;
        final long longValue;

        private LongFixture(String json, long value) {
            this.json = json;
            this.longValue = value;
        }
    }

    @ParameterizedTest
    @EnumSource(LongFixture.class)
    public void getLongShouldReturnLongAsExpected(LongFixture fixture) {
        JsonParser parser = factory.createParser(new StringReader(fixture.json));

        parser.next();
        long actual = parser.getLong();
        parser.close();

        assertThat(actual).isEqualTo(fixture.longValue);
    }
}