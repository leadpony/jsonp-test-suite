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

import java.util.logging.Logger;
import java.util.stream.Stream;

import javax.json.Json;
import javax.json.JsonException;
import javax.json.JsonPointer;
import javax.json.JsonStructure;
import javax.json.JsonValue;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.leadpony.jsonp.testsuite.helper.LogHelper;

/**
 * A test for testing {@link JsonPointer}.
 *
 * @author leadpony
 */
public class JsonPointerTest {

    private static final Logger LOG = LogHelper.getLogger(JsonPointerTest.class);

    /**
     * Test cases for JSON pointer creation.
     *
     * @author leadpony
     */
    enum JsonPointerTestCase {
        // examples in RFC 6901
        WHOLE("", true),
        FOO("/foo", true),
        FOO_0("/foo/0", true),
        SLASH("/", true),
        AB("/a~1b", true),
        CD("/c%d", true),
        EF("/e^f", true),
        GH("/g|h", true),
        IJ("/i\\\\j", true),
        KL("/k\\\"l", true),
        SPACE("/ ", true),
        MN("/m~0n", true),

        MULTIPLE_SLASH("//", true),
        ENDING_WITH_SLASH("/foo/", true),

        TILDE_FOLLOWED_BY_ILLEGAL_CHARACTER("/a~2b", true),
        TILDE_FOLLOWED_BY_SLASH("/a~/", true),
        ENDING_WITH_TILDE("/a~", true),

        // invalid
        MISSING_PREFIX_SLASH("foo", false);

        final String pointer;
        final boolean valid;

        JsonPointerTestCase(String pointer, boolean valid) {
            this.pointer = pointer;
            this.valid = valid;
        }
    }

    @ParameterizedTest
    @EnumSource(JsonPointerTestCase.class)
    public void createPointerShouldCreateJsonPointerAsExpeced(JsonPointerTestCase test) {
        Throwable thrown = catchThrowable(() -> {
            JsonPointer pointer = Json.createPointer(test.pointer);
            assertThat(pointer).isNotNull();
        });

        if (test.valid) {
            assertThat(thrown).isNull();
        } else {
            assertThat(thrown).isNotNull().isInstanceOf(JsonException.class);
            LOG.info(thrown.getMessage());
        }
    }

    /**
     * Test cases for {@code add} and {@code replace} operations.
     *
     * @author leadpony
     */
    static class AddOrReplaceTestCase {

        final JsonStructure json;
        final String pointer;
        final JsonValue value;
        final JsonValue result;

        AddOrReplaceTestCase(JsonStructure json, String pointer, JsonValue value, JsonValue result) {
            this.json = json;
            this.pointer = pointer;
            this.value = value;
            this.result = result;
        }

        @Override
        public String toString() {
            return pointer;
        }
    }

    public static Stream<AddOrReplaceTestCase> getAddOrReplaceTestCases(TestCaseResource resource) {
        return resource.getObjectStream()
            .flatMap(object -> {
                JsonStructure json = (JsonStructure) object.get("json");
                return object.getJsonArray("tests")
                    .stream()
                    .map(JsonValue::asJsonObject)
                    .map(test -> {
                        String pointer = test.getString("pointer");
                        JsonValue value = test.get("value");
                        JsonValue result = test.get("result");
                        return new AddOrReplaceTestCase(json, pointer, value, result);
                    });
            });
    }

    public static Stream<AddOrReplaceTestCase> addShouldAddValueAsExpeced() {
        return getAddOrReplaceTestCases(TestCaseResource.JSON_POINTER_ADD);
    }

    @ParameterizedTest
    @MethodSource
    public void addShouldAddValueAsExpeced(AddOrReplaceTestCase test) {
        JsonPointer pointer = Json.createPointer(test.pointer);

        if (test.result != null) {
            JsonValue actual = pointer.add(test.json, test.value);
            assertThat(actual).isEqualTo(test.result);
        } else {
            Throwable thrown = catchThrowable(() -> {
                pointer.add(test.json, test.value);
            });
            assertThat(thrown).isInstanceOf(JsonException.class);
            LOG.info(thrown.getMessage());
        }
    }

    public static Stream<AddOrReplaceTestCase> replaceShouldReplaceValueAsExpeced() {
        return getAddOrReplaceTestCases(TestCaseResource.JSON_POINTER_REPLACE);
    }

    @ParameterizedTest
    @MethodSource
    public void replaceShouldReplaceValueAsExpeced(AddOrReplaceTestCase test) {
        JsonPointer pointer = Json.createPointer(test.pointer);

        if (test.result != null) {
            JsonValue actual = pointer.replace(test.json, test.value);
            assertThat(actual).isEqualTo(test.result);
        } else {
            Throwable thrown = catchThrowable(() -> {
                pointer.replace(test.json, test.value);
            });
            assertThat(thrown).isInstanceOf(JsonException.class);
            LOG.info(thrown.getMessage());
        }
    }

    /**
     * Test cases for {@code remove} operation.
     *
     * @author leadpony
     */
    static class RemovalTestCase {

        final JsonStructure json;
        final String pointer;
        final JsonValue result;

        RemovalTestCase(JsonStructure json, String pointer, JsonValue result) {
            this.json = json;
            this.pointer = pointer;
            this.result = result;
        }

        @Override
        public String toString() {
            return pointer;
        }
    }

    public static Stream<RemovalTestCase> removeShouldRemoveValueAsExpeced() {
        return TestCaseResource.JSON_POINTER_REMOVE.getObjectStream()
            .flatMap(object -> {
                JsonStructure json = (JsonStructure) object.get("json");
                return object.getJsonArray("tests")
                    .stream()
                    .map(JsonValue::asJsonObject)
                    .map(test -> {
                        String pointer = test.getString("pointer");
                        JsonValue result = test.get("result");
                        return new RemovalTestCase(json, pointer, result);
                    });
            });
    }

    @ParameterizedTest
    @MethodSource
    public void removeShouldRemoveValueAsExpeced(RemovalTestCase test) {
        JsonPointer pointer = Json.createPointer(test.pointer);

        if (test.result != null) {
            JsonValue actual = pointer.remove(test.json);
            assertThat(actual).isEqualTo(test.result);
        } else {
            Throwable thrown = catchThrowable(() -> {
                pointer.remove(test.json);
            });
            assertThat(thrown).isInstanceOf(JsonException.class);
            LOG.info(thrown.getMessage());
        }
    }

    /**
     * Test cases for JSON pointer evaluation.
     *
     * @author leadpony
     */
    static class EvaluationTestCase {

        final JsonStructure json;
        final String pointer;
        final JsonValue value;

        EvaluationTestCase(JsonStructure json, String pointer, JsonValue value) {
            this.json = json;
            this.pointer = pointer;
            this.value = value;
        }

        @Override
        public String toString() {
            return pointer;
        }
    }

    public static Stream<EvaluationTestCase> containsValueShouldReturnTrueAsExpeced() {
        return TestCaseResource.JSON_POINTER.getObjectStream()
            .flatMap(object -> {
                JsonStructure json = (JsonStructure) object.get("json");
                return object.getJsonArray("tests")
                    .stream()
                    .map(JsonValue::asJsonObject)
                    .map(test -> {
                        String pointer = test.getString("pointer");
                        JsonValue value = test.get("value");
                        return new EvaluationTestCase(json, pointer, value);
                    });
            });
    }

    @ParameterizedTest
    @MethodSource
    public void containsValueShouldReturnTrueAsExpeced(EvaluationTestCase test) {
        JsonPointer pointer = Json.createPointer(test.pointer);
        assertThatCode(() -> {
            boolean actual = pointer.containsValue(test.json);
            assertThat(actual).isEqualTo(test.value != null);
        }).doesNotThrowAnyException();
    }

    @ParameterizedTest
    @MethodSource("containsValueShouldReturnTrueAsExpeced")
    public void getValueShouldReturnValueAsExpeced(EvaluationTestCase test) {
        JsonPointer pointer = Json.createPointer(test.pointer);

        if (test.value != null) {
            JsonValue actual = pointer.getValue(test.json);
            assertThat(actual).isNotNull().isEqualTo(test.value);
        } else {
            Throwable thrown = catchThrowable(() -> {
                pointer.getValue(test.json);
            });
            assertThat(thrown).isInstanceOf(JsonException.class);
            LOG.info(thrown.getMessage());
        }
    }
}
