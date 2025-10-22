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

package com.castlemock.model.core.utility.parser.expression;

import com.castlemock.model.core.utility.parser.expression.argument.ExpressionArgument;
import com.castlemock.model.core.utility.parser.expression.argument.ExpressionArgumentString;

import java.security.SecureRandom;

/**
 * Random Hex Expression generates cryptographically secure random hexadecimal strings.
 * Perfect for generating API keys, tokens, transaction IDs, and other unique identifiers.
 *
 * Syntax: ${RANDOM_HEX(length="64")}
 *
 * Parameters:
 * - length: Length of the hexadecimal string (default: 32)
 *   Must be a positive integer. Common values:
 *   - 16: Short identifiers (8 bytes)
 *   - 32: Medium identifiers (16 bytes)
 *   - 64: Long identifiers (32 bytes, like SHA-256 hashes)
 *   - 128: Extra long identifiers (64 bytes, like SHA-512 hashes)
 *
 * Examples:
 * - ${RANDOM_HEX()} -> "a1b2c3d4e5f67890abcdef1234567890"
 * - ${RANDOM_HEX(length="16")} -> "a1b2c3d4e5f67890"
 * - ${RANDOM_HEX(length="64")} -> "cbd198b7ddc8e078435e99aea97546110f98b5011f107295d977c590d0960ec3f"
 *
 * @since 1.69.3
 */
public final class RandomHexExpression extends AbstractExpression {

    public static final String IDENTIFIER = "RANDOM_HEX";
    private static final String LENGTH_ARGUMENT = "length";
    private static final int DEFAULT_LENGTH = 32;
    private static final SecureRandom secureRandom = new SecureRandom();

    @Override
    public String transform(final ExpressionInput input) {
        try {
            final int length = parseLength(input);
            return generateRandomHex(length);
        } catch (Exception e) {
            // On any error, return default length hex string
            return generateRandomHex(DEFAULT_LENGTH);
        }
    }

    private int parseLength(final ExpressionInput input) {
        final ExpressionArgument<?> lengthArg = input.getArgument(LENGTH_ARGUMENT);
        if (lengthArg instanceof ExpressionArgumentString) {
            final String lengthValue = ((ExpressionArgumentString) lengthArg).getValue();
            if (lengthValue != null && !lengthValue.trim().isEmpty()) {
                try {
                    final int length = Integer.parseInt(lengthValue.trim());
                    if (length > 0 && length <= 1024) { // Reasonable upper limit
                        return length;
                    }
                } catch (NumberFormatException e) {
                    // Fall through to default
                }
            }
        }
        return DEFAULT_LENGTH;
    }

    private String generateRandomHex(final int length) {
        final StringBuilder hexString = new StringBuilder();
        final int bytesNeeded = (length + 1) / 2; // Round up for odd lengths
        final byte[] randomBytes = new byte[bytesNeeded];

        secureRandom.nextBytes(randomBytes);

        for (byte b : randomBytes) {
            hexString.append(String.format("%02x", b));
        }

        // If length is odd, trim the last character
        if (hexString.length() > length) {
            return hexString.substring(0, length);
        }

        return hexString.toString();
    }

    @Override
    public boolean match(final String identifier) {
        return IDENTIFIER.equalsIgnoreCase(identifier);
    }
}