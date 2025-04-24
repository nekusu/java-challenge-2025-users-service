package com.sharks.users_service.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.sharks.users_service.enums.RoleType;
import com.sharks.users_service.models.dtos.UserDTO;
import com.sharks.users_service.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Users", description = "Operations related to user management")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Get all users", description = "Returns a list of all users. Requires ADMIN role.", responses = {
            @ApiResponse(responseCode = "200", description = "List of users retrieved successfully", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UserDTO.class)))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDTO> getUserDTOs() {
        return userService.getAllUserDTOs();
    }

    @Operation(summary = "Get current user", description = "Returns the currently authenticated user.", responses = {
            @ApiResponse(responseCode = "200", description = "Current user retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/self")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO getCurrentUser(@Parameter(hidden = true) @ModelAttribute("email") String email) {
        return userService.getUserDTOByEmail(email);
    }

    @Operation(summary = "Get user by ID", description = "Returns a user by their ID. Requires ADMIN role.", parameters = {
            @Parameter(name = "id", description = "ID of the user", required = true, example = "1")
    }, responses = {
            @ApiResponse(responseCode = "200", description = "User retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO getUserDTOById(@PathVariable Long id) {
        return userService.getUserDTOById(id);
    }

    @Operation(summary = "Get available roles", description = "Returns the list of available user roles. Requires ADMIN role.", responses = {
            @ApiResponse(responseCode = "200", description = "List of roles retrieved successfully", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = RoleType.class)))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @GetMapping("/roles")
    @ResponseStatus(HttpStatus.OK)
    public List<RoleType> getRoles() {
        return userService.getRoles();
    }
}
