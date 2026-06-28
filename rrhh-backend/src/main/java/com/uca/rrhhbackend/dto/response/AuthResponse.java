package com.uca.rrhhbackend.dto.response;

public record AuthResponse(
        String token,
        String tokenType,
        Long expiresIn,
        String email,
        String role
) {
}
