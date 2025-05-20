package com.example.demo.service;

import com.example.demo.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest // 🧪 Carrega o contexto completo da aplicação Spring
@AutoConfigureMockMvc // 🧪 Habilita o uso do MockMvc para simular requisições HTTP
public class SecurityConfigTest {

    // Injetando o PasswordEncoder para testar se ele foi configurado corretamente
    @Autowired
    private PasswordEncoder passwordEncoder;

    // Injetando o MockMvc para simular requisições às rotas HTTP da aplicação
    @Autowired
    private MockMvc mockMvc;

    @Test
    void testPasswordEncoderIsLoaded() {
        // 🧪 Verifica se o PasswordEncoder foi corretamente registrado no contexto do Spring
        assertThat(passwordEncoder).isNotNull(); // O bean deve existir
        assertThat(passwordEncoder.encode("123456")).isNotBlank(); // Deve gerar um hash não vazio
    }

    @Test
    void testPublicEndpointsAreAccessible() throws Exception {
        // 🧪 Testa se a rota pública está acessível sem autenticação
        // Essa rota deve estar liberada no SecurityConfig, como "/auth/test"
        mockMvc.perform(get("/auth/test"))
                .andExpect(status().isOk()); // Esperamos status 200 OK
    }

    @Test
    void testProtectedEndpointRequiresAuthentication() throws Exception {
        // 🧪 Testa se a rota protegida está realmente exigindo autenticação
        // "/rota-protegida" deve estar bloqueada para usuários não autenticados
        mockMvc.perform(get("/rota-protegida"))
                .andExpect(status().isForbidden()); // Esperamos 403 Forbidden se não houver token
    }
}
