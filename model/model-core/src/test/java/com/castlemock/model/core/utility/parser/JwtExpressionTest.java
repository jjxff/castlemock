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

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.castlemock.model.core.utility.parser.expression.ExpressionInput;
import com.castlemock.model.core.utility.parser.expression.JwtExpression;
import com.castlemock.model.core.utility.parser.expression.argument.ExpressionArgumentString;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class JwtExpressionTest {

    private final JwtExpression expression = new JwtExpression();

    @Test
    @DisplayName("Should match JWT identifier")
    void testMatch() {
        assertTrue(expression.match("JWT"));
        assertTrue(expression.match("jwt"));
        assertTrue(expression.match("Jwt"));
        assertFalse(expression.match("TOKEN"));
        assertFalse(expression.match(""));
    }

    @Test
    @DisplayName("Should generate JWT with default settings")
    void testDefaultJwt() {
        final ExpressionInput input = new ExpressionInput(JwtExpression.IDENTIFIER);
        final String token = expression.transform(input);

        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.contains("."));

        // Verify token can be decoded
        final DecodedJWT decoded = JWT.require(Algorithm.HMAC256("default-secret")).build().verify(token);
        assertNotNull(decoded.getId()); // jti
        assertNotNull(decoded.getIssuedAt()); // iat
        assertNotNull(decoded.getExpiresAt()); // exp
    }

    @Test
    @DisplayName("Should generate JWT with HS512 algorithm")
    void testHS512Algorithm() {
        final ExpressionInput input = new ExpressionInput(JwtExpression.IDENTIFIER);
        input.addArgument("algorithm", new ExpressionArgumentString("HS512"));
        input.addArgument("secret", new ExpressionArgumentString("test-secret-key"));

        final String token = expression.transform(input);

        // Verify token with HS512
        final DecodedJWT decoded = JWT.require(Algorithm.HMAC512("test-secret-key")).build().verify(token);
        assertEquals("HS512", decoded.getAlgorithm());
    }

    @Test
    @DisplayName("Should generate JWT with custom claims")
    void testCustomClaims() {
        final ExpressionInput input = new ExpressionInput(JwtExpression.IDENTIFIER);
        input.addArgument("secret", new ExpressionArgumentString("test-key"));
        input.addArgument("factor", new ExpressionArgumentString("javier.ordonez+1@fravega.com.ar"));
        input.addArgument("status", new ExpressionArgumentString("Código OTP verificado"));
        input.addArgument("is_valid", new ExpressionArgumentString("true"));
        input.addArgument("issuer", new ExpressionArgumentString("srv-authfactors-arq"));
        input.addArgument("user_id", new ExpressionArgumentString("12345"));
        input.addArgument("score", new ExpressionArgumentString("95.5"));

        final String token = expression.transform(input);
        final DecodedJWT decoded = JWT.require(Algorithm.HMAC256("test-key")).build().verify(token);

        // Check custom claims
        assertEquals("javier.ordonez+1@fravega.com.ar", decoded.getClaim("factor").asString());
        assertEquals("Código OTP verificado", decoded.getClaim("status").asString());
        assertTrue(decoded.getClaim("is_valid").asBoolean());
        assertEquals("srv-authfactors-arq", decoded.getClaim("issuer").asString());
        assertEquals(12345L, decoded.getClaim("user_id").asLong());
        assertEquals(95.5, decoded.getClaim("score").asDouble(), 0.01);
    }

    @Test
    @DisplayName("Should handle custom expiration")
    void testCustomExpiration() {
        final long now = Instant.now().getEpochSecond();
        final ExpressionInput input = new ExpressionInput(JwtExpression.IDENTIFIER);
        input.addArgument("secret", new ExpressionArgumentString("test-key"));
        input.addArgument("exp", new ExpressionArgumentString("+300")); // 5 minutes

        final String token = expression.transform(input);
        final DecodedJWT decoded = JWT.require(Algorithm.HMAC256("test-key")).build().verify(token);

        final long tokenExp = decoded.getExpiresAt().getTime() / 1000;
        final long tokenIat = decoded.getIssuedAt().getTime() / 1000;

        // Should expire in approximately 300 seconds (allow 5 second tolerance)
        assertTrue(Math.abs((tokenExp - tokenIat) - 300) <= 5);
        assertTrue(tokenIat >= now - 5 && tokenIat <= now + 5);
    }

    @Test
    @DisplayName("Should handle HS384 algorithm")
    void testHS384Algorithm() {
        final ExpressionInput input = new ExpressionInput(JwtExpression.IDENTIFIER);
        input.addArgument("algorithm", new ExpressionArgumentString("HS384"));
        input.addArgument("secret", new ExpressionArgumentString("hs384-secret"));

        final String token = expression.transform(input);
        final DecodedJWT decoded = JWT.require(Algorithm.HMAC384("hs384-secret")).build().verify(token);
        assertEquals("HS384", decoded.getAlgorithm());
    }

    @Test
    @DisplayName("Should generate unique JTI for each token")
    void testUniqueJti() {
        final ExpressionInput input1 = new ExpressionInput(JwtExpression.IDENTIFIER);
        input1.addArgument("secret", new ExpressionArgumentString("test-key"));

        final ExpressionInput input2 = new ExpressionInput(JwtExpression.IDENTIFIER);
        input2.addArgument("secret", new ExpressionArgumentString("test-key"));

        final String token1 = expression.transform(input1);
        final String token2 = expression.transform(input2);

        final DecodedJWT decoded1 = JWT.require(Algorithm.HMAC256("test-key")).build().verify(token1);
        final DecodedJWT decoded2 = JWT.require(Algorithm.HMAC256("test-key")).build().verify(token2);

        assertNotEquals(decoded1.getId(), decoded2.getId());
    }

    @Test
    @DisplayName("Should handle invalid algorithm gracefully")
    void testInvalidAlgorithm() {
        final ExpressionInput input = new ExpressionInput(JwtExpression.IDENTIFIER);
        input.addArgument("algorithm", new ExpressionArgumentString("INVALID"));
        input.addArgument("secret", new ExpressionArgumentString("test-key"));

        final String token = expression.transform(input);

        // Should fallback to HS256
        assertDoesNotThrow(() -> {
            JWT.require(Algorithm.HMAC256("test-key")).build().verify(token);
        });
    }

    @Test
    @DisplayName("Should create realistic JWT similar to example")
    void testRealisticJwt() {
        final ExpressionInput input = new ExpressionInput(JwtExpression.IDENTIFIER);
        input.addArgument("algorithm", new ExpressionArgumentString("HS512"));
        input.addArgument("secret", new ExpressionArgumentString("my-secret-key"));
        input.addArgument("exp", new ExpressionArgumentString("+300")); // 5 minutes
        input.addArgument("factor", new ExpressionArgumentString("javier.ordonez+1@fravega.com.ar"));
        input.addArgument("is_valid", new ExpressionArgumentString("true"));
        input.addArgument("issuer", new ExpressionArgumentString("srv-authfactors-arq"));
        input.addArgument("status", new ExpressionArgumentString("Código OTP verificado"));

        final String token = expression.transform(input);
        final DecodedJWT decoded = JWT.require(Algorithm.HMAC512("my-secret-key")).build().verify(token);

        // Verify structure
        assertNotNull(decoded.getId()); // jti
        assertNotNull(decoded.getIssuedAt()); // iat
        assertNotNull(decoded.getExpiresAt()); // exp

        // Verify custom claims
        assertEquals("javier.ordonez+1@fravega.com.ar", decoded.getClaim("factor").asString());
        assertTrue(decoded.getClaim("is_valid").asBoolean());
        assertEquals("srv-authfactors-arq", decoded.getClaim("issuer").asString());
        assertEquals("Código OTP verificado", decoded.getClaim("status").asString());

        // Verify algorithm
        assertEquals("HS512", decoded.getAlgorithm());
    }
}