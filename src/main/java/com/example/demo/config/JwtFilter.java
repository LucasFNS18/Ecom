package com.example.demo.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // Libera rotas públicas
        String path = request.getRequestURI();
        if (path.startsWith("/auth") || path.startsWith("/h2-console")) {
            chain.doFilter(request, response);
            return;
        }

        // Aqui iria a validação do JWT, se necessário
        // Mas como ainda não está implementada corretamente, deixamos passar

        chain.doFilter(request, response);
    }
}
