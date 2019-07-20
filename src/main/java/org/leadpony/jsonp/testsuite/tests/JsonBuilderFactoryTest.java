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
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

/**
 * @author leadpony
 */
public class JsonBuilderFactoryTest {

    private JsonBuilderFactory factory;

    @BeforeEach
    public void setUp() {
        this.factory = Json.createBuilderFactory(Collections.emptyMap());
    }

    /**
     * @author leadpony
     */
    enum CollectionTestCase {
        EMPTY(Collections.emptyList(), JsonValue.EMPTY_JSON_ARRAY),
        LIST(
            Arrays.asList("hello", 365, 3.14, true, null),
            array(b -> b.add("hello").add(365).add(3.14).add(true).addNull()));

        final Collection<?> collection;
        final JsonArray expected;

        CollectionTestCase(Collection<?> collection, JsonArray expected) {
            this.collection = collection;
            this.expected = expected;
        }
    }

    @ParameterizedTest
    @EnumSource(CollectionTestCase.class)
    public void createArrayBuilderShouldCreateBuilderFilledWithCollection(CollectionTestCase test) {
        try {
            JsonArrayBuilder builder = factory.createArrayBuilder(test.collection);
            JsonArray actual = builder.build();

            assertThat(actual).isEqualTo(test.expected);
        } catch (Exception e) {
            fail(e);
        }
    }

    /**
     * @author leadpony
     */
    @SuppressWarnings("serial")
    enum MapTestCase {
        EMPTY(Collections.emptyMap(), JsonValue.EMPTY_JSON_OBJECT),
        MAP(new LinkedHashMap<String, Object>() {{
                put("a", "hello");
                put("b", 365);
                put("c", 3.14);
                put("d", true);
                put("e", null);
            }},
            object(b -> {
                b.add("a", "hello")
                 .add("b", 365)
                 .add("c", 3.14)
                 .add("d", true)
                 .addNull("e");
            }));

        final Map<String, Object> map;
        final JsonObject expected;

        MapTestCase(Map<String, Object> map, JsonObject expected) {
            this.map = map;
            this.expected = expected;
        }
    }

    @ParameterizedTest
    @EnumSource(MapTestCase.class)
    public void createArrayBuilderShouldCreateBuilderFilledWithMap(MapTestCase test) {
        JsonObjectBuilder builder = factory.createObjectBuilder(test.map);
        JsonObject actual = builder.build();

        assertThat(actual).isEqualTo(test.expected);
    }

    private static JsonArray array(Consumer<JsonArrayBuilder> consumer) {
        JsonArrayBuilder builder = Json.createArrayBuilder();
        consumer.accept(builder);
        return builder.build();
    }

    private static JsonObject object(Consumer<JsonObjectBuilder> consumer) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        consumer.accept(builder);
        return builder.build();
    }
}
