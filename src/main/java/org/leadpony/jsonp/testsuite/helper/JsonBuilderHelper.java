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
package org.leadpony.jsonp.testsuite.helper;

import java.util.function.Consumer;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonBuilderFactory;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

/**
 * @author leadpony
 */
public final class JsonBuilderHelper {

    private static final JsonBuilderFactory BUILDER_FACTORY = Json.createBuilderFactory(null);

    /**
     * Builds an JSON array.
     *
     * @param consumer the function to build an array.
     * @return JSON array built.
     */
    public static JsonArray array(Consumer<JsonArrayBuilder> consumer) {
        JsonArrayBuilder builder = BUILDER_FACTORY.createArrayBuilder();
        consumer.accept(builder);
        return builder.build();
    }

    /**
     * Builds an JSON object.
     *
     * @param consumer the function to build an object.
     * @return JSON object built.
     */
    public static JsonObject object(Consumer<JsonObjectBuilder> consumer) {
        JsonObjectBuilder builder = BUILDER_FACTORY.createObjectBuilder();
        consumer.accept(builder);
        return builder.build();
    }


    private JsonBuilderHelper() {
    }
}
