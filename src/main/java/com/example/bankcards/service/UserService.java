package com.example.bankcards.service;

import com.example.bankcards.dto.response.User;
import com.example.bankcards.dto.response.UserPage;

import java.util.Optional;

public interface UserService {

    UserPage findAll(String username, String role, Integer page, Integer size);
    Optional<User> findById(Long id);
    boolean existsByUsername(String username);
    User create(String username, char[] password, String role);
}
