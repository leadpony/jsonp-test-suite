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

import java.io.InputStream;
import java.util.stream.Stream;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;

/**
 * JSON resources which describe test cases.
 *
 * @author leadpony
 */
enum TestCaseResource {
    JSON_POINTER("/org/leadpony/jsonp/testsuite/tests/json-pointer.json"),
    JSON_POINTER_REMOVE("/org/leadpony/jsonp/testsuite/tests/json-pointer-remove.json");

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
