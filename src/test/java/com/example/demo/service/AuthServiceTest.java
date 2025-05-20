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
    void testRegisterWhenUserAlreadyExists() {
        // 🧪 Testa o cenário onde o usuário já está cadastrado.
        // Espera-se que o método retorne a mensagem "Usuário já existe!" sem tentar salvar um novo usuário.
        AuthDTO dto = new AuthDTO("lucas@example.com", "123456");
        when(userRepository.findByUsername(dto.getUsername()))
            .thenReturn(Optional.of(new User()));

        String result = authService.register(dto);

        assertEquals("Usuário já existe!", result);
    }

    @Test
    void testAuthenticateSuccess() {
        // 🧪 Testa se a autenticação retorna true quando o usuário existe e a senha confere.
        // Simula um usuário existente com senha codificada e usa o PasswordEncoder para verificar a correspondência.
        AuthDTO dto = new AuthDTO("email@example.com", "12345");
        User user = new User();
        user.setUsername("email@example.com");
        user.setPassword("hashed");

        when(userRepository.findByUsername("email@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("12345", "hashed")).thenReturn(true);

        boolean result = authService.authenticate(dto);

        assertTrue(result);
    }

    @Test
    void testRegisterWhenUserDoesNotExist() {
        // 🧪 Testa o fluxo de registro de um novo usuário.
        // Simula um usuário que ainda não existe no repositório e verifica se o retorno é a mensagem de sucesso.
        AuthDTO dto = new AuthDTO("lucas@example.com", "123456");

        when(userRepository.findByUsername(dto.getUsername()))
            .thenReturn(Optional.empty());

        when(passwordEncoder.encode("123456")).thenReturn("hashedPass");
        when(userRepository.save(any(User.class))).thenReturn(new User());

        String result = authService.register(dto);

        assertEquals("Usuário registrado com sucesso!", result);
    }

    @Test
    void testAuthenticateWithInvalidPassword() {
    // 🧪 Simula autenticação com senha incorreta
    AuthDTO dto = new AuthDTO("email@example.com", "senhaErrada");
    User user = new User();
    user.setUsername("email@example.com");
    user.setPassword("senhaCorretaHash");

    when(userRepository.findByUsername("email@example.com"))
        .thenReturn(Optional.of(user));
    when(passwordEncoder.matches("senhaErrada", "senhaCorretaHash"))
        .thenReturn(false);

    boolean result = authService.authenticate(dto);

    // Espera-se que a autenticação falhe
    assertEquals(false, result);
}
@Test
void testAuthenticateUserNotFound() {
    // 🧪 Simula tentativa de login com usuário não cadastrado
    AuthDTO dto = new AuthDTO("inexistente@example.com", "12345");

    when(userRepository.findByUsername(dto.getUsername()))
        .thenReturn(Optional.empty());

    boolean result = authService.authenticate(dto);

    // Espera-se que a autenticação falhe pois o usuário não existe
    assertEquals(false, result);
}


    

}
