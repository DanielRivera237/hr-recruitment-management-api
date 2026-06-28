package com.uca.rrhhbackend.service.impl;

import com.uca.rrhhbackend.dto.request.LoginRequest;
import com.uca.rrhhbackend.dto.request.RegisterRequest;
import com.uca.rrhhbackend.dto.response.AuthResponse;
import com.uca.rrhhbackend.entity.Role;
import com.uca.rrhhbackend.entity.User;
import com.uca.rrhhbackend.exception.BusinessException;
import com.uca.rrhhbackend.exception.ConflictException;
import com.uca.rrhhbackend.exception.ResourceNotFoundException;
import com.uca.rrhhbackend.repository.RoleRepository;
import com.uca.rrhhbackend.repository.UserRepository;
import com.uca.rrhhbackend.security.JwtService;
import com.uca.rrhhbackend.service.AuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

    private static final String DEFAULT_ROLE = "CANDIDATE";

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Value("${security.jwt.expiration-time}")
    private Long expirationTime;

    public AuthServiceImpl(
            AuthenticationManager authenticationManager,
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        String email = request.email().trim().toLowerCase();

        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new ConflictException(
                    "Ya existe un usuario registrado con ese correo"
            );
        }

        if (!request.password().equals(request.confirmPassword())) {
            throw new BusinessException("Las contraseñas no coinciden");
        }

        Role candidateRole = roleRepository
                .findByNameIgnoreCase(DEFAULT_ROLE)
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName(DEFAULT_ROLE);
                    role.setActive(true);

                    return roleRepository.save(role);
                });

        if (!Boolean.TRUE.equals(candidateRole.getActive())) {
            throw new BusinessException(
                    "El rol de candidato no está activo"
            );
        }

        User user = new User();
        user.setName(request.name().trim());
        user.setSurname(request.surname().trim());
        user.setEmail(email);
        user.setPassword(
                passwordEncoder.encode(request.password())
        );
        user.setActive(true);
        user.setBlocked(false);
        user.setRole(candidateRole);

        User savedUser = userRepository.save(user);

        return buildAuthResponse(savedUser);
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
                        new ResourceNotFoundException(
                                "Usuario no encontrado"
                        )
                );

        return buildAuthResponse(user);
    }

    private AuthResponse buildAuthResponse(User user) {
        UserDetails userDetails =
                org.springframework.security.core.userdetails.User
                        .withUsername(user.getEmail())
                        .password(user.getPassword())
                        .authorities(
                                "ROLE_" +
                                        user.getRole()
                                                .getName()
                                                .toUpperCase()
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