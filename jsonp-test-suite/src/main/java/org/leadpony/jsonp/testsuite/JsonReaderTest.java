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
import java.util.HashMap;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonReaderFactory;
import javax.json.JsonValue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * A test type to test {@link JsonReader}.
 *
 * @author leadpony
 */
public class JsonReaderTest {

    private static JsonReaderFactory factory;

    @BeforeAll
    public static void setUpOnce() {
        factory = Json.createReaderFactory(null);
    }

    @Test
    public void readArrayShouldReadEmptyArray() {
        String json ="[]";

        JsonReader reader = factory.createReader(new StringReader(json));
        JsonArray actual = reader.readArray();
        reader.close();

        assertThat(actual.isEmpty());
    }

    @Test
    public void readArrayShouldReadSimpleArray() {
        String json ="[true,false,null,365,\"hello\"]";

        JsonReader reader = factory.createReader(new StringReader(json));
        JsonArray actual = reader.readArray();
        reader.close();

        assertThat(actual).containsExactly(
                JsonValue.TRUE,
                JsonValue.FALSE,
                JsonValue.NULL,
                Json.createValue(365),
                Json.createValue("hello")
                );
    }

    @Test
    public void readObjectShouldReadEmptyObject() {
        String json ="{}";

        JsonReader reader = factory.createReader(new StringReader(json));
        JsonObject actual = reader.readObject();
        reader.close();

        assertThat(actual.isEmpty());
    }

    @Test
    public void readObjectShouldReadSimpleObject() {
        String json ="{\"a\":true,\"b\":false,\"c\":null,\"d\":365,\"e\":\"hello\"}";

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
}
