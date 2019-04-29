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

import java.util.stream.Stream;

import javax.json.Json;
import javax.json.JsonString;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * A test type to test {@link JsonString}.
 *
 * @author leadpony
 */
public class JsonStringTest {

    private static Arguments[] toStringFixtures = new Arguments[] {
            fixture("hello", "\"hello\""),
            // empty string
            fixture("", "\"\""),
            // blank string
            fixture(" ", "\" \""),
            // contains space
            fixture("foo bar", "\"foo bar\""),
            // contains quotation mark
            fixture("foo\"bar", "\"foo\\\"bar\""),
            // contains reverse solidus
            fixture("foo\\bar", "\"foo\\\\bar\""),
    };

    public static Stream<Arguments> toStringFixtures() {
        return Stream.of(toStringFixtures);
    }

    @ParameterizedTest
    @MethodSource("toStringFixtures")
    public void toStringShouldEscapeString(String value, String expected) {
        JsonString sut = Json.createValue(value);

        String actual = sut.toString();

        assertThat(actual).isEqualTo(expected);
    }

    private static Arguments fixture(Object... args) {
        return Arguments.of(args);
    }
}
