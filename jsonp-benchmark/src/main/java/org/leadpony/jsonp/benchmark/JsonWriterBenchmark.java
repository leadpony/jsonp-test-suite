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
package org.leadpony.jsonp.benchmark;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.concurrent.TimeUnit;

import javax.json.Json;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

/**
 * A benchmark for {@link JsonWriter}.
 *
 * @author leadpony
 */
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Benchmark)
public class JsonWriterBenchmark {

    @Param({ "RFC7159_OBJECT", "RFC7159_ARRAY" })
    private JsonResource resource;

    private JsonValue jsonValue;
    private JsonWriterFactory writerFactory;

    @Setup
    public void setUp() {
        this.writerFactory = Json.createWriterFactory(null);
        this.jsonValue = readJsonValue(resource.openStream());
    }

    private JsonValue readJsonValue(InputStream in) {
        try (JsonReader reader = Json.createReader(resource.openStream())) {
            return reader.readValue();
        }
    }

    @Benchmark
    public String write() {
        StringWriter stringWriter = new StringWriter();
        try (JsonWriter writer = writerFactory.createWriter(stringWriter)) {
            writer.write(this.jsonValue);
        }
        return stringWriter.toString();
    }
}
