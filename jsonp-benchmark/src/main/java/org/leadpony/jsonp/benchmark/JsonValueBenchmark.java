/*
 * Copyright 2019 the JSON-P Benchmark Authors.
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
import javax.json.JsonReaderFactory;
import javax.json.JsonValue;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

/**
 * A benchmark of JSON Processing API implementation.
 *
 * @author leadpony
 */
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Benchmark)
public class JsonValueBenchmark {

    @Param({"GLOSSARY"})
    private JsonResource resource;

    private JsonValue value;

    @Setup
    public void setUp() {
        JsonReaderFactory factory= Json.createReaderFactory(null);
        try (JsonReader reader = factory.createReader(resource.createReader())) {
            this.value = reader.readValue();
        }
    }

    @Benchmark
    public String valueToString() {
        return this.value.toString();
    }
}
