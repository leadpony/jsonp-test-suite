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

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * A utility class creating instances of {@link Writer}.
 *
 * @author leadpony
 */
public final class Writers {

    public static Writer throwingWhenWriting(Writer writer) {
        return new FilterWriter(writer) {
            @Override
            public void write(char[] cbuf, int off, int len) throws IOException {
                throw new IOException();
            }

            @Override
            public void write(int c) throws IOException {
                throw new IOException();
            }

            @Override
            public void write(String str, int off, int len) throws IOException {
                throw new IOException();
            }
        };
    }

    public static Writer throwingWhenFlushing(Writer writer) {
        return new FilterWriter(writer) {
            @Override
            public void flush() throws IOException {
                throw new IOException();
            }
        };
    }

    public static Writer throwingWhenClosing(Writer writer) {
        return new FilterWriter(writer) {
            @Override
            public void close() throws IOException {
                throw new IOException();
            }
        };
    }

    private Writers() {
    }
}
