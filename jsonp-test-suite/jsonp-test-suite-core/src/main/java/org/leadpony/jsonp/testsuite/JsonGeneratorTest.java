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

import java.io.StringWriter;
import java.util.function.Consumer;

import javax.json.Json;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonGeneratorFactory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

/**
 * A test type to test {@link JsonGenerator}.
 *
 * @author leadpony
 */
public class JsonGeneratorTest {

    private static JsonGeneratorFactory factory;

    @BeforeAll
    public static void setUpOnce() {
        factory = Json.createGeneratorFactory(null);
    }

    static enum BooleanFixture {
        TRUE(true, "true"),
        FALSE(false, "false")
        ;

        final boolean value;
        final String expected;

        private BooleanFixture(boolean value, String expected) {
            this.value = value;
            this.expected = expected;
        }
    }

    @ParameterizedTest
    @EnumSource(BooleanFixture.class)
    public void writeShouldWriteBoolean(BooleanFixture fixture) {

        String actual = generate(g->{
            g.write(fixture.value);
        });

        assertThat(actual).isEqualTo(fixture.expected);
    }

    @Test
    public void writeNullShouldWriteNull() {

        String actual = generate(g->{
            g.writeNull();
        });

        assertThat(actual).isEqualTo("null");
    }

    static enum StringFixture {
        EMPTY_STRING("", "\"\""),
        BLANK_STRING(" ", "\" \""),
        SINGLE_WORD("hello", "\"hello\""),
        NULL("null", "\"null\""),
        INTEGER("42", "\"42\""),
        NUMBER("3.14", "\"3.14\""),
        CONTAINING_SPACE("hello world", "\"hello world\""),
        ;

        final String value;
        final String expected;

        private StringFixture(String value, String expected) {
            this.value = value;
            this.expected = expected;
        }
    }

    @ParameterizedTest
    @EnumSource(StringFixture.class)
    public void writeShouldWriteString(StringFixture fixture) {

        String actual = generate(g->{
            g.write(fixture.value);
        });

        assertThat(actual).isEqualTo(fixture.expected);
    }

    static enum IntFixture {
        ZERO(0, "0"),
        ONE(1, "1"),
        MINUS_ONE(-1, "-1"),
        TEN(10, "10"),
        MINUS_TEN(-10, "-10"),
        HUNDRED(100, "100"),
        MINUS_HUNDRED(-100, "-100"),
        THOUSAND(1000, "1000"),
        MINUS_THOUSAND(-1000, "-1000"),
        HOURS_PER_DAY(24, "24"),
        DAYS_PER_YEAR(365, "365"),
        MINUS_HOURS_PER_DAY(-24, "-24"),
        MINUS_DAYS_PER_YEAR(-365, "-365"),
        MAX_INTEGER(Integer.MAX_VALUE, "2147483647"),
        MIN_INTEGER(Integer.MIN_VALUE, "-2147483648"),
        ;

        final int value;
        final String expected;

        private IntFixture(int value, String expected) {
            this.value = value;
            this.expected = expected;
        }
    }

    @ParameterizedTest
    @EnumSource(IntFixture.class)
    public void writeShouldWriteInteger(IntFixture fixture) {

        String actual = generate(g->{
            g.write(fixture.value);
        });

        assertThat(actual).isEqualTo(fixture.expected);
    }

    static enum LongFixture {
        ZERO(0L, "0"),
        ONE(1L, "1"),
        MINUS_ONE(-1L, "-1"),
        TEN(10L, "10"),
        MINUS_TEN(-10L, "-10"),
        HUNDRED(100L, "100"),
        MINUS_HUNDRED(-100L, "-100"),
        THOUSAND(1000L, "1000"),
        MINUS_THOUSAND(-1000L, "-1000"),
        HOURS_PER_DAY(24L, "24"),
        DAYS_PER_YEAR(365L, "365"),
        MINUS_HOURS_PER_DAY(-24L, "-24"),
        MINUS_DAYS_PER_YEAR(-365L, "-365"),
        MAX_INTEGER(Long.MAX_VALUE, "9223372036854775807"),
        MIN_INTEGER(Long.MIN_VALUE, "-9223372036854775808"),
        ;

        final long value;
        final String expected;

        private LongFixture(long value, String expected) {
            this.value = value;
            this.expected = expected;
        }
    }

    @ParameterizedTest
    @EnumSource(LongFixture.class)
    public void writeShouldWriteLong(LongFixture fixture) {

        String actual = generate(g->{
            g.write(fixture.value);
        });

        assertThat(actual).isEqualTo(fixture.expected);
    }

    @ParameterizedTest
    @EnumSource(JsonValueFixture.class)
    public void writeShouldWriteJsonValue(JsonValueFixture fixture) {

        String actual = generate(g->{
            g.write(fixture.getJsonValue());
        });

        assertThat(actual).isEqualTo(fixture.getString());
    }

    @Test
    public void generatorShouldWriteEmptyArray() {

        String actual = generate(g->{
            g.writeStartArray();
            g.writeEnd();
            g.flush();
        });

        assertThat(actual).isEqualTo("[]");
    }

    @Test
    public void generatorShouldWriteSingleItemArray() {

        String actual = generate(g->{
            g.writeStartArray();
            g.write(true);
            g.writeEnd();
            g.flush();
        });

        assertThat(actual).isEqualTo("[true]");
    }

    @Test
    public void generatorShouldWriteMultipleItemsArray() {

        String actual = generate(g->{
            g.writeStartArray();
            g.write(true);
            g.write(false);
            g.writeEnd();
        });

        assertThat(actual).isEqualTo("[true,false]");
    }

    @Test
    public void generatorShouldWriteArrayContainingArray() {

        String actual = generate(g->{
            g.writeStartArray();
            g.writeStartArray();
            g.write(1);
            g.write(2);
            g.writeEnd();
            g.writeStartArray();
            g.write(3);
            g.write(4);
            g.writeEnd();
            g.writeEnd();
        });

        assertThat(actual).isEqualTo("[[1,2],[3,4]]");
    }

    @Test
    public void generatorShouldWriteArrayContainingObject() {

        String actual = generate(g->{
            g.writeStartArray();
            g.writeStartObject();
            g.write("a", 1);
            g.write("b", 2);
            g.writeEnd();
            g.writeStartObject();
            g.write("c", 3);
            g.write("d", 4);
            g.writeEnd();
            g.writeEnd();
        });

        assertThat(actual).isEqualTo("[{\"a\":1,\"b\":2},{\"c\":3,\"d\":4}]");
    }

    @Test
    public void generatorShouldWriteEmptyObject() {

        String actual = generate(g->{
            g.writeStartObject();
            g.writeEnd();
        });

        assertThat(actual).isEqualTo("{}");
    }

    @Test
    public void generatorShouldWriteSinglePropertyObject() {

        String actual = generate(g->{
            g.writeStartObject();
            g.write("a", 365);
            g.writeEnd();
        });

        assertThat(actual).isEqualTo("{\"a\":365}");
    }

    @Test
    public void generatorShouldWriteMultiplePropertiesObject() {

        String actual = generate(g->{
            g.writeStartObject();
            g.write("a", 365);
            g.write("b", "hello");
            g.writeEnd();
        });

        assertThat(actual).isEqualTo("{\"a\":365,\"b\":\"hello\"}");
    }

    @Test
    public void generatorShouldWriteObjectContainingArray() {

        String actual = generate(g->{
            g.writeStartObject();
            g.writeStartArray("a");
            g.write(1);
            g.write(2);
            g.writeEnd();
            g.writeStartArray("b");
            g.write(3);
            g.write(4);
            g.writeEnd();
            g.writeEnd();
        });

        assertThat(actual).isEqualTo(
                "{\"a\":[1,2],\"b\":[3,4]}");
    }

    @Test
    public void generatorShouldWriteObjectContainingObject() {

        String actual = generate(g->{
            g.writeStartObject();
            g.writeStartObject("a");
            g.write("a1", 1);
            g.write("a2", 2);
            g.writeEnd();
            g.writeStartObject("b");
            g.write("b1", 3);
            g.write("b2", 4);
            g.writeEnd();
            g.writeEnd();
        });

        assertThat(actual).isEqualTo(
                "{\"a\":{\"a1\":1,\"a2\":2},\"b\":{\"b1\":3,\"b2\":4}}");
    }

    private String generate(Consumer<JsonGenerator> consumer) {
        StringWriter writer = new StringWriter();
        try (JsonGenerator g = factory.createGenerator(writer)) {
            consumer.accept(g);
        }
        return writer.toString();
    }
}
