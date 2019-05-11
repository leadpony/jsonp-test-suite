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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import javax.json.JsonValue.ValueType;

/**
 * @author leadpony
 */
enum JsonResource {
    GLOSSARY("/org/json/example/glossary.json", ValueType.OBJECT),
    MENU("/org/json/example/menu.json", ValueType.OBJECT),
    MENU2("/org/json/example/menu2.json", ValueType.OBJECT),
    WEB_APP("/org/json/example/web-app.json", ValueType.OBJECT),
    WIDGET("/org/json/example/widget.json", ValueType.OBJECT),

    IMAGE("/org/ietf/rfc/rfc7159/image.json", ValueType.OBJECT),
    ZIP("/org/ietf/rfc/rfc7159/zip.json", ValueType.ARRAY),

    ATOM_API("/com/github/atom-api.json", ValueType.OBJECT),
    ;

    private final String name;
    private final ValueType type;
    private final Charset charset;

    private JsonResource(String name, ValueType type) {
        this(name, type, StandardCharsets.UTF_8);
    }

    private JsonResource(String name, ValueType type, Charset charset) {
        this.name = name;
        this.type = type;
        this.charset = charset;
    }

    InputStream openStream() {
        return getClass().getResourceAsStream(name);
    }

    Reader createReader() {
        return new InputStreamReader(openStream(), getCharset());
    }

    ValueType getType() {
        return type;
    }

    Charset getCharset() {
        return charset;
    }

    boolean isArray() {
        return type == ValueType.ARRAY;
    }

    boolean isObject() {
        return type == ValueType.OBJECT;
    }

    boolean isStructure() {
        return type == ValueType.ARRAY || type == ValueType.OBJECT;
    }

    String getMinifiedString() {
        String filename = this.name.substring(0, this.name.lastIndexOf('.'));
        String name = filename.concat(".min.json");
        StringBuilder b = new StringBuilder();
        InputStream in = getClass().getResourceAsStream(name);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            b.append(reader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        return b.toString();
    }

    static Stream<JsonResource> getArraysAsStream() {
        return Stream.of(values()).filter(JsonResource::isArray);
    }

    static Stream<JsonResource> getObjectsAsStream() {
        return Stream.of(values()).filter(JsonResource::isObject);
    }

    static Stream<JsonResource> getStructuresAsStream() {
        return Stream.of(values()).filter(JsonResource::isStructure);
    }
}
