package com.sharks.users_service.services.impl;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sharks.users_service.enums.RoleType;
import com.sharks.users_service.exceptions.EmailAlreadyInUseException;
import com.sharks.users_service.exceptions.UserNotFoundException;
import com.sharks.users_service.exceptions.UsernameAlreadyInUseException;
import com.sharks.users_service.models.AppUser;
import com.sharks.users_service.models.dtos.NewUser;
import com.sharks.users_service.models.dtos.UserDTO;
import com.sharks.users_service.repositories.UserRepository;
import com.sharks.users_service.services.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    public UserServiceImpl(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public AppUser getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public UserDTO getUserDTOById(Long id) {
        return new UserDTO(getUserById(id));
    }

    @Override
    public AppUser getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public UserDTO getUserDTOByEmail(String email) {
        return new UserDTO(getUserByEmail(email));
    }

    @Override
    public List<AppUser> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<UserDTO> getAllUserDTOs() {
        return getAllUsers().stream().map(UserDTO::new).toList();
    }

    @Override
    public UserDTO createUser(NewUser newUser) {
        validateUser(newUser);
        String encodedPassword = passwordEncoder.encode(newUser.password());
        AppUser user = new AppUser(newUser.username(), newUser.email(), encodedPassword);
        return new UserDTO(userRepository.save(user));
    }

    @Override
    public List<RoleType> getRoles() {
        return List.of(RoleType.values());
    }

    private void validateUser(NewUser newUser) {
        if (userRepository.existsByUsername(newUser.username()))
            throw new UsernameAlreadyInUseException();
        if (userRepository.existsByEmail(newUser.email()))
            throw new EmailAlreadyInUseException();
    }
}
