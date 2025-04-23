package com.sharks.users_service.models.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginUser(
        @NotBlank(message = "Email is required") @Email String email,
        @NotBlank(message = "Password is required") String password) {
}
