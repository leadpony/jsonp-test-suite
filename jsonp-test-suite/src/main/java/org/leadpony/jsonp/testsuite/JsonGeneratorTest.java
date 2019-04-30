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
import java.util.stream.Stream;

import javax.json.Json;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonGeneratorFactory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

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

    private static final Arguments[] booleanFixtures = new Arguments[] {
            fixture(true, "true"),
            fixture(false, "false"),
    };

    public static Stream<Arguments> booleanFixtures() {
        return Stream.of(booleanFixtures);
    }

    @ParameterizedTest
    @MethodSource("booleanFixtures")
    public void writeShouldWriteBoolean(boolean value, String expected) {

        String actual = generate(g->{
            g.write(value);
        });

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void writeNullShouldWriteNull() {

        String actual = generate(g->{
            g.writeNull();
        });

        assertThat(actual).isEqualTo("null");
    }

    private static final Arguments[] stringFixtures = new Arguments[] {
            fixture("hello", "\"hello\""),
            fixture("", "\"\""),
    };

    public static Stream<Arguments> stringFixtures() {
        return Stream.of(stringFixtures);
    }

    @ParameterizedTest
    @MethodSource("stringFixtures")
    public void writeShouldWriteString(String value, String expected) {

        String actual = generate(g->{
            g.write(value);
        });

        assertThat(actual).isEqualTo(expected);
    }

    private static final Arguments[] intFixtures = new Arguments[] {
            fixture(0, "0"),
            fixture(1, "1"),
            fixture(-1, "-1"),
            fixture(24, "24"),
            fixture(365, "365"),
            fixture(2147483647, "2147483647"),
            fixture(-2147483648, "-2147483648"),
    };

    public static Stream<Arguments> intFixtures() {
        return Stream.of(intFixtures);
    }

    @ParameterizedTest
    @MethodSource("intFixtures")
    public void writeShouldWriteInteger(int value, String expected) {

        String actual = generate(g->{
            g.write(value);
        });

        assertThat(actual).isEqualTo(expected);
    }

    private static final Arguments[] longFixtures = new Arguments[] {
            fixture(0L, "0"),
            fixture(1L, "1"),
            fixture(-1L, "-1"),
            fixture(24L, "24"),
            fixture(365L, "365"),
            fixture(9223372036854775807L, "9223372036854775807"),
            fixture(-9223372036854775808L, "-9223372036854775808"),
    };

    public static Stream<Arguments> longFixtures() {
        return Stream.of(longFixtures);
    }

    @ParameterizedTest
    @MethodSource("longFixtures")
    public void writeShouldWriteLong(long value, String expected) {

        String actual = generate(g->{
            g.write(value);
        });

        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @EnumSource(JsonValueSample.class)
    public void writeShouldWriteJsonValue(JsonValueSample fixture) {

        String actual = generate(g->{
            g.write(fixture.asJsonValue());
        });

        assertThat(actual).isEqualTo(fixture.asString());
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

    private static Arguments fixture(Object... args) {
        return Arguments.of(args);
    }
}
