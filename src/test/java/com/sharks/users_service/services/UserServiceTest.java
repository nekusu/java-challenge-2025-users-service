package com.sharks.users_service.services;

import com.sharks.users_service.enums.RoleType;
import com.sharks.users_service.exceptions.EmailAlreadyInUseException;
import com.sharks.users_service.exceptions.UserNotFoundException;
import com.sharks.users_service.exceptions.UsernameAlreadyInUseException;
import com.sharks.users_service.models.AppUser;
import com.sharks.users_service.models.dtos.NewUser;
import com.sharks.users_service.models.dtos.UserDTO;
import com.sharks.users_service.repositories.UserRepository;
import com.sharks.users_service.services.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private AppUser user;
    private NewUser newUser;

    @BeforeEach
    void setUp() {
        user = new AppUser("testuser", "test@example.com", "password");
        ReflectionTestUtils.setField(user, "id", 1L);
        newUser = new NewUser("testuser", "test@example.com", "password");
    }

    @Test
    void testGetUserById_Found() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        AppUser result = userService.getUserById(1L);
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
    }

    @Test
    void testGetUserById_NotFound_ThrowsException() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(2L));
    }

    @Test
    void testGetUserByEmail_Found() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        AppUser result = userService.getUserByEmail("test@example.com");
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
    }

    @Test
    void testGetUserByEmail_NotFound_ThrowsException() {
        when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserByEmail("notfound@example.com"));
    }

    @Test
    void testGetAllUsers_ReturnsList() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<AppUser> result = userService.getAllUsers();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("testuser", result.get(0).getUsername());
    }

    @Test
    void testGetAllUsers_ReturnsEmptyList() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        List<AppUser> result = userService.getAllUsers();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testCreateUser_Success() {
        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(any(AppUser.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserDTO result = userService.createUser(newUser);
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    void testCreateUser_UsernameAlreadyInUse_ThrowsException() {
        when(userRepository.existsByUsername("testuser")).thenReturn(true);

        assertThrows(UsernameAlreadyInUseException.class, () -> userService.createUser(newUser));
    }

    @Test
    void testCreateUser_EmailAlreadyInUse_ThrowsException() {
        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        assertThrows(EmailAlreadyInUseException.class, () -> userService.createUser(newUser));
    }

    @Test
    void testGetRoles_ReturnsAllRoles() {
        List<RoleType> roles = userService.getRoles();
        assertNotNull(roles);
        assertTrue(roles.contains(RoleType.USER));
    }
}
