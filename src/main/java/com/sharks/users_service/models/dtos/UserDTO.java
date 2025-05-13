package com.sharks.users_service.models.dtos;

import com.sharks.users_service.models.AppUser;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserDTO {

    private Long id;

    private String username;

    private String email;

    private String role;

    public UserDTO(AppUser user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.role = user.getRole().toString();
    }
}
