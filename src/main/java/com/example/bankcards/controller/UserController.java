package com.example.bankcards.controller;

import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

import com.example.bankcards.dto.request.CreateUser;
import com.example.bankcards.dto.request.UserFilter;
import com.example.bankcards.dto.response.User;
import com.example.bankcards.dto.response.UserPage;
import com.example.bankcards.exception.ResourceNotFoundException;
import com.example.bankcards.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private static final String ID_PATH_PARAM = "/{id}";
    private static final String USER_NOT_FOUND = "User not found";

    private final UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<UserPage> getUsers(@Validated final UserFilter filter) {
        return ok(userService.findAll(filter.username(), filter.role(), filter.page(), filter.pageSize()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable final Long id) {
        final User user = userService.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));
        return ok(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<User> create(@RequestBody final CreateUser request) {
        final User user = userService.create(request.username(), request.password(), request.role());
        final URI location = fromCurrentRequest()
            .path(ID_PATH_PARAM)
            .buildAndExpand(user.id())
            .toUri();
        return created(location).body(user);
    }
}
