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

import javax.json.JsonValue;

/**
 * @author leadpony
 */
interface JsonSource {

    String getJson();

    default JsonValue getValue() {
        throw new UnsupportedOperationException();
    }

    default String getJsonAsArrayItem() {
        return "[" + getJson() + "]";
    }

    default String getJsonAsPropertyKey() {
        return "{" + getJson() + ":\"foo\"}";
    }

    default String getJsonAsPropertyValue() {
        return "{\"foo\":" + getJson() + "}";
    }
}
