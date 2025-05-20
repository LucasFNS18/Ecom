package com.example.demo.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class JwtFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7); // remove "Bearer "

            if (JwtUtil.validateToken(token)) {
                String username = JwtUtil.extractUsername(token);

                // Cria uma autenticação fictícia com permissão de USER (sem roles reais por enquanto)
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                username,
                                null,
                                List.of(new SimpleGrantedAuthority("USER"))
                        );

                // Seta o usuário como autenticado no contexto do Spring Security
                SecurityContextHolder.getContext().setAuthentication(authentication);

                System.out.println("✅ Token válido para: " + username);
            } else {
                System.out.println("❌ Token inválido recebido.");
            }
        }

        chain.doFilter(request, response);
    }
}
