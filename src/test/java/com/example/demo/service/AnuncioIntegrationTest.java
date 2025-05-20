package com.example.demo.service;

import com.example.demo.dto.AuthDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AnuncioIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void fluxoCompletoDaAplicacao() throws Exception {
        // 1. Cadastro e login do usuário
        String email = "e2e" + System.currentTimeMillis() + "@teste.com";
        String senha = "123456";
        AuthDTO usuario = new AuthDTO(email, senha);

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isOk());

        MvcResult login = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isOk())
                .andReturn();

        String token = login.getResponse().getContentAsString();

        // 2. Criação de anúncio
        String anuncioJson = """
            {
                "title": "Anúncio E2E",
                "description": "Descrição completa",
                "category": "Tecnologia",
                "imageUrl": "https://via.placeholder.com/123"
            }
            """;

        mockMvc.perform(post("/api/anuncios")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(anuncioJson))
                .andExpect(status().isOk());

        // 3. Listagem e captura do ID
        MvcResult getResult = mockMvc.perform(get("/api/anuncios")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn();

        Long id = objectMapper.readTree(getResult.getResponse().getContentAsString())
                .get(0).get("id").asLong();

        // 4. Edição do anúncio
        String atualizado = """
            {
                "id": %d,
                "title": "Anúncio E2E Editado",
                "description": "Descrição atualizada",
                "category": "Atualizado",
                "imageUrl": "https://via.placeholder.com/321"
            }
            """.formatted(id);

        mockMvc.perform(put("/api/anuncios/" + id)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(atualizado))
                .andExpect(status().isOk());

        // 5. Exclusão do anúncio
        mockMvc.perform(delete("/api/anuncios/" + id)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }
}
