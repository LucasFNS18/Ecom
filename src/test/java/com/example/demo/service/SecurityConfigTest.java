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

import io.qameta.allure.*;
import static io.qameta.allure.Allure.step;
import org.junit.jupiter.api.DisplayName;

@Epic("Segurança")
@Feature("Configuração de Segurança com Spring Security")
@SpringBootTest
@AutoConfigureMockMvc
public class SecurityConfigTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Story("Verifica se o PasswordEncoder foi carregado corretamente")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("PasswordEncoder deve estar disponível e funcionando")
    void testPasswordEncoderIsLoaded() {
        step("Verifica se o PasswordEncoder foi injetado pelo Spring", () -> {
            assertThat(passwordEncoder).isNotNull();
        });

        step("Verifica se o PasswordEncoder gera hash corretamente", () -> {
            assertThat(passwordEncoder.encode("123456")).isNotBlank();
        });
    }

    @Test
    @Story("Verifica acesso à rota pública liberada")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Deve permitir acesso à rota pública /auth/test")
    void testPublicEndpointsAreAccessible() throws Exception {
        step("Faz GET para /auth/test e espera status 200 OK", () -> {
            mockMvc.perform(get("/auth/test"))
                    .andExpect(status().isOk());
        });
    }

    @Test
    @Story("Verifica bloqueio de rota sem autenticação")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Deve bloquear acesso à rota protegida sem autenticação")
    void testProtectedEndpointRequiresAuthentication() throws Exception {
        step("Faz GET para /rota-protegida e espera status 403 Forbidden", () -> {
            mockMvc.perform(get("/rota-protegida"))
                    .andExpect(status().isForbidden());
        });
    }
}
