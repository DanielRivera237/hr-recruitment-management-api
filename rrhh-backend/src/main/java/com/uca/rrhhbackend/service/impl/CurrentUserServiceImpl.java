package com.uca.rrhhbackend.service.impl;

import com.uca.rrhhbackend.entity.User;
import com.uca.rrhhbackend.exception.BusinessException;
import com.uca.rrhhbackend.exception.ResourceNotFoundException;
import com.uca.rrhhbackend.repository.UserRepository;
import com.uca.rrhhbackend.service.CurrentUserService;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Service
@Transactional(readOnly = true)
public class CurrentUserServiceImpl implements CurrentUserService {

    private final UserRepository userRepository;

    public CurrentUserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {

            throw new BusinessException(
                    "No existe un usuario autenticado"
            );
        }

        String email = authentication.getName();

        if (email == null || email.isBlank()) {
            throw new BusinessException(
                    "No se pudo obtener el correo del usuario autenticado"
            );
        }

        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No se encontró el usuario autenticado"
                        )
                );

        validateUserStatus(user);

        return user;
    }

    @Override
    public void requireCandidate(User user) {
        requireAnyRole(user, "CANDIDATE");
    }

    @Override
    public void requireRecruiter(User user) {
        requireAnyRole(user, "RECRUITER");
    }

    @Override
    public void requireAdmin(User user) {
        requireAnyRole(user, "ADMIN");
    }

    @Override
    public void requireAnyRole(User user, String... allowedRoles) {

        if (user == null) {
            throw new BusinessException(
                    "No se proporcionó un usuario válido"
            );
        }

        if (user.getRole() == null
                || user.getRole().getName() == null
                || user.getRole().getName().isBlank()) {

            throw new BusinessException(
                    "El usuario no tiene un rol asignado"
            );
        }

        String currentRole = user.getRole()
                .getName()
                .trim()
                .toUpperCase();

        boolean roleAllowed = Arrays.stream(allowedRoles)
                .filter(role -> role != null && !role.isBlank())
                .map(role -> role.trim().toUpperCase())
                .anyMatch(currentRole::equals);

        if (!roleAllowed) {
            throw new BusinessException(
                    "El usuario no tiene permisos para realizar esta acción"
            );
        }
    }

    private void validateUserStatus(User user) {

        if (!Boolean.TRUE.equals(user.getActive())) {
            throw new BusinessException(
                    "El usuario no está activo"
            );
        }

        if (Boolean.TRUE.equals(user.getBlocked())) {
            throw new BusinessException(
                    "El usuario está bloqueado"
            );
        }
    }
}