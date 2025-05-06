package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthDTO {
    private String username;
    private String password;

    public AuthDTO() {
        // construtor vazio para o Spring e para testes
    }

    public AuthDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
