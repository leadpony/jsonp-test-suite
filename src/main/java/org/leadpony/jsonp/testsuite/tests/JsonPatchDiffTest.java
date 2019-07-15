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

import java.util.stream.Stream;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonPatch;
import javax.json.JsonStructure;
import javax.json.JsonValue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * @author leadpony
 */
public class JsonPatchDiffTest {

    @Test
    public void createDiffShouldCreateEmptyPatchIfStructureIsSame() {
        JsonStructure source = JsonValue.EMPTY_JSON_OBJECT;
        JsonStructure target = source;
        JsonPatch actual = Json.createDiff(source, target);
        assertThat(actual.toJsonArray()).isEqualTo(JsonValue.EMPTY_JSON_ARRAY);
    }

    /**
     * Test case for {@code Json#createDiff(JsonStructure, JsonStructure)}.
     *
     * @author leadpony
     */
    public static final class DiffTestCase {

        private final String description;
        final JsonStructure source;
        final JsonStructure target;
        final JsonArray patch;

        DiffTestCase(String description, JsonStructure source, JsonStructure target, JsonArray patch) {
            this.description = description;
            this.source = source;
            this.target = target;
            this.patch = patch;
        }

        @Override
        public String toString() {
            return description;
        }
    }

    public static Stream<DiffTestCase> createDiffShouldCreatePatchAsExpected() {
        return TestCaseResource.JSON_PATCH_DIFF.getObjectStream()
            .map(object -> new DiffTestCase(
                    object.getString("description"),
                    (JsonStructure) object.get("source"),
                    (JsonStructure) object.get("target"),
                    object.getJsonArray("patch")
                ));
    }

    @ParameterizedTest
    @MethodSource
    public void createDiffShouldCreatePatchAsExpected(DiffTestCase test) {
        JsonPatch actual = Json.createDiff(test.source, test.target);

        assertThatCode(() -> {
            JsonStructure result = actual.apply(test.source);
            assertThat(result).isEqualTo(test.target);
        }).doesNotThrowAnyException();

        if (test.patch != null) {
            assertThat(actual.toJsonArray()).isEqualTo(test.patch);
        }
    }
}
