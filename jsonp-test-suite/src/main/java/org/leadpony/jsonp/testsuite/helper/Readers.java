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
package org.leadpony.jsonp.testsuite.helper;

import java.io.FilterReader;
import java.io.IOException;
import java.io.Reader;

/**
 * A utility class creating instances of {@link Reader}.
 *
 * @author leadpony
 */
public final class Readers {

    /**
     * Returns a reader which will throw an exception when reading.
     *
     * @param reader the original reader.
     * @return newly created reader.
     */
    public static Reader throwingWhenReading(Reader reader) {
        return new FilterReader(reader) {
            @Override
            public int read() throws IOException {
                throw new IOException();
            }

            @Override
            public int read(char[] cbuf, int off, int len) throws IOException {
                throw new IOException();
            }
        };
    }

    /**
     * Returns a reader which will throw an exception when closing.
     *
     * @param reader the original reader.
     * @return newly created reader.
     */
    public static Reader throwingWhenClosing(Reader reader) {
        return new FilterReader(reader) {
            @Override
            public void close() throws IOException {
                throw new IOException();
            }
        };
    }

    private Readers() {
    }
}
