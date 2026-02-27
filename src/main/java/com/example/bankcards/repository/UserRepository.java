package com.example.bankcards.repository;

import com.example.bankcards.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long>, JpaSpecificationExecutor<UserEntity> {

    boolean existsByUsername(String username);
    Optional<UserEntity> findByUsername(String username);
}
