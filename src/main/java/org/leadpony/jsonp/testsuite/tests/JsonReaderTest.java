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
import static org.assertj.core.api.Assertions.catchThrowable;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonReaderFactory;
import jakarta.json.JsonStructure;
import jakarta.json.JsonValue;
import jakarta.json.stream.JsonLocation;
import jakarta.json.stream.JsonParsingException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.leadpony.jsonp.testsuite.annotation.Ambiguous;
import org.leadpony.jsonp.testsuite.annotation.JsonExclusive;
import org.leadpony.jsonp.testsuite.helper.LoggerFactory;

/**
 * A test type to test {@link JsonReader}.
 *
 * @author leadpony
 */
public class JsonReaderTest {

    private static final Logger LOG = LoggerFactory.getLogger(JsonReaderTest.class);

    private static JsonReaderFactory factory;

    @BeforeAll
    public static void setUpOnce() {
        factory = Json.createReaderFactory(null);
    }

    /*
     * Tests for readArray()
     */

    @Test
    public void readArrayShouldReadEmptyArray() {
        String json = "[]";

        JsonReader reader = factory.createReader(new StringReader(json));
        JsonArray actual = reader.readArray();
        reader.close();

        assertThat(actual.isEmpty());
    }

    @Test
    public void readArrayShouldReadSimpleArray() {
        String json = "[true,false,null,365,\"hello\"]";

        JsonReader reader = factory.createReader(new StringReader(json));
        JsonArray actual = reader.readArray();
        reader.close();

        assertThat(actual).containsExactly(
                JsonValue.TRUE,
                JsonValue.FALSE,
                JsonValue.NULL,
                Json.createValue(365),
                Json.createValue("hello"));
    }

    @ParameterizedTest
    @MethodSource("org.leadpony.jsonp.testsuite.tests.JsonResource#getArraysAsStream")
    public void readArrayShouldReadArrayAsExpected(JsonResource resource) {
        JsonArray actual;
        try (JsonReader reader = factory.createReader(resource.createReader())) {
            actual = reader.readArray();
        }

        assertThat(actual).isNotNull();
        assertThat(actual.toString()).isEqualTo(resource.getMinifiedJsonAsString());
    }

    @ParameterizedTest
    @ValueSource(strings = {"{}", "\"hello\"", "42", "true", "false", "null"})
    public void readArrayShouldThrowJsonParsingException(String json) {
        JsonReader reader = factory.createReader(new StringReader(json));

        Throwable thrown = catchThrowable(() -> {
            reader.readArray();
        });

        assertThat(thrown).isInstanceOf(JsonParsingException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "[1,2],3",
        "[1,2][3,4]"
    })
    @Ambiguous
    public void readArrayShouldReadArrayIgnoringGarbage(String json) {
        JsonReader reader = factory.createReader(new StringReader(json));
        JsonArray array = reader.readArray();
        assertThat(array).isNotNull();
    }

    /*
     * Tests for readObject()
     */

    @Test
    public void readObjectShouldReadEmptyObject() {
        String json = "{}";

        JsonReader reader = factory.createReader(new StringReader(json));
        JsonObject actual = reader.readObject();
        reader.close();

        assertThat(actual.isEmpty());
    }

    @Test
    public void readObjectShouldReadSimpleObject() {
        String json = "{\"a\":true,\"b\":false,\"c\":null,\"d\":365,\"e\":\"hello\"}";

        Map<String, JsonValue> expected = new HashMap<>();
        expected.put("a", JsonValue.TRUE);
        expected.put("b", JsonValue.FALSE);
        expected.put("c", JsonValue.NULL);
        expected.put("d", Json.createValue(365));
        expected.put("e", Json.createValue("hello"));

        JsonReader reader = factory.createReader(new StringReader(json));
        JsonObject actual = reader.readObject();
        reader.close();

        assertThat(actual).hasSameSizeAs(expected).containsAllEntriesOf(expected);
    }

    @ParameterizedTest
    @MethodSource("org.leadpony.jsonp.testsuite.tests.JsonResource#getObjectsAsStream")
    public void readObjectShouldReadObjectAsExpected(JsonResource resource) {
        JsonObject actual;
        try (JsonReader reader = factory.createReader(resource.createReader())) {
            actual = reader.readObject();
        }

        assertThat(actual).isNotNull();
        assertThat(actual.toString()).isEqualTo(resource.getMinifiedJsonAsString());
    }

    @ParameterizedTest
    @ValueSource(strings = {"[]", "\"hello\"", "42", "true", "false", "null"})
    public void readObjectShouldThrowJsonParsingException(String json) {
        JsonReader reader = factory.createReader(new StringReader(json));

        Throwable thrown = catchThrowable(() -> {
            reader.readObject();
        });

        assertThat(thrown).isInstanceOf(JsonParsingException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "{\"a\":1},2",
        "{\"a\":1}{\"b\":2}"
    })
    @Ambiguous
    public void readObjectShouldReadObjectIgnoringGarbage(String json) {
        JsonReader reader = factory.createReader(new StringReader(json));
        JsonObject object = reader.readObject();
        assertThat(object).isNotNull();
    }

    /*
     * Tests for read()
     */

    @ParameterizedTest
    @MethodSource("org.leadpony.jsonp.testsuite.tests.JsonResource#getStructuresAsStream")
    public void readShouldReadStructureAsExpected(JsonResource resource) {
        JsonStructure actual;
        try (JsonReader reader = factory.createReader(resource.createReader())) {
            actual = reader.read();
        }

        assertThat(actual).isNotNull();
        assertThat(actual.toString()).isEqualTo(resource.getMinifiedJsonAsString());
    }

    @ParameterizedTest
    @ValueSource(strings = {"\"hello\"", "42", "true", "false", "null"})
    public void readShouldThrowJsonParsingException(String json) {
        JsonReader reader = factory.createReader(new StringReader(json));

        Throwable thrown = catchThrowable(() -> {
            reader.read();
        });

        assertThat(thrown).isInstanceOf(JsonParsingException.class);
    }

    @Test
    public void readShouldThrowJsonParsingExceptionIfInputIsEmptyReader() {
        String json = "";
        JsonReader reader = factory.createReader(new StringReader(json));

        Throwable thrown = catchThrowable(() -> {
            reader.read();
        });

        assertThat(thrown).isInstanceOf(JsonParsingException.class);
        LOG.info(thrown.getMessage());
    }

    /*
     * Tests for readValue()
     */

    @ParameterizedTest
    @EnumSource(JsonResource.class)
    public void readValueShouldReadValueAsExpected(JsonResource resource) {
        JsonValue actual;
        try (JsonReader reader = factory.createReader(resource.createReader())) {
            actual = reader.readValue();
        }

        assertThat(actual).isNotNull();
        assertThat(actual.getValueType()).isEqualTo(resource.getType());
        assertThat(actual.toString()).isEqualTo(resource.getMinifiedJsonAsString());
    }

    @ParameterizedTest
    @EnumSource(IllFormedDocumentTestCase.class)
    public void readValueShouldThrowExceptionIfIllFormed(IllFormedDocumentTestCase test) {
        Throwable thrown = catchThrowable(() -> {
            try (JsonReader reader = factory.createReader(
                    new StringReader(test.getJson()))) {
                reader.readValue();
            }
        });

        LOG.info(test.toString());

        assertThat(thrown).isInstanceOf(JsonParsingException.class);
        LOG.info(thrown.getMessage());

        JsonParsingException actual = (JsonParsingException) thrown;
        JsonLocation location = actual.getLocation();
        assertThat(location.getLineNumber()).isEqualTo(test.getLineNumber());
        assertThat(location.getColumnNumber()).isEqualTo(test.getColumnNumber());
        assertThat(location.getStreamOffset()).isEqualTo(test.getStreamOffset());
    }

    @JsonExclusive
    @ParameterizedTest
    @EnumSource(IllFormedJsonTestCase.class)
    public void readValueShouldThrowExceptionIfIllFormed(IllFormedJsonTestCase test) {
        Throwable thrown = catchThrowable(() -> {
            try (JsonReader reader = factory.createReader(
                    new StringReader(test.getJson()))) {
                reader.readValue();
            }
        });

        LOG.info(test.toString());

        assertThat(thrown).isInstanceOf(JsonParsingException.class);
        LOG.info(thrown.getMessage());

        JsonParsingException actual = (JsonParsingException) thrown;
        JsonLocation location = actual.getLocation();
        assertThat(location.getLineNumber()).isEqualTo(test.getLineNumber());
        assertThat(location.getColumnNumber()).isEqualTo(test.getColumnNumber());
        assertThat(location.getStreamOffset()).isEqualTo(test.getStreamOffset());
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "1 2",
        "\"hello\" \"world\"",
        "true false"
    })
    @Ambiguous
    public void readValueShouldReadValueIgnoringGarbage(String json) {
        JsonReader reader = factory.createReader(new StringReader(json));
        JsonValue value = reader.readValue();
        assertThat(value).isNotNull();
    }
}
