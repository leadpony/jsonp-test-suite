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
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.io.StringWriter;
import java.io.Writer;
import java.util.logging.Logger;

import javax.json.Json;
import javax.json.JsonException;
import javax.json.JsonStructure;
import javax.json.JsonValue;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.leadpony.jsonp.testsuite.helper.LoggerFactory;
import org.leadpony.jsonp.testsuite.helper.Writers;

/**
 * @author leadpony
 */
public class JsonWriterIOExceptionTest {

    private static final Logger LOG = LoggerFactory.getLogger(JsonWriterIOExceptionTest.class);

    private static JsonWriterFactory factory;

    @BeforeAll
    public static void setUpOnce() {
        factory = Json.createWriterFactory(null);
    }

    @Test
    public void writeArrayShouldThrowJsonException() {
        Writer writer = Writers.throwingWhenWriting(new StringWriter());
        JsonWriter jsonWriter = factory.createWriter(writer);
        Throwable thrown = catchThrowable(() -> {
            jsonWriter.writeArray(JsonValue.EMPTY_JSON_ARRAY);
        });

        assertThat(thrown).isInstanceOf(JsonException.class);
        LOG.info(thrown.getMessage());
    }

    @Test
    public void writeObjectShouldThrowJsonException() {
        Writer writer = Writers.throwingWhenWriting(new StringWriter());
        JsonWriter jsonWriter = factory.createWriter(writer);
        Throwable thrown = catchThrowable(() -> {
            jsonWriter.writeObject(JsonValue.EMPTY_JSON_OBJECT);
        });

        assertThat(thrown).isInstanceOf(JsonException.class);
        LOG.info(thrown.getMessage());
    }

    @Test
    public void writeStructureShouldThrowJsonException() {
        Writer writer = Writers.throwingWhenWriting(new StringWriter());
        JsonWriter jsonWriter = factory.createWriter(writer);
        Throwable thrown = catchThrowable(() -> {
            jsonWriter.write((JsonStructure) JsonValue.EMPTY_JSON_ARRAY);
        });

        assertThat(thrown).isInstanceOf(JsonException.class);
        LOG.info(thrown.getMessage());
    }

    @Test
    public void writeValueShouldThrowJsonException() {
        Writer writer = Writers.throwingWhenWriting(new StringWriter());
        JsonWriter jsonWriter = factory.createWriter(writer);
        Throwable thrown = catchThrowable(() -> {
            jsonWriter.write(JsonValue.TRUE);
        });

        assertThat(thrown).isInstanceOf(JsonException.class);
        LOG.info(thrown.getMessage());
    }

    @Test
    public void closeShouldThrowJsonException() {
        Writer writer = Writers.throwingWhenClosing(new StringWriter());
        JsonWriter jsonWriter = factory.createWriter(writer);

        assertThatCode(() -> {
            jsonWriter.write(JsonValue.TRUE);
        }).doesNotThrowAnyException();

        Throwable thrown = catchThrowable(() -> {
            jsonWriter.close();
        });

        assertThat(thrown).isInstanceOf(JsonException.class);
        LOG.info(thrown.getMessage());
    }
}
