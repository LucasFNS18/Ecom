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

import static org.junit.jupiter.api.Assertions.assertTrue;
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

    
}
