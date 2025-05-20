package com.example.demo.service.Integration;

import com.example.demo.dto.AuthDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    void deveRegistrarNovoUsuarioComSucesso() throws Exception {
        // Cria o DTO com dados do novo usuário
        AuthDTO novoUsuario = new AuthDTO("registro@example.com", "senha123");

        // Faz a requisição POST para o endpoint de registro
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoUsuario)))
                .andExpect(status().isOk()); // Espera que o cadastro seja bem-sucedido (200 OK)
    }

    @Test
    void deveFazerLoginComUsuarioValido() throws Exception {
        String email = "login" + System.currentTimeMillis() + "@teste.com";
        String senha = "123456";
        AuthDTO dto = new AuthDTO(email, senha);
    
        // Registra o usuário
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    
        // Faz login e verifica se o retorno é um token JWT válido (string que começa com "ey")
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string(startsWith("ey"))); // JWT geralmente começa com "ey..."
    }

    @Test
    void deveExtrairTokenDaRespostaDeLogin() throws Exception {
        // 1. Cria credenciais únicas
        String email = "tokenuser" + System.currentTimeMillis() + "@teste.com";
        String senha = "senhaSegura";
        AuthDTO dto = new AuthDTO(email, senha);

        // 2. Registra o usuário
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        // 3. Faz login e captura a resposta
        MvcResult loginResult = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andReturn();

        // 4. Extrai o token da resposta (como string pura)
        String token = loginResult.getResponse().getContentAsString();

        // 5. Valida que o token começa com "ey" (JWT válido)
        assertTrue(token.startsWith("ey"), "Token JWT inválido: " + token);
    }

    @Test
    void deveAcessarRotaProtegidaComTokenValido() throws Exception {
        // 1. Registra um novo usuário
        String email = "authuser" + System.currentTimeMillis() + "@teste.com";
        String senha = "123456";
        AuthDTO dto = new AuthDTO(email, senha);

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        // 2. Faz login e extrai o token
        MvcResult loginResult = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andReturn();

        String token = loginResult.getResponse().getContentAsString();

        // 3. Acessa a rota protegida usando o token
        mockMvc.perform(get("/api/anuncios")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk()); // ✅ acesso autorizado
    }

    @Test
    void naoDeveFazerLoginComSenhaErrada() throws Exception {
        String email = "invalido" + System.currentTimeMillis() + "@teste.com";
        String senha = "senhaCerta";
        AuthDTO dto = new AuthDTO(email, senha);

        // Cria usuário válido
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        // Tenta logar com senha errada
        AuthDTO loginErrado = new AuthDTO(email, "senhaErrada");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginErrado)))
                .andExpect(status().isUnauthorized());
    }
    // Usuario existente não fazer Login 
    @Test
    void naoDeveFazerLoginComUsuarioInexistente() throws Exception {
    AuthDTO dto = new AuthDTO("naoexiste@example.com", "123456");

    mockMvc.perform(post("/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isUnauthorized());
    }

 

    

}