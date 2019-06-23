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

import java.util.stream.Stream;

import javax.json.Json;
import javax.json.JsonMergePatch;
import javax.json.JsonObject;
import javax.json.JsonValue;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * @author leadpony
 */
public class JsonMergePatchTest {

    /**
     * @author leadpony
     */
    static class MergePatchTestCase {

        final String description;
        final JsonValue original;
        final JsonValue patch;
        final JsonValue result;

        MergePatchTestCase(String description, JsonValue original, JsonValue patch, JsonValue result) {
            this.description = description;
            this.original = original;
            this.patch = patch;
            this.result = result;
        }

        @Override
        public String toString() {
            return description;
        }
    }

    public static Stream<MergePatchTestCase> getMergePatchTestCases() {
        return Stream.of(
            TestCaseResource.RFC7396_EXAMPLES,
            TestCaseResource.JSON_MERGE_PATCH)
            .flatMap(TestCaseResource::getObjectStream)
            .flatMap((JsonObject object) -> {
                JsonValue original = object.get("original");
                return object.getJsonArray("tests")
                    .stream()
                    .map(JsonValue::asJsonObject)
                    .map((JsonObject test) -> new MergePatchTestCase(
                        test.getString("description"),
                        original,
                        test.get("patch"),
                        test.get("result")
                        )
                    );
            });
    }

    @ParameterizedTest
    @MethodSource("getMergePatchTestCases")
    public void applyShouldApplyPatchAsExpected(MergePatchTestCase test) {
        JsonMergePatch patch = Json.createMergePatch(test.patch);
        JsonValue actual = patch.apply(test.original);
        assertThat(actual).isEqualTo(test.result);
    }

    @ParameterizedTest
    @MethodSource("getMergePatchTestCases")
    public void toJsonValueShouldReturnJsonValue(MergePatchTestCase test) {
        JsonMergePatch patch = Json.createMergePatch(test.patch);
        JsonValue actual = patch.toJsonValue();
        assertThat(actual).isEqualTo(test.patch);
    }
}
