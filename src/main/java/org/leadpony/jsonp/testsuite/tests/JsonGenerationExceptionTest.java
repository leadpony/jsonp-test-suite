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
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.function.Supplier;
import java.util.logging.Logger;

import javax.json.Json;
import javax.json.JsonValue;
import javax.json.stream.JsonGenerationException;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonGeneratorFactory;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.leadpony.jsonp.testsuite.helper.LoggerFactory;

/**
 * @author leadpony
 */
public class JsonGenerationExceptionTest {

    private static final Logger LOG = LoggerFactory.getLogger(JsonGenerationExceptionTest.class);

    private static JsonGeneratorFactory factory;
    protected JsonGenerator g;

    @BeforeAll
    public static void setUpOnce() {
        factory = Json.createGeneratorFactory(null);
    }

    @BeforeEach
    public void setUp() {
        g = factory.createGenerator(new StringWriter());
    }

    @AfterEach
    public void tearDown() {
    }

    /**
     * A supplier of {@link JsonGenerator}.
     *
     * @author leadpony
     */
    class GeneratorSupplier implements Supplier<JsonGenerator> {

        @Override
        public JsonGenerator get() {
            return g;
        }
    }

    /**
     * A type to test writing various kinds of values.
     *
     * @author leadpony
     */
    interface WritingValueTest extends Supplier<JsonGenerator> {

        @Test
        default void writeStartArrayShouldThrowException() {
            Throwable thrown = catchThrowable(() -> {
                get().writeStartArray();
            });
            assertThat(thrown).isInstanceOf(JsonGenerationException.class);
            LOG.info(thrown.getMessage());
        }

        @Test
        default void writeStartObjectShouldThrowException() {
            Throwable thrown = catchThrowable(() -> {
                get().writeStartObject();
            });
            assertThat(thrown).isInstanceOf(JsonGenerationException.class);
            LOG.info(thrown.getMessage());
        }

        @Test
        default void writeJsonValueShouldThrowException() {
            Throwable thrown = catchThrowable(() -> {
                get().write(JsonValue.TRUE);
            });
            assertThat(thrown).isInstanceOf(JsonGenerationException.class);
            LOG.info(thrown.getMessage());
        }

        @Test
        default void writeStringShouldThrowException() {
            Throwable thrown = catchThrowable(() -> {
                get().write("hello");
            });
            assertThat(thrown).isInstanceOf(JsonGenerationException.class);
            LOG.info(thrown.getMessage());
        }

        @Test
        default void writeBigIntegerShouldThrowException() {
            Throwable thrown = catchThrowable(() -> {
                get().write(new BigInteger("42"));
            });
            assertThat(thrown).isInstanceOf(JsonGenerationException.class);
            LOG.info(thrown.getMessage());
        }

        @Test
        default void writeBigDecimalShouldThrowException() {
            Throwable thrown = catchThrowable(() -> {
                get().write(new BigDecimal("42"));
            });
            assertThat(thrown).isInstanceOf(JsonGenerationException.class);
            LOG.info(thrown.getMessage());
        }

        @Test
        default void writeIntShouldThrowException() {
            Throwable thrown = catchThrowable(() -> {
                get().write(42);
            });
            assertThat(thrown).isInstanceOf(JsonGenerationException.class);
            LOG.info(thrown.getMessage());
        }

        @Test
        default void writeLongShouldThrowException() {
            Throwable thrown = catchThrowable(() -> {
                get().write(42L);
            });
            assertThat(thrown).isInstanceOf(JsonGenerationException.class);
            LOG.info(thrown.getMessage());
        }

        @Test
        default void writeBooleanShouldThrowException() {
            Throwable thrown = catchThrowable(() -> {
                get().write(true);
            });
            assertThat(thrown).isInstanceOf(JsonGenerationException.class);
            LOG.info(thrown.getMessage());
        }

        @Test
        default void writeNullShouldThrowException() {
            Throwable thrown = catchThrowable(() -> {
                get().writeNull();
            });
            assertThat(thrown).isInstanceOf(JsonGenerationException.class);
            LOG.info(thrown.getMessage());
        }
    }

    /**
     * A type to test writing name.
     *
     * @author leadpony
     */
    interface WritingNameTest extends Supplier<JsonGenerator> {

        @Test
        default void writeStartArrayWithNameShouldThrowException() {
            Throwable thrown = catchThrowable(() -> {
                get().writeStartArray("key");
            });
            assertThat(thrown).isInstanceOf(JsonGenerationException.class);
            LOG.info(thrown.getMessage());
        }

        @Test
        default void writeStartObjectWithNameShouldThrowException() {
            Throwable thrown = catchThrowable(() -> {
                get().writeStartObject("key");
            });
            assertThat(thrown).isInstanceOf(JsonGenerationException.class);
            LOG.info(thrown.getMessage());
        }

        @Test
        default void writeKeyShouldThrowException() {
            Throwable thrown = catchThrowable(() -> {
                get().writeKey("key");
            });
            assertThat(thrown).isInstanceOf(JsonGenerationException.class);
            LOG.info(thrown.getMessage());
        }
    }

    /**
     * A type to test writing end of collection.
     *
     * @author leadpony
     */
    interface WritingEndTest extends Supplier<JsonGenerator> {

        @Test
        default void writeEndShouldThrowException() {
            Throwable thrown = catchThrowable(() -> {
                get().writeEnd();
            });
            assertThat(thrown).isInstanceOf(JsonGenerationException.class);
            LOG.info(thrown.getMessage());
        }
    }

    /**
     * A test containing all tests writing values.
     *
     * @author leadpony
     */
    interface AllWritingTest extends WritingValueTest, WritingNameTest, WritingEndTest {
    }

    /**
     * A type to test {@code JsonGenerator#close()}.
     *
     * @author leadpony
     */
    interface ClosingTest extends Supplier<JsonGenerator> {

        @Test
        default void closeShouldThrowException() {
            Throwable thrown = catchThrowable(() -> {
                get().close();
            });
            assertThat(thrown).isInstanceOf(JsonGenerationException.class);
            LOG.info(thrown.getMessage());
        }
    }

    /**
     * When the JSON generator is in initial state.
     *
     * @author leadpony
     */
    @Nested
    public class WhenInitial extends GeneratorSupplier
        implements WritingNameTest, WritingEndTest, ClosingTest {

        /**
         * After {@code JsonGenerator#write(JsonValue)} was called.
         *
         * @author leadpony
         */
        @Nested
        public class AfterWritingJsonValue extends GeneratorSupplier
            implements AllWritingTest {

            @BeforeEach
            public void setUp() {
                get().write(JsonValue.TRUE);
            }
        }

        /**
         * After {@code JsonGenerator#write(String)} was called.
         *
         * @author leadpony
         */
        @Nested
        public class AfterWritingString extends GeneratorSupplier
            implements AllWritingTest {

            @BeforeEach
            public void setUp() {
                get().write("hello");
            }
        }

        /**
         * After {@code JsonGenerator#write(BigInteger)} was called.
         *
         * @author leadpony
         */
        @Nested
        public class AfterWritingBigInteger extends GeneratorSupplier
            implements AllWritingTest {

            @BeforeEach
            public void setUp() {
                get().write(new BigInteger("42"));
            }
        }

        /**
         * After {@code JsonGenerator#write(BigDecimal)} was called.
         *
         * @author leadpony
         */
        @Nested
        public class AfterWritingBigDecimal extends GeneratorSupplier
            implements AllWritingTest {

            @BeforeEach
            public void setUp() {
                get().write(new BigDecimal("3.14"));
            }
        }

        /**
         * After {@code JsonGenerator#write(int)} was called.
         *
         * @author leadpony
         */
        @Nested
        public class AfterWritingInt extends GeneratorSupplier
            implements AllWritingTest {

            @BeforeEach
            public void setUp() {
                get().write(123);
            }
        }

        /**
         * After {@code JsonGenerator#write(long)} was called.
         *
         * @author leadpony
         */
        @Nested
        public class AfterWritingLong extends GeneratorSupplier
            implements AllWritingTest {

            @BeforeEach
            public void setUp() {
                get().write(123L);
            }
        }

        /**
         * After {@code JsonGenerator#write(double)} was called.
         *
         * @author leadpony
         */
        @Nested
        public class AfterWritingDouble extends GeneratorSupplier
            implements AllWritingTest {

            @BeforeEach
            public void setUp() {
                get().write(3.14);
            }
        }

        /**
         * After {@code JsonGenerator#write(boolean)} was called.
         *
         * @author leadpony
         */
        @Nested
        public class AfterWritingBoolean extends GeneratorSupplier
            implements AllWritingTest {

            @BeforeEach
            public void setUp() {
                get().write(true);
            }
        }

        /**
         * After {@code JsonGenerator#writeNull()} was called.
         *
         * @author leadpony
         */
        @Nested
        public class AfterWritingNull extends GeneratorSupplier
            implements AllWritingTest {

            @BeforeEach
            public void setUp() {
                get().writeNull();
            }
        }

        /**
         * After {@code JsonGenerator#writeStartArray()} was called.
         *
         * @author leadpony
         */
        @Nested
        public class AfterWritingStartArray extends GeneratorSupplier
            implements WritingNameTest, ClosingTest {

            @BeforeEach
            public void setUp() {
                get().writeStartArray();
            }

            /**
             * After writing the first item.
             *
             * @author leadpony
             */
            @Nested
            public class AfterWritingFirstItem extends GeneratorSupplier
                implements WritingNameTest, ClosingTest {

                @BeforeEach
                public void setUp() {
                    get().write(123);
                }

                /**
                 * After {@code JsonGenerator#writeEnd()} was called.
                 *
                 * @author leadpony
                 */
                @Nested
                public class AfterWritingEnd extends GeneratorSupplier
                    implements AllWritingTest {

                    @BeforeEach
                    public void setUp() {
                        get().writeEnd();
                    }
                }
            }

            /**
             * After {@code JsonGenerator#writeEnd()} was called without items.
             *
             * @author leadpony
             */
            @Nested
            public class AfterWritingEnd extends GeneratorSupplier
                implements AllWritingTest {

                @BeforeEach
                public void setUp() {
                    get().writeEnd();
                }
            }
        }

        /**
         * After {@code JsonGenerator#writeStartObject()} was called.
         *
         * @author leadpony
         */
        @Nested
        public class AfterWritingStartObject extends GeneratorSupplier
            implements WritingValueTest, ClosingTest {

            @BeforeEach
            public void setUp() {
                get().writeStartObject();
            }

            /**
             * After writing a key of the first property.
             *
             * @author leadpony
             */
            @Nested
            public class AfterWritingKey extends GeneratorSupplier
                implements WritingNameTest, WritingEndTest, ClosingTest {

                @BeforeEach
                public void setUp() {
                    get().writeKey("key");
                }

                /**
                 * Johnzon fails this test.
                 */
                @Test
                public void writeShouldStringValue() {
                    assertThatCode(() -> {
                        get().write("value");
                    }).doesNotThrowAnyException();
                }

                /**
                 * After writing a value of the first property.
                 *
                 * @author leadpony
                 */
                @Nested
                public class AfterWritingValue extends GeneratorSupplier
                    implements WritingValueTest, ClosingTest {

                    @BeforeEach
                    public void setUp() {
                        try {
                            get().write("value");
                        } catch (JsonGenerationException e) {
                            // Johnzon throws this.
                            assumeFalse(false);
                        }
                    }

                    /**
                     * After {@code JsonGenerator#writeEnd} was called.
                     *
                     * @author leadpony
                     */
                    @Nested
                    public class AfterWritingEnd extends GeneratorSupplier
                        implements AllWritingTest {

                        @BeforeEach
                        public void setUp() {
                            try {
                                get().writeEnd();
                            } catch (JsonGenerationException e) {
                                // Johnzon throws this.
                                assumeFalse(false);
                            }
                        }
                    }
                }
            }

            /**
             * After writing the first pair of key and value.
             *
             * @author leadpony
             */
            @Nested
            public class AfterWritingKeyValue extends GeneratorSupplier
                implements WritingValueTest, ClosingTest {

                @BeforeEach
                public void setUp() {
                    get().write("key", "value");
                }

                /**
                 * After {@code JsonGenerator#writeEnd} was called.
                 *
                 * @author leadpony
                 */
                @Nested
                public class AfterWritingEnd extends GeneratorSupplier
                    implements AllWritingTest {

                    @BeforeEach
                    public void setUp() {
                        get().writeEnd();
                    }
                }
            }

            /**
             * After {@code JsonGenerator#writeEnd} was called without any properties.
             *
             * @author leadpony
             */
            @Nested
            public class AfterWritingEnd extends GeneratorSupplier
                implements AllWritingTest {

                @BeforeEach
                public void setUp() {
                    get().writeEnd();
                }
            }
        }
    }
}
