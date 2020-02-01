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

import java.math.BigDecimal;

import jakarta.json.Json;
import jakarta.json.JsonValue;

import org.leadpony.jsonp.testsuite.helper.JsonSupplier;

/**
 * @author leadpony
 */
enum JsonTestCase implements JsonSupplier {
    TRUE,
    FALSE,
    NULL,

    EMPTY_STRING,
    BLANK_STRING,
    STRING_WORD,
    STRING_CONTAINING_SPACE,

    ZERO,
    MINUS_ZERO("-0", ZERO.value),
    ONE,
    MINUS_ONE,
    TEN,
    MINUS_TEN,
    HUNDRED,
    MINUS_HUNDRED,
    THOUSAND,
    MINUS_THOUSAND,
    HOURS_PER_DAY,
    MINUS_HOURS_PER_DAY,
    DAYS_PER_YEAR,
    MINUS_DAYS_PER_YEAR,

    MAX_INTEGER,
    MIN_INTEGER,
    MAX_LONG,
    MIN_LONG,

    BASE_OF_NATURAL_LOGARITHM,

    PI,
    MINUS_PI,

    HUNDRED_SCIENTIFIC_NOTATION("1e+2", new BigDecimal("1E+2")),
    HUNDRED_SCIENTIFIC_NOTATION_CAPITAL("1E+2", HUNDRED_SCIENTIFIC_NOTATION.value),

    HUNDREDTH_SCIENTIFIC_NOTATION_MINUS("1e-2", new BigDecimal("1E-2")),
    HUNDREDTH_NOTATION_CAPITAL_MINNUS("1E-2", HUNDREDTH_SCIENTIFIC_NOTATION_MINUS.value);

    private final String json;
    private final JsonValue value;

    JsonTestCase() {
        JsonValueTestCase testCase = JsonValueTestCase.valueOf(name());
        this.json = testCase.getString();
        this.value = testCase.getJsonValue();
    }

    JsonTestCase(String json, JsonValue value) {
        this.json = json;
        this.value = value;
    }

    JsonTestCase(String json, BigDecimal value) {
        this.json = json;
        this.value = Json.createValue(value);
    }

    @Override
    public String getJson() {
        return json;
    }

    @Override
    public JsonValue getValue() {
        return value;
    }
}
