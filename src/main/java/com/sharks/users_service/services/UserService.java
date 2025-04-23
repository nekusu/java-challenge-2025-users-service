package com.sharks.users_service.services;

import java.util.List;

import com.sharks.users_service.enums.RoleType;
import com.sharks.users_service.models.AppUser;
import com.sharks.users_service.models.dtos.NewUser;
import com.sharks.users_service.models.dtos.UserDTO;

public interface UserService {

    AppUser getUserById(Long id);

    UserDTO getUserDTOById(Long id);

    AppUser getUserByEmail(String email);

    UserDTO getUserDTOByEmail(String email);

    List<AppUser> getAllUsers();

    List<UserDTO> getAllUserDTOs();

    UserDTO createUser(NewUser newUser);

    List<RoleType> getRoles();
}
