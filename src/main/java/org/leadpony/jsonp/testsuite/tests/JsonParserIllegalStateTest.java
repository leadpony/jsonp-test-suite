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

import java.io.StringReader;
import java.util.function.Consumer;
import java.util.logging.Logger;

import javax.json.Json;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParserFactory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.leadpony.jsonp.testsuite.helper.JsonSupplier;
import org.leadpony.jsonp.testsuite.helper.LogHelper;

/**
 * @author leadpony
 */
public class JsonParserIllegalStateTest {

    private static final Logger LOG = LogHelper.getLogger(JsonParserIllegalStateTest.class);

    private static JsonParserFactory parserFactory;

    @BeforeAll
    public static void setUpOnce() {
        parserFactory = Json.createParserFactory(null);
    }

    /**
     * Tests cases for retrieving string value.
     *
     * @author leadpony
     */
    enum IllegalStringRetrievalTestCase implements JsonSupplier {
        EMPTY("", 0),

        TRUE("true", 1),
        FALSE("false", 1),
        NULL("null", 1),

        ARRAY_OPENING("[]", 1),
        ARRAY_CLOSING("[]", 2),

        OBJECT_OPENING("{}", 1),
        OBJECT_CLOSING("{}", 2);

        private final String json;
        final int iterations;

        IllegalStringRetrievalTestCase(String json, int iterations) {
            this.json = json;
            this.iterations = iterations;
        }

        public String getJson() {
            return json;
        }
    }

    @ParameterizedTest
    @EnumSource(IllegalStringRetrievalTestCase.class)
    public void getStringShouldThrowIllegalStateException(IllegalStringRetrievalTestCase test) {
        Throwable thrown = doIllegalCall(
                test.getJson(), test.iterations,
                JsonParser::getString);

        assertThat(thrown).isInstanceOf(IllegalStateException.class);

        LOG.info(thrown.getMessage());
    }

    /**
     * Tests cases for retrieving numeric value.
     *
     * @author leadpony
     */
    enum IllegalNumberRetrievalTestCase implements JsonSupplier {
        EMPTY("", 0),

        TRUE("true", 1),
        FALSE("false", 1),
        NULL("null", 1),

        STRING("\"hello\"", 1),

        ARRAY_OPENING("[]", 1),
        ARRAY_CLOSING("[]", 2),

        OBJECT_OPENING("{}", 1),
        OBJECT_CLOSING("{}", 2);

        private final String json;
        final int iterations;

        IllegalNumberRetrievalTestCase(String json, int iterations) {
            this.json = json;
            this.iterations = iterations;
        }

        public String getJson() {
            return json;
        }
    }

    @ParameterizedTest
    @EnumSource(IllegalNumberRetrievalTestCase.class)
    public void getBigDecimalShouldThrowIllegalStateException(IllegalNumberRetrievalTestCase test) {
        Throwable thrown = doIllegalCall(
                test.getJson(), test.iterations,
                JsonParser::getBigDecimal);

        assertThat(thrown).isInstanceOf(IllegalStateException.class);

        LOG.info(thrown.getMessage());
    }

    @ParameterizedTest
    @EnumSource(IllegalNumberRetrievalTestCase.class)
    public void isIntegralNumberShouldThrowIllegalStateException(IllegalNumberRetrievalTestCase test) {
        Throwable thrown = doIllegalCall(
                test.getJson(), test.iterations,
                JsonParser::isIntegralNumber);

        assertThat(thrown).isInstanceOf(IllegalStateException.class);

        LOG.info(thrown.getMessage());
    }

    @ParameterizedTest
    @EnumSource(IllegalNumberRetrievalTestCase.class)
    public void getIntNumberShouldThrowIllegalStateException(IllegalNumberRetrievalTestCase test) {
        Throwable thrown = doIllegalCall(
                test.getJson(), test.iterations,
                JsonParser::getInt);

        assertThat(thrown).isInstanceOf(IllegalStateException.class);

        LOG.info(thrown.getMessage());
    }

    @ParameterizedTest
    @EnumSource(IllegalNumberRetrievalTestCase.class)
    public void getLongNumberShouldThrowIllegalStateException(IllegalNumberRetrievalTestCase test) {
        Throwable thrown = doIllegalCall(
                test.getJson(), test.iterations,
                JsonParser::getLong);

        assertThat(thrown).isInstanceOf(IllegalStateException.class);

        LOG.info(thrown.getMessage());
    }

    /**
     * Test cases for retrieving JSON value.
     *
     * @author leadpony
     */
    enum IllegalValueRetrievalTestCase implements JsonSupplier {
        EMPTY("", 0),
        ARRAY_CLOSING("[]", 2),
        OBJECT_CLOSING("{}", 2);

        private final String json;
        final int iterations;

        IllegalValueRetrievalTestCase(String json, int iterations) {
            this.json = json;
            this.iterations = iterations;
        }

        public String getJson() {
            return json;
        }
    }

    @ParameterizedTest
    @EnumSource(IllegalValueRetrievalTestCase.class)
    public void getValueShouldThrowIllegalStateException(IllegalValueRetrievalTestCase test) {
        Throwable thrown = doIllegalCall(
                test.getJson(), test.iterations,
                JsonParser::getValue);

        assertThat(thrown).isInstanceOf(IllegalStateException.class);

        LOG.info(thrown.getMessage());
    }

    private Throwable doIllegalCall(String json, int iterations, Consumer<JsonParser> consumer) {
        try (JsonParser parser = createJsonParser(json)) {
            while (iterations-- > 0) {
                parser.next();
            }
            return catchThrowable(() -> {
                consumer.accept(parser);
            });
        }
    }

    private JsonParser createJsonParser(String json) {
        return parserFactory.createParser(new StringReader(json));
    }
}
