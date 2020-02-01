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

import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

import java.io.StringReader;
import java.time.Duration;

import jakarta.json.Json;
import jakarta.json.stream.JsonParser;
import jakarta.json.stream.JsonParserFactory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * @author leadpony
 */
public class JsonParserSkipTest {

    private static JsonParserFactory parserFactory;

    @BeforeAll
    public static void setUpOnce() {
        parserFactory = Json.createParserFactory(null);
    }

    /**
     * @see https://github.com/eclipse-ee4j/jsonp/pull/147
     */
    @Test
    public void skipArrayShouldNotLoopForeverEvenIfNotClosed() {
        String json = "[1,2,3";
        JsonParser parser = createJsonParser(json);
        parser.next();

        assertTimeoutPreemptively(Duration.ofMillis(500), () -> {
            try {
                parser.skipArray();
            } catch (Exception e) {
            }
        });
    }

    /**
     * @see https://github.com/eclipse-ee4j/jsonp/pull/147
     */
    @Test
    public void skipObjectShouldNotLoopForeverEvenIfNotClosed() {
        String json = "{\"a\":1";
        JsonParser parser = createJsonParser(json);
        parser.next();

        assertTimeoutPreemptively(Duration.ofMillis(500), () -> {
            try {
                parser.skipObject();
            } catch (Exception e) {
            }
        });
    }

    private JsonParser createJsonParser(String json) {
        return parserFactory.createParser(new StringReader(json));
    }
}
