package com.example.bankcards.security;

import static java.util.Collections.singletonList;

import com.example.bankcards.entity.UserEntity;
import com.example.bankcards.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private static final String USER_NOT_FOUND_MESSAGE = "User not found";

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final UserEntity user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND_MESSAGE));
        return new User(
            user.getUsername(),
            user.getPassword(),
            singletonList(new SimpleGrantedAuthority(user.getRole().getName()))
        );
    }
}
