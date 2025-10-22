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

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.castlemock.model.core.utility.parser.expression.argument.ExpressionArgument;
import com.castlemock.model.core.utility.parser.expression.argument.ExpressionArgumentString;
import com.jayway.jsonpath.JsonPath;

import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * JWT Expression generates JSON Web Tokens with customizable claims and algorithms.
 * Supports HMAC algorithms (HS256, HS384, HS512) with custom claims and expiration.
 *
 * Syntax: ${JWT(algorithm="HS512", secret="key", exp="+300", claim1="value1", claim2="value2")}
 *
 * Parameters:
 * - algorithm: JWT algorithm (HS256, HS384, HS512) - defaults to HS256
 * - secret: Secret key for HMAC signing - defaults to "default-secret"
 * - exp: Expiration in seconds from now (e.g., "+300" for 5 minutes) - defaults to "+3600"
 * - Any other parameters become JWT claims
 *
 * Auto-generated claims:
 * - iat: issued at (current timestamp)
 * - jti: JWT ID (random UUID)
 * - exp: expiration timestamp (iat + exp parameter)
 *
 * @since 1.69.2
 */
public final class JwtExpression extends AbstractExpression {

    public static final String IDENTIFIER = "JWT";
    private static final String ALGORITHM_ARGUMENT = "algorithm";
    private static final String SECRET_ARGUMENT = "secret";
    private static final String EXPIRATION_ARGUMENT = "exp";

    private static final String DEFAULT_ALGORITHM = "HS256";
    private static final String DEFAULT_SECRET = "default-secret";
    private static final String DEFAULT_EXPIRATION = "+3600"; // 1 hour

    @Override
    public String transform(final ExpressionInput input) {
        try {
            final String algorithmName = getArgumentString(input, ALGORITHM_ARGUMENT, DEFAULT_ALGORITHM);
            final String secret = getArgumentString(input, SECRET_ARGUMENT, DEFAULT_SECRET);
            final String expiration = getArgumentString(input, EXPIRATION_ARGUMENT, DEFAULT_EXPIRATION);

            final Algorithm algorithm = getAlgorithm(algorithmName, secret);
            final long now = Instant.now().getEpochSecond();
            final long exp = now + parseExpirationSeconds(expiration);

            JWTCreator.Builder jwtBuilder = JWT.create()
                    .withIssuedAt(new Date(now * 1000))
                    .withExpiresAt(new Date(exp * 1000))
                    .withJWTId(UUID.randomUUID().toString());

            // Add custom claims from all other arguments
            Map<String, ExpressionArgument<?>> arguments = input.getArguments();
            for (Map.Entry<String, ExpressionArgument<?>> entry : arguments.entrySet()) {
                String argumentIdentifier = entry.getKey();
                if (!isReservedArgument(argumentIdentifier)) {
                    ExpressionArgument<?> arg = entry.getValue();
                    if (arg instanceof ExpressionArgumentString) {
                        String value = ((ExpressionArgumentString) arg).getValue();
                        if (value != null && !value.trim().isEmpty()) {
                            // Resolve dynamic values if they start with @
                            String resolvedValue = resolveDynamicValue(value, input);
                            // Try to parse as boolean or number, otherwise use as string
                            addClaimWithTypeInference(jwtBuilder, argumentIdentifier, resolvedValue);
                        }
                    }
                }
            }

            return jwtBuilder.sign(algorithm);

        } catch (Exception e) {
            // On any error, return a placeholder token
            return "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlcnJvciI6InRva2VuX2dlbmVyYXRpb25fZmFpbGVkIn0.invalid";
        }
    }

    private Algorithm getAlgorithm(final String algorithmName, final String secret) {
        return switch (algorithmName.toUpperCase()) {
            case "HS256" -> Algorithm.HMAC256(secret);
            case "HS384" -> Algorithm.HMAC384(secret);
            case "HS512" -> Algorithm.HMAC512(secret);
            default -> Algorithm.HMAC256(secret); // fallback
        };
    }

    private long parseExpirationSeconds(final String expiration) {
        try {
            // Remove '+' prefix if present and parse as seconds
            String exp = expiration.startsWith("+") ? expiration.substring(1) : expiration;
            return Long.parseLong(exp);
        } catch (NumberFormatException e) {
            return 3600; // default 1 hour
        }
    }

    private void addClaimWithTypeInference(JWTCreator.Builder jwtBuilder, final String key, final String value) {
        // Try to infer the type of the value
        if ("true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value)) {
            jwtBuilder.withClaim(key, Boolean.parseBoolean(value));
        } else {
            try {
                // Try as integer first
                long longValue = Long.parseLong(value);
                jwtBuilder.withClaim(key, longValue);
            } catch (NumberFormatException e1) {
                try {
                    // Try as double
                    double doubleValue = Double.parseDouble(value);
                    jwtBuilder.withClaim(key, doubleValue);
                } catch (NumberFormatException e2) {
                    // Use as string
                    jwtBuilder.withClaim(key, value);
                }
            }
        }
    }

    private boolean isReservedArgument(final String argumentIdentifier) {
        return ALGORITHM_ARGUMENT.equals(argumentIdentifier) ||
               SECRET_ARGUMENT.equals(argumentIdentifier) ||
               EXPIRATION_ARGUMENT.equals(argumentIdentifier);
    }

    private String getArgumentString(final ExpressionInput input, final String argumentIdentifier, final String defaultValue) {
        ExpressionArgument<?> arg = input.getArgument(argumentIdentifier);
        if (arg instanceof ExpressionArgumentString) {
            String value = ((ExpressionArgumentString) arg).getValue();
            return value != null && !value.trim().isEmpty() ? value : defaultValue;
        }
        return defaultValue;
    }

    /**
     * Resolves dynamic values that start with @ prefix
     * Supported patterns:
     * - @body.fieldName -> Extract field from JSON body using JsonPath $.fieldName
     * - @query.paramName -> Extract query parameter (future implementation)
     * - @path.paramName -> Extract path parameter (future implementation)
     * - @header.headerName -> Extract header value (future implementation)
     */
    private String resolveDynamicValue(final String value, final ExpressionInput input) {
        if (value == null || !value.startsWith("@")) {
            return value; // Return as-is if not a dynamic value
        }

        try {
            if (value.startsWith("@body.")) {
                return resolveBodyField(value.substring(6), input); // Remove "@body." prefix
            }
            // Future: Add support for @query., @path., @header.

            // If no pattern matches, return original value
            return value;
        } catch (Exception e) {
            // On any error, return the original value
            return value;
        }
    }

    /**
     * Extracts a field from the JSON request body using JsonPath
     */
    private String resolveBodyField(final String fieldPath, final ExpressionInput input) {
        try {
            // Get the request body from the input context
            ExpressionArgument<?> bodyArg = input.getArgument("BODY");
            if (bodyArg instanceof ExpressionArgumentString) {
                String jsonBody = ((ExpressionArgumentString) bodyArg).getValue();
                if (jsonBody != null && !jsonBody.trim().isEmpty()) {
                    // Use JsonPath to extract the field
                    String jsonPath = "$." + fieldPath;
                    Object extractedValue = JsonPath.read(jsonBody, jsonPath);
                    return extractedValue != null ? extractedValue.toString() : "";
                }
            }
            return "";
        } catch (Exception e) {
            // If JsonPath extraction fails, return empty string
            return "";
        }
    }

    @Override
    public boolean match(final String identifier) {
        return IDENTIFIER.equalsIgnoreCase(identifier);
    }
}