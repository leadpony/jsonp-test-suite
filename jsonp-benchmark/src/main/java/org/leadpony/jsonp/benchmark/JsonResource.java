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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * JSON resources for benchmarking.
 *
 * @author leadpony
 */
public enum JsonResource {
    GLOSSARY("/org/json/example/glossary.json"),
    RFC7159_OBJECT("/org/ietf/rfc/rfc7159/image.json"),
    RFC7159_ARRAY("/org/ietf/rfc/rfc7159/zip.json"),
    ATOM_API("/com/github/atom-api.json"),
    ;

    private final String name;

    private JsonResource(String name) {
        this.name = name;
    }

    InputStream openStream() {
        return getClass().getResourceAsStream(name);
    }

    Reader createReader() {
        return new InputStreamReader(openStream(), StandardCharsets.UTF_8);
    }

    String getJsonAsString() {
        try (BufferedReader reader = new BufferedReader(createReader())) {
            return reader.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
