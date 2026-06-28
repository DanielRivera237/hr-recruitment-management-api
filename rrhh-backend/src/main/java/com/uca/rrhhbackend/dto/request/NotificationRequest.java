package com.uca.rrhhbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record NotificationRequest(

        @NotBlank(message = "El título es obligatorio")
        @Size(max = 150)
        String title,

        @NotBlank(message = "El mensaje es obligatorio")
        @Size(max = 2000)
        String message,

        @NotNull(message = "El usuario es obligatorio")
        Long userId
) {
}