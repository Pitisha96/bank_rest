package com.example.bankcards.dto.response;

import java.util.List;

public record UserPage(
    List<User> data,
    Integer totalPages,
    Integer currentPage,
    Integer pageSize
) { }
