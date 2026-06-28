package com.uca.rrhhbackend.service.impl;

import com.uca.rrhhbackend.dto.request.LoginRequest;
import com.uca.rrhhbackend.dto.response.AuthResponse;
import com.uca.rrhhbackend.entity.User;
import com.uca.rrhhbackend.exception.ResourceNotFoundException;
import com.uca.rrhhbackend.repository.UserRepository;
import com.uca.rrhhbackend.security.JwtService;
import com.uca.rrhhbackend.service.AuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Value("${security.jwt.expiration-time}")
    private Long expirationTime;

    public AuthServiceImpl(
            AuthenticationManager authenticationManager,
            UserRepository userRepository,
            JwtService jwtService
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        User user = userRepository
                .findByEmailIgnoreCase(request.email())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Usuario no encontrado")
                );

        UserDetails userDetails =
                org.springframework.security.core.userdetails.User
                        .withUsername(user.getEmail())
                        .password(user.getPassword())
                        .authorities(
                                "ROLE_" + user.getRole().getName().toUpperCase()
                        )
                        .build();

        String token = jwtService.generateToken(
                userDetails,
                user.getRole().getName()
        );

        return new AuthResponse(
                token,
                "Bearer",
                expirationTime,
                user.getEmail(),
                user.getRole().getName()
        );
    }
}