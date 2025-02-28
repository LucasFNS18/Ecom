package com.example.demo.config;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class JwtUtil {

    private static final String SECRET_KEY = "mySuperSecretKeyForJWTThatMustBeAtLeast32CharactersLong!";
    private static final long EXPIRATION_TIME = 86400000; // 1 dia (24 horas)

    private static final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    // Método para gerar um token JWT
    public static String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256) // Corrigido
                .compact();
    }

    // Método para validar um token JWT
    public static boolean validateToken(String token) {
        try {
            Jwts.parserBuilder() // Corrigido
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    // Método para extrair o nome de usuário do token
    public static String extractUsername(String token) {
        return Jwts.parserBuilder() // Corrigido
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}