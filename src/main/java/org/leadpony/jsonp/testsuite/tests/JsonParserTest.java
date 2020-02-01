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
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.leadpony.jsonp.testsuite.helper.JsonLocations.at;

import java.io.StringReader;
import java.math.BigDecimal;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.logging.Logger;
import java.util.stream.Stream;

import jakarta.json.stream.JsonLocation;
import jakarta.json.stream.JsonParser;
import jakarta.json.stream.JsonParser.Event;
import jakarta.json.Json;
import jakarta.json.JsonValue;
import jakarta.json.stream.JsonParserFactory;
import jakarta.json.stream.JsonParsingException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.leadpony.jsonp.testsuite.annotation.Ambiguous;
import org.leadpony.jsonp.testsuite.helper.JsonLocations;
import org.leadpony.jsonp.testsuite.helper.JsonSupplier;
import org.leadpony.jsonp.testsuite.helper.LoggerFactory;

/**
 * A test type to test {@link JsonParser}.
 *
 * @author leadpony
 */
public class JsonParserTest {

    private static final Logger LOG = LoggerFactory.getLogger(JsonParserTest.class);

    private static JsonParserFactory parserFactory;

    @BeforeAll
    public static void setUpOnce() {
        parserFactory = Json.createParserFactory(null);
    }

    /**
     * Test cases for {@code JsonParser#hasNext()}.
     *
     * @author leadpony
     */
    enum TerminationTestCase {
        LITERAL("365", 1),
        ARRAY("[1,2,3]", 5),
        OBJECT("{\"a\":1}", 4);

        final String json;
        final int iterations;

        TerminationTestCase(String json, int iterations) {
            this.json = json;
            this.iterations = iterations;
        }
    }

    @ParameterizedTest
    @EnumSource(TerminationTestCase.class)
    public void hasNextShouldReturnResult(TerminationTestCase test) {
        JsonParser parser = createJsonParser(test.json);

        int remaining = test.iterations;
        while (remaining-- > 0) {
            assertThat(parser.hasNext()).isTrue();
            parser.next();
        }
        assertThat(parser.hasNext()).isEqualTo(false);
        parser.close();
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "    "})
    @Ambiguous
    public void hasNextShouldReturnFalseIfInputIsBlank(String json) {
        JsonParser parser = createJsonParser(json);
        assertThat(parser.hasNext()).isEqualTo(false);
        parser.close();
    }

    /**
     * @author leadpony
     */
    enum HasNextExceptionTestCase {
        VALUE_AFTER_ARRAY("[1,2] 3", 4),
        VALUE_AFTER_OBJECT("{\"a\":1} 2}", 4),
        VALUE_AFTER_VALUE("1 2", 1),
        END_AFTER_ARRAY_START("[", 1),
        END_AFTER_FIRST_ITEM("[1", 2),
        END_AFTER_SECOND_ITEM("[1,2", 3),
        EOI_AFTER_OBJECT_START("{", 1),
        EOI_AFTER_FIRST_PROPERTY_KEY("{\"a\"", 2),
        EOI_AFTER_FIRST_PROPERTY_VALUE("{\"a\":1", 3),
        EOI_AFTER_SECOND_PROPERTY_KEY("{\"a\":1,\"b\"", 4),
        EOI_AFTER_SECOND_PROPERTY_VALUE("{\"a\":1,\"b\":2", 5),

        END_AFTER_ITEM_SEPARATOR("[1,", 2, true),
        EOI_AFTER_COLON("{\"a\":", 2, true),
        EOI_AFTER_PROPERTY_SEPARATOR("{\"a\":1,", 3, true),

        EMPTY("", 0, false),
        BLANK("    ", 0, false);

        final String json;
        final int iterations;
        final boolean throwing;
        final boolean expected;

        // separator
        HasNextExceptionTestCase(String json, int iterations) {
            this.json = json;
            this.iterations = iterations;
            this.throwing = true;
            this.expected = true;
        }

        HasNextExceptionTestCase(String json, int iterations, boolean expected) {
            this.json = json;
            this.iterations = iterations;
            this.throwing = false;
            this.expected = expected;
        }
    }

    @ParameterizedTest
    @EnumSource(HasNextExceptionTestCase.class)
    public void hasNextShouldThrowJsonParsingException(HasNextExceptionTestCase test) {
        JsonParser parser = createJsonParser(test.json);
        int iterations = test.iterations;
        while (iterations-- > 0) {
            parser.next();
        }

        Throwable thrown = catchThrowable(() -> {
            boolean actual = parser.hasNext();
            assertThat(actual).isEqualTo(test.expected);
        });
        parser.close();

        if (thrown != null) {
            LOG.info(thrown.getMessage());
        }

        if (test.throwing) {
            assertThat(thrown).isNotNull().isInstanceOf(JsonParsingException.class);
        } else {
            assertThat(thrown).isNull();
        }
    }

    /**
     * Test cases for {@code JsonParser#next()}.
     *
     * @author leadpony
     */
    enum ParserEventTestCase {
        TRUE("true", Event.VALUE_TRUE),
        FALSE("false", Event.VALUE_FALSE),
        NULL("null", Event.VALUE_NULL),
        WORD("\"abc\"", Event.VALUE_STRING),
        INTEGER("42", Event.VALUE_NUMBER),
        NUMBER("3.14", Event.VALUE_NUMBER),

        EMPTY_ARRAY("[]",
            Event.START_ARRAY, Event.END_ARRAY),
        ARRAY_OF_ITEM("[42]",
            Event.START_ARRAY, Event.VALUE_NUMBER, Event.END_ARRAY),
        ARRAY_OF_MULTIPLE_ITEMS("[true,false,null,\"abc\",42]",
            Event.START_ARRAY, Event.VALUE_TRUE, Event.VALUE_FALSE,
            Event.VALUE_NULL, Event.VALUE_STRING, Event.VALUE_NUMBER, Event.END_ARRAY),
        ARRAY_OF_ARRAY("[[]]",
            Event.START_ARRAY, Event.START_ARRAY, Event.END_ARRAY, Event.END_ARRAY),
        ARRAY_OF_OBJECT("[{}]",
            Event.START_ARRAY, Event.START_OBJECT, Event.END_OBJECT, Event.END_ARRAY),

        EMPTY_OBJECT("{}",
            Event.START_OBJECT, Event.END_OBJECT),
        OBJECT_OF_SINGLE_PROPERTY("{\"a\":42}",
            Event.START_OBJECT, Event.KEY_NAME, Event.VALUE_NUMBER,
            Event.END_OBJECT),
        OBJECT_OF_MULTIPLE_PROPERTIES("{\"a\":true,\"b\":false,\"c\":null,\"d\":\"abc\",\"e\":42}", Event.START_OBJECT,
            Event.KEY_NAME, Event.VALUE_TRUE, Event.KEY_NAME, Event.VALUE_FALSE, Event.KEY_NAME, Event.VALUE_NULL,
            Event.KEY_NAME, Event.VALUE_STRING, Event.KEY_NAME, Event.VALUE_NUMBER, Event.END_OBJECT),
        OBJECT_OF_ARRAY_PROPERTY("{\"a\":[]}",
            Event.START_OBJECT, Event.KEY_NAME, Event.START_ARRAY, Event.END_ARRAY,
            Event.END_OBJECT),
        OBJECT_OF_OBJECT_PROPERTY("{\"a\":{}}",
            Event.START_OBJECT, Event.KEY_NAME, Event.START_OBJECT,
            Event.END_OBJECT, Event.END_OBJECT);

        private final String json;
        private final Event[] events;

        ParserEventTestCase(String json, Event... events) {
            this.json = json;
            this.events = events;
        }
    };

    @ParameterizedTest
    @EnumSource(ParserEventTestCase.class)
    public void nextShouldReturnEventsAsExpected(ParserEventTestCase test) {
        JsonParser parser = createJsonParser(test.json);
        List<Event> actual = new ArrayList<>();

        while (parser.hasNext()) {
            actual.add(parser.next());
        }
        parser.close();

        assertThat(actual).containsExactly(test.events);
    }

    @Test
    public void nextShouldThrowNoSuchElementExceptionAfterEOI() {
        JsonParser parser = createJsonParser("{}");

        parser.next();
        parser.next();

        Throwable thrown = catchThrowable(() -> {
            parser.next();
        });

        parser.close();

        assertThat(thrown).isInstanceOf(NoSuchElementException.class);

        LOG.info(thrown.getMessage());
    }

    /**
     * Test cases for {@code JsonParser#getString()}.
     *
     * @author leadpony
     */
    enum StringRetrievalTestCase implements JsonSupplier {
        EMPTY_STRING("\"\"", ""),
        BLANK_STRING("\" \"", " "),
        TWO_SPACES("\"  \"", "  "),
        FOUR_SPACES("\"    \"", "    "),
        SINGLE_WORD("\"hello\"", "hello"),

        SENTENCE("\"The quick brown fox jumps over the lazy dog\"",
            "The quick brown fox jumps over the lazy dog"),

        STARTING_WITH_SPACE("\" hello\"", " hello"),
        ENDING_WITH_SPACE("\"hello \"", "hello "),
        CONTAINING_SPACE("\"hello world\"", "hello world"),

        QUOTATION("\"\\\"\"", "\""),
        ESCAPED_SOLIDUS("\"\\/\"", "/"),
        REVERSE_SOLIDUS("\"\\\\\"", "\\"),

        BACKSPACE("\"\\b\"", "\b"),
        FORM_FEED("\"\\f\"", "\f"),
        LINE_FEED("\"\\n\"", "\n"),
        CARIAGE_RETURN("\"\\r\"", "\r"),
        TAB("\"\\t\"", "\t"),

        CONTAINING_QUOTATION("\"hello\\\"world\"", "hello\"world"),
        CONTAINING_ESCAPED_SOLIDUS("\"hello\\/world\"", "hello/world"),
        CONTAINING_REVERSE_SOLIDUS("\"hello\\\\world\"", "hello\\world"),

        CONTAINING_BACKSPACE("\"hello\\bworld\"", "hello\bworld"),
        CONTAINING_FORM_FEED("\"hello\\fworld\"", "hello\fworld"),
        CONTAINING_LINE_FEED("\"hello\\nworld\"", "hello\nworld"),
        CONTAINING_CARIAGE_RETURN("\"hello\\rworld\"", "hello\rworld"),
        CONTAINING_TAB("\"hello\\tworld\"", "hello\tworld"),

        NUL("\"\\u0000\"", "\u0000"),
        INFINITE("\"\\u221E\"", "∞"),
        PI("\"\\u03c0\"", "π"),
        // surrogate pair
        G_CLEF("\"\\ud834\\udd1e\"", "\ud834\udd1e"),

        CONTAINING_INFINITE("\"hello\\u221Eworld\"", "hello∞world"),
        CONTAINING_PI("\"hello\\u03c0world\"", "helloπworld"),
        CONTAINING_G_CLEF("\"hello\\ud834\\udd1eworld\"", "hello\ud834\udd1eworld"),

        INTEGER("42", "42"),
        NEGATIVE_INTEGER("-42", "-42"),
        NUMBER("3.14", "3.14"),
        NEGATIVE_NUMBER("-3.14", "-3.14"),;

        private final String json;
        private final String value;

        StringRetrievalTestCase(String json, String value) {
            this.json = json;
            this.value = value;
        }

        public String getJson() {
            return json;
        }

        boolean isString() {
            return json.startsWith("\"");
        }
    }

    @ParameterizedTest
    @EnumSource(StringRetrievalTestCase.class)
    public void getStringShouldReturnString(StringRetrievalTestCase test) {
        JsonParser parser = createJsonParser(test.getJson());

        parser.next();
        String actual = parser.getString();
        parser.close();

        assertThat(actual).isEqualTo(test.value);
    }

    @ParameterizedTest
    @EnumSource(StringRetrievalTestCase.class)
    public void getStringShouldReturnStringFromItem(StringRetrievalTestCase test) {
        JsonParser parser = createJsonParser(test.getJsonAsArrayItem());

        parser.next(); // '['
        parser.next();
        String actual = parser.getString();
        parser.close();

        assertThat(actual).isEqualTo(test.value);
    }

    public static Stream<StringRetrievalTestCase> getStringShouldReturnStringFromPropertyKey() {
        return Stream.of(StringRetrievalTestCase.values()).filter(StringRetrievalTestCase::isString);
    }

    @ParameterizedTest
    @MethodSource
    public void getStringShouldReturnStringFromPropertyKey(StringRetrievalTestCase test) {
        JsonParser parser = createJsonParser(test.getJsonAsPropertyKey());

        parser.next(); // '{'
        parser.next(); // key name
        String actual = parser.getString();
        parser.close();

        assertThat(actual).isEqualTo(test.value);
    }

    @ParameterizedTest
    @EnumSource(StringRetrievalTestCase.class)
    public void getStringShouldReturnStringFromPropertyValue(StringRetrievalTestCase test) {
        JsonParser parser = createJsonParser(test.getJsonAsPropertyValue());

        parser.next(); // '{'
        parser.next(); // key name
        parser.next();
        String actual = parser.getString();
        parser.close();

        assertThat(actual).isEqualTo(test.value);
    }

    /**
     * Test cases for {@code JsonParser#getBigDecimal()}.
     *
     * @author leadpony
     */
    enum BigDecimalRetrievalTestCase implements JsonSupplier {
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
        MINUS_ONE_WITH_FRACTIONLAL_PART("-1.0"),

        TEN_WITH_FRACTIONLAL_PART("10.0"),
        MINUS_TEN_WITH_FRACTIONLAL_PART("-10.0"),

        HUNDRED_BY_SCIENTIFIC_NOTATION("1e+2"),
        HUNDRED_BY_SCIENTIFIC_NOTATION_CAPITAL("1E+2"),

        MINUS_HUNDRED_BY_SCIENTIFIC_NOTATION("-1e+2"),
        MINUS_HUNDRED_BY_SCIENTIFIC_NOTATION_CAPITAL("-1E+2"),

        TENTH_BY_SCIENTIFIC_NOTATION("1e-1"),
        HUNDREDTH_BY_SCIENTIFIC_NOTATION("1e-2"),

        AVOGADRO_CONSTANT("6.022140857e23"),;

        private final String json;
        private final BigDecimal value;

        BigDecimalRetrievalTestCase(String json) {
            this.json = json;
            this.value = new BigDecimal(json);
        }

        public String getJson() {
            return json;
        }
    }

    @ParameterizedTest
    @EnumSource(BigDecimalRetrievalTestCase.class)
    public void getBigDecimalShouldReturnBigDecimal(BigDecimalRetrievalTestCase test) {
        JsonParser parser = createJsonParser(test.getJson());

        parser.next();
        BigDecimal actual = parser.getBigDecimal();
        parser.close();

        assertThat(actual).isEqualTo(test.value);
    }

    @ParameterizedTest
    @EnumSource(BigDecimalRetrievalTestCase.class)
    public void getBigDecimalShouldReturnBigDecimalFromItem(BigDecimalRetrievalTestCase test) {
        JsonParser parser = createJsonParser(test.getJsonAsArrayItem());

        parser.next(); // '['
        parser.next();
        BigDecimal actual = parser.getBigDecimal();
        parser.close();

        assertThat(actual).isEqualTo(test.value);
    }

    @ParameterizedTest
    @EnumSource(BigDecimalRetrievalTestCase.class)
    public void getBigDecimalShouldReturnBigDecimalFromPropertyValue(BigDecimalRetrievalTestCase test) {
        JsonParser parser = createJsonParser(test.getJsonAsPropertyValue());

        parser.next(); // '{'
        parser.next(); // key name
        parser.next();
        BigDecimal actual = parser.getBigDecimal();
        parser.close();

        assertThat(actual).isEqualTo(test.value);
    }

    /**
     * Test cases for {@code JsonParser#isIntegralNumber()}.
     *
     * @author leadpony
     */
    enum IsIntegralTestCase {
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
        MINUS_ONE_WITH_FRACTIONLAL_PART("-1.0", false),

        TEN_WITH_FRACTIONLAL_PART("10.0", false),
        MINUS_TEN_WITH_FRACTIONLAL_PART("-10.0", false),

        HUNDRED_BY_SCIENTIFIC_NOTATION("1e+2", false),
        HUNDRED_BY_SCIENTIFIC_NOTATION_CAPITAL("1E+2", false),

        MINUS_HUNDRED_BY_SCIENTIFIC_NOTATION("-1e+2", false),
        MINUS_HUNDRED_BY_SCIENTIFIC_NOTATION_CAPITAL("-1E+2", false),

        TENTH_BY_SCIENTIFIC_NOTATION("1e-1", false),
        HUNDREDTH_BY_SCIENTIFIC_NOTATION("1e-2", false),

        AVOGADRO_CONSTANT("6.022140857e23", false),;

        final String json;
        final boolean isIntegral;

        IsIntegralTestCase(String json, boolean isIntegral) {
            this.json = json;
            this.isIntegral = isIntegral;
        }
    }

    @ParameterizedTest
    @EnumSource(IsIntegralTestCase.class)
    public void isIntegralNumberShouldReturnBoolean(IsIntegralTestCase test) {
        JsonParser parser = createJsonParser(test.json);

        parser.next();
        boolean actual = parser.isIntegralNumber();
        parser.close();

        assertThat(actual).isEqualTo(test.isIntegral);
    }

    /**
     * Test cases for {@code JsonParser#getInt()}.
     *
     * @author leadpony
     */
    enum IntRetrievalTestCase implements JsonSupplier {
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
        MINUS_HUNDRED_WITH_SCIENTIFIC_NOTATION_CAPITAL("-1E+2", -100),;

        final String json;
        final int value;

        IntRetrievalTestCase(String json, int value) {
            this.json = json;
            this.value = value;
        }

        public String getJson() {
            return json;
        }
    }

    @ParameterizedTest
    @EnumSource(IntRetrievalTestCase.class)
    public void getIntShouldReturnInt(IntRetrievalTestCase test) {
        JsonParser parser = createJsonParser(test.getJson());

        parser.next();
        int actual = parser.getInt();
        parser.close();

        assertThat(actual).isEqualTo(test.value);
    }

    @ParameterizedTest
    @EnumSource(IntRetrievalTestCase.class)
    public void getIntShouldReturnIntFromItem(IntRetrievalTestCase test) {
        JsonParser parser = createJsonParser(test.getJsonAsArrayItem());

        parser.next(); // '['
        parser.next();
        int actual = parser.getInt();
        parser.close();

        assertThat(actual).isEqualTo(test.value);
    }

    @ParameterizedTest
    @EnumSource(IntRetrievalTestCase.class)
    public void getIntShouldReturnIntFromPropertyValue(IntRetrievalTestCase test) {
        JsonParser parser = createJsonParser(test.getJsonAsPropertyValue());

        parser.next(); // '{'
        parser.next(); // key name
        parser.next();
        int actual = parser.getInt();
        parser.close();

        assertThat(actual).isEqualTo(test.value);
    }

    /**
     * Test cases for {@code JsonParser#getLong()}.
     *
     * @author leadpony
     */
    enum LongRetrievalTestCase implements JsonSupplier {
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
        MINUS_HUNDRED_WITH_SCIENTIFIC_NOTATION_CAPITAL("-1E+2", -100),;

        final String json;
        final long value;

        LongRetrievalTestCase(String json, long value) {
            this.json = json;
            this.value = value;
        }

        public String getJson() {
            return json;
        }
    }

    @ParameterizedTest
    @EnumSource(LongRetrievalTestCase.class)
    public void getLongShouldReturnLong(LongRetrievalTestCase test) {
        JsonParser parser = createJsonParser(test.getJson());

        parser.next();
        long actual = parser.getLong();
        parser.close();

        assertThat(actual).isEqualTo(test.value);
    }

    @ParameterizedTest
    @EnumSource(LongRetrievalTestCase.class)
    public void getLongShouldReturnLongFromItem(LongRetrievalTestCase test) {
        JsonParser parser = createJsonParser(test.getJsonAsArrayItem());

        parser.next(); // '[
        parser.next();
        long actual = parser.getLong();
        parser.close();

        assertThat(actual).isEqualTo(test.value);
    }

    @ParameterizedTest
    @EnumSource(LongRetrievalTestCase.class)
    public void getLongShouldReturnLongFromPropertyValue(LongRetrievalTestCase test) {
        JsonParser parser = createJsonParser(test.getJsonAsPropertyValue());

        parser.next(); // '{'
        parser.next(); // key name
        parser.next();
        long actual = parser.getLong();
        parser.close();

        assertThat(actual).isEqualTo(test.value);
    }

    /**
     * Test cases for {@code JsonParser#getLocation()}.
     *
     * @author leadpony
     */
    enum LocationTestCase {

        SIMPLE_VALUE("42", at(1, 3, 2)),

        ARRAY_IN_ONE_LINE("[\"hello\",42,true]",
            at(1, 2, 1), // [
            at(1, 9, 8), // "hello"
            at(1, 12, 11), // 42
            at(1, 17, 16), // true
            at(1, 18, 17) // ]
        ),

        ARRAY_IN_MULTIPLE_LINES("[\n" + "    \"hello\",\n" + "    42,\n" + "    true\n" + "]",
            at(1, 2, 1), // [
            at(2, 12, 13), // "hello"
            at(3, 7, 21), // 42
            at(4, 9, 31), // true
            at(5, 2, 33) // ]
        ),

        ARRAY_IN_MULTIPLE_LINES_CRLF("[\r\n" + "    \"hello\",\r\n" + "    42,\r\n" + "    true\r\n" + "]",
            at(1, 2, 1), // [
            at(2, 12, 14), // "hello"
            at(3, 7, 23), // 42
            at(4, 9, 34), // true
            at(5, 2, 37) // ]
        ),

        OBJECT_IN_ONE_LINE("{\"first\":\"hello\",\"second\":42}",
            at(1, 2, 1), // {
            at(1, 9, 8), // "first"
            at(1, 17, 16), // "hello"
            at(1, 26, 25), // "second"
            at(1, 29, 28), // 42
            at(1, 30, 29) // }
        ),

        OBJECT_IN_MULTIPLE_LINES("{\n" + "    \"first\":\"hello\",\n" + "    \"second\":42\n" + "}",
            at(1, 2, 1), // {
            at(2, 12, 13), // "first"
            at(2, 20, 21), // "hello"
            at(3, 13, 35), // "second"
            at(3, 16, 38), // 42
            at(4, 2, 40) // }
        ),

        OBJECT_IN_MULTIPLE_LINES_CRLF("{\r\n" + "    \"first\":\"hello\",\r\n" + "    \"second\":42\r\n" + "}",
            at(1, 2, 1), // {
            at(2, 12, 14), // "first"
            at(2, 20, 22), // "hello"
            at(3, 13, 37), // "second"
            at(3, 16, 40), // 42
            at(4, 2, 43) // }
        ),;

        final String json;
        final List<JsonLocation> locations;

        LocationTestCase(String json, JsonLocation... locations) {
            this.json = json;
            this.locations = Arrays.asList(locations);
        }

        JsonLocation getFinalLocation() {
            return locations.get(locations.size() - 1);
        }
    }

    @ParameterizedTest
    @EnumSource(LocationTestCase.class)
    public void getLocationShouldReturnLocations(LocationTestCase test) {
        JsonParser parser = createJsonParser(test.json);

        List<JsonLocation> actual = new ArrayList<>();
        while (parser.hasNext()) {
            parser.next();
            actual.add(parser.getLocation());
        }

        parser.close();

        assertThat(actual).usingElementComparator(JsonLocations.COMPARATOR)
            .containsExactlyElementsOf(test.locations);
    }

    @ParameterizedTest
    @EnumSource(LocationTestCase.class)
    public void getLocationShouldReturnInitialLocation(LocationTestCase test) {
        JsonParser parser = createJsonParser(test.json);
        JsonLocation actual = parser.getLocation();
        parser.close();

        assertThat(actual.getLineNumber()).isEqualTo(1);
        assertThat(actual.getColumnNumber()).isEqualTo(1);
        assertThat(actual.getStreamOffset()).isEqualTo(0);
    }

    @ParameterizedTest
    @EnumSource(LocationTestCase.class)
    @Ambiguous
    public void getLocationShouldReturnFinalLocation(LocationTestCase test) {
        JsonParser parser = createJsonParser(test.json);
        while (parser.hasNext()) {
            parser.next();
        }
        JsonLocation actual = parser.getLocation();
        parser.close();

        assertThat(actual).usingComparator(JsonLocations.COMPARATOR).isEqualTo(test.getFinalLocation());
    }

    @ParameterizedTest
    @EnumSource(JsonTestCase.class)
    public void getValueShouldReturnValue(JsonTestCase test) {
        JsonParser parser = createJsonParser(test.getJson());

        parser.next();
        JsonValue actual = parser.getValue();
        parser.close();

        assertThat(actual).isEqualTo(test.getValue());
    }

    @ParameterizedTest
    @EnumSource(JsonTestCase.class)
    public void getValueShouldReturnValueFromItem(JsonTestCase test) {
        JsonParser parser = createJsonParser(test.getJsonAsArrayItem());

        parser.next(); // '['
        parser.next();
        JsonValue actual = parser.getValue();
        parser.close();

        assertThat(actual).isEqualTo(test.getValue());
    }

    @ParameterizedTest
    @EnumSource(JsonTestCase.class)
    public void getValueShouldReturnValueFromPropertyValue(JsonTestCase test) {
        JsonParser parser = createJsonParser(test.getJsonAsPropertyValue());

        parser.next(); // '{'
        parser.next(); // key name
        parser.next();
        JsonValue actual = parser.getValue();
        parser.close();

        assertThat(actual).isEqualTo(test.getValue());
    }

    /**
     * Test cases for {@code JsonParser#getArrayStream()}.
     *
     * @author leadpony
     */
    enum ArrayStreamTestCase implements JsonSupplier {
        EMPTY_ARRAY("[]"),
        SIMPLE_ARRAY("[42,\"hello\", true,false,null]",
            Json.createValue(42),
            Json.createValue("hello"),
            JsonValue.TRUE,
            JsonValue.FALSE,
            JsonValue.NULL),
        NESTED_ARRAY("[[],{}]",
            JsonValue.EMPTY_JSON_ARRAY,
            JsonValue.EMPTY_JSON_OBJECT);

        private final String json;
        final JsonValue[] values;

        ArrayStreamTestCase(String json, JsonValue... values) {
            this.json = json;
            this.values = values;
        }

        @Override
        public String getJson() {
            return json;
        }
    }

    @ParameterizedTest
    @EnumSource(ArrayStreamTestCase.class)
    public void getArrayStreamShouldReturnsItemsAsStream(ArrayStreamTestCase test) {
        JsonParser parser = createJsonParser(test.getJson());
        parser.next();
        Stream<JsonValue> actual = parser.getArrayStream();

        assertThat(actual).containsExactly(test.values);
        parser.close();
    }

    /**
     * Test cases for {@code JsonParser#getObjectStream()}.
     *
     * @author leadpony
     */
    enum ObjectStreamTestCase implements JsonSupplier {
        EMPTY_OBJECT("{}"),

        SIMPLE_OBJECT("{\"a\":42,\"b\":\"hello\",\"c\":true,\"d\":false,\"e\":null}",
            entry("a", Json.createValue(42)),
            entry("b", Json.createValue("hello")),
            entry("c", JsonValue.TRUE),
            entry("d", JsonValue.FALSE),
            entry("e", JsonValue.NULL)),

        NESTED_OBJECT("{\"a\":[],\"b\":{}}",
            entry("a", JsonValue.EMPTY_JSON_ARRAY),
            entry("b", JsonValue.EMPTY_JSON_OBJECT));

        private final String json;
        final Map.Entry<String, JsonValue>[] values;

        @SafeVarargs
        ObjectStreamTestCase(String json, Map.Entry<String, JsonValue>... values) {
            this.json = json;
            this.values = values;
        }

        @Override
        public String getJson() {
            return json;
        }

        private static Map.Entry<String, JsonValue> entry(String key, JsonValue value) {
            return new AbstractMap.SimpleEntry<String, JsonValue>(key, value);
        }
    }

    @ParameterizedTest
    @EnumSource(ObjectStreamTestCase.class)
    public void getObjectStreamShouldReturnsPropertiesAsStream(ObjectStreamTestCase test) {
        JsonParser parser = createJsonParser(test.getJson());
        parser.next();
        Stream<Map.Entry<String, JsonValue>> actual = parser.getObjectStream();

        assertThat(actual).containsExactly(test.values);
        parser.close();
    }

    /**
     * Test cases for {@code JsonParser#getValueStream()}.
     *
     * @author leadpony
     */
    enum ValueStreamTestCase implements JsonSupplier {
        NUMBER("42", Json.createValue(42)),
        STRING("\"hello\"", Json.createValue("hello")),
        TRUE("true", JsonValue.TRUE),
        FALSE("false", JsonValue.FALSE),
        NULL("null", JsonValue.NULL),
        EMPTY_ARRAY("[]", JsonValue.EMPTY_JSON_ARRAY),
        EMPTY_OBJECT("{}", JsonValue.EMPTY_JSON_OBJECT);

        private final String json;
        final JsonValue[] values;

        ValueStreamTestCase(String json, JsonValue... values) {
            this.json = json;
            this.values = values;
        }

        @Override
        public String getJson() {
            return json;
        }
    }

    @ParameterizedTest
    @EnumSource(ValueStreamTestCase.class)
    public void getValueStreamShouldReturnsValuesAsStream(ValueStreamTestCase test) {
        JsonParser parser = createJsonParser(test.getJson());

        assertThatCode(() -> {
            Stream<JsonValue> actual = parser.getValueStream();
            assertThat(actual).containsExactly(test.values);
        }).doesNotThrowAnyException();

        parser.close();
    }

    private JsonParser createJsonParser(String json) {
        return parserFactory.createParser(new StringReader(json));
    }
}
