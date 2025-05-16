package com.example.demo.service;

import com.example.demo.dto.AuthDTO;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @Test
    void testAuthenticateSuccess() {
        // Arrange
        AuthDTO dto = new AuthDTO("email@example.com", "12345");
        User user = new User();
        user.setUsername("email@example.com");
        user.setPassword("hashed");

        when(userRepository.findByUsername("email@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("12345", "hashed")).thenReturn(true);

        // Act
        boolean result = authService.authenticate(dto);

        // Assert
        assertTrue(result);
    }

     @Test
    void testRegisterUserAlreadyExists() {
        // Arrange
        AuthDTO request = new AuthDTO("email@example.com", "12345");
        User existingUser = new User();
        existingUser.setUsername("email@example.com");

        when(userRepository.findByUsername("email@example.com")).thenReturn(Optional.of(existingUser));

        // Act
        String result = authService.register(request);

        // Assert
        assertEquals("Usuário já existe!", result);
        verify(userRepository, never()).save(any(User.class));  // Verifica se o save nunca foi chamado
    }

    @Test
    void testRegisterUserSuccess() {
        // Arrange
        AuthDTO request = new AuthDTO("email@example.com", "12345");
        when(userRepository.findByUsername("email@example.com")).thenReturn(Optional.empty());  // Usuário não existe
        when(passwordEncoder.encode("12345")).thenReturn("hashedPassword");

        // Act
        String result = authService.register(request);

        // Assert
        assertEquals("Usuário registrado com sucesso!", result);
        verify(userRepository, times(1)).save(any(User.class));  // Verifica se o save foi chamado uma vez
    }
}
