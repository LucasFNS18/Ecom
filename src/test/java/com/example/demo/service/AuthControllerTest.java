package com.example.demo.service;
import com.example.demo.controller.AuthController;

import com.example.demo.dto.AuthDTO;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AuthService;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class AuthControllerTest {
    @InjectMocks
    AuthController authControllerTest;
    @Mock
    AuthService authService;
    @Mock
    UserRepository userRepository;
    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void testeRegisterAuthController(){
        AuthDTO objetoRegister = new AuthDTO();
        authControllerTest.register(objetoRegister);
    }
    @Test
    public void testeLoginAuthController(){
        AuthDTO objetoLogin = new AuthDTO();
        authControllerTest.login(objetoLogin);
    }
    @Test
    public void testeIFLoginAuthController(){
        AuthDTO objetoLogin = new AuthDTO();
        objetoLogin.setPassword("123456");
        User brian = new User();
        brian.setPassword("123456");
        when(authService.authenticate(any())).thenReturn(true);
        authControllerTest.login(objetoLogin);
    }

    @Test
    public void rotadetesteAuthController(){
        authControllerTest.test();
    }
}