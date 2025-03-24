package com.example.demo.service;


import com.example.demo.dto.AuthDTO;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String register(AuthDTO request) {
        Optional<User> existingUser = userRepository.findByUsername(request.getUsername());
        if (existingUser.isPresent()) {
            return "Usuário já existe!";
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);
        return "Usuário registrado com sucesso!";
    }

    public boolean authenticate(AuthDTO request) {
        Optional<User> user = userRepository.findByUsername(request.getUsername());
        return user.isPresent() && passwordEncoder.matches(request.getPassword(), user.get().getPassword());
    }
}