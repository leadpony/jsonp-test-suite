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

import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonReader;
import javax.json.JsonReaderFactory;
import javax.json.JsonValue;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonGeneratorFactory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.leadpony.jsonp.testsuite.helper.Ambiguous;

/**
 * A test type for testing pretty printing in {@link JsonGenerator}.
 *
 * @author leadpony
 */
@Ambiguous
public class JsonGeneratorPrettyPrintTest {

    private static JsonReaderFactory readerFactory;
    private static JsonGeneratorFactory generatorFactory;

    @BeforeAll
    public static void setUpOnce() {
        readerFactory = Json.createReaderFactory(null);

        Map<String, Object> config = new HashMap<>();
        config.put(JsonGenerator.PRETTY_PRINTING, Boolean.TRUE);
        generatorFactory = Json.createGeneratorFactory(config);
    }

    @ParameterizedTest
    @EnumSource(JsonResource.class)
    public void writeShouldGeneratePrettyPrintedJson(JsonResource test) {
        JsonValue value = readValue(test);
        StringWriter writer = new StringWriter();
        try (JsonGenerator g = generatorFactory.createGenerator(writer)) {
            g.write(value);
        }

        String actual = writer.toString().trim();
        String expected = test.getJsonIndentedWithSpacesAsString(4);
        assertThat(actual).isEqualTo(expected);
    }

    private static JsonValue readValue(JsonResource test) {
        try (JsonReader reader = readerFactory.createReader(test.openStream(), StandardCharsets.UTF_8)) {
            return reader.readValue();
        }
    }
}
