package com.uca.rrhhbackend.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record TechnicalTestRequest(

        @NotBlank(message = "El título es obligatorio")
        @Size(max = 150)
        String title,

        @NotBlank(message = "El enlace externo es obligatorio")
        @Size(max = 500)
        String externalUrl,

        @NotNull(message = "La fecha límite es obligatoria")
        @Future(message = "La fecha límite debe ser futura")
        LocalDateTime deadline,

        @NotNull(message = "La postulación es obligatoria")
        Long applicationId
) {
}