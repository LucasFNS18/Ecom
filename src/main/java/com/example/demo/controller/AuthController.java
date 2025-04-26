package com.example.demo.controller;

import com.example.demo.config.JwtUtil;
import com.example.demo.dto.AuthDTO;
import com.example.demo.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AuthDTO request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthDTO request) {
        boolean authenticated = authService.authenticate(request);
        if (authenticated) {
            // ✅ Gera o token usando o nome de usuário
            String token = JwtUtil.generateToken(request.getUsername());
            return ResponseEntity.ok(token); // 🔁 Retorna o token no corpo da resposta
        } else {
            return ResponseEntity.status(401).body("Credenciais inválidas!");
        }
    }

    // Rota de teste liberada
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Rota liberada!");
    }
}
