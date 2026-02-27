package com.example.bankcards.repository;

import static java.util.Objects.nonNull;

import com.example.bankcards.entity.RoleEntity;
import com.example.bankcards.entity.UserEntity;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class UserEntitySpecifications {

    private static final String USERNAME = "username";
    private static final String ROLE = "role";
    private static final String NAME = "name";
    private static final String LIKE_FORMAT = "%%%s%%";
    private static final String ROLE_FORMAT = "ROLE_%s";

    public static Specification<UserEntity> withFilter(final String username, final String role) {
        Specification<UserEntity> combined = (root, query, criteriaBuilder) -> null;
        if (nonNull(username)) {
            combined = combined.and(usernameContains(username));
        }
        if (nonNull(role)) {
            combined = combined.and(roleEquals(role));
        }
        return combined;
    }

    public static Specification<UserEntity> usernameContains(final String username) {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.like(root.get(USERNAME), LIKE_FORMAT.formatted(username));
    }

    public static Specification<UserEntity> roleEquals(final String role) {
        return (root, query, criteriaBuilder) -> {
            final Join<UserEntity, RoleEntity> roleJoin = root.join(ROLE);
            return criteriaBuilder.equal(roleJoin.get(NAME), ROLE_FORMAT.formatted(role));
        };
    }
}
