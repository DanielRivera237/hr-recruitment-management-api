package com.uca.rrhhbackend.service.impl;

import com.uca.rrhhbackend.entity.User;
import com.uca.rrhhbackend.exception.BusinessException;
import com.uca.rrhhbackend.exception.ResourceNotFoundException;
import com.uca.rrhhbackend.repository.UserRepository;
import com.uca.rrhhbackend.service.CurrentUserService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

@Service
public class CurrentUserServiceImpl implements CurrentUserService {

    private final UserRepository userRepository;

    public CurrentUserServiceImpl(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    @Override
    public User getCurrentUser(HttpServletRequest request) {

        String userIdHeader = request.getHeader("X-User-Id");

        if (userIdHeader == null || userIdHeader.isBlank()) {
            throw new BusinessException(
                    "El header X-User-Id es obligatorio y no puede estar vacío"
            );
        }

        Long userId = parseUserId(userIdHeader);

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("No se encontró el usuario con id " + userId)
                );

        if (!Boolean.TRUE.equals(user.getActive())) {
            throw new BusinessException("El usuario no está activo");
        }

        if (Boolean.TRUE.equals(user.getBlocked())) {
            throw new BusinessException("El usuario está bloqueado");
        }

        return user;
    }

    @Override
    public void requireCandidate(User user) {

        if (user.getRole() == null || user.getRole().getName() == null) {
            throw new BusinessException("El usuario no tiene rol asignado");
        }

        String normalizedRole = user.getRole()
                .getName()
                .trim()
                .toUpperCase();

        if (!"CANDIDATE".equals(normalizedRole)) {
            throw new BusinessException("Solo los candidatos pueden realizar esta acción");
        }
    }

    private Long parseUserId(String value) {

        try {
            return Long.parseLong(value.trim());
        } catch (NumberFormatException exception) {
            throw new BusinessException("El header X-User-Id debe ser un número válido");
        }
    }

}