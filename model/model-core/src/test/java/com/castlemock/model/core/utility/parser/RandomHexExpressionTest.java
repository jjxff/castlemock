/*
 * Copyright 2025 Castle Mock Contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.castlemock.model.core.utility.parser;

import com.castlemock.model.core.utility.parser.expression.ExpressionInput;
import com.castlemock.model.core.utility.parser.expression.RandomHexExpression;
import com.castlemock.model.core.utility.parser.expression.argument.ExpressionArgumentString;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

class RandomHexExpressionTest {

    private final RandomHexExpression expression = new RandomHexExpression();

    @Test
    @DisplayName("Should match RANDOM_HEX identifier")
    void testMatch() {
        assertTrue(expression.match("RANDOM_HEX"));
        assertTrue(expression.match("random_hex"));
        assertTrue(expression.match("Random_Hex"));
        assertFalse(expression.match("RANDOM_STRING"));
        assertFalse(expression.match("HEX"));
        assertFalse(expression.match(""));
    }

    @Test
    @DisplayName("Should generate hex string with default length")
    void testDefaultLength() {
        final ExpressionInput input = new ExpressionInput(RandomHexExpression.IDENTIFIER);
        final String result = expression.transform(input);

        assertNotNull(result);
        assertEquals(32, result.length());
        assertTrue(result.matches("^[a-f0-9]+$"));
    }

    @Test
    @DisplayName("Should generate hex string with custom length")
    void testCustomLength() {
        final ExpressionInput input = new ExpressionInput(RandomHexExpression.IDENTIFIER);
        input.addArgument("length", new ExpressionArgumentString("64"));

        final String result = expression.transform(input);

        assertNotNull(result);
        assertEquals(64, result.length());
        assertTrue(result.matches("^[a-f0-9]+$"));
    }

    @Test
    @DisplayName("Should generate hex string with length 16")
    void testLength16() {
        final ExpressionInput input = new ExpressionInput(RandomHexExpression.IDENTIFIER);
        input.addArgument("length", new ExpressionArgumentString("16"));

        final String result = expression.transform(input);

        assertNotNull(result);
        assertEquals(16, result.length());
        assertTrue(result.matches("^[a-f0-9]+$"));
    }

    @Test
    @DisplayName("Should generate hex string with length 128")
    void testLength128() {
        final ExpressionInput input = new ExpressionInput(RandomHexExpression.IDENTIFIER);
        input.addArgument("length", new ExpressionArgumentString("128"));

        final String result = expression.transform(input);

        assertNotNull(result);
        assertEquals(128, result.length());
        assertTrue(result.matches("^[a-f0-9]+$"));
    }

    @Test
    @DisplayName("Should handle odd length correctly")
    void testOddLength() {
        final ExpressionInput input = new ExpressionInput(RandomHexExpression.IDENTIFIER);
        input.addArgument("length", new ExpressionArgumentString("7"));

        final String result = expression.transform(input);

        assertNotNull(result);
        assertEquals(7, result.length());
        assertTrue(result.matches("^[a-f0-9]+$"));
    }

    @Test
    @DisplayName("Should handle invalid length gracefully")
    void testInvalidLength() {
        final ExpressionInput input = new ExpressionInput(RandomHexExpression.IDENTIFIER);
        input.addArgument("length", new ExpressionArgumentString("invalid"));

        final String result = expression.transform(input);

        assertNotNull(result);
        assertEquals(32, result.length()); // Should fallback to default
        assertTrue(result.matches("^[a-f0-9]+$"));
    }

    @Test
    @DisplayName("Should handle negative length gracefully")
    void testNegativeLength() {
        final ExpressionInput input = new ExpressionInput(RandomHexExpression.IDENTIFIER);
        input.addArgument("length", new ExpressionArgumentString("-10"));

        final String result = expression.transform(input);

        assertNotNull(result);
        assertEquals(32, result.length()); // Should fallback to default
        assertTrue(result.matches("^[a-f0-9]+$"));
    }

    @Test
    @DisplayName("Should handle zero length gracefully")
    void testZeroLength() {
        final ExpressionInput input = new ExpressionInput(RandomHexExpression.IDENTIFIER);
        input.addArgument("length", new ExpressionArgumentString("0"));

        final String result = expression.transform(input);

        assertNotNull(result);
        assertEquals(32, result.length()); // Should fallback to default
        assertTrue(result.matches("^[a-f0-9]+$"));
    }

    @Test
    @DisplayName("Should handle very large length gracefully")
    void testVeryLargeLength() {
        final ExpressionInput input = new ExpressionInput(RandomHexExpression.IDENTIFIER);
        input.addArgument("length", new ExpressionArgumentString("2000"));

        final String result = expression.transform(input);

        assertNotNull(result);
        assertEquals(32, result.length()); // Should fallback to default due to limit
        assertTrue(result.matches("^[a-f0-9]+$"));
    }

    @Test
    @DisplayName("Should generate unique strings on multiple calls")
    void testUniqueness() {
        final ExpressionInput input = new ExpressionInput(RandomHexExpression.IDENTIFIER);
        input.addArgument("length", new ExpressionArgumentString("32"));

        final String result1 = expression.transform(input);
        final String result2 = expression.transform(input);
        final String result3 = expression.transform(input);

        assertNotEquals(result1, result2);
        assertNotEquals(result2, result3);
        assertNotEquals(result1, result3);
    }

    @Test
    @DisplayName("Should handle empty length parameter")
    void testEmptyLength() {
        final ExpressionInput input = new ExpressionInput(RandomHexExpression.IDENTIFIER);
        input.addArgument("length", new ExpressionArgumentString(""));

        final String result = expression.transform(input);

        assertNotNull(result);
        assertEquals(32, result.length()); // Should fallback to default
        assertTrue(result.matches("^[a-f0-9]+$"));
    }

    @Test
    @DisplayName("Should handle whitespace in length parameter")
    void testWhitespaceLength() {
        final ExpressionInput input = new ExpressionInput(RandomHexExpression.IDENTIFIER);
        input.addArgument("length", new ExpressionArgumentString("  64  "));

        final String result = expression.transform(input);

        assertNotNull(result);
        assertEquals(64, result.length());
        assertTrue(result.matches("^[a-f0-9]+$"));
    }

    @Test
    @DisplayName("Should generate example like cbd198b7...")
    void testExampleGeneration() {
        final ExpressionInput input = new ExpressionInput(RandomHexExpression.IDENTIFIER);
        input.addArgument("length", new ExpressionArgumentString("64"));

        final String result = expression.transform(input);

        assertNotNull(result);
        assertEquals(64, result.length());
        assertTrue(result.matches("^[a-f0-9]+$"));
        // Should look like: cbd198b7ddc8e078435e99aea97546110f98b5011f107295d977c590d0960ec3f
    }
}