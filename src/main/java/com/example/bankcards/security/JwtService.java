package com.example.bankcards.security;

import static java.time.Instant.now;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.joining;
import static org.springframework.security.oauth2.jose.jws.MacAlgorithm.HS256;
import static org.springframework.security.oauth2.jwt.JwsHeader.with;
import static org.springframework.security.oauth2.jwt.JwtClaimsSet.builder;
import static org.springframework.security.oauth2.jwt.JwtEncoderParameters.from;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Objects;

@Service
public class JwtService {

    private static final String ROLES = "roles";
    private static final String ROLE_PREFIX = "ROLE_";
    private static final String SPACE = " ";
    private static final String BANKCARDS = "bankcards";
    private final JwtEncoder jwtEncoder;
    private final Long expirationTime;

    public JwtService(final JwtEncoder jwtEncoder, @Value("${app.auth.expiration}") final Long expirationTime) {
        this.jwtEncoder = jwtEncoder;
        this.expirationTime = expirationTime;
    }

    public String generateToken(final Authentication auth) {
        if (isNull(auth)) {
            return null;
        }
        final Instant now = now();
        final String roles = auth.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .filter(Objects::nonNull)
            .filter(authority -> authority.startsWith(ROLE_PREFIX))
            .collect(joining(SPACE));
        final JwtClaimsSet claims = builder()
            .issuer(BANKCARDS)
            .issuedAt(now)
            .expiresAt(now.plusSeconds(expirationTime))
            .subject(auth.getName())
            .claim(ROLES, roles)
            .build();
        final JwsHeader jwsHeader = with(HS256).build();
        return jwtEncoder.encode(from(jwsHeader, claims)).getTokenValue();
    }
}
