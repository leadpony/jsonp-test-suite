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
 * Test cases for ill-formed document.
 *
 * @author leadpony
 */
enum IllFormedDocumentTestCase {

    STRING_ONLY_QUOTATION("\"", 1, 2, 1),
    STRING_UNCLOSED("\"hello", 1, 7, 6),

    ARRAY_EOI_FOR_CLOSING_BRACKET("[1,2", 1, 5, 4),
    ARRAY_EOI_FOR_VALUE("[1,", 1, 4, 3),

    ARRAY_MISSING_FIRST_VALUE("[,2,3]", 1, 2, 1),
    ARRAY_MISSING_SECOND_VALUE("[1,,3]", 1, 4, 3),

    ARRAY_CLOSED_BY_CURLY_BRACKET("[1,2,3}", 1, 7, 6),
    CLOSING_SQUARE_BRACKET("]", 1, 1, 0),

    OBJECT_EOI_FOR_CLOSING_BRACKET("{\"a\": 1,\"b\": 2", 1, 15, 14),
    OBJECT_EOI_FOR_VALUE("{\"a\": 1,\"b\":", 1, 13, 12),
    OBJECT_EOI_FOR_COLON("{\"a\": 1,\"b\"", 1, 12, 11),
    OBJECT_EOI_FOR_KEY("{\"a\": 1,", 1, 9, 8),
    OBJECT_EOI_FOR_COMMA("{\"a\": 1", 1, 8, 7),

    OBJECT_MISSING_COLON("{\"a\": 1,\"b\"2}", 1, 12, 11),
    OBJECT_MISSING_KEY("{\"a\": 1,: 2}", 1, 9, 8),

    OBJECT_CLOSED_BY_SQUARE_BRACKET("{\"a\": 1]", 1, 8, 7),
    CLOSING_OBJECT_BRACKET("}", 1, 1, 0);

    private final String json;
    private final long lineNumber;
    private final long columnNumber;
    private final long streamOffset;

    IllFormedDocumentTestCase(String json, long lineNumber, long columnNumber, long streamOffset) {
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
