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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.io.StringWriter;
import java.util.logging.Logger;

import javax.json.Json;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonGeneratorFactory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.leadpony.jsonp.testsuite.helper.LogHelper;

/**
 * @author leadpony
 */
public class JsonGeneratorNumberFormatExceptionTest {

    private static final Logger LOG = LogHelper.getLogger(JsonGeneratorNumberFormatExceptionTest.class);

    private static JsonGeneratorFactory factory;

    @BeforeAll
    public static void setUpOnce() {
        factory = Json.createGeneratorFactory(null);
    }

    /**
     * @author leadpony
     */
    enum IllegalDoubleTestCase {
        NAN(Double.NaN),
        NEGATIVE_INFINITY(Double.NEGATIVE_INFINITY),
        POSITIVE_INFINITY(Double.POSITIVE_INFINITY);

        final double value;

        IllegalDoubleTestCase(double value) {
            this.value = value;
        }
    }

    @ParameterizedTest
    @EnumSource(IllegalDoubleTestCase.class)
    public void writeShouldThrowNumberFormatException(IllegalDoubleTestCase test) {
        JsonGenerator g = generator();

        Throwable thrown = catchThrowable(() -> {
            g.write(test.value);
        });

        LOG.info(thrown.getMessage());

        assertThat(thrown)
            .isNotNull()
            .isInstanceOf(NumberFormatException.class);
    }

    /**
     * @author leadpony
     */
    enum IllegalNamedDoubleTestCase {
        NAN("abc", Double.NaN),
        NEGATIVE_INFINITY("abc", Double.NEGATIVE_INFINITY),
        POSITIVE_INFINITY("abc", Double.POSITIVE_INFINITY);

        final String name;
        final double value;

        IllegalNamedDoubleTestCase(String name, double value) {
            this.name = name;
            this.value = value;
        }
    }

    @ParameterizedTest
    @EnumSource(IllegalNamedDoubleTestCase.class)
    public void writeShouldThrowNumberFormatException(IllegalNamedDoubleTestCase test) {
        JsonGenerator g = generator();

        g.writeStartObject();
        Throwable thrown = catchThrowable(() -> {
            g.write(test.name, test.value);
        });

        LOG.info(thrown.getMessage());

        assertThat(thrown)
            .isNotNull()
            .isInstanceOf(NumberFormatException.class);
    }

    private static JsonGenerator generator() {
        StringWriter writer = new StringWriter();
        return factory.createGenerator(writer);
    }
}
