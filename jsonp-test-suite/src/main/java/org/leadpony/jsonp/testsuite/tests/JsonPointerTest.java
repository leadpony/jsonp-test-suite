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
import static org.leadpony.jsonp.testsuite.helper.JsonBuilderHelper.array;
import static org.leadpony.jsonp.testsuite.helper.JsonBuilderHelper.object;

import java.util.logging.Logger;

import javax.json.Json;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.JsonPointer;
import javax.json.JsonValue;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.leadpony.jsonp.testsuite.helper.LogHelper;

/**
 * A test for testing {@link JsonPointer}.
 *
 * @author leadpony
 */
public class JsonPointerTest {

    private static final Logger LOG = LogHelper.getLogger(JsonPointerTest.class);

    private static final JsonObject RFC6901_OBJECT = object(
        b -> {
            b.add("foo", array(b2 -> b2.add("bar").add("baz")));
            b.add("", 0);
            b.add("a/b", 1);
            b.add("c%d", 2);
            b.add("e^f", 3);
            b.add("g|h", 4);
            b.add("i\\\\j", 5);
            b.add("k\\\"l", 6);
            b.add(" ", 7);
            b.add("m~n", 8);
        });

    enum JsonPointerTestCase {
        // examples in RFC 6901
        WHOLE("", true),
        FOO("/foo", true),
        FOO_0("/foo/0", true),
        SLASH("/", true),
        AB("/a~1b", true),
        CD("/c%d", true),
        EF("/e^f", true),
        GH("/g|h", true),
        IJ("/i\\\\j", true),
        KL("/k\\\"l", true),
        SPACE("/ ", true),
        MN("/m~0n", true),

        MULTIPLE_SLASH("//", true),
        ENDING_WITH_SLASH("/foo/", true),

        TILDE_FOLLOWED_BY_ILLEGAL_CHARACTER("/a~2b", true),
        TILDE_FOLLOWED_BY_SLASH("/a~/", true),
        ENDING_WITH_TILDE("/a~", true),

        // invalid
        MISSING_PREFIX_SLASH("foo", false);

        final String pointer;
        final boolean valid;

        JsonPointerTestCase(String pointer, boolean valid) {
            this.pointer = pointer;
            this.valid = valid;
        }
    }

    @ParameterizedTest
    @EnumSource(JsonPointerTestCase.class)
    public void createPointerShouldCreateJsonPointerAsExpeced(JsonPointerTestCase test) {
        Throwable thrown = catchThrowable(() -> {
            JsonPointer pointer = Json.createPointer(test.pointer);
            assertThat(pointer).isNotNull();
        });

        if (test.valid) {
            assertThat(thrown).isNull();
        } else {
            assertThat(thrown).isNotNull().isInstanceOf(JsonException.class);
            LOG.info(thrown.getMessage());
        }
    }

    enum RetrievalTestCase {
        WHOLE("", RFC6901_OBJECT),
        FOO("/foo", array(b -> b.add("bar").add("baz"))),
        FOO_0("/foo/0", Json.createValue("bar")),
        SLASH("/", Json.createValue(0)),
        AB("/a~1b", Json.createValue(1)),
        CD("/c%d", Json.createValue(2)),
        EF("/e^f", Json.createValue(3)),
        GH("/g|h", Json.createValue(4)),
        IJ("/i\\\\j", Json.createValue(5)),
        KL("/k\\\"l", Json.createValue(6)),
        SPACE("/ ", Json.createValue(7)),
        MN("/m~0n", Json.createValue(8));

        final String pointer;
        final JsonValue pointed;

        RetrievalTestCase(String pointer, JsonValue pointed) {
            this.pointer = pointer;
            this.pointed = pointed;
        }
    }

    @ParameterizedTest
    @EnumSource(RetrievalTestCase.class)
    public void getValueShouldReturnValueAsExpeced(RetrievalTestCase test) {
        JsonPointer pointer = Json.createPointer(test.pointer);
        JsonValue actual = pointer.getValue(RFC6901_OBJECT);

        assertThat(actual).isEqualTo(test.pointed);
    }

    @ParameterizedTest
    @EnumSource(RetrievalTestCase.class)
    public void containsValueShouldReturnTrueAsExpeced(RetrievalTestCase test) {
        JsonPointer pointer = Json.createPointer(test.pointer);
        boolean actual = pointer.containsValue(RFC6901_OBJECT);

        assertThat(actual).isTrue();
    }
}
