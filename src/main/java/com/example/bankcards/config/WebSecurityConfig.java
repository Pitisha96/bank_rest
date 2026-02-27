package com.example.bankcards.config;

import static com.example.bankcards.util.SecretEncoder.encodeBase64;
import static com.nimbusds.jose.JWSAlgorithm.HS256;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
import static org.springframework.security.oauth2.jwt.NimbusJwtDecoder.withSecretKey;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.OctetSequenceKey.Builder;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Order(1)
    @Bean
    public SecurityFilterChain authSecurityFilterChain(HttpSecurity http) {
        http
            .securityMatcher("/api/v1/auth/**")
            .csrf(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .sessionManagement(sessionManagement -> sessionManagement
                .sessionCreationPolicy(STATELESS)
            )
            .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                .requestMatchers("/api/v1/auth/**").permitAll()
                .anyRequest().authenticated()
            );
        return http.build();
    }

    @Order(2)
    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http, final JwtAuthenticationConverter converter) {
        http
            .securityMatcher(request -> !request.getRequestURI().startsWith("/api/v1/auth/"))
            .csrf(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .sessionManagement(sessionManagement -> sessionManagement
                .sessionCreationPolicy(STATELESS)
            )
            .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth -> oauth
                .jwt(jwt -> jwt.jwtAuthenticationConverter(converter))
            );
        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder(@Value("${app.auth.secret}") final char[] secret) {
        final SecretKeySpec key = new SecretKeySpec(encodeBase64(secret), "HmacSHA256");
        return withSecretKey(key).build();
    }

    @Bean
    public JwtEncoder jwtEncoder(@Value("${app.auth.secret}") final char[] secret) {
        final JWK jwk = new Builder(encodeBase64(secret))
            .algorithm(HS256)
            .build();
        return new NimbusJwtEncoder(new ImmutableJWKSet<>(new JWKSet(jwk)));
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        final var authConverter = new JwtGrantedAuthoritiesConverter();
        authConverter.setAuthorityPrefix(EMPTY);
        authConverter.setAuthoritiesClaimName("roles");
        final var converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(authConverter);
        return converter;
    }

    @Bean
    public AuthenticationManager authenticationManager(final AuthenticationConfiguration authConfig) {
        return authConfig.getAuthenticationManager();
    }
}
