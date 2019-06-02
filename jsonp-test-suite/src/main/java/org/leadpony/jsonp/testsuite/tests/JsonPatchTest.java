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

import java.util.logging.Logger;
import java.util.stream.Stream;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.JsonPatch;
import javax.json.JsonStructure;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.leadpony.jsonp.testsuite.helper.LogHelper;

/**
 * @author leadpony
 *
 */
public class JsonPatchTest {

    private static final Logger LOG = LogHelper.getLogger(JsonPatchTest.class);

    static class PatchTestCase {

        final String title;
        final JsonStructure json;
        final JsonArray patch;
        final JsonStructure result;

        PatchTestCase(JsonObject object) {
            this.title = object.getString("title");
            this.json = (JsonStructure) object.get("json");
            this.patch = object.getJsonArray("patch");
            this.result = (JsonStructure) object.get("result");
        }

        @Override
        public String toString() {
            return title;
        }
    }

    public static Stream<PatchTestCase> applyShouldApplyOperationsAsExpected() {
        return Stream.of(
            TestCaseResource.RFC6902_EXAMPLES,
            TestCaseResource.JSON_PATCH)
            .flatMap(TestCaseResource::getObjectStream)
            .filter(object -> !object.getBoolean("skip", false))
            .map(PatchTestCase::new);
    }

    @ParameterizedTest
    @MethodSource
    public void applyShouldApplyOperationsAsExpected(PatchTestCase test) {
        JsonPatch patch = Json.createPatch(test.patch);

        if (test.result != null) {
            JsonStructure actual = patch.apply(test.json);
            assertThat(actual).isEqualTo(test.result);
        } else {
            Throwable thrown = catchThrowable(() -> {
                patch.apply(test.json);
            });
            assertThat(thrown)
                .isNotNull()
                .isInstanceOf(JsonException.class);
            LOG.info(thrown.getMessage());
        }
    }
}
