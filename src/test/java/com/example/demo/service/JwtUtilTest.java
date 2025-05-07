package com.example.demo.service;


import org.junit.jupiter.api.Test;

import com.example.demo.config.JwtUtil;

import static org.junit.jupiter.api.Assertions.*;

public class JwtUtilTest {

    @Test
    void testGenerateTokenAndExtractUsername() {
        String username = "lucas@example.com";
        String token = JwtUtil.generateToken(username);

        assertNotNull(token);
        assertTrue(token.startsWith("ey")); // tokens JWT começam com 'ey'

        String extractedUsername = JwtUtil.extractUsername(token);
        assertEquals(username, extractedUsername);
    }

    @Test
    void testValidateValidToken() {
        String token = JwtUtil.generateToken("lucas@example.com");

        assertTrue(JwtUtil.validateToken(token));
    }

    @Test
    void testValidateInvalidToken() {
        String invalidToken = "invalid.jwt.token";
        assertFalse(JwtUtil.validateToken(invalidToken));
    }

    @Test
    void testExpiredTokenShouldFailValidation() throws InterruptedException {
        // Simulando expiração imediata (opcional, caso queira adaptar a classe JwtUtil)
        // Aqui apenas forçamos um token inválido com estrutura correta
        String fakeExpiredToken = JwtUtil.generateToken("lucas@example.com") + "tampered";
        assertFalse(JwtUtil.validateToken(fakeExpiredToken));
    }
}
