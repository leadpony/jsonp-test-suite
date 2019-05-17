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
import javax.json.JsonValue;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerationException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.leadpony.jsonp.testsuite.helper.LogHelper;

/**
 * @author leadpony
 */
public class JsonWriterIllegalStateTest {

    private static final Logger LOG = LogHelper.getLogger(JsonWriterIllegalStateTest.class);

    private static JsonWriterFactory factory;
    protected JsonWriter writer;

    @BeforeAll
    public static void setUpOnce() {
        factory = Json.createWriterFactory(null);
    }

    @BeforeEach
    public void setUp() {
        writer = factory.createWriter(new StringWriter());
    }

    @Test
    public void closeShouldThrowJsonGenerationException() {
        Throwable thrown = catchThrowable(() -> {
            writer.close();
        });
        assertThat(thrown).isInstanceOf(JsonGenerationException.class);
        LOG.info(thrown.getMessage());
    }

    abstract class IllegalStateTest {

        @Test
        public void writeValueShouldThrowIllegalStateException() {
            Throwable thrown = catchThrowable(() -> {
                writer.write(JsonValue.TRUE);
            });
            assertThat(thrown).isInstanceOf(IllegalStateException.class);
            LOG.info(thrown.getMessage());
        }

        @Test
        public void writeStructureShouldThrowIllegalStateException() {
            Throwable thrown = catchThrowable(() -> {
                writer.write(JsonValue.EMPTY_JSON_ARRAY);
            });
            assertThat(thrown).isInstanceOf(IllegalStateException.class);
            LOG.info(thrown.getMessage());
        }

        @Test
        public void writeArrayShouldThrowIllegalStateException() {
            Throwable thrown = catchThrowable(() -> {
                writer.writeArray(JsonValue.EMPTY_JSON_ARRAY);
            });
            assertThat(thrown).isInstanceOf(IllegalStateException.class);
            LOG.info(thrown.getMessage());
        }

        @Test
        public void writeObjectShouldThrowIllegalStateException() {
            Throwable thrown = catchThrowable(() -> {
                writer.writeObject(JsonValue.EMPTY_JSON_OBJECT);
            });
            assertThat(thrown).isInstanceOf(IllegalStateException.class);
            LOG.info(thrown.getMessage());
        }
    }

    @Nested
    public class AfterClosing extends IllegalStateTest {

        @BeforeEach
        public void setUp() {
            writer.write(JsonValue.TRUE);
            writer.close();
        }
    }

    @Nested
    public class AfterWritingValue extends IllegalStateTest {

        @BeforeEach
        public void setUp() {
            writer.write(JsonValue.TRUE);
        }
    }

    @Nested
    public class AfterWritingStructure extends IllegalStateTest {

        @BeforeEach
        public void setUp() {
            writer.write(JsonValue.EMPTY_JSON_ARRAY);
        }
    }

    @Nested
    public class AfterWritingArray extends IllegalStateTest {

        @BeforeEach
        public void setUp() {
            writer.writeArray(JsonValue.EMPTY_JSON_ARRAY);
        }
    }

    @Nested
    public class AfterWritingObject extends IllegalStateTest {

        @BeforeEach
        public void setUp() {
            writer.writeObject(JsonValue.EMPTY_JSON_OBJECT);
        }
    }
}
