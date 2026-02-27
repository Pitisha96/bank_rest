package com.example.bankcards.dto.response;

public record User(
    Long id,
    String username,
    Role role
) { }
