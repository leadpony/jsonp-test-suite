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

import java.io.InputStream;
import java.util.stream.Stream;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;

/**
 * JSON resources describing test cases.
 *
 * @author leadpony
 */
enum TestCaseResource {
    JSON_POINTER("/org/leadpony/jsonp/testsuite/json-pointer.json"),
    JSON_POINTER_ADD("/org/leadpony/jsonp/testsuite/json-pointer-add.json"),
    JSON_POINTER_REMOVE("/org/leadpony/jsonp/testsuite/json-pointer-remove.json"),
    JSON_POINTER_REPLACE("/org/leadpony/jsonp/testsuite/json-pointer-replace.json"),
    JSON_PATCH("/org/leadpony/jsonp/testsuite/json-patch.json"),
    JSON_PATCH_MALFORMED("/org/leadpony/jsonp/testsuite/json-patch-malformed.json"),
    JSON_PATCH_DIFF("/org/leadpony/jsonp/testsuite/json-patch-diff.json"),
    JSON_MERGE_PATCH("/org/leadpony/jsonp/testsuite/json-merge-patch.json"),
    JSON_MERGE_DIFF("/org/leadpony/jsonp/testsuite/json-merge-diff.json"),
    RFC6902_EXAMPLES("/org/ietf/rfc/rfc6902/examples.json"),
    RFC7396_EXAMPLES("/org/ietf/rfc/rfc7396/examples.json");

    private final String name;

    TestCaseResource(String name) {
        this.name = name;
    }

    Stream<JsonObject> getObjectStream() {
        InputStream in = getClass().getResourceAsStream(name);
        try (JsonReader reader = Json.createReader(in)) {
            return reader.readArray().stream().map(JsonValue::asJsonObject);
        }
    }
}
