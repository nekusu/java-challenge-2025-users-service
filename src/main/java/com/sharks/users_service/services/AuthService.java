package com.sharks.users_service.services;

import com.sharks.users_service.models.dtos.LoginUser;
import com.sharks.users_service.models.dtos.NewUser;
import com.sharks.users_service.models.dtos.UserDTO;

public interface AuthService {

    UserDTO register(NewUser newUser);

    String login(LoginUser loginUser);
}
