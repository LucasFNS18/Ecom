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

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityConfigTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testPasswordEncoderIsLoaded() {
        assertThat(passwordEncoder).isNotNull();
        assertThat(passwordEncoder.encode("123456")).isNotBlank();
    }

    @Test
    void testPublicEndpointsAreAccessible() throws Exception {
        // Usa uma rota que EXISTE e está liberada em SecurityConfig
        mockMvc.perform(get("/auth/test"))
                .andExpect(status().isOk());
    }

    @Test
    void testProtectedEndpointRequiresAuthentication() throws Exception {
        // Usa uma rota que exige autenticação
        mockMvc.perform(get("/rota-protegida"))
                .andExpect(status().isForbidden()); // Foi 403 na execução real
    }
}
