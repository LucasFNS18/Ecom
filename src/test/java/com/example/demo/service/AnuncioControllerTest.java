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

    @InjectMocks
    private AnuncioController anuncioController;

    @Mock
    private AnuncioRepository anuncioRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldCreateAnuncioSuccessfully() {
        Anuncio anuncio = new Anuncio();
        when(anuncioRepository.save(any())).thenReturn(anuncio);

        Anuncio result = anuncioController.create(anuncio);
        assertNotNull(result);
    }

    @Test
    public void shouldUpdateAnuncioWithCorrectId() {
        Anuncio anuncio = new Anuncio();
        long id = 1L;
        anuncio.setId(id);

        when(anuncioRepository.save(any())).thenReturn(anuncio);

        Anuncio result = anuncioController.update(id, anuncio);
        assertEquals(id, result.getId());
    }

    @Test
    public void shouldDeleteAnuncioWithoutErrors() {
        long id = 1L;
        anuncioController.delete(id);
        // Como delete não retorna nada, estamos apenas testando que não lança exceção
    }

    @Test
    public void shouldReturnAllAnuncios() {
        Anuncio anuncio = new Anuncio();
        when(anuncioRepository.findAll()).thenReturn(List.of(anuncio));

        List<Anuncio> result = anuncioController.getAll();
        assertEquals(1, result.size());
    }
}
