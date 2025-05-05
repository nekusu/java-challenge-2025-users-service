package com.sharks.users_service.services.impl;

import org.springframework.amqp.core.AmqpTemplate;
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

    private final AuthenticationManager authenticationManager;

    private final AmqpTemplate amqpTemplate;

    private final JwtUtils jwtUtils;

    private final UserService userService;

    public AuthServiceImpl(AuthenticationManager authenticationManager, AmqpTemplate amqpTemplate, JwtUtils jwtUtils,
            UserService userService) {
        this.authenticationManager = authenticationManager;
        this.amqpTemplate = amqpTemplate;
        this.jwtUtils = jwtUtils;
        this.userService = userService;
    }

    @Override
    public UserDTO register(NewUser newUser) {
        UserDTO userDTO = userService.createUser(newUser);
        amqpTemplate.convertAndSend("email-exchange", "user.registration", userDTO);
        return userDTO;
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
