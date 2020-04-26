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

import java.io.Reader;
import java.io.StringReader;

import jakarta.json.stream.JsonParser;
import jakarta.json.Json;

/**
 * A test type to test {@link JsonParser}.
 *
 * @author leadpony
 */
public class JsonParserTest {

    public static class ReaderTest extends AbstractJsonParserTest {

        @Override
        protected JsonParser createJsonParser(String json) {
            Reader reader = new StringReader(json);
            return Json.createParser(reader);
        }
    }
}
