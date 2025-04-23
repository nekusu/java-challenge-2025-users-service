package com.sharks.users_service.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.sharks.users_service.enums.RoleType;
import com.sharks.users_service.models.dtos.UserDTO;
import com.sharks.users_service.services.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDTO> getUserDTOs() {
        return userService.getAllUserDTOs();
    }

    @GetMapping(params = "email")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO getUserDTOByEmail(@RequestParam String email) {
        return userService.getUserDTOByEmail(email);
    }

    @GetMapping("/self")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO getCurrentUser(@ModelAttribute("email") String email) {
        return userService.getUserDTOByEmail(email);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO getUserDTOById(@PathVariable Long id) {
        return userService.getUserDTOById(id);
    }

    @GetMapping("/roles")
    @ResponseStatus(HttpStatus.OK)
    public List<RoleType> getRoles() {
        return userService.getRoles();
    }
}
