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
import com.castlemock.model.core.utility.parser.expression.TimeExpression;
import com.castlemock.model.core.utility.parser.expression.argument.ExpressionArgumentString;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

public class TimeExpressionTest {

    @Test
    public void testTransformWithoutOffset() {
        final TimeExpression expression = new TimeExpression();
        final ExpressionInput expressionInput = new ExpressionInput(TimeExpression.IDENTIFIER);
        final String result = expression.transform(expressionInput);

        assertNotNull(result);
        // Verify it's a valid ISO time format
        assertDoesNotThrow(() -> LocalTime.parse(result, DateTimeFormatter.ISO_LOCAL_TIME));
    }

    @Test
    public void testTransformWithSecondsOffset() {
        final TimeExpression expression = new TimeExpression();
        final ExpressionInput expressionInput = new ExpressionInput(TimeExpression.IDENTIFIER);
        expressionInput.addArgument("offset", new ExpressionArgumentString("30 seconds"));

        final String result = expression.transform(expressionInput);
        assertNotNull(result);

        LocalTime resultTime = LocalTime.parse(result, DateTimeFormatter.ISO_LOCAL_TIME);
        LocalTime now = LocalTime.now();
        LocalTime expectedTime = now.plusSeconds(30);

        // Allow for a small difference due to execution time
        long diffSeconds = Math.abs(resultTime.toSecondOfDay() - expectedTime.toSecondOfDay());
        assertTrue(diffSeconds <= 2, "Expected offset around 30 seconds, got difference: " + diffSeconds);
    }

    @Test
    public void testTransformWithMinutesOffset() {
        final TimeExpression expression = new TimeExpression();
        final ExpressionInput expressionInput = new ExpressionInput(TimeExpression.IDENTIFIER);
        expressionInput.addArgument("offset", new ExpressionArgumentString("15 minutes"));

        final String result = expression.transform(expressionInput);
        assertNotNull(result);

        LocalTime resultTime = LocalTime.parse(result, DateTimeFormatter.ISO_LOCAL_TIME);
        LocalTime now = LocalTime.now();
        LocalTime expectedTime = now.plusMinutes(15);

        // Allow for a small difference due to execution time
        long diffMinutes = Math.abs((resultTime.toSecondOfDay() - expectedTime.toSecondOfDay()) / 60);
        assertTrue(diffMinutes <= 1, "Expected offset around 15 minutes, got difference: " + diffMinutes + " minutes");
    }

    @Test
    public void testTransformWithNegativeOffset() {
        final TimeExpression expression = new TimeExpression();
        final ExpressionInput expressionInput = new ExpressionInput(TimeExpression.IDENTIFIER);
        expressionInput.addArgument("offset", new ExpressionArgumentString("-45 minutes"));

        final String result = expression.transform(expressionInput);
        assertNotNull(result);

        LocalTime resultTime = LocalTime.parse(result, DateTimeFormatter.ISO_LOCAL_TIME);
        LocalTime now = LocalTime.now();
        LocalTime expectedTime = now.minusMinutes(45);

        // Allow for a small difference due to execution time
        long diffMinutes = Math.abs((resultTime.toSecondOfDay() - expectedTime.toSecondOfDay()) / 60);
        assertTrue(diffMinutes <= 1, "Expected offset around -45 minutes, got difference: " + diffMinutes + " minutes");
    }

    @Test
    public void testTransformWithHoursOffset() {
        final TimeExpression expression = new TimeExpression();
        final ExpressionInput expressionInput = new ExpressionInput(TimeExpression.IDENTIFIER);
        expressionInput.addArgument("offset", new ExpressionArgumentString("2 hours"));

        final String result = expression.transform(expressionInput);
        assertNotNull(result);

        LocalTime resultTime = LocalTime.parse(result, DateTimeFormatter.ISO_LOCAL_TIME);
        LocalTime now = LocalTime.now();
        LocalTime expectedTime = now.plusHours(2);

        // Allow for a small difference due to execution time
        long diffHours = Math.abs((resultTime.toSecondOfDay() - expectedTime.toSecondOfDay()) / 3600);
        assertTrue(diffHours <= 1, "Expected offset around 2 hours, got difference: " + diffHours + " hours");
    }

    @Test
    public void testTransformWithInvalidOffset() {
        final TimeExpression expression = new TimeExpression();
        final ExpressionInput expressionInput = new ExpressionInput(TimeExpression.IDENTIFIER);
        expressionInput.addArgument("offset", new ExpressionArgumentString("invalid offset"));

        final String result = expression.transform(expressionInput);
        assertNotNull(result);

        // Should return current time when offset is invalid
        LocalTime resultTime = LocalTime.parse(result, DateTimeFormatter.ISO_LOCAL_TIME);
        LocalTime now = LocalTime.now();
        long diffSeconds = Math.abs(resultTime.toSecondOfDay() - now.toSecondOfDay());
        assertTrue(diffSeconds <= 1, "Expected current time when offset is invalid");
    }

    @Test
    public void testMatch() {
        final TimeExpression expression = new TimeExpression();
        assertTrue(expression.match("TIME"));
        assertTrue(expression.match("time"));
        assertTrue(expression.match("Time"));
        assertFalse(expression.match("DATE"));
        assertFalse(expression.match("NOW"));
        assertFalse(expression.match("RANDOM_TIME"));
    }
}