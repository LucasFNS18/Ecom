package com.example.demo.service;

import com.example.demo.controller.AnuncioController;
import com.example.demo.model.Anuncio;
import com.example.demo.repository.AnuncioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class AnuncioControllerTest {

    // Aqui estou injetando o controller que será testado, com os mocks automaticamente atribuídos
    @InjectMocks
    private AnuncioController anuncioController;

    // Mock do repositório para simular as interações com o banco de dados
    @Mock
    private AnuncioRepository anuncioRepository;

    // Esse método roda antes de cada teste para inicializar os mocks
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldCreateAnuncioSuccessfully() {
        // 🧪 Testa se o método create() do controller cria um anúncio corretamente
        Anuncio anuncio = new Anuncio();
        // Simulo o comportamento do repositório retornando o anúncio salvo
        when(anuncioRepository.save(any())).thenReturn(anuncio);

        // Executo o método do controller
        Anuncio result = anuncioController.create(anuncio);

        // Verifico se o resultado não é nulo (ou seja, foi criado com sucesso)
        assertNotNull(result);
    }

    @Test
    public void shouldUpdateAnuncioWithCorrectId() {
        // 🧪 Testa se o método update() define corretamente o ID do anúncio ao atualizar
        Anuncio anuncio = new Anuncio();
        long id = 1L;
        anuncio.setId(id);

        // Simulo o repositório salvando o anúncio atualizado
        when(anuncioRepository.save(any())).thenReturn(anuncio);

        // Executo o método de update
        Anuncio result = anuncioController.update(id, anuncio);

        // Verifico se o ID retornado está correto
        assertEquals(id, result.getId());
    }

    @Test
    public void shouldDeleteAnuncioWithoutErrors() {
        // 🧪 Testa se o método delete() é executado sem lançar exceções
        long id = 1L;

        // Como o método não retorna nada, só testamos se não dá erro
        anuncioController.delete(id);
    }

    @Test
    public void shouldReturnAllAnuncios() {
        // 🧪 Testa se o método getAll() retorna todos os anúncios corretamente
        Anuncio anuncio = new Anuncio();

        // Simulo o repositório retornando uma lista com 1 anúncio
        when(anuncioRepository.findAll()).thenReturn(List.of(anuncio));

        // Executo o método do controller
        List<Anuncio> result = anuncioController.getAll();

        // Verifico se a lista retornada tem exatamente 1 item
        assertEquals(1, result.size());
    }
}
