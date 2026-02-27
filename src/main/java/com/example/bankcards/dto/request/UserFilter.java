package com.example.bankcards.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record UserFilter(

    String username,

    String role,

    @NotNull
    @PositiveOrZero
    Integer page,

    @NotNull
    @Positive
    @Max(50)
    Integer pageSize
) { }
