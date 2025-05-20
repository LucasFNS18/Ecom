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
        // 🧪 Teste para verificar se um token válido permite o prosseguimento da requisição no filtro

        // Gerando um token JWT válido para o e-mail especificado
        String token = JwtUtil.generateToken("lucas@example.com");

        // Criando uma instância do filtro que será testado
        JwtFilter filter = new JwtFilter();

        // Mockando os objetos da requisição HTTP, resposta e cadeia de filtros
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        // Simulando o header "Authorization" com o token válido
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);

        // Executando o filtro
        filter.doFilterInternal(request, response, chain);

        // Verificando se o filtro permitiu que a requisição seguisse normalmente
        verify(chain, times(1)).doFilter(request, response);
    }

    @Test
    public void testDoFilterWithInvalidToken() throws ServletException, IOException {
        // 🧪 Teste para simular um token inválido no cabeçalho de autorização

        // Simulando um token malformado ou inválido
        String invalidToken = "Bearer abc.invalid.token";

        JwtFilter filter = new JwtFilter();

        // Mockando os objetos da requisição e cadeia de filtros
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        // Simulando o header Authorization com o token inválido
        when(request.getHeader("Authorization")).thenReturn(invalidToken);

        // Executando o filtro
        filter.doFilterInternal(request, response, chain);

        // Mesmo com token inválido, a requisição deve continuar (sem autenticar o usuário)
        verify(chain, times(1)).doFilter(request, response);
    }

    @Test
    public void testDoFilterWithoutAuthorizationHeader() throws ServletException, IOException {
        // 🧪 Teste para o caso em que não existe o cabeçalho Authorization na requisição

        JwtFilter filter = new JwtFilter();

        // Mockando os objetos da requisição, resposta e cadeia de filtros
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        // Simulando ausência do cabeçalho Authorization
        when(request.getHeader("Authorization")).thenReturn(null);

        // Executando o filtro
        filter.doFilterInternal(request, response, chain);

        // Mesmo sem header, o filtro deve deixar a requisição seguir normalmente
        verify(chain, times(1)).doFilter(request, response);
    }
}
