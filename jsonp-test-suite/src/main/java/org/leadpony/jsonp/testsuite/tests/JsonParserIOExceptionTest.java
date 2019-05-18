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

import java.io.Reader;
import java.io.StringReader;
import java.util.logging.Logger;

import javax.json.Json;
import javax.json.JsonException;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParserFactory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.leadpony.jsonp.testsuite.helper.LogHelper;
import org.leadpony.jsonp.testsuite.helper.Readers;

/**
 * @author leadpony
 */
public class JsonParserIOExceptionTest {

    private static final Logger LOG = LogHelper.getLogger(JsonParserIOExceptionTest.class);

    private static JsonParserFactory parserFactory;

    @BeforeAll
    public static void setUpOnce() {
        parserFactory = Json.createParserFactory(null);
    }

    @Test
    public void nextShouldThrowJsonException() {
        Reader reader = Readers.throwingWhenReading(new StringReader("{}"));
        JsonParser parser = parserFactory.createParser(reader);

        Throwable thrown = catchThrowable(() -> {
            parser.hasNext();
            parser.next();
        });

        assertThat(thrown).isInstanceOf(JsonException.class);
        LOG.info(thrown.getMessage());
    }

    @Test
    public void closeShouldThrowJsonException() {
        Reader reader = Readers.throwingWhenClosing(new StringReader("{}"));
        JsonParser parser = parserFactory.createParser(reader);

        Throwable thrown = catchThrowable(() -> {
            parser.close();
        });

        assertThat(thrown).isInstanceOf(JsonException.class);
        LOG.info(thrown.getMessage());
    }
}
