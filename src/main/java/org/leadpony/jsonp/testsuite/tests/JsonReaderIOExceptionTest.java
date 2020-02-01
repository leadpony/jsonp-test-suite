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

import java.io.Reader;
import java.io.StringReader;
import java.util.logging.Logger;

import jakarta.json.Json;
import jakarta.json.JsonException;
import jakarta.json.JsonReader;
import jakarta.json.JsonReaderFactory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.leadpony.jsonp.testsuite.helper.LoggerFactory;
import org.leadpony.jsonp.testsuite.helper.Readers;

/**
 * @author leadpony
 */
public class JsonReaderIOExceptionTest {

    private static final Logger LOG = LoggerFactory.getLogger(JsonReaderIOExceptionTest.class);

    private static JsonReaderFactory factory;

    @BeforeAll
    public static void setUpOnce() {
        factory = Json.createReaderFactory(null);
    }

    @Test
    public void readArrayShouldThrowJsonException() {
        Reader reader = Readers.throwingWhenReading(new StringReader("[]"));
        JsonReader jsonReader = factory.createReader(reader);
        Throwable thrown = catchThrowable(() -> {
            jsonReader.readArray();
        });

        assertThat(thrown).isInstanceOf(JsonException.class);
        LOG.info(thrown.getMessage());
    }

    @Test
    public void readObjectShouldThrowJsonException() {
        Reader reader = Readers.throwingWhenReading(new StringReader("{}"));
        JsonReader jsonReader = factory.createReader(reader);
        Throwable thrown = catchThrowable(() -> {
            jsonReader.readObject();
        });

        assertThat(thrown).isInstanceOf(JsonException.class);
        LOG.info(thrown.getMessage());
    }

    @Test
    public void readShouldThrowJsonException() {
        Reader reader = Readers.throwingWhenReading(new StringReader("{}"));
        JsonReader jsonReader = factory.createReader(reader);
        Throwable thrown = catchThrowable(() -> {
            jsonReader.read();
        });

        assertThat(thrown).isInstanceOf(JsonException.class);
        LOG.info(thrown.getMessage());
    }

    @Test
    public void readValueShouldThrowJsonException() {
        Reader reader = Readers.throwingWhenReading(new StringReader("true"));
        JsonReader jsonReader = factory.createReader(reader);
        Throwable thrown = catchThrowable(() -> {
            jsonReader.readValue();
        });

        assertThat(thrown).isInstanceOf(JsonException.class);
        LOG.info(thrown.getMessage());
    }

    @Test
    public void closeShouldThrowJsonException() {
        Reader reader = Readers.throwingWhenClosing(new StringReader("{}"));
        JsonReader jsonReader = factory.createReader(reader);
        Throwable thrown = catchThrowable(() -> {
            jsonReader.close();
        });

        assertThat(thrown).isInstanceOf(JsonException.class);
        LOG.info(thrown.getMessage());
    }
}
