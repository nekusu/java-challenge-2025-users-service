package com.sharks.users_service.services;

import com.sharks.users_service.config.JwtUtils;
import com.sharks.users_service.models.AppUser;
import com.sharks.users_service.models.dtos.LoginUser;
import com.sharks.users_service.models.dtos.NewUser;
import com.sharks.users_service.models.dtos.UserDTO;
import com.sharks.users_service.services.impl.AuthServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Collections;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private AmqpTemplate amqpTemplate;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void testRegister_Success() {
        NewUser newUser = new NewUser("testuser", "test@example.com", "password");
        AppUser user = new AppUser("testuser", "test@example.com", "password");
        ReflectionTestUtils.setField(user, "id", 1L);
        UserDTO userDTO = new UserDTO(user);

        when(userService.createUser(newUser)).thenReturn(userDTO);

        UserDTO result = authService.register(newUser);
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals("test@example.com", result.getEmail());
        verify(amqpTemplate).convertAndSend("email-exchange", "user.registration", userDTO);
    }

    @Test
    void testLogin_Success() {
        LoginUser loginUser = new LoginUser("test@example.com", "password");
        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@example.com");
        when(authentication.getAuthorities())
                .thenAnswer(invocation -> Collections.singletonList(new SimpleGrantedAuthority("USER")));
        when(jwtUtils.generateToken("test@example.com", "USER")).thenReturn("jwt-token");

        String token = authService.login(loginUser);

        assertNotNull(token);
        assertEquals("jwt-token", token);
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtils).generateToken("test@example.com", "USER");
    }

    @Test
    void testLogin_InvalidCredentials_ThrowsException() {
        LoginUser loginUser = new LoginUser("wrong@example.com", "wrongpassword");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new AuthenticationException("Bad credentials") {
                });

        assertThrows(AuthenticationException.class, () -> authService.login(loginUser));
        verify(jwtUtils, never()).generateToken(anyString(), anyString());
    }
}
