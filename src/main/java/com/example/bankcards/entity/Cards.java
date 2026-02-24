package com.example.bankcards.entity;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static java.math.BigDecimal.ZERO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "cards")
@NoArgsConstructor
@Setter
@Getter
public class Cards {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Pattern(regexp = "^[0-9]{16,20}$", message = "Card number must be 16-20 digits number")
    @Column(nullable = false, unique = true, length = 20)
    private String number;

    @Column(nullable = false)
    private Long ownerId;

    @Column(nullable = false)
    private LocalDate expiryDate;

    @Enumerated(STRING)
    @Column(nullable = false, length = 10)
    private Status status;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance = ZERO;
}
