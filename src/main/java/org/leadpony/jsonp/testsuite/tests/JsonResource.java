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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import jakarta.json.JsonValue.ValueType;

/**
 * JSON resources.
 *
 * @author leadpony
 */
public enum JsonResource {
    GLOSSARY("/org/json/example/glossary.json", ValueType.OBJECT),
    MENU("/org/json/example/menu.json", ValueType.OBJECT),
    MENU2("/org/json/example/menu2.json", ValueType.OBJECT),
    WEB_APP("/org/json/example/web-app.json", ValueType.OBJECT),
    WIDGET("/org/json/example/widget.json", ValueType.OBJECT),

    IMAGE("/org/ietf/rfc/rfc7159/image.json", ValueType.OBJECT),
    ZIP("/org/ietf/rfc/rfc7159/zip.json", ValueType.ARRAY),

    ATOM_API("/com/github/atom-api.json", ValueType.OBJECT);

    private final String name;
    private final ValueType type;
    private final Charset charset;

    JsonResource(String name, ValueType type) {
        this(name, type, StandardCharsets.UTF_8);
    }

    JsonResource(String name, ValueType type, Charset charset) {
        this.name = name;
        this.type = type;
        this.charset = charset;
    }

    public InputStream openStream() {
        return getClass().getResourceAsStream(name);
    }

    public Reader createReader() {
        return new InputStreamReader(openStream(), getCharset());
    }

    /**
     * Returns the type of this JSON value.
     *
     * @return the type of this JSON value.
     */
    public ValueType getType() {
        return type;
    }

    /**
     * Returns the character set of this JSON resource.
     *
     * @return the character set.
     */
    public Charset getCharset() {
        return charset;
    }

    /**
     * Checks if this JSON is an array or not.
     *
     * @return {@code true} if this JSON is an array, {@code false} otherwise.
     */
    public boolean isArray() {
        return type == ValueType.ARRAY;
    }

    /**
     * Checks if this JSON is an object or not.
     *
     * @return {@code true} if this JSON is an object, {@code false} otherwise.
     */
    public boolean isObject() {
        return type == ValueType.OBJECT;
    }

    /**
     * Checks if this JSON is an array or object.
     *
     * @return {@code true} if this JSON is an array or object, {@code false} otherwise.
     */
    public boolean isStructure() {
        return type == ValueType.ARRAY || type == ValueType.OBJECT;
    }

    /**
     * Returns the original JSON as a string.
     *
     * @return the JSON as a string.
     */
    public String getJsonAsString() {
        return getResourceAsString(this.name);
    }

    /**
     * Returns the indented version of the JSON as a string.
     * Tab is used for every indentation.
     *
     * @param spaces the number of spaces used as an indentation.
     * @return the indented JSON as a string.
     */
    public String getJsonIndentedWithTabAsString() {
        String name = new StringBuilder(getNameWithoutExtension())
                .append(".tab.json")
                .toString();
        return getResourceAsString(name);
    }

    /**
     * Returns the indented version of the JSON as a string.
     * Spaces are used for every indentation.
     *
     * @param spaces the number of spaces used as an indentation.
     * @return the indented JSON as a string.
     */
    public String getJsonIndentedWithSpacesAsString(int spaces) {
        String name = new StringBuilder(getNameWithoutExtension())
                .append(".sp")
                .append(spaces)
                .append(".json")
                .toString();
        return getResourceAsString(name);
    }

    /**
     * Returns the minified version of the JSON as a string.
     *
     * @return the minified JSON as a string.
     */
    public String getMinifiedJsonAsString() {
        String name = new StringBuilder(getNameWithoutExtension())
            .append(".min.json")
            .toString();
        return getResourceAsString(name);
    }

    public static Stream<JsonResource> getArraysAsStream() {
        return Stream.of(values()).filter(JsonResource::isArray);
    }

    public static Stream<JsonResource> getObjectsAsStream() {
        return Stream.of(values()).filter(JsonResource::isObject);
    }

    public static Stream<JsonResource> getStructuresAsStream() {
        return Stream.of(values()).filter(JsonResource::isStructure);
    }

    private String getNameWithoutExtension() {
        return this.name.substring(0, this.name.lastIndexOf('.'));
    }

    private String getResourceAsString(String name) {
        InputStream in = getClass().getResourceAsStream(name);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
