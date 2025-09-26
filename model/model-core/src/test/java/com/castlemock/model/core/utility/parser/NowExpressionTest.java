/*
 * Copyright 2024 Castle Mock Contributors
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
import com.castlemock.model.core.utility.parser.expression.NowExpression;
import com.castlemock.model.core.utility.parser.expression.argument.ExpressionArgumentString;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

public class NowExpressionTest {

    @Test
    public void testTransformWithoutOffset() {
        final NowExpression expression = new NowExpression();
        final ExpressionInput expressionInput = new ExpressionInput(NowExpression.IDENTIFIER);
        final String result = expression.transform(expressionInput);

        assertNotNull(result);
        // Verify it's a valid ISO instant format
        assertDoesNotThrow(() -> Instant.parse(result));
    }

    @Test
    public void testTransformWithSecondsOffset() {
        final NowExpression expression = new NowExpression();
        final ExpressionInput expressionInput = new ExpressionInput(NowExpression.IDENTIFIER);
        expressionInput.addArgument("offset", new ExpressionArgumentString("20 seconds"));

        final String result = expression.transform(expressionInput);
        assertNotNull(result);

        // Verify it's a valid ISO instant format
        Instant resultInstant = Instant.parse(result);
        Instant now = Instant.now();

        // The result should be approximately 20 seconds from now (allowing for execution time)
        long diffSeconds = resultInstant.getEpochSecond() - now.getEpochSecond();
        assertTrue(diffSeconds >= 19 && diffSeconds <= 21, "Expected offset around 20 seconds, got: " + diffSeconds);
    }

    @Test
    public void testTransformWithNegativeOffset() {
        final NowExpression expression = new NowExpression();
        final ExpressionInput expressionInput = new ExpressionInput(NowExpression.IDENTIFIER);
        expressionInput.addArgument("offset", new ExpressionArgumentString("-30 minutes"));

        final String result = expression.transform(expressionInput);
        assertNotNull(result);

        Instant resultInstant = Instant.parse(result);
        Instant now = Instant.now();

        // The result should be approximately 30 minutes before now
        long diffMinutes = (now.getEpochSecond() - resultInstant.getEpochSecond()) / 60;
        assertTrue(diffMinutes >= 29 && diffMinutes <= 31, "Expected offset around -30 minutes, got: " + diffMinutes);
    }

    @Test
    public void testTransformWithInvalidOffset() {
        final NowExpression expression = new NowExpression();
        final ExpressionInput expressionInput = new ExpressionInput(NowExpression.IDENTIFIER);
        expressionInput.addArgument("offset", new ExpressionArgumentString("invalid offset"));

        final String result = expression.transform(expressionInput);
        assertNotNull(result);

        // Should return current time when offset is invalid
        Instant resultInstant = Instant.parse(result);
        Instant now = Instant.now();
        long diffSeconds = Math.abs(resultInstant.getEpochSecond() - now.getEpochSecond());
        assertTrue(diffSeconds <= 1, "Expected current time when offset is invalid");
    }

    @Test
    public void testMatch() {
        final NowExpression expression = new NowExpression();
        assertTrue(expression.match("NOW"));
        assertTrue(expression.match("now"));
        assertTrue(expression.match("Now"));
        assertFalse(expression.match("RANDOM_DATE_TIME"));
        assertFalse(expression.match("DATE"));
    }
}