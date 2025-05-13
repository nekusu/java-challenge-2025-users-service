package com.sharks.users_service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.sharks.users_service.enums.RoleType;
import com.sharks.users_service.models.AppUser;
import com.sharks.users_service.repositories.UserRepository;

@SpringBootApplication
public class UsersServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UsersServiceApplication.class, args);
	}

	@Bean
	public CommandLineRunner createAdminUser(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			String adminUsername = "admin";
			if (userRepository.existsByUsername(adminUsername))
				return;
			AppUser adminUser = new AppUser(adminUsername, "admin@email.com", "admin");
			adminUser.setPassword(passwordEncoder.encode(adminUser.getPassword()));
			adminUser.setRole(RoleType.ADMIN);
			userRepository.save(adminUser);
		};
	}
}
