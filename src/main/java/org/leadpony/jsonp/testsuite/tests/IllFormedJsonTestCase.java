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

/**
 * Test cases for ill-formed JSON.
 *
 * @author leadpony
 */
enum IllFormedJsonTestCase {

    EMPTY("", 1, 1, 0),
    BLANK("    ", 1, 5, 4),

    NUL("\u0000", 1, 1, 0),
    NUL_IN_STRING("\"\u0000\"", 1, 2, 1),

    TAB_IN_STRING("\"hello\tworld\"", 1, 7, 6),

    TRUE_INCOMPLETE("tru", 1, 4, 3),
    FALSE_INCOMPLETE("fals", 1, 5, 4),
    NULL_INCOMPLETE("nul", 1, 4, 3),

    ARRAY_MISSING_COMMA("[1 2]", 1, 4, 3),
    ARRAY_MISSING_LAST_VALUE("[1,2,]", 1, 6, 5),

    OBJECT_MISSING_VALUE("{\"a\":1,\"b\":}", 1, 12, 11),
    OBJECT_MISSING_COMMA("{\"a\": 1\"b\": 2}", 1, 8, 7);

    private final String json;
    private final long lineNumber;
    private final long columnNumber;
    private final long streamOffset;

    IllFormedJsonTestCase(String json, long lineNumber, long columnNumber, long streamOffset) {
        this.json = json;
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
        this.streamOffset = streamOffset;
    }

    String getJson() {
        return json;
    }

    long getLineNumber() {
        return lineNumber;
    }

    long getColumnNumber() {
        return columnNumber;
    }

    long getStreamOffset() {
        return streamOffset;
    }
}
