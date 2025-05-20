package com.example.demo.service;

import com.example.demo.config.JwtFilter;
import com.example.demo.config.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class JwtFilterTest {

    @BeforeEach
    public void setup() {
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    public void cleanup() {
        SecurityContextHolder.clearContext();
    }

    @Test
    public void testDoFilterWithValidToken() throws ServletException, IOException {
        // 🧪 Verifica se um token válido autentica o usuário corretamente

        String token = JwtUtil.generateToken("lucas@example.com");
        JwtFilter filter = new JwtFilter();

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);

        // ✅ Usa o método público doFilter
        filter.doFilter(request, response, chain);

        // ✅ Verifica se a requisição continuou
        verify(chain, times(1)).doFilter(request, response);

        // ✅ Verifica se a autenticação foi preenchida corretamente
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication);
        assertEquals("lucas@example.com", authentication.getPrincipal());
    }

    @Test
    public void testDoFilterWithInvalidToken() throws ServletException, IOException {
        String invalidToken = "Bearer abc.invalid.token";
        JwtFilter filter = new JwtFilter();

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        when(request.getHeader("Authorization")).thenReturn(invalidToken);

        // ✅ Usa o método público doFilter
        filter.doFilter(request, response, chain);

        // Mesmo com token inválido, o filtro não deve barrar
        verify(chain, times(1)).doFilter(request, response);

        // ✅ Confirma que nenhum usuário foi autenticado
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    public void testDoFilterWithoutAuthorizationHeader() throws ServletException, IOException {
        JwtFilter filter = new JwtFilter();

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        when(request.getHeader("Authorization")).thenReturn(null);

        // ✅ Usa o método público doFilter
        filter.doFilter(request, response, chain);

        verify(chain, times(1)).doFilter(request, response);

        // ✅ Confirma que nenhum usuário foi autenticado
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}
