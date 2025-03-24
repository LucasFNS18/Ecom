package com.example.demo.config;

<<<<<<< HEAD
=======

import com.example.demo.config.JwtUtil;
>>>>>>> 7f688f942a33054b92e1b94de93739bb4fd3cc6a
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
<<<<<<< HEAD
=======

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
>>>>>>> 7f688f942a33054b92e1b94de93739bb4fd3cc6a
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

<<<<<<< HEAD
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
=======
        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            if (JwtUtil.validateToken(token)) {
                String username = JwtUtil.extractUsername(token);
                UsernamePasswordAuthenticationFilter authentication =
                        new UsernamePasswordAuthenticationFilter();
                SecurityContextHolder.getContext().setAuthentication((Authentication) authentication);
            }
        }

        chain.doFilter(request, response);
    }
}
>>>>>>> 7f688f942a33054b92e1b94de93739bb4fd3cc6a
