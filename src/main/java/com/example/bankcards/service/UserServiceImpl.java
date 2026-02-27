package com.example.bankcards.service;

import static com.example.bankcards.repository.UserEntitySpecifications.withFilter;
import static java.nio.CharBuffer.wrap;

import com.example.bankcards.dto.response.User;
import com.example.bankcards.dto.response.UserPage;
import com.example.bankcards.entity.RoleEntity;
import com.example.bankcards.entity.UserEntity;
import com.example.bankcards.exception.ResourceNotFoundException;
import com.example.bankcards.mapper.UserMapper;
import com.example.bankcards.repository.RoleRepository;
import com.example.bankcards.repository.UserRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final String ROLE_NOT_FOUND = "Role %s not found";
    private static final String ROLE_PREFIX = "ROLE_%s";

    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    @Override
    public UserPage findAll(final String username, final String role, final Integer page, final Integer size) {
        final Page<User> userPage = userRepository.findAll(withFilter(username, role), PageRequest.of(page, size))
                .map(userMapper::mapUserToDto);
        return new UserPage(
            userPage.getContent(),
            userPage.getTotalPages(),
            userPage.getNumber(),
            userPage.getSize()
        );
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<User> findById(final Long id) {
        return userRepository.findById(id).map(userMapper::mapUserToDto);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsByUsername(final String username) {
        return userRepository.existsByUsername(username);
    }

    @Transactional
    @Override
    public User create(final String username, @NotNull final char[] password, final String role) {
        final RoleEntity roleEntity = roleRepository.findByName(ROLE_PREFIX.formatted(role))
            .orElseThrow(() -> new ResourceNotFoundException(ROLE_NOT_FOUND.formatted(role)));
        final UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setPassword(passwordEncoder.encode(wrap(password)));
        userEntity.setRole(roleEntity);
        return userMapper.mapUserToDto(userRepository.save(userEntity));
    }
}
