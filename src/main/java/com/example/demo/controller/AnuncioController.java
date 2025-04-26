package com.example.demo.controller;

import com.example.demo.model.Anuncio;
import com.example.demo.repository.AnuncioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/anuncios")
public class AnuncioController {

    @Autowired
    private AnuncioRepository anuncioRepository;

    @GetMapping
    public List<Anuncio> getAll() {
        return anuncioRepository.findAll();
    }

    @PostMapping
    public Anuncio create(@RequestBody Anuncio anuncio) {
        return anuncioRepository.save(anuncio);
    }

    @PutMapping("/{id}")
    public Anuncio update(@PathVariable Long id, @RequestBody Anuncio anuncio) {
        anuncio.setId(id);
        return anuncioRepository.save(anuncio);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        anuncioRepository.deleteById(id);
    }
}
