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

import org.assertj.core.api.AbstractAssert;

/**
 * @author leadpony
 */
public final class JsonAssertions {

    public static JsonAssertion assertThat(String actual) {
        return new JsonAssertion(actual);
    }

    private JsonAssertions() {
    }

    /**
     * @author leadpony
     */
    public static final class JsonAssertion extends AbstractAssert<JsonAssertion, String> {

        private JsonAssertion(String actual) {
            super(actual, JsonAssertion.class);
        }

        public JsonAssertion isEqualTo(String expected) {
            isNotNull();
            if (!normalizeJson(actual).equals(normalizeJson(expected))) {
                failWithMessage("Expected JSON to be <%s> but was <%s>", expected, actual);
            }
            return this;
        }

        private static String normalizeJson(String json) {
            StringBuilder builder = new StringBuilder();
            final int length = json.length();
            for (int i = 0; i < length; i++) {
                char c = json.charAt(i);
                builder.append(c);
                if (c == '\"') {
                    while (++i < length) {
                        c = json.charAt(i);
                        builder.append(c);
                        if (c == '\"') {
                            break;
                        } else if (c == '\\') {
                            if (++i < length) {
                                c = json.charAt(i);
                                builder.append(c);
                                if (c == 'u') {
                                    int j = 0;
                                    while (j++ < 4 && ++i < length) {
                                        c = json.charAt(i);
                                        builder.append(toUpperHexDigit(c));
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return builder.toString();
        }

        private static char toUpperHexDigit(char c) {
            if ('a' <= c && c <= 'f') {
                return (char) ('A' + (c - 'a'));
            }
            return c;
        }
    }
}
