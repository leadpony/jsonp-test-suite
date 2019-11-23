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
import java.util.HashMap;
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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.leadpony.jsonp.testsuite.helper.Ambiguous;

/**
 * @author leadpony
 */
public class JsonBuilderFactoryTest {

    /**
     * @author leadpony
     */
    enum CollectionTestCase {
        EMPTY(
            Collections.emptyList(),
            JsonValue.EMPTY_JSON_ARRAY),
        LIST(
            collection("hello", 365, 3.14, true, null),
            array(b -> b.add("hello").add(365).add(3.14).add(true).addNull())),
        MAX_INTEGER(
            collection(Integer.MAX_VALUE),
            array(b -> b.add(Integer.MAX_VALUE))
            ),
        MIN_INTEGER(
            collection(Integer.MIN_VALUE),
            array(b -> b.add(Integer.MIN_VALUE))
            ),
        MAX_LONG(
            collection(Long.MAX_VALUE),
            array(b -> b.add(Long.MAX_VALUE))
            ),
        MIN_LONG(
            collection(Long.MIN_VALUE),
            array(b -> b.add(Long.MIN_VALUE))
            ),
        MAX_DOUBLE(
            collection(Double.MAX_VALUE),
            array(b -> b.add(Double.MAX_VALUE))
            ),
        MIN_DOUBLE(
            collection(Double.MIN_VALUE),
            array(b -> b.add(Double.MIN_VALUE))
            ),
        JSON_VALUE_TRUE(
            collection(JsonValue.TRUE),
            array(b -> b.add(JsonValue.TRUE))
            ),
        JSON_VALUE_FALSE(
            collection(JsonValue.FALSE),
            array(b -> b.add(JsonValue.FALSE))
            ),
        JSON_VALUE_NULL(
            collection(JsonValue.NULL),
            array(b -> b.add(JsonValue.NULL))
            );

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
        JsonBuilderFactory factory = createFactory();
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
    enum AmbiguousCollectionTestCase {
        ARRAY_BUILDER(
            collection(
                "hello",
                createFactory().createArrayBuilder().add(365)
                ),
            array(b -> b.add("hello").add(array(b2 -> b2.add(365))))
            );

        final Collection<?> collection;
        final JsonArray expected;

        AmbiguousCollectionTestCase(Collection<?> collection, JsonArray expected) {
            this.collection = collection;
            this.expected = expected;
        }
    }

    @Ambiguous
    @ParameterizedTest
    @EnumSource(AmbiguousCollectionTestCase.class)
    public void createArrayBuilderShouldCreateBuilderFilledWithCollection(AmbiguousCollectionTestCase test) {
        JsonBuilderFactory factory = createFactory();
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
    enum MapTestCase {
        EMPTY(
            Collections.emptyMap(),
            JsonValue.EMPTY_JSON_OBJECT),
        MAP(
            map(m -> {
                m.put("a", "hello");
                m.put("b", 365);
                m.put("c", 3.14);
                m.put("d", true);
                m.put("e", null);
            }),
            object(b -> {
                b.add("a", "hello")
                 .add("b", 365)
                 .add("c", 3.14)
                 .add("d", true)
                 .addNull("e");
            })),
        MAX_INTEGER(
            map(m -> m.put("a", Integer.MAX_VALUE)),
            object(b -> b.add("a", Integer.MAX_VALUE))
            ),
        MIN_INTEGER(
            map(m -> m.put("a", Integer.MIN_VALUE)),
            object(b -> b.add("a", Integer.MIN_VALUE))
            ),
        MAX_LONG(
            map(m -> m.put("a", Long.MAX_VALUE)),
            object(b -> b.add("a", Long.MAX_VALUE))
            ),
        MIN_LONG(
            map(m -> m.put("a", Long.MIN_VALUE)),
            object(b -> b.add("a", Long.MIN_VALUE))
            ),
        MAX_DOUBLE(
            map(m -> m.put("a", Double.MAX_VALUE)),
            object(b -> b.add("a", Double.MAX_VALUE))
            ),
        MIN_DOUBLE(
            map(m -> m.put("a", Double.MIN_VALUE)),
            object(b -> b.add("a", Double.MIN_VALUE))
            ),
        JSON_VALUE_TRUE(
            map(m -> m.put("a", JsonValue.TRUE)),
            object(b -> b.add("a", JsonValue.TRUE))
            ),
        JSON_VALUE_FALSE(
            map(m -> m.put("a", JsonValue.FALSE)),
            object(b -> b.add("a", JsonValue.FALSE))
            ),
        JSON_VALUE_NULL(
            map(m -> m.put("a", JsonValue.NULL)),
            object(b -> b.add("a", JsonValue.NULL))
            );

        final Map<String, Object> map;
        final JsonObject expected;

        MapTestCase(Map<String, Object> map, JsonObject expected) {
            this.map = map;
            this.expected = expected;
        }
    }

    @ParameterizedTest
    @EnumSource(MapTestCase.class)
    public void createObjectBuilderShouldCreateBuilderFilledWithMap(MapTestCase test) {
        JsonBuilderFactory factory = createFactory();
        try {
            JsonObjectBuilder builder = factory.createObjectBuilder(test.map);
            JsonObject actual = builder.build();

            assertThat(actual).isEqualTo(test.expected);
        } catch (Exception e) {
            fail(e);
        }
    }

    /**
     * @author leadpony
     */
    enum AmbiguousMapTestCase {
        OBJECT_BUILDER(
            map(m -> {
                m.put("a", "hello");
                m.put("b", createFactory().createObjectBuilder().add("x", 365));
            }),
            object(b -> b.add("a", "hello").add("b", object(b2 -> b2.add("x", 365))))
            );

        final Map<String, Object> map;
        final JsonObject expected;

        AmbiguousMapTestCase(Map<String, Object> map, JsonObject expected) {
            this.map = map;
            this.expected = expected;
        }
    }

    @Ambiguous
    @ParameterizedTest
    @EnumSource(AmbiguousMapTestCase.class)
    public void createObjectBuilderShouldCreateBuilderFilledWithMap(AmbiguousMapTestCase test) {
        JsonBuilderFactory factory = createFactory();
        try {
            JsonObjectBuilder builder = factory.createObjectBuilder(test.map);
            JsonObject actual = builder.build();

            assertThat(actual).isEqualTo(test.expected);
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void getConfigInUseShouldReturnEmptyMap() {
        Map<String, Object> config = new HashMap<>();
        JsonBuilderFactory factory = createFactory(config);

        Map<String, ?> actual = factory.getConfigInUse();

        assertThat(actual).isEmpty();
    }

    @Test
    public void getConfigInUseShouldNotContainUnknownProperty() {
        Map<String, Object> config = new HashMap<>();
        config.put("unknown", Boolean.TRUE);
        JsonBuilderFactory factory = createFactory(config);

        Map<String, ?> actual = factory.getConfigInUse();

        assertThat(actual).doesNotContainKey("unknown");
    }

    private static JsonBuilderFactory createFactory() {
        return Json.createBuilderFactory(Collections.emptyMap());
    }

    private static JsonBuilderFactory createFactory(Map<String, ?> config) {
        return Json.createBuilderFactory(config);
    }

    private static Collection<?> collection(Object... objects) {
        return Arrays.asList(objects);
    }

    private static Map<String, Object> map(Consumer<Map<String, Object>> consumer) {
        Map<String, Object> map = new LinkedHashMap<>();
        consumer.accept(map);
        return map;
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
