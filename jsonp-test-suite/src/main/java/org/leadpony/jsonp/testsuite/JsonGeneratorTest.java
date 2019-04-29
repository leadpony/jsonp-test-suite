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
package org.leadpony.jsonp.testsuite;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.StringWriter;

import javax.json.Json;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonGeneratorFactory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * @author leadpony
 */
public class JsonGeneratorTest {

    private static JsonGeneratorFactory factory;

    @BeforeAll
    public static void setUpOnce() {
        factory = Json.createGeneratorFactory(null);
    }

    @Test
    public void writeShouldWriteTrue() {
        StringWriter writer = new StringWriter();
        JsonGenerator g = factory.createGenerator(writer);

        g.write(true);
        g.flush();

        assertThat(writer.toString()).isEqualTo("true");
    }

    @Test
    public void writeShouldWriteFalse() {
        StringWriter writer = new StringWriter();
        JsonGenerator g = factory.createGenerator(writer);

        g.write(false);
        g.flush();

        assertThat(writer.toString()).isEqualTo("false");
    }

    @Test
    public void writeNullShouldWriteNull() {
        StringWriter writer = new StringWriter();
        JsonGenerator g = factory.createGenerator(writer);

        g.writeNull();
        g.flush();

        assertThat(writer.toString()).isEqualTo("null");
    }

    @Test
    public void writeShouldWriteString() {
        StringWriter writer = new StringWriter();
        JsonGenerator g = factory.createGenerator(writer);

        g.write("hello");
        g.flush();

        assertThat(writer.toString()).isEqualTo("\"hello\"");
    }

    @Test
    public void writeShouldWriteInteger() {
        StringWriter writer = new StringWriter();
        JsonGenerator g = factory.createGenerator(writer);

        g.write(365);
        g.flush();

        assertThat(writer.toString()).isEqualTo("365");
    }

    @Test
    public void generatorShouldWriteEmptyArray() {
        StringWriter writer = new StringWriter();
        JsonGenerator g = factory.createGenerator(writer);

        g.writeStartArray();
        g.writeEnd();
        g.flush();

        assertThat(writer.toString()).isEqualTo("[]");
    }

    @Test
    public void generatorShouldWriteSingleItemArray() {
        StringWriter writer = new StringWriter();
        JsonGenerator g = factory.createGenerator(writer);

        g.writeStartArray();
        g.write(true);
        g.writeEnd();
        g.flush();

        assertThat(writer.toString()).isEqualTo("[true]");
    }

    @Test
    public void generatorShouldWriteMultipleItemsArray() {
        StringWriter writer = new StringWriter();
        JsonGenerator g = factory.createGenerator(writer);

        g.writeStartArray();
        g.write(true);
        g.write(false);
        g.writeEnd();
        g.flush();

        assertThat(writer.toString()).isEqualTo("[true,false]");
    }

    @Test
    public void generatorShouldWriteArrayContainingArray() {
        StringWriter writer = new StringWriter();
        JsonGenerator g = factory.createGenerator(writer);

        g.writeStartArray();
        g.writeStartArray();
        g.write(1);
        g.write(2);
        g.writeEnd();
        g.writeStartArray();
        g.write(3);
        g.write(4);
        g.writeEnd();
        g.writeEnd();
        g.flush();

        assertThat(writer.toString()).isEqualTo("[[1,2],[3,4]]");
    }

    @Test
    public void generatorShouldWriteArrayContainingObject() {
        StringWriter writer = new StringWriter();
        JsonGenerator g = factory.createGenerator(writer);

        g.writeStartArray();
        g.writeStartObject();
        g.write("a", 1);
        g.write("b", 2);
        g.writeEnd();
        g.writeStartObject();
        g.write("c", 3);
        g.write("d", 4);
        g.writeEnd();
        g.writeEnd();
        g.flush();

        assertThat(writer.toString()).isEqualTo("[{\"a\":1,\"b\":2},{\"c\":3,\"d\":4}]");
    }

    @Test
    public void generatorShouldWriteEmptyObject() {
        StringWriter writer = new StringWriter();
        JsonGenerator g = factory.createGenerator(writer);

        g.writeStartObject();
        g.writeEnd();
        g.flush();

        assertThat(writer.toString()).isEqualTo("{}");
    }

    @Test
    public void generatorShouldWriteSinglePropertyObject() {
        StringWriter writer = new StringWriter();
        JsonGenerator g = factory.createGenerator(writer);

        g.writeStartObject();
        g.write("a", 365);
        g.writeEnd();
        g.flush();

        assertThat(writer.toString()).isEqualTo("{\"a\":365}");
    }

    @Test
    public void generatorShouldWriteMultiplePropertiesObject() {
        StringWriter writer = new StringWriter();
        JsonGenerator g = factory.createGenerator(writer);

        g.writeStartObject();
        g.write("a", 365);
        g.write("b", "hello");
        g.writeEnd();
        g.flush();

        assertThat(writer.toString()).isEqualTo("{\"a\":365,\"b\":\"hello\"}");
    }

    @Test
    public void generatorShouldWriteObjectContainingArray() {
        StringWriter writer = new StringWriter();
        JsonGenerator g = factory.createGenerator(writer);

        g.writeStartObject();
        g.writeStartArray("a");
        g.write(1);
        g.write(2);
        g.writeEnd();
        g.writeStartArray("b");
        g.write(3);
        g.write(4);
        g.writeEnd();
        g.writeEnd();
        g.flush();

        assertThat(writer.toString()).isEqualTo(
                "{\"a\":[1,2],\"b\":[3,4]}");
    }

    @Test
    public void generatorShouldWriteObjectContainingObject() {
        StringWriter writer = new StringWriter();
        JsonGenerator g = factory.createGenerator(writer);

        g.writeStartObject();
        g.writeStartObject("a");
        g.write("a1", 1);
        g.write("a2", 2);
        g.writeEnd();
        g.writeStartObject("b");
        g.write("b1", 3);
        g.write("b2", 4);
        g.writeEnd();
        g.writeEnd();
        g.flush();

        assertThat(writer.toString()).isEqualTo(
                "{\"a\":{\"a1\":1,\"a2\":2},\"b\":{\"b1\":3,\"b2\":4}}");
    }
}
