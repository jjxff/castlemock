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

package com.castlemock.model.core.utility.parser.expression;

import com.castlemock.model.core.utility.parser.expression.argument.ExpressionArgument;
import com.castlemock.model.core.utility.parser.expression.argument.ExpressionArgumentString;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * {@link DateExpression} is an {@link Expression} that will
 * transform an input string into the current date with optional offset.
 *
 * Supported syntax:
 * - ${DATE} - current date in ISO format (YYYY-MM-DD)
 * - ${DATE offset='20 days'} - current date plus 20 days
 * - ${DATE offset='-30 days'} - current date minus 30 days
 *
 * Supported time units: seconds, minutes, hours, days
 *
 * @author Castle Mock Contributors
 * @since 1.69.2
 */
public class DateExpression extends AbstractExpression {

    public static final String IDENTIFIER = "DATE";
    private static final Pattern OFFSET_PATTERN = Pattern.compile("^([+-]?\\d+)\\s*(seconds?|minutes?|hours?|days?)$", Pattern.CASE_INSENSITIVE);

    /**
     * The transform method provides the functionality to transform a provided input.
     * @param input The input string that will be transformed.
     * @return A transformed input with current date (optionally with offset).
     */
    @Override
    public String transform(final ExpressionInput input) {
        LocalDate today = LocalDate.now();

        // Check for offset argument
        ExpressionArgument<?> offsetArg = input.getArgument("offset");
        if (offsetArg instanceof ExpressionArgumentString) {
            String offsetValue = ((ExpressionArgumentString) offsetArg).getValue();
            today = applyOffset(today, offsetValue);
        }

        return DateTimeFormatter.ISO_LOCAL_DATE.format(today);
    }

    /**
     * Apply time offset to the given LocalDate.
     * @param date The base date
     * @param offsetString The offset string (e.g., "20 days", "-30 days")
     * @return The date with offset applied
     */
    private LocalDate applyOffset(LocalDate date, String offsetString) {
        Matcher matcher = OFFSET_PATTERN.matcher(offsetString.trim());
        if (!matcher.matches()) {
            // If offset format is invalid, return original date
            return date;
        }

        long amount = Long.parseLong(matcher.group(1));
        String unit = matcher.group(2).toLowerCase();

        switch (unit) {
            case "second":
            case "seconds":
                // For dates, seconds are converted to days (1 day = 86400 seconds)
                return date.plusDays(amount / 86400);
            case "minute":
            case "minutes":
                // For dates, minutes are converted to days (1 day = 1440 minutes)
                return date.plusDays(amount / 1440);
            case "hour":
            case "hours":
                // For dates, hours are converted to days (1 day = 24 hours)
                return date.plusDays(amount / 24);
            case "day":
            case "days":
                return date.plusDays(amount);
            default:
                return date;
        }
    }

    /**
     * The match method is used to determine if an input string matches
     * the criteria to be transformed.
     * @param input The input that will be determined if it matches the criteria to be transformed.
     * @return True if the input string matches the criteria. False otherwise.
     */
    @Override
    public boolean match(final String input) {
        return IDENTIFIER.equalsIgnoreCase(input);
    }
}