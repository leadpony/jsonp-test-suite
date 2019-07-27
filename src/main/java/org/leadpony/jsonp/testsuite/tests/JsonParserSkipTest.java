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
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

import java.io.StringReader;
import java.time.Duration;

import javax.json.Json;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParserFactory;
import javax.json.stream.JsonParser.Event;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

/**
 * @author leadpony
 */
public class JsonParserSkipTest {

    private static JsonParserFactory parserFactory;

    @BeforeAll
    public static void setUpOnce() {
        parserFactory = Json.createParserFactory(null);
    }

    /**
     * Test cases for {@code JsonParser#skipArray()}.
     *
     * @author leadpony
     */
    enum SkipArrayTestCase implements JsonSource {
        EMPTY_ARRAY("[]", 1, 3),
        SIMPLE_ARRAY("[1,2,3]", 1, 8),
        ARRAY_IN_ARRAY("[[1,2],[3,4]]", 2, 7, Event.START_ARRAY),
        ARRAY_IN_OBJECT("{\"a\":[1,2],\"b\":[3,4]}", 3, 11, Event.KEY_NAME);

        private final String json;
        final int iterations;
        final int columnNumber;
        final Event event;

        SkipArrayTestCase(String json, int iterations, int columnNumber) {
            this(json, iterations, columnNumber, null);
        }

        SkipArrayTestCase(String json, int iterations, int columnNumber, Event event) {
            this.json = json;
            this.iterations = iterations;
            this.columnNumber = columnNumber;
            this.event = event;
        }

        @Override
        public String getJson() {
            return json;
        }
    }

    @ParameterizedTest
    @EnumSource(SkipArrayTestCase.class)
    public void skipArrayShouldSkipArrayAsExpected(SkipArrayTestCase test) {
        JsonParser parser = createJsonParser(test.getJson());

        int iterations = test.iterations;
        while (iterations-- > 0) {
            parser.next();
        }

        parser.skipArray();

        long column = parser.getLocation().getColumnNumber();
        Event event = parser.hasNext() ? parser.next() : null;

        parser.close();

        assertThat(column).isEqualTo(test.columnNumber);
        assertThat(event).isSameAs(test.event);
    }

    /**
     * @see https://github.com/eclipse-ee4j/jsonp/pull/147
     */
    @Test
    public void skipArrayShouldNotLoopForeverIfNotClosed() {
        String json = "[1,2,3";
        JsonParser parser = createJsonParser(json);
        parser.next();

        assertTimeoutPreemptively(Duration.ofMillis(500), () -> {
            try {
                parser.skipArray();
            } catch (Exception e) {
            }
        });
    }

    /**
     * Test cases for {@code JsonParser#skipObject()}.
     *
     * @author leadpony
     */
    enum SkipObjectTestCase implements JsonSource {
        EMPTY_OBJECT("{}", 1, 3),
        SIMPLE_OBJECT("{\"a\":1,\"b\":2}", 1, 14),
        OBJECT_IN_ARRAY(
            "[{\"a\":1,\"b\":2},{\"c\":3,\"d\":4}]",
            2, 15, Event.START_OBJECT);

        private final String json;
        final int iterations;
        final int columnNumber;
        final Event event;

        SkipObjectTestCase(String json, int iterations, int columnNumber) {
            this(json, iterations, columnNumber, null);
        }

        SkipObjectTestCase(String json, int iterations, int columnNumber, Event event) {
            this.json = json;
            this.iterations = iterations;
            this.columnNumber = columnNumber;
            this.event = event;
        }

        @Override
        public String getJson() {
            return json;
        }
    }

    @ParameterizedTest
    @EnumSource(SkipObjectTestCase.class)
    public void skipObjectShouldSkipArrayAsExpected(SkipObjectTestCase test) {
        JsonParser parser = createJsonParser(test.getJson());

        int iterations = test.iterations;
        while (iterations-- > 0) {
            parser.next();
        }

        parser.skipObject();

        long column = parser.getLocation().getColumnNumber();
        Event event = parser.hasNext() ? parser.next() : null;

        parser.close();

        assertThat(column).isEqualTo(test.columnNumber);
        assertThat(event).isSameAs(test.event);
    }

    /**
     * @see https://github.com/eclipse-ee4j/jsonp/pull/147
     */
    @Test
    public void skipObjectShouldNotLoopForeverIfNotClosed() {
        String json = "{\"a\":1";
        JsonParser parser = createJsonParser(json);
        parser.next();

        assertTimeoutPreemptively(Duration.ofMillis(500), () -> {
            try {
                parser.skipObject();
            } catch (Exception e) {
            }
        });
    }

    private JsonParser createJsonParser(String json) {
        return parserFactory.createParser(new StringReader(json));
    }
}
