package com.example.demo.service;

import com.example.demo.controller.AuthController;
import com.example.demo.dto.AuthDTO;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class AuthControllerTest {

    // Aqui injeto o controller que quero testar, com os mocks injetados automaticamente
    @InjectMocks
    AuthController authControllerTest;

    // Simulo o AuthService, que é chamado dentro do controller
    @Mock
    AuthService authService;

    // Também simulo o repositório de usuários, embora ele não seja diretamente usado aqui
    @Mock
    UserRepository userRepository;

    // Antes de cada teste, inicializo os mocks
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testeRegisterAuthController() {
        // 🧪 Testa o método de registro do controller.
        // Aqui não estou fazendo asserções, só verificando se o método pode ser chamado sem erros.
        AuthDTO objetoRegister = new AuthDTO();
        authControllerTest.register(objetoRegister);
    }

    @Test
    public void testeLoginAuthController() {
        // 🧪 Testa o login com um objeto DTO vazio.
        // Aqui o objetivo é apenas garantir que o método é executado sem exceções.
        AuthDTO objetoLogin = new AuthDTO();
        authControllerTest.login(objetoLogin);
    }

    @Test
    public void testeIFLoginAuthController() {
        // 🧪 Testa o login com senha preenchida e simula autenticação válida
        // Aqui simulo que a senha confere e o AuthService retorna true
        AuthDTO objetoLogin = new AuthDTO();
        objetoLogin.setPassword("123456");

        User brian = new User();
        brian.setPassword("123456");

        // Simulo que a autenticação será bem-sucedida
        when(authService.authenticate(any())).thenReturn(true);

        authControllerTest.login(objetoLogin);
    }

    @Test
    public void rotadetesteAuthController() {
        // 🧪 Testa uma rota de teste simples criada no controller (por exemplo, um endpoint GET /test)
        authControllerTest.test();
    }
}
