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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.io.StringWriter;
import java.io.Writer;
import java.util.logging.Logger;

import jakarta.json.Json;
import jakarta.json.JsonException;
import jakarta.json.stream.JsonGenerator;
import jakarta.json.stream.JsonGeneratorFactory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.leadpony.jsonp.testsuite.helper.LoggerFactory;
import org.leadpony.jsonp.testsuite.helper.Writers;

/**
 * @author leadpony
 */
public class JsonGeneratorIOExceptionTest {

    private static final Logger LOG = LoggerFactory.getLogger(JsonGeneratorIOExceptionTest.class);

    private static JsonGeneratorFactory factory;

    @BeforeAll
    public static void setUpOnce() {
        factory = Json.createGeneratorFactory(null);
    }

    @Test
    public void flushShouldThrowJsonException() {
        Writer writer = Writers.throwingWhenFlushing(new StringWriter());
        JsonGenerator g = factory.createGenerator(writer);
        Throwable thrown = catchThrowable(() -> {
            g.write(true);
            g.flush();
        });

        assertThat(thrown).isInstanceOf(JsonException.class);
        LOG.info(thrown.getMessage());
    }

    @Test
    public void closeShouldThrowJsonException() {
        Writer writer = Writers.throwingWhenClosing(new StringWriter());
        JsonGenerator g = factory.createGenerator(writer);
        Throwable thrown = catchThrowable(() -> {
            g.write(true);
            g.close();
        });

        assertThat(thrown).isInstanceOf(JsonException.class);
        LOG.info(thrown.getMessage());
    }
}
