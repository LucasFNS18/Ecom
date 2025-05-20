package com.example.demo.service;

import org.junit.jupiter.api.Test;
import com.example.demo.config.JwtUtil;

import static org.junit.jupiter.api.Assertions.*;

public class JwtUtilTest {

    @Test
    void testGenerateTokenAndExtractUsername() {
        // 🧪 Gera um token JWT e testa se é possível extrair corretamente o e-mail do usuário

        String username = "lucas@example.com";

        // Geração do token
        String token = JwtUtil.generateToken(username);

        // Verificações:
        assertNotNull(token); // o token não pode ser nulo
        assertTrue(token.startsWith("ey")); // tokens JWT válidos normalmente começam com "ey"

        // Extração do username a partir do token
        String extractedUsername = JwtUtil.extractUsername(token);

        // Verifica se o username extraído é igual ao original
        assertEquals(username, extractedUsername);
    }

    @Test
    void testValidateValidToken() {
        // 🧪 Gera um token válido e testa se ele passa na validação

        String token = JwtUtil.generateToken("lucas@example.com");

        // O método validateToken deve retornar true para um token válido
        assertTrue(JwtUtil.validateToken(token));
    }

    @Test
    void testValidateInvalidToken() {
        // 🧪 Testa a validação com um token completamente inválido (estrutura incorreta)

        String invalidToken = "invalid.jwt.token";

        // O método deve retornar false pois o token não é válido
        assertFalse(JwtUtil.validateToken(invalidToken));
    }

    @Test
    void testExpiredTokenShouldFailValidation() throws InterruptedException {
        // 🧪 Testa um token "falsamente expirado" para simular falha de validação

        // Aqui não estamos realmente esperando a expiração, mas corrompendo o token
        // adicionando texto no final para invalidá-lo
        String fakeExpiredToken = JwtUtil.generateToken("lucas@example.com") + "tampered";

        // Como o token foi adulterado, a validação deve falhar
        assertFalse(JwtUtil.validateToken(fakeExpiredToken));
    }
}
