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

import java.util.concurrent.TimeUnit;

import javax.json.Json;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParserFactory;
import javax.json.stream.JsonParser.Event;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

/**
 * A benchmark for in-memory {@link JsonParser}.
 *
 * @author leadpony
 */
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Benchmark)
public class JsonValueParserBenchmark {

    @Param({ "ATOM_API", "RFC7159_OBJECT", "RFC7159_ARRAY" })
    private JsonResource resource;

    private JsonParserFactory parserFactory;
    private JsonValue json;

    @Setup
    public void setUp() {
        parserFactory = Json.createParserFactory(null);
        json = readJsonValue();
    }

    private JsonValue readJsonValue() {
        try (JsonReader reader = Json.createReader(resource.createReader())) {
            return reader.readValue();
        }
    }

    @Benchmark
    public Event parseValue() {
        Event event = null;
        try (JsonParser parser = createParser(json)) {
            while (parser.hasNext()) {
                event = parser.next();
            }
        }
        return event;
    }

    private JsonParser createParser(JsonValue value) {
        switch (value.getValueType()) {
        case ARRAY:
            return parserFactory.createParser(value.asJsonArray());
        case OBJECT:
            return parserFactory.createParser(value.asJsonObject());
        default:
            throw new IllegalArgumentException();
        }
    }
}
