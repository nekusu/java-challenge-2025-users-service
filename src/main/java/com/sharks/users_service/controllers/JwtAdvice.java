package com.sharks.users_service.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import com.sharks.users_service.config.JwtUtils;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice(assignableTypes = UserController.class)
public class JwtAdvice {

    private JwtUtils jwtUtils;

    public JwtAdvice(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @ModelAttribute("email")
    public String extractEmail(HttpServletRequest request) {
        try {
            String token = jwtUtils.extractTokenFromRequest(request);
            if (token == null)
                throw new JwtException("Authorization header is missing or invalid");
            return jwtUtils.extractUsername(token);
        } catch (JwtException e) {
            log.error("Error extracting email from token: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @ModelAttribute("role")
    public String extractRole(HttpServletRequest request) {
        try {
            String token = jwtUtils.extractTokenFromRequest(request);
            if (token == null)
                throw new JwtException("Authorization header is missing or invalid");
            return jwtUtils.extractRole(token);
        } catch (JwtException e) {
            log.error("Error extracting role from token: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}
