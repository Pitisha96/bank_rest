package com.example.bankcards.entity;

import static jakarta.persistence.GenerationType.IDENTITY;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "roles")
@NoArgsConstructor
@Setter
@Getter
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Pattern(regexp = "^ROLE_.+", message = "role name must starts with ROLE_")
    @Column(nullable = false, unique = true, length = 20)
    private String name;
}
