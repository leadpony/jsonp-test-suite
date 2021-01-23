/*
 * Copyright 2019-2021 the JSON-P Test Suite Authors.
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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import jakarta.json.Json;
import jakarta.json.JsonException;
import jakarta.json.stream.JsonParser;
import jakarta.json.stream.JsonParserFactory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

/**
 * @author leadpony
 */
public class CharsetDetectionTest {

    static final Charset UTF_32BE = Charset.forName("UTF-32BE");
    static final Charset UTF_32LE = Charset.forName("UTF-32LE");

    private static final byte[] UTF_8_BOM = {(byte) 0xef, (byte) 0xbb, (byte) 0xbf};
    private static final byte[] UTF_16BE_BOM = {(byte) 0xfe, (byte) 0xff};
    private static final byte[] UTF_16LE_BOM = {(byte) 0xff, (byte) 0xfe};
    private static final byte[] UTF_32BE_BOM = {0x00, 0x00, (byte) 0xfe, (byte) 0xff};
    private static final byte[] UTF_32LE_BOM = {(byte) 0xff, (byte) 0xfe, 0x00, 0x00};

    private static JsonParserFactory parserFactory;

    @BeforeAll
    public static void setUpOnce() {
        parserFactory = Json.createParserFactory(null);
    }

    @ParameterizedTest
    @EnumSource(JsonTestCase.class)
    public void utf8(JsonTestCase test) {
        testParser(test, StandardCharsets.UTF_8);
    }

    @ParameterizedTest
    @EnumSource(JsonTestCase.class)
    public void utf16be(JsonTestCase test) {
        testParser(test, StandardCharsets.UTF_16BE);
    }

    @ParameterizedTest
    @EnumSource(JsonTestCase.class)
    public void utf16le(JsonTestCase test) {
        testParser(test, StandardCharsets.UTF_16LE);
    }

    @ParameterizedTest
    @EnumSource(JsonTestCase.class)
    public void utf32be(JsonTestCase test) {
        testParser(test, UTF_32BE);
    }

    @ParameterizedTest
    @EnumSource(JsonTestCase.class)
    public void utf32le(JsonTestCase test) {
        testParser(test, UTF_32LE);
    }

    @ParameterizedTest
    @EnumSource(JsonTestCase.class)
    public void utf8WithBom(JsonTestCase test) {
        testParser(test, StandardCharsets.UTF_8, UTF_8_BOM);
    }

    @ParameterizedTest
    @EnumSource(JsonTestCase.class)
    public void utf16beWithBom(JsonTestCase test) {
        testParser(test, StandardCharsets.UTF_16BE, UTF_16BE_BOM);
    }

    @ParameterizedTest
    @EnumSource(JsonTestCase.class)
    public void utf16leWithBom(JsonTestCase test) {
        testParser(test, StandardCharsets.UTF_16LE, UTF_16LE_BOM);
    }

    @ParameterizedTest
    @EnumSource(JsonTestCase.class)
    public void utf32beWithBom(JsonTestCase test) {
        testParser(test, UTF_32BE, UTF_32BE_BOM);
    }

    @ParameterizedTest
    @EnumSource(JsonTestCase.class)
    public void utf32leWithBom(JsonTestCase test) {
        testParser(test, UTF_32LE, UTF_32LE_BOM);
    }

    @Test
    public void shouldFailToDetectEncoding() {
        byte[] bytes = {0x00};
        InputStream in = new ByteArrayInputStream(bytes);
        Throwable thrown = catchThrowable(() -> {
            parserFactory.createParser(in);
        });
        assertThat(thrown).isInstanceOf(JsonException.class);
    }

    private void testParser(JsonTestCase test, Charset charset) {
        InputStream in = createEncodedStream(test.getJson(), charset);
        testParserWithStream(in);
    }

    private void testParser(JsonTestCase test, Charset charset, byte[] bom) {
        InputStream in = createEncodedStream(test.getJson(), charset, bom);
        testParserWithStream(in);
    }

    private void testParserWithStream(InputStream in) {
        assertThatCode(() -> {
            JsonParser parser = parserFactory.createParser(in);
            while (parser.hasNext()) {
                parser.next();
            }
            parser.close();
        }).doesNotThrowAnyException();
    }

    private InputStream createEncodedStream(String json, Charset charset) {
        ByteBuffer buffer = charset.encode(json);
        return new ByteArrayInputStream(buffer.array(), 0, buffer.remaining());
    }

    private InputStream createEncodedStream(String json, Charset charset, byte[] bom) {
        ByteBuffer buffer = charset.encode(json);
        byte[] bytes = new byte[bom.length + buffer.remaining()];
        System.arraycopy(bom, 0, bytes, 0, bom.length);
        System.arraycopy(buffer.array(), 0, bytes, bom.length, buffer.remaining());
        return new ByteArrayInputStream(bytes);
    }
}
