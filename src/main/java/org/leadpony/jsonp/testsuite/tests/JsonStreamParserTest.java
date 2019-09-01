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

import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonStructure;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParserFactory;

/**
 * @author leadpony
 */
public class JsonStreamParserTest extends AbstractJsonValueParserTest {

    private static final JsonParserFactory PARSER_FACTORY = Json.createParserFactory(null);

    @Override
    protected JsonParser createParser(JsonStructure value) {
        StringReader reader = new StringReader(value.toString());
        return PARSER_FACTORY.createParser(reader);
    }
}
