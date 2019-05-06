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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import javax.json.stream.JsonParser;

import org.junit.jupiter.api.Disabled;

/**
 * @author leadpony
 */
abstract class StreamJsonParserTest extends JsonParserTest {

    static final Charset UTF_32BE = Charset.forName("UTF-32BE");
    static final Charset UTF_32LE = Charset.forName("UTF-32LE");

    abstract Charset getEncoding();

    byte[] getByeOrderMark() {
        return null;
    }

    // Disables this because of repeated failures in some implementations.
    @Disabled
    @Override
    public void hasNextShouldReturnResultAsExpected(ContinuityFixture fixture) {
    }

    // Disables this because of repeated failures in some implementations.
    @Disabled
    @Override
    public void getLocationShouldReturnFinalLocationAsExpected(LocationFixture fixture) {
    }

    @Override
    protected JsonParser createJsonParser(String json) {
        Charset charset = getEncoding();
        byte[] bom = getByeOrderMark();
        InputStream in;
        if (bom != null) {
            in = createEncodedStream(json, charset, bom);
        } else {
            in = createEncodedStream(json, charset);
        }
        return parserFactory.createParser(in);
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

    public static class UTF8Test extends StreamJsonParserTest {

        @Override
        Charset getEncoding() {
            return StandardCharsets.UTF_8;
        }
    }

    public static class UTF16BETest extends StreamJsonParserTest {

        @Override
        Charset getEncoding() {
            return StandardCharsets.UTF_16BE;
        }
    }

    public static class UTF16LETest extends StreamJsonParserTest {

        @Override
        Charset getEncoding() {
            return StandardCharsets.UTF_16LE;
        }
    }

    public static class UTF32BETest extends StreamJsonParserTest {

        @Override
        Charset getEncoding() {
            return UTF_32BE;
        }
    }

    public static class UTF32LETest extends StreamJsonParserTest {

        @Override
        Charset getEncoding() {
            return UTF_32LE;
        }
    }

    public static class BomUTF8Test extends UTF8Test {

        private static final byte[] BOM = { (byte) 0xef, (byte) 0xbb, (byte) 0xbf };

        @Override
        byte[] getByeOrderMark() {
            return BOM;
        }
    }

    public static class BomUTF16BETest extends UTF16BETest {

        private static final byte[] BOM = { (byte) 0xfe, (byte) 0xff };

        @Override
        byte[] getByeOrderMark() {
            return BOM;
        }
    }

    public static class BomUTF16LETest extends UTF16LETest {

        private static final byte[] BOM = { (byte) 0xff, (byte) 0xfe };

        @Override
        byte[] getByeOrderMark() {
            return BOM;
        }
    }

    public static class BomUTF32BETest extends UTF32BETest {

        private static final byte[] BOM = { 0x00, 0x00, (byte) 0xfe, (byte) 0xff };

        @Override
        byte[] getByeOrderMark() {
            return BOM;
        }
    }

    public static class BomUTF32LETest extends UTF32LETest {

        private static final byte[] BOM = { (byte) 0xff, (byte) 0xfe, 0x00, 0x00 };

        @Override
        byte[] getByeOrderMark() {
            return BOM;
        }
    }
}
