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

public class FornecedorIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveCadastrarAnuncioComTokenValido() throws Exception {
        // 1. Cria usuário e login
        String email = "user" + System.currentTimeMillis() + "@teste.com";
        String senha = "123456";
        AuthDTO usuario = new AuthDTO(email, senha);
    
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isOk());
    
        MvcResult loginResult = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isOk())
                .andReturn();
    
        String token = loginResult.getResponse().getContentAsString();
    
        // 2. JSON do Anúncio
        String anuncioJson = """
            {
                "title": "Anúncio Teste",
                "description": "Descrição de teste",
                "category": "Categoria teste",
                "imageUrl": "https://via.placeholder.com/300"
            }
            """;
    
        // 3. Envia POST para /api/anuncios
        mockMvc.perform(post("/api/anuncios")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(anuncioJson))
                .andExpect(status().isOk()); // ou isCreated()
    }

    @Test
    void deveAtualizarAnuncioComSucesso() throws Exception {
    // 1. Cria e autentica usuário
    String email = "editar" + System.currentTimeMillis() + "@teste.com";
    String senha = "123456";
    AuthDTO usuario = new AuthDTO(email, senha);

    mockMvc.perform(post("/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(usuario)))
            .andExpect(status().isOk());

    MvcResult login = mockMvc.perform(post("/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(usuario)))
            .andReturn();

    String token = login.getResponse().getContentAsString();

    // 2. Cria anúncio
    String anuncioJson = """
        {
            "title": "Original",
            "description": "Descrição original",
            "category": "Teste",
            "imageUrl": "https://via.placeholder.com/100"
        }
        """;

    MvcResult postResult = mockMvc.perform(post("/api/anuncios")
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(anuncioJson))
            .andReturn();

    // 3. Extrai ID do anúncio criado (assumindo que seu controller retorna JSON com o ID)
    // Aqui como fallback, vamos buscar com GET e pegar o primeiro
    MvcResult getResult = mockMvc.perform(get("/api/anuncios")
            .header("Authorization", "Bearer " + token))
            .andReturn();

    String body = getResult.getResponse().getContentAsString();
    Long id = objectMapper.readTree(body).get(0).get("id").asLong();

    // 4. Atualiza
    String atualizado = """
        {
            "id": %d,
            "title": "Atualizado",
            "description": "Nova descrição",
            "category": "Nova categoria",
            "imageUrl": "https://via.placeholder.com/200"
        }
        """.formatted(id);

    mockMvc.perform(put("/api/anuncios/" + id)
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(atualizado))
            .andExpect(status().isOk());
}
    //  Excluir um anúncio (DELETE /api/anuncios/{id})
    @Test
    void deveExcluirAnuncioComSucesso() throws Exception {
    // Cria usuário, login, token e anúncio como no teste anterior

    String email = "delete" + System.currentTimeMillis() + "@teste.com";
    String senha = "123456";
    AuthDTO usuario = new AuthDTO(email, senha);

    mockMvc.perform(post("/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(usuario)))
            .andExpect(status().isOk());

    MvcResult login = mockMvc.perform(post("/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(usuario)))
            .andReturn();

    String token = login.getResponse().getContentAsString();

    String anuncioJson = """
        {
            "title": "Excluir",
            "description": "Deletar",
            "category": "Remover",
            "imageUrl": "https://via.placeholder.com/100"
        }
        """;

    mockMvc.perform(post("/api/anuncios")
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(anuncioJson))
            .andExpect(status().isOk());

    MvcResult getResult = mockMvc.perform(get("/api/anuncios")
            .header("Authorization", "Bearer " + token))
            .andReturn();

    Long id = objectMapper.readTree(getResult.getResponse().getContentAsString())
            .get(0).get("id").asLong();

    // DELETE
    mockMvc.perform(delete("/api/anuncios/" + id)
            .header("Authorization", "Bearer " + token))
            .andExpect(status().isOk());
}


    
}



    