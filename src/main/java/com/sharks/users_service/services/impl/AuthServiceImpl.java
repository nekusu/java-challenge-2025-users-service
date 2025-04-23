package com.sharks.users_service.services.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.sharks.users_service.config.JwtUtils;
import com.sharks.users_service.models.dtos.LoginUser;
import com.sharks.users_service.models.dtos.NewUser;
import com.sharks.users_service.models.dtos.UserDTO;
import com.sharks.users_service.services.AuthService;
import com.sharks.users_service.services.UserService;

@Service
public class AuthServiceImpl implements AuthService {

    private AuthenticationManager authenticationManager;

    private JwtUtils jwtUtils;

    private UserService userService;

    public AuthServiceImpl(AuthenticationManager authenticationManager, JwtUtils jwtUtils, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userService = userService;
    }

    @Override
    public UserDTO register(NewUser newUser) {
        return userService.createUser(newUser);
    }

    @Override
    public String login(LoginUser loginUser) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginUser.email(), loginUser.password()));
        return jwtUtils.generateToken(
                authentication.getName(),
                authentication.getAuthorities().iterator().next().getAuthority());
    }
}
