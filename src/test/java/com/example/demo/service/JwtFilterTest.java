package com.example.demo.service;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.example.demo.config.JwtFilter;
import com.example.demo.config.JwtUtil;

import java.io.IOException;

import static org.mockito.Mockito.*;

public class JwtFilterTest {

    @Test
    public void testDoFilterWithValidToken() throws ServletException, IOException {
        // Arrange
        String token = JwtUtil.generateToken("lucas@example.com");

        JwtFilter filter = new JwtFilter();

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);

        // Act
        filter.doFilterInternal(request, response, chain);

        // Assert: Verifica se passou para o próximo filtro
        verify(chain, times(1)).doFilter(request, response);
    }

    @Test
    public void testDoFilterWithInvalidToken() throws ServletException, IOException {
        // Arrange
        String invalidToken = "Bearer abc.invalid.token";

        JwtFilter filter = new JwtFilter();

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        when(request.getHeader("Authorization")).thenReturn(invalidToken);

        // Act
        filter.doFilterInternal(request, response, chain);

        // Assert: Ainda deve continuar o fluxo mesmo se o token for inválido
        verify(chain, times(1)).doFilter(request, response);
    }

    @Test
    public void testDoFilterWithoutAuthorizationHeader() throws ServletException, IOException {
        // Arrange
        JwtFilter filter = new JwtFilter();

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        when(request.getHeader("Authorization")).thenReturn(null);

        // Act
        filter.doFilterInternal(request, response, chain);

        // Assert
        verify(chain, times(1)).doFilter(request, response);
    }
}
