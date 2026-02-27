package com.example.bankcards.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateUser(

    @NotNull
    @Size(min = 1, max = 50)
    String username,

    @NotNull
    char[] password,

    @NotNull
    @Size(min = 1, max = 15)
    String role
) { }
