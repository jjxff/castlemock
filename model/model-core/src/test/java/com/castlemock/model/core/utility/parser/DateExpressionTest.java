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

import com.castlemock.model.core.utility.parser.expression.DateExpression;
import com.castlemock.model.core.utility.parser.expression.ExpressionInput;
import com.castlemock.model.core.utility.parser.expression.argument.ExpressionArgumentString;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

public class DateExpressionTest {

    @Test
    public void testTransformWithoutOffset() {
        final DateExpression expression = new DateExpression();
        final ExpressionInput expressionInput = new ExpressionInput(DateExpression.IDENTIFIER);
        final String result = expression.transform(expressionInput);

        assertNotNull(result);
        // Verify it's a valid ISO date format
        assertDoesNotThrow(() -> LocalDate.parse(result, DateTimeFormatter.ISO_LOCAL_DATE));

        // Should be today's date
        LocalDate today = LocalDate.now();
        assertEquals(today.toString(), result);
    }

    @Test
    public void testTransformWithDaysOffset() {
        final DateExpression expression = new DateExpression();
        final ExpressionInput expressionInput = new ExpressionInput(DateExpression.IDENTIFIER);
        expressionInput.addArgument("offset", new ExpressionArgumentString("5 days"));

        final String result = expression.transform(expressionInput);
        assertNotNull(result);

        LocalDate resultDate = LocalDate.parse(result, DateTimeFormatter.ISO_LOCAL_DATE);
        LocalDate expectedDate = LocalDate.now().plusDays(5);
        assertEquals(expectedDate, resultDate);
    }

    @Test
    public void testTransformWithNegativeDaysOffset() {
        final DateExpression expression = new DateExpression();
        final ExpressionInput expressionInput = new ExpressionInput(DateExpression.IDENTIFIER);
        expressionInput.addArgument("offset", new ExpressionArgumentString("-10 days"));

        final String result = expression.transform(expressionInput);
        assertNotNull(result);

        LocalDate resultDate = LocalDate.parse(result, DateTimeFormatter.ISO_LOCAL_DATE);
        LocalDate expectedDate = LocalDate.now().minusDays(10);
        assertEquals(expectedDate, resultDate);
    }

    @Test
    public void testTransformWithHoursOffset() {
        final DateExpression expression = new DateExpression();
        final ExpressionInput expressionInput = new ExpressionInput(DateExpression.IDENTIFIER);
        expressionInput.addArgument("offset", new ExpressionArgumentString("25 hours")); // More than 24 hours = 1 day

        final String result = expression.transform(expressionInput);
        assertNotNull(result);

        LocalDate resultDate = LocalDate.parse(result, DateTimeFormatter.ISO_LOCAL_DATE);
        LocalDate expectedDate = LocalDate.now().plusDays(1); // 25/24 = 1 day
        assertEquals(expectedDate, resultDate);
    }

    @Test
    public void testTransformWithInvalidOffset() {
        final DateExpression expression = new DateExpression();
        final ExpressionInput expressionInput = new ExpressionInput(DateExpression.IDENTIFIER);
        expressionInput.addArgument("offset", new ExpressionArgumentString("invalid offset"));

        final String result = expression.transform(expressionInput);
        assertNotNull(result);

        // Should return current date when offset is invalid
        LocalDate resultDate = LocalDate.parse(result, DateTimeFormatter.ISO_LOCAL_DATE);
        LocalDate today = LocalDate.now();
        assertEquals(today, resultDate);
    }

    @Test
    public void testMatch() {
        final DateExpression expression = new DateExpression();
        assertTrue(expression.match("DATE"));
        assertTrue(expression.match("date"));
        assertTrue(expression.match("Date"));
        assertFalse(expression.match("RANDOM_DATE"));
        assertFalse(expression.match("NOW"));
        assertFalse(expression.match("TIME"));
    }
}