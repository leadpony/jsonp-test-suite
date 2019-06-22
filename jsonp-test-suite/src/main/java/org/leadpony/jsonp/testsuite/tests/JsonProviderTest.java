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
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.logging.Logger;
import java.util.stream.Stream;

import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import javax.json.spi.JsonProvider;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.leadpony.jsonp.testsuite.helper.LogHelper;

/**
 * @author leadpony
 */
public class JsonProviderTest {

    private static final Logger LOG = LogHelper.getLogger(JsonProviderTest.class);

    private static JsonProvider provider;

    @BeforeAll
    public static void setUpOnces() {
        provider = JsonProvider.provider();
    }

    @Test
    public void createArrayBuilderShouldReturnEmptyBuilder() {
        JsonArrayBuilder builder = provider.createArrayBuilder();
        JsonArray result = builder.build();
        assertThat(result).isEqualTo(JsonValue.EMPTY_JSON_ARRAY);
    }

    public static Stream<JsonValueTestCase> createArrayBuilderShouldReturnBuilderFilledWithArray() {
        return JsonValueTestCase.getArraysAsStream();
    }

    @ParameterizedTest
    @MethodSource
    public void createArrayBuilderShouldReturnBuilderFilledWithArray(JsonValueTestCase test) {
        JsonArray array = (JsonArray) test.getJsonValue();
        JsonArrayBuilder builder = provider.createArrayBuilder(array);
        JsonArray result = builder.build();
        assertThat(result).isEqualTo(array);
    }

    /**
     * @author leadpony
     */
    enum CollectionBasedArrayBuilderTestCase {
        SIMPLE(
            collection(c -> {
                c.add("hello");
                c.add(42);
                c.add(3.14);
                c.add(true);
                c.add(false);
            }),
            "[\"hello\",42,3.14,true,false]"),
        NULL(
            collection(c -> c.add(null)),
            "[null]"
            ),
        ARRAY(
            collection(c -> {
                c.add(collection(c2 -> {
                    c2.add("hello");
                    c2.add(42);
                }));
                c.add(collection(c2 -> {
                    c2.add(3.14);
                    c2.add(true);
                }));
            }),
            "[[\"hello\",42],[3.14,true]]"
            ),
        OBJECT(
            collection(c -> {
                c.add(map(m -> {
                        m.put("a", "hello");
                        m.put("b", 42);
                    }));
                c.add(map(m -> {
                    m.put("c", 3.14);
                    m.put("d", true);
                }));
            }),
            "[{\"a\":\"hello\",\"b\":42},{\"c\":3.14,\"d\":true}]"
            ),
        OPTIONAL(
            collection(c -> {
                c.add(Optional.of("hello"));
                c.add(Optional.empty());
                c.add(Optional.of(3.14));
                c.add(Optional.of(true));
                c.add(Optional.empty());
            }),
            "[\"hello\",3.14,true]"
            );

        final Collection<Object> collection;
        final String string;

        CollectionBasedArrayBuilderTestCase(List<Object> collection, String string) {
            this.collection = collection;
            this.string = string;
        }
    }

    @ParameterizedTest
    @EnumSource(CollectionBasedArrayBuilderTestCase.class)
    public void createArrayBuilderShouldReturnBuilderFilledWithCollection(CollectionBasedArrayBuilderTestCase test) {
        assertThatCode(() -> {

            JsonArrayBuilder builder = provider.createArrayBuilder(test.collection);
            JsonArray result = builder.build();
            assertThat(result.toString()).isEqualTo(test.string);

        }).doesNotThrowAnyException();
    }

    public static Stream<Object> illegalValuesAsJsonValue() {
        return Stream.of(
            Paths.get("."),
            LocalDateTime.now(),
            StandardCharsets.UTF_8
            );
    }

    @ParameterizedTest
    @MethodSource("illegalValuesAsJsonValue")
    public void createArrayBuilderShouldThrowException(Object value) {
        List<Object> collection = new ArrayList<>();
        collection.add(value);

        Throwable thrown = catchThrowable(() -> {
            provider.createArrayBuilder(collection);
        });

        LOG.info(thrown.getMessage());
        assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void createObjectBuilderShouldReturnEmptyBuilder() {
        JsonObjectBuilder builder = provider.createObjectBuilder();
        JsonObject result = builder.build();
        assertThat(result).isEqualTo(JsonValue.EMPTY_JSON_OBJECT);
    }

    public static Stream<JsonValueTestCase> createObjectBuilderShouldReturnBuilderFilledWithObject() {
        return JsonValueTestCase.getObjectsAsStream();
    }

    @ParameterizedTest
    @MethodSource
    public void createObjectBuilderShouldReturnBuilderFilledWithObject(JsonValueTestCase test) {
        JsonObject object = (JsonObject) test.getJsonValue();
        JsonObjectBuilder builder = provider.createObjectBuilder(object);
        JsonObject result = builder.build();
        assertThat(result).isEqualTo(object);
    }

    /**
     * @author leadpony
     */
    enum MapBasedObjectBuilderTestCase {
        SIMPLE(
            map(m -> {
                m.put("a", "hello");
                m.put("b", 42);
                m.put("c", 3.14);
                m.put("d", true);
                m.put("e", false);
            }),
            "{\"a\":\"hello\",\"b\":42,\"c\":3.14,\"d\":true,\"e\":false}"),
        NULL(
            map(m -> {
                m.put("a", null);
            }),
            "{\"a\":null}"
            ),
        ARRAY(
            map(m -> {
                m.put("a", collection(c -> {
                    c.add("hello");
                    c.add(42);
                }));
                m.put("b", collection(c -> {
                    c.add(3.14);
                    c.add(true);
                }));
            }),
            "{\"a\":[\"hello\",42],\"b\":[3.14,true]}"
            ),
        OBJECT(
            map(m -> {
                m.put("a", map(m2 -> {
                    m2.put("b", "hello");
                    m2.put("c", 42);
                }));
                m.put("d", map(m2 -> {
                    m2.put("e", 3.14);
                    m2.put("f", true);
                }));
            }),
            "{\"a\":{\"b\":\"hello\",\"c\":42},\"d\":{\"e\":3.14,\"f\":true}}"
            ),
        OPTIONAL(
            map(m -> {
                m.put("a", Optional.of("hello"));
                m.put("b", Optional.empty());
                m.put("c", Optional.of(3.14));
                m.put("d", Optional.of(true));
                m.put("e", Optional.empty());
            }),
            "{\"a\":\"hello\",\"c\":3.14,\"d\":true}"
            );

        final Map<String, Object> map;
        final String string;

        MapBasedObjectBuilderTestCase(Map<String, Object> map, String string) {
            this.map = map;
            this.string = string;
        }
    }

    @ParameterizedTest
    @EnumSource(MapBasedObjectBuilderTestCase.class)
    public void createObjectBuilderShouldReturnBuilderFilledWithMap(MapBasedObjectBuilderTestCase test) {
        assertThatCode(() -> {

            JsonObjectBuilder builder = provider.createObjectBuilder(test.map);
            JsonObject result = builder.build();
            assertThat(result.toString()).isEqualTo(test.string);

        }).doesNotThrowAnyException();
    }

    @ParameterizedTest
    @MethodSource("illegalValuesAsJsonValue")
    public void createObjectBuilderShouldThrowException(Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put("a", value);

        Throwable thrown = catchThrowable(() -> {
            provider.createObjectBuilder(map);
        });

        LOG.info(thrown.getMessage());
        assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
    }

    private static List<Object> collection(Consumer<List<Object>> consumer) {
        List<Object> list = new ArrayList<>();
        consumer.accept(list);
        return list;
    }

    private static Map<String, Object> map(Consumer<Map<String, Object>> consumer) {
        Map<String, Object> map = new LinkedHashMap<>();
        consumer.accept(map);
        return map;
    }
}
