package com.uca.rrhhbackend.dto.response;

import java.time.LocalDateTime;

public record NotificationResponse(
        Long id,
        String title,
        String message,
        Boolean read,
        LocalDateTime createdAt,
        Long userId
) {
}