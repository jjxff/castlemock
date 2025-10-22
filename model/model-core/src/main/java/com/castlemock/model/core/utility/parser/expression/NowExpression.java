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
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * {@link NowExpression} is an {@link Expression} that will
 * transform an input string into the current date and time with optional offset.
 *
 * Supported syntax:
 * - ${NOW} - current date and time in ISO format
 * - ${NOW offset='20 seconds'} - current date and time plus 20 seconds
 * - ${NOW offset='-30 minutes'} - current date and time minus 30 minutes
 *
 * Supported time units: seconds, minutes, hours, days
 *
 * @author Castle Mock Contributors
 * @since 1.69.2
 */
public class NowExpression extends AbstractExpression {

    public static final String IDENTIFIER = "NOW";
    private static final Pattern OFFSET_PATTERN = Pattern.compile("^([+-]?\\d+)\\s*(seconds?|minutes?|hours?|days?)$", Pattern.CASE_INSENSITIVE);

    /**
     * The transform method provides the functionality to transform a provided input.
     * @param input The input string that will be transformed.
     * @return A transformed input with current date/time (optionally with offset).
     */
    @Override
    public String transform(final ExpressionInput input) {
        Instant now = Instant.now();

        // Check for offset argument
        ExpressionArgument<?> offsetArg = input.getArgument("offset");
        if (offsetArg instanceof ExpressionArgumentString) {
            String offsetValue = ((ExpressionArgumentString) offsetArg).getValue();
            now = applyOffset(now, offsetValue);
        }

        return DateTimeFormatter.ISO_INSTANT.format(now);
    }

    /**
     * Apply time offset to the given Instant.
     * @param instant The base instant
     * @param offsetString The offset string (e.g., "20 seconds", "-30 minutes")
     * @return The instant with offset applied
     */
    private Instant applyOffset(Instant instant, String offsetString) {
        Matcher matcher = OFFSET_PATTERN.matcher(offsetString.trim());
        if (!matcher.matches()) {
            // If offset format is invalid, return original instant
            return instant;
        }

        long amount = Long.parseLong(matcher.group(1));
        String unit = matcher.group(2).toLowerCase();

        switch (unit) {
            case "second":
            case "seconds":
                return instant.plusSeconds(amount);
            case "minute":
            case "minutes":
                return instant.plusSeconds(amount * 60);
            case "hour":
            case "hours":
                return instant.plusSeconds(amount * 3600);
            case "day":
            case "days":
                return instant.plusSeconds(amount * 86400);
            default:
                return instant;
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